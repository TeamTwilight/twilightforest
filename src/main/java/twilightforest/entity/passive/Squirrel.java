package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.ItemTagGenerator;

public class Squirrel extends Animal {

	public Squirrel(EntityType<? extends Squirrel> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.38F));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0F, Ingredient.of(ItemTagGenerator.SQUIRREL_TEMPT_ITEMS), true));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 2.0F, 0.8F, 1.4F));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 0.8F, 1.4F));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 0.8F, 1.4F));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 8.0F, 0.8F, 1.4F));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Fox.class, 8.0F, 0.8F, 1.4F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.25F));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D);
	}

	@Override
	public float getStepHeight() {
		return 1.0F;
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getBbHeight() * 0.7F;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos) {
		// prefer standing on leaves
		BlockState state = this.level().getBlockState(pos.below());
		if (state.is(BlockTags.LEAVES)) {
			return 12.0F;
		}
		if (state.is(BlockTags.LOGS)) {
			return 15.0F;
		}
		if (state.is(BlockTags.DIRT)) {
			return 10.0F;
		}
		// default to just prefering lighter areas
		return this.level().getMaxLocalRawBrightness(pos) - 0.5F;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return null;
	}
}
