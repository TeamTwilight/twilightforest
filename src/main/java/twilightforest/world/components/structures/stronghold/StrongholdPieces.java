package twilightforest.world.components.structures.stronghold;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

import java.util.ArrayList;
import java.util.List;


public class StrongholdPieces {

	private static final StrongholdPieceWeight[] pieceWeightArray = new StrongholdPieceWeight[]{
		new StrongholdPieceWeight(StrongholdSmallHallwayComponent::new, 40, 0),
		new StrongholdPieceWeight(StrongholdLeftTurnComponent::new, 20, 0),
		new StrongholdPieceWeight(StrongholdCrossingComponent::new, 10, 4),
		new StrongholdPieceWeight(StrongholdRightTurnComponent::new, 20, 0),
		new StrongholdPieceWeight(StrongholdDeadEndComponent::new, 5, 0),
		new StrongholdPieceWeight(StrongholdBalconyRoomComponent::new, 10, 3, 2),
		new StrongholdPieceWeight(StrongholdTrainingRoomComponent::new, 10, 2),
		new StrongholdPieceWeight(StrongholdSmallStairsComponent::new, 10, 0),
		new StrongholdPieceWeight(StrongholdTreasureCorridorComponent::new, 5, 0),
		new StrongholdPieceWeight(StrongholdAtriumComponent::new, 5, 2, 3),
		new StrongholdPieceWeight(StrongholdFoundryComponent::new, 5, 1, 4),
		new StrongholdPieceWeight(StrongholdTreasureRoomComponent::new, 5, 1, 4),
		new StrongholdPieceWeight(StrongholdBossRoomComponent::new, 15, 1, 4)};

	private List<StrongholdPieceWeight> pieceList;
	static int totalWeight = 0;

	private static StrongholdPieceWeight lastPieceMade;

	/**
	 * sets up Arrays with the Structure pieces and their weights
	 */
	public void prepareStructurePieces() {
		pieceList = new ArrayList<>();

		for (StrongholdPieceWeight piece : pieceWeightArray) {
			StrongholdPieceWeight cloned = piece.clone();
			cloned.instancesSpawned = 0;
			pieceList.add(cloned);
		}
	}

	public void markBossRoomUsed() {
		// let's assume the boss room is the last one on the list
		pieceList.remove(pieceList.size() - 1);
	}

	private boolean hasMoreLimitedPieces() {
		boolean flag = false;
		totalWeight = 0;

		for (StrongholdPieceWeight piece : pieceList) {
			totalWeight += piece.pieceWeight;

			if (piece.instancesLimit > 0 && piece.instancesSpawned < piece.instancesLimit) {
				flag = true;
			}
		}

		return flag;
	}

	public KnightStrongholdComponent getNextComponent(StructurePiece parent, StructurePieceAccessor list, RandomSource random, int index, Direction facing, int x, int y, int z) {
		if (!hasMoreLimitedPieces()) {
			return null;
		} else {
			// repeat up to 5 times if we're not getting the right thing
			for (int i = 0; i < 5; i++) {
				int counter = random.nextInt(totalWeight);

				for (StrongholdPieceWeight piece : pieceList) {
					counter -= piece.pieceWeight;

					if (counter < 0) {

						if (!piece.isDeepEnough(index) || piece == lastPieceMade) {
							break;
						}

						// we're here!
						KnightStrongholdComponent component = piece.factory.newInstance(index, facing, x, y, z);

						if (list.findCollisionPiece(component.getBoundingBox()) == null) {
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
		KnightStrongholdComponent deadEnd = new StrongholdDeadEndComponent(index, facing, x, y, z);

		if (list.findCollisionPiece(deadEnd.getBoundingBox()) == null) {
			return deadEnd;
		} else {
			return null;
		}
	}
}
