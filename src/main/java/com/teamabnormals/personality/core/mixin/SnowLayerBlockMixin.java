package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.blueprint.common.entity.BlueprintFallingBlockEntity;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public abstract class SnowLayerBlockMixin extends Block implements Fallable {

	@Shadow
	public abstract boolean canSurvive(BlockState p_56602_, LevelReader p_56603_, BlockPos p_56604_);

	public SnowLayerBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
	public void updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos, CallbackInfoReturnable<BlockState> cir) {
		if (PersonalityConfig.COMMON.fallingSnowLayers.get()) {
			level.scheduleTick(currentPos, this, 2);
			cir.setReturnValue(super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos));
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (PersonalityConfig.COMMON.fallingSnowLayers.get()) {
			level.scheduleTick(pos, this, 2);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (PersonalityConfig.COMMON.fallingSnowLayers.get() && !canSurvive(state, level, pos) && (level.isEmptyBlock(pos.below()) || FallingBlock.isFree(level.getBlockState(pos.below()))) && pos.getY() >= level.getMinBuildHeight()) {
			BlueprintFallingBlockEntity.fall(level, pos, level.getBlockState(pos));
		}
	}

	@Override
	public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
		BlockState state = level.getBlockState(pos);
		if (PersonalityConfig.COMMON.fallingSnowLayers.get() && state.is(Blocks.SNOW) && state.getValue(SnowLayerBlock.LAYERS) < SnowLayerBlock.MAX_HEIGHT) {
			int layers = state.getValue(SnowLayerBlock.LAYERS) + entity.getBlockState().getValue(SnowLayerBlock.LAYERS);
			level.setBlockAndUpdate(pos, state.setValue(SnowLayerBlock.LAYERS, Math.min(8, layers)));
			if (layers > 8) {
				level.setBlockAndUpdate(pos.above(), state.setValue(SnowLayerBlock.LAYERS, layers - 8));
			}
		}
	}
}