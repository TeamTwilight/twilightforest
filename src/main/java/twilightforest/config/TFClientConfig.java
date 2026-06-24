package twilightforest.config;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class TFClientConfig {

	final ModConfigSpec.BooleanValue silentCicadas;
	final ModConfigSpec.BooleanValue silentCicadasOnHead;
	final ModConfigSpec.BooleanValue firstPersonEffects;
	final ModConfigSpec.BooleanValue rotateTrophyHeadsGui;
	final ModConfigSpec.BooleanValue disableOptifineNagScreen;
	final ModConfigSpec.BooleanValue disableLockedBiomeToasts;
	final ModConfigSpec.BooleanValue showQuestRamCrosshairIndicator;
	final ModConfigSpec.BooleanValue showFortificationShieldIndicator;
	final ModConfigSpec.BooleanValue showFortificationShieldIndicatorInCreative;
	final ModConfigSpec.IntValue cloudBlockPrecipitationDistance;
	final ModConfigSpec.ConfigValue<List<? extends String>> giantSkinUUIDs;
	final ModConfigSpec.ConfigValue<List<? extends String>> auroraBiomes;
	final ModConfigSpec.BooleanValue prettifyOreMeterGui;
	final ModConfigSpec.BooleanValue spawnCharmAnimationAsTotem;
	final ModConfigSpec.BooleanValue manualTravellersWingsGradualGlide;
	final ModConfigSpec.BooleanValue firstPersonGloveOverlay;

	final ItemDisplay ITEM_DISPLAY = new ItemDisplay();

	public TFClientConfig(ModConfigSpec.Builder builder) {
		silentCicadas = builder
			.translation(TFConfig.CONFIG_ID + "silent_cicadas")
			.comment(ConfigComments.SILENT_CICADAS)
			.define("silentCicadas", false);

		silentCicadasOnHead = builder
			.translation(TFConfig.CONFIG_ID + "silent_cicadas_on_head")
			.comment(ConfigComments.SILENT_CICADAS_ON_HEAD)
			.define("silentCicadasOnHead", false);

		firstPersonEffects = builder
			.translation(TFConfig.CONFIG_ID + "screen_shake")
			.comment(ConfigComments.SCREEN_SHAKE)
			.define("screenShakingEffect", true);

		rotateTrophyHeadsGui = builder
			.translation(TFConfig.CONFIG_ID + "animate_trophies")
			.comment(ConfigComments.ANIMATE_TROPHIES)
			.define("rotateTrophyHeadsGui", true);

		disableOptifineNagScreen = builder
			.translation(TFConfig.CONFIG_ID + "optifine")
			.comment(ConfigComments.OPTIFINE)
			.define("disableOptifineNagScreen", false);

		disableLockedBiomeToasts = builder
			.translation(TFConfig.CONFIG_ID + "locked_toasts")
			.comment(ConfigComments.LOCKED_TOASTS)
			.define("disableLockedBiomeToasts", false);

		showQuestRamCrosshairIndicator = builder
			.translation(TFConfig.CONFIG_ID + "ram_indicator")
			.comment(ConfigComments.QUESTING_RAM_WOOL)
			.define("questRamWoolIndicator", true);

		showFortificationShieldIndicator = builder
			.translation(TFConfig.CONFIG_ID + "shield_indicator")
			.comment(ConfigComments.FORTIFICATION)
			.define("fortificationShieldIndicator", true);

		showFortificationShieldIndicatorInCreative = builder
			.translation(TFConfig.CONFIG_ID + "shield_indicator_creative")
			.comment(ConfigComments.FORTIFICATION_CREATIVE)
			.define("fortificationShieldIndicatorInCreative", false);

		cloudBlockPrecipitationDistance = builder
			.translation(TFConfig.CONFIG_ID + "cloud_precipitation")
			.comment(ConfigComments.CLOUD_PRECIP_CLIENT)
			.defineInRange("cloudBlockPrecipitationDistance", -1, -1, Integer.MAX_VALUE);

		giantSkinUUIDs = builder
			.translation(TFConfig.CONFIG_ID + "giant_skin_uuid_list")
			.comment(ConfigComments.GIANT_SKINS)
			.defineListAllowEmpty("giantSkinUUIDs", new ArrayList<>(), () -> "", s -> s instanceof String string && string.split("-").length == 5);

		auroraBiomes = builder
			.translation(TFConfig.CONFIG_ID + "aurora_biomes")
			.comment(ConfigComments.AURORA_SHADER)
			.defineListAllowEmpty("auroraBiomes", List.of("twilightforest:glacier"), () -> "", s -> s instanceof String string && Identifier.tryParse(string) != null);

		prettifyOreMeterGui = builder
			.translation(TFConfig.CONFIG_ID + "prettify_ore_meter_gui")
			.comment(ConfigComments.PRETTIFY_ORE_METER)
			.define("prettifyOreMeterGui", true);

		spawnCharmAnimationAsTotem = builder.translation(TFConfig.CONFIG_ID + "totem_charm_animation")
			.comment(ConfigComments.CHARMS_AS_TOTEMS)
			.define("totemCharmAnimation", false);

		manualTravellersWingsGradualGlide = builder.translation(TFConfig.CONFIG_ID + "manual_travellers_wings_gradual_glide")
			.comment(ConfigComments.MANUAL_TRAVELLERS_WINGS_GRADUAL_GLIDE)
			.define("travellersWingsGradualGlide", true);

		firstPersonGloveOverlay = builder.translation(TFConfig.CONFIG_ID + "first_person_glove_overlay")
			.comment(ConfigComments.FIRST_PERSON_GLOVE_OVERLAY)
			.define("firstPersonGloveOverlay", true);

		builder.comment(ConfigComments.ITEM_DISPLAY).translation(TFConfig.CONFIG_ID + "item_display").push("Item Display Modifier Settings");
		{

			ITEM_DISPLAY.screenOffsetX = builder
				.translation(TFConfig.CONFIG_ID + "screen_offset_x")
				.comment(ConfigComments.DISPLAY_SCREEN_OFFSET_X)
				.defineInRange("screenOffsetX", 4, Integer.MIN_VALUE, Integer.MAX_VALUE);

			ITEM_DISPLAY.screenOffsetY = builder
				.translation(TFConfig.CONFIG_ID + "screen_offset_y")
				.comment(ConfigComments.DISPLAY_SCREEN_OFFSET_Y)
				.defineInRange("screenOffsetY", 4, Integer.MIN_VALUE, Integer.MAX_VALUE);

			ITEM_DISPLAY.screenScale = builder
				.translation(TFConfig.CONFIG_ID + "screen_scale")
				.comment(ConfigComments.DISPLAY_SCALE)
				.defineInRange("screenScale", 1.0D, 0.1D, 10.0D);

			ITEM_DISPLAY.twentyFourHourFormat = builder
				.translation(TFConfig.CONFIG_ID + "twenty_four_hour_format")
				.comment(ConfigComments.TWENTY_FOUR_HOUR_FORMAT)
				.define("twentyFourHourFormat", TFConfig.use24HourTimeDefault());
		}
		builder.pop();
	}

	static class ItemDisplay {
		ModConfigSpec.IntValue screenOffsetX;
		ModConfigSpec.IntValue screenOffsetY;
		ModConfigSpec.DoubleValue screenScale;
		ModConfigSpec.BooleanValue twentyFourHourFormat;
	}
}
