package twilightforest;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

public class TFAchievementPage extends net.minecraftforge.common.AchievementPage {
	
	
	public static Achievement twilightPortal = (new Achievement(TwilightForestMod.ID + 1, "twilightPortal", -2, 1, TFBlocks.portal, (Achievement)null)).setSpecial().registerStat();
	public static Achievement twilightArrival = (new Achievement(TwilightForestMod.ID + 2, "twilightArrival", 0, 0, new ItemStack(TFBlocks.log, 1, 9), twilightPortal)).registerStat();
	public static Achievement twilightHunter = (new Achievement(TwilightForestMod.ID + 3, "twilightHunter", 2, 2, TFItems.feather, twilightArrival)).registerStat();
	public static Achievement twilightMagicMapFocus = (new Achievement(TwilightForestMod.ID + 5, "twilightMagicMapFocus", 2, 0, TFItems.magicMapFocus, twilightHunter)).registerStat();
	public static Achievement twilightHill1 = (new Achievement(TwilightForestMod.ID + 10, "twilightHill1", -2, -1, Blocks.IRON_ORE, twilightArrival)).registerStat();
	public static Achievement twilightHill2 = (new Achievement(TwilightForestMod.ID + 11, "twilightHill2", -3, -2, Blocks.GOLD_ORE, twilightArrival)).registerStat();
	public static Achievement twilightHill3 = (new Achievement(TwilightForestMod.ID + 12, "twilightHill3", -1, -3, Blocks.DIAMOND_ORE, twilightArrival)).registerStat();
	public static Achievement twilightHedge = (new Achievement(TwilightForestMod.ID + 13, "twilightHedge", 2, -3, TFBlocks.hedge, twilightArrival)).registerStat();
	public static Achievement twilightMagicMap = (new Achievement(TwilightForestMod.ID + 14, "twilightMagicMap", 4, -1, TFItems.magicMap, twilightMagicMapFocus)).registerStat();

	public static Achievement twilightKillNaga = (new Achievement(TwilightForestMod.ID + 6, "twilightKillNaga", 4, 3, new ItemStack(TFItems.trophy, 1, 0), twilightHunter)).registerStat();
	public static Achievement twilightProgressNaga = (new Achievement(TwilightForestMod.ID + 17, "twilightProgressNaga", 4, 5, TFItems.nagaScale, twilightKillNaga)).registerStat();
	public static Achievement twilightKillLich = (new Achievement(TwilightForestMod.ID + 8, "twilightKillLich", 2, 5, new ItemStack(TFItems.trophy, 1, 1), twilightProgressNaga)).registerStat();
	public static Achievement twilightProgressLich = (new Achievement(TwilightForestMod.ID + 18, "twilightProgressLich", -1, 4, TFItems.scepterLifeDrain, twilightKillLich)).registerStat();
	public static Achievement twilightProgressLabyrinth = (new Achievement(TwilightForestMod.ID + 28, "twilightProgressLabyrinth", -4, 6, TFItems.meefStroganoff, twilightProgressLich)).registerStat();
	public static Achievement twilightKillHydra = (new Achievement(TwilightForestMod.ID + 30, "twilightKillHydra", -6, 4, new ItemStack(TFItems.trophy, 1, 2), twilightProgressLabyrinth)).registerStat();
	public static Achievement twilightProgressHydra = (new Achievement(TwilightForestMod.ID + 20, "twilightProgressHydra", -8, 4, TFItems.fieryBlood, twilightKillHydra)).registerStat();
	public static Achievement twilightProgressTrophyPedestal = (new Achievement(TwilightForestMod.ID + 29, "twilightProgressTrophyPedestal", -5, 2, TFBlocks.trophyPedestal, twilightProgressHydra)).registerStat();
	public static Achievement twilightProgressKnights = (new Achievement(TwilightForestMod.ID + 21, "twilightProgressKnights", -5, -1, TFItems.phantomHelm, twilightProgressTrophyPedestal)).registerStat();
	public static Achievement twilightProgressUrghast = (new Achievement(TwilightForestMod.ID + 22, "twilightProgressUrghast", -7, -1, new ItemStack(TFItems.trophy, 1, 3), twilightProgressKnights)).registerStat();
	public static Achievement twilightProgressYeti = (new Achievement(TwilightForestMod.ID + 23, "twilightProgressYeti", -7, -3, TFItems.alphaFur, twilightProgressUrghast)).registerStat();
	public static Achievement twilightProgressGlacier = (new Achievement(TwilightForestMod.ID + 24, "twilightProgressGlacier", -5, -5, new ItemStack(TFItems.trophy, 1, 5), twilightProgressYeti)).registerStat();
	public static Achievement twilightProgressTroll = (new Achievement(TwilightForestMod.ID + 25, "twilightProgressTroll", -5, -7, TFItems.lampOfCinders, twilightProgressGlacier)).registerStat();
	public static Achievement twilightProgressThorns = (new Achievement(TwilightForestMod.ID + 26, "twilightProgressThorns", -3, -7, TFBlocks.thorns, twilightProgressTroll)).registerStat();
	public static Achievement twilightProgressCastle = (new Achievement(TwilightForestMod.ID + 27, "twilightProgressCastle", -1, -7, Blocks.STONEBRICK, twilightProgressThorns)).registerStat();

