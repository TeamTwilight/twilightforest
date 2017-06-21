package twilightforest.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;


public class EntityTFBunny extends EntityCreature implements IAnimals {

	private static final DataParameter<Byte> DATA_TYPE = EntityDataManager.createKey(EntityTFBunny.class, DataSerializers.BYTE);

	public EntityTFBunny(World par1World) {
		super(par1World);
		this.setSize(0.3F, 0.7F);

		// maybe this will help them move cuter?
		this.stepHeight = 1;
		setBunnyType(rand.nextInt(4));
	}

	@Override
	protected void initEntityAI() {
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 2.0F));
		this.tasks.addTask(2, new EntityAITempt(this, 1.0F, Items.WHEAT_SEEDS, true));
		this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 2.0F, 0.8F, 1.33F));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8F));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0F));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_TYPE, (byte) 0);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("BunnyType", this.getBunnyType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setBunnyType(par1NBTTagCompound.getInteger("BunnyType"));
	}

	public int getBunnyType() {
		return dataManager.get(DATA_TYPE);
	}

	public void setBunnyType(int par1) {
		dataManager.set(DATA_TYPE, (byte) par1);
	}

	@Override
	public float getRenderSizeModifier() {
		return 0.3F;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public float getBlockPathWeight(BlockPos pos) {
		// avoid leaves & wood
		Material underMaterial = this.world.getBlockState(pos.down()).getMaterial();
		if (underMaterial == Material.LEAVES) {
			return -1.0F;
		}
		if (underMaterial == Material.WOOD) {
			return -1.0F;
		}
		if (underMaterial == Material.GRASS) {
			return 10.0F;
		}
		// default to just prefering lighter areas
		return this.world.getLightBrightness(pos) - 0.5F;
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer) {
			((EntityPlayer) par1DamageSource.getSourceOfDamage()).addStat(TFAchievementPage.twilightHunter);
		}
	}
}
