package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleDungeonSteps extends StructureTFComponentOld {
	public ComponentTFFinalCastleDungeonSteps() {
	}

	public ComponentTFFinalCastleDungeonSteps(Random rand, int i, int x, int y, int z, EnumFacing rotation) {
		this.spawnListIndex = 2; // dungeon monsters

		this.setCoordBaseMode(rotation);
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox2(x, y, z, -2, -15, -3, 5, 15, 20, rotation);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	/**
	 * build more steps towards the specified direction
	 */
	public ComponentTFFinalCastleDungeonSteps buildMoreStepsTowards(StructureComponent parent, List<StructureComponent> list, Random rand, Rotation rotation) {

		EnumFacing direction = getStructureRelativeRotation(rotation);

		int sx = 2;
		int sy = 0;
		int sz = 17;

		switch (rotation) {
			case NONE:
				sz -= 5;
				break;
			case CLOCKWISE_90:
				sx -= 5;
				break;
			case CLOCKWISE_180:
				sz += 5;
				break;
			case COUNTERCLOCKWISE_90:
				sx += 6;
				break;
		}

		// find center of landing
		int dx = this.getXWithOffset(sx, sz);
		int dy = this.getYWithOffset(sy);
		int dz = this.getZWithOffset(sx, sz);


		// build a new stairway there
		ComponentTFFinalCastleDungeonSteps steps = new ComponentTFFinalCastleDungeonSteps(rand, this.componentType + 1, dx, dy, dz, direction);
		list.add(steps);
		steps.buildComponent(this, list, rand);

		return steps;
	}

	/**
	 * build a new level under the exit
	 */
	public ComponentTFFinalCastleDungeonEntrance buildLevelUnder(StructureComponent parent, List<StructureComponent> list, Random rand, int level) {
		// find center of landing
		int dx = this.getXWithOffset(2, 19);
		int dy = this.getYWithOffset(-7);
		int dz = this.getZWithOffset(2, 19);

		// build a new dungeon level under there
		ComponentTFFinalCastleDungeonEntrance room = new ComponentTFFinalCastleDungeonEntrance(getFeatureType(), rand, 8, dx, dy, dz, getCoordBaseMode(), level);
		list.add(room);
		room.buildComponent(this, list, rand);

		return room;
	}

	/**
	 * build the boss room
	 */
	public ComponentTFFinalCastleDungeonForgeRoom buildBossRoomUnder(StructureComponent parent, List<StructureComponent> list, Random rand) {
		// find center of landing
		int dx = this.getXWithOffset(2, 19);
		int dy = this.getYWithOffset(-31);
		int dz = this.getZWithOffset(2, 19);

		// build a new dungeon level under there
		ComponentTFFinalCastleDungeonForgeRoom room = new ComponentTFFinalCastleDungeonForgeRoom(getFeatureType(), rand, 8, dx, dy, dz, this.coordBaseMode);
		list.add(room);
		room.buildComponent(this, list, rand);

		return room;
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		final IBlockState stairState = deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		for (int z = 0; z < 15; z++) {
			int y = 14 - z;

			this.fillWithBlocks(world, sbb, 0, y, z, 4, y, z, stairState, stairState, false);
			this.fillWithAir(world, sbb, 0, y + 1, z, 4, y + 6, z);
		}
		this.fillWithAir(world, sbb, 0, 0, 15, 4, 5, 19);

		return true;
	}

}
