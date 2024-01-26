package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class Deer extends Animal {

	public Deer(EntityType<? extends Deer> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(ItemTagGenerator.DEER_TEMPT_ITEMS), false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.5D, 1.8D));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0)
				.add(Attributes.MOVEMENT_SPEED, 0.2);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getBbHeight() * 0.7F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.DEER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.DEER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.DEER_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	@Override
	public Deer getBreedOffspring(ServerLevel level, AgeableMob mate) {
		return TFEntities.DEER.get().create(level);
	}

	@Override
	protected float getStandingEyeHeight(Pose pos, EntityDimensions size) {
		return this.isBaby() ? size.height * 0.95F : 1.65F;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(ItemTagGenerator.DEER_TEMPT_ITEMS);
	}
}
