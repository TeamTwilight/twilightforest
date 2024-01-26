package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBannerPatterns;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFDimensionSettings;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.WoodPalette;

import java.util.concurrent.CompletableFuture;

//a place to hold all custom tags, since I imagine we wont have a lot of them
public class CustomTagGenerator {

	public static class EnchantmentTagGenerator extends TagsProvider<Enchantment> {

		public static final TagKey<Enchantment> PHANTOM_ARMOR_BANNED_ENCHANTS = TagKey.create(Registries.ENCHANTMENT, TwilightForestMod.prefix("phantom_armor_banned_enchants"));

		public EnchantmentTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
			super(output, Registries.ENCHANTMENT, provider, TwilightForestMod.ID, helper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(PHANTOM_ARMOR_BANNED_ENCHANTS).add(BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.VANISHING_CURSE).get(), BuiltInRegistries.ENCHANTMENT.getResourceKey(Enchantments.BINDING_CURSE).get());
		}

		@Override
		public String getName() {
			return "Twilight Forest Enchantment Tags";
		}
	}

	public static class BlockEntityTagGenerator extends TagsProvider<BlockEntityType<?>> {

		public static final TagKey<BlockEntityType<?>> RELOCATION_NOT_SUPPORTED = TagKey.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("forge", "relocation_not_supported"));
		public static final TagKey<BlockEntityType<?>> IMMOVABLE = TagKey.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("forge", "immovable"));

		public BlockEntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
			super(output, Registries.BLOCK_ENTITY_TYPE, provider, TwilightForestMod.ID, helper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(RELOCATION_NOT_SUPPORTED).add(
					TFBlockEntities.ANTIBUILDER.getKey(),
					TFBlockEntities.BEANSTALK_GROWER.getKey(),
					TFBlockEntities.NAGA_SPAWNER.getKey(),
					TFBlockEntities.LICH_SPAWNER.getKey(),
					TFBlockEntities.MINOSHROOM_SPAWNER.getKey(),
					TFBlockEntities.HYDRA_SPAWNER.getKey(),
					TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.getKey(),
					TFBlockEntities.UR_GHAST_SPAWNER.getKey(),
					TFBlockEntities.ALPHA_YETI_SPAWNER.getKey(),
					TFBlockEntities.SNOW_QUEEN_SPAWNER.getKey(),
					TFBlockEntities.FINAL_BOSS_SPAWNER.getKey());

			this.tag(IMMOVABLE).add(
					TFBlockEntities.ANTIBUILDER.getKey(),
					TFBlockEntities.BEANSTALK_GROWER.getKey(),
					TFBlockEntities.NAGA_SPAWNER.getKey(),
					TFBlockEntities.LICH_SPAWNER.getKey(),
					TFBlockEntities.MINOSHROOM_SPAWNER.getKey(),
					TFBlockEntities.HYDRA_SPAWNER.getKey(),
					TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.getKey(),
					TFBlockEntities.UR_GHAST_SPAWNER.getKey(),
					TFBlockEntities.ALPHA_YETI_SPAWNER.getKey(),
					TFBlockEntities.SNOW_QUEEN_SPAWNER.getKey(),
					TFBlockEntities.FINAL_BOSS_SPAWNER.getKey());
		}

		@Override
		public String getName() {
			return "Twilight Forest Block Entity Tags";
		}
	}

	public static class BannerPatternTagGenerator extends TagsProvider<BannerPattern> {

		public static final TagKey<BannerPattern> NAGA_BANNER_PATTERN = create("pattern_item/naga");
		public static final TagKey<BannerPattern> LICH_BANNER_PATTERN = create("pattern_item/lich");
		public static final TagKey<BannerPattern> MINOSHROOM_BANNER_PATTERN = create("pattern_item/minoshroom");
		public static final TagKey<BannerPattern> HYDRA_BANNER_PATTERN = create("pattern_item/hydra");
		public static final TagKey<BannerPattern> KNIGHT_PHANTOM_BANNER_PATTERN = create("pattern_item/knight_phantom");
		public static final TagKey<BannerPattern> UR_GHAST_BANNER_PATTERN = create("pattern_item/ur_ghast");
		public static final TagKey<BannerPattern> ALPHA_YETI_BANNER_PATTERN = create("pattern_item/alpha_yeti");
		public static final TagKey<BannerPattern> SNOW_QUEEN_BANNER_PATTERN = create("pattern_item/snow_queen");
		public static final TagKey<BannerPattern> QUEST_RAM_BANNER_PATTERN = create("pattern_item/quest_ram");

		public BannerPatternTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, Registries.BANNER_PATTERN, provider, TwilightForestMod.ID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(NAGA_BANNER_PATTERN).add(TFBannerPatterns.NAGA.getKey());
			tag(LICH_BANNER_PATTERN).add(TFBannerPatterns.LICH.getKey());
			tag(MINOSHROOM_BANNER_PATTERN).add(TFBannerPatterns.MINOSHROOM.getKey());
			tag(HYDRA_BANNER_PATTERN).add(TFBannerPatterns.HYDRA.getKey());
			tag(KNIGHT_PHANTOM_BANNER_PATTERN).add(TFBannerPatterns.KNIGHT_PHANTOM.getKey());
			tag(UR_GHAST_BANNER_PATTERN).add(TFBannerPatterns.UR_GHAST.getKey());
			tag(ALPHA_YETI_BANNER_PATTERN).add(TFBannerPatterns.ALPHA_YETI.getKey());
			tag(SNOW_QUEEN_BANNER_PATTERN).add(TFBannerPatterns.SNOW_QUEEN.getKey());
			tag(QUEST_RAM_BANNER_PATTERN).add(TFBannerPatterns.QUEST_RAM.getKey());
		}

		private static TagKey<BannerPattern> create(String name) {
			return TagKey.create(Registries.BANNER_PATTERN, TwilightForestMod.prefix(name));
		}

		@Override
		public String getName() {
			return "Twilight Forest Banner Pattern Tags";
		}
	}

	public static class WoodPaletteTagGenerator extends TagsProvider<WoodPalette> {
		public static final TagKey<WoodPalette> WELL_SWIZZLE_MASK = create("well_swizzle_mask");
		public static final TagKey<WoodPalette> DRUID_HUT_SWIZZLE_MASK = create("druid_hut_swizzle_mask");
		public static final TagKey<WoodPalette> COMMON_PALETTES = create("common");
		public static final TagKey<WoodPalette> UNCOMMON_PALETTES = create("uncommon");
		public static final TagKey<WoodPalette> RARE_PALETTES = create("rare");
		public static final TagKey<WoodPalette> TREASURE_PALETTES = create("treasure");

		public WoodPaletteTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
			super(output, TFRegistries.Keys.WOOD_PALETTES, provider, TwilightForestMod.ID, helper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(WELL_SWIZZLE_MASK).add(WoodPalettes.OAK);
			tag(DRUID_HUT_SWIZZLE_MASK).add(WoodPalettes.OAK, WoodPalettes.SPRUCE, WoodPalettes.BIRCH);

			tag(COMMON_PALETTES).add(WoodPalettes.SPRUCE, WoodPalettes.CANOPY);
			tag(UNCOMMON_PALETTES).add(WoodPalettes.OAK, WoodPalettes.DARKWOOD, WoodPalettes.TWILIGHT_OAK);
			tag(RARE_PALETTES).add(WoodPalettes.BIRCH, WoodPalettes.JUNGLE, WoodPalettes.MANGROVE);
			tag(TREASURE_PALETTES).add(WoodPalettes.TIMEWOOD, WoodPalettes.TRANSWOOD, WoodPalettes.MINEWOOD, WoodPalettes.SORTWOOD);
		}

		private static TagKey<WoodPalette> create(String name) {
			return TagKey.create(TFRegistries.Keys.WOOD_PALETTES, TwilightForestMod.prefix(name));
		}

		@Override
		public String getName() {
			return "Twilight Forest Wood Palette Tags";
		}
	}

	public static class DimensionTypeTagGenerator extends TagsProvider<DimensionType> {

		public static final TagKey<DimensionType> ALLOWS_MAGIC_MAP_CHARTING = TagKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("allows_magic_map_charting"));

		public DimensionTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
			super(output, Registries.DIMENSION_TYPE, provider, TwilightForestMod.ID, helper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(ALLOWS_MAGIC_MAP_CHARTING).add(TFDimensionSettings.TWILIGHT_DIM_TYPE);
		}

		@Override
		public String getName() {
			return "Twilight Forest DimensionType Tags";
		}
	}
}
