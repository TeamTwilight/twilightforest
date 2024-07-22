package twilightforest.world.components.structures.util;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.world.components.structures.start.TFStructureStart;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class ConquerableStructure extends LandmarkStructure implements CustomStructureData {
	public ConquerableStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
		super(decorationConfig, structureSettings);
	}

	@Override
	public final StructureStart generate(RegistryAccess registryAccess, ChunkGenerator chunkGen, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager templateManager, long seed, ChunkPos chunkPos, int references, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> isValidBiome) {
		return this.generateCustom(registryAccess, chunkGen, biomeSource, randomState, templateManager, seed, chunkPos, references, heightAccessor, isValidBiome);
	}

	// [VANILLA COPY] Structure.generate
	//  StructureStart construction swapped for TFStructureStart construction
	public StructureStart generateCustom(RegistryAccess registryAccess, ChunkGenerator chunkGen, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager templateManager, long pseed, ChunkPos chunkPos, int references, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> isValidBiome) {
		GenerationContext structure$generationcontext = new GenerationContext(registryAccess, chunkGen, biomeSource, randomState, templateManager, pseed, chunkPos, heightAccessor, isValidBiome);
		Optional<GenerationStub> optional = this.findValidGenerationPoint(structure$generationcontext);
		if (optional.isPresent()) {
			StructureStart structurestart = this.createStart(chunkPos, references, optional.get());
			if (structurestart.isValid()) return structurestart;
		}

		return StructureStart.INVALID_START;
	}

	protected StructureStart createStart(ChunkPos chunkPos, int reference, GenerationStub generationStub) {
		return new TFStructureStart(this, chunkPos, reference, generationStub.getPiecesBuilder().build());
	}
}
