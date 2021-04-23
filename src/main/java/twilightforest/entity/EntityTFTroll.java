package twilightforest.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.entity.boss.EntityTFIceBomb;
import twilightforest.util.WorldUtil;

import java.util.Random;

public class EntityTFTroll extends MonsterEntity implements IRangedAttackMob {

	private static final DataParameter<Boolean> ROCK_FLAG = EntityDataManager.createKey(EntityTFTroll.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ROCK_MODIFIER = new AttributeModifier("Rock follow boost", 24, AttributeModifier.Operation.ADDITION);

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

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.28D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D);
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
				if (!getAttribute(Attributes.FOLLOW_RANGE).hasModifier(ROCK_MODIFIER)) {
					this.getAttribute(Attributes.FOLLOW_RANGE).applyNonPersistentModifier(ROCK_MODIFIER);
				}
			} else {
				this.getAttribute(Attributes.FOLLOW_RANGE).removeModifier(ROCK_MODIFIER);
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
	}

	private void ripenTrollBerNearby(int offset) {
		int range = 12;
		for (BlockPos pos : WorldUtil.getAllAround(new BlockPos(this.getPosition()), range)) {
			ripenBer(offset, pos);
		}
	}

	private void ripenBer(int offset, BlockPos pos) {
		if (this.world.getBlockState(pos).getBlock() == TFBlocks.unripe_trollber.get() && this.rand.nextBoolean() && (Math.abs(pos.getX() + pos.getY() + pos.getZ()) % 5 == offset)) {
			this.world.setBlockState(pos, TFBlocks.trollber.get().getDefaultState());
			world.playEvent(2004, pos, 0);
		}
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
		if (this.hasRock()) {
			EntityTFIceBomb ice = new EntityTFIceBomb(TFEntities.thrown_ice, this.world, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.getPosX() - this.getPosX();
			double d1 = target.getBoundingBox().minY + target.getHeight() / 3.0F - ice.getPosY();
			double d2 = target.getPosZ() - this.getPosZ();
			double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
			ice.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 14 - this.world.getDifficulty().getId() * 4);

			this.playSound(TFSounds.ICEBOMB_FIRED, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.addEntity(ice);
		}
	}

	public static boolean canSpawn(EntityType<? extends EntityTFTroll> type, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		BlockPos blockpos = pos.down();
		return !(world.getBlockState(blockpos).getBlock() == TFBlocks.giant_obsidian.get()) && !world.canSeeSky(pos) && pos.getY() < 60;
	}
}
