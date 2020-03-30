package twilightforest.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;

// TODO: See Bunny
public class EntityTFSquirrel extends AnimalEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/squirrel");
	protected static final Ingredient SEEDS = Ingredient.fromItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);

	public EntityTFSquirrel(EntityType<? extends EntityTFSquirrel> type, World world) {
		super(type, world);

		// maybe this will help them move cuter?
		this.stepHeight = 1;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.38F));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0F, true, SEEDS));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PlayerEntity.class, 2.0F, 0.8F, 1.4F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.25F));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}

	/**
	 * TODO: maybe they should just take less damage?
	 */
	@Override
	public boolean handleFallDamage(float distance, float multiplier) {
		return false;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.7F;
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
		if (underMaterial == Material.ORGANIC) {
			return 10.0F;
		}
		// default to just prefering lighter areas
		return this.world.getLight(pos) - 0.5F;
	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Nullable
	@Override
	public AgeableEntity createChild(AgeableEntity ageableEntity) {
		return null;
	}
}
