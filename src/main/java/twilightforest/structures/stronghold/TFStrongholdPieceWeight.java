package twilightforest.structures.stronghold;

import twilightforest.structures.StructureTFComponentOld;

/**
 * Based off StructureStrongholdPieceWeight
 */
public class TFStrongholdPieceWeight {

	public Class<? extends StructureTFComponentOld> pieceClass;
	public final int pieceWeight;
	public int instancesSpawned;

	/**
	 * How many Structure Pieces of this type may spawn in a structure
	 */
	public int instancesLimit;
	public int minimumDepth;

	public TFStrongholdPieceWeight(Class<? extends StructureTFComponentOld> par1Class, int weight, int limit) {
		this(par1Class, weight, limit, 0);
	}

	public TFStrongholdPieceWeight(Class<? extends StructureTFComponentOld> par1Class, int weight, int limit, int minDepth) {
		this.pieceClass = par1Class;
		this.pieceWeight = weight;
		this.instancesLimit = limit;
		this.minimumDepth = minDepth;
	}

	public boolean isDeepEnough(int par1) {
		return canSpawnMoreStructures() && par1 >= this.minimumDepth;
	}

	public boolean canSpawnMoreStructures() {
		return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
	}

}
