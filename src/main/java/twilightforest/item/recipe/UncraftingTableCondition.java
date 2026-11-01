package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import twilightforest.config.TFConfig;

public class UncraftingTableCondition implements ICondition {

	public static final UncraftingTableCondition INSTANCE = new UncraftingTableCondition();
	public static final MapCodec<UncraftingTableCondition> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	// PORTAGE : Forge passe aussi les operations de serialisation a la condition ; NeoForge non.
	// Elles ne servent pas ici, mais la signature doit correspondre.
	@Override
	public boolean test(IContext context, com.mojang.serialization.DynamicOps<?> ops) {
		return !TFConfig.disableEntireTable;
	}

	@Override
	public String toString() {
		return "Uncrafting Table Enabled";
	}
}
