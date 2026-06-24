package twilightforest.datagen.helpers;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class TFLangProvider extends LanguageProvider {
	private final Map<String, String> TF_TIPS = new HashMap<>();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final PackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;
	public final Map<String, String> upsideDownEntries = new HashMap<>();
	private final java.util.Map<String, String> customDataMap = new java.util.HashMap<>();

	public TFLangProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, TwilightForestMod.ID, "en_us");
		this.output = output;
		this.registries = registries;
	}

	@Override
	public void add(String key, String value) {
		if (customDataMap.containsKey(key) && customDataMap.get(key).equals(value)) {
			return;
		}

		customDataMap.put(key, value);

		try {
			super.add(key, value);
		} catch (Throwable _) {}

		try {
			List<LangFormatSplitter.Component> splitEnglish = LangFormatSplitter.split(value);
			this.upsideDownEntries.put(key, LangConversionHelper.convertComponents(splitEnglish));
		} catch (Throwable _) {}
	}

	@Override
	protected final void addTranslations() {}

	protected abstract void addTranslations(HolderLookup.Provider registries);

	public void addBiome(ResourceKey<@NotNull Biome> biome, String name) {
		this.add("biome.twilightforest." + biome.identifier().getPath(), name);
	}

	public void addSapling(String woodPrefix, String saplingName) {
		this.add("block.twilightforest." + woodPrefix + "_sapling", saplingName);
		this.add("block.twilightforest.potted_" + woodPrefix + "_sapling", "Potted " + saplingName);
	}

	public void createLogs(String woodPrefix, String woodName) {
		this.add("block.twilightforest." + woodPrefix + "_log", woodName + " Log");
		this.add("block.twilightforest." + woodPrefix + "_wood", woodName + " Wood");
		this.add("block.twilightforest.stripped_" + woodPrefix + "_log", "Stripped " + woodName + " Log");
		this.add("block.twilightforest.stripped_" + woodPrefix + "_wood", "Stripped " + woodName + " Wood");
		this.createHollowLogs(woodPrefix, woodName, false);
	}

	public void createHollowLogs(String woodPrefix, String woodName, boolean stem) {
		this.add("block.twilightforest.hollow_" + woodPrefix + (stem ? "_stem" : "_log"), "Hollow " + woodName + (stem ? " Stem" : " Log"));
	}

	public void createWoodSet(String woodPrefix, String woodName) {
		this.add("block.twilightforest." + woodPrefix + "_planks", woodName + " Planks");
		this.add("block.twilightforest." + woodPrefix + "_slab", woodName + " Slab");
		this.add("block.twilightforest." + woodPrefix + "_stairs", woodName + " Stairs");
		this.add("block.twilightforest." + woodPrefix + "_button", woodName + " Button");
		this.add("block.twilightforest." + woodPrefix + "_fence", woodName + " Fence");
		this.add("block.twilightforest." + woodPrefix + "_fence_gate", woodName + " Fence Gate");
		this.add("block.twilightforest." + woodPrefix + "_pressure_plate", woodName + " Pressure Plate");
		this.add("block.twilightforest." + woodPrefix + "_trapdoor", woodName + " Trapdoor");
		this.add("block.twilightforest." + woodPrefix + "_door", woodName + " Door");
		this.add("block.twilightforest." + woodPrefix + "_sign", woodName + " Sign");
		this.add("block.twilightforest." + woodPrefix + "_banister", woodName + " Banister");
		this.add("block.twilightforest." + woodPrefix + "_chest", woodName + " Chest");
		this.add("block.twilightforest." + woodPrefix + "_trapped_chest", "Trapped " + woodName + " Chest");
		this.add("item.twilightforest." + woodPrefix + "_boat", woodName + " Boat");
		this.add("entity.twilightforest." + woodPrefix + "_boat", woodName + " Boat");
		this.add("item.twilightforest." + woodPrefix + "_chest_boat", woodName + " Boat with Chest");
		this.add("entity.twilightforest." + woodPrefix + "_chest_boat", woodName + " Boat with Chest");
		this.add("block.twilightforest." + woodPrefix + "_hanging_sign", woodName + " Hanging Sign");
		this.add("block.twilightforest." + woodPrefix + "_drying_rack", woodName + " Drying Rack");
	}

	public <T> void addGameRule(DeferredHolder<@NotNull GameRule<?>, @NotNull GameRule<@NotNull T>> gameRule, String gameRuleName) {
		this.add("gamerule." + gameRule.get().id(), gameRuleName);
	}

	public <T> void addGameRuleDescription(DeferredHolder<@NotNull GameRule<?>, @NotNull GameRule<@NotNull T>> gameRule, String gameRuleDescription) {
		this.add("gamerule." + gameRule.get().id() + ".description", gameRuleDescription);
	}

	public void addBannerPattern(String patternPrefix, String patternName) {
		this.add("item.twilightforest." + patternPrefix + "_banner_pattern", "Banner Pattern");
		this.add("item.twilightforest." + patternPrefix + "_banner_pattern.desc", patternName);
		for (DyeColor color : DyeColor.values()) {
			this.add("block.minecraft.banner.twilightforest." + patternPrefix + "." + color.getName(), WordUtils.capitalize(color.getName().replace('_', ' ')) + " " + patternName);
		}
	}

	public void addStoneVariants(String blockKey, String blockName) {
		this.add("block.twilightforest." + blockKey, blockName);
		this.add("block.twilightforest.cracked_" + blockKey, "Cracked " + blockName);
		this.add("block.twilightforest.mossy_" + blockKey, "Mossy " + blockName);
	}

	public void addArmor(String itemKey, String item) {
		this.add("item.twilightforest." + itemKey + "_helmet", item + " Helmet");
		this.add("item.twilightforest." + itemKey + "_chestplate", item + " Chestplate");
		this.add("item.twilightforest." + itemKey + "_leggings", item + " Leggings");
		this.add("item.twilightforest." + itemKey + "_boots", item + " Boots");
	}

	public void addTools(String itemKey, String item) {
		this.add("item.twilightforest." + itemKey + "_sword", item + " Sword");
		this.add("item.twilightforest." + itemKey + "_pickaxe", item + " Pickaxe");
		this.add("item.twilightforest." + itemKey + "_axe", item + " Axe");
		this.add("item.twilightforest." + itemKey + "_shovel", item + " Shovel");
		this.add("item.twilightforest." + itemKey + "_hoe", item + " Hoe");
	}

	public void addMusicDisc(DeferredItem<@NotNull Item> disc, String description) {
		this.addItem(disc, "Music Disc");

		String discPath = disc.getId().getPath();
		String songName = discPath.replace("music_disc_", "");
		String songKey = "jukebox_song.twilightforest." + songName;

		this.add(songKey, description);
	}


	public void addStructure(ResourceKey<@NotNull Structure> biome, String name) {
		this.add("structure.twilightforest." + biome.identifier().getPath(), name);
	}

	public void addAdvancement(String key, String title, String desc) {
		this.add("advancement.twilightforest." + key, title);
		this.add("advancement.twilightforest." + key + ".desc", desc);
	}

	public void addEnchantment(String key, String title, String desc) {
		this.add("enchantment.twilightforest." + key, title);
		this.add("enchantment.twilightforest." + key + ".desc", desc);
	}

	public void addEntityAndEgg(DeferredHolder<@NotNull EntityType<?>, ? extends @NotNull EntityType<?>> entity, String name) {
		this.addEntityType(entity, name);
		this.add("item.twilightforest." + entity.getId().getPath() + "_spawn_egg", name + " Spawn Egg");
	}

	public void addDeathMessage(String key, String name) {
		this.add("death.attack.twilightforest." + key, name);
	}

	public void addStat(String key, String name) {
		this.add("stat.twilightforest." + key, name);
	}

	public void addMessage(String key, String name) {
		this.add("misc.twilightforest." + key, name);
	}

	public void addCommand(String key, String name) {
		this.add("commands.tffeature." + key, name);
	}

	public void addTrim(String key, String name) {
		this.add("trim_material.twilightforest." + key, name + " Material");
	}

	public void addBookAndContents(String bookKey, String bookTitle, String... pages) {
		this.add("twilightforest.book." + bookKey, bookTitle);
		int pageCount = 0;
		for (String page : pages) {
			pageCount++;
			this.add("twilightforest.book." + bookKey + "." + pageCount, page);
		}
	}

	public void addScreenMessage(String key, String name) {
		this.add("gui.twilightforest." + key, name);
	}

	public void addKeyBindCategory(KeyMapping.Category category, String name) {
		this.add(category.label().getString(), name);
	}

	public void addKeyMapping(KeyMapping keyMapping, String name) {
		this.add(keyMapping.getName(), name);
	}

	public void addTravellersModifier(HolderLookup.Provider registries, ResourceKey<@NotNull TravellersModifier> modifier, String name) {
		String translationKey = modifier.identifier().toLanguageKey("travellers_modifier");
		this.add(translationKey, name);
	}

	public void addTravellersDescription(HolderLookup.Provider registries, ResourceKey<@NotNull TravellersModifier> modifier, String description) {
		String translationKey = modifier.identifier().toLanguageKey("travellers_modifier", "description");
		this.add(translationKey, description);
	}

	public void createTip(String key, String translation) {
		String fullKey = "twilightforest.tips." + key;
		this.add(fullKey, translation);
		TF_TIPS.put(fullKey, key);
	}

	public void translateTag(TagKey<?> tag, String name) {
		this.add(String.format("tag.%s.%s.%s", tag.registry().identifier().getPath(), tag.location().getNamespace(), tag.location().getPath().replace('/', '.')), name);
	}

	public void configEntry(String key, String name, String description) {
		this.add(TFConfig.CONFIG_ID + key, name);
		this.add(TFConfig.CONFIG_ID + key + ".tooltip", description);
	}

	public void configCategory(String key, String name, String description) {
		this.add(TFConfig.CONFIG_ID + key, name);
		this.add(TFConfig.CONFIG_ID + key + ".tooltip", description);
		this.add(name + ".button", "Edit");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		ImmutableList.Builder<@NotNull CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		CompletableFuture<?> languageGen = this.registries.thenCompose(provider -> {
			this.addTranslations(provider);
			JsonObject normalLangFile = new JsonObject();
			return super.run(cache);
		});
		futuresBuilder.add(languageGen);

		JsonObject upsideDownFile = new JsonObject();
		this.upsideDownEntries.forEach(upsideDownFile::addProperty);
		futuresBuilder.add(DataProvider.saveStable(cache, upsideDownFile, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(TwilightForestMod.ID).resolve("lang").resolve("en_ud.json")));

		for (Map.Entry<String, String> entry : TF_TIPS.entrySet()) {
			JsonObject object = new JsonObject();
			object.addProperty("type", "tipsmod:simple");
			Component tooltipText = Component.translatable(entry.getKey()).withStyle(ChatFormatting.GREEN);
			object.add("text", ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, tooltipText).getOrThrow());
			futuresBuilder.add(DataProvider.saveStable(cache, GSON.toJsonTree(object), this.output.getOutputFolder().resolve("assets/twilightforest/tips/" + entry.getValue() + ".json")));
		}

		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

}
