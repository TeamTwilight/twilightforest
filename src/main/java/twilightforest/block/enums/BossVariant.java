package twilightforest.block.enums;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;
import twilightforest.tileentity.*;

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

	private final boolean hasTrophy;
	private final Class<? extends TileEntityTFBossSpawner> spawnerClass;
	public static final BossVariant[] VALUES = values();

	BossVariant(boolean isNotMiniBoss, Class<? extends TileEntityTFBossSpawner> spawnerClass) {
		this.hasTrophy = isNotMiniBoss;
		this.spawnerClass = spawnerClass;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public boolean hasTrophy() {
		return hasTrophy;
	}

	public Class<? extends TileEntityTFBossSpawner> getSpawnerClass() {
		return spawnerClass;
	}
}
