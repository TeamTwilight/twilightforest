package twilightforest.block.enums;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;
import twilightforest.tileentity.TileEntityTFBossSpawner;
import twilightforest.tileentity.TileEntityTFHydraSpawner;
import twilightforest.tileentity.TileEntityTFKnightPhantomsSpawner;
import twilightforest.tileentity.TileEntityTFLichSpawner;
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
	SNOW_QUEEN(true, TileEntityTFSnowQueenSpawner.class);

	private final boolean hasTrophy;
	private final Class<? extends TileEntityTFBossSpawner> spawnerClass;

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
