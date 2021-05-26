package twilightforest.structures.mushroomtower;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
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
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerRoof;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.List;
import java.util.Random;

public class ComponentTFTowerRoofMushroom extends ComponentTFTowerRoof {

	public ComponentTFTowerRoofMushroom(TemplateManager manager, CompoundNBT nbt) {
		super(TFMushroomTowerPieces.TFMTRoofMush, nbt);
	}

	public ComponentTFTowerRoofMushroom(TFFeature feature, int i, ComponentTFTowerWing wing, float pHang) {
		super(TFMushroomTowerPieces.TFMTRoofMush, feature, i);
		this.height = wing.size;
		int overhang = (int) (height * pHang);
		this.size = height + (overhang * 2);
		this.setCoordBaseMode(Direction.SOUTH);
		this.boundingBox = new MutableBoundingBox(wing.getBoundingBox().minX - overhang, wing.getBoundingBox().maxY + 2, wing.getBoundingBox().minZ - overhang, wing.getBoundingBox().maxX + overhang, wing.getBoundingBox().maxY + this.height + 1, wing.getBoundingBox().maxZ + overhang);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	/**
	 * Makes a pointy roof out of stuff
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		for (int y = 0; y <= height; y++) {

			int radius = (int) (MathHelper.sin((y + height / 1.2F) / (height * 2.05F) * 3.14F) * this.size / 2F);
			int hollow = MathHelper.floor(radius * .9F);

			if ((height - y) < 3) {
				hollow = -1;
			}

			makeCircle(world, y, radius, hollow, sbb);
		}

		return true;
	}

	private void makeCircle(ISeedReader world, int y, int radius, int hollow, MutableBoundingBox sbb) {

		int cx = size / 2;
		int cz = size / 2;

		// build the trunk upwards
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				// determine how far we are from the center.
				float dist = MathHelper.sqrt(dx * dx + dz * dz);

				// make a trunk!
				if (dist <= (radius + 0.5F)) {
					if (dist > hollow) {
						setBlockState(world, deco.accentState, dx + cx, y, dz + cz, sbb);
					} else {
						// spore
						setBlockState(world, deco.accentState.getBlock().getDefaultState(), dx + cx, y, dz + cz, sbb);

					}
				}
			}
		}
	}
}
