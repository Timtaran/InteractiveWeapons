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
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DeagleSlide extends VxBody implements IGrabbable {
    public boolean grabAllowed = false;

    public DeagleSlide(VxBodyType type, VxPhysicsWorld physicsWorld, UUID id) {
        super(type, physicsWorld, id);
    }

    public DeagleSlide(VxBodyType type, UUID id) {
        super(type, id);
    }

    public static int createJoltBody(VxBody body, VxRigidBodyFactory factory) {
        VxRotatedTranslatedShape shape = new VxRotatedTranslatedShape(
                new Vec3(0f, 0.05f, 0f),
                new Quat(),
                new VxBoxShape(new Vec3(0.01875f, 0.02f, 0.125f))
        );

        try (BodyCreationSettings bcs = new BodyCreationSettings()) {
            bcs.setMotionType(EMotionType.Dynamic);
            bcs.setObjectLayer(PhysicsLayerRegistry.getGhostLayer());
            System.out.println(PhysicsLayerRegistry.getGhostLayer());

            MassProperties massProperties = bcs.getMassPropertiesOverride();
            massProperties.scaleToMass(0.6f);
            bcs.setMassPropertiesOverride(massProperties);

            return factory.create(shape, bcs);
        }
    }

    @Override
    public @Nullable GrabPoint getGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint, QuatArg rotationDifference) {
        if (grabAllowed) {
            return new GrabPoint(intersectionPoint, rotationDifference);
        }

        return null;
    }

    @Override
    public @Nullable GrabPoint getRemoteGrabPoint(Player player, PlayerBodyPart bodyPart, RVec3Arg intersectionPoint) {
        return null;
    }
}
