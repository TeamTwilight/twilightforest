package twilightforest.structures.stronghold;

import twilightforest.structures.stronghold.StructureTFStrongholdComponent.Factory;

/**
 * Based off StructureStrongholdPieceWeight
 */
public class TFStrongholdPieceWeight {

	public final Factory<? extends StructureTFStrongholdComponent> factory;
	public final int pieceWeight;
	public int instancesSpawned;

	/**
	 * How many Structure Pieces of this type may spawn in a structure
	 */
	public int instancesLimit;
	public int minimumDepth;

	public <T extends StructureTFStrongholdComponent> TFStrongholdPieceWeight(Factory<T> factory, int weight, int limit) {
		this(factory, weight, limit, 0);
	}

	public <T extends StructureTFStrongholdComponent> TFStrongholdPieceWeight(Factory<T> factory, int weight, int limit, int minDepth) {
		this.factory = factory;
		this.pieceWeight = weight;
		this.instancesLimit = limit;
		this.minimumDepth = minDepth;
	}

	public boolean isDeepEnough(int depth) {
		return canSpawnMoreStructures() && depth >= this.minimumDepth;
	}

	public boolean canSpawnMoreStructures() {
		return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
	}

}
