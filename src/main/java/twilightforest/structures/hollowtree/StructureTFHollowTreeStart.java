package twilightforest.structures.hollowtree;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.structures.StructureTFComponent;
import twilightforest.world.TFWorld;

import java.util.Random;

public class StructureTFHollowTreeStart extends StructureStart {

	public StructureTFHollowTreeStart(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox mbb, int ref, long seed) {
		super(structure, chunkX, chunkZ, mbb, ref, seed);
	}

	@Override
	public void init(ChunkGenerator<?> generator, TemplateManager manager, int chunkX, int chunkZ, Biome biome) {
		int x = (chunkX << 4) + 8;
		int z = (chunkZ << 4) + 8;
		int y = TFWorld.SEALEVEL + 1;

		StructureTFComponent trunk = new ComponentTFHollowTreeTrunk(rand, 0, x, y, z);
		components.add(trunk);
		trunk.buildComponent(trunk, components, rand);
		recalculateStructureSize();
	}

	/**
	 * Do everything except leaves before we do leaves.
	 */
	@Override
	public void generateStructure(IWorld worldIn, ChunkGenerator<?> generator, Random rand, MutableBoundingBox sbb, ChunkPos pos) {
		// first wood
		for (StructurePiece sc : components) {
			if (sc.getBoundingBox().intersectsWith(sbb) && sc instanceof StructureTFTreeComponent) {
				((StructureTFTreeComponent)sc).addComponentParts(worldIn.getWorld(), generator, rand, sbb, false);
			}
		}

		// now leaves
		for (StructurePiece sc : components) {
			if (sc.getBoundingBox().intersectsWith(sbb) && sc instanceof StructureTFTreeComponent) {
				((StructureTFTreeComponent)sc).addComponentParts(worldIn.getWorld(), generator, rand, sbb, true);
			}
		}
	}
}
