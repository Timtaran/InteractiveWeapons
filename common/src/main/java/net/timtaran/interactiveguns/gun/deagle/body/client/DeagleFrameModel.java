package net.timtaran.interactiveguns.gun.deagle.body.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.timtaran.interactiveguns.util.InteractiveGunsIdentifier;

public class DeagleFrameModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(InteractiveGunsIdentifier.get("deaglerenderer"), "main");
    public static final ResourceLocation TEXTURE_LOCATION = InteractiveGunsIdentifier.get("textures/entity/deagle.png");

    public static final float MODEL_SCALE = 1f / 16f;

    private static ModelPart root;
    private static ModelPart magazine;
    private static ModelPart slider;
    private static ModelPart frame;

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        root = modelSet.bakeLayer(LAYER_LOCATION);

        magazine = root.getChild("magazine");
        slider = root.getChild("slider");
        frame = root.getChild("frame");

        initialized = true;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition slider = partdefinition.addOrReplaceChild("slider", CubeListBuilder.create().texOffs(69, 106).addBox(-4.8F, -42.4F, -31.92F, 9.6F, 4.0F, 23.92F, new CubeDeformation(0.0F))
                .texOffs(69, 135).addBox(3.2F, -39.2F, -32.0F, 2.4F, 7.2F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(0, 106).addBox(3.2F, -36.8F, -8.0F, 2.4F, 4.8F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(85, 68).addBox(-5.6F, -36.8F, -8.0F, 2.4F, 4.8F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(122, 135).addBox(-5.6F, -39.2F, -32.0F, 2.4F, 7.2F, 24.0F, new CubeDeformation(0.0F))
                .texOffs(26, 184).addBox(-1.616F, -44.2128F, -27.44F, 3.232F, 1.8128F, 1.28F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition sight_r1 = slider.addOrReplaceChild("sight_r1", CubeListBuilder.create().texOffs(183, 113).addBox(-4.0F, -3.2F, 0.0F, 3.2F, 3.2F, 3.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4F, -41.6F, -28.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition magazine = partdefinition.addOrReplaceChild("magazine", CubeListBuilder.create().texOffs(138, 106).addBox(-4.0F, -1.6F, -27.2F, 8.0F, 0.8F, 14.416F, new CubeDeformation(0.0F))
                .texOffs(0, 184).addBox(-4.776F, -1.6F, -17.6F, 0.784F, 0.8F, 4.816F, new CubeDeformation(0.0F))
                .texOffs(13, 184).addBox(3.936F, -1.6F, -17.6F, 0.784F, 0.8F, 4.816F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = magazine.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 144).addBox(-4.0F, -28.8F, -7.088F, 8.0F, 27.2F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(182, 0).addBox(-4.0F, -1.6F, 0.912F, 8.0F, 1.44F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(182, 30).addBox(-4.0F, 0.64F, -7.088F, 8.0F, 0.64F, 3.84F, new CubeDeformation(0.0F))
                .texOffs(138, 122).addBox(-4.0F, -1.6F, -7.088F, 8.0F, 2.24F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.6F, -20.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition frame = partdefinition.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(129, 167).addBox(-4.768F, -5.76F, -30.848F, 9.536F, 4.16F, 8.96F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.832F, -32.0F, -32.0F, 9.664F, 3.2F, 64.0F, new CubeDeformation(0.0F))
                .texOffs(0, 68).addBox(-4.8F, -41.6F, -8.0F, 9.6F, 4.8F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(187, 41).addBox(-1.616F, -43.192F, 30.72F, 3.232F, 1.592F, 0.96F, new CubeDeformation(0.0F))
                .texOffs(86, 167).addBox(-4.4F, -20.8F, -11.2F, 8.8F, 1.2F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(64, 183).addBox(-4.4F, -27.2F, -0.8F, 8.8F, 6.4F, 1.2F, new CubeDeformation(0.0F))
                .texOffs(41, 167).addBox(-4.4F, -28.8F, -9.6F, 8.8F, 1.6F, 12.8F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(3.184F, -3.264F, -26.208F, 1.6F, 2.4F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(86, 181).addBox(-4.784F, -3.28F, -26.208F, 1.6F, 2.08F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = frame.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(182, 36).addBox(-4.8F, -2.688F, -1.44F, 9.6F, 2.4F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.28F, -12.8F, 0.48F, 0.0F, 0.0F));

        PartDefinition cube_r3 = frame.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(41, 183).addBox(-6.8F, -1.6F, 0.0F, 4.8F, 1.6F, 5.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4F, -27.2F, -9.6F, -1.5272F, 0.0F, 0.0F));

        PartDefinition sight_r2 = frame.addOrReplaceChild("sight_r2", CubeListBuilder.create().texOffs(183, 106).addBox(-4.0F, -3.2F, 0.0F, 3.2F, 3.2F, 3.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4F, -41.6F, 28.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition zadnayahueta_r1 = frame.addOrReplaceChild("zadnayahueta_r1", CubeListBuilder.create().texOffs(142, 181).addBox(-4.8F, -8.0F, 0.0F, 4.8F, 8.0F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4F, -32.0F, -30.4F, 0.9599F, 0.0F, 0.0F));

        PartDefinition cube_r4 = frame.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(154, 84).addBox(-9.6F, -11.2F, -9.6F, 9.6F, 11.2F, 9.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8F, -32.0F, 24.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r5 = frame.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(168, 167).addBox(-4.784F, -30.4F, 4.912F, 9.568F, 28.0F, 0.8F, new CubeDeformation(0.0F))
                .texOffs(182, 14).addBox(3.216F, -1.6F, -7.088F, 1.6F, 1.76F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.6F, -20.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r6 = frame.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(175, 154).addBox(-4.8F, -7.52F, 0.96F, 9.6F, 6.4F, 2.992F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -28.16F, -29.76F, 1.6581F, 0.0F, 0.0F));

        PartDefinition cube_r7 = frame.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(182, 6).addBox(-4.768F, -5.44F, 0.0F, 9.536F, 5.44F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -28.16F, -29.76F, 1.4835F, 0.0F, 0.0F));

        PartDefinition cube_r8 = frame.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(115, 181).addBox(-4.768F, -4.8F, 0.0F, 9.536F, 4.8F, 3.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -24.96F, -27.2F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r9 = frame.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(175, 122).addBox(-4.784F, -9.6F, 0.0F, 9.568F, 9.6F, 6.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -19.2F, -26.16F, 0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r10 = frame.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(41, 144).addBox(-4.8F, -18.456F, -1.408F, 9.6F, 18.08F, 2.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.992F, -29.6F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r11 = frame.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(182, 25).addBox(-4.8F, -2.456F, -1.408F, 9.6F, 2.08F, 2.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.992F, -27.2F, -0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r12 = frame.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(154, 42).addBox(-4.816F, -28.96F, -6.976F, 1.6F, 27.36F, 14.08F, new CubeDeformation(0.0F))
                .texOffs(149, 0).addBox(3.216F, -28.96F, -6.976F, 1.6F, 27.36F, 14.08F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.6F, -22.08F, -0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    public static ModelPart getRoot() {
        return root;
    }

    public static ModelPart getFrame() {
        return frame;
    }

    public static ModelPart getMagazine() {
        return magazine;
    }

    public static ModelPart  getSlider() {
        return slider;
    }
}
