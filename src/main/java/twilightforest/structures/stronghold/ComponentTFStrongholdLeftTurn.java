package twilightforest.structures.stronghold;

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

public class ComponentTFStrongholdLeftTurn extends StructureTFStrongholdComponent {

	public ComponentTFStrongholdLeftTurn(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSLT, nbt);
	}

	public ComponentTFStrongholdLeftTurn(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(TFStrongholdPieces.TFSLT, feature, i, facing, x, y, z);
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 7, 9, facing);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		// entrance
		this.addDoor(4, 1, 0);

		// make a random component to the left
		addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 9, 1, 4);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 8, rand, deco.randomBlocks);

		// clear inside
		fillWithAir(world, sbb, 1, 1, 1, 7, 5, 7);

//		// entrance doorway
//		placeDoorwayAt(world, rand, 2, 4, 1, 0, sbb);
//		
//		// left turn doorway
//		placeDoorwayAt(world, rand, 1, 8, 1, 4, sbb);

		// statue
		placeCornerStatue(world, 2, 1, 6, 1, sbb);

		// doors
		placeDoors(world, sbb);

		return true;
	}
}
