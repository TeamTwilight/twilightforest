package twilightforest.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class EntityTFBighorn extends EntitySheep {

	public static final ResourceLocation SHEARED_LOOT_TABLE = TwilightForestMod.prefix("entities/bighorn_sheep/sheared");
	public static final Map<EnumDyeColor, ResourceLocation> COLORED_LOOT_TABLES;

	static {
		Map<EnumDyeColor, ResourceLocation> map = new EnumMap<>(EnumDyeColor.class);
		for (EnumDyeColor color : EnumDyeColor.values()) {
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

	private static EnumDyeColor getRandomFleeceColor(Random random) {
		return random.nextBoolean()
				? EnumDyeColor.BROWN
				: EnumDyeColor.byMetadata(random.nextInt(16));
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setFleeceColor(getRandomFleeceColor(this.world.rand));
		return livingdata;
	}

	@Override
	public EntitySheep createChild(EntityAgeable ageable) {
		EntityTFBighorn otherParent = (EntityTFBighorn) ageable;
		EntityTFBighorn babySheep = new EntityTFBighorn(world);
		babySheep.setFleeceColor(getDyeColorMixFromParents(this, otherParent));
		return babySheep;
	}
}
