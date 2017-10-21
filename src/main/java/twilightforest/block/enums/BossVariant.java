package twilightforest.block.enums;

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
	NAGA          (true , TrophyType.GOLD    , TileEntityTFNagaSpawner.class),
	LICH          (true , TrophyType.GOLD    , TileEntityTFLichSpawner.class),
	HYDRA         (true , TrophyType.GOLD    , TileEntityTFHydraSpawner.class),
	UR_GHAST      (true , TrophyType.GOLD    , TileEntityTFTowerBossSpawner.class),
	KNIGHT_PHANTOM(false, TrophyType.IRON    , TileEntityTFKnightPhantomsSpawner.class),
	SNOW_QUEEN    (true , TrophyType.GOLD    , TileEntityTFSnowQueenSpawner.class),
	MINOSHROOM    (false, TrophyType.IRON    , TileEntityTFMinoshroomSpawner.class),
	ALPHA_YETI    (false, TrophyType.IRON    , TileEntityTFAlphaYetiSpawner.class),
	QUEST_RAM     (false, TrophyType.IRONWOOD, null);

	private final boolean isMajorBoss;
	private final Class<? extends TileEntityTFBossSpawner> spawnerClass;
	private final TrophyType trophyType;

	public static final BossVariant[] VARIANTS = values();

	BossVariant(boolean isMajorBoss, TrophyType trophyType, @Nullable Class<? extends TileEntityTFBossSpawner> spawnerClass) {
		this.isMajorBoss = isMajorBoss;
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

	public boolean usesGoldBackground() {
		return isMajorBoss;
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
