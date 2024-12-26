package twilightforest.world.components.structures.stronghold;

import twilightforest.world.components.structures.stronghold.KnightStrongholdComponent.Factory;

/**
 * Based off StructureStrongholdPieceWeight
 */
public class StrongholdPieceWeight implements Cloneable {

	public final Factory<? extends KnightStrongholdComponent> factory;
	public final int pieceWeight;
	public int instancesSpawned;

	/**
	 * How many Structure Pieces of this type may spawn in a structure
	 */
	public final int instancesLimit;
	public final int minimumDepth;

	public <T extends KnightStrongholdComponent> StrongholdPieceWeight(Factory<T> factory, int weight, int limit) {
		this(factory, weight, limit, 0);
	}

	public <T extends KnightStrongholdComponent> StrongholdPieceWeight(Factory<T> factory, int weight, int limit, int minDepth) {
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

	@Override
	public StrongholdPieceWeight clone() {
		try {
			StrongholdPieceWeight clone = (StrongholdPieceWeight) super.clone();
			clone.instancesSpawned = 0;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
