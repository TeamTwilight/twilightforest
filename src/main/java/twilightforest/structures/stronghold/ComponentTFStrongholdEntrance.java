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
import twilightforest.TwilightForestMod;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdEntrance extends StructureTFStrongholdComponent {

	public TFStrongholdPieces lowerPieces;

	public ComponentTFStrongholdEntrance(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSEnter, nbt);

		this.deco = new StructureTFDecoratorStronghold();

		lowerPieces = new TFStrongholdPieces();
	}

	public ComponentTFStrongholdEntrance(TFFeature feature, int i, int x, int y, int z) {
		super(TFStrongholdPieces.TFSEnter, feature, i, Direction.SOUTH, x, y - 10, z);

		this.deco = new StructureTFDecoratorStronghold();

		lowerPieces = new TFStrongholdPieces();
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random random) {
		super.buildComponent(parent, list, random);

		// make a random component in each direction
		lowerPieces.prepareStructurePieces();
		addNewComponent(parent, list, random, Rotation.NONE, 4, 1, 18);
		lowerPieces.prepareStructurePieces();
		if (listContainsBossRoom(list)) {
			lowerPieces.markBossRoomUsed();
		}
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 1, 13);
		lowerPieces.prepareStructurePieces();
		if (listContainsBossRoom(list)) {
			lowerPieces.markBossRoomUsed();
		}
		addNewComponent(parent, list, random, Rotation.CLOCKWISE_180, 13, 1, -1);
		lowerPieces.prepareStructurePieces();
		if (listContainsBossRoom(list)) {
			lowerPieces.markBossRoomUsed();
		}
		addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 1, 4);
		if (!listContainsBossRoom(list)) {
			TwilightForestMod.LOGGER.warn("Did not find boss room from exit 3 - EPIC FAIL");
		}
		MutableBoundingBox shieldBox = new MutableBoundingBox(this.boundingBox);

		int tStairs = 0;
		int tCorridors = 0;
		int deadEnd = 0;
		int tRooms = 0;
		int bossRooms = 0;

		// compute and generate MEGASHIELD
		for (StructurePiece component : list) {
			shieldBox.expandTo(component.getBoundingBox());


			if (component instanceof ComponentTFStrongholdSmallStairs && ((ComponentTFStrongholdSmallStairs) component).hasTreasure) {
				tStairs++;
			}
			if (component instanceof ComponentTFStrongholdTreasureCorridor) {
				tCorridors++;
			}
			if (component instanceof ComponentTFStrongholdDeadEnd) {
				deadEnd++;
			}
			if (component instanceof ComponentTFStrongholdTreasureRoom) {
				tRooms++;
			}
			if (component instanceof ComponentTFStrongholdBossRoom) {
				bossRooms++;
			}
		}

//		System.out.printf("MEGASHIELD computed!  %d, %d, %d to %d, %d, %d.\n", shieldBox.minX, shieldBox.minY, shieldBox.minZ, shieldBox.maxX, shieldBox.maxY, shieldBox.maxZ);
//		System.out.printf("Stronghold at this point contains %d elements.\n", pieceList.size());
//		
//		StructureTFStrongholdShield shield = new StructureTFStrongholdShield(shieldBox.minX - 1, shieldBox.minY, shieldBox.minZ - 1, shieldBox.maxX, shieldBox.maxY, shieldBox.maxZ);
//		list.add(shield);

		// add the upper stronghold
		StructureTFStrongholdComponent accessChamber = new ComponentTFStrongholdAccessChamber(getFeatureType(), 2, this.getCoordBaseMode(), boundingBox.minX + 8, boundingBox.minY + 7, boundingBox.minZ + 4);
		list.add(accessChamber);
		accessChamber.buildComponent(this, list, random);
	}

	private boolean listContainsBossRoom(List<StructurePiece> list) {
		for (StructurePiece component : list) {
			if (component instanceof ComponentTFStrongholdBossRoom) {
				return true;
			}
		}

		return false;
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return MutableBoundingBox.getComponentToAddBoundingBox(x, y, z, -1, -1, 0, 18, 7, 18, facing);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 6, 17, rand, deco.randomBlocks);

		// statues
		placeCornerStatue(world, 5, 1, 5, 0, sbb);
		placeCornerStatue(world, 5, 1, 12, 1, sbb);
		placeCornerStatue(world, 12, 1, 5, 2, sbb);
		placeCornerStatue(world, 12, 1, 12, 3, sbb);

		// statues
		this.placeWallStatue(world, 9, 1, 16, Rotation.NONE, sbb);
		this.placeWallStatue(world, 1, 1, 9, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 8, 1, 1, Rotation.CLOCKWISE_180, sbb);
		this.placeWallStatue(world, 16, 1, 8, Rotation.COUNTERCLOCKWISE_90, sbb);

		// doors
		this.placeDoors(world, rand, sbb);

		return true;
	}
}
