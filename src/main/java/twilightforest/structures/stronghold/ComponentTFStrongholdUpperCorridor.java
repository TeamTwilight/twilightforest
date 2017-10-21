package twilightforest.structures.stronghold;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdUpperCorridor extends StructureTFStrongholdComponent {


	public ComponentTFStrongholdUpperCorridor() {
	}

	public ComponentTFStrongholdUpperCorridor(TFFeature feature, int i, EnumFacing facing, int x, int y, int z) {
		super(feature, i, facing, x, y, z);
	}

	@Override
	public StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z) {
		return StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, -2, -1, 0, 5, 5, 9, facing);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
		super.buildComponent(parent, list, random);

		// make a random component at the end
		addNewUpperComponent(parent, list, random, Rotation.NONE, 2, 1, 9);

	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		if (this.isLiquidInStructureBoundingBox(world, sbb)) {
			return false;
		} else {
			placeUpperStrongholdWalls(world, sbb, 0, 0, 0, 4, 4, 8, rand, deco.randomBlocks);

			// entrance doorway
			placeSmallDoorwayAt(world, rand, 2, 2, 1, 0, sbb);

			// end
			placeSmallDoorwayAt(world, rand, 2, 2, 1, 8, sbb);

			return true;
		}
	}

	@Override
	public boolean isComponentProtected() {
		return false;
	}
}
