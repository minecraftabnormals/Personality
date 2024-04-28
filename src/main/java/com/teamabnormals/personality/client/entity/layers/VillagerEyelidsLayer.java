package com.teamabnormals.personality.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;

public class VillagerEyelidsLayer extends RenderLayer<Villager, VillagerModel<Villager>> {
	private static final ResourceLocation LOCATION = new ResourceLocation(Personality.MOD_ID, "textures/entity/villager/eyelids.png");

	public VillagerEyelidsLayer(RenderLayerParent<Villager, VillagerModel<Villager>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, Villager villager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (PersonalityConfig.CLIENT.villagersCloseEyes.get() && !villager.isInvisible() && villager.isSleeping()) {
			int time = (int) (villager.level().getDayTime() % 24000L);
			if (time >= 12000 && time <= 23900) {
				VertexConsumer builder = buffer.getBuffer(RenderType.entityCutoutNoCull(LOCATION));
				this.getParentModel().setupAnim(villager, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				this.getParentModel().renderToBuffer(poseStack, builder, packedLightIn, LivingEntityRenderer.getOverlayCoords(villager, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}
}