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
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(InteractiveGunsIdentifier.get("deaglerenderer"), "main");
    private static final ResourceLocation TEXTURE_LOCATION = InteractiveGunsIdentifier.get("textures/entity/deagle.png");

    private ModelPart root;
    private ModelPart box;
    private ModelPart chamber;
    private ModelPart frame;

    private boolean initialized = false;

    private void init() {
        if (initialized) return;

        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        this.root = modelSet.bakeLayer(LAYER_LOCATION);

        this.chamber = this.root.getChild("chamber");
        this.frame = this.root.getChild("frame");

        initialized = true;
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition chamber = partdefinition.addOrReplaceChild("chamber", CubeListBuilder.create().texOffs(11, 0).addBox(-0.25F, -0.1F, -1.7F, 0.5F, 0.05F, 0.901F, new CubeDeformation(0.0F))
                .texOffs(13, 11).addBox(-0.2985F, -0.1F, -1.1F, 0.049F, 0.05F, 0.301F, new CubeDeformation(0.0F))
                .texOffs(13, 12).addBox(0.246F, -0.1F, -1.1F, 0.049F, 0.05F, 0.301F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = chamber.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 11).addBox(-0.25F, -2.4F, -0.443F, 0.5F, 2.3F, 0.75F, new CubeDeformation(0.0F))
                .texOffs(10, 11).addBox(-0.25F, -0.1F, 0.057F, 0.5F, 0.09F, 0.25F, new CubeDeformation(0.0F))
                .texOffs(5, 13).addBox(-0.25F, 0.04F, -0.443F, 0.5F, 0.04F, 0.24F, new CubeDeformation(0.0F))
                .texOffs(5, 11).addBox(-0.25F, -0.1F, -0.443F, 0.5F, 0.14F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1F, -1.25F, -0.1745F, 0.0F, 0.0F));

        PartDefinition frame = partdefinition.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(10, 12).addBox(-0.299F, -0.35F, -1.781F, 0.598F, 0.25F, 0.15F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.302F, -2.0F, -2.2F, 0.604F, 0.2F, 4.2F, new CubeDeformation(0.0F))
                .texOffs(0, 5).addBox(-0.3F, -2.6F, -1.8F, 0.6F, 0.6F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(11, 2).addBox(-0.275F, -1.3F, -0.7F, 0.55F, 0.05F, 0.68F, new CubeDeformation(0.0F))
                .texOffs(8, 13).addBox(-0.275F, -1.8F, -0.1F, 0.55F, 0.5F, 0.05F, new CubeDeformation(0.0F))
                .texOffs(11, 7).addBox(0.199F, -0.204F, -1.638F, 0.1F, 0.15F, 0.75F, new CubeDeformation(0.0F))
                .texOffs(11, 9).addBox(-0.299F, -0.205F, -1.638F, 0.1F, 0.13F, 0.75F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(11, 13).addBox(-0.3F, -0.168F, -0.09F, 0.6F, 0.15F, 0.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.08F, -0.8F, 0.48F, 0.0F, 0.0F));

        PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(11, 4).addBox(-0.299F, -1.9F, 0.307F, 0.598F, 1.75F, 0.05F, new CubeDeformation(0.0F))
                .texOffs(5, 11).addBox(0.201F, -0.1F, -0.443F, 0.1F, 0.11F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(-0.301F, -1.78F, -0.443F, 0.1F, 1.68F, 0.75F, new CubeDeformation(0.0F))
                .texOffs(5, 11).addBox(-0.301F, -0.1F, -0.443F, 0.1F, 0.11F, 0.5F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(0.201F, -1.78F, -0.443F, 0.1F, 1.68F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1F, -1.25F, -0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r4 = frame.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 12).addBox(-0.298F, -0.34F, 0.0F, 0.596F, 0.34F, 0.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.76F, -1.86F, 1.4835F, 0.0F, 0.0F));

        PartDefinition cube_r5 = frame.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(9, 11).addBox(-0.298F, -0.3F, 0.0F, 0.596F, 0.3F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.56F, -1.7F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r6 = frame.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(9, 11).addBox(-0.299F, -0.6F, 0.0F, 0.598F, 0.6F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.2F, -1.635F, 0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r7 = frame.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(10, 12).addBox(-0.3F, -1.1535F, -0.088F, 0.6F, 1.13F, 0.15F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.312F, -1.7F, -0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r8 = frame.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(10, 12).addBox(-0.3F, -0.1535F, -0.088F, 0.6F, 0.13F, 0.15F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.062F, -1.7F, -0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void render(VxBody body, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, int packedLight, VxRenderState renderState) {
        init();

        poseStack.pushPose();

        poseStack.mulPose(VxConversions.toJoml(renderState.transform.getRotation()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_LOCATION));
        root.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        poseStack.pushPose();

        poseStack.mulPose(VxConversions.toJoml(renderState.transform.getRotation()));
        poseStack.translate(-0.01875f, 0f, -0.1375f);
        poseStack.scale(0.0375f, 0.1625f, 0.275f);
        WireframeRenderer.renderUnitCubeWireframe(poseStack, bufferSource, packedLight, 0, 1, 0, 1);

        poseStack.popPose();
    }
}
