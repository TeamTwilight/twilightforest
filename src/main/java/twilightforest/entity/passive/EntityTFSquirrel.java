package twilightforest.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFTempt;

public class EntityTFSquirrel extends EntityCreature implements IAnimals {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/squirrel");
	protected static final Ingredient SEEDS = Ingredient.fromItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);

	public EntityTFSquirrel(World world) {
		super(world);
		this.setSize(0.3F, 0.5F);

		// maybe this will help them move cuter?
		this.stepHeight = 1;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.38F));
		this.tasks.addTask(2, new EntityAITFTempt(this, 1.0F, true, SEEDS));
		this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 2.0F, 0.8F, 1.4F));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0F));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.25F));
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
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.7F;
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
