package com.teamabnormals.personality.core.mixin.client;

import com.teamabnormals.personality.client.entity.layers.VillagerEyelidsLayer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerRenderer.class)
public abstract class VillagerRendererMixin extends MobRenderer<Villager, VillagerModel<Villager>> {

	public VillagerRendererMixin(Context context, VillagerModel<Villager> model, float shadowSize) {
		super(context, model, shadowSize);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityRendererProvider.Context context, CallbackInfo ci) {
		this.layers.add(0, new VillagerEyelidsLayer(this));
	}
}