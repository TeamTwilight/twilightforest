package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.enums.DeadrockVariant;
import twilightforest.enums.ThornVariant;

import static java.util.Arrays.stream;
import static net.minecraft.init.MobEffects.REGENERATION;
import static net.minecraft.inventory.EntityEquipmentSlot.*;
import static net.minecraft.item.Item.ToolMaterial.DIAMOND;
import static twilightforest.item.TFItems.*;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFRegisterItemEvent {

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		ItemRegistryHelper items = new ItemRegistryHelper(event.getRegistry());

		items.register("naga_scale", new ItemTF().setUnlocalizedName("nagaScale"));
		items.register("naga_chestplate", new ItemTFNagaArmor(ARMOR_NAGA, CHEST).setUnlocalizedName("plateNaga").setMaxStackSize(1));
		items.register("naga_leggings", new ItemTFNagaArmor(ARMOR_NAGA, LEGS).setUnlocalizedName("legsNaga").setMaxStackSize(1));
		items.register("twilight_scepter", new ItemTFTwilightWand().setUnlocalizedName("scepterTwilight").setMaxStackSize(1));
		items.register("lifedrain_scepter", new ItemTFScepterLifeDrain().setUnlocalizedName("scepterLifeDrain").setMaxStackSize(1));
		items.register("zombie_scepter", new ItemTFZombieWand().setUnlocalizedName("scepterZombie").setMaxStackSize(1));
		//items.register("Wand of Pacification [NYI]", new ItemTF().setIconIndex(6).setUnlocalizedName("wandPacification").setMaxStackSize(1));
		items.register("ore_meter", new ItemTFOreMeter().setUnlocalizedName("oreMeter").setMaxStackSize(1));
		items.register("magic_map", new ItemTFMagicMap().setUnlocalizedName("magicMap").setMaxStackSize(1));
		items.register("maze_map", new ItemTFMazeMap(false).setUnlocalizedName("mazeMap").setMaxStackSize(1));
		items.register("ore_map", new ItemTFMazeMap(true).setUnlocalizedName("oreMap").setMaxStackSize(1));
		items.register("raven_feather", new ItemTF().setUnlocalizedName("tfFeather"));
		items.register("magic_map_focus", new ItemTF().setUnlocalizedName("magicMapFocus"));
		items.register("maze_map_focus", new ItemTF().setUnlocalizedName("mazeMapFocus"));
		items.register("liveroot", new ItemTF().setUnlocalizedName("liveRoot"));
		items.register("ironwood_raw", new ItemTF().setUnlocalizedName("ironwoodRaw"));
		items.register("ironwood_ingot", new ItemTF().setUnlocalizedName("ironwoodIngot"));
		items.register("ironwood_helmet", new ItemTFIronwoodArmor(ARMOR_IRONWOOD, HEAD).setUnlocalizedName("ironwoodHelm").setMaxStackSize(1));
		items.register("ironwood_chestplate", new ItemTFIronwoodArmor(ARMOR_IRONWOOD, CHEST).setUnlocalizedName("ironwoodPlate").setMaxStackSize(1));
		items.register("ironwood_leggings", new ItemTFIronwoodArmor(ARMOR_IRONWOOD, LEGS).setUnlocalizedName("ironwoodLegs").setMaxStackSize(1));
		items.register("ironwood_boots", new ItemTFIronwoodArmor(ARMOR_IRONWOOD, FEET).setUnlocalizedName("ironwoodBoots").setMaxStackSize(1));
		items.register("ironwood_sword", new ItemTFIronwoodSword(TOOL_IRONWOOD).setUnlocalizedName("ironwoodSword").setMaxStackSize(1));
		items.register("ironwood_shovel", new ItemTFIronwoodShovel(TOOL_IRONWOOD).setUnlocalizedName("ironwoodShovel").setMaxStackSize(1));
		items.register("ironwood_pickaxe", new ItemTFIronwoodPick(TOOL_IRONWOOD).setUnlocalizedName("ironwoodPick").setMaxStackSize(1));
		items.register("ironwood_axe", new ItemTFIronwoodAxe(TOOL_IRONWOOD).setUnlocalizedName("ironwoodAxe").setMaxStackSize(1));
		items.register("ironwood_hoe", new ItemTFIronwoodHoe(TOOL_IRONWOOD).setUnlocalizedName("ironwoodHoe").setMaxStackSize(1));
		items.register("torchberries", new ItemTF().setUnlocalizedName("torchberries"));
		items.register("raw_venison", (new ItemTFFood(3, 0.3F, true)).setUnlocalizedName("venisonRaw"));
		items.register("cooked_venison", (new ItemTFFood(8, 0.8F, true)).setUnlocalizedName("venisonCooked"));
		items.register("hydra_chop", (new ItemTFHydraChops(18, 2.0F)).setPotionEffect(new PotionEffect(REGENERATION, 100, 0), 1.0F).setUnlocalizedName("hydraChop"));
		items.register("fiery_blood", new ItemTF().makeRare().setUnlocalizedName("fieryBlood"));
		items.register("trophy", new ItemTFTrophy().setUnlocalizedName("trophy"));
		items.register("fiery_ingot", new ItemTF().makeRare().setUnlocalizedName("fieryIngot"));
		items.register("fiery_helmet", new ItemTFFieryArmor(ARMOR_FIERY, HEAD).setUnlocalizedName("fieryHelm").setMaxStackSize(1));
		items.register("fiery_chestplate", new ItemTFFieryArmor(ARMOR_FIERY, CHEST).setUnlocalizedName("fieryPlate").setMaxStackSize(1));
		items.register("fiery_leggings", new ItemTFFieryArmor(ARMOR_FIERY, LEGS).setUnlocalizedName("fieryLegs").setMaxStackSize(1));
		items.register("fiery_boots", new ItemTFFieryArmor(ARMOR_FIERY, FEET).setUnlocalizedName("fieryBoots").setMaxStackSize(1));
		items.register("fiery_sword", new ItemTFFierySword(TOOL_FIERY).setUnlocalizedName("fierySword").setMaxStackSize(1));
		items.register("fiery_pickaxe", new ItemTFFieryPick(TOOL_FIERY).setUnlocalizedName("fieryPick").setMaxStackSize(1));
		items.register("steeleaf_ingot", new ItemTF().setUnlocalizedName("steeleafIngot"));
		items.register("steeleaf_helmet", new ItemTFSteeleafArmor(ARMOR_STEELEAF, HEAD).setUnlocalizedName("steeleafHelm").setMaxStackSize(1));
		items.register("steeleaf_chestplate", new ItemTFSteeleafArmor(ARMOR_STEELEAF, CHEST).setUnlocalizedName("steeleafPlate").setMaxStackSize(1));
		items.register("steeleaf_leggings", new ItemTFSteeleafArmor(ARMOR_STEELEAF, LEGS).setUnlocalizedName("steeleafLegs").setMaxStackSize(1));
		items.register("steeleaf_boots", new ItemTFSteeleafArmor(ARMOR_STEELEAF, FEET).setUnlocalizedName("steeleafBoots").setMaxStackSize(1));
		items.register("steeleaf_sword", new ItemTFSteeleafSword(TOOL_STEELEAF).setUnlocalizedName("steeleafSword").setMaxStackSize(1));
		items.register("steeleaf_shovel", new ItemTFSteeleafShovel(TOOL_STEELEAF).setUnlocalizedName("steeleafShovel").setMaxStackSize(1));
		items.register("steeleaf_pickaxe", new ItemTFSteeleafPick(TOOL_STEELEAF).setUnlocalizedName("steeleafPick").setMaxStackSize(1));
		items.register("steeleaf_axe", new ItemTFSteeleafAxe(TOOL_STEELEAF).setUnlocalizedName("steeleafAxe").setMaxStackSize(1));
		items.register("steeleaf_hoe", new ItemTFSteeleafHoe(TOOL_STEELEAF).setUnlocalizedName("steeleafHoe").setMaxStackSize(1));
		items.register("minotaur_axe", new ItemTFMinotaurAxe(DIAMOND).setUnlocalizedName("minotaurAxe").setMaxStackSize(1));
		items.register("mazebreaker_pickaxe", new ItemTFMazebreakerPick(DIAMOND).setUnlocalizedName("mazebreakerPick").setMaxStackSize(1));
		items.register("transformation_powder", new ItemTFTransformPowder().makeRare().setUnlocalizedName("transformPowder"));
		items.register("raw_meef", (new ItemTFFood(2, 0.3F, true)).setUnlocalizedName("meefRaw"));
		items.register("cooked_meef", (new ItemTFFood(6, 0.6F, true)).setUnlocalizedName("meefSteak"));
		items.register("meef_stroganoff", (new ItemTFSoup(8)).setUnlocalizedName("meefStroganoff"));
		items.register("maze_wafer", (new ItemTFFood(4, 0.6F, false)).setUnlocalizedName("mazeWafer"));
		items.register("magic_map_empty", (new ItemTFEmptyMagicMap()).setUnlocalizedName("emptyMagicMap"));
		items.register("maze_map_empty", (new ItemTFEmptyMazeMap(false)).setUnlocalizedName("emptyMazeMap"));
		items.register("ore_map_empty", (new ItemTFEmptyMazeMap(true)).setUnlocalizedName("emptyOreMap"));
		items.register("ore_magnet", (new ItemTFOreMagnet()).setUnlocalizedName("oreMagnet"));
		items.register("crumble_horn", (new ItemTFCrumbleHorn()).setUnlocalizedName("crumbleHorn"));
		items.register("peacock_fan", (new ItemTFPeacockFan()).setUnlocalizedName("peacockFan"));
		items.register("moonworm_queen", (new ItemTFMoonwormQueen()).setUnlocalizedName("moonwormQueen"));
		items.register("charm_of_life_1", new ItemCharmBaubleable().setUnlocalizedName("charmOfLife1"));
		items.register("charm_of_life_2", new ItemCharmBaubleable().setUnlocalizedName("charmOfLife2"));
		items.register("charm_of_keeping_1", new ItemCharmBaubleable().setUnlocalizedName("charmOfKeeping1"));
		items.register("charm_of_keeping_2", new ItemCharmBaubleable().setUnlocalizedName("charmOfKeeping2"));
		items.register("charm_of_keeping_3", new ItemCharmBaubleable().setUnlocalizedName("charmOfKeeping3"));
		items.register("tower_key", new ItemTFTowerKey().setUnlocalizedName("towerKey"));
		items.register("borer_essence", new ItemTF().setUnlocalizedName("borerEssence"));
		items.register("carminite", new ItemTF().makeRare().setUnlocalizedName("carminite"));
		items.register("experiment_115", new ItemTFExperiment115().setUnlocalizedName("experiment115"));
		items.register("armor_shard", new ItemTF().setUnlocalizedName("armorShards"));
		items.register("knightmetal_ingot", new ItemTF().setUnlocalizedName("knightMetal"));
		items.register("armor_shard_cluster", new ItemTF().setUnlocalizedName("shardCluster"));
		items.register("knightmetal_helmet", new ItemTFKnightlyArmor(ARMOR_KNIGHTLY, HEAD).setUnlocalizedName("knightlyHelm").setMaxStackSize(1));
		items.register("knightmetal_chestplate", new ItemTFKnightlyArmor(ARMOR_KNIGHTLY, CHEST).setUnlocalizedName("knightlyPlate").setMaxStackSize(1));
		items.register("knightmetal_leggings", new ItemTFKnightlyArmor(ARMOR_KNIGHTLY, LEGS).setUnlocalizedName("knightlyLegs").setMaxStackSize(1));
		items.register("knightmetal_boots", new ItemTFKnightlyArmor(ARMOR_KNIGHTLY, FEET).setUnlocalizedName("knightlyBoots").setMaxStackSize(1));
		items.register("knightmetal_sword", new ItemTFKnightlySword(TOOL_KNIGHTLY).setUnlocalizedName("knightlySword").setMaxStackSize(1));
		items.register("knightmetal_pickaxe", new ItemTFKnightlyPick(TOOL_KNIGHTLY).setUnlocalizedName("knightlyPick").setMaxStackSize(1));
		items.register("knightmetal_axe", new ItemTFKnightlyAxe(TOOL_KNIGHTLY).setUnlocalizedName("knightlyAxe").setMaxStackSize(1));
		items.register("phantom_helmet", new ItemTFPhantomArmor(ARMOR_PHANTOM, HEAD).setUnlocalizedName("phantomHelm").setMaxStackSize(1));
		items.register("phantom_chestplate", new ItemTFPhantomArmor(ARMOR_PHANTOM, CHEST).setUnlocalizedName("phantomPlate").setMaxStackSize(1));
		items.register("lamp_of_cinders", new ItemTFLampOfCinders().setUnlocalizedName("lampOfCinders"));
		items.register("fiery_tears", new ItemTF().makeRare().setUnlocalizedName("fieryTears"));
		items.register("alpha_fur", new ItemTF().makeRare().setUnlocalizedName("alphaFur"));
		items.register("yeti_helmet", new ItemTFYetiArmor(ARMOR_YETI, HEAD).setUnlocalizedName("yetiHelm").setMaxStackSize(1));
		items.register("yeti_chestplate", new ItemTFYetiArmor(ARMOR_YETI, CHEST).setUnlocalizedName("yetiPlate").setMaxStackSize(1));
		items.register("yeti_leggings", new ItemTFYetiArmor(ARMOR_YETI, LEGS).setUnlocalizedName("yetiLegs").setMaxStackSize(1));
		items.register("yeti_boots", new ItemTFYetiArmor(ARMOR_YETI, FEET).setUnlocalizedName("yetiBoots").setMaxStackSize(1));
		items.register("ice_bomb", new ItemTFIceBomb().makeRare().setUnlocalizedName("iceBomb").setMaxStackSize(16));
		items.register("arctic_fur", new ItemTF().setUnlocalizedName("arcticFur"));
		items.register("arctic_helmet", new ItemTFArcticArmor(ARMOR_ARCTIC, HEAD).setUnlocalizedName("arcticHelm").setMaxStackSize(1));
		items.register("arctic_chestplate", new ItemTFArcticArmor(ARMOR_ARCTIC, CHEST).setUnlocalizedName("arcticPlate").setMaxStackSize(1));
		items.register("arctic_leggings", new ItemTFArcticArmor(ARMOR_ARCTIC, LEGS).setUnlocalizedName("arcticLegs").setMaxStackSize(1));
		items.register("arctic_boots", new ItemTFArcticArmor(ARMOR_ARCTIC, FEET).setUnlocalizedName("arcticBoots").setMaxStackSize(1));
		items.register("magic_beans", new ItemTFMagicBeans().setUnlocalizedName("magicBeans"));
		items.register("giant_pickaxe", new ItemTFGiantPick(TOOL_GIANT).setUnlocalizedName("giantPick").setMaxStackSize(1));
		items.register("giant_sword", new ItemTFGiantSword(TOOL_GIANT).setUnlocalizedName("giantSword").setMaxStackSize(1));
		items.register("triple_bow", new ItemTFTripleBow().setUnlocalizedName("tripleBow").setMaxStackSize(1));
		items.register("seeker_bow", new ItemTFSeekerBow().setUnlocalizedName("seekerBow").setMaxStackSize(1));
		items.register("ice_bow", new ItemTFIceBow().setUnlocalizedName("iceBow").setMaxStackSize(1));
		items.register("ender_bow", new ItemTFEnderBow().setUnlocalizedName("enderBow").setMaxStackSize(1));
		items.register("ice_sword", new ItemTFIceSword(TOOL_ICE).setUnlocalizedName("iceSword").setMaxStackSize(1));
		items.register("glass_sword", new ItemTFGlassSword(TOOL_GLASS).setUnlocalizedName("glassSword").setMaxStackSize(1));
		items.register("knightmetal_ring", new ItemTF().setUnlocalizedName("knightmetalRing"));
		items.register("block_and_chain", new ItemTFChainBlock().setUnlocalizedName("chainBlock").setMaxStackSize(1));
		items.register("cube_talisman", new ItemTF().setUnlocalizedName("cubeTalisman"));
		items.register("cube_of_annihilation", new ItemTFCubeOfAnnihilation().setUnlocalizedName("cubeOfAnnihilation").setMaxStackSize(1));

		String[] thornNames = stream(ThornVariant.values()).map(IStringSerializable::getName).toArray(String[]::new);
		String[] deadrockNames = stream(DeadrockVariant.values()).map(IStringSerializable::getName).toArray(String[]::new);
		// register blocks with their pickup values
		items.registerSubItemBlock(TFBlocks.twilight_log);
		items.registerSubItemBlock(TFBlocks.root);
		items.register(new ItemBlockTFLeaves(TFBlocks.twilight_leaves));
		items.register(new ItemBlockWearable(TFBlocks.firefly));
		items.register(new ItemBlockWearable(TFBlocks.cicada));
		items.registerSubItemBlock(TFBlocks.maze_stone);
		items.registerSubItemBlock(TFBlocks.hedge);
		items.registerSubItemBlock(TFBlocks.bossSpawner);
		items.registerBlock(TFBlocks.firefly_jar);
		items.register(new ItemBlockTFPlant(TFBlocks.twilight_plant));
		items.registerBlock(TFBlocks.uncrafting_table);
		items.registerSubItemBlock(TFBlocks.fire_jet);
		items.registerSubItemBlock(TFBlocks.naga_stone);
		items.registerSubItemBlock(TFBlocks.twilight_sapling);
		items.register(new ItemBlockWearable(TFBlocks.moonworm));
		items.registerSubItemBlock(TFBlocks.magic_log);
		items.register(new ItemBlockTFLeaves(TFBlocks.magic_leaves));
		items.registerSubItemBlock(TFBlocks.magic_log_core);
		items.registerSubItemBlock(TFBlocks.tower_wood);
		items.registerSubItemBlock(TFBlocks.tower_device);
		items.registerSubItemBlock(TFBlocks.tower_translucent);
		items.registerSubItemBlock(TFBlocks.stronghold_shield);
		items.registerSubItemBlock(TFBlocks.trophy_pedestal);
		items.registerBlock(TFBlocks.aurora_block);
		items.registerSubItemBlock(TFBlocks.underbrick);
		items.register(new ItemMultiTexture(TFBlocks.thorns, TFBlocks.thorns, thornNames));
		items.registerBlock(TFBlocks.burnt_thorns);
		items.registerBlock(TFBlocks.thorn_rose);
		items.register(new ItemBlockTFLeaves(TFBlocks.twilight_leaves_3));
		items.register(new ItemMultiTexture(TFBlocks.deadrock, TFBlocks.deadrock, deadrockNames));
		items.registerBlock(TFBlocks.dark_leaves);
		items.registerBlock(TFBlocks.aurora_pillar);
		items.register(new ItemSlab(TFBlocks.aurora_slab, TFBlocks.aurora_slab, TFBlocks.double_aurora_slab));
		items.registerBlock(TFBlocks.trollsteinn);
		items.registerBlock(TFBlocks.wispy_cloud);
		items.registerBlock(TFBlocks.fluffy_cloud);
		items.register(new ItemTFGiantBlock(TFBlocks.giant_cobblestone));
		items.register(new ItemTFGiantBlock(TFBlocks.giant_log));
		items.register(new ItemTFGiantBlock(TFBlocks.giant_leaves));
		items.register(new ItemTFGiantBlock(TFBlocks.giant_obsidian));
		items.registerBlock(TFBlocks.uberous_soil);
		items.registerBlock(TFBlocks.huge_stalk);
		items.registerBlock(TFBlocks.huge_mushgloom);
		items.registerBlock(TFBlocks.trollvidr);
		items.registerBlock(TFBlocks.unripe_trollber);
		items.registerBlock(TFBlocks.trollber);
		items.registerBlock(TFBlocks.knightmetal_block);
		items.register(new ItemBlockTFHugeLilyPad(TFBlocks.huge_lilypad));
		items.register(new ItemBlockTFHugeWaterLily(TFBlocks.huge_waterlily));
		items.registerSubItemBlock(TFBlocks.slider);
		items.registerSubItemBlock(TFBlocks.castle_brick);
		items.registerSubItemBlock(TFBlocks.castle_pillar);
		items.registerSubItemBlock(TFBlocks.castle_stairs);
		items.registerSubItemBlock(TFBlocks.castle_rune_brick);
		items.registerSubItemBlock(TFBlocks.force_field);
		items.registerBlock(TFBlocks.cinder_furnace);
		items.registerSubItemBlock(TFBlocks.cinder_log);
		items.registerSubItemBlock(TFBlocks.castle_door);
		items.registerSubItemBlock(TFBlocks.castle_door_vanished);
		items.register(new ItemTFMiniatureStructure(TFBlocks.miniature_structure));
		items.register(new ItemTFCompressed(TFBlocks.block_storage));
		//items.registerBlock(TFBlocks.lapis_block);
		items.registerBlock(TFBlocks.spiral_bricks);
		items.registerBlock(TFBlocks.etched_nagastone);
		items.registerBlock(TFBlocks.nagastone_pillar);
		items.registerSubItemBlock(TFBlocks.nagastone_stairs);
		items.registerBlock(TFBlocks.etched_nagastone_mossy);
		items.registerBlock(TFBlocks.nagastone_pillar_mossy);
		items.registerSubItemBlock(TFBlocks.nagastone_stairs_mossy);
		items.registerBlock(TFBlocks.etched_nagastone_weathered);
		items.registerBlock(TFBlocks.nagastone_pillar_weathered);
		items.registerSubItemBlock(TFBlocks.nagastone_stairs_weathered);
	}

	private static class ItemRegistryHelper {
		private final IForgeRegistry<Item> registry;

		ItemRegistryHelper(IForgeRegistry<Item> registry) {
			this.registry = registry;
		}

		private void register(String registryName, Item item) {
			item.setRegistryName(TwilightForestMod.ID, registryName);
			registry.register(item);
		}

		private void registerBlock(Block block) {
			ItemBlock metaItemBlock = new ItemBlock(block);
			register(metaItemBlock);
		}

		private void registerSubItemBlock(Block block) {
			registerSubItemBlock(block, true);
		}

		private void registerSubItemBlock(Block block, boolean shouldAppendNumber) {
			ItemBlockTFMeta metaItemBlock = new ItemBlockTFMeta(block).setAppend(shouldAppendNumber);
			register(metaItemBlock);
		}

		private void register(ItemBlock item) {
			item.setRegistryName(item.getBlock().getRegistryName());
			item.setUnlocalizedName(item.getBlock().getUnlocalizedName());
			registry.register(item);
		}
	}
}
