package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.tags.TFItemTags;

public class Deer extends Animal {

	public Deer(EntityType<? extends Deer> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(3, new TemptGoal(this, 1.25D, i -> i.is(TFItemTags.DEER_TEMPT_ITEMS), false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.5D, 1.8D));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Animal.createAnimalAttributes()
			.add(Attributes.MAX_HEALTH, 10.0)
			.add(Attributes.MOVEMENT_SPEED, 0.2);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		InteractionResult result = super.mobInteract(player, hand);
		ItemStack stack = player.getItemInHand(hand);
		if (result == InteractionResult.PASS && stack.is(TFItems.SHIKA_SENBEI) && this.getHealth() < this.getMaxHealth()) {
			this.usePlayerItem(player, hand, stack);
			return InteractionResult.SUCCESS;
		}

		return result;
	}

	@Override
	public void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
		if (stack.is(TFItems.SHIKA_SENBEI)) {
			if (!this.level().isClientSide())
				this.heal(4.0F);
			this.level().playSound(null, getX(), getY(), getZ(), TFSounds.DEER_EAT.get(), this.getSoundSource(), 1, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
		}
		super.usePlayerItem(player, hand, stack);
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
		return TFEntities.DEER.get().create(level, EntitySpawnReason.BREEDING);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(TFItemTags.DEER_TEMPT_ITEMS);
	}
}
