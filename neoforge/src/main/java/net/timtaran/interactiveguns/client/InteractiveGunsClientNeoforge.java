package net.timtaran.interactiveguns.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.timtaran.interactiveguns.init.InteractiveGuns;

@Mod(value = InteractiveGuns.MOD_ID, dist = Dist.CLIENT)
public class InteractiveGunsClientNeoforge {
    public InteractiveGunsClientNeoforge(IEventBus eventBus) {
        InteractiveGuns.onClientInit();
    }
}
