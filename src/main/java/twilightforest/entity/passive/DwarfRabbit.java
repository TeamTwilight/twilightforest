package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFDataSerializers;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.DwarfRabbitVariants;

import java.util.Optional;

public class DwarfRabbit extends Animal implements VariantHolder<Holder<DwarfRabbitVariant>> {

	private static final EntityDataAccessor<Holder<DwarfRabbitVariant>> VARIANT = SynchedEntityData.defineId(DwarfRabbit.class, TFDataSerializers.DWARF_RABBIT_VARIANT.get());

	public DwarfRabbit(EntityType<? extends DwarfRabbit> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0F));
		this.goalSelector.addGoal(2, new BreedGoal(this, 0.8D));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0F, Ingredient.of(ItemTagGenerator.DWARF_RABBIT_TEMPT_ITEMS), false));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 2.0F, 0.8F, 1.33F));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Ocelot.class, 8.0F, 0.8F, 1.1F));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Cat.class, 8.0F, 0.8F, 1.1F));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 0.8F, 1.1F));
		this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Fox.class, 8.0F, 0.8F, 1.1F));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8F));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 3.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.3D)
			.add(Attributes.STEP_HEIGHT, 1.0D);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
		DwarfRabbit dwarf = TFEntities.DWARF_RABBIT.get().create(level);
		Holder<DwarfRabbitVariant> variant = DwarfRabbitVariant.getRandomCommonVariant(level.registryAccess(), level.getRandom());
		if (dwarf != null && mob instanceof DwarfRabbit parent) {
			if (this.getRandom().nextInt(20) != 0) {
				if (this.getRandom().nextBoolean()) {
					variant = this.getVariant();
				} else {
					variant = parent.getVariant();
				}
			}
			dwarf.setVariant(variant);
		}

		return dwarf;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, this.registryAccess().registryOrThrow(TFRegistries.Keys.DWARF_RABBIT_VARIANT).getHolderOrThrow(DwarfRabbitVariants.BROWN));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("variant", this.getVariant().unwrapKey().orElse(DwarfRabbitVariants.BROWN).location().toString());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		Optional.ofNullable(ResourceLocation.tryParse(compound.getString("variant")))
			.map(location -> ResourceKey.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, location))
			.flatMap(key -> this.registryAccess().registryOrThrow(TFRegistries.Keys.DWARF_RABBIT_VARIANT).getHolder(key))
			.ifPresent(this::setVariant);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(accessor, difficulty, type, data);
		this.setVariant(DwarfRabbitVariant.getVariant(accessor.registryAccess(), accessor.getBiome(this.blockPosition()), this.getRandom()));
		return data;
	}

	@Override
	public Holder<DwarfRabbitVariant> getVariant() {
		return this.getEntityData().get(VARIANT);
	}

	@Override
	public void setVariant(Holder<DwarfRabbitVariant> variant) {
		this.getEntityData().set(VARIANT, variant);
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos) {
		// avoid leaves & wood
		BlockState underMaterial = this.level().getBlockState(pos.below());
		if (underMaterial.is(BlockTags.LEAVES) || underMaterial.is(BlockTags.LOGS)) {
			return -1.0F;
		}
		if (underMaterial.is(BlockTags.DIRT)) {
			return 10.0F;
		}
		// default to just prefering lighter areas
		return this.level().getMaxLocalRawBrightness(pos) - 0.5F;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(ItemTagGenerator.DWARF_RABBIT_TEMPT_ITEMS);
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.DWARF_RABBIT_DEATH.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.DWARF_RABBIT_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.DWARF_RABBIT_AMBIENT.get();
	}
}
