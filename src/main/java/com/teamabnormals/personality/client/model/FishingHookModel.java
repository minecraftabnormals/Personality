package com.teamabnormals.personality.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.projectile.FishingHook;

public class FishingHookModel extends EntityModel<FishingHook> {
	private final ModelPart bobber;

	public FishingHookModel(ModelPart root) {
		this.bobber = root.getChild("bobber");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition bobber = root.addOrReplaceChild("bobber", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		bobber.addOrReplaceChild("cross_1", CubeListBuilder.create().texOffs(5, 6).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, true), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		bobber.addOrReplaceChild("cross_2", CubeListBuilder.create().texOffs(5, 6).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		bobber.addOrReplaceChild("hook", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, 0.0F, -2.5F, 0.0F, 4.0F, 3.0F, true), PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(FishingHook entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.bobber.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}