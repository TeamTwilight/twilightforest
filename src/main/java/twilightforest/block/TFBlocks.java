package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.IStringSerializable;
import twilightforest.TwilightForestMod;
import twilightforest.block.enums.DeadrockVariant;
import twilightforest.block.enums.ThornVariant;
import twilightforest.item.ItemBlockTFHugeLilyPad;
import twilightforest.item.ItemBlockTFHugeWaterLily;
import twilightforest.item.ItemBlockTFMeta;
import net.minecraftforge.fml.common.registry.GameRegistry;
import twilightforest.item.ItemBlockTFPlant;

import java.util.Arrays;

public class TFBlocks {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public static Block log;
    public static Block leaves;
    public static Block firefly;
    public static Block portal;
    public static Block mazestone;
    public static Block hedge;
    public static Block bossSpawner;
    public static Block fireflyJar;
    public static Block plant;
    public static Block cicada;
    public static Block root;
    public static Block uncraftingTable;
    public static Block fireJet;
    public static Block nagastone;
    public static Block sapling;
    public static Block magicLog;
    public static Block magicLogSpecial;
    public static Block magicLeaves;
    public static Block moonworm;
    public static Block towerWood;
    public static Block towerDevice;
    //public static Block towerAntenna;
    public static Block towerTranslucent;
    public static Block trophy;
    public static Block shield;
    public static Block trophyPedestal;
    public static Block auroraBlock;
    public static Block underBrick;
    public static Block thorns;
    public static Block burntThorns;
    public static Block thornRose;
    public static Block leaves3;
    public static Block deadrock;
    public static Block darkleaves;
    public static Block auroraPillar;
    public static BlockSlab auroraSlab;
    public static BlockSlab auroraDoubleSlab;
    public static Block trollSteinn;
    public static Block wispyCloud;
    public static Block fluffyCloud;
    public static Block giantCobble;
    public static Block giantLog;
    public static Block giantLeaves;
    public static Block giantObsidian;
    public static Block uberousSoil;
    public static Block hugeStalk;
    public static Block hugeGloomBlock;
    public static Block trollVidr;
    public static Block unripeTrollBer;
    public static Block trollBer;
    public static Block knightmetalStorage;
    public static Block hugeLilyPad;
    public static Block hugeWaterLily;
    public static Block slider;
    public static Block castleBlock;
    public static Block castleMagic;
    public static Block forceField;
    public static Block cinderFurnace;
    public static Block cinderFurnaceLit;
    public static Block cinderLog;
    public static Block castleDoor;
    public static Block castleDoorVanished;
    public static Block castleUnlock;

