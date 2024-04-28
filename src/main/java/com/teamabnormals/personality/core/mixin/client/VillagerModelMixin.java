package com.teamabnormals.personality.core.mixin.client;

import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerModel.class)
public abstract class VillagerModelMixin<T extends Entity> extends HierarchicalModel<T> {

	@Shadow
	@Final
	private ModelPart head;

	@Inject(method = "setupAnim", at = @At("HEAD"))
	protected void setupAnim(T entity, float p_104054_, float p_104055_, float p_104056_, float p_104057_, float p_104058_, CallbackInfo ci) {
		float scale = entity instanceof Villager villager && villager.isBaby() && PersonalityConfig.CLIENT.largeBabyVillagerHeads.get() ? 1.5F : 1.0F;

		this.head.xScale = scale;
		this.head.yScale = scale;
		this.head.zScale = scale;
	}
}