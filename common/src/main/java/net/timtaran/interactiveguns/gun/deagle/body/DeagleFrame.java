package net.timtaran.interactiveguns.gun.deagle.body;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.operator.Op;
import com.github.stephengold.joltjni.readonly.QuatArg;
import com.github.stephengold.joltjni.readonly.RVec3Arg;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.timtaran.interactivemc.body.player.PlayerBodyPart;
import net.timtaran.interactivemc.body.player.interaction.TriggerState;
import net.timtaran.interactivemc.body.type.GrabPoint;
import net.timtaran.interactivemc.body.type.IGrabbable;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.VxBodyType;
import net.xmx.velthoric.core.body.factory.VxRigidBodyFactory;
import net.xmx.velthoric.core.physics.VxJoltBridge;
import net.xmx.velthoric.core.physics.VxPhysicsLayers;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;
import net.xmx.velthoric.math.VxConversions;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DeagleFrame extends VxBody implements IGrabbable {
    private static final GrabPoint GRAB_POINT = new GrabPoint(new RVec3(0f, -0.046875f, -0.078125f), new Quat());
    private static final float BULLET_DISTANCE = 40f;

    public DeagleFrame(VxBodyType type, VxPhysicsWorld physicsWorld, UUID id) {
        super(type, physicsWorld, id);
    }

    @Environment(EnvType.CLIENT)
    public DeagleFrame(VxBodyType type, UUID id) {
        super(type, id);
    }


    public static int createJoltBody(VxBody body, VxRigidBodyFactory factory) {
        Vec3 fullSize = new Vec3(0.0375f, 0.1625f, 0.275f);

        try (ShapeSettings shapeSettings = new BoxShapeSettings(new Vec3(fullSize.getX() / 2, fullSize.getY() / 2, fullSize.getZ() / 2)); BodyCreationSettings bcs = new BodyCreationSettings()) {
            bcs.setMotionType(EMotionType.Dynamic);
            bcs.setObjectLayer(VxPhysicsLayers.MOVING);

            MassProperties massProperties = bcs.getMassPropertiesOverride();

            massProperties.scaleToMass(1.15f);
            bcs.setMassPropertiesOverride(massProperties);
            return factory.create(shapeSettings, bcs);
        }
    }

    @Override
    public @Nullable GrabPoint getGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint, QuatArg rotationDifference) {
        return GRAB_POINT;
    }

    @Override
    public @Nullable GrabPoint getRemoteGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint) {
        return GRAB_POINT;
    }

    @Override
    public void onTriggerStateUpdate(Player player, PlayerBodyPart bodyPart, TriggerState triggerState) {
        if (triggerState != TriggerState.PRESS)
            return;

        Level level = physicsWorld.getLevel();

        Body joltBody = VxJoltBridge.INSTANCE.getJoltBody(physicsWorld, this);

        Vec3 direction = Op.star(joltBody.getRotation(), new Vec3(0d, 0d, 1d));

        System.out.println(direction);
        System.out.println(Op.star(new Vec3(0d, 0d, 1d), joltBody.getRotation()));


        net.minecraft.world.phys.Vec3 start = VxConversions.toMinecraft(joltBody.getPosition());
        net.minecraft.world.phys.Vec3 end = start.add(
                -direction.getX() * BULLET_DISTANCE,
                -direction.getY() * BULLET_DISTANCE,
                -direction.getZ() * BULLET_DISTANCE
        );

        AABB searchBox = new AABB(start, end).inflate(1.0d);

        level.getServer().execute(() -> {
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                    level,
                    player,
                    start,
                    end,
                    searchBox,
                    entity -> entity != player && entity.isPickable()
            );

            if (entityHitResult == null)
                return;

            entityHitResult.getEntity().hurt(new DamageSources(level.registryAccess()).playerAttack(player), 10f);

            physicsWorld.execute(() -> {
                float linearStrength = 855f;
                Vec3 linearImpulse = Op.star(direction, linearStrength);
                System.out.println(linearImpulse);
                joltBody.addImpulse(linearImpulse);

                Vec3 up = new Vec3(0, 1, 0);
                Vec3 right = direction.cross(up).normalized();

                float angularStrength = 350f;
                Vec3 angularImpulse = Op.star(right, angularStrength);
                System.out.println(angularImpulse);
                joltBody.addAngularImpulse(angularImpulse);
            });
        });
    }

    @Override
    public void onRelease(Player player, PlayerBodyPart bodyPart, boolean isAttached) {
    }

    @Override
    public boolean canRelease(Player player, PlayerBodyPart bodyPart) {
        return true;
    }

}
