/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns.gun.deagle;

import com.github.stephengold.joltjni.Quat;
import com.github.stephengold.joltjni.RVec3;
import com.github.stephengold.joltjni.enumerate.EActivation;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleBolt;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleChamber;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleFrame;
import net.timtaran.interactiveguns.init.registry.BodyRegistry;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.VxBodyType;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;
import net.xmx.velthoric.math.VxTransform;

import java.util.UUID;

public class Deagle {
    /**
     * Creates pistol physical bodies.
     *
     * @return VxBody of pistol frame
     */
    public static VxBody create(VxBodyType type, VxPhysicsWorld world, UUID id) {
        DeagleFrame frame = new DeagleFrame(type, world, id);

        VxTransform transform = new VxTransform(
                new RVec3(0f, 0f, 0f),
                new Quat()
        );
        DeagleChamber chamber = (DeagleChamber) world.getBodyManager().createBody(
                BodyRegistry.DEAGLE_CHAMBER,
                transform,
                EActivation.Activate,
                body -> {
                    body.setServerData(DeagleChamber.CARTRIDGE_AMOUNT, 0);
                }
        );

        DeagleBolt bolt = (DeagleBolt) world.getBodyManager().createBody(
                BodyRegistry.DEAGLE_BOLT,
                transform,
                EActivation.Activate,
                body -> {
                }
        );

        return frame;
    }
}
