package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFSounds;
import twilightforest.item.LifedrainScepterItem;
import twilightforest.util.entities.EntityUtil;

import java.util.EnumSet;

public class LichPopMobsGoal extends Goal {

	private final Lich lich;

	@SuppressWarnings("this-escape")
	public LichPopMobsGoal(Lich lich) {
		this.lich = lich;
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		super.start();
		this.lich.setScepterTime();
	}

	@Override
	public void stop() {
		super.stop();
		this.lich.resetScepterTime();
	}

	@Override
	public boolean canUse() {
		return !this.lich.isShadowClone() &&
			this.lich.getHealth() < this.lich.getMaxHealth() &&
			this.lich.getPopCooldown() == 0 &&
			!this.lich.level().getEntitiesOfClass(Mob.class,
				this.lich.getBoundingBox().inflate(32.0D, 16.0D, 32.0D),
				e -> e.getType().is(EntityTagGenerator.LICH_POPPABLES) && this.lich.hasLineOfSight(e)).isEmpty();
	}

	@Override
	public void tick() {
		if (this.lich.getTeleportInvisibility() > 0) return;
		super.tick();
		if (this.lich.getScepterTimeLeft() > 0 || this.lich.level().isClientSide()) return;
		for (Mob mob : this.lich.level().getEntitiesOfClass(Mob.class, this.lich.getBoundingBox().inflate(32.0D, 16.0D, 32.0D), e -> e.getType().is(EntityTagGenerator.LICH_POPPABLES) && e != this.lich)) {
			if (this.lich.getSensing().hasLineOfSight(mob)) {
				mob.discard();
				//rain particles
				LifedrainScepterItem.animateTargetShatter((ServerLevel) this.lich.level(), mob);
				// play death sound
				SoundEvent deathSound = EntityUtil.getDeathSound(mob);
				if (deathSound != null) {
					this.lich.level().playSound(null, mob.blockPosition(), deathSound, SoundSource.HOSTILE, 1.0F, mob.getVoicePitch());
				}
				//funny pop sound
				this.lich.playSound(TFSounds.LICH_POP_MOB.get(), 3.0F, 0.4F + this.lich.getRandom().nextFloat() * 0.2F);
				mob.playSound(TFSounds.LICH_POP_MOB.get(), 3.0F, 0.4F + this.lich.getRandom().nextFloat() * 0.2F);
				// make trail so it's clear that we did it
				this.lich.makeMagicTrail(mob.getEyePosition(), this.lich.getEyePosition(), 1.0F, 0.5F, 0.5F);
				//heal a little health
				this.lich.heal(2.0F);
				this.lich.swing(InteractionHand.MAIN_HAND);
				this.lich.setPopCooldown(40);
				this.lich.gameEvent(GameEvent.ENTITY_DIE);
				break;
			}
		}
	}
}
