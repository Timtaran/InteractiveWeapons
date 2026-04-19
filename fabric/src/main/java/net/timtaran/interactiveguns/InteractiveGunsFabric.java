/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns;

import net.fabricmc.api.ModInitializer;
import net.timtaran.interactiveguns.init.InteractiveGuns;

public class InteractiveGunsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        InteractiveGuns.onInit();
    }
}
