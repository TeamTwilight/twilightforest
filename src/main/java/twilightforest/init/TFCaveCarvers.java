package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.NoiseCarverWallProvider;
import twilightforest.world.components.TFCavesCarver;

import java.util.List;

//this was all put into 1 class because it seems like a waste to have it in 2
public class TFCaveCarvers {
	public static final DeferredRegister<WorldCarver<?>> CARVER_TYPES = DeferredRegister.create(Registries.CARVER, TwilightForestMod.ID);

	public static final DeferredHolder<WorldCarver<?>, TFCavesCarver> TF_CAVES = CARVER_TYPES.register("tf_caves", () -> new TFCavesCarver(
		CaveCarverConfiguration.CODEC,
		false,
		new NoiseCarverWallProvider(
			6972119253061020355L,
			new NormalNoise.NoiseParameters(0, 1.0),
			0.5f,
			List.of(
				Blocks.DIRT.defaultBlockState(),
				Blocks.DIRT.defaultBlockState(),
				Blocks.ROOTED_DIRT.defaultBlockState(),
				Blocks.DIRT.defaultBlockState(),
				Blocks.DIRT.defaultBlockState(),
				Blocks.COARSE_DIRT.defaultBlockState(),
				Blocks.DIRT.defaultBlockState(),
				Blocks.DIRT.defaultBlockState()
			)
		)
	));
	public static final DeferredHolder<WorldCarver<?>, TFCavesCarver> HIGHLAND_CAVES = CARVER_TYPES.register("highland_caves", () -> {
		WeightedList<BlockState> highlandWalls = WeightedList.<BlockState>builder()
			.add(TFBlocks.TROLLSTEINN.value().defaultBlockState(), 1)
			.add(Blocks.STONE.defaultBlockState(), 3)
			.build();

		return new TFCavesCarver(
			CaveCarverConfiguration.CODEC,
			true,
			(random, _) -> highlandWalls.getRandomOrThrow(random)
		);
	});

	public static final ResourceKey<ConfiguredWorldCarver<?>> TFCAVES_CONFIGURED = registerKey("tf_caves");
	public static final ResourceKey<ConfiguredWorldCarver<?>> HIGHLANDCAVES_CONFIGURED = registerKey("highland_caves");

	private static ResourceKey<ConfiguredWorldCarver<?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_CARVER, TwilightForestMod.prefix(name));
	}

}
