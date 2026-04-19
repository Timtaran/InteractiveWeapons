/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns.init;

import net.timtaran.interactiveguns.init.registry.BodyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InteractiveGuns {
    public static final String MOD_ID = "interactiveguns";
    public static final String MOD_NAME = "Interactive Guns";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void onInit() {
        LOGGER.info("Initializing Interactive Guns");
        BodyRegistry.register();
    }

    public static void onClientInit() {
        LOGGER.info("Initializing Interactive Guns Client");
        BodyRegistry.registerClient();
    }
}
