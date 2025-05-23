package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

public class TowerwoodBorer extends Monster {

	private SummonBorersGoal summonBorers;

	public TowerwoodBorer(EntityType<? extends TowerwoodBorer> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
		this.goalSelector.addGoal(3, this.summonBorers = new SummonBorersGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new HideInTowerwoodGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 15.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.27D)
			.add(Attributes.ATTACK_DAMAGE, 5.0D)
			.add(Attributes.FOLLOW_RANGE, 8.0D);
	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.EVENTS;
	}

	@Override
	public boolean isSteppingCarefully() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.TOWERWOOD_BORER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.TOWERWOOD_BORER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.TOWERWOOD_BORER_DEATH.get();
	}

	// [VanillaCopy] Silverfish.hurt
	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (this.isInvulnerableTo(level, source)) {
			return false;
		} else {
			if ((source.getEntity() != null || source.is(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH)) && this.summonBorers != null) {
				this.summonBorers.notifyHurt();
			}

			return super.hurtServer(level, source, amount);
		}
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(TFSounds.TOWERWOOD_BORER_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	public void tick() {
		this.yBodyRot = this.getYRot();
		super.tick();
	}

	// [VanillaCopy] Silverfish.SilverfishMergeWithStoneGoal. Changes noted
	private static class HideInTowerwoodGoal extends RandomStrollGoal {

		private Direction facing;
		private boolean doMerge;

		public HideInTowerwoodGoal(TowerwoodBorer borer) {
			super(borer, 1.0D, 10);
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (this.mob.getTarget() != null) {
				return false;
			} else if (!this.mob.getNavigation().isDone()) {
				return false;
			} else {
				RandomSource random = this.mob.getRandom();

				if (random.nextInt(10) == 0 && EventHooks.canEntityGrief((ServerLevel) this.mob.level(), this.mob)) {
					this.facing = Direction.getRandom(random);
					BlockPos blockpos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.facing);
					BlockState state = this.mob.level().getBlockState(blockpos);

					// TF - Change block check
					if (state.is(TFBlocks.TOWERWOOD)) {
						this.doMerge = true;
						return true;
					}
				}

				this.doMerge = false;
				return super.canUse();
			}
		}

		@Override
		public boolean canContinueToUse() {
			return !this.doMerge && super.canContinueToUse();
		}

		@Override
		public void start() {
			if (!this.doMerge) {
				super.start();
			} else {
				Level level = this.mob.level();
				BlockPos blockpos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()).relative(this.facing);
				BlockState state = level.getBlockState(blockpos);

				// TF - Change block check
				// TF - add a random chance to dig. This should prevent them from instantly digging away
				if (state.is(TFBlocks.TOWERWOOD) && this.mob.getRandom().nextInt(5) == 0) {
					// TF - Change block type
					level.setBlock(blockpos, TFBlocks.INFESTED_TOWERWOOD.get().defaultBlockState(), Block.UPDATE_ALL);
					this.mob.spawnAnim();
					this.mob.discard();
				}
			}
		}
	}

	// [VanillaCopy] of Silverfish.SilverfishWakeUpFriendsGoal. Changes noted
	private static class SummonBorersGoal extends Goal {

		private final TowerwoodBorer borer; // TF - type change
		private int lookForFriends;

		public SummonBorersGoal(TowerwoodBorer borer) {
			this.borer = borer;
		}

		public void notifyHurt() {
			if (this.lookForFriends == 0) {
				this.lookForFriends = 20;
			}
		}

		@Override
		public boolean canUse() {
			return this.lookForFriends > 0;
		}

		@Override
		public void tick() {
			--this.lookForFriends;

			if (this.lookForFriends <= 0) {

				Level world = this.borer.level();
				RandomSource random = this.borer.getRandom();
				BlockPos pos = new BlockPos(this.borer.blockPosition());

				for (int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i) {
					for (int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j) {
						for (int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k) {

							BlockPos offsetPos = pos.offset(j, i, k);
							BlockState state = world.getBlockState(offsetPos);

							// TF - Change block check
							if (state.is(TFBlocks.INFESTED_TOWERWOOD)) {
								if (EventHooks.canEntityGrief((ServerLevel) world, this.borer)) {
									world.destroyBlock(offsetPos, true);
									this.borer.gameEvent(GameEvent.BLOCK_DESTROY);
								} else {
									// TF - reset to normal tower wood
									world.setBlock(offsetPos, TFBlocks.TOWERWOOD.get().defaultBlockState(), Block.UPDATE_ALL);
								}

								if (random.nextBoolean()) {
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}
