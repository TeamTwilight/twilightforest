package twilightforest.structures.minotaurmaze;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.structures.StructureTFComponent;

import java.util.List;
import java.util.Random;

/**
 * This component is the base for the maze/ruins combo feature.  There are village-like ruins above and a maze underneath.
 *
 * @author Ben
 */
public class ComponentTFMazeRuins extends StructureTFComponent {

	public ComponentTFMazeRuins() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComponentTFMazeRuins(World world, Random rand, int i, int x, int y, int z) {
		super(i);
		this.setCoordBaseMode(EnumFacing.SOUTH);

		// I have no bounding box
		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 0, 0, 0, EnumFacing.SOUTH);

	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list, Random random) {
		super.buildComponent(structurecomponent, list, random);

		// add a maze
		ComponentTFMinotaurMaze maze = new ComponentTFMinotaurMaze(1, boundingBox.minX, boundingBox.minY - 14, boundingBox.minZ, 1);
		list.add(maze);
		maze.buildComponent(this, list, random);

		// add maze entrance shaft
		ComponentTFMazeEntranceShaft mazeEnter = new ComponentTFMazeEntranceShaft(2, random, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ + 1);
		list.add(mazeEnter);
		mazeEnter.buildComponent(this, list, random);

		// add aboveground maze entrance building
		ComponentTFMazeMound mazeAbove = new ComponentTFMazeMound(2, random, boundingBox.minX - 14, boundingBox.minY, boundingBox.minZ - 14);
		list.add(mazeAbove);
		mazeAbove.buildComponent(this, list, random);

	}


	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
	 * the end, it adds Fences...
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// I have no components
		return true;
	}

}
