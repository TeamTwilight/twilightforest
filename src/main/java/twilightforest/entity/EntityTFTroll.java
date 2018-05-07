package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
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
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/troll");
	private static final DataParameter<Boolean> ROCK_FLAG = EntityDataManager.createKey(EntityTFTroll.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ROCK_MODIFIER = new AttributeModifier("Rock follow boost", 24, 0).setSaved(false);

	private EntityAIAttackRanged aiArrowAttack;
	private EntityAIAttackMelee aiAttackOnCollide;

	public EntityTFTroll(World par1World) {
		super(par1World);
		this.setSize(1.4F, 2.4F);
	}

	@Override
	public void initEntityAI() {
		aiArrowAttack = new EntityAIAttackRanged(this, 1.0D, 20, 60, 15.0F);
		aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false);

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));

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
	public boolean attackEntityAsMob(Entity par1Entity) {
		swingArm(EnumHand.MAIN_HAND);
		return super.attackEntityAsMob(par1Entity);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("HasRock", this.hasRock());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setHasRock(par1NBTTagCompound.getBoolean("HasRock"));
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

	private void makeTrollStoneInAABB(AxisAlignedBB par1AxisAlignedBB) {
		int minX = MathHelper.ceil(par1AxisAlignedBB.minX);
		int minY = MathHelper.ceil(par1AxisAlignedBB.minY);
		int minZ = MathHelper.ceil(par1AxisAlignedBB.minZ);
		int maxX = MathHelper.floor(par1AxisAlignedBB.maxX);
		int maxY = MathHelper.floor(par1AxisAlignedBB.maxY);
		int maxZ = MathHelper.floor(par1AxisAlignedBB.maxZ);

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
	public void attackEntityWithRangedAttack(EntityLivingBase target, float par2) {
		if (this.hasRock()) {
			EntityTFIceBomb ice = new EntityTFIceBomb(this.world, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.posX - this.posX;
			double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - ice.posY;
			double d2 = target.posZ - this.posZ;
			double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			ice.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));

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
