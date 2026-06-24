package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
import net.minecraft.client.renderer.item.properties.numeric.Count;
import net.minecraft.client.renderer.item.properties.numeric.Time;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.item.TravellersGearItemModel;
import twilightforest.client.properties.*;
import twilightforest.client.renderer.special.*;
import twilightforest.datagen.helpers.ItemModelBuilders;
import twilightforest.init.*;
import twilightforest.item.ArcticArmorItem;
import twilightforest.util.MoonPhaseConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ItemModelGenerator extends ItemModelBuilders {
	public ItemModelGenerator(ItemModelOutput output, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(output, modelOutput);
	}

	@Override
	public void run() {
		this.generateFlatItem(TFItems.MAGIC_MAP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAZE_MAP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ORE_MAP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FILLED_MAGIC_MAP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FILLED_MAZE_MAP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FILLED_ORE_MAP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TORCHBERRIES.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAVEN_FEATHER.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAGIC_MAP_FOCUS.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAZE_MAP_FOCUS.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_LIFE_1.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_LIFE_2.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_KEEPING_1.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_KEEPING_2.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHARM_OF_KEEPING_3.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TRANSFORMATION_POWDER.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAW_VENISON.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COOKED_VENISON.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAW_MEEF.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COOKED_MEEF.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAZE_WAFER.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MEEF_STROGANOFF.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.HYDRA_CHOP.get(), ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFBlocks.EXPERIMENT_115.asItem(), ItemModelUtils.select(new Experiment115Type(), ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.EXPERIMENT_115.asItem(), ModelTemplates.FLAT_ITEM)),
			ItemModelUtils.when("think", ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(TFBlocks.EXPERIMENT_115.asItem(), "_think"), TextureMapping.layer0(new Material(TwilightForestMod.prefix("item/think115"))), this.modelOutput))),
			ItemModelUtils.when("full", ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFBlocks.EXPERIMENT_115.get(), "_8_8")))));
		this.generateFlatItem(TFItems.LIVEROOT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RAW_IRONWOOD.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_INGOT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_INGOT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.NAGA_SCALE.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.WROUGHT_IRON_BAR.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ARMOR_SHARD.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ARMOR_SHARD_CLUSTER.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_INGOT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_RING.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FIERY_BLOOD.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FIERY_TEARS.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FIERY_INGOT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ARCTIC_FUR.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ALPHA_YETI_FUR.get(), ModelTemplates.FLAT_ITEM);
		Identifier empty = ModelTemplates.FLAT_ITEM.create(TwilightForestMod.prefix("item/potion_flask_empty"), TextureMapping.layer0(new Material(TwilightForestMod.prefix("block/blank"))), this.modelOutput);
		this.generatePotionFlask(TFItems.BRITTLE_FLASK.get(), true, empty);
		this.generatePotionFlask(TFItems.GREATER_FLASK.get(), false, empty);
		this.generateTwoLayerItem(TFItems.EXANIMATE_ESSENCE.get(), "", "_powder", "_flames", ModelTemplates.TWO_LAYERED_ITEM);
		this.generateFlatItem(TFItems.CROWN_SPLINTER.get(), ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFBlocks.RED_THREAD.asItem(), ItemModelUtils.rangeSelect(new Count(true), ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), ModelTemplates.FLAT_ITEM)), List.of(
			ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), "_bundle_0", ModelTemplates.FLAT_ITEM)), 4.0F / 64.0F),
			ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), "_bundle_1", ModelTemplates.FLAT_ITEM)), 16.0F / 64.0F),
			ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(TFBlocks.RED_THREAD.asItem(), "_bundle_2", ModelTemplates.FLAT_ITEM)), 32.0F / 64.0F))));
		this.generateTwoLayerItem(TFItems.BORER_ESSENCE.get(), "_particles", ModelTemplates.TWO_LAYERED_ITEM);
		this.generateFlatItem(TFItems.CARMINITE.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TOWER_KEY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MAGIC_BEANS.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MUSIC_DISC_THREAD.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_FINDINGS.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_RADIANCE.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_STEPS.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_MOTION.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_WAYFARER.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_HOME.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_MAKER.get(), ModelTemplates.MUSIC_DISC);
		this.generateFlatItem(TFItems.MUSIC_DISC_SUPERSTITIOUS.get(), ModelTemplates.MUSIC_DISC);

		this.generatePattern(TFItems.NAGA_BANNER_PATTERN.get());
		this.generatePattern(TFItems.LICH_BANNER_PATTERN.get());
		this.generatePattern(TFItems.MINOSHROOM_BANNER_PATTERN.get());
		this.generatePattern(TFItems.HYDRA_BANNER_PATTERN.get());
		this.generatePattern(TFItems.KNIGHT_PHANTOM_BANNER_PATTERN.get());
		this.generatePattern(TFItems.UR_GHAST_BANNER_PATTERN.get());
		this.generatePattern(TFItems.ALPHA_YETI_BANNER_PATTERN.get());
		this.generatePattern(TFItems.SNOW_QUEEN_BANNER_PATTERN.get());
		this.generatePattern(TFItems.QUEST_RAM_BANNER_PATTERN.get());

		this.generateFlatItem(TFItems.TWILIGHT_OAK_BOAT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CANOPY_BOAT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MANGROVE_BOAT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.DARK_BOAT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TIME_BOAT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TRANSFORMATION_BOAT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MINING_BOAT.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SORTING_BOAT.get(), ModelTemplates.FLAT_ITEM);

		this.generateChestBoat(TFItems.TWILIGHT_OAK_CHEST_BOAT.get());
		this.generateChestBoat(TFItems.CANOPY_CHEST_BOAT.get());
		this.generateChestBoat(TFItems.MANGROVE_CHEST_BOAT.get());
		this.generateChestBoat(TFItems.DARK_CHEST_BOAT.get());
		this.generateChestBoat(TFItems.TIME_CHEST_BOAT.get());
		this.generateChestBoat(TFItems.TRANSFORMATION_CHEST_BOAT.get());
		this.generateChestBoat(TFItems.MINING_CHEST_BOAT.get());
		this.generateChestBoat(TFItems.SORTING_CHEST_BOAT.get());

		this.generateDynamicTrimmableItem(TFItems.IRONWOOD_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET);
		this.generateDynamicTrimmableItem(TFItems.IRONWOOD_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
		this.generateDynamicTrimmableItem(TFItems.IRONWOOD_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS);
		this.generateDynamicTrimmableItem(TFItems.IRONWOOD_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS);
		this.generateFlatItem(TFItems.IRONWOOD_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateDynamicTrimmableItem(TFItems.STEELEAF_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET);
		this.generateDynamicTrimmableItem(TFItems.STEELEAF_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
		this.generateDynamicTrimmableItem(TFItems.STEELEAF_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS);
		this.generateDynamicTrimmableItem(TFItems.STEELEAF_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS);
		this.generateFlatItem(TFItems.STEELEAF_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateDynamicTrimmableItem(TFItems.KNIGHTMETAL_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET);
		this.generateDynamicTrimmableItem(TFItems.KNIGHTMETAL_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
		this.generateDynamicTrimmableItem(TFItems.KNIGHTMETAL_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS);
		this.generateDynamicTrimmableItem(TFItems.KNIGHTMETAL_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS);
		this.generateFlatItem(TFItems.KNIGHTMETAL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.itemModelOutput.accept(TFItems.BLOCK_AND_CHAIN.get(), ItemModelUtils.conditional(new HasComponent(TFDataComponents.THROWN_PROJECTILE.get(), false),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.BLOCK_AND_CHAIN.get(), "_thrown", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.BLOCK_AND_CHAIN.get(), ModelTemplates.FLAT_HANDHELD_ITEM))));
		this.generateKnightmetalShield(TFItems.KNIGHTMETAL_SHIELD.get());

		this.generateDynamicTrimmableItem(TFItems.FIERY_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET);
		this.generateDynamicTrimmableItem(TFItems.FIERY_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
		this.generateDynamicTrimmableItem(TFItems.FIERY_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS);
		this.generateDynamicTrimmableItem(TFItems.FIERY_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS);
		this.generateFlatItem(TFItems.FIERY_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.FIERY_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateDynamicTrimmableItem(TFItems.ARCTIC_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET, ArcticArmorItem.DEFAULT_COLOR);
		this.generateDynamicTrimmableItem(TFItems.ARCTIC_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, ArcticArmorItem.DEFAULT_COLOR);
		this.generateDynamicTrimmableItem(TFItems.ARCTIC_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS, ArcticArmorItem.DEFAULT_COLOR);
		this.generateDynamicTrimmableItem(TFItems.ARCTIC_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS, ArcticArmorItem.DEFAULT_COLOR);

		this.generateDynamicTrimmableItem(TFItems.YETI_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET);
		this.generateDynamicTrimmableItem(TFItems.YETI_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
		this.generateDynamicTrimmableItem(TFItems.YETI_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS);
		this.generateDynamicTrimmableItem(TFItems.YETI_BOOTS.get(), ItemModelGenerators.TRIM_PREFIX_BOOTS);

		this.generateDynamicTrimmableItem(TFItems.PHANTOM_HELMET.get(), ItemModelGenerators.TRIM_PREFIX_HELMET);
		this.generateDynamicTrimmableItem(TFItems.PHANTOM_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);

		this.generateDynamicTrimmableItem(TFItems.NAGA_CHESTPLATE.get(), ItemModelGenerators.TRIM_PREFIX_CHESTPLATE);
		this.generateDynamicTrimmableItem(TFItems.NAGA_LEGGINGS.get(), ItemModelGenerators.TRIM_PREFIX_LEGGINGS);

		this.itemModelOutput.accept(TFItems.MYSTIC_CROWN.get(), ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(TFItems.MYSTIC_CROWN.get()), new MysticCrownSpecialRenderer.Unbaked()));

		this.generateFlatItem(TFItems.MAZEBREAKER_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.DIAMOND_MINOTAUR_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.GOLDEN_MINOTAUR_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateTwoLayerItem(TFItems.ICE_SWORD.get(), "", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_HANDHELD);
		this.generateTwoLayerItem(TFItems.GLASS_SWORD.get(), "", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_HANDHELD);

		this.generateBow(TFItems.TRIPLE_BOW.get(), false);
		this.generateBow(TFItems.SEEKER_BOW.get(), false);
		this.generateBow(TFItems.ICE_BOW.get(), true);
		this.generateBow(TFItems.ENDER_BOW.get(), false);

		this.generateGiantTool(TFItems.GIANT_SWORD.get(), Items.STONE_SWORD);
		this.generateGiantTool(TFItems.GIANT_PICKAXE.get(), Items.STONE_PICKAXE);

		this.generateFlatItem(TFItems.ICE_BOMB.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TWILIGHT_SCEPTER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.LIFEDRAIN_SCEPTER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.ZOMBIE_SCEPTER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.FORTIFICATION_SCEPTER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.LAMP_OF_CINDERS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.EMPERORS_CLOTH.get(), ModelTemplates.FLAT_ITEM);
		this.generateOreMagnet(TFItems.ORE_MAGNET.get());
		this.itemModelOutput.accept(TFItems.ORE_METER.get(), ItemModelUtils.conditional(new OreMeterFlash(),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.ORE_METER.get(), "_active", ModelTemplates.FLAT_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.ORE_METER.get(), ModelTemplates.FLAT_ITEM))));
		this.generateFlatItem(TFItems.POCKET_WATCH.get(), ModelTemplates.FLAT_ITEM);
		this.generateMoonDial(TFItems.MOON_DIAL.get());
		this.generateBooleanDispatch(TFItems.CRUMBLE_HORN.get(), ItemModelUtils.isUsingItem(),
			ItemModelUtils.plainModel(ModelTemplates.createItem(Identifier.withDefaultNamespace("tooting_goat_horn").toString(), TextureSlot.LAYER0).create(TwilightForestMod.prefix("tooting_crumble_horn"), TextureMapping.layer0(TFItems.CRUMBLE_HORN.get()), this.modelOutput)),
			ItemModelUtils.plainModel(ModelTemplates.createItem(Identifier.withDefaultNamespace("goat_horn").toString(), TextureSlot.LAYER0).create(TFItems.CRUMBLE_HORN.get(), TextureMapping.layer0(TFItems.CRUMBLE_HORN.get()), this.modelOutput)));
		this.generateFlatItem(TFItems.PEACOCK_FEATHER_FAN.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.itemModelOutput.accept(TFItems.MOONWORM_QUEEN.get(), ItemModelUtils.conditional(new MoonwormQueenPulse(),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.MOONWORM_QUEEN.get(), "_alt", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.MOONWORM_QUEEN.get(), ModelTemplates.FLAT_HANDHELD_ITEM))));
		this.generateFlatItem(TFItems.MAGIC_PAINTING.get(), ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.CUBE_TALISMAN.get(), ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFItems.CUBE_OF_ANNIHILATION.get(), ItemModelUtils.conditional(new HasComponent(TFDataComponents.THROWN_PROJECTILE.get(), false),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.CUBE_OF_ANNIHILATION.get(), "_thrown", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.CUBE_OF_ANNIHILATION.get(), ModelTemplates.FLAT_HANDHELD_ITEM))));
		this.generateFlatItem(TFItems.FOUR_LEAF_CLOVER.get(), ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.RASPBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.BLUEBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.BLACKBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MALOBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.BLIGHTBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.DUSKBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SKYBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.STINGBERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COPPER_BERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.IRON_BERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.GOLD_BERRY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.ESSENCE_BERRY.get(), ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.BEEF_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.CHICKEN_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.PORK_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MUTTON_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.RABBIT_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MEEF_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.VENISON_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.MONSTER_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.COD_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SALMON_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TROPICAL_FISH_JERKY.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.FUGU_JERKY.get(), ModelTemplates.FLAT_ITEM);

		this.generateFlatItem(TFItems.GELATINOUS_MAZE_SLIME_DROP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.GELATINOUS_SLIME_DROP.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.SHIKA_SENBEI.get(), ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFItems.BERRY_MEDLEY.get(), ItemModelUtils.plainModel(this.generateLayeredItem(TFItems.BERRY_MEDLEY.get(), new Material(Identifier.withDefaultNamespace("item/bowl")), new Material(TwilightForestMod.prefix("item/berry_medley")))));
		this.itemModelOutput.accept(TFItems.MOSS_SOUP.get(), ItemModelUtils.plainModel(this.generateLayeredItem(TFItems.MOSS_SOUP.get(), new Material(Identifier.withDefaultNamespace("item/bowl")), new Material(TwilightForestMod.prefix("item/moss_soup")))));

		this.generateFlatItem(TFItems.MAZE_SLIME_BALL.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TANNIN.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TREATED_LEATHER.get(), ModelTemplates.FLAT_ITEM);
		this.generateFlatItem(TFItems.TANNED_LEATHER.get(), ModelTemplates.FLAT_ITEM);
		this.itemModelOutput.accept(TFItems.STALE_BREAD.get(), ItemModelUtils.plainModel(ModelTemplates.FLAT_HANDHELD_ITEM.create(TFItems.STALE_BREAD.get(), TextureMapping.layer0(Items.BREAD), this.modelOutput)));

		this.generateTravellersGear(TFItems.TRAVELLERS_GOGGLES.get(), TwilightForestMod.prefix("travellers_modifiers/goggles"));
		this.generateLayeredTravellersGear(TFItems.TRAVELLERS_VEST.get(), TFItems.TRAVELLERS_GLOVES.get(), new HasComponent(TFDataComponents.TRAVELLERS_HAS_GLOVES.get(), true), TwilightForestMod.prefix("travellers_modifiers/vest"));
		this.generateTravellersGear(TFItems.TRAVELLERS_WINGS.get(), TwilightForestMod.prefix("travellers_modifiers/wings"));
		this.generateTravellersGear(TFItems.TRAVELLERS_BOOTS.get(), TwilightForestMod.prefix("travellers_modifiers/boots"));
		this.generateFlatItem(TFItems.TRAVELLERS_BELT.get(), ModelTemplates.FLAT_ITEM);

		this.generateSpawnEgg("alpha_yeti", 0xCDCDCD, 0x29486E);
		this.generateSpawnEgg("armored_giant", 0x239391, 0x9A9A9A);
		this.generateSpawnEgg("bighorn_sheep", 0xDBCEAF, 0xD7C771);
		this.generateSpawnEgg("block_and_chain_goblin", 0xD3E7BC, 0x1F3FFF);
		this.generateSpawnEgg("boar", 0x83653B, 0xFFEFCA);
		this.generateSpawnEgg("carminite_broodling", 0x343C14, 0xBAEE02);
		this.generateSpawnEgg("carminite_ghastguard", 0xBCBCBC, 0xB77878);
		this.generateSpawnEgg("carminite_ghastling", 0xBCBCBC, 0xA74343);
		this.generateSpawnEgg("carminite_golem", 0x6B3D20, 0xE2DDDA);
		this.generateSpawnEgg("death_tome", 0x774E22, 0xDBCDBE);
		this.generateSpawnEgg("deer", 0x7B4D2E, 0x4B241D);
		this.generateSpawnEgg("dwarf_rabbit", 0xFEFEEE, 0xCCAA99);
		this.generateSpawnEgg("fire_beetle", 0x1D0B00, 0xCB6F25);
		this.generateSpawnEgg("giant_miner", 0x211B52, 0x9A9A9A);
		this.generateSpawnEgg("hedge_spider", 0x235F13, 0x562653);
		this.generateSpawnEgg("helmet_crab", 0xFB904B, 0xD3E7BC);
		this.generateSpawnEgg("hostile_wolf", 0xD7D3D3, 0xAB1E14);
		this.generateSpawnEgg("hydra", 0x142940, 0x29806B);
		this.generateSpawnEgg("ice_crystal", 0xDCE9FE, 0xADCAFB);
		this.generateSpawnEgg("king_spider", 0x2C1A0E, 0xFFC017);
		this.generateSpawnEgg("knight_phantom", 0xA6673B, 0xD3E7BC);
		this.generateSpawnEgg("kobold", 0x372096, 0x895D1B);
		this.generateSpawnEgg("lich", 0xACA489, 0x360472);
		this.generateSpawnEgg("lower_goblin_knight", 0x566055, 0xD3E7BC);
		this.generateSpawnEgg("maze_slime", 0xA3A3A3, 0x2A3B17);
		this.generateSpawnEgg("minoshroom", 0xA81012, 0xAA7D66);
		this.generateSpawnEgg("minotaur", 0x3F3024, 0xAA7D66);
		this.generateSpawnEgg("mist_wolf", 0x3A1411, 0xE2C88A);
		this.generateSpawnEgg("mosquito_swarm", 0x080904, 0x2D2F21);
		this.generateSpawnEgg("naga", 0xA4D316, 0x1B380B);
		this.generateSpawnEgg("penguin", 0x12151B, 0xF9EDD2);
		this.generateSpawnEgg("pinch_beetle", 0xBC9327, 0x241609);
		this.generateSpawnEgg("quest_ram", 0xFEFEEE, 0x33AADD);
		this.generateSpawnEgg("raven", 0x000011, 0x222233);
		this.generateSpawnEgg("redcap", 0x3B3A6C, 0xAB1E14);
		this.generateSpawnEgg("redcap_sapper", 0x575D21, 0xAB1E14);
		this.generateSpawnEgg("skeleton_druid", 0xA3A3A3, 0x2A3B17);
		this.generateSpawnEgg("slime_beetle", 0x0C1606, 0x60A74C);
		this.generateSpawnEgg("snow_guardian", 0xD3E7BC, 0xFEFEFE);
		this.generateSpawnEgg("snow_queen", 0xB1B2D4, 0x87006E);
		this.generateSpawnEgg("squirrel", 0x904F12, 0xEEEEEE);
		this.generateSpawnEgg("stable_ice_core", 0xA1BFF3, 0x7000F8);
		this.generateSpawnEgg("swarm_spider", 0x32022E, 0x17251E);
		this.generateSpawnEgg("tiny_bird", 0x33AADD, 0x1188EE);
		this.generateSpawnEgg("towerwood_borer", 0x5D2B21, 0xACA03A);
		this.generateSpawnEgg("troll", 0x9EA98F, 0xB0948E);
		this.generateSpawnEgg("unstable_ice_core", 0x9AACF5, 0x9B0FA5);
		this.generateSpawnEgg("ur_ghast", 0xBCBCBC, 0xB77878);
		this.generateSpawnEgg("winter_wolf", 0xDFE3E5, 0xB2BCCA);
		this.generateSpawnEgg("wraith", 0x505050, 0x838383);
		this.generateSpawnEgg("yeti", 0xDEDEDE, 0x4675BB);

		this.generateLayeredItem(TwilightForestMod.prefix("item/shield"), new Material(TwilightForestMod.prefix("item/lich_shield_frame")), new Material(TwilightForestMod.prefix("item/lich_shield_fill")));
	}

	private void generateSpawnEgg(String entityName, int primary, int secondary) {
		Item item = BuiltInRegistries.ITEM.getValue(TwilightForestMod.prefix(entityName + "_spawn_egg"));
		Identifier model = this.generateLayeredItem(item, new Material(TwilightForestMod.prefix("item/spawn_egg_base")), new Material(TwilightForestMod.prefix("item/spawn_egg_overlay")));

		this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model,
			ItemModelUtils.constantTint(primary),
			ItemModelUtils.constantTint(secondary)
		));
	}

	public void generatePattern(Item item) {
		Identifier modelLoc = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(new Material(TwilightForestMod.prefix("item/tf_banner_pattern"))), this.modelOutput);
		this.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelLoc));
	}

	public void generateGiantTool(Item tool, Item baseTool) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(ModelTemplates.FLAT_HANDHELD_ITEM.create(tool, TextureMapping.layer0(baseTool), this.modelOutput));
		ItemModel.Unbaked gui = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(tool).withSuffix("_gui"));
		this.itemModelOutput.accept(tool, ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
	}

	public void generateChestBoat(Item boat) {
		Identifier modelLoc = ModelTemplates.TWO_LAYERED_ITEM.create(boat, TextureMapping.layered(new Material(ModelLocationUtils.getModelLocation(Items.OAK_CHEST_BOAT)), new Material(ModelLocationUtils.getModelLocation(boat))), this.modelOutput);
		this.itemModelOutput.accept(boat, ItemModelUtils.plainModel(modelLoc));
	}

	public void generateKnightmetalShield(Item shieldItem) {
		var shieldRenderer = new net.minecraft.client.renderer.special.ShieldSpecialRenderer.Unbaked();
		var normal = ItemModelUtils.specialModel(
			ModelLocationUtils.getModelLocation(shieldItem),
			shieldRenderer
		);
		var blocking = ItemModelUtils.specialModel(
			ModelLocationUtils.getModelLocation(shieldItem, "_blocking"),
			shieldRenderer
		);
		this.generateBooleanDispatch(shieldItem, ItemModelUtils.isUsingItem(), blocking, normal);
	}


	public void generateDynamicTrimmableItem(Item armor, Identifier slotTrimPrefix) {
		this.generateDynamicTrimmableItem(armor, this.createFlatItemModel(armor, ModelTemplates.FLAT_ITEM), slotTrimPrefix);
	}

	public void generateDynamicTrimmableItem(Item armor, Identifier slotTrimPrefix, int color) {
		this.generateDynamicTrimmableItem(armor, this.createFlatItemModel(armor, ModelTemplates.FLAT_ITEM), slotTrimPrefix, color);
	}

	public void generateBow(Item bowItem, boolean twoLayered) {
		Identifier baseModel = twoLayered ? this.twoLayerItem(bowItem, "", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, ModelTemplates.BOW);
		Identifier pull0Model = twoLayered ? this.twoLayerItem(bowItem, "_pulling_0", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_0", ModelTemplates.BOW);
		Identifier pull1Model = twoLayered ? this.twoLayerItem(bowItem, "_pulling_1", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_1", ModelTemplates.BOW);
		Identifier pull2Model = twoLayered ? this.twoLayerItem(bowItem, "_pulling_2", "_solid", "_clear", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_2", ModelTemplates.BOW);

		ItemModel.Unbaked base = ItemModelUtils.plainModel(baseModel);

		ItemModel.Unbaked bowPulling = ItemModelUtils.rangeSelect(
			new UseDuration(false),
			ItemModelUtils.plainModel(pull0Model),
			ItemModelUtils.override(ItemModelUtils.plainModel(pull1Model), 0.65F),
			ItemModelUtils.override(ItemModelUtils.plainModel(pull2Model), 0.9F)
		);

		this.itemModelOutput.accept(bowItem, ItemModelUtils.conditional(
			ItemModelUtils.isUsingItem(),
			bowPulling,
			base
		));
	}

	public void generateOreMagnet(Item magnetItem) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, TFModelTemplates.SPECIAL_HANDHELD));
		ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, "_pulling_1", TFModelTemplates.SPECIAL_HANDHELD));
		ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, "_pulling_2", TFModelTemplates.SPECIAL_HANDHELD));
		List<RangeSelectItemModel.Entry> entries = new ArrayList<>();
		entries.add(ItemModelUtils.override(pulling1, 0.5F));
		entries.add(ItemModelUtils.override(pulling2, 1.0F));
		this.itemModelOutput.accept(magnetItem, ItemModelUtils.conditional(ItemModelUtils.isUsingItem(),
			ItemModelUtils.rangeSelect(new UseDuration(false), 0.05F, entries),
			base));
	}

	public void generateMoonDial(Item dial) {
		List<RangeSelectItemModel.Entry> list = new ArrayList<>();
		ItemModel.Unbaked itemmodel$unbaked = ItemModelUtils.plainModel(this.createFlatItemModel(dial, "/" + MoonPhaseConverter.convertIndexToPhase(0), TFModelTemplates.MOON_DIAL));
		list.add(ItemModelUtils.override(itemmodel$unbaked, 0.0F));
		for (int i = 1; i < 8; i++) {
			ItemModel.Unbaked phase = ItemModelUtils.plainModel(this.createFlatItemModel(dial, "/" + MoonPhaseConverter.convertIndexToPhase(i), TFModelTemplates.MOON_DIAL));
			list.add(ItemModelUtils.override(phase, (float) i - 0.5F));
		}
		list.add(ItemModelUtils.override(itemmodel$unbaked, 7.5F));
		this.itemModelOutput.accept(dial, ItemModelUtils.rangeSelect(new Time(false, Time.TimeSource.MOON_PHASE), 8.0F, list));
	}

	public void generatePotionFlask(Item flask, boolean crackable, Identifier empty) {
		List<RangeSelectItemModel.Entry> potionEntries = new ArrayList<>();
		List<RangeSelectItemModel.Entry> flaskEntries = new ArrayList<>();
		String[] suffixes = {"_labelled", "_splintered", "_damaged"};
		for (int i = 0; i < 3; i++) {
			potionEntries.add(ItemModelUtils.override(ItemModelUtils.tintedModel(this.createFlatItemModel(flask, "_" + (i + 1), ModelTemplates.FLAT_ITEM), new PotionFlaskTintSource()), (float)(i + 1)));
			if (i == 0) {
				var base = ItemModelUtils.plainModel(this.createFlatItemModel(flask, ModelTemplates.FLAT_ITEM));
				flaskEntries.add(ItemModelUtils.override(crackable ?
					ItemModelUtils.conditional(new HasComponent(TFDataComponents.POTION_FLASK_CONTENTS.get(), true),
						ItemModelUtils.plainModel(this.createFlatItemModel(flask, suffixes[i], ModelTemplates.FLAT_ITEM)), base) : base, 0.0F));
			} else if (crackable) {
				flaskEntries.add(ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(flask, suffixes[i], ModelTemplates.FLAT_ITEM)), (float)i));
			}
		}
		ItemModel.Unbaked flaskModel = crackable ? ItemModelUtils.rangeSelect(new PotionFlaskDamage(false), flaskEntries.getFirst().model(), flaskEntries) : flaskEntries.getFirst().model();
		ItemModel.Unbaked potionModel = ItemModelUtils.rangeSelect(new PotionFlaskDosage(false), ItemModelUtils.plainModel(empty), potionEntries);
		this.itemModelOutput.accept(flask, ItemModelUtils.composite(potionModel, flaskModel));
	}

	public void generateTwoLayerItem(Item item, String modelSuffix, String suffix1, String suffix2, ModelTemplate template) {
		this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.twoLayerItem(item, modelSuffix, suffix1, suffix2, template)));
	}

	public void generateTwoLayerItem(Item item, String suffix, ModelTemplate template) {
		this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.twoLayerItem(item, "", "", suffix, template)));
	}

	public Identifier twoLayerItem(Item item, String suffix, ModelTemplate template) {
		return this.twoLayerItem(item, "", "", suffix, template);
	}

	public Identifier twoLayerItem(Item item, String modelSuffix, String suffix1, String suffix2, ModelTemplate template) {
		return template.create(ModelLocationUtils.getModelLocation(item, modelSuffix), TextureMapping.layered(TextureMapping.getItemTexture(item, suffix1 + modelSuffix), TextureMapping.getItemTexture(item, suffix2 + modelSuffix)), this.modelOutput);
	}

	public void generateTravellersGear(Item item, Identifier modifierDirectory) {
		this.itemModelOutput.accept(item, ItemModelUtils.conditional(new Broken(),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, "_broken", ModelTemplates.FLAT_ITEM)), modifierDirectory.withSuffix("/broken")),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM)), modifierDirectory)));
	}

	public void generateLayeredTravellersGear(Item item, Item overlay, ConditionalItemModelProperty property, Identifier modifierDirectory) {
		ItemModel.Unbaked gearModel = ItemModelUtils.conditional(new Broken(),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, "_broken", ModelTemplates.FLAT_ITEM)), modifierDirectory.withSuffix("/broken")),
			new TravellersGearItemModel.Unbaked(ItemModelUtils.plainModel(this.createFlatItemModel(item, ModelTemplates.FLAT_ITEM)), modifierDirectory));
		ItemModel.Unbaked baseOverlay = ItemModelUtils.plainModel(this.createFlatItemModel(overlay, ModelTemplates.FLAT_ITEM));
		ItemModel.Unbaked overlayModel = ItemModelUtils.conditional(new Broken(),
			ItemModelUtils.plainModel(this.createFlatItemModel(overlay, "_broken", ModelTemplates.FLAT_ITEM)),
			baseOverlay);
		this.itemModelOutput.accept(overlay, baseOverlay);
		this.itemModelOutput.accept(item, ItemModelUtils.conditional(property, ItemModelUtils.composite(gearModel, overlayModel), gearModel));
	}
}
