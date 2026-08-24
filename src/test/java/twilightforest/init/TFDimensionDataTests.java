package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TFDimensionDataTests {

	private static final DensityFunction.FunctionContext TEST_POSITION = new DensityFunction.SinglePointContext(0, -1, 0);

	@Test
	void twilightNoiseSettingsEnableAquifers() {
		NoiseGeneratorSettings settings = makeNoiseSettings(false, -0.1);

		assertTrue(settings.isAquifersEnabled(), "Twilight Forest carvers must use enabled aquifers");
	}

	@Test
	void skylightNoiseSettingsKeepAquifersDisabled() {
		NoiseGeneratorSettings settings = makeNoiseSettings(true, -0.1);

		assertFalse(settings.isAquifersEnabled(), "Air-filled skylight terrain does not need aquifers");
		assertEquals(0.0, settings.noiseRouter().fluidLevelFloodednessNoise().compute(TEST_POSITION));
	}

	@Test
	void naturalTerrainOpeningsRemainFlooded() {
		NoiseGeneratorSettings settings = makeNoiseSettings(false, -0.1);

		assertEquals(1.0, settings.noiseRouter().fluidLevelFloodednessNoise().compute(TEST_POSITION));
	}

	@Test
	void terrainRemovedLaterByCarversRemainsDry() {
		NoiseGeneratorSettings settings = makeNoiseSettings(false, 0.1);

		assertEquals(-1.0, settings.noiseRouter().fluidLevelFloodednessNoise().compute(TEST_POSITION));
	}

	@Test
	void densityBoundaryRemainsDry() {
		NoiseGeneratorSettings settings = makeNoiseSettings(false, 0.0);

		assertEquals(-1.0, settings.noiseRouter().fluidLevelFloodednessNoise().compute(TEST_POSITION));
	}

	private static NoiseGeneratorSettings makeNoiseSettings(boolean skylight, double terrainDensity) {
		BootstrapContext<NoiseGeneratorSettings> context = mock();
		HolderGetter<DensityFunction> densityFunctions = mock();
		Holder.Reference<DensityFunction> densityFunction = mock();

		when(context.lookup(Registries.DENSITY_FUNCTION)).thenReturn(densityFunctions);
		when(densityFunctions.getOrThrow(
			skylight ? TFDensityFunctions.SKYLIGHT_TERRAIN : TFDensityFunctions.FORESTED_TERRAIN
		)).thenReturn(densityFunction);
		when(densityFunction.value()).thenReturn(DensityFunctions.constant(terrainDensity));

		return TFDimensionData.makeNoiseSettings(context, skylight);
	}
}
