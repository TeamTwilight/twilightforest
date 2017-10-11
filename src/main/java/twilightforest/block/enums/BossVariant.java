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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum BossVariant implements IStringSerializable {
	NAGA(true, TileEntityTFNagaSpawner.class),
	LICH(true, TileEntityTFLichSpawner.class),
	HYDRA(true, TileEntityTFHydraSpawner.class),
	UR_GHAST(true, TileEntityTFTowerBossSpawner.class),
	KNIGHT_PHANTOM(false, TileEntityTFKnightPhantomsSpawner.class),
	SNOW_QUEEN(true, TileEntityTFSnowQueenSpawner.class),
	MINOSHROOM(false, TileEntityTFMinoshroomSpawner.class),
	ALPHA_YETI(false, TileEntityTFAlphaYetiSpawner.class);

	private final boolean usesGoldBackground;
	private final Class<? extends TileEntityTFBossSpawner> spawnerClass;

	public static final BossVariant[] VARIANTS = values();

	BossVariant(boolean isNotMiniBoss, Class<? extends TileEntityTFBossSpawner> spawnerClass) {
		this.usesGoldBackground = isNotMiniBoss;
		this.spawnerClass = spawnerClass;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public boolean usesGoldBackground() {
		return usesGoldBackground;
	}

	public Class<? extends TileEntityTFBossSpawner> getSpawnerClass() {
		return spawnerClass;
	}

	public static BossVariant getVariant(int id) {
		return id >= 0 && id < VARIANTS.length ? VARIANTS[id] : NAGA;
	}
}
