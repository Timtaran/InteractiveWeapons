/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns.init.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.timtaran.interactiveguns.gun.deagle.Deagle;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleFrame;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleMagazine;
import net.timtaran.interactiveguns.gun.deagle.body.DeagleSlide;
import net.timtaran.interactiveguns.gun.deagle.body.client.DeagleFrameRenderer;
import net.timtaran.interactiveguns.gun.deagle.body.client.DeagleMagazineRenderer;
import net.timtaran.interactiveguns.gun.deagle.body.client.DeagleSlideRenderer;
import net.timtaran.interactiveguns.util.InteractiveGunsIdentifier;
import net.xmx.velthoric.core.body.VxBodyType;
import net.xmx.velthoric.core.body.registry.VxBodyRegistry;
import net.xmx.velthoric.core.network.internal.behavior.VxNetSyncBehavior;
import net.xmx.velthoric.core.network.synchronization.behavior.VxSyncBehavior;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;

import java.util.UUID;

public class BodyRegistry {
    /**
     * Desert eagle frame (main part).
     * <p>
     * Creates physical bodies using
     * {@link Deagle#create(VxBodyType, VxPhysicsWorld, UUID)} which creates another parts of weapon.
     */
    public static final VxBodyType<DeagleFrame> DEAGLE_FRAME = VxBodyType.Builder
            .create(Deagle::create)
            .rigidProvider(DeagleFrame::createJoltBody)
            .behavior(VxNetSyncBehavior.ID)
            .behavior(VxSyncBehavior.ID)
            .setPersistent(true)
            .build(InteractiveGunsIdentifier.get("deagle_frame"));

    public static final VxBodyType<DeagleMagazine> DEAGLE_MAGAZINE = VxBodyType.Builder
            .<DeagleMagazine>create(DeagleMagazine::new)
            .rigidProvider(DeagleMagazine::createJoltBody)
            .behavior(VxNetSyncBehavior.ID)
            .behavior(VxSyncBehavior.ID)
            .persistence(DeagleMagazine::writePersistenceData, DeagleMagazine::readPersistenceData)
            .setPersistent(true)
            .build(InteractiveGunsIdentifier.get("deagle_magazine"));

    public static final VxBodyType<DeagleSlide> DEAGLE_SLIDE = VxBodyType.Builder
            .<DeagleSlide>create(DeagleSlide::new)
            .rigidProvider(DeagleSlide::createJoltBody)
            .behavior(VxNetSyncBehavior.ID)
            .behavior(VxSyncBehavior.ID)
            .setPersistent(true)
            .build(InteractiveGunsIdentifier.get("deagle_slide"));

    /**
     * Registers all body types on the server side.
     */
    public static void register() {
        VxBodyRegistry.getInstance().register(DEAGLE_FRAME);
        VxBodyRegistry.getInstance().register(DEAGLE_MAGAZINE);
        VxBodyRegistry.getInstance().register(DEAGLE_SLIDE);
    }

    /**
     * Registers client-side factories and renderers for body types.
     */
    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        var registry = VxBodyRegistry.getInstance();

        // Client-side factory registration
        registry.registerClientFactory(DEAGLE_FRAME.getTypeId(), DeagleFrame::new);
        registry.registerClientFactory(DEAGLE_MAGAZINE.getTypeId(), DeagleFrame::new);
        registry.registerClientFactory(DEAGLE_SLIDE.getTypeId(), DeagleFrame::new);

        // Client-side renderer registration
        registry.registerClientRenderer(DEAGLE_FRAME.getTypeId(), new DeagleFrameRenderer());
        registry.registerClientRenderer(DEAGLE_MAGAZINE.getTypeId(), new DeagleMagazineRenderer());
        registry.registerClientRenderer(DEAGLE_SLIDE.getTypeId(), new DeagleSlideRenderer());
    }
}
