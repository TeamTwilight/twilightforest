package twilightforest.enums;

import net.minecraft.block.BlockPlanks;
import net.minecraft.util.IStringSerializable;
import twilightforest.util.IMapColorSupplier;

import java.util.Locale;

public enum MagicWoodVariant implements IStringSerializable, IMapColorSupplier {

	TIME(BlockPlanks.EnumType.JUNGLE),
	TRANS(BlockPlanks.EnumType.OAK),
	MINE(BlockPlanks.EnumType.BIRCH),
	SORT(BlockPlanks.EnumType.SPRUCE);

	private final BlockPlanks.EnumType plankType;

	MagicWoodVariant(BlockPlanks.EnumType plankType) {
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
