package twilightforest.structures.stronghold;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.structures.StructureTFComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TFStrongholdPieces {

	private static final TFStrongholdPieceWeight[] pieceWeightArray = new TFStrongholdPieceWeight[]{
			new TFStrongholdPieceWeight(ComponentTFStrongholdSmallHallway.class, 40, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdLeftTurn.class, 20, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdCrossing.class, 10, 4),
			new TFStrongholdPieceWeight(ComponentTFStrongholdRightTurn.class, 20, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdDeadEnd.class, 5, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdBalconyRoom.class, 10, 3, 2),
			new TFStrongholdPieceWeight(ComponentTFStrongholdTrainingRoom.class, 10, 2),
			new TFStrongholdPieceWeight(ComponentTFStrongholdSmallStairs.class, 10, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdTreasureCorridor.class, 5, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdAtrium.class, 5, 2, 3),
			new TFStrongholdPieceWeight(ComponentTFStrongholdFoundry.class, 5, 1, 4),
			new TFStrongholdPieceWeight(ComponentTFStrongholdTreasureRoom.class, 5, 1, 4),
			new TFStrongholdPieceWeight(ComponentTFStrongholdBossRoom.class, 10, 1, 4)};

	private List<TFStrongholdPieceWeight> pieceList;
	static int totalWeight = 0;

	private static Class<? extends StructureTFComponent> lastPieceMade;

	public static void registerPieces() {
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdSmallHallway.class, "TFSSH");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdLeftTurn.class, "TFSLT");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdCrossing.class, "TFSCr");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdRightTurn.class, "TFSRT");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdDeadEnd.class, "TFSDE");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdBalconyRoom.class, "TFSBR");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdTrainingRoom.class, "TFSTR");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdSmallStairs.class, "TFSSS");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdTreasureCorridor.class, "TFSTC");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdAtrium.class, "TFSAt");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdFoundry.class, "TFSFo");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdTreasureRoom.class, "TFTreaR");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdBossRoom.class, "TFSBR");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdAccessChamber.class, "TFSAC");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdEntrance.class, "TFSEnter");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdUpperAscender.class, "TFSUA");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdUpperLeftTurn.class, "TFSULT");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdUpperRightTurn.class, "TFSURT");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdUpperCorridor.class, "TFSUCo");
		MapGenStructureIO.registerStructureComponent(ComponentTFStrongholdUpperTIntersection.class, "TFSUTI");
		MapGenStructureIO.registerStructureComponent(StructureTFStrongholdShield.class, "TFSShield");
	}

	/**
	 * sets up Arrays with the Structure pieces and their weights
	 */
	public void prepareStructurePieces() {
		pieceList = new ArrayList<TFStrongholdPieceWeight>();

		for (TFStrongholdPieceWeight piece : pieceWeightArray) {
			piece.instancesSpawned = 0;
			pieceList.add(piece);
		}
	}

	public void markBossRoomUsed() {
		// let's assume the boss room is the last one on the list
		pieceList.remove(pieceList.size() - 1);
	}

	private boolean hasMoreLimitedPieces() {
		boolean flag = false;
		totalWeight = 0;

		for (TFStrongholdPieceWeight piece : pieceList) {
			totalWeight += piece.pieceWeight;

			if (piece.instancesLimit > 0 && piece.instancesSpawned < piece.instancesLimit) {
				flag = true;
			}
		}

		return flag;
	}

	public StructureTFStrongholdComponent getNextComponent(StructureComponent parent, List<StructureComponent> list, Random random, int index, EnumFacing facing, int x, int y, int z) {
		if (!hasMoreLimitedPieces()) {
			return null;
		} else {
			// repeat up to 5 times if we're not getting the right thing
			for (int i = 0; i < 5; i++) {
				int counter = random.nextInt(totalWeight);

				for (TFStrongholdPieceWeight piece : pieceList) {
					counter -= piece.pieceWeight;

					if (counter < 0) {

						if (!piece.isDeepEnough(index) || piece.pieceClass == lastPieceMade) {
							break;
						}

						// we're here!
						StructureTFStrongholdComponent component = instantiateComponent(piece.pieceClass, index, facing, x, y, z);

						if (StructureComponent.findIntersecting(list, component.getBoundingBox()) == null) {
							++piece.instancesSpawned;

							if (!piece.canSpawnMoreStructures()) {
								pieceList.remove(piece);
							}

							lastPieceMade = piece.pieceClass;

							return component;
						}
					}
				}
			}
		}

		// dead end?
		StructureTFStrongholdComponent deadEnd = new ComponentTFStrongholdDeadEnd(index, facing, x, y, z);

		if (StructureComponent.findIntersecting(list, deadEnd.getBoundingBox()) == null) {
			return deadEnd;
		} else {
			return null;
		}
	}

	private static StructureTFStrongholdComponent instantiateComponent(Class<? extends StructureTFComponent> pieceClass, int index, EnumFacing facing, int x, int y, int z) {
		// todo 1.9
		try {
			return (StructureTFStrongholdComponent) pieceClass.getConstructor(int.class, EnumFacing.class, int.class, int.class, int.class).newInstance(index, facing, x, y, z);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		////			attempted = new ComponentTFStrongholdRoom(index, nFacing, nx, ny, nz);

		return null;
	}

}
