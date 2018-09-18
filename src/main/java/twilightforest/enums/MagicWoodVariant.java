package twilightforest.enums;

import net.minecraft.block.BlockPlanks;
import net.minecraft.util.IStringSerializable;
import twilightforest.util.IMapColorSupplier;

import java.util.Locale;

public enum MagicWoodVariant implements IStringSerializable, IMapColorSupplier {
	TIME {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.JUNGLE;
		}
	},
	TRANS {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.OAK;
		}
	},
	MINE {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.BIRCH;
		}
	},
	SORT {
		@Override
		public BlockPlanks.EnumType supplyPlankColor() {
			return BlockPlanks.EnumType.SPRUCE;
		}
	};

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
