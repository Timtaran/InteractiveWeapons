package net.timtaran.interactiveguns.gun.deagle.body.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.timtaran.interactiveguns.util.InteractiveGunsIdentifier;
import net.timtaran.interactivemc.util.client.render.WireframeRenderer;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.client.VxRenderState;
import net.xmx.velthoric.core.body.client.renderer.VxBodyRenderer;
import net.xmx.velthoric.math.VxConversions;

@Environment(EnvType.CLIENT)
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

        poseStack.pushPose();

        poseStack.mulPose(VxConversions.toJoml(renderState.transform.getRotation()));
        poseStack.translate(-0.01875f, 0f, -0.1375f);
        poseStack.scale(0.0375f, 0.1625f, 0.275f);
        WireframeRenderer.renderUnitCubeWireframe(poseStack, bufferSource, packedLight, 0, 1, 0, 1);

        poseStack.popPose();
    }
}
