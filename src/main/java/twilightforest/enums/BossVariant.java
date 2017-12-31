package twilightforest.enums;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;
import twilightforest.tileentity.TileEntityTFAlphaYetiSpawner;
import twilightforest.tileentity.TileEntityTFBossSpawner;
import twilightforest.tileentity.TileEntityTFHydraSpawner;
import twilightforest.tileentity.TileEntityTFKnightPhantomsSpawner;
import twilightforest.tileentity.TileEntityTFLichSpawner;
import twilightforest.tileentity.TileEntityTFMinoshroomSpawner;
import twilightforest.tileentity.TileEntityTFNagaSpawner;
import twilightforest.tileentity.TileEntityTFSnowQueenSpawner;
import twilightforest.tileentity.TileEntityTFTowerBossSpawner;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum BossVariant implements IStringSerializable {
	NAGA          (TrophyType.GOLD    , TileEntityTFNagaSpawner.class),
	LICH          (TrophyType.GOLD    , TileEntityTFLichSpawner.class),
	HYDRA         (TrophyType.GOLD    , TileEntityTFHydraSpawner.class),
	UR_GHAST      (TrophyType.GOLD    , TileEntityTFTowerBossSpawner.class),
	KNIGHT_PHANTOM(TrophyType.IRON    , TileEntityTFKnightPhantomsSpawner.class),
	SNOW_QUEEN    (TrophyType.GOLD    , TileEntityTFSnowQueenSpawner.class),
	MINOSHROOM    (TrophyType.IRON    , TileEntityTFMinoshroomSpawner.class),
	ALPHA_YETI    (TrophyType.IRON    , TileEntityTFAlphaYetiSpawner.class),
	QUEST_RAM     (TrophyType.IRONWOOD, null);

	private final Class<? extends TileEntityTFBossSpawner> spawnerClass;
	private final TrophyType trophyType;

	public static final BossVariant[] VARIANTS = values();

	BossVariant(TrophyType trophyType, @Nullable Class<? extends TileEntityTFBossSpawner> spawnerClass) {
		this.spawnerClass = spawnerClass;
		this.trophyType = trophyType;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public TrophyType getTrophyType() {
		return this.trophyType;
	}

	@Nullable
	public Class<? extends TileEntityTFBossSpawner> getSpawnerClass() {
		return spawnerClass;
	}

	public static BossVariant getVariant(int id) {
		return id >= 0 && id < VARIANTS.length ? VARIANTS[id] : NAGA;
	}

	public enum TrophyType {
		GOLD("trophy"),
		IRON("trophy_minor"),
		IRONWOOD("trophy_quest");

		private final String modelName;

		TrophyType(String modelName) {
			this.modelName = modelName;
		}

		public String getModelName() {
			return this.modelName;
		}
	}
}
