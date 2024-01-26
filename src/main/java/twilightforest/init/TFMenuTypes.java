package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.inventory.UncraftingMenu;

public class TFMenuTypes {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, TwilightForestMod.ID);

	public static final DeferredHolder<MenuType<?>, MenuType<UncraftingMenu>> UNCRAFTING = CONTAINERS.register("uncrafting",
			() -> new MenuType<>(UncraftingMenu::fromNetwork, FeatureFlags.REGISTRY.allFlags()));
}
