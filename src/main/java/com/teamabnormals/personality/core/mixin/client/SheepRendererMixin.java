package com.teamabnormals.personality.core.mixin.client;

import com.teamabnormals.personality.client.entity.layers.SheepFurLayer;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepRenderer.class)
public abstract class SheepRendererMixin extends MobRenderer<Sheep, SheepModel<Sheep>> {

	public SheepRendererMixin(Context context, SheepModel<Sheep> model, float shadowSize) {
		super(context, model, shadowSize);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(Context context, CallbackInfo ci) {
		this.layers.add(0, new SheepFurLayer(this));
	}
}