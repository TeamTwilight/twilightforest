package twilightforest.structures.lichtower;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;

public class ComponentTFTowerOutbuilding extends ComponentTFTowerWing {

	public ComponentTFTowerOutbuilding(TemplateManager manager, CompoundNBT nbt) {
		super(TFLichTowerPieces.TFLTOut, nbt);
	}

	protected ComponentTFTowerOutbuilding(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(TFLichTowerPieces.TFLTOut, feature, i, x, y, z, pSize, pHeight, direction);
	}

	/**
	 * NO BEARDS!
	 */
	@Override
	public void makeABeard(StructurePiece parent, List<StructurePiece> list, Random rand) {
    }

	/**
	 * Outbuildings should not make new wings close to the ground.
	 */
	@Override
	public boolean makeTowerWing(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation direction) {
		if (y > 7) {
			return super.makeTowerWing(list, rand, index, x, y, z, wingSize, wingHeight, direction);
		} else {
			return false;
		}
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		final BlockState cobblestone = Blocks.COBBLESTONE.getDefaultState();
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				this.replaceAirAndLiquidDownwards(world, cobblestone, x, -1, z, sbb);
			}
		}
		return super.func_230383_a_(world, manager, generator, rand, sbb, chunkPosIn, blockPos);
	}
}
