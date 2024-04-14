package twilightforest.config;

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
	final ModConfigSpec.IntValue cloudBlockPrecipitationDistance;
	final ModConfigSpec.ConfigValue<List<? extends String>> giantSkinUUIDs;
	final ModConfigSpec.ConfigValue<List<? extends String>> auroraBiomes;
	final ModConfigSpec.BooleanValue prettifyOreMeterGui;
	final ModConfigSpec.BooleanValue spawnCharmAnimationAsTotem;

	public TFClientConfig(ModConfigSpec.Builder builder) {
		silentCicadas = builder
				.translation(TFConfig.CONFIG_ID + "silent_cicadas")
				.comment("Make cicadas silent for those having sound library problems, or otherwise finding them annoying.")
				.define("silentCicadas", false);

		silentCicadasOnHead = builder
				.translation(TFConfig.CONFIG_ID + "silent_cicadas_on_head")
				.comment("Make cicadas silent when sitting on your head. If the above option is already true, this won't have any effect.")
				.define("silentCicadasOnHead", false);

		firstPersonEffects = builder
				.translation(TFConfig.CONFIG_ID + "first_person_effects")
				.comment("Controls whether various effects from the mod are rendered while in first-person view. Turn this off if you find them distracting.")
				.define("firstPersonEffects", true);

		rotateTrophyHeadsGui = builder
				.translation(TFConfig.CONFIG_ID + "animate_trophies")
				.comment("Rotate trophy heads on item model. Has no performance impact at all. For those who don't like fun.")
				.define("rotateTrophyHeadsGui", true);

		disableOptifineNagScreen = builder
				.translation(TFConfig.CONFIG_ID + "optifine")
				.comment("Disable the nag screen when Optifine is installed.")
				.define("disableOptifineNagScreen", false);

		disableLockedBiomeToasts = builder
				.translation(TFConfig.CONFIG_ID + "locked_toasts")
				.comment("Disables the toasts that appear when a biome is locked. Not recommended if you're not familiar with progression.")
				.define("disableLockedBiomeToasts", false);

		showQuestRamCrosshairIndicator = builder
				.translation(TFConfig.CONFIG_ID + "ram_indicator")
				.comment("Renders a little check mark or x above your crosshair depending on if fed the Questing Ram that color of wool. Turn this off if you find it intrusive.")
				.define("questRamWoolIndicator", true);

		showFortificationShieldIndicator = builder
				.translation(TFConfig.CONFIG_ID + "shield_indicator")
				.comment("Renders how many fortification shields are currently active on your player above your armor bar. Turn this off if you find it intrusive or other mods render over/under it.")
				.define("fortificationShieldIndicator", true);

		cloudBlockPrecipitationDistance = builder
				.translation(TFConfig.CONFIG_ID + "cloud_block_precipitation_distance")
				.comment("""
						Renders precipitation underneath cloud blocks. -1 sets it to be synced with the common config.
						Set this to a lower number if you're experiencing poor performance, or set it to 0 if you wish to turn it off""")
				.defineInRange("cloudBlockPrecipitationDistance", -1, -1, Integer.MAX_VALUE);

		giantSkinUUIDs = builder
				.translation(TFConfig.CONFIG_ID + "giant_skin_uuid_list")
				.comment("""
						List of player UUIDs whose skins the giants of Twilight Forest should use.
						If left empty, the giants will appear the same as the player viewing them does.""")
				.defineListAllowEmpty("giantSkinUUIDs", new ArrayList<>(), s -> s instanceof String);

		auroraBiomes = builder
				.translation(TFConfig.CONFIG_ID + "aurora_biomes")
				.comment("Defines which biomes the aurora shader effect will appear in. Leave the list empty to disable the effect.")
				.defineListAllowEmpty("auroraBiomes", List.of("twilightforest:glacier"), s -> s instanceof String);

		prettifyOreMeterGui = builder
				.translation(TFConfig.CONFIG_ID + "prettify_ore_meter_gui")
				.comment("Lines up dashes & percentages in Ore Meter GUI")
				.define("prettifyOreMeterGui", true);

		spawnCharmAnimationAsTotem = builder.translation(TFConfig.CONFIG_ID + "totem_charm_animation")
				.comment("If true, Twilight Forest charm items will display similar to the totem of undying when used.")
				.define("totemCharmAnimation", false);
	}
}
