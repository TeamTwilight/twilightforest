package twilightforest.enums;

import net.minecraft.block.BlockPlanks;
import net.minecraft.util.IStringSerializable;
import twilightforest.util.IMapColorSupplier;

import java.util.Locale;

public enum WoodVariant implements IStringSerializable, IMapColorSupplier {

	OAK(BlockPlanks.EnumType.OAK),
	CANOPY(BlockPlanks.EnumType.SPRUCE),
	MANGROVE(BlockPlanks.EnumType.JUNGLE),
	DARK(BlockPlanks.EnumType.ACACIA);

	private final BlockPlanks.EnumType plankType;

	WoodVariant(BlockPlanks.EnumType plankType) {
		this.plankType = plankType;
	}

	@Override
	public BlockPlanks.EnumType supplyPlankColor() {
		return plankType;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
