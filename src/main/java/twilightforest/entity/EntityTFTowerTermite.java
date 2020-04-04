package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import java.util.EnumSet;
import java.util.Random;

public class EntityTFTowerTermite extends MonsterEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/tower_termite");
	private AISummonSilverfish summonSilverfish;

	public EntityTFTowerTermite(EntityType<? extends EntityTFTowerTermite> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, this.summonSilverfish = new AISummonSilverfish(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(4, new AIHideInStone(this));
		this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(8.0D);
	}

	@Override
	public boolean bypassesSteppingEffects() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SILVERFISH_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SILVERFISH_DEATH;
	}

	// [VanillaCopy] EntitySilverfish.attackEntityFrom
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if ((source instanceof EntityDamageSource || source == DamageSource.MAGIC) && this.summonSilverfish != null) {
				this.summonSilverfish.notifyHurt();
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
	}

	//TODO: Move to loot table
	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void tick() {
		this.renderYawOffset = this.rotationYaw;
		super.tick();
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	// [VanillaCopy] EntitySilverfish$AIHideInStone. Changes noted
	private static class AIHideInStone extends RandomWalkingGoal {

		private Direction facing;
		private boolean doMerge;

		public AIHideInStone(EntityTFTowerTermite silverfishIn) {
			super(silverfishIn, 1.0D, 10);
			this.setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute() {
			if (this.creature.getAttackTarget() != null) {
				return false;
			} else if (!this.creature.getNavigator().noPath()) {
				return false;
			} else {
				Random random = this.creature.getRNG();

				if (random.nextInt(10) == 0 && ForgeEventFactory.getMobGriefingEvent(this.creature.world, this.creature)) {
					this.facing = Direction.random(random);
					BlockPos blockpos = (new BlockPos(this.creature.getX(), this.creature.getY() + 0.5D, this.creature.getZ())).offset(this.facing);
					BlockState iblockstate = this.creature.world.getBlockState(blockpos);

					// TF - Change block check
					if (iblockstate == TFBlocks.tower_wood.get().getDefaultState()) {
						this.doMerge = true;
						return true;
					}
				}

				this.doMerge = false;
				return super.shouldExecute();
			}
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean shouldContinueExecuting() {
			return this.doMerge ? false : super.shouldContinueExecuting();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void startExecuting() {
			if (!this.doMerge) {
				super.startExecuting();
			} else {
				World world = this.creature.world;
				BlockPos blockpos = (new BlockPos(this.creature.getX(), this.creature.getY() + 0.5D, this.creature.getZ())).offset(this.facing);
				BlockState iblockstate = world.getBlockState(blockpos);

				// TF - Change block check
				if (iblockstate == TFBlocks.tower_wood.get().getDefaultState()) {
					// TF - Change block type
					world.setBlockState(blockpos, TFBlocks.tower_wood_infested.get().getDefaultState(), 3);
					this.creature.spawnExplosionParticle();
					this.creature.remove();
				}
			}
		}
	}

	// [VanillaCopy] of EntitySilverfish$AISummonSilverfish. Changes noted
	private static class AISummonSilverfish extends Goal {

		private EntityTFTowerTermite silverfish; // TF - type change
		private int lookForFriends;

		public AISummonSilverfish(EntityTFTowerTermite silverfishIn) {
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
		public boolean shouldExecute() {
			return this.lookForFriends > 0;
		}

		/**
		 * Updates the task
		 */
		@Override
		public void tick() {
			--this.lookForFriends;

			if (this.lookForFriends <= 0) {

				World world = this.silverfish.world;
				Random random = this.silverfish.getRNG();
				BlockPos blockpos = new BlockPos(this.silverfish);

				for (int i = 0; i <= 5 && i >= -5; i = i <= 0 ? 1 - i : 0 - i) {
					for (int j = 0; j <= 10 && j >= -10; j = j <= 0 ? 1 - j : 0 - j) {
						for (int k = 0; k <= 10 && k >= -10; k = k <= 0 ? 1 - k : 0 - k) {

							BlockPos blockpos1 = blockpos.add(j, i, k);
							BlockState iblockstate = world.getBlockState(blockpos1);

							// TF - Change block check
							if (iblockstate == TFBlocks.tower_wood_infested.get().getDefaultState()) {
								if (ForgeEventFactory.getMobGriefingEvent(world, this.silverfish)) {
									world.destroyBlock(blockpos1, true);
								} else {
									// TF - reset to normal tower wood
									world.setBlockState(blockpos1, TFBlocks.tower_wood.get().getDefaultState(), 3);
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
