package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.init.custom.DwarfRabbitVariant;

public class DwarfRabbit extends Animal {

	private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(DwarfRabbit.class, EntityDataSerializers.STRING);

	public DwarfRabbit(EntityType<? extends DwarfRabbit> type, Level world) {
		super(type, world);
		this.setBunnyType(DwarfRabbitVariant.getVariantId(DwarfRabbitVariant.getRandomVariant(this.getRandom())));
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0F));
		this.goalSelector.addGoal(2, new BreedGoal(this, 0.8D));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0F, Ingredient.of(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION), false));
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
		if (this.getRandom().nextInt(20) != 0) {
			if (mob instanceof DwarfRabbit rabbit && this.getRandom().nextBoolean()) {
				variant = rabbit.getBunnyType();
			} else {
				variant = this.getBunnyType();
			}
		}

		dwarf.setBunnyType(DwarfRabbitVariant.getVariantId(variant));
		return dwarf;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(TYPE, DwarfRabbitVariant.getVariantId(DwarfRabbitVariant.getRandomVariant(this.getRandom())));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("BunnyType", DwarfRabbitVariant.getVariantId(this.getBunnyType()));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setBunnyType(compound.getString("BunnyType"));
	}

	public DwarfRabbitVariant getBunnyType() {
		return DwarfRabbitVariant.getVariant(this.entityData.get(TYPE)).orElse(DwarfRabbitVariant.BROWN.get());
	}

	public void setBunnyType(String type) {
		this.entityData.set(TYPE, type);
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
		Material underMaterial = this.getLevel().getBlockState(pos.below()).getMaterial();
		if (underMaterial == Material.LEAVES) {
			return -1.0F;
		}
		if (underMaterial == Material.WOOD) {
			return -1.0F;
		}
		if (underMaterial == Material.GRASS) {
			return 10.0F;
		}
		// default to just prefering lighter areas
		return this.getLevel().getMaxLocalRawBrightness(pos) - 0.5F;
	}

	private static boolean isTemptingItem(ItemStack stack) {
		return stack.is(Items.CARROT) || stack.is(Items.GOLDEN_CARROT) || stack.is(Blocks.DANDELION.asItem());
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return isTemptingItem(stack);
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
