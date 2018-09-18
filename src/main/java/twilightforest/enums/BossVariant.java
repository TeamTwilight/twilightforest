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
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum BossVariant implements IStringSerializable {
	NAGA          (TrophyType.GOLD    , TileEntityTFNagaSpawner::new),
	LICH          (TrophyType.GOLD    , TileEntityTFLichSpawner::new),
	HYDRA         (TrophyType.GOLD    , TileEntityTFHydraSpawner::new),
	UR_GHAST      (TrophyType.GOLD    , TileEntityTFTowerBossSpawner::new),
	KNIGHT_PHANTOM(TrophyType.IRON    , TileEntityTFKnightPhantomsSpawner::new),
	SNOW_QUEEN    (TrophyType.GOLD    , TileEntityTFSnowQueenSpawner::new),
	MINOSHROOM    (TrophyType.IRON    , TileEntityTFMinoshroomSpawner::new),
	ALPHA_YETI    (TrophyType.IRON    , TileEntityTFAlphaYetiSpawner::new),
	QUEST_RAM     (TrophyType.IRONWOOD, null);

	private final Supplier<? extends TileEntityTFBossSpawner> factory;
	private final TrophyType trophyType;

	public static final BossVariant[] VARIANTS = values();

	BossVariant(TrophyType trophyType, @Nullable Supplier<? extends TileEntityTFBossSpawner> factory) {
		this.factory = factory;
		this.trophyType = trophyType;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public TrophyType getTrophyType() {
		return this.trophyType;
	}

	public boolean hasSpawner() {
		return factory != null;
	}

	@Nullable
	public TileEntityTFBossSpawner getSpawner() {
		return factory != null ? factory.get() : null;
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
