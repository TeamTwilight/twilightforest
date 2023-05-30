package twilightforest.data.helpers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

public abstract class TFLangProvider extends LanguageProvider {
	public TFLangProvider(PackOutput output) {
		super(output, TwilightForestMod.ID, "en_us");
	}

	public void addBiome(ResourceKey<Biome> biome, String name) {
		this.add("biome.twilightforest." + biome.location().getPath(), name);
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
		this.add("block.twilightforest.hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_horizontal", "Hollow " + woodName + (stem ? " Stem" : " Log"));
		this.add("block.twilightforest.hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_vertical", "Hollow " + woodName + (stem ? " Stem" : " Log"));
		this.add("block.twilightforest.hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_climbable", "Hollow " + woodName + (stem ? " Stem" : " Log"));
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
		this.add("block.twilightforest." + woodPrefix + "_wall_sign", woodName + " Wall Sign");
		this.add("block.twilightforest." + woodPrefix + "_banister", woodName + " Banister");
		this.add("block.twilightforest." + woodPrefix + "_chest", woodName + " Chest");
		this.add("item.twilightforest." + woodPrefix + "_boat", woodName + " Boat");
		this.add("item.twilightforest." + woodPrefix + "_chest_boat", woodName + " Chest Boat");
		//this.add("block.twilightforest." + woodPrefix + "_hanging_sign", woodName + " Hanging Sign");
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

	public void addMusicDisc(Supplier<Item> disc, String description) {
		this.addItem(disc, "Music Disc");
		this.add(disc.get().getDescriptionId() + ".desc", description);
	}

	public void addStructure(ResourceKey<Structure> biome, String name) {
		this.add("structure.twilightforest." + biome.location().getPath(), name);
	}

	public void addAdvancement(String key, String title, String desc) {
		this.add("advancement.twilightforest." + key, title);
		this.add("advancement.twilightforest." + key + ".desc", desc);
	}

	public void addEnchantment(String key, String title, String desc) {
		this.add("enchantment.twilightforest." + key, title);
		this.add("enchantment.twilightforest." + key + ".desc", desc);
	}

	public void addEntityAndEgg(RegistryObject<? extends EntityType<?>> entity, String name) {
		this.addEntityType(entity, name);
		this.add("item.twilightforest." + entity.getId().getPath() + "_spawn_egg", name + " Spawn Egg");
	}

	public void addSubtitle(RegistryObject<SoundEvent> sound, String name) {
		String[] splitSoundName  = sound.getId().getPath().split("\\.", 3);
		this.add("subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2], name);
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
}
