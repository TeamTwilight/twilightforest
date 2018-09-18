package twilightforest.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;


public class EntityTFBunny extends EntityCreature implements IAnimals {

	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/bunny");
	private static final DataParameter<Byte> DATA_TYPE = EntityDataManager.createKey(EntityTFBunny.class, DataSerializers.BYTE);

	public EntityTFBunny(World world) {
		super(world);
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
		this.tasks.addTask(2, new EntityAITempt(this, 1.0F, Items.CARROT, true));
		this.tasks.addTask(2, new EntityAITempt(this, 1.0F, Items.GOLDEN_CARROT, true));
		this.tasks.addTask(2, new EntityAITempt(this, 1.0F, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), true));
		this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 2.0F, 0.8F, 1.33F));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8F));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0F));
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
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("BunnyType", this.getBunnyType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setBunnyType(compound.getInteger("BunnyType"));
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	public int getBunnyType() {
		return dataManager.get(DATA_TYPE);
	}

	public void setBunnyType(int type) {
		dataManager.set(DATA_TYPE, (byte) type);
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.4F;
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

}
