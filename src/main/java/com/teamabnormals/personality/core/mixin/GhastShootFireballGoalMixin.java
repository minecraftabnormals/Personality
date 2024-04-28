package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.entity.monster.Ghast$GhastShootFireballGoal")
public class GhastShootFireballGoalMixin {

	@Shadow
	@Final
	private Ghast ghast;

	@Shadow
	public int chargeTime;

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Ghast;setCharging(Z)V", shift = At.Shift.BEFORE))
	public void tick(CallbackInfo ci) {
		if (this.chargeTime == 14 && PersonalityConfig.CLIENT.ghastAttackAnimation.get()) {
			this.ghast.level().broadcastEntityEvent(this.ghast, (byte) 4);
		}
	}
}