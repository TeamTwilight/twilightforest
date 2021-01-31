package twilightforest.structures.hollowtree;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.structures.StructureTFComponent;
import twilightforest.world.TFGenerationSettings;

import java.util.Random;

public class StructureTFHollowTreeStart extends StructureStart<NoFeatureConfig> {

	public StructureTFHollowTreeStart(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox mbb, int ref, long seed) {
		super(structure, chunkX, chunkZ, mbb, ref, seed);
	}

	@Override
	public void func_230364_a_(DynamicRegistries dynamicRegistries, ChunkGenerator cgenerator, TemplateManager manager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) { //init
		int x = (chunkX << 4) + 8;
		int z = (chunkZ << 4) + 8;
		int y = TFGenerationSettings.SEALEVEL + 1;

		StructureTFComponent trunk = new ComponentTFHollowTreeTrunk(rand, 0, x, y, z);
		components.add(trunk);
		trunk.buildComponent(trunk, components, rand);
		recalculateStructureSize();
	}

	/**
	 * Do everything except leaves before we do leaves.
	 */
	@Override
	public void func_230366_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos pos) { //generateStructure
		// first wood
		for (StructurePiece sc : this.components) {
			if (sc.getBoundingBox().intersectsWith(sbb) && sc instanceof StructureTFTreeComponent) {
				((StructureTFTreeComponent)sc).addComponentParts(world, generator, rand, sbb, false);
			}
		}

		// now leaves
		for (StructurePiece sc : components) {
			if (sc.getBoundingBox().intersectsWith(sbb) && sc instanceof StructureTFTreeComponent) {
				((StructureTFTreeComponent)sc).addComponentParts(world, generator, rand, sbb, true);
			}
		}
	}
}
