package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleBridge extends StructureTFComponentOld {

	public ComponentTFFinalCastleBridge(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCBri, nbt);
	}

	public ComponentTFFinalCastleBridge(TFFeature feature, int i, int x, int y, int z, int length, Direction direction) {
		super(TFFinalCastlePieces.TFFCBri, feature, i);
		this.setCoordBaseMode(direction);
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox2(x, y, z, 0, -1, -3, length - 1, 5, 6, direction);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int length = (this.getCoordBaseMode() == Direction.SOUTH || this.getCoordBaseMode() == Direction.NORTH) ? this.boundingBox.maxX - this.boundingBox.minX : this.boundingBox.maxZ - this.boundingBox.minZ;

		// span
		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, length, 1, 6, false, rand, deco.randomBlocks);
		// rails
		//fillWithRandomizedBlocks(world, sbb, 0, 1, 0, length, 2, 0, false, rand, deco.randomBlocks);
		//fillWithRandomizedBlocks(world, sbb, 0, 1, 6, length, 2, 6, false, rand, deco.randomBlocks);
		BlockState castlePillar = TFBlocks.castle_pillar_bold.get().getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.X);

		fillWithBlocks(world, sbb, 0, 2, 0, length, 2, 0, castlePillar, castlePillar, false);
		fillWithBlocks(world, sbb, 0, 2, 6, length, 2, 6, castlePillar, castlePillar, false);

		// supports
		int l3 = length / 3;
		for (int i = 0; i < l3; i++) {
			int sl = l3 - (int) (MathHelper.cos((float) (l3 - i) / (float) l3 * 1.6F) * l3); // this could be better, maybe?
			fillWithRandomizedBlocks(world, sbb, i, -sl, 0, i, 0, 0, false, rand, deco.randomBlocks);
			fillWithRandomizedBlocks(world, sbb, i, -sl, 6, i, 0, 6, false, rand, deco.randomBlocks);
			fillWithRandomizedBlocks(world, sbb, length - i, -sl, 0, length - i, 0, 0, false, rand, deco.randomBlocks);
			fillWithRandomizedBlocks(world, sbb, length - i, -sl, 6, length - i, 0, 6, false, rand, deco.randomBlocks);
		}

		// doorframes
		this.fillWithBlocks(world, sbb, 0, 2, 1, 0, 7, 1, deco.pillarState, deco.pillarState, false);
		this.fillWithBlocks(world, sbb, 0, 2, 5, 0, 7, 5, deco.pillarState, deco.pillarState, false);
		this.fillWithBlocks(world, sbb, 0, 6, 2, 0, 6, 4, deco.accentState, deco.accentState, false);
		this.setBlockState(world, deco.pillarState, 0, 7, 3, sbb);

		this.fillWithBlocks(world, sbb, length, 2, 1, length, 7, 1, deco.pillarState, deco.pillarState, false);
		this.fillWithBlocks(world, sbb, length, 2, 5, length, 7, 5, deco.pillarState, deco.pillarState, false);
		this.fillWithBlocks(world, sbb, length, 6, 2, length, 6, 4, deco.accentState, deco.accentState, false);
		this.setBlockState(world, deco.pillarState, length, 7, 3, sbb);

		return true;
	}
}
