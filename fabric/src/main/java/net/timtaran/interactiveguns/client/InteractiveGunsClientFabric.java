package net.timtaran.interactiveguns.client;

import net.fabricmc.api.ClientModInitializer;
import net.timtaran.interactiveguns.init.InteractiveGuns;

public class InteractiveGunsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InteractiveGuns.onClientInit();
    }
}
