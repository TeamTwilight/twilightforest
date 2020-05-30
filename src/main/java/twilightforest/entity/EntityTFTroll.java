package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;
import twilightforest.entity.boss.EntityTFIceBomb;
import twilightforest.util.WorldUtil;

public class EntityTFTroll extends MonsterEntity implements IRangedAttackMob {

	private static final DataParameter<Boolean> ROCK_FLAG = EntityDataManager.createKey(EntityTFTroll.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ROCK_MODIFIER = new AttributeModifier("Rock follow boost", 24, AttributeModifier.Operation.ADDITION).setSaved(false);

	private RangedAttackGoal aiArrowAttack;
	private MeleeAttackGoal aiAttackOnCollide;

	public EntityTFTroll(EntityType<? extends EntityTFTroll> type, World world) {
		super(type, world);
	}

	@Override
	public void registerGoals() {
		aiArrowAttack = new RangedAttackGoal(this, 1.0D, 20, 60, 15.0F);
		aiAttackOnCollide = new MeleeAttackGoal(this, 1.2D, false);

		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));

		if (world != null && !world.isRemote) {
			this.setCombatTask();
		}
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(ROCK_FLAG, false);
	}

	public boolean hasRock() {
		return dataManager.get(ROCK_FLAG);
	}

	public void setHasRock(boolean rock) {
		dataManager.set(ROCK_FLAG, rock);

		if (!world.isRemote) {
			if (rock) {
				if (!getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).hasModifier(ROCK_MODIFIER)) {
					this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(ROCK_MODIFIER);
				}
			} else {
				this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(ROCK_MODIFIER);
			}
			this.setCombatTask();
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		swingArm(Hand.MAIN_HAND);
		return super.attackEntityAsMob(entity);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("HasRock", this.hasRock());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setHasRock(compound.getBoolean("HasRock"));
	}

	private void setCombatTask() {
		this.goalSelector.removeGoal(this.aiAttackOnCollide);
		this.goalSelector.removeGoal(this.aiArrowAttack);

		if (this.hasRock()) {
			this.goalSelector.addGoal(4, this.aiArrowAttack);
		} else {
			this.goalSelector.addGoal(4, this.aiAttackOnCollide);
		}
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();

		if (this.deathTime % 5 == 0) {
			this.ripenTrollBerNearby(this.deathTime / 5);
		}

		if (this.deathTime == 1) {
			//this.makeTrollStoneInAABB(this.boundingBox);
		}
	}

	private void ripenTrollBerNearby(int offset) {
		int range = 12;
		for (BlockPos pos : WorldUtil.getAllAround(new BlockPos(this), range)) {
			ripenBer(offset, pos);
		}
	}

	private void ripenBer(int offset, BlockPos pos) {
		if (this.world.getBlockState(pos).getBlock() == TFBlocks.unripe_trollber.get() && this.rand.nextBoolean() && (Math.abs(pos.getX() + pos.getY() + pos.getZ()) % 5 == offset)) {
			this.world.setBlockState(pos, TFBlocks.trollber.get().getDefaultState());
			world.playEvent(2004, pos, 0);
		}
	}

	private void makeTrollStoneInAABB(AxisAlignedBB aabb) {
		int minX = MathHelper.ceil(aabb.minX);
		int minY = MathHelper.ceil(aabb.minY);
		int minZ = MathHelper.ceil(aabb.minZ);
		int maxX = MathHelper.floor(aabb.maxX);
		int maxY = MathHelper.floor(aabb.maxY);
		int maxZ = MathHelper.floor(aabb.maxZ);

		for (BlockPos pos : BlockPos.getAllInBoxMutable(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
			if (world.isAirBlock(pos)) {
				world.setBlockState(pos, TFBlocks.trollsteinn.get().getDefaultState());
				world.playEvent(2001, pos, Block.getStateId(TFBlocks.trollsteinn.get().getDefaultState()));
			}
		}
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
		if (this.hasRock()) {
			EntityTFIceBomb ice = new EntityTFIceBomb(TFEntities.thrown_ice.get(), this.world, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.getX() - this.getX();
			double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - ice.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			ice.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));

			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.addEntity(ice);
		}
	}

	// [VanillaCopy] super but hardcode swing progress to ignore potions
	@Override
	protected void updateArmSwingProgress() {
		int i = 6;

		if (this.isSwingInProgress) {
			++this.swingProgressInt;

			if (this.swingProgressInt >= i) {
				this.swingProgressInt = 0;
				this.isSwingInProgress = false;
			}
		} else {
			this.swingProgressInt = 0;
		}

		this.swingProgress = (float) this.swingProgressInt / (float) i;
	}
}