	public static void registerBlocks() {
		
        log = (new BlockTFLog()).setUnlocalizedName("TFLog");
        leaves = (new BlockTFLeaves()).setUnlocalizedName("TFLeaves");
        firefly = (new BlockTFFirefly()).setUnlocalizedName("TFFirefly");
        cicada = (new BlockTFCicada()).setUnlocalizedName("TFCicada");
        portal = (new BlockTFPortal()).setUnlocalizedName("TFPortal");
        mazestone = (new BlockTFMazestone()).setUnlocalizedName("TFMazestone");
        hedge = (new BlockTFHedge()).setUnlocalizedName("TFHedge");
        bossSpawner = (new BlockTFBossSpawner()).setUnlocalizedName("TFBossSpawner");
        fireflyJar = (new BlockTFFireflyJar()).setUnlocalizedName("TFFireflyJar");
        plant = (new BlockTFPlant()).setUnlocalizedName("TFPlant");
        root = (new BlockTFRoots()).setUnlocalizedName("TFRoots");
        uncraftingTable = (new BlockTFUncraftingTable()).setUnlocalizedName("TFUncraftingTable");
        fireJet = (new BlockTFFireJet()).setUnlocalizedName("TFFireJet");
        nagastone = (new BlockTFNagastone()).setUnlocalizedName("TFNagastone");
        sapling = (new BlockTFSapling()).setUnlocalizedName("TFSapling");
        magicLog = (new BlockTFMagicLog()).setUnlocalizedName("TFMagicLog");
        magicLogSpecial = (new BlockTFMagicLogSpecial()).setUnlocalizedName("TFMagicLogSpecial");
        magicLeaves = (new BlockTFMagicLeaves()).setUnlocalizedName("TFMagicLeaves");
        moonworm = (new BlockTFMoonworm()).setUnlocalizedName("TFMoonworm");
        towerWood = (new BlockTFTowerWood()).setUnlocalizedName("TFTowerStone");
        towerDevice = (new BlockTFTowerDevice()).setUnlocalizedName("TFTowerDevice");
        towerTranslucent = (new BlockTFTowerTranslucent()).setUnlocalizedName("TFTowerTranslucent");
        trophy = (new BlockTFTrophy()).setUnlocalizedName("TFTrophy");
        shield = (new BlockTFShield()).setUnlocalizedName("TFShield");
        trophyPedestal = (new BlockTFTrophyPedestal()).setUnlocalizedName("TFTrophyPedestal");
        auroraBlock = (new BlockTFAuroraBrick()).setUnlocalizedName("TFAuroraBrick");
        underBrick = (new BlockTFUnderBrick()).setUnlocalizedName("TFUnderBrick");
        thorns = (new BlockTFThorns()).setUnlocalizedName("TFThorns");
        burntThorns = (new BlockTFBurntThorns()).setUnlocalizedName("TFBurntThorns");
        thornRose = (new BlockTFThornRose()).setUnlocalizedName("TFThornRose");
        leaves3 = (new BlockTFLeaves3()).setUnlocalizedName("TFLeaves3");
        deadrock = (new BlockTFDeadrock()).setUnlocalizedName("TFDeadrock"); 
        darkleaves = (new BlockTFDarkLeaves()).setUnlocalizedName("DarkLeaves"); 
        auroraPillar = (new BlockTFAuroraPillar()).setUnlocalizedName("AuroraPillar"); 
        auroraSlab = (BlockSlab) (new BlockTFAuroraSlab(false)).setUnlocalizedName("AuroraSlab");
        auroraDoubleSlab = (BlockSlab) (new BlockTFAuroraSlab(true)).setUnlocalizedName("AuroraDoubleSlab");
        trollSteinn = (new BlockTFTrollSteinn()).setUnlocalizedName("TrollSteinn");
        wispyCloud = (new BlockTFWispyCloud()).setUnlocalizedName("WispyCloud");
        fluffyCloud = (new BlockTFFluffyCloud()).setUnlocalizedName("FluffyCloud");
        giantCobble = (new BlockTFGiantCobble()).setUnlocalizedName("GiantCobble");
        giantLog = (new BlockTFGiantLog()).setUnlocalizedName("GiantLog");
        giantLeaves = (new BlockTFGiantLeaves()).setUnlocalizedName("GiantLeaves");
        giantObsidian = (new BlockTFGiantObsidian()).setUnlocalizedName("GiantObsidian");
        uberousSoil = (new BlockTFUberousSoil()).setUnlocalizedName("UberousSoil");
        hugeStalk = (new BlockTFHugeStalk()).setUnlocalizedName("HugeStalk");
        hugeGloomBlock = (new BlockTFHugeGloomBlock()).setUnlocalizedName("HugeGloomBlock");
        trollVidr = (new BlockTFTrollRoot()).setUnlocalizedName("TrollVidr");
        unripeTrollBer = (new BlockTFUnripeTorchCluster()).setUnlocalizedName("UnripeTrollBer");
        trollBer = (new BlockTFRipeTorchCluster()).setUnlocalizedName("TrollBer");
        knightmetalStorage = (new BlockTFKnightmetalBlock()).setUnlocalizedName("KnightmetalBlock");
        hugeLilyPad = (new BlockTFHugeLilyPad()).setUnlocalizedName("HugeLilyPad");
        hugeWaterLily = (new BlockTFHugeWaterLily()).setUnlocalizedName("HugeWaterLily");
        slider = (new BlockTFSlider()).setUnlocalizedName("Slider");
        castleBlock = (new BlockTFCastleBlock()).setUnlocalizedName("CastleBrick");
        castleMagic = (new BlockTFCastleMagic()).setUnlocalizedName("CastleMagic");
        forceField = (new BlockTFForceField()).setUnlocalizedName("ForceField");
        cinderFurnace = (new BlockTFCinderFurnace(false)).setUnlocalizedName("CinderFurnaceIdle");
        cinderFurnaceLit = (new BlockTFCinderFurnace(true)).setUnlocalizedName("CinderFurnaceLit");
        cinderLog = (new BlockTFCinderLog()).setUnlocalizedName("CinderLog");
        castleDoor = (new BlockTFCastleDoor(false)).setUnlocalizedName("CastleDoor");
        castleDoorVanished = (new BlockTFCastleDoor(true)).setUnlocalizedName("CastleDoorVanished");
        castleUnlock = (new BlockTFCastleUnlock()).setUnlocalizedName("CastleUnlock");

		// register blocks with their pickup values
		registerMyBlock(log, "twilight_log");
		registerMyBlock(root, "root");
		registerMyBlock(leaves, "twilight_leaves");
		registerMyBlock(firefly, "firefly", new ItemBlock(firefly));
		registerMyBlock(cicada, "cicada", new ItemBlock(cicada));
		registerMyBlock(portal, "twilight_portal", null);
		registerMyBlock(mazestone, "maze_stone");
		registerMyBlock(hedge, "hedge");
		registerMyBlock(bossSpawner, "boss_spawner");
		registerMyBlock(fireflyJar, "firefly_jar", new ItemBlock(fireflyJar));
		registerMyBlock(plant, "twilight_plant", new ItemBlockTFPlant(plant));
		registerMyBlock(uncraftingTable, "uncrafting_table", new ItemBlock(uncraftingTable));
		registerMyBlock(fireJet, "fire_jet");
		registerMyBlock(nagastone, "naga_stone");
		registerMyBlock(sapling, "twilight_sapling");
		registerMyBlock(moonworm, "moonworm", new ItemBlock(moonworm));
		registerMyBlock(magicLog, "magic_log");
		registerMyBlock(magicLeaves, "magic_leaves");
		registerMyBlock(magicLogSpecial, "magic_log_core");
		registerMyBlock(towerWood, "tower_wood");
		registerMyBlock(towerDevice, "tower_device");
		registerMyBlock(towerTranslucent, "tower_translucent");
		registerMyBlockWithoutItem(trophy, "trophy");
		registerMyBlock(shield, "stronghold_shield");
		registerMyBlock(trophyPedestal, "trophy_pedestal");
		registerMyBlock(auroraBlock, "aurora_block", new ItemBlock(auroraBlock));
		registerMyBlock(underBrick, "underbrick");
		String[] thornNames = Arrays.stream(ThornVariant.values()).map(IStringSerializable::getName).toArray(String[]::new);
		registerMyBlock(thorns, "thorns", new ItemMultiTexture(thorns, thorns, thornNames));
		registerMyBlock(burntThorns, "burnt_thorns", new ItemBlock(burntThorns));
		registerMyBlock(thornRose, "thorn_rose", new ItemBlock(thornRose));
		registerMyBlock(leaves3, "twilight_leaves_3");
        String[] deadrockNames = Arrays.stream(DeadrockVariant.values()).map(IStringSerializable::getName).toArray(String[]::new);
		registerMyBlock(deadrock, "deadrock", new ItemMultiTexture(deadrock, deadrock, deadrockNames));
		registerMyBlock(darkleaves, "dark_leaves", new ItemBlock(darkleaves));
		registerMyBlock(auroraPillar, "aurora_pillar", new ItemBlock(auroraPillar));
		registerMyBlock(auroraSlab,  "aurora_slab", new ItemSlab(auroraSlab, auroraSlab, auroraDoubleSlab));
        registerMyBlock(auroraDoubleSlab,  "double_aurora_slab", null);
		registerMyBlock(trollSteinn, "trollsteinn", new ItemBlock(trollSteinn));
		registerMyBlock(wispyCloud, "wispy_cloud", new ItemBlock(wispyCloud));
		registerMyBlock(fluffyCloud, "fluffy_cloud", new ItemBlock(fluffyCloud));
		registerMyBlock(giantCobble, "giant_cobblestone", new ItemBlock(giantCobble));
		registerMyBlock(giantLog, "giant_log", new ItemBlock(giantLog));
		registerMyBlock(giantLeaves, "giant_leaves", new ItemBlock(giantLeaves));
		registerMyBlock(giantObsidian, "giant_obsidian", new ItemBlock(giantObsidian));
		registerMyBlock(uberousSoil, "uberous_soil", new ItemBlock(uberousSoil));
		registerMyBlock(hugeStalk, "huge_stalk", new ItemBlock(hugeStalk));
		registerMyBlock(hugeGloomBlock, "huge_mushgloom", new ItemBlock(hugeGloomBlock));
		registerMyBlock(trollVidr, "trollvidr", new ItemBlock(trollVidr));
		registerMyBlock(unripeTrollBer, "unripe_trollber", new ItemBlock(unripeTrollBer));
		registerMyBlock(trollBer, "trollber", new ItemBlock(trollBer));
		registerMyBlock(knightmetalStorage, "knightmetal_block", new ItemBlock(knightmetalStorage));
		registerMyBlock(hugeLilyPad, "huge_lilypad", new ItemBlockTFHugeLilyPad(hugeLilyPad));
		registerMyBlock(hugeWaterLily, "huge_waterlily", new ItemBlockTFHugeWaterLily(hugeWaterLily));
		registerMyBlock(slider, "slider");
		registerMyBlock(castleBlock, "castle_brick");
		registerMyBlock(castleMagic, "castle_rune_brick");
		registerMyBlock(forceField, "force_field");
		registerMyBlock(cinderFurnace, "cinder_furnace", new ItemBlock(cinderFurnace));
		registerMyBlock(cinderFurnaceLit, "cinder_furnace_lit", null);
		registerMyBlock(cinderLog, "cinder_log");
		registerMyBlock(castleDoor, "castle_door");
		registerMyBlock(castleDoorVanished, "castle_door_vanished");

		// fire info
        Blocks.FIRE.setFireInfo(log, 5, 5);
        Blocks.FIRE.setFireInfo(leaves, 30, 60);
        Blocks.FIRE.setFireInfo(leaves3, 30, 60);
	}

	private static void registerMyBlockWithoutItem(Block block, String registryName)
	{
		registerMyBlock(block, registryName, null);
	}

	private static void registerMyBlock(Block block, String registryName)
    {
        registerMyBlock(block, registryName, new ItemBlockTFMeta(block));
    }

	private static void registerMyBlock(Block block, String registryName, Item itemForm)
    {
        block.setRegistryName(TwilightForestMod.ID, registryName);
        GameRegistry.register(block);

        if (itemForm != null)
        {
            itemForm.setRegistryName(TwilightForestMod.ID, registryName);
            GameRegistry.register(itemForm);
        }
    }
}
