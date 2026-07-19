package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import tamaized.beanification.Autowired;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;

public class FinalCastleBossGazeboComponent extends TFStructureComponentOld {

	public static final Identifier GAZEBO_TEMP_POOL = TwilightForestMod.prefix("final_castle/temp/gazebo");

	@Autowired
	private static StructureTemplateDefinitions structureTemplateDefinitions;

	@SuppressWarnings("unused")
	public FinalCastleBossGazeboComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCBoGaz.get(), nbt);
	}

	@SuppressWarnings("this-escape")
	public FinalCastleBossGazeboComponent(int i, TFStructureComponentOld keep, int x, int y, int z) {
		super(TFStructurePieceTypes.TFFCBoGaz.get(), i, x, y, z);
		this.spawnListIndex = -1; // no monsters

		this.setOrientation(keep.getOrientation());
		this.boundingBox = new BoundingBox(keep.getBoundingBox().minX() + 14, keep.getBoundingBox().maxY() + 2, keep.getBoundingBox().minZ() + 14, keep.getBoundingBox().maxX() - 14, keep.getBoundingBox().maxY() + 13, keep.getBoundingBox().maxZ() - 14);

	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		this.deco = new StructureTFDecoratorCastle();
		this.deco.blockState = TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get().defaultBlockState();

		this.deco.fenceState = TFBlocks.VIOLET_FORCE_FIELD.get().defaultBlockState();

		TwilightJigsawPiece templatePiece = structureTemplateDefinitions.initializeTemplateFromPool(GAZEBO_TEMP_POOL, this.getWorldPos(10, -1, 10), this.rotation.rotation().rotate(FrontAndTop.UP_SOUTH), "twilightforest:final_castle/final_boss", rand, this.genDepth + 1, ServerLifecycleHooks.getCurrentServer().getStructureManager());
		if (templatePiece != null) {
			list.addPiece(templatePiece);
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		/* Placed by Template
		BlockState state = TFBlocks.VIOLET_FORCE_FIELD.get().defaultBlockState();

		// walls
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			this.fillBlocksRotated(world, sbb, 0, 0, 0, 0, 10, 20, state, rotation);
		}

		// roof
		this.generateBox(world, sbb, 1, 11, 0, 19, 11, 20, state, state, false);
		this.generateBox(world, sbb, 0, 11, 0, 0, 11, 20, state, state, false);
		this.generateBox(world, sbb, 20, 11, 0, 20, 11, 20, state, state, false);

		this.willBeAddingFinalBossSoon(world, sbb);

		// placeBlock(world, TFBlocks.boss_spawner_final_boss.get().defaultBlockState(), 10, 1, 10, sbb);
		*/
	}

	public static final String INTERACTION_TAG = "final_castle_wip";
}
