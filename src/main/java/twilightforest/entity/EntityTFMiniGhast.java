package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFMiniGhast extends EntityTFTowerGhast {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/mini_ghast");
	private boolean isMinion = false;

	public EntityTFMiniGhast(World world) {
		super(world);
		this.setSize(1.1F, 1.5F);
		this.wanderFactor = 4.0F;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 16;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.isMinion ? 6 : 10);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
	}

	@Override
	public float getEyeHeight() {
		return 1.2F;
	}

	// Loosely based on EntityEnderman.shouldAttackPlayer
	@Override
	protected boolean shouldAttack(EntityLivingBase living) {
		ItemStack helmet = living.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		if (!helmet.isEmpty() && helmet.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
			return false;
		} else if (living.getDistance(this) <= 3.5F) {
			return living.canEntityBeSeen(this);
		} else {
			Vec3d vec3d = living.getLook(1.0F).normalize();
			Vec3d vec3d1 = new Vec3d(this.posX - living.posX, this.getEntityBoundingBox().minY + (double) this.getEyeHeight() - (living.posY + (double) living.getEyeHeight()), this.posZ - living.posZ);
			double d0 = vec3d1.length();
			vec3d1 = vec3d1.normalize();
			double d1 = vec3d.dotProduct(vec3d1);
			return d1 > 1.0D - 0.025D / d0 ? living.canEntityBeSeen(this) : false;
		}
	}

	@Override
	protected boolean isValidLightLevel() {
		if (isMinion) {
			return true;
		}

		// [VanillaCopy] EntityMob.isValidLightLevel
		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
			return false;
		} else {
			int i = this.world.getLightFromNeighbors(blockpos);

			if (this.world.isThundering()) {
				int j = this.world.getSkylightSubtracted();
				this.world.setSkylightSubtracted(10);
				i = this.world.getLightFromNeighbors(blockpos);
				this.world.setSkylightSubtracted(j);
			}

			return i <= this.rand.nextInt(8);
		}
	}

	public void makeBossMinion() {
		this.wanderFactor = 0.005F;
		this.isMinion = true;

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6);
		this.setHealth(6);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	public boolean isMinion() {
		return isMinion;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setBoolean("isMinion", this.isMinion);
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.getBoolean("isMinion")) {
			makeBossMinion();
		}
	}

}
