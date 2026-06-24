package twilightforest.init.custom;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.client.overlay.display.ClockDisplay;
import twilightforest.client.overlay.display.CompassDisplay;
import twilightforest.client.overlay.display.MapDisplay;
import twilightforest.client.overlay.display.MoonDialDisplay;
import twilightforest.init.TFItems;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

import java.util.Optional;

public class ItemDisplays {

	public static final DeferredRegister<ItemDisplayType> DISPLAYS = DeferredRegister.create(TFRegistries.Keys.ITEM_DISPLAY_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<ItemDisplayType, ItemDisplayType> MAP = DISPLAYS.register("map", () -> new ItemDisplayType(stack -> stack.getItem() instanceof MapItem, () -> new MapDisplay(), Optional.of(TwilightForestMod.prefix("textures/item/map_display.png"))));
	public static final DeferredHolder<ItemDisplayType, ItemDisplayType> COMPASS = DISPLAYS.register("compass", () -> new ItemDisplayType(stack -> stack.is(Items.COMPASS), () -> new CompassDisplay(), Optional.of(TwilightForestMod.prefix("textures/item/compass_display.png"))));
	public static final DeferredHolder<ItemDisplayType, ItemDisplayType> CLOCK = DISPLAYS.register("clock", () -> new ItemDisplayType(stack -> stack.is(Items.CLOCK), () -> new ClockDisplay(), Optional.of(TwilightForestMod.prefix("textures/item/clock_display.png"))));
	public static final DeferredHolder<ItemDisplayType, ItemDisplayType> MOON_DIAL = DISPLAYS.register("moon_dial", () -> new ItemDisplayType(stack -> stack.is(TFItems.MOON_DIAL), () -> new MoonDialDisplay(), Optional.of(TwilightForestMod.prefix("textures/item/moon_dial_display.png"))));
}
