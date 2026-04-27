package net.timtaran.interactiveguns.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.timtaran.interactiveguns.gun.deagle.body.client.DeagleFrameRenderer;
import net.timtaran.interactiveguns.init.InteractiveGuns;

public class InteractiveGunsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(DeagleFrameRenderer.LAYER_LOCATION, DeagleFrameRenderer::createBodyLayer);
        InteractiveGuns.onClientInit();
    }
}
