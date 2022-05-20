package twilightforest.inventory;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.client.UncraftingGui;

import static twilightforest.TFConstants.MOD_ID;

public class TFContainers {

	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

	public static final RegistryObject<MenuType<UncraftingContainer>> UNCRAFTING = CONTAINERS.register("uncrafting",
			() -> new MenuType<>(UncraftingContainer::fromNetwork));

	@OnlyIn(Dist.CLIENT)
	public static void renderScreens() {
		MenuScreens.register(UNCRAFTING.get(), UncraftingGui::new);
	}
}
