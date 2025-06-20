package twilightforest.events;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.GrindstoneEvent;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiers;

import java.util.List;
import java.util.stream.Stream;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class GrindstoneEvents {
	@SubscribeEvent
	public static void removeModifiersFromTravellersGear(GrindstoneEvent.OnPlaceItem event) {
		List<ItemStack> travellersItemStacks = Stream.of(event.getTopItem(), event.getBottomItem())
			.filter(stack -> stack.getItem() instanceof TravellersArmorItem)
			.toList();

		if (travellersItemStacks.isEmpty())
			return; // Delegate to vanilla logic
		if (travellersItemStacks.size() > 1) {
			event.setCanceled(true);
			return;
		}
		ItemStack inputStack = travellersItemStacks.getFirst();
		List<InsertableTravellersModifier> modifiers = TravellersModifiers.findAllInsertableModifiers(inputStack);
		if (modifiers.isEmpty()) {
			event.setCanceled(true);
			return;
		}

		ItemStack unmodifiedStack = inputStack.copy();
		modifiers.forEach(modifier -> modifier.removeModifier(unmodifiedStack));
		ItemStack outputStack = unmodifiedStack.copy();
		if (outputStack.is(TFItems.TRAVELLERS_WINGS_BELT))
			outputStack = new ItemStack(TFItems.TRAVELLERS_WINGS, outputStack.getCount(), outputStack.getComponentsPatch());
		event.setOutput(outputStack);
	}
}
