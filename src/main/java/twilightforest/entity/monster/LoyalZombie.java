package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class LoyalZombie extends TamableAnimal {

	private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
	private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_ID, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	private static final EntityDimensions BABY_DIMENSIONS = TFEntities.LOYAL_ZOMBIE.get().getDimensions().scale(0.5F).withEyeHeight(0.93F);

	public LoyalZombie(EntityType<? extends LoyalZombie> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Monster.class, true));
	}

	@Override
	public Animal getBreedOffspring(ServerLevel level, AgeableMob animal) {
		return null;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 40.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.3D)
			.add(Attributes.ARMOR, 3.0D);
	}

	@Override
	public boolean doHurtTarget(ServerLevel server, Entity entity) {
		boolean success = entity.hurtServer(server, this.damageSources().mobAttack(this), 7.0F);

		if (success) {
			entity.push(0.0D, 0.2D, 0.0D);
		}

		return success;
	}

	@Override
	public void aiStep() {
		// once our damage boost effect wears out, start to decay
		// the effect here is that we die shortly after our 60 second lifespan
		if (level() instanceof ServerLevel server && getEffect(MobEffects.STRENGTH) == null) {
			if (tickCount % 20 == 0) {
				hurtServer(server, TFDamageTypes.getDamageSource(server, TFDamageTypes.EXPIRED), 2);
			}
		}

		super.aiStep();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 vec3) {
		//feeding a loyal zombie rotten flesh will refresh its death timer, allowing your minions to stick around for longer
		if (this.getOwner() != null && this.getOwner().is(player) && player.getItemInHand(hand).is(Items.ROTTEN_FLESH)) {
			this.removeEffect(MobEffects.STRENGTH);
			this.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 1200, 1));
			this.heal(1.0F);
			this.playSound(SoundEvents.ZOMBIE_INFECT, this.getSoundVolume(), this.getVoicePitch());
			player.getItemInHand(hand).consume(1, player);
			return InteractionResult.SUCCESS;
		}

		return super.interact(player, hand, vec3);
	}

	/**
	 * [VanillaCopy] {@link Wolf#wantsToAttack(LivingEntity, LivingEntity)} ()}, substituting with our class
	 */
	@Override
	public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
		if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
			return switch (target) {
				case LoyalZombie zombie -> !zombie.isTame() || zombie.getOwner() != owner;
				case Player pTarget when owner instanceof Player pOwner && !pOwner.canHarmPlayer(pTarget) -> false;
				case AbstractHorse horse when horse.isTamed() -> false;
				default -> !(target instanceof TamableAnimal animal) || !animal.isTame();
			};
		} else {
			return false;
		}
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !this.isTame();
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.LOYAL_ZOMBIE_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.LOYAL_ZOMBIE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.LOYAL_ZOMBIE_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(TFSounds.LOYAL_ZOMBIE_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	protected void dropExperience(ServerLevel server, @Nullable Entity entity) {

	}

	@Override
	public boolean canAgeUp() {
		return false;
	}

	@Override
	protected void ageBoundaryReached() {
		super.ageBoundaryReached();
		AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed != null) {
			speed.removeModifier(SPEED_MODIFIER_BABY_ID);
			if (isBaby()) {
				speed.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
	}
}
