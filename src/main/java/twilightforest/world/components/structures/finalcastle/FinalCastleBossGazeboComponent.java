package twilightforest.world.components.structures.finalcastle;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class FinalCastleBossGazeboComponent extends TFStructureComponentOld {

	@SuppressWarnings("unused")
	public FinalCastleBossGazeboComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCBoGaz.get(), nbt);
	}

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
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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
	}

	public static final String INTERACTION_TAG = "final_castle_wip";

	private void willBeAddingFinalBossSoon(WorldGenLevel world, BoundingBox sbb) {
		// this.placeSignAtCurrentPosition(world, 10, 0, 10, sbb, "Final Boss Here", "You win!", "discord.gg/6v3z26B");

		// old message - revised 9/25/2022
		// setInvisibleTextEntity(world, 10, 0, 10, sbb, "Final Boss Here", true, 2.3f);
		// setInvisibleTextEntity(world, 10, 0, 10, sbb, "You win!", true, 2.0f);
		// setInvisibleTextEntity(world, 10, 0, 10, sbb, "You can join the Twilight Forest Discord server to follow",true, 1.0f);
		// setInvisibleTextEntity(world, 10, 0, 10, sbb, "the latest updates on this castle and other content at:",true, 0.7f);

		ImmutableList.Builder<Vec3> positionAccumulator = ImmutableList.builder();

		// Places Display Text entities, configured to only rotate-sync with camera (billboarding) except with locked vertical angle: It will only rotate sideways.
		this.setInvisibleTextEntity(world, 10, 0, 10, sbb, "Final Castle WIP.", true, 2.3f, positionAccumulator::add, Display.BillboardConstraints.VERTICAL);
		this.setInvisibleTextEntity(world, 10, 0, 10, sbb, "Join our Discord server to", true, 1.3f, positionAccumulator::add, Display.BillboardConstraints.VERTICAL);
		this.setInvisibleTextEntity(world, 10, 0, 10, sbb, "follow development of the mod:", true, 1.0f, positionAccumulator::add, Display.BillboardConstraints.VERTICAL);
		this.setInvisibleTextEntity(world, 10, 0, 10, sbb, "discord.experiment115.com", true, 0.7f, positionAccumulator::add, Display.BillboardConstraints.VERTICAL);

		// The hitbox for the Interaction entity capturing Player Punches
		AABB punchBox = BoundingBoxUtils.vectorsMinMax(positionAccumulator.build(), 1);
		if (punchBox != null) {
			double width = (punchBox.getXsize() + punchBox.getZsize()) * 0.5;
			double height = punchBox.getYsize();

			Interaction interaction = new Interaction(EntityType.INTERACTION, world.getLevel());

			// IMPORTANT: Punching this box has to produce behavior of discarding the text
			// Executed via EntityEvents#onAttackEvent using AttackEntityEvent
			interaction.addTag(INTERACTION_TAG);

			interaction.setHeight((float) height);
			interaction.setWidth((float) width);
            Vec3 center = punchBox.getCenter();
			interaction.setPos(center.x, punchBox.minY, center.z);
			world.addFreshEntity(interaction);
		}
	}
}
