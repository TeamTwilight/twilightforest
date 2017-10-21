package twilightforest.structures.darktower;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;

public class ComponentTFDarkTowerEntrance extends ComponentTFDarkTowerWing {

	public ComponentTFDarkTowerEntrance() {
	}

	protected ComponentTFDarkTowerEntrance(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i, x, y, z, pSize, pHeight, direction);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		super.buildComponent(parent, list, rand);

		// a few more openings
		addOpening(size / 2, 1, 0, Rotation.CLOCKWISE_90, EnumDarkTowerDoor.REAPPEARING);
		addOpening(size / 2, 1, size - 1, Rotation.COUNTERCLOCKWISE_90, EnumDarkTowerDoor.REAPPEARING);
	}

	@Override
	public void makeABeard(StructureComponent parent, List<StructureComponent> list, Random rand) {
	}

	@Override
	public void makeARoof(StructureComponent parent, List<StructureComponent> list, Random rand) {
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// make walls
		makeEncasedWalls(world, rand, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

		// deco to ground
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				this.setBlockState(world, deco.accentState, x, -1, z, sbb);
			}
		}

		// clear inside
		fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		// sky light
		nullifySkyLightForBoundingBox(world);

		// openings
		makeOpenings(world, sbb);

		return true;
	}

}
