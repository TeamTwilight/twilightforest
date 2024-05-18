package twilightforest.entity.passive;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFDataSerializers;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.TinyBirdVariants;

import java.util.Optional;

public class TinyBird extends FlyingBird implements VariantHolder<Holder<TinyBirdVariant>> {

	private static final EntityDataAccessor<Holder<TinyBirdVariant>> VARIANT = SynchedEntityData.defineId(TinyBird.class, TFDataSerializers.TINY_BIRD_VARIANT.get());

	public TinyBird(EntityType<? extends TinyBird> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 1.0D, 1.25D));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Ocelot.class, 8.0F, 1.0D, 1.25D));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, this.registryAccess().registryOrThrow(TFRegistries.Keys.TINY_BIRD_VARIANT).getHolderOrThrow(TinyBirdVariants.RED));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return FlyingBird.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 4.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.2D)
			.add(Attributes.STEP_HEIGHT, 1.0D);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("variant", this.getVariant().unwrapKey().orElse(TinyBirdVariants.RED).location().toString());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		Optional.ofNullable(ResourceLocation.tryParse(compound.getString("variant")))
			.map(location -> ResourceKey.create(TFRegistries.Keys.TINY_BIRD_VARIANT, location))
			.flatMap(key -> this.registryAccess().registryOrThrow(TFRegistries.Keys.TINY_BIRD_VARIANT).getHolder(key))
			.ifPresent(this::setVariant);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(accessor, difficulty, type, data);
		this.setVariant(TinyBirdVariant.getVariant(accessor.registryAccess(), accessor.getBiome(this.blockPosition()), this.getRandom()));
		return data;
	}

	@Override
	public Holder<TinyBirdVariant> getVariant() {
		return this.getEntityData().get(VARIANT);
	}

	@Override
	public void setVariant(Holder<TinyBirdVariant> variant) {
		this.getEntityData().set(VARIANT, variant);
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
	public boolean isSpooked() {
		if (this.getLastHurtByMob() != null) return true;
		Player closestPlayer = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 4.0D, true);
		return closestPlayer != null && closestPlayer.isHolding(stack -> stack.is(this.getTemptItems()));
	}

	@Override
	public TagKey<Item> getTemptItems() {
		return ItemTagGenerator.TINY_BIRD_TEMPT_ITEMS;
	}
}
