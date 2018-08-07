package twilightforest.structures.stronghold;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.start.StructureStartKnightStronghold;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TFStrongholdPieces {

	private static final TFStrongholdPieceWeight[] pieceWeightArray = new TFStrongholdPieceWeight[]{
			new TFStrongholdPieceWeight(ComponentTFStrongholdSmallHallway::new, 40, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdLeftTurn::new, 20, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdCrossing::new, 10, 4),
			new TFStrongholdPieceWeight(ComponentTFStrongholdRightTurn::new, 20, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdDeadEnd::new, 5, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdBalconyRoom::new, 10, 3, 2),
			new TFStrongholdPieceWeight(ComponentTFStrongholdTrainingRoom::new, 10, 2),
			new TFStrongholdPieceWeight(ComponentTFStrongholdSmallStairs::new, 10, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdTreasureCorridor::new, 5, 0),
			new TFStrongholdPieceWeight(ComponentTFStrongholdAtrium::new, 5, 2, 3),
			new TFStrongholdPieceWeight(ComponentTFStrongholdFoundry::new, 5, 1, 4),
			new TFStrongholdPieceWeight(ComponentTFStrongholdTreasureRoom::new, 5, 1, 4),
			new TFStrongholdPieceWeight(ComponentTFStrongholdBossRoom::new, 10, 1, 4)};

	private List<TFStrongholdPieceWeight> pieceList;
	static int totalWeight = 0;

	private static TFStrongholdPieceWeight lastPieceMade;

	public static void registerPieces() {
		MapGenStructureIO.registerStructure(StructureStartKnightStronghold.class, "TFKSt");

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
		pieceList = new ArrayList<>();

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

	public StructureTFStrongholdComponent getNextComponent(StructureComponent parent, List<StructureComponent> list, Random random, TFFeature feature, int index, EnumFacing facing, int x, int y, int z) {
		if (!hasMoreLimitedPieces()) {
			return null;
		} else {
			// repeat up to 5 times if we're not getting the right thing
			for (int i = 0; i < 5; i++) {
				int counter = random.nextInt(totalWeight);

				for (TFStrongholdPieceWeight piece : pieceList) {
					counter -= piece.pieceWeight;

					if (counter < 0) {

						if (!piece.isDeepEnough(index) || piece == lastPieceMade) {
							break;
						}

						// we're here!
						StructureTFStrongholdComponent component = piece.factory.newInstance(feature, index, facing, x, y, z);

						if (StructureComponent.findIntersecting(list, component.getBoundingBox()) == null) {
							++piece.instancesSpawned;

							if (!piece.canSpawnMoreStructures()) {
								pieceList.remove(piece);
							}

							lastPieceMade = piece;

							return component;
						}
					}
				}
			}
		}

		// dead end?
		StructureTFStrongholdComponent deadEnd = new ComponentTFStrongholdDeadEnd(parent instanceof StructureTFComponentOld ? ((StructureTFComponentOld)parent).getFeatureType() : TFFeature.NOTHING, index, facing, x, y, z);

		if (StructureComponent.findIntersecting(list, deadEnd.getBoundingBox()) == null) {
			return deadEnd;
		} else {
			return null;
		}
	}
}
