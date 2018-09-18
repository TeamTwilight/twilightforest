package twilightforest.enums;

import net.minecraft.block.BlockPlanks;
import net.minecraft.util.IStringSerializable;
import twilightforest.util.IMapColorSupplier;

import java.util.Locale;

public enum WoodVariant implements IStringSerializable, IMapColorSupplier {
	OAK {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.OAK;
		}
	},
	CANOPY {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.SPRUCE;
		}
	},
	MANGROVE {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.JUNGLE;
		}
	},
	DARK {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.ACACIA;
		}
	};

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
