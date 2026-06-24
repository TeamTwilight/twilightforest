package twilightforest.item.travellers_gear.modifiers.display;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.overlay.display.ItemDisplay;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record ItemDisplayType(Predicate<ItemStack> validItems, Supplier<ItemDisplay> display, Optional<Identifier> slotTexture) {
}
