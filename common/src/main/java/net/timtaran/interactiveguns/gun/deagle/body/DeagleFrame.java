/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns.gun.deagle.body;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EActivation;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.enumerate.EMotorState;
import com.github.stephengold.joltjni.operator.Op;
import com.github.stephengold.joltjni.readonly.QuatArg;
import com.github.stephengold.joltjni.readonly.RVec3Arg;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import net.timtaran.interactiveguns.gun.deagle.logic.DeagleVRLogic;
import net.timtaran.interactiveguns.init.registry.BodyRegistry;
import net.timtaran.interactivemc.body.player.PlayerBodyPart;
import net.timtaran.interactivemc.body.player.interaction.TriggerState;
import net.timtaran.interactivemc.body.type.GrabPoint;
import net.timtaran.interactivemc.body.type.IGrabbable;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.VxBodyType;
import net.xmx.velthoric.core.body.factory.VxRigidBodyFactory;
import net.xmx.velthoric.core.body.shape.VxBoxShape;
import net.xmx.velthoric.core.body.shape.VxRotatedTranslatedShape;
import net.xmx.velthoric.core.body.shape.VxStaticCompoundShape;
import net.xmx.velthoric.core.constraint.VxConstraint;
import net.xmx.velthoric.core.network.synchronization.VxDataSerializers;
import net.xmx.velthoric.core.network.synchronization.VxSynchronizedData;
import net.xmx.velthoric.core.network.synchronization.accessor.VxServerAccessor;
import net.xmx.velthoric.core.physics.VxJoltBridge;
import net.xmx.velthoric.core.physics.VxPhysicsLayers;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;
import net.xmx.velthoric.math.VxTransform;
import net.xmx.velthoric.network.VxByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DeagleFrame extends VxBody implements IGrabbable {
    private static final UUID NULL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static final VxServerAccessor<UUID> MAGAZINE_CONSTRAINT_ID = VxServerAccessor.create(DeagleFrame.class, VxDataSerializers.UUID);
    public static final VxServerAccessor<UUID> MAGAZINE_BODY_ID = VxServerAccessor.create(DeagleFrame.class, VxDataSerializers.UUID);
    public static final VxServerAccessor<UUID> SLIDE_CONSTRAINT_ID = VxServerAccessor.create(DeagleFrame.class, VxDataSerializers.UUID);
    public static final VxServerAccessor<UUID> SLIDE_BODY_ID = VxServerAccessor.create(DeagleFrame.class, VxDataSerializers.UUID);

    /**
     * Indicates if slide was racked after magazine insertion.
     */
    public static final VxServerAccessor<Boolean> IS_SLIDE_RACKED = VxServerAccessor.create(DeagleFrame.class, VxDataSerializers.BOOLEAN);

    private static final GrabPoint GRAB_POINT = new GrabPoint(new RVec3(0f, -0.04f, -0.078125f), new Quat());

    private DeagleMagazine magazine;
    private DeagleSlide slide;

    private boolean isGrabbed;

    private DeagleVRLogic logic;

    public DeagleFrame(VxBodyType type, VxPhysicsWorld physicsWorld, UUID id) {
        super(type, physicsWorld, id);
        isGrabbed = false;

        PhysicsSettings physicsSettings = physicsWorld.getPhysicsSystem().getPhysicsSettings();
        physicsSettings.setBaumgarte(0.6f);
        physicsWorld.getPhysicsSystem().setPhysicsSettings(physicsSettings);
    }

    @Environment(EnvType.CLIENT)
    public DeagleFrame(VxBodyType type, UUID id) {
        super(type, id);
    }


    public static int createJoltBody(VxBody body, VxRigidBodyFactory factory) {
        VxStaticCompoundShape shape = new VxStaticCompoundShape();
        shape.addShape(new VxRotatedTranslatedShape(
                new Vec3(),
                new Quat(-0.087156f, 0f, 0f, 0.996195f),
                new VxBoxShape(new Vec3(0.01875f, 0.05, 0.04))
        ), new Vec3(0f, -0.02f, 0.075f));
        shape.addShape(
                new VxBoxShape(new Vec3(0.01875f, 0.025f, 0.1375f)),
                new Vec3(0f, 0.055f, 0f)
        );
        shape.addShape(
                new VxBoxShape(new Vec3(0.01675f, 0.02f, 0.02f)),
                new Vec3(0f, 0.017f, 0.02f)
        );

        try (BodyCreationSettings bcs = new BodyCreationSettings()) {
            bcs.setMotionType(EMotionType.Dynamic);
            bcs.setObjectLayer(VxPhysicsLayers.MOVING);

            MassProperties massProperties = bcs.getMassPropertiesOverride();

            massProperties.scaleToMass(1.2f);
            bcs.setMassPropertiesOverride(massProperties);
            return factory.create(shape, bcs);
        }
    }

    @Override
    public @Nullable GrabPoint getGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint, QuatArg rotationDifference) {
        return isGrabbed ? null : GRAB_POINT;
    }

    @Override
    public @Nullable GrabPoint getRemoteGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint) {
        return isGrabbed ? null : GRAB_POINT;
    }

    @Override
    public void onTriggerStateUpdate(Player player, PlayerBodyPart bodyPart, TriggerState triggerState) {
        if (triggerState != TriggerState.PRESS)
            return;

        logic.fire(player);
    }

    @Override
    public void onGrab(Player player, VxBody grabberBody, PlayerBodyPart bodyPart, boolean isAttached) {
        physicsWorld.getBodyPairIgnoreHandler().ignorePair(magazine.getBodyId(), grabberBody.getBodyId());
        physicsWorld.getBodyPairIgnoreHandler().ignorePair(slide.getBodyId(), grabberBody.getBodyId());
        isGrabbed = true;
        slide.grabAllowed = true;
    }

    @Override
    public void onRelease(Player player, VxBody grabberBody, PlayerBodyPart bodyPart, boolean isAttached) {
        physicsWorld.getBodyPairIgnoreHandler().removeIgnorePair(magazine.getBodyId(), grabberBody.getBodyId());
        physicsWorld.getBodyPairIgnoreHandler().removeIgnorePair(slide.getBodyId(), grabberBody.getBodyId());
        isGrabbed = false;
        slide.grabAllowed = false;
    }

    @Override
    public boolean canRelease(Player player, PlayerBodyPart bodyPart) {
        return true;
    }

    public static void writePersistenceData(VxBody body, VxByteBuf buf) {
        VxDataSerializers.UUID.write(buf, body.get(MAGAZINE_CONSTRAINT_ID));
        VxDataSerializers.UUID.write(buf, body.get(MAGAZINE_BODY_ID));
        VxDataSerializers.UUID.write(buf, body.get(SLIDE_CONSTRAINT_ID));
        VxDataSerializers.UUID.write(buf, body.get(SLIDE_BODY_ID));

        VxDataSerializers.BOOLEAN.write(buf, body.get(IS_SLIDE_RACKED));
    }

    public static void readPersistenceData(VxBody body, VxByteBuf buf) {
        UUID magazineConstraintId = VxDataSerializers.UUID.read(buf);
        UUID magazineId = VxDataSerializers.UUID.read(buf);
        UUID slideConstraintId = VxDataSerializers.UUID.read(buf);
        UUID slideId = VxDataSerializers.UUID.read(buf);

        boolean isSlideRacked = VxDataSerializers.BOOLEAN.read(buf);

        DeagleFrame frame = (DeagleFrame) body;

        frame.setServerData(MAGAZINE_CONSTRAINT_ID, magazineConstraintId);
        frame.setServerData(MAGAZINE_BODY_ID, magazineId);
        frame.setServerData(SLIDE_CONSTRAINT_ID, slideConstraintId);
        frame.setServerData(SLIDE_BODY_ID, slideId);
        frame.setServerData(IS_SLIDE_RACKED, isSlideRacked);

        body.getPhysicsWorld().execute(() -> {
            frame.magazine = (DeagleMagazine) frame.getPhysicsWorld().getBodyManager().getVxBody(magazineId);
            frame.slide = (DeagleSlide) frame.getPhysicsWorld().getBodyManager().getVxBody(slideId);
        });
    }

    public static void defineSyncData(VxSynchronizedData.Builder builder) {
        builder.define(MAGAZINE_CONSTRAINT_ID, NULL_UUID);
        builder.define(MAGAZINE_BODY_ID, NULL_UUID);
        builder.define(SLIDE_CONSTRAINT_ID, NULL_UUID);
        builder.define(SLIDE_BODY_ID, NULL_UUID);

        builder.define(IS_SLIDE_RACKED, true);
    }

    @Override
    public void onBodyAdded(VxPhysicsWorld world) {
        world.execute(() -> { // onBodyAdded is called after readPersistenceData, so this execute is queued after setting magazine and slide
            Body joltBody = VxJoltBridge.INSTANCE.getJoltBody(world, this);
            VxTransform spawnTransform = new VxTransform(Op.plus(joltBody.getPosition(), new RVec3(0f, 0.011f, 0f)), joltBody.getRotation());

            if (magazine == null) {
                magazine = world.getBodyManager().createBody(
                        BodyRegistry.DEAGLE_MAGAZINE,
                        spawnTransform,
                        EMotionType.Dynamic,
                        EActivation.Activate,
                        body -> {
                            body.setServerData(DeagleMagazine.IS_INSERTED, true);
                        }
                );

                try (FixedConstraintSettings constraintSettings = new FixedConstraintSettings()) {
                    constraintSettings.setAutoDetectPoint(true);
                    VxConstraint constraint = world.getConstraintManager().createConstraint(
                            constraintSettings,
                            magazine.getPhysicsId(),
                            getPhysicsId()
                    );

                    setServerData(MAGAZINE_CONSTRAINT_ID, constraint.getConstraintId());

                    world.execute(() -> {
                        // todo: rework
                        constraint.getJoltConstraint().setConstraintPriority(constraint.getJoltConstraint().getConstraintPriority() + 50000);
                    });
                }

                this.setServerData(MAGAZINE_BODY_ID, magazine.getPhysicsId());
            }

            if (slide == null) {
                slide = world.getBodyManager().createBody(
                        BodyRegistry.DEAGLE_SLIDE,
                        spawnTransform,
                        EMotionType.Dynamic,
                        EActivation.Activate,
                        body -> {
                        }
                );

                try (SliderConstraintSettings constraintSettings = new SliderConstraintSettings()) {
//                    constraintSettings.setSpace(EConstraintSpace.WorldSpace);
//                    constraintSettings.setPosition1(spawnTransform.getTranslation());
//                    constraintSettings.setPosition1(spawnTransform.getTranslation());
                    constraintSettings.setAutoDetectPoint(true);
                    constraintSettings.setSliderAxis(new Vec3(0f, 0f, 1f));
                    constraintSettings.setLimitsMin(-0.05f);
                    constraintSettings.setLimitsMax(0f);

                    constraintSettings.setMotorSettings(new MotorSettings(8f, 1f));

                    VxConstraint constraint = world.getConstraintManager().createConstraint(
                            constraintSettings,
                            slide.getPhysicsId(),
                            getPhysicsId()
                    );

                    setServerData(SLIDE_CONSTRAINT_ID, constraint.getConstraintId());

                    world.execute(() -> {
                        // todo: rework
                        SliderConstraint sliderConstraint = (SliderConstraint) constraint.getJoltConstraint();

                        sliderConstraint.setMotorState(EMotorState.Position);

                        sliderConstraint.setConstraintPriority(constraint.getJoltConstraint().getConstraintPriority() + 50000);
                        sliderConstraint.setNumPositionStepsOverride(10);
                        sliderConstraint.setNumVelocityStepsOverride(50);
                    });
                }

                this.setServerData(SLIDE_BODY_ID, slide.getPhysicsId());
            }

            initLogic();

            world.getBodyPairIgnoreHandler().ignorePair(getBodyId(), magazine.getBodyId());
            world.getBodyPairIgnoreHandler().ignorePair(getBodyId(), slide.getBodyId());
            world.getBodyPairIgnoreHandler().ignorePair(magazine.getBodyId(), slide.getBodyId());
        });
    }

    private void initLogic() {
        logic = new DeagleVRLogic(getPhysicsWorld(), this, slide, magazine);
        slide.setLogic(logic);
    }
}
