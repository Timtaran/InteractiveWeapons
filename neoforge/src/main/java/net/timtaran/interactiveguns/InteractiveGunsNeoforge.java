/*
 * This file is part of Interactive Guns.
 * Licensed under GPL 3.0.
 */
package net.timtaran.interactiveguns;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.timtaran.interactiveguns.init.InteractiveGuns;

@Mod(InteractiveGuns.MOD_ID)
public class InteractiveGunsNeoforge {
    public InteractiveGunsNeoforge(IEventBus eventBus) {
        InteractiveGuns.onInit();
    }
}
