package twilightforest.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class EntityTFBighorn extends SheepEntity {

	public static final ResourceLocation SHEARED_LOOT_TABLE = TwilightForestMod.prefix("entities/bighorn_sheep/sheared");
	public static final Map<DyeColor, ResourceLocation> COLORED_LOOT_TABLES;

	static {
		Map<DyeColor, ResourceLocation> map = new EnumMap<>(DyeColor.class);
		for (DyeColor color : DyeColor.values()) {
			map.put(color, TwilightForestMod.prefix("entities/bighorn_sheep/" + color.getString()));
		}
		COLORED_LOOT_TABLES = Collections.unmodifiableMap(map);
	}

	public EntityTFBighorn(EntityType<? extends EntityTFBighorn> type, World world) {
		super(type, world);
	}

	public EntityTFBighorn(World world, double x, double y, double z) {
		this(TFEntities.bighorn_sheep, world);
		this.setPosition(x, y, z);
	}

	@Override
	public ResourceLocation getLootTable() {
		return this.getSheared() ? SHEARED_LOOT_TABLE : COLORED_LOOT_TABLES.get(this.getFleeceColor());
	}

	private static DyeColor getRandomFleeceColor(Random random) {
		return random.nextBoolean()
				? DyeColor.BROWN
				: DyeColor.byId(random.nextInt(16));
	}

	@Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT dataTag) {
        livingdata = super.onInitialSpawn(worldIn, difficulty, reason, livingdata, dataTag);
        this.setFleeceColor(getRandomFleeceColor(this.world.rand));
        return livingdata;
    }

    @Override
	public SheepEntity func_241840_a(ServerWorld world, AgeableEntity ageable) {
		if (!(ageable instanceof EntityTFBighorn)) {
			TwilightForestMod.LOGGER.error("Code was called to breed a Bighorn with a non Bighorn! Cancelling!");
			return null;
		}

		EntityTFBighorn otherParent = (EntityTFBighorn) ageable;
		EntityTFBighorn babySheep = TFEntities.bighorn_sheep.create(world);
		babySheep.setFleeceColor(getDyeColorMixFromParents(this, otherParent));
		return babySheep;
	}
    
    @Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.BIGHORN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.BIGHORN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.BIGHORN_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(TFSounds.BIGHORN_STEP, 0.15F, 1.0F);
	}
}
