package twilightforest.world.components.structures.stronghold;

import twilightforest.world.components.structures.stronghold.StructureTFStrongholdComponent.Factory;

/**
 * Based off StructureStrongholdPieceWeight
 */
public class StrongholdPieceWeight {

	public final Factory<? extends StructureTFStrongholdComponent> factory;
	public final int pieceWeight;
	public int instancesSpawned;

	/**
	 * How many Structure Pieces of this type may spawn in a structure
	 */
	public final int instancesLimit;
	public final int minimumDepth;

	public <T extends StructureTFStrongholdComponent> StrongholdPieceWeight(Factory<T> factory, int weight, int limit) {
		this(factory, weight, limit, 0);
	}

	public <T extends StructureTFStrongholdComponent> StrongholdPieceWeight(Factory<T> factory, int weight, int limit, int minDepth) {
		this.factory = factory;
		this.pieceWeight = weight;
		this.instancesLimit = limit;
		this.minimumDepth = minDepth;
	}

	/**
	 * Fresh copy with a zeroed spawn counter, for use by a single stronghold
	 */
	public StrongholdPieceWeight copy() {
		return new StrongholdPieceWeight(this.factory, this.pieceWeight, this.instancesLimit, this.minimumDepth);
	}

	public boolean isDeepEnough(int depth) {
		return canSpawnMoreStructures() && depth >= this.minimumDepth;
	}

	public boolean canSpawnMoreStructures() {
		return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
	}

}
