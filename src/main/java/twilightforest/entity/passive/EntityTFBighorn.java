package twilightforest.entity.passive;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

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
			map.put(color, TwilightForestMod.prefix("entities/bighorn_sheep/" + color.getName()));
		}
		COLORED_LOOT_TABLES = Collections.unmodifiableMap(map);
	}

	public EntityTFBighorn(World world) {
		super(world);
		setSize(0.9F, 1.3F);
	}

	public EntityTFBighorn(World world, double x, double y, double z) {
		this(world);
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
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT dataTag) {
        livingdata = super.onInitialSpawn(worldIn, difficulty, reason, livingdata, dataTag);
        this.setFleeceColor(getRandomFleeceColor(this.world.rand));
        return livingdata;
    }

    @Override
	public SheepEntity createChild(AgeableEntity ageable) {
		EntityTFBighorn otherParent = (EntityTFBighorn) ageable;
		EntityTFBighorn babySheep = new EntityTFBighorn(world);
		babySheep.setFleeceColor(getDyeColorMixFromParents(this, otherParent));
		return babySheep;
	}
}
