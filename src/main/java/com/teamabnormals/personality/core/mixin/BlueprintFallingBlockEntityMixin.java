package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.blueprint.common.entity.BlueprintFallingBlockEntity;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlueprintFallingBlockEntity.class)
public abstract class BlueprintFallingBlockEntityMixin extends FallingBlockEntity {

	public BlueprintFallingBlockEntityMixin(EntityType<? extends FallingBlockEntity> p_31950_, Level p_31951_) {
		super(p_31950_, p_31951_);
	}

	@ModifyVariable(method = "tick", at = @At("STORE"))
	private BlockPos blockPos(BlockPos pos) {
		BlockState state = this.level().getBlockState(pos);
		if (PersonalityConfig.COMMON.fallingSnowLayers.get() && this.getBlockState().is(Blocks.SNOW) && this.onGround() && state.is(Blocks.SNOW) && state.getValue(SnowLayerBlock.LAYERS) == SnowLayerBlock.MAX_HEIGHT) {
			return pos.above();
		}
		return pos;
	}
}