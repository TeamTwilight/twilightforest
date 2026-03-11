package twilightforest.init.custom;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.DwarfRabbitVariant;

public class DwarfRabbitVariants {
	public static final ResourceKey<DwarfRabbitVariant> BROWN = makeKey(TwilightForestMod.prefix("brown"));
	public static final ResourceKey<DwarfRabbitVariant> DUTCH = makeKey(TwilightForestMod.prefix("dutch"));
	public static final ResourceKey<DwarfRabbitVariant> WHITE = makeKey(TwilightForestMod.prefix("white"));

	private static ResourceKey<DwarfRabbitVariant> makeKey(ResourceLocation name) {
		return ResourceKey.create(TFRegistries.Keys.DWARF_RABBIT_VARIANT, name);
	}


	public static void bootstrap(BootstrapContext<DwarfRabbitVariant> context) {
		context.register(BROWN, new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnybrown.png")));
		context.register(DUTCH, new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnydutch.png")));
		context.register(WHITE, new DwarfRabbitVariant(TwilightForestMod.getModelTexture("bunnywhite.png")));
	}
}
