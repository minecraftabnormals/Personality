package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.common.extension.GhastExtension;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(Ghast.class)
public abstract class GhastMixin extends FlyingMob implements GhastExtension {
	@Unique
	private int attackTimer;
	@Unique
	private int prevAttackTimer;

	private GhastMixin(EntityType<? extends FlyingMob> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide() && PersonalityConfig.CLIENT.ghastAttackAnimation.get()) {
			this.prevAttackTimer = this.attackTimer;
			if (this.attackTimer > 0) {
				this.attackTimer--;
			}
		}
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (PersonalityConfig.CLIENT.ghastAttackAnimation.get() && id == 4) {
			this.prevAttackTimer = 10;
			this.attackTimer = 10;
			return;
		}

		super.handleEntityEvent(id);
	}

	@Override
	public int getPreviousAttackTimer() {
		return this.prevAttackTimer;
	}

	@Override
	public int getAttackTimer() {
		return this.attackTimer;
	}
}