	public static Achievement twilightNagaArmors = (new Achievement(TwilightForestMod.ID + 7, "twilightNagaArmors", 5, 1, TFItems.plateNaga, twilightKillNaga)).setSpecial().registerStat();
	public static Achievement twilightLichScepters = (new Achievement(TwilightForestMod.ID + 9, "twilightLichScepters", 3, 7, TFItems.scepterZombie, twilightKillLich)).setSpecial().registerStat();

	public static Achievement twilightMazeMap = (new Achievement(TwilightForestMod.ID + 15, "twilightMazeMap", 1, 7, TFItems.mazeMap, twilightProgressLich)).registerStat();
	public static Achievement twilightOreMap = (new Achievement(TwilightForestMod.ID + 16, "twilightOreMap", 1, 9, TFItems.oreMap, twilightMazeMap)).setSpecial().registerStat();

	public static Achievement twilightHydraChop = (new Achievement(TwilightForestMod.ID + 31, "twilightHydraChop", -6, 6, TFItems.hydraChop, twilightKillHydra)).registerStat();
	public static Achievement twilightMazebreaker = (new Achievement(TwilightForestMod.ID + 32, "twilightMazebreaker", -3, 4, TFItems.mazebreakerPick, twilightProgressLich)).setSpecial().registerStat();
	public static Achievement twilightFierySet = (new Achievement(TwilightForestMod.ID + 33, "twilightFierySet", -8, 7, TFItems.fierySword, twilightProgressHydra)).setSpecial().registerStat();
	public static Achievement twilightQuestRam = (new Achievement(TwilightForestMod.ID + 34, "twilightQuestRam", 1, -5, TFItems.crumbleHorn, twilightArrival)).setSpecial().registerStat();



	public TFAchievementPage() {
		super("Twilight Forest", twilightPortal, twilightArrival, twilightHunter, twilightMagicMapFocus, twilightKillNaga, twilightNagaArmors, twilightKillLich, 
				twilightLichScepters, twilightHill1, twilightHill2, twilightHill3, twilightHedge, twilightMagicMap, twilightMazeMap, twilightOreMap,
				twilightProgressNaga, twilightProgressLich, twilightProgressLabyrinth, twilightHydraChop, twilightProgressKnights, twilightProgressUrghast, 
				twilightProgressYeti, twilightProgressGlacier, twilightProgressTroll, twilightProgressThorns, twilightProgressCastle,
				twilightKillHydra, twilightHydraChop, twilightProgressTrophyPedestal, twilightProgressHydra, twilightMazebreaker, 
				twilightFierySet, twilightQuestRam);
	}

}
