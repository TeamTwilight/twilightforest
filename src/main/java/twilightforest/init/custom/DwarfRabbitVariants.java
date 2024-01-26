package twilightforest.init.custom;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.DwarfRabbitVariant;

public class DwarfRabbitVariants {
	public static final DeferredRegister<DwarfRabbitVariant> DWARF_RABBIT_VARIANTS = DeferredRegister.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, TwilightForestMod.ID);

	public static final DeferredHolder<DwarfRabbitVariant, DwarfRabbitVariant> BROWN = DWARF_RABBIT_VARIANTS.register("brown", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnybrown.png")));
	public static final DeferredHolder<DwarfRabbitVariant, DwarfRabbitVariant> DUTCH = DWARF_RABBIT_VARIANTS.register("dutch", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnydutch.png")));
	public static final DeferredHolder<DwarfRabbitVariant, DwarfRabbitVariant> WHITE = DWARF_RABBIT_VARIANTS.register("white", () -> new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnywhite.png")));

}
