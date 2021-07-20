package twilightforest.structures.darktower;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.List;
import java.util.Random;

public class ComponentTFDarkTowerBridge extends ComponentTFTowerWing {

	public ComponentTFDarkTowerBridge(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public ComponentTFDarkTowerBridge(TemplateManager manager, CompoundNBT nbt) {
		super(TFDarkTowerPieces.TFDTBri, nbt);
	}

	private int dSize;
	private int dHeight;

	protected ComponentTFDarkTowerBridge(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(type, feature, i, x, y, z, 5, 5, direction);

		this.dSize = pSize;
		this.dHeight = pHeight;
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
		makeTowerWing(list, rand, this.getComponentType(), 4, 1, 2, dSize, dHeight, Rotation.NONE);
	}

	@Override
	public boolean makeTowerWing(List<StructurePiece> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		// kill too-small towers
		if (wingHeight < 6) {
			return false;
		}

		Direction direction = getStructureRelativeRotation(rotation);
		int[] dx = offsetTowerCoords(x, y, z, wingSize, direction);

		if (dx[1] + wingHeight > 255) {
			// end of the world!
			return false;
		}

		ComponentTFTowerWing wing = new ComponentTFDarkTowerWing(TFDarkTowerPieces.TFDTWin, getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
		// check to see if it intersects something already there
		StructurePiece intersect = StructurePiece.findIntersecting(list, wing.getBoundingBox());
		if (intersect == null || intersect == this) {
			list.add(wing);
			wing.buildComponent(this, list, rand);
			addOpening(x, y, z, rotation);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// make walls
		fillWithBlocks(world, sbb, 0, 0, 0, size - 1, height - 1, size - 1, deco.blockState, deco.blockState, false);

		// accents
		for (int x = 0; x < size; x++) {
			this.setBlockState(world, deco.accentState, x, 0, 0, sbb);
			this.setBlockState(world, deco.accentState, x, height - 1, 0, sbb);
			this.setBlockState(world, deco.accentState, x, 0, size - 1, sbb);
			this.setBlockState(world, deco.accentState, x, height - 1, size - 1, sbb);
		}

		// nullify sky light
//		nullifySkyLightForBoundingBox(world.getWorld());

		// clear inside
		fillWithAir(world, sbb, 0, 1, 1, size - 1, height - 2, size - 2);

		return true;
	}

	/**
	 * Gets the bounding box of the tower wing we would like to make.
	 *
     */
	public MutableBoundingBox getWingBB() {
		int[] dest = offsetTowerCoords(4, 1, 2, dSize, this.getCoordBaseMode());
		return feature.getComponentToAddBoundingBox(dest[0], dest[1], dest[2], 0, 0, 0, dSize - 1, dHeight - 1, dSize - 1, this.getCoordBaseMode());
	}
}
