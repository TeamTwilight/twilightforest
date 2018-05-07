package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFTowerWood;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerWoodVariant;

import java.util.Random;

public class EntityTFTowerTermite extends EntityMob {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/tower_termite");
	private AISummonSilverfish summonSilverfish;

	public EntityTFTowerTermite(World par1World) {
		super(par1World);
		this.setSize(0.3F, 0.7F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.summonSilverfish = new AISummonSilverfish(this));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(4, new AIHideInStone(this));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(8.0D);
	}

	@Override
	protected boolean canTriggerWalking() {
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
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else {
			if ((source instanceof EntityDamageSource || source == DamageSource.MAGIC) && this.summonSilverfish != null) {
				this.summonSilverfish.notifyHurt();
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	protected void playStepSound(BlockPos pos, Block par4) {
		this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void onUpdate() {
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	// [VanillaCopy] EntitySilverfish$AIHideInStone. Changes noted
	private static class AIHideInStone extends EntityAIWander {
		private final EntityTFTowerTermite silverfish; // TF - type change
		private EnumFacing facing;
		private boolean doMerge;

		public AIHideInStone(EntityTFTowerTermite silverfishIn) {
			super(silverfishIn, 1.0D, 10);
			this.silverfish = silverfishIn;
			this.setMutexBits(1);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute() {
			if (!this.silverfish.world.getGameRules().getBoolean("mobGriefing")) {
				return false;
			} else if (this.silverfish.getAttackTarget() != null) {
				return false;
			} else if (!this.silverfish.getNavigator().noPath()) {
				return false;
			} else {
				Random random = this.silverfish.getRNG();

				if (random.nextInt(10) == 0) {
					this.facing = EnumFacing.random(random);
					BlockPos blockpos = (new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5D, this.silverfish.posZ)).offset(this.facing);
					IBlockState iblockstate = this.silverfish.world.getBlockState(blockpos);

					if (iblockstate == TFBlocks.tower_wood.getDefaultState()) // TF - Change block check
					{
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
		public boolean shouldContinueExecuting() {
			return this.doMerge ? false : super.shouldContinueExecuting();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			if (!this.doMerge) {
				super.startExecuting();
			} else {
				World world = this.silverfish.world;
				BlockPos blockpos = (new BlockPos(this.silverfish.posX, this.silverfish.posY + 0.5D, this.silverfish.posZ)).offset(this.facing);
				IBlockState iblockstate = world.getBlockState(blockpos);

				if (iblockstate == TFBlocks.tower_wood.getDefaultState()) // TF - Change block check
				{
					// TF - Change block type
					world.setBlockState(blockpos, TFBlocks.tower_wood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.INFESTED), 3);
					this.silverfish.spawnExplosionParticle();
					this.silverfish.setDead();
				}
			}
		}
	}

	// [VanillaCopy] of EntitySilverfish$AISummonSilverfish. Changes noted
	private static class AISummonSilverfish extends EntityAIBase {
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
		public boolean shouldExecute() {
			return this.lookForFriends > 0;
		}

		/**
		 * Updates the task
		 */
		public void updateTask() {
			--this.lookForFriends;

			if (this.lookForFriends <= 0) {
				World world = this.silverfish.world;
				Random random = this.silverfish.getRNG();
				BlockPos blockpos = new BlockPos(this.silverfish);

				for (int i = 0; i <= 5 && i >= -5; i = i <= 0 ? 1 - i : 0 - i) {
					for (int j = 0; j <= 10 && j >= -10; j = j <= 0 ? 1 - j : 0 - j) {
						for (int k = 0; k <= 10 && k >= -10; k = k <= 0 ? 1 - k : 0 - k) {
							BlockPos blockpos1 = blockpos.add(j, i, k);
							IBlockState iblockstate = world.getBlockState(blockpos1);

							if (iblockstate == TFBlocks.tower_wood.getDefaultState().withProperty(BlockTFTowerWood.VARIANT, TowerWoodVariant.INFESTED)) // TF - Change block check
							{
								if (world.getGameRules().getBoolean("mobGriefing")) {
									world.destroyBlock(blockpos1, true);
								} else {
									// TF - reset to normal tower wood
									world.setBlockState(blockpos1, TFBlocks.tower_wood.getDefaultState(), 3);
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
