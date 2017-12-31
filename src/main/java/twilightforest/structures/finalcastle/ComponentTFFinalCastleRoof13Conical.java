package twilightforest.structures.finalcastle;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

/**
 * Pointy cone roof with variable height
 */
public class ComponentTFFinalCastleRoof13Conical extends StructureTFComponentOld {

	public int slope;

	public ComponentTFFinalCastleRoof13Conical() {
	}

	public ComponentTFFinalCastleRoof13Conical(TFFeature feature, Random rand, int i, StructureTFComponentOld sideTower) {
		super(feature, i);

		this.slope = 2 + rand.nextInt(3) + rand.nextInt(3);

		int height = slope * 4;

		this.setCoordBaseMode(sideTower.getCoordBaseMode());
		this.boundingBox = new StructureBoundingBox(sideTower.getBoundingBox().minX - 2, sideTower.getBoundingBox().maxY - 1, sideTower.getBoundingBox().minZ - 2, sideTower.getBoundingBox().maxX + 2, sideTower.getBoundingBox().maxY + height - 1, sideTower.getBoundingBox().maxZ + 2);

	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		super.writeStructureToNBT(tagCompound);
		tagCompound.setInteger("slope", this.slope);
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(tagCompound, templateManager);
		this.slope = tagCompound.getInteger("slope");
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			this.fillBlocksRotated(world, sbb, 0, -1, 0, 3, 2, 3, deco.blockState, rotation);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 2, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 1, -2, 1, rotation, sbb);
			this.setBlockStateRotated(world, deco.blockState, 2, -2, 1, rotation, sbb);

			this.fillBlocksRotated(world, sbb, 4, 0, 1, 12, 1, 1, deco.blockState, rotation);

			// more teeny crenellations
			for (int i = 3; i < 13; i += 2) {
				this.fillBlocksRotated(world, sbb, i, -1, 1, i, 2, 1, deco.blockState, rotation);
			}

			// cone roof
			for (int i = 2; i < 9; i++) {
				int base = 2 - slope;
				if (i < 7) {
					this.fillBlocksRotated(world, sbb, i - 1, ((i - 1) * slope) + base, i - 1, i, (i * slope) + base - 1, i, deco.blockState, rotation);
				} else {
					this.fillBlocksRotated(world, sbb, 16 - i, ((i - 1) * slope) + base, i, 16 - i, (i * slope) + base - 1, i, deco.roofState, rotation);
				}
				this.fillBlocksRotated(world, sbb, i + 1, ((i - 1) * slope) + base, i, 15 - i, (i * slope) + base - 1, i, deco.roofState, rotation);
			}

			// point!
			this.fillBlocksRotated(world, sbb, 8, (slope * 6) + 2, 8, 8, (slope * 7) + 2, 8, deco.roofState, rotation);

		}

		return true;
	}
}
