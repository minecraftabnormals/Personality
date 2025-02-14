package com.teamabnormals.personality.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class SheepFurLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {
	private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation(Personality.MOD_ID, "textures/entity/sheep/sheep_fur_overlay.png");

	public SheepFurLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, Sheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!sheep.isInvisible() && PersonalityConfig.CLIENT.sheepFurOverlay.get()) {
			float f;
			float f1;
			float f2;
			if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getString())) {
				int i1 = 25;
				int i = sheep.tickCount / 25 + sheep.getId();
				int j = DyeColor.values().length;
				int k = i % j;
				int l = (i + 1) % j;
				float f3 = ((float) (sheep.tickCount % 25) + partialTicks) / 25.0F;
				float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
				float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
				f = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
				f1 = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
				f2 = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
			} else {
				float[] afloat = Sheep.getColorArray(sheep.getColor());
				f = afloat[0];
				f1 = afloat[1];
				f2 = afloat[2];
			}

			coloredCutoutModelCopyLayerRender(this.getParentModel(), this.getParentModel(), SHEEP_FUR_LOCATION, stack, buffer, packedLightIn, sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, f, f1, f2);
		}
	}
}