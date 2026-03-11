package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public interface TreeGrowerStartable {
	boolean checkSaplingClearance(ServerLevel level, BlockPos pos);

	StructureStart generateFromSapling(
		RegistryAccess registryAccess,
		ChunkGenerator generator,
		BiomeSource biomeSource,
		RandomState randomState,
		StructureTemplateManager templateManager,
		long seed,
		BlockPos blockPos,
		LevelHeightAccessor heightAccessor
	);
}
