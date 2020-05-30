package twilightforest.world.surfacebuilders;

import net.minecraft.block.Blocks;
import net.minecraft.util.LazyValue;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFSurfaceBuilders {
	// Biomes are registered before surface builders and need the raw objects. So don't use DeferredRegister here.
	public static final SurfaceBuilder<SurfaceBuilderConfig> DEFAULT_TF = new TFDefaultSurfaceBuilder(SurfaceBuilderConfig::deserialize);
	public static final SurfaceBuilder<SurfaceBuilderConfig> HIGHLANDS = new TFHighlandsSurfaceBuilder(SurfaceBuilderConfig::deserialize);
	public static final SurfaceBuilder<SurfaceBuilderConfig> PLATEAU = new TFPlateauSurfaceBuilder(SurfaceBuilderConfig::deserialize);

	public static final LazyValue<SurfaceBuilderConfig> FINAL_PLATEAU = new LazyValue<>(() -> new SurfaceBuilderConfig(TFBlocks.deadrock_weathered.get().getDefaultState(), TFBlocks.deadrock_cracked.get().getDefaultState(), Blocks.GRAVEL.getDefaultState()));

	public static void register(RegistryEvent.Register<SurfaceBuilder<?>> evt) {
		evt.getRegistry().register(DEFAULT_TF.setRegistryName(TwilightForestMod.ID, "default_tf"));
		evt.getRegistry().register(HIGHLANDS.setRegistryName(TwilightForestMod.ID, "highlands"));
		evt.getRegistry().register(PLATEAU.setRegistryName(TwilightForestMod.ID, "plateau"));
	}
}
