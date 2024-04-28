package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends StandingAndWallBlockItem {

	public BannerItemMixin(Block floorBlock, Block wallBlockIn, Properties propertiesIn, Direction direction) {
		super(floorBlock, wallBlockIn, propertiesIn, direction);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return PersonalityConfig.COMMON.equipableBanners.get() ? EquipmentSlot.HEAD : super.getEquipmentSlot(stack);
	}
}
