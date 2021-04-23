package twilightforest.world.surfacebuilders;

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFSurfaceBuilders {
	// Biomes are registered before surface builders and need the raw objects. So don't use DeferredRegister here.
	// Do we need to keep these? What do these do that vanilla cannot?
	public static final TFDefaultSurfaceBuilder DEFAULT_TF = new TFDefaultSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_);
	public static final TFHighlandsSurfaceBuilder HIGHLANDS = new TFHighlandsSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_);
	public static final TFPlateauSurfaceBuilder PLATEAU = new TFPlateauSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_);

	static {
		// setRegistryName for some dumb reason erases the type for SurfaceBuilder<> and we have to keep the type for later in the line, so the reg names are kept here instead
		DEFAULT_TF.setRegistryName(TwilightForestMod.ID, "default");
		HIGHLANDS.setRegistryName(TwilightForestMod.ID, "highlands");
		PLATEAU.setRegistryName(TwilightForestMod.ID, "plateau");
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<SurfaceBuilder<?>> evt) {
		evt.getRegistry().register(DEFAULT_TF);
		evt.getRegistry().register(HIGHLANDS);
		evt.getRegistry().register(PLATEAU);
	}
}
