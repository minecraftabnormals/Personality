package com.teamabnormals.personality.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.personality.common.extension.GhastExtension;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GhastRenderer.class)
public class GhastRendererMixin {

	@Inject(method = "scale(Lnet/minecraft/world/entity/monster/Ghast;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At("HEAD"), cancellable = true)
	protected void preRenderCallback(Ghast entity, PoseStack matrixStack, float partialTicks, CallbackInfo ci) {
		if (!PersonalityConfig.CLIENT.ghastAttackAnimation.get()) return;
		
		GhastExtension ext = (GhastExtension) entity;

		float attack = Mth.lerp(partialTicks, ext.getPreviousAttackTimer(), ext.getAttackTimer()) / 10F;
		float anim = (float) (attack * attack - Math.pow(attack, 6) - Math.pow(attack, 7) + attack);
		float y = 4F - anim / 1.5F;
		float xz = 4F + anim;

		matrixStack.scale(xz, y, xz);

		ci.cancel();
	}
}