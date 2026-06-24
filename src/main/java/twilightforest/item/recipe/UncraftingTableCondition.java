package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import twilightforest.config.TFConfig;

public class UncraftingTableCondition implements ICondition {

	public static final UncraftingTableCondition INSTANCE = new UncraftingTableCondition();
	public static final MapCodec<UncraftingTableCondition> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(IContext context) {
		return !TFConfig.disableEntireTable;
	}

	@Override
	public String toString() {
		return "Uncrafting Table Enabled";
	}
}
