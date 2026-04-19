package net.timtaran.interactiveguns.gun.deagle.body.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.client.VxRenderState;
import net.xmx.velthoric.core.body.client.renderer.VxBodyRenderer;
import net.xmx.velthoric.math.VxConversions;

@Environment(EnvType.CLIENT)
public class DeagleFrameRenderer extends VxBodyRenderer<VxBody> {
    // VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(YOUR_TEXTURE));
    @Override
    public void render(VxBody body, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int packedLight, VxRenderState renderState) {
        BlockState blockState = Blocks.WHITE_CONCRETE.defaultBlockState();

        float hx = 0.0375f / 2f;
        float hy = 0.1625f / 2f;
        float hz = 0.275f / 2f;

        float fullWidth = hx * 2.0f;
        float fullHeight = hy * 2.0f;
        float fullDepth = hz * 2.0f;

        poseStack.pushPose();

        poseStack.mulPose(VxConversions.toJoml(renderState.transform.getRotation()));

        poseStack.translate(-hx, -hy, -hz);
        poseStack.scale(fullWidth, fullHeight, fullDepth);

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                blockState,
                poseStack,
                bufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

    }
}
