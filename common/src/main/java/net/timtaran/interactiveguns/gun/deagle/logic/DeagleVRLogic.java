/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns.gun.deagle.logic;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.operator.Op;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleFrame;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleMagazine;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleSlide;
import net.timtaran.interactivemc.body.player.store.PlayerBodyDataStore;
import net.timtaran.interactivemc.init.registry.PhysicsLayerRegistry;
import net.xmx.velthoric.core.constraint.VxConstraint;
import net.xmx.velthoric.core.intersection.raycast.VxHitResult;
import net.xmx.velthoric.core.intersection.raycast.VxRaycaster;
import net.xmx.velthoric.core.physics.VxJoltBridge;
import net.xmx.velthoric.core.physics.VxPhysicsLayers;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;
import net.xmx.velthoric.math.VxConversions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controls the VR-specific logic for the Deagle gun.
 */
public class DeagleVRLogic {
    private static final float BULLET_DISTANCE = 40f;

    private VxPhysicsWorld physicsWorld;
    private final DeagleFrame frame;
    private final DeagleMagazine magazine;
    private final DeagleSlide slide;

    private boolean isSlideRetracted = false;

    public DeagleVRLogic(VxPhysicsWorld physicsWorld, DeagleFrame frame, DeagleSlide slide, DeagleMagazine magazine) {
        this.physicsWorld = physicsWorld;
        this.frame = frame;
        this.slide = slide;
        this.magazine = magazine;
    }

    public void fire(Player player) {
        if (!frame.get(DeagleFrame.IS_SLIDE_RACKED) || isSlideRetracted || magazine.get(DeagleMagazine.CARTRIDGE_AMOUNT) == 0)
            return;

        Level level = physicsWorld.getLevel();

        Body joltBody = VxJoltBridge.INSTANCE.getJoltBody(physicsWorld, frame);

        Vec3 direction = Op.star(joltBody.getRotation(), new Vec3(0d, 0d, 1d));
        direction.scaleInPlace(-1f); // Invert the direction to point forward

        float linearStrength = 555f;
        Vec3 linearImpulse = Op.star(direction, linearStrength);
        linearImpulse.scaleInPlace(-1f);
        joltBody.addImpulse(linearImpulse);

        // todo: also retract the slide

        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = direction.cross(up).normalized();

        float angularStrength = 355f;
        Vec3 angularImpulse = Op.star(right, angularStrength);
        joltBody.addAngularImpulse(angularImpulse);

        RVec3 bodyPosition = joltBody.getPosition();

        magazine.decreaseCartridgeAmount(1);

        physicsWorld.execute(() -> {
            List<VxHitResult> physicsResult = VxRaycaster.raycastAll(physicsWorld, bodyPosition, direction, BULLET_DISTANCE, new ObjectLayerFilter() {
                @Override
                public boolean shouldCollide(int objectLayer) {
                    return objectLayer != VxPhysicsLayers.TERRAIN && objectLayer != PhysicsLayerRegistry.getGhostLayer();
                }
            });

            AtomicBoolean physicsSuccess = new AtomicBoolean(false);
            AtomicReference<Double> physicsDistance = new AtomicReference<>(BULLET_DISTANCE * BULLET_DISTANCE + 1d);
            AtomicReference<VxHitResult.PhysicsHit> finalPhysicsHit = new AtomicReference<>(null);

            for (VxHitResult hit : physicsResult) {
                Optional<VxHitResult.PhysicsHit> optionalHit = hit.getPhysicsHit();
                if (optionalHit.isEmpty()) {
                    continue;
                }

                VxHitResult.PhysicsHit physicsHit = optionalHit.get();

                if (PlayerBodyDataStore.isPlayerControlledBody(player.getUUID(), physicsHit.bodyId()) || physicsHit.bodyId() == frame.getBodyId() || physicsHit.bodyId() == magazine.getBodyId() || physicsHit.bodyId() == slide.getBodyId() || physicsWorld.getBodyManager().getByJoltBodyId(physicsHit.bodyId()) == null)
                    continue;

                double hitDistance = Op.minus(hit.getPhysicsHit().get().position(), bodyPosition).lengthSq();
                if (hitDistance < physicsDistance.get()) {
                    physicsSuccess.set(true);
                    physicsDistance.set(hitDistance);
                    finalPhysicsHit.set(physicsHit);
                }
            }

            level.getServer().execute(() -> {
                net.minecraft.world.phys.Vec3 start = VxConversions.toMinecraft(bodyPosition);
                net.minecraft.world.phys.Vec3 end = start.add(
                        direction.getX() * BULLET_DISTANCE,
                        direction.getY() * BULLET_DISTANCE,
                        direction.getZ() * BULLET_DISTANCE
                );

                AABB searchBox = new AABB(start, end).inflate(1.0d);

                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                        level,
                        player,
                        start,
                        end,
                        searchBox,
                        entity -> entity != player && entity.isPickable()
                );

                boolean entitySuccess = entityHitResult != null;
                double entityDistance = entitySuccess ? entityHitResult.getLocation().distanceToSqr(start) : BULLET_DISTANCE * BULLET_DISTANCE + 1d;

                if (!entitySuccess && !physicsSuccess.get()) {
                    return;
                }

                if (entityDistance < physicsDistance.get()) {
                    entityHitResult.getEntity().hurt(new DamageSources(level.registryAccess()).playerAttack(player), 10f);
                } else {
                    physicsWorld.execute(() -> {
                        try (BodyInterface bodyInterface = physicsWorld.getPhysicsSystem().getBodyInterface()) {
                            VxHitResult.PhysicsHit physicsHit = finalPhysicsHit.get();
                            bodyInterface.addImpulse(physicsHit.bodyId(), Op.star(linearImpulse, -1f), physicsHit.position());
                        }
                    });
                }
            });
        });
    }

    /**
     * Called every tick by slide to update it's state.
     */
    public void updateSlideState() {
        SliderConstraint slideConstraint = getSlideConstraint();
        if (slideConstraint != null) {
            if (slideConstraint.getCurrentPosition() < -0.04) {
                if (isSlideRetracted)
                    return;
                slideConstraint.getMotorSettings().getSpringSettings().setFrequency(3f);
                isSlideRetracted = true;
                magazine.decreaseCartridgeAmount(1);
            } else if (isSlideRetracted) {
                slideConstraint.getMotorSettings().getSpringSettings().setFrequency(8f);
                isSlideRetracted = false;
                frame.setServerData(DeagleFrame.IS_SLIDE_RACKED, true);
                slideConstraint.setTargetPosition(0f);
            }
        }
    }

    private SliderConstraint getSlideConstraint() {
        VxConstraint slideConstraint = physicsWorld.getConstraintManager().getActiveConstraint(frame.get(DeagleFrame.SLIDE_CONSTRAINT_ID));

        return slideConstraint == null ? null : (SliderConstraint) slideConstraint.getJoltConstraint();
    }
}
