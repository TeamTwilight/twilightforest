package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.RangedAttackGoal;
import net.minecraft.entity.ai.FleeSunGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.RestrictSunGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.entity.boss.EntityTFIceBomb;
import twilightforest.util.WorldUtil;

public class EntityTFTroll extends EntityMob implements IRangedAttackMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/troll");
	private static final DataParameter<Boolean> ROCK_FLAG = EntityDataManager.createKey(EntityTFTroll.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ROCK_MODIFIER = new AttributeModifier("Rock follow boost", 24, 0).setSaved(false);

	private RangedAttackGoal aiArrowAttack;
	private MeleeAttackGoal aiAttackOnCollide;

	public EntityTFTroll(World world) {
		super(world);
		this.setSize(1.4F, 2.4F);
	}

	@Override
	public void registerGoals() {
		aiArrowAttack = new RangedAttackGoal(this, 1.0D, 20, 60, 15.0F);
		aiAttackOnCollide = new MeleeAttackGoal(this, 1.2D, false);

		this.tasks.addTask(1, new SwimGoal(this));
		this.tasks.addTask(2, new RestrictSunGoal(this));
		this.tasks.addTask(3, new FleeSunGoal(this, 1.0D));
		this.tasks.addTask(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(6, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, false));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));

		if (world != null && !world.isRemote) {
			this.setCombatTask();
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(ROCK_FLAG, false);
	}

	public boolean hasRock() {
		return dataManager.get(ROCK_FLAG);
	}

	public void setHasRock(boolean rock) {
		dataManager.set(ROCK_FLAG, rock);

		if (!world.isRemote) {
			if (rock) {
				if (!getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).hasModifier(ROCK_MODIFIER)) {
					this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(ROCK_MODIFIER);
				}
			} else {
				this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(ROCK_MODIFIER);
			}
			this.setCombatTask();
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		swingArm(EnumHand.MAIN_HAND);
		return super.attackEntityAsMob(entity);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("HasRock", this.hasRock());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setHasRock(compound.getBoolean("HasRock"));
	}

	private void setCombatTask() {
		this.tasks.removeTask(this.aiAttackOnCollide);
		this.tasks.removeTask(this.aiArrowAttack);

		if (this.hasRock()) {
			this.tasks.addTask(4, this.aiArrowAttack);
		} else {
			this.tasks.addTask(4, this.aiAttackOnCollide);
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
		if (this.world.getBlockState(pos).getBlock() == TFBlocks.unripe_trollber && this.rand.nextBoolean() && (Math.abs(pos.getX() + pos.getY() + pos.getZ()) % 5 == offset)) {
			this.world.setBlockState(pos, TFBlocks.trollber.getDefaultState());
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

		for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
			if (world.isAirBlock(pos)) {
				world.setBlockState(pos, TFBlocks.trollsteinn.getDefaultState());
				world.playEvent(2001, pos, Block.getStateId(TFBlocks.trollsteinn.getDefaultState()));
			}
		}
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if (this.hasRock()) {
			EntityTFIceBomb ice = new EntityTFIceBomb(this.world, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.posX - this.posX;
			double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - ice.posY;
			double d2 = target.posZ - this.posZ;
			double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			ice.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));

			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.spawnEntity(ice);
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {} // todo 1.12

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
