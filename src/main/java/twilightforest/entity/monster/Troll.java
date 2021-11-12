package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.util.WorldUtil;

import java.util.Random;

public class Troll extends Monster implements RangedAttackMob {

	private static final EntityDataAccessor<Boolean> ROCK_FLAG = SynchedEntityData.defineId(Troll.class, EntityDataSerializers.BOOLEAN);
	private static final AttributeModifier ROCK_MODIFIER = new AttributeModifier("Rock follow boost", 24, AttributeModifier.Operation.ADDITION);

	private RangedAttackGoal aiArrowAttack;
	private MeleeAttackGoal aiAttackOnCollide;

	public Troll(EntityType<? extends Troll> type, Level world) {
		super(type, world);
	}

	@Override
	public void registerGoals() {
		aiArrowAttack = new RangedAttackGoal(this, 1.0D, 20, 60, 15.0F);
		aiAttackOnCollide = new MeleeAttackGoal(this, 1.2D, false);

		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));

		if (level != null && !level.isClientSide) {
			this.setCombatTask();
		}
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D)
				.add(Attributes.ATTACK_DAMAGE, 7.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ROCK_FLAG, false);
	}

	public boolean hasRock() {
		return entityData.get(ROCK_FLAG);
	}

	public void setHasRock(boolean rock) {
		entityData.set(ROCK_FLAG, rock);

		if (!level.isClientSide) {
			if (rock) {
				if (!getAttribute(Attributes.FOLLOW_RANGE).hasModifier(ROCK_MODIFIER)) {
					this.getAttribute(Attributes.FOLLOW_RANGE).addTransientModifier(ROCK_MODIFIER);
				}
			} else {
				this.getAttribute(Attributes.FOLLOW_RANGE).removeModifier(ROCK_MODIFIER);
			}
			this.setCombatTask();
		}
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		swing(InteractionHand.MAIN_HAND);
		return super.doHurtTarget(entity);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("HasRock", this.hasRock());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
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
	protected void tickDeath() {
		super.tickDeath();

		if (this.deathTime % 5 == 0) {
			this.ripenTrollBerNearby(this.deathTime / 5);
		}
	}

	private void ripenTrollBerNearby(int offset) {
		int range = 12;
		for (BlockPos pos : WorldUtil.getAllAround(new BlockPos(this.blockPosition()), range)) {
			ripenBer(offset, pos);
		}
	}

	private void ripenBer(int offset, BlockPos pos) {
		if (this.level.getBlockState(pos).getBlock() == TFBlocks.UNRIPE_TROLLBER.get() && this.random.nextBoolean() && (Math.abs(pos.getX() + pos.getY() + pos.getZ()) % 5 == offset)) {
			this.level.setBlockAndUpdate(pos, TFBlocks.TROLLBER.get().defaultBlockState());
			level.levelEvent(2004, pos, 0);
		}
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (this.hasRock()) {
			IceBomb ice = new IceBomb(TFEntities.THROWN_ICE, this.level, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.getX() - this.getX();
			double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - ice.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
			ice.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 14 - this.level.getDifficulty().getId() * 4);

			this.playSound(TFSounds.ICEBOMB_FIRED, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level.addFreshEntity(ice);
		}
	}

	public static boolean canSpawn(EntityType<? extends Troll> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
		BlockPos blockpos = pos.below();
		return  world.getDifficulty() != Difficulty.PEACEFUL &&
				world.getBlockState(blockpos).getBlock() != TFBlocks.GIANT_OBSIDIAN.get() &&
				!world.canSeeSky(pos);
	}
}
