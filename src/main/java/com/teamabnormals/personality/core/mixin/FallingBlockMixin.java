package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin {

	@Inject(method = "isFree", at = @At("RETURN"), cancellable = true)
	private static void isFree(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (PersonalityConfig.COMMON.fallingSnowLayers.get() && cir.getReturnValue() && state.is(Blocks.SNOW) && state.getValue(SnowLayerBlock.LAYERS) == SnowLayerBlock.MAX_HEIGHT) {
			cir.setReturnValue(false);
		}
	}
}