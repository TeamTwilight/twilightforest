package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.DirectionUtil;
import twilightforest.util.RotationUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.TwilightJigsawPiece;

import java.util.ArrayList;
import java.util.List;

public final class LichTowerFoyer extends TwilightJigsawPiece implements PieceBeardifierModifier, SpawnIndexProvider {
	private final boolean putChest;
	private final boolean chestSide;

	public LichTowerFoyer(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.LICH_TOWER_FOYER.get(), compoundTag, ctx, readSettings(compoundTag));

		LichTowerUtil.addDefaultProcessors(this.placeSettings);

		this.putChest = compoundTag.getBoolean("put_chest");
		this.chestSide = compoundTag.getBoolean("chest_side");
	}

	public LichTowerFoyer(StructureTemplateManager structureManager, JigsawPlaceContext placeContext, boolean putChest, boolean chestSide) {
		super(TFStructurePieceTypes.LICH_TOWER_FOYER.get(), 0, structureManager, TwilightForestMod.prefix("lich_tower/tower_foyer"), placeContext);

		LichTowerUtil.addDefaultProcessors(this.placeSettings);

		this.putChest = putChest;
		this.chestSide = chestSide;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putBoolean("put_chest", this.putChest);
		structureTag.putBoolean("chest_side", this.chestSide);
	}

	// No need to serialize, this stateful object only needs to exist globally within this StructurePiece for the structure instance's initialization
	private final List<BlockPos> shelfPositions = new ArrayList<>();
	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		if ("twilightforest:lich_tower/tower_base".equals(connection.target())) {
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), this.structureManager, TwilightForestMod.prefix("lich_tower/tower_base"), "twilightforest:lich_tower/tower_base", random);

			if (placeableJunction == null) return;

			StructurePiece towerBase = new LichTowerBase(this.structureManager, placeableJunction);
			pieceAccessor.addPiece(towerBase);
			towerBase.addChildren(this, pieceAccessor, random);
		} else if ("twilightforest:shelf".equals(connection.target()) && random.nextFloat() <= 0.5f) {
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), connection.orientation(), this.structureManager, TwilightForestMod.prefix("lich_tower/foyer_decor"), "twilightforest:shelf", random);

			// Don't want to place next to an existing shelf
			if (placeableJunction == null || this.hasShelfNeighbor(connection.pos())) return;

			StructurePiece towerBase = new LichTowerFoyerDecor(this.genDepth + 1, this.structureManager, placeableJunction);
			pieceAccessor.addPiece(towerBase);
			towerBase.addChildren(this, pieceAccessor, random);

			this.shelfPositions.add(connection.pos());
		}
	}

	private boolean hasShelfNeighbor(BlockPos pos) {
		for (BlockPos occupied : this.shelfPositions)
			if (occupied.distManhattan(pos) < 1.5f) // Don't trust distManhattan to be fully integer
				return true;

		return false;
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		String[] directionSplit = label.split("@");

		if (directionSplit.length == 0) return;

		Rotation dataRotation = directionSplit.length == 1
			? Rotation.CLOCKWISE_180
			: RotationUtil.getRelativeRotation(Direction.NORTH, DirectionUtil.fromStringOrElse(directionSplit[1], Direction.SOUTH));

		if (this.putChest) {
			// The chest should only generate at one of the 2 data markers
			String toMatch = this.chestSide ? "chest_a" : "chest_b";
			if (toMatch.equals(directionSplit[0])) {

				level.removeBlock(pos, false); // Clears block entity data left by Data Marker

				Rotation stateRotation = this.placeSettings.getRotation().getRotated(dataRotation);
				level.setBlock(pos, Blocks.CHEST.defaultBlockState().rotate(stateRotation), Block.UPDATE_CLIENTS);

				if (level.getBlockEntity(pos) instanceof RandomizableContainer lootBlock) {
					// FIXME Use actual loot table
					lootBlock.setLootTable(TFLootTables.USELESS_LOOT, random.nextLong());
				}

				level.setBlock(pos.below(), TFBlocks.CANOPY_PLANKS.value().defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.BEARD_BOX;
	}

	@Override
	public int getGroundLevelDelta() {
		return 1;
	}

	@Override
	public int getSpawnIndex() {
		return LichTowerPieces.EMPTY;
	}
}
