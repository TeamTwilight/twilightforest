package twilightforest.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.enums.TwilightArmorMaterial;
import twilightforest.enums.TwilightItemTier;

import javax.annotation.Nullable;
import java.util.UUID;

public class TFItems {
	public static final Food EXPERIMENT_115 = new Food.Builder().hunger(4).saturation(0.3F).build();
	public static final Food HYDRA_CHOP = new Food.Builder().hunger(18).saturation(2.0F).effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F).build();
	public static final Food MAZE_WAFER = new Food.Builder().hunger(4).saturation(0.6F).build();
	public static final Food MEEF_COOKED = new Food.Builder().hunger(6).saturation(0.6F).meat().build();
	public static final Food MEEF_RAW = new Food.Builder().hunger(2).saturation(0.3F).meat().build();
	public static final Food MEEF_STROGANOFF = new Food.Builder().hunger(8).saturation(0.6F).build();
	public static final Food VENISON_COOKED = new Food.Builder().hunger(8).saturation(0.8F).meat().build();
	public static final Food VENISON_RAW = new Food.Builder().hunger(3).saturation(0.3F).meat().build();

	static final UUID GIANT_REACH_MODIFIER = UUID.fromString("7f10172d-de69-49d7-81bd-9594286a6827");

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestMod.ID);

	public static final RegistryObject<Item> naga_scale = ITEMS.register("naga_scale", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> naga_chestplate = ITEMS.register("naga_chestplate", () -> new ItemTFNagaArmor(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlotType.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> naga_leggings = ITEMS.register("naga_leggings", () -> new ItemTFNagaArmor(TwilightArmorMaterial.ARMOR_NAGA, EquipmentSlotType.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> twilight_scepter = ITEMS.register("twilight_scepter", () -> new ItemTFTwilightWand(defaultBuilder().maxDamage(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> lifedrain_scepter = ITEMS.register("lifedrain_scepter", () -> new ItemTFScepterLifeDrain(defaultBuilder().maxDamage(99).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> zombie_scepter = ITEMS.register("zombie_scepter", () -> new ItemTFZombieWand(defaultBuilder().maxDamage(9).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> shield_scepter = ITEMS.register("shield_scepter", () -> new ItemTFShieldWand(defaultBuilder().maxDamage(9).rarity(Rarity.UNCOMMON)));
	//items.register("Wand of Pacification [NYI]", new Item().setIconIndex(6).setTranslationKey("wandPacification").setMaxStackSize(1));
	public static final RegistryObject<Item> ore_meter = ITEMS.register("ore_meter", () -> new ItemTFOreMeter(defaultBuilder()));
	public static final RegistryObject<Item> magic_map = ITEMS.register("magic_map", () -> new ItemTFMagicMap(unstackable()));
	public static final RegistryObject<Item> maze_map = ITEMS.register("maze_map", () -> new ItemTFMazeMap(false, unstackable()));
	public static final RegistryObject<Item> ore_map = ITEMS.register("ore_map", () -> new ItemTFMazeMap(true, unstackable().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> raven_feather = ITEMS.register("raven_feather", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> magic_map_focus = ITEMS.register("magic_map_focus", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> maze_map_focus = ITEMS.register("maze_map_focus", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> liveroot = ITEMS.register("liveroot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ironwood_raw = ITEMS.register("ironwood_raw", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ironwood_ingot = ITEMS.register("ironwood_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> ironwood_helmet = ITEMS.register("ironwood_helmet", () -> new ItemTFIronwoodArmor(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlotType.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_chestplate = ITEMS.register("ironwood_chestplate", () -> new ItemTFIronwoodArmor(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlotType.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_leggings = ITEMS.register("ironwood_leggings", () -> new ItemTFIronwoodArmor(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlotType.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_boots = ITEMS.register("ironwood_boots", () -> new ItemTFIronwoodArmor(TwilightArmorMaterial.ARMOR_IRONWOOD, EquipmentSlotType.FEET, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_sword = ITEMS.register("ironwood_sword", () -> new ItemTFIronwoodSword(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_shovel = ITEMS.register("ironwood_shovel", () -> new ItemTFIronwoodShovel(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_pickaxe = ITEMS.register("ironwood_pickaxe", () -> new ItemTFIronwoodPick(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_axe = ITEMS.register("ironwood_axe", () -> new ItemTFIronwoodAxe(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> ironwood_hoe = ITEMS.register("ironwood_hoe", () -> new ItemTFIronwoodHoe(TwilightItemTier.TOOL_IRONWOOD, defaultBuilder()));
	public static final RegistryObject<Item> torchberries = ITEMS.register("torchberries", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> raw_venison = ITEMS.register("raw_venison", () -> new Item(defaultBuilder().food(TFItems.VENISON_RAW)));
	public static final RegistryObject<Item> cooked_venison = ITEMS.register("cooked_venison", () -> new Item(defaultBuilder().food(TFItems.VENISON_COOKED)));
	public static final RegistryObject<Item> hydra_chop = ITEMS.register("hydra_chop", () -> new ItemTFHydraChops(defaultBuilder().isImmuneToFire().food(TFItems.HYDRA_CHOP).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_blood = ITEMS.register("fiery_blood", () -> new Item(defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_tears = ITEMS.register("fiery_tears", () -> new Item(defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> naga_trophy = ITEMS.register("naga_trophy", () -> new ItemTFTrophy(TFBlocks.naga_trophy.get(), TFBlocks.naga_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.NAGA));
	public static final RegistryObject<Item> lich_trophy = ITEMS.register("lich_trophy", () -> new ItemTFTrophy(TFBlocks.lich_trophy.get(), TFBlocks.lich_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.LICH));
	public static final RegistryObject<Item> minoshroom_trophy = ITEMS.register("minoshroom_trophy", () -> new ItemTFTrophy(TFBlocks.minoshroom_trophy.get(), TFBlocks.minoshroom_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.MINOSHROOM));
	public static final RegistryObject<Item> hydra_trophy = ITEMS.register("hydra_trophy", () -> new ItemTFTrophy(TFBlocks.hydra_trophy.get(), TFBlocks.hydra_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.HYDRA));
	public static final RegistryObject<Item> knight_phantom_trophy = ITEMS.register("knight_phantom_trophy", () -> new ItemTFTrophy(TFBlocks.knight_phantom_trophy.get(), TFBlocks.knight_phantom_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.KNIGHT_PHANTOM));
	public static final RegistryObject<Item> ur_ghast_trophy = ITEMS.register("ur_ghast_trophy", () -> new ItemTFTrophy(TFBlocks.ur_ghast_trophy.get(), TFBlocks.ur_ghast_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.UR_GHAST));
	public static final RegistryObject<Item> snow_queen_trophy = ITEMS.register("snow_queen_trophy", () -> new ItemTFTrophy(TFBlocks.snow_queen_trophy.get(), TFBlocks.snow_queen_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.SNOW_QUEEN));
	public static final RegistryObject<Item> quest_ram_trophy = ITEMS.register("quest_ram_trophy", () -> new ItemTFTrophy(TFBlocks.quest_ram_trophy.get(), TFBlocks.quest_ram_wall_trophy.get(), defaultBuilder().rarity(TwilightForestMod.getRarity()), BossVariant.QUEST_RAM));
	public static final RegistryObject<Item> fiery_ingot = ITEMS.register("fiery_ingot", () -> new Item(defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_helmet = ITEMS.register("fiery_helmet", () -> new ItemTFFieryArmor(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlotType.HEAD, defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_chestplate = ITEMS.register("fiery_chestplate", () -> new ItemTFFieryArmor(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlotType.CHEST, defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_leggings = ITEMS.register("fiery_leggings", () -> new ItemTFFieryArmor(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlotType.LEGS, defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_boots = ITEMS.register("fiery_boots", () -> new ItemTFFieryArmor(TwilightArmorMaterial.ARMOR_FIERY, EquipmentSlotType.FEET, defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_sword = ITEMS.register("fiery_sword", () -> new ItemTFFierySword(TwilightItemTier.TOOL_FIERY, defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> fiery_pickaxe = ITEMS.register("fiery_pickaxe", () -> new ItemTFFieryPick(TwilightItemTier.TOOL_FIERY, defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> steeleaf_ingot = ITEMS.register("steeleaf_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_helmet = ITEMS.register("steeleaf_helmet", () -> new ItemTFSteeleafArmor(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlotType.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_chestplate = ITEMS.register("steeleaf_chestplate", () -> new ItemTFSteeleafArmor(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlotType.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_leggings = ITEMS.register("steeleaf_leggings", () -> new ItemTFSteeleafArmor(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlotType.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_boots = ITEMS.register("steeleaf_boots", () -> new ItemTFSteeleafArmor(TwilightArmorMaterial.ARMOR_STEELEAF, EquipmentSlotType.FEET, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_sword = ITEMS.register("steeleaf_sword", () -> new ItemTFSteeleafSword(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_shovel = ITEMS.register("steeleaf_shovel", () -> new ItemTFSteeleafShovel(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_pickaxe = ITEMS.register("steeleaf_pickaxe", () -> new ItemTFSteeleafPick(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_axe = ITEMS.register("steeleaf_axe", () -> new ItemTFSteeleafAxe(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> steeleaf_hoe = ITEMS.register("steeleaf_hoe", () -> new ItemTFSteeleafHoe(TwilightItemTier.TOOL_STEELEAF, defaultBuilder()));
	public static final RegistryObject<Item> minotaur_axe_gold = ITEMS.register("minotaur_axe_gold", () -> new ItemTFMinotaurAxe(ItemTier.GOLD, defaultBuilder().rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> minotaur_axe = ITEMS.register("minotaur_axe", () -> new ItemTFMinotaurAxe(ItemTier.DIAMOND, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> mazebreaker_pickaxe = ITEMS.register("mazebreaker_pickaxe", () -> new ItemTFMazebreakerPick(ItemTier.DIAMOND, defaultBuilder().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> transformation_powder = ITEMS.register("transformation_powder", () -> new ItemTFTransformPowder(defaultBuilder()));
	public static final RegistryObject<Item> raw_meef = ITEMS.register("raw_meef", () -> new Item(defaultBuilder().food(TFItems.MEEF_RAW)));
	public static final RegistryObject<Item> cooked_meef = ITEMS.register("cooked_meef", () -> new Item(defaultBuilder().food(TFItems.MEEF_COOKED)));
	public static final RegistryObject<Item> meef_stroganoff = ITEMS.register("meef_stroganoff", () -> new ItemTFSoup(defaultBuilder().food(TFItems.MEEF_STROGANOFF)));
	public static final RegistryObject<Item> maze_wafer = ITEMS.register("maze_wafer", () -> new Item(defaultBuilder().food(TFItems.MAZE_WAFER)));
	public static final RegistryObject<Item> magic_map_empty = ITEMS.register("magic_map_empty", () -> new ItemTFEmptyMagicMap(defaultBuilder()));
	public static final RegistryObject<Item> maze_map_empty = ITEMS.register("maze_map_empty", () -> new ItemTFEmptyMazeMap(false, defaultBuilder()));
	public static final RegistryObject<Item> ore_map_empty = ITEMS.register("ore_map_empty", () -> new ItemTFEmptyMazeMap(true, defaultBuilder()));
	public static final RegistryObject<Item> ore_magnet = ITEMS.register("ore_magnet", () -> new ItemTFOreMagnet(defaultBuilder().maxDamage(12)));
	public static final RegistryObject<Item> crumble_horn = ITEMS.register("crumble_horn", () -> new ItemTFCrumbleHorn(defaultBuilder().maxDamage(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> peacock_fan = ITEMS.register("peacock_fan", () -> new ItemTFPeacockFan(defaultBuilder().maxDamage(1024).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> moonworm_queen = ITEMS.register("moonworm_queen", () -> new ItemTFMoonwormQueen(defaultBuilder().setNoRepair().maxDamage(256).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> charm_of_life_1 = ITEMS.register("charm_of_life_1", () -> new ItemCharmBaubleable(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_life_2 = ITEMS.register("charm_of_life_2", () -> new ItemCharmBaubleable(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_keeping_1 = ITEMS.register("charm_of_keeping_1", () -> new ItemCharmBaubleable(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_keeping_2 = ITEMS.register("charm_of_keeping_2", () -> new ItemCharmBaubleable(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> charm_of_keeping_3 = ITEMS.register("charm_of_keeping_3", () -> new ItemCharmBaubleable(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> tower_key = ITEMS.register("tower_key", () -> new Item(defaultBuilder().isImmuneToFire().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> borer_essence = ITEMS.register("borer_essence", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> carminite = ITEMS.register("carminite", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> experiment_115 = ITEMS.register("experiment_115", () -> new ItemTFExperiment115(TFBlocks.experiment_115.get(), defaultBuilder().food(TFItems.EXPERIMENT_115)));
	public static final RegistryObject<Item> armor_shard = ITEMS.register("armor_shard", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_ingot = ITEMS.register("knightmetal_ingot", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> armor_shard_cluster = ITEMS.register("armor_shard_cluster", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_helmet = ITEMS.register("knightmetal_helmet", () -> new ItemTFKnightlyArmor(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlotType.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_chestplate = ITEMS.register("knightmetal_chestplate", () -> new ItemTFKnightlyArmor(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlotType.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_leggings = ITEMS.register("knightmetal_leggings", () -> new ItemTFKnightlyArmor(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlotType.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_boots = ITEMS.register("knightmetal_boots", () -> new ItemTFKnightlyArmor(TwilightArmorMaterial.ARMOR_KNIGHTLY, EquipmentSlotType.FEET, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_sword = ITEMS.register("knightmetal_sword", () -> new ItemTFKnightlySword(TwilightItemTier.TOOL_KNIGHTLY, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_pickaxe = ITEMS.register("knightmetal_pickaxe", () -> new ItemTFKnightlyPick(TwilightItemTier.TOOL_KNIGHTLY, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_axe = ITEMS.register("knightmetal_axe", () -> new ItemTFKnightlyAxe(TwilightItemTier.TOOL_KNIGHTLY, defaultBuilder()));
	public static final RegistryObject<Item> knightmetal_shield = ITEMS.register("knightmetal_shield", () -> new ItemKnightlyShield(defaultBuilder().maxDamage(1024)));
	public static final RegistryObject<Item> phantom_helmet = ITEMS.register("phantom_helmet", () -> new ItemTFPhantomArmor(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlotType.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> phantom_chestplate = ITEMS.register("phantom_chestplate", () -> new ItemTFPhantomArmor(TwilightArmorMaterial.ARMOR_PHANTOM, EquipmentSlotType.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> lamp_of_cinders = ITEMS.register("lamp_of_cinders", () -> new ItemTFLampOfCinders(defaultBuilder().isImmuneToFire().maxDamage(1024).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> alpha_fur = ITEMS.register("alpha_fur", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_helmet = ITEMS.register("yeti_helmet", () -> new ItemTFYetiArmor(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlotType.HEAD, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_chestplate = ITEMS.register("yeti_chestplate", () -> new ItemTFYetiArmor(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlotType.CHEST, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_leggings = ITEMS.register("yeti_leggings", () -> new ItemTFYetiArmor(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlotType.LEGS, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> yeti_boots = ITEMS.register("yeti_boots", () -> new ItemTFYetiArmor(TwilightArmorMaterial.ARMOR_YETI, EquipmentSlotType.FEET, defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ice_bomb = ITEMS.register("ice_bomb", () -> new ItemTFIceBomb(defaultBuilder().maxStackSize(16)));
	public static final RegistryObject<Item> arctic_fur = ITEMS.register("arctic_fur", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> arctic_helmet = ITEMS.register("arctic_helmet", () -> new ItemTFArcticArmor(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlotType.HEAD, defaultBuilder()));
	public static final RegistryObject<Item> arctic_chestplate = ITEMS.register("arctic_chestplate", () -> new ItemTFArcticArmor(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlotType.CHEST, defaultBuilder()));
	public static final RegistryObject<Item> arctic_leggings = ITEMS.register("arctic_leggings", () -> new ItemTFArcticArmor(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlotType.LEGS, defaultBuilder()));
	public static final RegistryObject<Item> arctic_boots = ITEMS.register("arctic_boots", () -> new ItemTFArcticArmor(TwilightArmorMaterial.ARMOR_ARCTIC, EquipmentSlotType.FEET, defaultBuilder()));
	public static final RegistryObject<Item> magic_beans = ITEMS.register("magic_beans", () -> new ItemTFMagicBeans(defaultBuilder()));
	public static final RegistryObject<Item> giant_pickaxe = ITEMS.register("giant_pickaxe", () -> new ItemTFGiantPick(TwilightItemTier.TOOL_GIANT, defaultBuilder()));
	public static final RegistryObject<Item> giant_sword = ITEMS.register("giant_sword", () -> new ItemTFGiantSword(TwilightItemTier.TOOL_GIANT, defaultBuilder()));
	public static final RegistryObject<Item> triple_bow = ITEMS.register("triple_bow", () -> new ItemTFTripleBow(defaultBuilder().rarity(Rarity.UNCOMMON).maxDamage(384)));
	public static final RegistryObject<Item> seeker_bow = ITEMS.register("seeker_bow", () -> new ItemTFSeekerBow(defaultBuilder().rarity(Rarity.UNCOMMON).maxDamage(384)));
	public static final RegistryObject<Item> ice_bow = ITEMS.register("ice_bow", () -> new ItemTFIceBow(defaultBuilder().rarity(Rarity.UNCOMMON).maxDamage(384)));
	public static final RegistryObject<Item> ender_bow = ITEMS.register("ender_bow", () -> new ItemTFEnderBow(defaultBuilder().rarity(Rarity.UNCOMMON).maxDamage(384)));
	public static final RegistryObject<Item> ice_sword = ITEMS.register("ice_sword", () -> new ItemTFIceSword(TwilightItemTier.TOOL_ICE, defaultBuilder()));
	public static final RegistryObject<Item> glass_sword = ITEMS.register("glass_sword", () -> new ItemTFGlassSword(TwilightItemTier.TOOL_GLASS, defaultBuilder().setNoRepair().rarity(Rarity.RARE)));
	public static final RegistryObject<Item> knightmetal_ring = ITEMS.register("knightmetal_ring", () -> new Item(defaultBuilder()));
	public static final RegistryObject<Item> block_and_chain = ITEMS.register("block_and_chain", () -> new ItemTFChainBlock(defaultBuilder().maxDamage(99)));
	public static final RegistryObject<Item> cube_talisman = ITEMS.register("cube_talisman", () -> new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> cube_of_annihilation = ITEMS.register("cube_of_annihilation", () -> new ItemTFCubeOfAnnihilation(unstackable().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> moon_dial = ITEMS.register("moon_dial", () -> new Item(defaultBuilder()));

	public static ItemGroup creativeTab = new ItemGroup(TwilightForestMod.ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(TFBlocks.twilight_portal_miniature_structure.get());
		}
	};

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().group(creativeTab);
	}

	public static Item.Properties unstackable() {
		return defaultBuilder().maxStackSize(1);
	}

	@OnlyIn(Dist.CLIENT)
	public static void addItemModelProperties() {
		ItemModelsProperties.registerProperty(cube_of_annihilation.get(), TwilightForestMod.prefix("thrown"), (stack, world, entity) ->
				ItemTFCubeOfAnnihilation.getThrownUuid(stack) != null ? 1 : 0);
		ItemModelsProperties.registerProperty(moon_dial.get(), new ResourceLocation("phase"), new IItemPropertyGetter() {
			@Override
			public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entityBase) {
				boolean flag = entityBase != null;
				Entity entity = flag ? entityBase : stack.getItemFrame();

				if (world == null && entity != null) world = (ClientWorld) entity.world;

				return world == null ? 0.0F : (float) (world.getDimensionType().isNatural() ? MathHelper.frac(world.getMoonPhase() / 8.0f) : this.wobble(world, Math.random()));
			}

			@OnlyIn(Dist.CLIENT)
			double rotation;
			@OnlyIn(Dist.CLIENT)
			double rota;
			@OnlyIn(Dist.CLIENT)
			long lastUpdateTick;

			@OnlyIn(Dist.CLIENT)
			private double wobble(World world, double rotation) {
				if (world.getGameTime() != this.lastUpdateTick) {
					this.lastUpdateTick = world.getGameTime();
					double delta = rotation - this.rotation;
					delta = MathHelper.positiveModulo(delta + 0.5D, 1.0D) - 0.5D;
					this.rota += delta * 0.1D;
					this.rota *= 0.9D;
					this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
				}

				return this.rotation;
			}
		});
		ItemModelsProperties.registerProperty(moonworm_queen.get(), TwilightForestMod.prefix("alt"), (stack, world, entity) -> {
			if (entity != null && entity.getActiveItemStack() == stack) {
				int useTime = stack.getUseDuration() - entity.getItemInUseCount();
				if (useTime >= ItemTFMoonwormQueen.FIRING_TIME && (useTime >>> 1) % 2 == 0) {
					return 1;
				}
			}

			return 0;
		});
		ItemModelsProperties.registerProperty(ore_magnet.get(), new ResourceLocation("pull"), (stack, world, entity) -> {
			if (entity == null) {
				return 0.0F;
			} else {
				ItemStack itemstack = entity.getActiveItemStack();
				return !itemstack.isEmpty() ? (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F : 0.0F;
			}
		});
		ItemModelsProperties.registerProperty(ore_magnet.get(), new ResourceLocation("pulling"), (stack, world, entity) ->
				entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
		ItemModelsProperties.registerProperty(block_and_chain.get(), TwilightForestMod.prefix("thrown"), (stack, world, entity) ->
				ItemTFChainBlock.getThrownUuid(stack) != null ? 1 : 0);
		ItemModelsProperties.registerProperty(experiment_115.get(), ItemTFExperiment115.THINK, (stack, world, entity) ->
				stack.hasTag() && stack.getTag().contains("think") ? 1 : 0);
		ItemModelsProperties.registerProperty(experiment_115.get(), ItemTFExperiment115.FULL, (stack, world, entity) ->
				stack.hasTag() && stack.getTag().contains("full") ? 1 : 0);
	}
}
