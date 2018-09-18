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
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EntityTFSquirrel extends EntityCreature implements IAnimals {

	public EntityTFSquirrel(World world) {
		super(world);
		this.setSize(0.3F, 0.7F);

		// maybe this will help them move cuter?
		this.stepHeight = 1;
	}

	@Override
	protected void initEntityAI() {
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.38F));
		this.tasks.addTask(2, new EntityAITempt(this, 1.0F, Items.WHEAT_SEEDS, true));
		this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 2.0F, 0.8F, 1.4F));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0F));
		this.tasks.addTask(6, new EntityAIWander(this, 1.25F));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}

	/**
	 * TODO: maybe they should just take less damage?
	 */
	@Override
	public void fall(float distance, float multiplier) {
	}

	@Override
	public float getRenderSizeModifier() {
		return 0.3F;
	}

	@Override
	public float getBlockPathWeight(BlockPos pos) {
		// prefer standing on leaves
		Material underMaterial = this.world.getBlockState(pos.down()).getMaterial();
		if (underMaterial == Material.LEAVES) {
			return 12.0F;
		}
		if (underMaterial == Material.WOOD) {
			return 15.0F;
		}
		if (underMaterial == Material.GRASS) {
			return 10.0F;
		}
		// default to just prefering lighter areas
		return this.world.getLightBrightness(pos) - 0.5F;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

}
