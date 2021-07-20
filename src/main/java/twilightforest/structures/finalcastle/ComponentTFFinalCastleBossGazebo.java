package twilightforest.structures.finalcastle;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleBossGazebo extends StructureTFComponentOld {

	@SuppressWarnings("unused")
	public ComponentTFFinalCastleBossGazebo(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCBoGaz, nbt);
	}

	public ComponentTFFinalCastleBossGazebo(TFFeature feature, Random rand, int i, StructureTFComponentOld keep) {
		super(TFFinalCastlePieces.TFFCBoGaz, feature, i);
		this.spawnListIndex = -1; // no monsters

		this.setCoordBaseMode(keep.getCoordBaseMode());
		this.boundingBox = new MutableBoundingBox(keep.getBoundingBox().minX + 14, keep.getBoundingBox().maxY + 2, keep.getBoundingBox().minZ + 14, keep.getBoundingBox().maxX - 14, keep.getBoundingBox().maxY + 13, keep.getBoundingBox().maxZ - 14);

	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		this.deco = new StructureTFDecoratorCastle();
		this.deco.blockState = TFBlocks.castle_rune_brick_blue.get().getDefaultState();

		this.deco.fenceState = TFBlocks.force_field_purple.get().getDefaultState();
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random randomIn, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// walls
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			this.fillBlocksRotated(world, sbb, 0, 0, 0, 0, 10, 20, deco.fenceState, rotation);
		}

		// roof
		this.fillWithBlocks(world, sbb, 0, 11, 0, 20, 11, 20, deco.fenceState, deco.fenceState, false);

		//this.placeSignAtCurrentPosition(world, 10, 0, 10, sbb, "Final Boss Here", "You win!", "discord.gg/6v3z26B");

		setInvisibleTextEntity(world, 10, 0, 10, sbb, "Final Boss Here", true, 2.3f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "You win!", true, 2.0f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "You can join the Twilight Forest Discord server to follow",true, 1.0f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "the latest updates on this castle and other content at:",true, 0.7f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "discord.experiment115.com", true, 0.4f);

		setBlockState(world, TFBlocks.boss_spawner_final_boss.get().getDefaultState(), 10, 1, 10, sbb);

		return true;
	}
}
