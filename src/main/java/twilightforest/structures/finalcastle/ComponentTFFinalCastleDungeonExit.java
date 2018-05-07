package twilightforest.structures.finalcastle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFCastleDoor;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.block.BlockTFForceField;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.StructureBoundingBoxUtils;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleDungeonExit extends ComponentTFFinalCastleDungeonRoom31 {
	public ComponentTFFinalCastleDungeonExit() {
	}

	public ComponentTFFinalCastleDungeonExit(TFFeature feature, Random rand, int i, int x, int y, int z, EnumFacing direction, int level) {
		super(feature, rand, i, x, y, z, direction, level);
	}


	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// no need for additional rooms, we're along the outside anyways

		// add stairway down
		Rotation bestDir = this.findStairDirectionTowards(parent.getBoundingBox().minX, parent.getBoundingBox().minZ);

		ComponentTFFinalCastleDungeonSteps steps0 = new ComponentTFFinalCastleDungeonSteps(rand, 5, boundingBox.minX + 15, boundingBox.minY + 0, boundingBox.minZ + 15, bestDir.rotate(EnumFacing.SOUTH));
		list.add(steps0);
		steps0.buildComponent(this, list, rand);

		// another level!?
		if (this.level == 1) {
			steps0.buildLevelUnder(parent, list, rand, this.level + 1);
		} else {
			steps0.buildBossRoomUnder(parent, list, rand);
		}
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		// door
		final IBlockState castleDoor = TFBlocks.castle_door.getDefaultState()
				.withProperty(BlockTFCastleDoor.LOCK_INDEX, 2);
		this.fillWithBlocks(world, sbb, 7, 0, 16, 7, 3, 18, castleDoor, AIR, false);
		this.fillWithBlocks(world, sbb, 7, 4, 16, 7, 4, 18, deco.blockState, deco.blockState, false);


		return true;
	}


	public Rotation findStairDirectionTowards(int x, int z) {
		Vec3i center = StructureBoundingBoxUtils.getCenter(this.boundingBox);
		// difference
		int dx = center.getX() - x;
		int dz = center.getZ() - z;

		Rotation absoluteDir;
		if (Math.abs(dz) >= Math.abs(dx)) {
			absoluteDir = (dz >= 0) ? Rotation.CLOCKWISE_180 : Rotation.NONE;
		} else {
			absoluteDir = (dx >= 0) ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
		}

		return absoluteDir;
	}

	@Override
	protected EnumDyeColor getForceFieldMeta(Random decoRNG) {
		return BlockTFForceField.VALID_COLORS.get(1);
	}

	@Override
	protected EnumDyeColor getRuneMeta(EnumDyeColor fieldMeta) {
		return BlockTFCastleMagic.VALID_COLORS.get(0);
	}
}
