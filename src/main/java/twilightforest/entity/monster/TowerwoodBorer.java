package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;

import java.util.EnumSet;
import java.util.Random;

public class TowerwoodBorer extends Monster {

	private AISummonSilverfish summonSilverfish;

	public TowerwoodBorer(EntityType<? extends TowerwoodBorer> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, this.summonSilverfish = new AISummonSilverfish(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(4, new AIHideInStone(this));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
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
	public boolean isSteppingCarefully() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.TERMITE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.TERMITE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.TERMITE_DEATH;
	}

	// [VanillaCopy] EntitySilverfish.attackEntityFrom
	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if ((source instanceof EntityDamageSource || source == DamageSource.MAGIC) && this.summonSilverfish != null) {
				this.summonSilverfish.notifyHurt();
			}

			return super.hurt(source, amount);
		}
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(TFSounds.TERMITE_STEP, 0.15F, 1.0F);
	}

	@Override
	public void tick() {
		this.yBodyRot = this.getYRot();
		super.tick();
	}

	@Override
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	// [VanillaCopy] EntitySilverfish$AIHideInStone. Changes noted
	private static class AIHideInStone extends RandomStrollGoal {

		private Direction facing;
		private boolean doMerge;

		public AIHideInStone(TowerwoodBorer silverfishIn) {
			super(silverfishIn, 1.0D, 10);
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean canUse() {
			if (this.mob.getTarget() != null) {
				return false;
			} else if (!this.mob.getNavigation().isDone()) {
				return false;
			} else {
				Random random = this.mob.getRandom();

				if (random.nextInt(10) == 0 && ForgeEventFactory.getMobGriefingEvent(this.mob.level, this.mob)) {
					this.facing = Direction.getRandom(random);
					BlockPos blockpos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
					BlockState iblockstate = this.mob.level.getBlockState(blockpos);

					// TF - Change block check
					if (iblockstate == TFBlocks.TOWERWOOD.get().defaultBlockState()) {
						this.doMerge = true;
						return true;
					}
				}

				this.doMerge = false;
				return super.canUse();
			}
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean canContinueToUse() {
			return !this.doMerge && super.canContinueToUse();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void start() {
			if (!this.doMerge) {
				super.start();
			} else {
				Level world = this.mob.level;
				BlockPos blockpos = (new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ())).relative(this.facing);
				BlockState iblockstate = world.getBlockState(blockpos);

				// TF - Change block check
				if (iblockstate == TFBlocks.TOWERWOOD.get().defaultBlockState()) {
					// TF - Change block type
					world.setBlock(blockpos, TFBlocks.INFESTED_TOWERWOOD.get().defaultBlockState(), 3);
					this.mob.spawnAnim();
					this.mob.discard();
				}
			}
		}
	}

	// [VanillaCopy] of EntitySilverfish$AISummonSilverfish. Changes noted
	private static class AISummonSilverfish extends Goal {

		private final TowerwoodBorer silverfish; // TF - type change
		private int lookForFriends;

		public AISummonSilverfish(TowerwoodBorer silverfishIn) {
			this.silverfish = silverfishIn;
		}

		public void notifyHurt() {
			if (this.lookForFriends == 0) {
				this.lookForFriends = 20;
			}
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean canUse() {
			return this.lookForFriends > 0;
		}

		/**
		 * Updates the task
		 */
		@Override
		public void tick() {
			--this.lookForFriends;

			if (this.lookForFriends <= 0) {

				Level world = this.silverfish.level;
				Random random = this.silverfish.getRandom();
				BlockPos blockpos = new BlockPos(this.silverfish.blockPosition());

				for (int i = 0; i <= 5 && i >= -5; i = i <= 0 ? 1 - i : -i) {
					for (int j = 0; j <= 10 && j >= -10; j = j <= 0 ? 1 - j : -j) {
						for (int k = 0; k <= 10 && k >= -10; k = k <= 0 ? 1 - k : -k) {

							BlockPos blockpos1 = blockpos.offset(j, i, k);
							BlockState iblockstate = world.getBlockState(blockpos1);

							// TF - Change block check
							if (iblockstate == TFBlocks.INFESTED_TOWERWOOD.get().defaultBlockState()) {
								if (ForgeEventFactory.getMobGriefingEvent(world, this.silverfish)) {
									world.destroyBlock(blockpos1, true);
								} else {
									// TF - reset to normal tower wood
									world.setBlock(blockpos1, TFBlocks.TOWERWOOD.get().defaultBlockState(), 3);
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
