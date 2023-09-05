package twilightforest.entity.passive;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TinyBirdVariant;

public class TinyBird extends FlyingBird {

	private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(TinyBird.class, EntityDataSerializers.STRING);

	public TinyBird(EntityType<? extends TinyBird> type, Level level) {
		super(type, level);
		this.setBirdType(TinyBirdVariant.getVariantId(TinyBirdVariant.getRandomVariant(this.getRandom())));
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 1.0D, 1.25D));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Ocelot.class, 8.0F, 1.0D, 1.25D));
	}


	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(TYPE, TinyBirdVariant.getVariantId(TinyBirdVariant.getRandomVariant(this.getRandom())));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return FlyingBird.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("BirdType", TinyBirdVariant.getVariantId(this.getBirdType()));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setBirdType(compound.getString("BirdType"));
	}

	public TinyBirdVariant getBirdType() {
		return TinyBirdVariant.getVariant(this.getEntityData().get(TYPE)).orElse(TinyBirdVariant.BLUE.get());
	}

	public void setBirdType(String type) {
		this.getEntityData().set(TYPE, type);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.getRandom().nextInt(20) == 0 ? TFSounds.TINY_BIRD_SONG.get() : TFSounds.TINY_BIRD_CHIRP.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.TINY_BIRD_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.TINY_BIRD_HURT.get();
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getBbHeight() * 0.7F;
	}

	@Override
	public boolean isSpooked() {
		if (this.getLastHurtByMob() != null) return true;
		Player closestPlayer = this.getLevel().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 4.0D, true);
		return closestPlayer != null
				&& !SEEDS.test(closestPlayer.getMainHandItem())
				&& !SEEDS.test(closestPlayer.getOffhandItem());
	}
}
