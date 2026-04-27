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

        PartDefinition chamber = partdefinition.addOrReplaceChild("chamber", CubeListBuilder.create().texOffs(16, 12).addBox(-0.3F, -0.1F, -1.7F, 0.6F, 0.1F, 0.9F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = chamber.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 10).addBox(-0.25F, -1.2F, -0.443F, 0.5F, 1.1F, 0.75F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.25F, -0.1F, 0.057F, 0.5F, 0.14F, 0.25F, new CubeDeformation(0.0F))
                .texOffs(14, 16).addBox(-0.25F, -0.1F, -0.443F, 0.5F, 0.18F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1F, -1.25F, -0.1745F, 0.0F, 0.0F));

        PartDefinition frame = partdefinition.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3F, -2.0F, -2.2F, 0.6F, 0.2F, 4.4F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.3F, -2.6F, -1.8F, 0.6F, 0.6F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 13).addBox(-0.275F, -1.3F, -0.7F, 0.55F, 0.05F, 0.6F, new CubeDeformation(0.0F))
                .texOffs(16, 13).addBox(-0.275F, -1.8F, -0.1F, 0.55F, 0.55F, 0.05F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3F, -0.168F, -0.19F, 0.6F, 0.15F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.08F, -0.8F, 0.48F, 0.0F, 0.0F));

        PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 14).addBox(-0.299F, -1.9F, 0.307F, 0.598F, 1.75F, 0.05F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1F, -1.25F, -0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
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
    }
}
