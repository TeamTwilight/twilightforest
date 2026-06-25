package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;

public class FinalCastleLargeTowerComponent extends TowerWingComponent {

	public static final Identifier LARGE_TOWER_TEMP_POOL = TwilightForestMod.prefix("final_castle/temp/large_tower");

	public FinalCastleLargeTowerComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCLaTo.get(), nbt);
	}

	@SuppressWarnings("this-escape")
	public FinalCastleLargeTowerComponent(int i, int x, int y, int z, Direction rotation) {
		super(TFStructurePieceTypes.TFFCLaTo.get(), i, x, y, z);
		this.setOrientation(rotation);
		this.size = 13;
		this.height = 61;
		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -6, 0, -6, 12, 60, 12, Direction.SOUTH, false);

	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
		// add crown
		FinalCastleRoof9CrenellatedComponent roof = new FinalCastleRoof9CrenellatedComponent(4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);

		TwilightJigsawPiece templatePiece = TwilightJigsawPiece.initializeTemplateFromPool(LARGE_TOWER_TEMP_POOL, this.getWorldPos(0, 1, 2), this.rotation.rotation().rotate(FrontAndTop.WEST_UP), "twilightforest:final_castle/large_tower", rand, this.genDepth + 1, ServerLifecycleHooks.getCurrentServer().getStructureManager());
		if (templatePiece != null) {
			list.addPiece(templatePiece);
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		generateBox(world, sbb, 0, 0, 0, 12, 59, 12, false, rand, deco.randomBlocks);

		// add branching runes
		int numBranches = 6 + decoRNG.nextInt(4);
		for (int i = 0; i < numBranches; i++) {
			makeGlyphBranches(world, decoRNG, this.getGlyphMeta(), sbb);
		}

		// beard
		for (int i = 1; i < 4; i++) {
			generateBox(world, sbb, i, -(i * 2), i, 8 - i, 1 - (i * 2), 8 - i, false, rand, deco.randomBlocks);
		}
		this.placeBlock(world, deco.blockState, 4, -7, 4, sbb);

		// door, first floor
		final BlockState castleDoor = TFBlocks.YELLOW_CASTLE_DOOR.get().defaultBlockState();
		this.generateBox(world, sbb, 0, 1, 1, 0, 4, 3, castleDoor, AIR, false);

		// sign placed by template
		// this.placeSignAtCurrentPosition(world, 6, 1, 6, "Parkour area 1", "Unique monster?", sbb);

		// this.placeBlock(world, Blocks.JIGSAW.defaultBlockState().setValue(BlockStateProperties.ORIENTATION, FrontAndTop.WEST_UP), 0, 1, 2, sbb);
	}

	public BlockState getGlyphMeta() {
		return TFBlocks.BLUE_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}
}
