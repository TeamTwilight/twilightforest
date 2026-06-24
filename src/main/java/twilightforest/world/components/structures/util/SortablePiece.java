package twilightforest.world.components.structures.util;

public interface SortablePiece {
	/**
	 * @return Value 0 is functionally the default value.
	 * 	Negative values mean the StructurePiece implementer will generate sooner.
	 * 	Positive values mean the StructurePiece implementer will generate later.
	 * 	Pieces generating sooner will have their overlapping blocks replaced by later Pieces.
	 */
	int getSortKey();
}
