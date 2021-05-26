package twilightforest.structures.finalcastle;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleEntranceBottomTower extends ComponentTFFinalCastleMazeTower13 {

	public ComponentTFFinalCastleEntranceBottomTower(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCEnBoTo, nbt);
	}

	public ComponentTFFinalCastleEntranceBottomTower(TFFeature feature, Random rand, int i, int x, int y, int z, int floors, int entranceFloor, Direction direction) {
		super(TFFinalCastlePieces.TFFCEnBoTo, feature, rand, i, x, y, z, floors, entranceFloor, TFBlocks.castle_rune_brick_pink.get().getDefaultState(), direction);

    }

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// stairs
		addStairs(list, rand, this.getComponentType() + 1, this.size - 1, 1, size / 2, Rotation.NONE);
		addStairs(list, rand, this.getComponentType() + 1, 0, 1, size / 2, Rotation.CLOCKWISE_180);
		addStairs(list, rand, this.getComponentType() + 1, this.size / 2, 1, 0, Rotation.COUNTERCLOCKWISE_90);
		addStairs(list, rand, this.getComponentType() + 1, this.size / 2, 1, this.size - 1, Rotation.CLOCKWISE_90);
	}

	/**
	 * Add some stairs leading to this tower
	 */
	private boolean addStairs(List<StructurePiece> list, Random rand, int index, int x, int y, int z, Rotation rotation) {
		// add door
		this.addOpening(x, y, z, rotation);

		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dx = offsetTowerCCoords(x, y, z, 0, direction);

		ComponentTFFinalCastleEntranceStairs stairs = new ComponentTFFinalCastleEntranceStairs(getFeatureType(), index, dx.getX(), dx.getY(), dx.getZ(), direction);

		list.add(stairs);
		stairs.buildComponent(list.get(0), list, rand);
		return true;
	}

	@Override
	protected boolean hasAccessibleRoof() {
		return false;
	}
}
