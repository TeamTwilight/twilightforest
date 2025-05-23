package twilightforest.world.components.structures.lichtower;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;

public class TowerRoofPointyOverhangComponent extends TowerRoofPointyComponent {

	public TowerRoofPointyOverhangComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTRPO.get(), nbt);
	}

	public TowerRoofPointyOverhangComponent(int i, TowerWingComponent wing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFLTRPO.get(), i, wing, x, y, z);

		// same facing, but it doesn't matter
		this.setOrientation(wing.getOrientation());

		this.size = wing.size + 2; // assuming only square towers and roofs right now.
		this.height = size;

		// just hang out at the very top of the tower
		makeOverhangBB(wing);
	}
}
