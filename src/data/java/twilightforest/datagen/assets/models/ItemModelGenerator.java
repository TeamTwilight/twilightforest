package twilightforest.datagen.assets.models;

import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
import net.minecraft.client.renderer.item.properties.numeric.Time;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import twilightforest.TwilightForestMod;
import twilightforest.client.properties.MoonwormQueenPulse;
import twilightforest.client.properties.NaturalDimension;
import twilightforest.client.properties.OreMeterFlash;
import twilightforest.client.renderer.special.*;
import twilightforest.datagen.helpers.ItemModelBuilders;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEquipmentModels;
import twilightforest.init.TFItems;
import twilightforest.init.TFTrimMaterials;
import twilightforest.item.ArcticArmorItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ItemModelGenerator extends ItemModelBuilders {

	private static final List<ItemModelGenerators.TrimMaterialData> EX_TRIM_MATERIAL_MODELS = List.of(
		new ItemModelGenerators.TrimMaterialData("quartz", TrimMaterials.QUARTZ, Map.of()),
		new ItemModelGenerators.TrimMaterialData("iron", TrimMaterials.IRON, Map.of(EquipmentAssets.IRON, "iron_darker")),
		new ItemModelGenerators.TrimMaterialData("netherite", TrimMaterials.NETHERITE, Map.of(EquipmentAssets.NETHERITE, "netherite_darker")),
		new ItemModelGenerators.TrimMaterialData("redstone", TrimMaterials.REDSTONE, Map.of()),
		new ItemModelGenerators.TrimMaterialData("copper", TrimMaterials.COPPER, Map.of()),
		new ItemModelGenerators.TrimMaterialData("gold", TrimMaterials.GOLD, Map.of(EquipmentAssets.GOLD, "gold_darker")),
		new ItemModelGenerators.TrimMaterialData("emerald", TrimMaterials.EMERALD, Map.of()),
		new ItemModelGenerators.TrimMaterialData("diamond", TrimMaterials.DIAMOND, Map.of(EquipmentAssets.DIAMOND, "diamond_darker")),
		new ItemModelGenerators.TrimMaterialData("lapis", TrimMaterials.LAPIS, Map.of()),
		new ItemModelGenerators.TrimMaterialData("amethyst", TrimMaterials.AMETHYST, Map.of()),
		new ItemModelGenerators.TrimMaterialData("resin", TrimMaterials.RESIN, Map.of()),
		new ItemModelGenerators.TrimMaterialData("ironwood", TFTrimMaterials.IRONWOOD, Map.of()),
		new ItemModelGenerators.TrimMaterialData("steeleaf", TFTrimMaterials.STEELEAF, Map.of()),
		new ItemModelGenerators.TrimMaterialData("fiery", TFTrimMaterials.FIERY, Map.of()),
		new ItemModelGenerators.TrimMaterialData("knightmetal", TFTrimMaterials.KNIGHTMETAL, Map.of()),
		new ItemModelGenerators.TrimMaterialData("carminite", TFTrimMaterials.CARMINITE, Map.of()),
		new ItemModelGenerators.TrimMaterialData("naga_scale", TFTrimMaterials.NAGA_SCALE, Map.of()));

	public ItemModelGenerator(ItemModelOutput output, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(output, modelOutput);
	}

	@Override
	public void run() {
		this.generateExpandedTrimmableItem(TFItems.IRONWOOD_HELMET.get(), TFEquipmentModels.IRONWOOD, "helmet");
		this.generateExpandedTrimmableItem(TFItems.IRONWOOD_CHESTPLATE.get(), TFEquipmentModels.IRONWOOD, "chestplate");
		this.generateExpandedTrimmableItem(TFItems.IRONWOOD_LEGGINGS.get(), TFEquipmentModels.IRONWOOD, "leggings");
		this.generateExpandedTrimmableItem(TFItems.IRONWOOD_BOOTS.get(), TFEquipmentModels.IRONWOOD, "boots");
		this.generateFlatItem(TFItems.IRONWOOD_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.IRONWOOD_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateExpandedTrimmableItem(TFItems.STEELEAF_HELMET.get(), TFEquipmentModels.STEELEAF, "helmet");
		this.generateExpandedTrimmableItem(TFItems.STEELEAF_CHESTPLATE.get(), TFEquipmentModels.STEELEAF, "chestplate");
		this.generateExpandedTrimmableItem(TFItems.STEELEAF_LEGGINGS.get(), TFEquipmentModels.STEELEAF, "leggings");
		this.generateExpandedTrimmableItem(TFItems.STEELEAF_BOOTS.get(), TFEquipmentModels.STEELEAF, "boots");
		this.generateFlatItem(TFItems.STEELEAF_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.STEELEAF_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateExpandedTrimmableItem(TFItems.KNIGHTMETAL_HELMET.get(), TFEquipmentModels.KNIGHTMETAL, "helmet");
		this.generateExpandedTrimmableItem(TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFEquipmentModels.KNIGHTMETAL, "chestplate");
		this.generateExpandedTrimmableItem(TFItems.KNIGHTMETAL_LEGGINGS.get(), TFEquipmentModels.KNIGHTMETAL, "leggings");
		this.generateExpandedTrimmableItem(TFItems.KNIGHTMETAL_BOOTS.get(), TFEquipmentModels.KNIGHTMETAL, "boots");
		this.generateFlatItem(TFItems.KNIGHTMETAL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.KNIGHTMETAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.itemModelOutput.accept(TFItems.BLOCK_AND_CHAIN.get(), ItemModelUtils.conditional(new HasComponent(TFDataComponents.THROWN_PROJECTILE.get(), false),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.BLOCK_AND_CHAIN.get(), "_thrown", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.BLOCK_AND_CHAIN.get(), ModelTemplates.FLAT_HANDHELD_ITEM))));
		this.generateKnightmetalShield(TFItems.KNIGHTMETAL_SHIELD.get());

		this.generateExpandedTrimmableItem(TFItems.FIERY_HELMET.get(), TFEquipmentModels.FIERY, "helmet");
		this.generateExpandedTrimmableItem(TFItems.FIERY_CHESTPLATE.get(), TFEquipmentModels.FIERY, "chestplate");
		this.generateExpandedTrimmableItem(TFItems.FIERY_LEGGINGS.get(), TFEquipmentModels.FIERY, "leggings");
		this.generateExpandedTrimmableItem(TFItems.FIERY_BOOTS.get(), TFEquipmentModels.FIERY, "boots");
		this.generateFlatItem(TFItems.FIERY_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.FIERY_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		this.generateExpandedTrimmableItem(TFItems.ARCTIC_HELMET.get(), TFEquipmentModels.ARCTIC, "helmet", ArcticArmorItem.DEFAULT_COLOR);
		this.generateExpandedTrimmableItem(TFItems.ARCTIC_CHESTPLATE.get(), TFEquipmentModels.ARCTIC, "chestplate", ArcticArmorItem.DEFAULT_COLOR);
		this.generateExpandedTrimmableItem(TFItems.ARCTIC_LEGGINGS.get(), TFEquipmentModels.ARCTIC, "leggings", ArcticArmorItem.DEFAULT_COLOR);
		this.generateExpandedTrimmableItem(TFItems.ARCTIC_BOOTS.get(), TFEquipmentModels.ARCTIC, "boots", ArcticArmorItem.DEFAULT_COLOR);

		this.generateExpandedTrimmableItem(TFItems.YETI_HELMET.get(), TFEquipmentModels.YETI, "helmet");
		this.generateExpandedTrimmableItem(TFItems.YETI_CHESTPLATE.get(), TFEquipmentModels.YETI, "chestplate");
		this.generateExpandedTrimmableItem(TFItems.YETI_LEGGINGS.get(), TFEquipmentModels.YETI, "leggings");
		this.generateExpandedTrimmableItem(TFItems.YETI_BOOTS.get(), TFEquipmentModels.YETI, "boots");

		this.generateExpandedTrimmableItem(TFItems.PHANTOM_HELMET.get(), TFEquipmentModels.PHANTOM, "helmet");
		this.generateExpandedTrimmableItem(TFItems.PHANTOM_CHESTPLATE.get(), TFEquipmentModels.PHANTOM, "chestplate");

		this.generateExpandedTrimmableItem(TFItems.NAGA_CHESTPLATE.get(), TFEquipmentModels.NAGA, "chestplate");
		this.generateExpandedTrimmableItem(TFItems.NAGA_LEGGINGS.get(), TFEquipmentModels.NAGA, "leggings");

		this.itemModelOutput.accept(TFItems.MYSTIC_CROWN.get(), ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(TFItems.MYSTIC_CROWN.get()), new MysticCrownSpecialRenderer.Unbaked()));

		this.generateFlatItem(TFItems.MAZEBREAKER_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.DIAMOND_MINOTAUR_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.GOLDEN_MINOTAUR_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateTwoLayerItem(TFItems.ICE_SWORD.get(), TFModelTemplates.TWO_LAYERED_HANDHELD);
		this.generateTwoLayerItem(TFItems.GLASS_SWORD.get(), TFModelTemplates.TWO_LAYERED_HANDHELD);

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
		this.itemModelOutput.accept(TFItems.LAMP_OF_CINDERS.get(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(TFItems.LAMP_OF_CINDERS.get())));
		this.generateFlatItem(TFItems.EMPERORS_CLOTH.get(), ModelTemplates.FLAT_ITEM);
		this.generateOreMagnet(TFItems.ORE_MAGNET.get());
		this.itemModelOutput.accept(TFItems.ORE_METER.get(), ItemModelUtils.conditional(new OreMeterFlash(),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.ORE_METER.get(), "_active", ModelTemplates.FLAT_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.ORE_METER.get(), ModelTemplates.FLAT_ITEM))));
		this.generateFlatItem(TFItems.POCKET_WATCH.get(), ModelTemplates.FLAT_ITEM);
		this.generateMoonDial(TFItems.MOON_DIAL.get());
		this.generateFlatItem(TFItems.CRUMBLE_HORN.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.generateFlatItem(TFItems.PEACOCK_FEATHER_FAN.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
		this.itemModelOutput.accept(TFItems.MOONWORM_QUEEN.get(), ItemModelUtils.conditional(new MoonwormQueenPulse(),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.MOONWORM_QUEEN.get(), "_alt", ModelTemplates.FLAT_HANDHELD_ITEM)),
			ItemModelUtils.plainModel(this.createFlatItemModel(TFItems.MOONWORM_QUEEN.get(), ModelTemplates.FLAT_HANDHELD_ITEM))));

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
	}

	private void generateSpawnEgg(String entityName, int primary, int secondary) {
		this.generateSpawnEgg(BuiltInRegistries.ITEM.getValue(TwilightForestMod.prefix(entityName + "_spawn_egg")), primary, secondary);
	}

	public void generateGiantTool(Item tool, Item baseTool) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(TFModelTemplates.GIANT_TOOL.create(tool, TextureMapping.layer0(baseTool), this.modelOutput));
		ItemModel.Unbaked gui = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(tool).withSuffix("_gui"));
		this.itemModelOutput.accept(tool, ItemModelUtils.select(new DisplayContext(), base, ItemModelUtils.when(ItemDisplayContext.GUI, gui)));
	}

	public void generateKnightmetalShield(Item shieldItem) {
		var normal = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(shieldItem), new KnightmetalShieldSpecialRenderer.Unbaked());
		var blocking = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(shieldItem, "_blocking"), new KnightmetalShieldSpecialRenderer.Unbaked());
		this.generateBooleanDispatch(shieldItem, ItemModelUtils.isUsingItem(), blocking, normal);
	}

	public void generateExpandedTrimmableItem(Item item, ResourceKey<EquipmentAsset> key, String name) {
		this.generateExpandedTrimmableItem(item, key, name, -1);
	}

	public void generateExpandedTrimmableItem(Item item, ResourceKey<EquipmentAsset> key, String name, int dyeColor) {
		ResourceLocation model = ModelLocationUtils.getModelLocation(item);
		ResourceLocation texture = TextureMapping.getItemTexture(item);
		ResourceLocation overlayTexture = TextureMapping.getItemTexture(item, "_overlay");
		List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> trims = new ArrayList<>(TRIM_MATERIAL_MODELS.size());

		for (ItemModelGenerators.TrimMaterialData data : EX_TRIM_MATERIAL_MODELS) {
			ResourceLocation trimModel = model.withSuffix("_" + data.name() + "_trim");
			ResourceLocation trimTexture = ResourceLocation.withDefaultNamespace(
				"trims/items/" + name + "_trim_" + data.textureName(key)
			);
			ItemModel.Unbaked trimMaterialModel;
			if (dyeColor != -1) {
				this.generateLayeredItem(trimModel, texture, overlayTexture, trimTexture);
				trimMaterialModel = ItemModelUtils.tintedModel(trimModel, new Dye(dyeColor));
			} else {
				trimMaterialModel = ItemModelUtils.plainModel(this.generateLayeredItem(trimModel, texture, trimTexture));
			}

			trims.add(ItemModelUtils.when(data.materialKey(), trimMaterialModel));
		}

		ItemModel.Unbaked armorModel;
		if (dyeColor != -1) {
			armorModel = ItemModelUtils.tintedModel(ModelTemplates.TWO_LAYERED_ITEM.create(model, TextureMapping.layered(texture, overlayTexture), this.modelOutput), new Dye(dyeColor));
		} else {
			armorModel = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(model, TextureMapping.layer0(texture), this.modelOutput));
		}

		this.itemModelOutput.accept(item, ItemModelUtils.select(new TrimMaterialProperty(), armorModel, trims));
	}

	public void generateBow(Item bowItem, boolean twoLayered) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, ModelTemplates.BOW));
		ItemModel.Unbaked pull0 = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, "_pulling_0", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_0", ModelTemplates.BOW));
		ItemModel.Unbaked pull1 = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, "_pulling_1", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_1", ModelTemplates.BOW));
		ItemModel.Unbaked pull2 = ItemModelUtils.plainModel(twoLayered ? this.twoLayerItem(bowItem, "_pulling_2", TFModelTemplates.TWO_LAYERED_BOW) : this.createFlatItemModel(bowItem, "_pulling_2", ModelTemplates.BOW));
		this.itemModelOutput.accept(bowItem, ItemModelUtils.conditional(ItemModelUtils.isUsingItem(),
			ItemModelUtils.rangeSelect(
				new UseDuration(false),
				0.05F,
				pull0,
				ItemModelUtils.override(pull1, 0.65F),
				ItemModelUtils.override(pull2, 0.9F)
			), base));
	}

	public void generateOreMagnet(Item magnetItem) {
		ItemModel.Unbaked base = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, TFModelTemplates.SPECIAL_HANDHELD));
		ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, "_pulling_1", TFModelTemplates.SPECIAL_HANDHELD));
		ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(this.createFlatItemModel(magnetItem, "_pulling_2", TFModelTemplates.SPECIAL_HANDHELD));
		this.itemModelOutput.accept(magnetItem, ItemModelUtils.conditional(ItemModelUtils.isUsingItem(),
			ItemModelUtils.rangeSelect(new UseDuration(false), 0.05F, base,
				ItemModelUtils.override(pulling1, 0.5F),
				ItemModelUtils.override(pulling2, 1.0F)),
			base));
	}

	public void generateMoonDial(Item dial) {
		List<RangeSelectItemModel.Entry> list = new ArrayList<>();
		ItemModel.Unbaked itemmodel$unbaked = ItemModelUtils.plainModel(this.createFlatItemModel(dial, TFModelTemplates.MOON_DIAL));
		list.add(ItemModelUtils.override(itemmodel$unbaked, 0.0F));

		for (int i = 1; i < 8; i++) {
			ItemModel.Unbaked phase = ItemModelUtils.plainModel(this.createFlatItemModel(dial, "_" + i, TFModelTemplates.MOON_DIAL));
			list.add(ItemModelUtils.override(phase, (float) i - 0.5F));
		}

		list.add(ItemModelUtils.override(itemmodel$unbaked, 7.5F));
		this.itemModelOutput.accept(dial, ItemModelUtils.conditional(new NaturalDimension(),
			ItemModelUtils.rangeSelect(new Time(false, Time.TimeSource.MOON_PHASE), 8.0F, list),
			ItemModelUtils.rangeSelect(new Time(true, Time.TimeSource.RANDOM), 8.0F, list)));
	}

	public void generateTwoLayerItem(Item item, ModelTemplate template) {
		this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.twoLayerItem(item, template)));
	}

	public ResourceLocation twoLayerItem(Item item, ModelTemplate template) {
		return this.twoLayerItem(item, "", template);
	}

	public ResourceLocation twoLayerItem(Item item, String suffix, ModelTemplate template) {
		ResourceLocation baseTex = TextureMapping.getItemTexture(item);
		return template.create(ModelLocationUtils.getModelLocation(item, suffix), TextureMapping.layered(baseTex.withSuffix("_solid"), baseTex.withSuffix("_clear")), this.modelOutput);
	}
}
