package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
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

public class DwarfRabbit extends Animal implements VariantHolder<DwarfRabbitVariant> {

	private static final EntityDataAccessor<DwarfRabbitVariant> VARIANT = SynchedEntityData.defineId(DwarfRabbit.class, TFDataSerializers.DWARF_RABBIT_VARIANT.get());

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
				.add(Attributes.MOVEMENT_SPEED, 0.3D);
	}

	@Override
	public float getStepHeight() {
		return 1.0F;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
		DwarfRabbit dwarf = TFEntities.DWARF_RABBIT.get().create(level);
		DwarfRabbitVariant variant = DwarfRabbitVariant.getRandomVariant(this.getRandom());
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
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(VARIANT, DwarfRabbitVariants.BROWN.get());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("variant", TFRegistries.DWARF_RABBIT_VARIANT.getKey(this.getVariant()).toString());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		DwarfRabbitVariant variant = TFRegistries.DWARF_RABBIT_VARIANT.get(ResourceLocation.tryParse(compound.getString("variant")));
		if (variant != null) {
			this.setVariant(variant);
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		this.setVariant(DwarfRabbitVariant.getRandomVariant(this.getRandom()));
		return data;
	}

	@Override
	public DwarfRabbitVariant getVariant() {
		return this.getEntityData().get(VARIANT);
	}

	@Override
	public void setVariant(DwarfRabbitVariant variant) {
		this.getEntityData().set(VARIANT, variant);
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getBbHeight() * 0.5F;
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
