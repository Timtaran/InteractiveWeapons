/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns.gun.deagle;

import com.github.stephengold.joltjni.enumerate.EActivation;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleFrame;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleMagazine;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleSlide;
import net.timtaran.interactiveguns.init.registry.BodyRegistry;
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
    public static DeagleFrame create(VxBodyType<DeagleFrame> type, VxPhysicsWorld world, UUID id) {
        DeagleFrame frame = new DeagleFrame(type, world, id);

        world.execute(() -> {
            VxTransform transform = frame.getTransform();

            DeagleMagazine magazine = world.getBodyManager().createBody(
                    BodyRegistry.DEAGLE_MAGAZINE,
                    transform,
                    EMotionType.Dynamic,
                    EActivation.Activate,
                    body -> {
                    }
            );

            DeagleSlide slide = world.getBodyManager().createBody(
                    BodyRegistry.DEAGLE_SLIDE,
                    transform,
                    EMotionType.Dynamic,
                    EActivation.Activate,
                    body -> {
                    }
            );
        });

        return frame;

    }
}
