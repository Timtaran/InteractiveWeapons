package net.timtaran.interactiveguns.gun.deagle.body.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.client.VxRenderState;
import net.xmx.velthoric.core.body.client.renderer.VxBodyRenderer;
import net.xmx.velthoric.math.VxConversions;

public class DeagleFrameRenderer extends VxBodyRenderer<VxBody> {
    @Override
    public void render(VxBody body, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int packedLight, VxRenderState renderState) {
        DeagleFrameModel.init();

        poseStack.pushPose();

        poseStack.mulPose(VxConversions.toJoml(renderState.transform.getRotation()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        poseStack.scale(DeagleFrameModel.MODEL_SCALE, DeagleFrameModel.MODEL_SCALE, DeagleFrameModel.MODEL_SCALE);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(DeagleFrameModel.TEXTURE_LOCATION));
        DeagleFrameModel.getFrame().render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}
