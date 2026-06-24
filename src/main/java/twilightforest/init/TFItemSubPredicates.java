package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.predicate.ItemColorPredicate;

public class TFItemSubPredicates {

	//TODO: These no longer exist. Check for their uses and port accordingly.
//	public static final DeferredRegister<ItemSubPredicate.Type<?>> TYPES = DeferredRegister.create(Registries.ITEM_SUB_PREDICATE_TYPE, TwilightForestMod.ID);
//
//	public static final DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<ItemColorPredicate>> COLOR = TYPES.register("color", () -> new ItemSubPredicate.Type<>(ItemColorPredicate.CODEC));
}
