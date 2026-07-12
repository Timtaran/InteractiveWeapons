/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns.gun.deagle.body;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.readonly.QuatArg;
import com.github.stephengold.joltjni.readonly.RVec3Arg;
import net.minecraft.world.entity.player.Player;
import net.timtaran.interactivemc.body.player.PlayerBodyPart;
import net.timtaran.interactivemc.body.type.GrabPoint;
import net.timtaran.interactivemc.body.type.IGrabbable;
import net.timtaran.interactivemc.init.registry.PhysicsLayerRegistry;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.VxBodyType;
import net.xmx.velthoric.core.body.factory.VxRigidBodyFactory;
import net.xmx.velthoric.core.body.shape.VxBoxShape;
import net.xmx.velthoric.core.body.shape.VxRotatedTranslatedShape;
import net.xmx.velthoric.core.network.synchronization.VxDataSerializers;
import net.xmx.velthoric.core.network.synchronization.VxSynchronizedData;
import net.xmx.velthoric.core.network.synchronization.accessor.VxServerAccessor;
import net.xmx.velthoric.core.physics.VxJoltBridge;
import net.xmx.velthoric.core.physics.VxPhysicsLayers;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;
import net.xmx.velthoric.network.VxByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DeagleMagazine extends VxBody implements IGrabbable {
    public static final VxServerAccessor<Boolean> IS_INSERTED = VxServerAccessor.create(DeagleMagazine.class, VxDataSerializers.BOOLEAN);
    public static final VxServerAccessor<Integer> CARTRIDGE_AMOUNT = VxServerAccessor.create(DeagleMagazine.class, VxDataSerializers.INTEGER);

    public static final int DEFAULT_CARTRIDGE_AMOUNT = 7;

    public DeagleMagazine(VxBodyType type, VxPhysicsWorld physicsWorld, UUID id) {
        super(type, physicsWorld, id);
    }

    public DeagleMagazine(VxBodyType type, UUID id) {
        super(type, id);
    }

    public static int createJoltBody(VxBody body, VxRigidBodyFactory factory) {
        VxRotatedTranslatedShape shape = new VxRotatedTranslatedShape(
                new Vec3(0f, -0.03f, 0.07f),
                new Quat(-0.087156f, 0f, 0f, 0.996195f),
                new VxBoxShape(new Vec3(0.01875f, 0.06, 0.025))
        );

        try (BodyCreationSettings bcs = new BodyCreationSettings()) {
            bcs.setMotionType(EMotionType.Dynamic);
            bcs.setObjectLayer(PhysicsLayerRegistry.getGhostLayer());
            System.out.println(PhysicsLayerRegistry.getGhostLayer());

            MassProperties massProperties = bcs.getMassPropertiesOverride();
            massProperties.scaleToMass(0.3f);
            bcs.setMassPropertiesOverride(massProperties);

            return factory.create(shape, bcs);
        }
    }

    public static void writePersistenceData(VxBody body, VxByteBuf buf) {
        VxDataSerializers.BOOLEAN.write(buf, body.get(IS_INSERTED));
        VxDataSerializers.INTEGER.write(buf, body.get(CARTRIDGE_AMOUNT));
    }

    public static void readPersistenceData(VxBody body, VxByteBuf buf) {
        body.setServerData(IS_INSERTED, VxDataSerializers.BOOLEAN.read(buf));
        body.setServerData(CARTRIDGE_AMOUNT, VxDataSerializers.INTEGER.read(buf));
    }

    public static void defineSyncData(VxSynchronizedData.Builder builder) {
        builder.define(IS_INSERTED, false);
        builder.define(CARTRIDGE_AMOUNT, DEFAULT_CARTRIDGE_AMOUNT);
    }

    @Override
    public @Nullable GrabPoint getGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint, QuatArg rotationDifference) {
        if (!get(IS_INSERTED)) {
            return new GrabPoint(intersectionPoint, rotationDifference);
        }

        return null;
    }

    @Override
    public @Nullable GrabPoint getRemoteGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint) {
        if (!get(IS_INSERTED)) {
            return new GrabPoint(intersectionPoint, new Quat());
        }

        return null;
    }

    public void decreaseCartridgeAmount(int amount) {
        setServerData(CARTRIDGE_AMOUNT, Math.max(0, get(CARTRIDGE_AMOUNT) - amount));
    }
}
