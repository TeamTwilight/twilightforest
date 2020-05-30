package twilightforest.entity.passive;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

// TODO: I feel like using the properly classes could be usueful
public class EntityTFBunny extends AnimalEntity {

	// Loot tables aren't done like this any more.
	private static final DataParameter<Byte> DATA_TYPE = EntityDataManager.createKey(EntityTFBunny.class, DataSerializers.BYTE);

	public EntityTFBunny(EntityType<? extends EntityTFBunny> type, World world) {
		super(type, world);

		// maybe this will help them move cuter?
		this.stepHeight = 1;
		setBunnyType(rand.nextInt(4));
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0F));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0F, Ingredient.fromItems(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION), true));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 2.0F, 0.8F, 1.33F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8F));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0F));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}

	@Nullable
	@Override
	public AgeableEntity createChild(AgeableEntity ageableEntity) {
		return null;
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(DATA_TYPE, (byte) 0);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("BunnyType", this.getBunnyType());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setBunnyType(compound.getInt("BunnyType"));
	}

	public int getBunnyType() {
		return dataManager.get(DATA_TYPE);
	}

	public void setBunnyType(int type) {
		dataManager.set(DATA_TYPE, (byte) type);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.5F;
	}

	//TODO: Moved to renderer?
//	@Override
//	public float getRenderSizeModifier() {
//		return 0.3F;
//	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
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
		if (underMaterial == Material.ORGANIC) {
			return 10.0F;
		}
		// default to just prefering lighter areas
		return this.world.getLight(pos) - 0.5F;
	}

}
