package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public final class RegisterBlockEvent {
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		BlockRegistryHelper blocks = new BlockRegistryHelper(event.getRegistry());

		blocks.register("twilight_log", (new BlockTFLog()).setUnlocalizedName("TFLog"));
		blocks.register("root", (new BlockTFRoots()).setUnlocalizedName("TFRoots"));
		blocks.register("twilight_leaves", (new BlockTFLeaves()).setUnlocalizedName("TFLeaves"));
		blocks.register("firefly", (new BlockTFFirefly()).setUnlocalizedName("TFFirefly"));
		blocks.register("cicada", (new BlockTFCicada()).setUnlocalizedName("TFCicada"));
		blocks.register("twilight_portal", (new BlockTFPortal()).setUnlocalizedName("TFPortal"));
		blocks.register("maze_stone", (new BlockTFMazestone()).setUnlocalizedName("TFMazestone"));
		blocks.register("hedge", (new BlockTFHedge()).setUnlocalizedName("TFHedge"));
		blocks.register("boss_spawner", (new BlockTFBossSpawner()).setUnlocalizedName("TFBossSpawner").setBlockUnbreakable());
		blocks.register("firefly_jar", (new BlockTFFireflyJar()).setUnlocalizedName("TFFireflyJar"));
		blocks.register("twilight_plant", (new BlockTFPlant()).setUnlocalizedName("TFPlant"));
		blocks.register("uncrafting_table", (new BlockTFUncraftingTable()).setUnlocalizedName("TFUncraftingTable"));
		blocks.register("fire_jet", (new BlockTFFireJet()).setUnlocalizedName("TFFireJet"));
		blocks.register("naga_stone", (new BlockTFNagastone()).setUnlocalizedName("TFNagastone"));
		blocks.register("twilight_sapling", (new BlockTFSapling()).setUnlocalizedName("TFSapling"));
		blocks.register("moonworm", (new BlockTFMoonworm()).setUnlocalizedName("TFMoonworm"));
		blocks.register("magic_log", (new BlockTFMagicLog()).setUnlocalizedName("TFMagicLog"));
		blocks.register("magic_leaves", (new BlockTFMagicLeaves()).setUnlocalizedName("TFMagicLeaves"));
		blocks.register("magic_log_core", (new BlockTFMagicLogSpecial()).setUnlocalizedName("TFMagicLogSpecial"));
		blocks.register("tower_wood", (new BlockTFTowerWood()).setUnlocalizedName("TFTowerStone"));
		blocks.register("tower_device", (new BlockTFTowerDevice()).setUnlocalizedName("TFTowerDevice"));
		blocks.register("tower_translucent", (new BlockTFTowerTranslucent()).setUnlocalizedName("TFTowerTranslucent"));
		blocks.register("trophy", (new BlockTFTrophy()).setUnlocalizedName("TFTrophy"));
		blocks.register("stronghold_shield", (new BlockTFShield()).setUnlocalizedName("TFShield"));
		blocks.register("trophy_pedestal", (new BlockTFTrophyPedestal()).setUnlocalizedName("TFTrophyPedestal"));
		blocks.register("aurora_block", (new BlockTFAuroraBrick()).setUnlocalizedName("TFAuroraBrick"));
		blocks.register("underbrick", (new BlockTFUnderBrick()).setUnlocalizedName("TFUnderBrick"));
		blocks.register("thorns", (new BlockTFThorns()).setUnlocalizedName("TFThorns"));
		blocks.register("burnt_thorns", (new BlockTFBurntThorns()).setUnlocalizedName("TFBurntThorns"));
		blocks.register("thorn_rose", (new BlockTFThornRose()).setUnlocalizedName("TFThornRose"));
		blocks.register("twilight_leaves_3", (new BlockTFLeaves3()).setUnlocalizedName("TFLeaves3"));
		blocks.register("deadrock", (new BlockTFDeadrock()).setUnlocalizedName("TFDeadrock"));
		blocks.register("dark_leaves", (new BlockTFDarkLeaves()).setUnlocalizedName("DarkLeaves"));
		blocks.register("aurora_pillar", (new BlockTFAuroraPillar()).setUnlocalizedName("AuroraPillar"));
		blocks.register("aurora_slab", (new BlockTFAuroraSlab(false)).setUnlocalizedName("AuroraSlab"));
		blocks.register("double_aurora_slab", (new BlockTFAuroraSlab(true)).setUnlocalizedName("AuroraDoubleSlab"));
		blocks.register("trollsteinn", (new BlockTFTrollSteinn()).setUnlocalizedName("TrollSteinn"));
		blocks.register("wispy_cloud", (new BlockTFWispyCloud()).setUnlocalizedName("WispyCloud"));
		blocks.register("fluffy_cloud", (new BlockTFFluffyCloud()).setUnlocalizedName("FluffyCloud"));
		blocks.register("giant_cobblestone", (new BlockTFGiantCobble()).setUnlocalizedName("GiantCobble"));
		blocks.register("giant_log", (new BlockTFGiantLog()).setUnlocalizedName("GiantLog"));
		blocks.register("giant_leaves", (new BlockTFGiantLeaves()).setUnlocalizedName("GiantLeaves"));
		blocks.register("giant_obsidian", (new BlockTFGiantObsidian()).setUnlocalizedName("GiantObsidian"));
		blocks.register("uberous_soil", (new BlockTFUberousSoil()).setUnlocalizedName("UberousSoil"));
		blocks.register("huge_stalk", (new BlockTFHugeStalk()).setUnlocalizedName("HugeStalk"));
		blocks.register("huge_mushgloom", (new BlockTFHugeGloomBlock()).setUnlocalizedName("HugeGloomBlock"));
		blocks.register("trollvidr", (new BlockTFTrollRoot()).setUnlocalizedName("TrollVidr"));
		blocks.register("unripe_trollber", (new BlockTFUnripeTorchCluster()).setUnlocalizedName("UnripeTrollBer"));
		blocks.register("trollber", (new BlockTFRipeTorchCluster()).setUnlocalizedName("TrollBer"));
		blocks.register("knightmetal_block", (new BlockTFKnightmetalBlock()).setUnlocalizedName("KnightmetalBlock"));
		blocks.register("huge_lilypad", (new BlockTFHugeLilyPad()).setUnlocalizedName("HugeLilyPad"));
		blocks.register("huge_waterlily", (new BlockTFHugeWaterLily()).setUnlocalizedName("HugeWaterLily"));
		blocks.register("slider", (new BlockTFSlider()).setUnlocalizedName("Slider"));
		blocks.register("castle_brick", (new BlockTFCastleBlock()).setUnlocalizedName("CastleBrick"));
		Block castlePillar = new BlockTFCastlePillar();
		blocks.register("castle_pillar", castlePillar.setUnlocalizedName("CastlePillar"));
		blocks.register("castle_stairs", (new BlockTFCastleStairs(castlePillar.getDefaultState())).setUnlocalizedName("CastleStairs"));
		blocks.register("castle_rune_brick", (new BlockTFCastleMagic()).setUnlocalizedName("CastleMagic"));
		blocks.register("force_field", (new BlockTFForceField()).setUnlocalizedName("ForceField"));
		blocks.register("cinder_furnace", (new BlockTFCinderFurnace(false)).setUnlocalizedName("CinderFurnaceIdle"));
		blocks.register("cinder_furnace_lit", (new BlockTFCinderFurnace(true)).setUnlocalizedName("CinderFurnaceLit"));
		blocks.register("cinder_log", (new BlockTFCinderLog()).setUnlocalizedName("CinderLog"));
		blocks.register("castle_door", (new BlockTFCastleDoor(false)).setUnlocalizedName("CastleDoor"));
		blocks.register("castle_door_vanished", (new BlockTFCastleDoor(true)).setUnlocalizedName("CastleDoorVanished"));
		blocks.register("castle_unlock", (new BlockTFCastleUnlock()).setUnlocalizedName("CastleUnlock"));
		blocks.register("experiment_115", (new BlockTFExperiment115()).setUnlocalizedName("experiment115").setHardness(0.5F));
		blocks.register("miniature_structure", (new BlockTFMiniatureStructure()).setUnlocalizedName("MiniatureStructure").setHardness(0.75F));
		blocks.register("block_storage", new BlockTFCompressed().setUnlocalizedName("BlockOfStorage"));
		blocks.register("lapis_block", new BlockTFLapisBlock().setUnlocalizedName("BlockOfLapisTF"));
		blocks.register("spiral_bricks", new BlockTFSpiralBrick().setUnlocalizedName("SpiralBricks"));
		Block etchedNagastone = new BlockTFNagastoneEtched().setUnlocalizedName("EtchedNagastone").setHardness(1.5F).setResistance(10.0F);
		blocks.register("etched_nagastone", etchedNagastone);
		blocks.register("nagastone_stairs", new BlockTFNagastoneStairs(etchedNagastone.getDefaultState()).setUnlocalizedName("NagastoneStairs").setHardness(1.5F).setResistance(10.0F));
		blocks.register("nagastone_pillar", new BlockTFNagastonePillar().setUnlocalizedName("NagastonePillar").setHardness(1.5F).setResistance(10.0F));
		Block etchedNagastoneMossy = new BlockTFNagastoneEtched().setUnlocalizedName("EtchedNagastoneMossy").setHardness(1.5F).setResistance(10.0F);
		blocks.register("etched_nagastone_mossy", etchedNagastoneMossy);
		blocks.register("nagastone_stairs_mossy", new BlockTFNagastoneStairs(etchedNagastoneMossy.getDefaultState()).setUnlocalizedName("NagastoneStairsMossy").setHardness(1.5F).setResistance(10.0F));
		blocks.register("nagastone_pillar_mossy", new BlockTFNagastonePillar().setUnlocalizedName("NagastonePillarMossy").setHardness(1.5F).setResistance(10.0F));
		Block etchedNagastoneWeathered = new BlockTFNagastoneEtched().setUnlocalizedName("EtchedNagastoneWeathered").setHardness(1.5F).setResistance(10.0F);
		blocks.register("etched_nagastone_weathered", etchedNagastoneWeathered);
		blocks.register("nagastone_stairs_weathered", new BlockTFNagastoneStairs(etchedNagastoneWeathered.getDefaultState()).setUnlocalizedName("NagastoneStairsWeathered").setHardness(1.5F).setResistance(10.0F));
		blocks.register("nagastone_pillar_weathered", new BlockTFNagastonePillar().setUnlocalizedName("NagastonePillarWeathered").setHardness(1.5F).setResistance(10.0F));

		registerFluidBlock(blocks, moltenFiery);
		registerFluidBlock(blocks, moltenKnightmetal);
		registerFluidBlock("fiery_essence", blocks, essenceFiery);
	}

	private static class BlockRegistryHelper {
		private final IForgeRegistry<Block> registry;

		BlockRegistryHelper(IForgeRegistry<Block> registry) {
			this.registry = registry;
		}

		private void register(String registryName, Block block) {
			block.setRegistryName(TwilightForestMod.ID, registryName);
			registry.register(block);
		}
	}

	public static final Fluid moltenFiery;
	public static final Fluid moltenKnightmetal;
	public static final Fluid essenceFiery;

	public static final ResourceLocation moltenFieryStill = new ResourceLocation(TwilightForestMod.ID, "blocks/molten_fiery_still");
	public static final ResourceLocation moltenFieryFlow = new ResourceLocation(TwilightForestMod.ID,"blocks/molten_fiery_flow");

	public static final ResourceLocation moltenKnightmetalStill = new ResourceLocation(TwilightForestMod.ID, "blocks/molten_knightmetal_still");
	public static final ResourceLocation moltenKnightmetalFlow = new ResourceLocation(TwilightForestMod.ID,"blocks/molten_knightmetal_flow");

	public static final ResourceLocation essenceFieryStill = new ResourceLocation(TwilightForestMod.ID, "blocks/fluid_fiery_still");
	public static final ResourceLocation essenceFieryFlow = new ResourceLocation(TwilightForestMod.ID,"blocks/fluid_fiery_flow");

	static {
		moltenFiery       = registerFluid(new Fluid("fierymetal" , moltenFieryStill, moltenFieryFlow).setTemperature(1000).setLuminosity(15));
		moltenKnightmetal = registerFluid(new Fluid("knightmetal", moltenKnightmetalStill, moltenKnightmetalFlow).setTemperature(1000).setLuminosity(15));
		essenceFiery      = registerFluid(new Fluid("fiery_essence", essenceFieryStill, essenceFieryFlow).setTemperature(1000));
	}

	private static Fluid registerFluid(Fluid fluidIn) {
		fluidIn.setUnlocalizedName(fluidIn.getName());

		if (!FluidRegistry.isFluidRegistered(fluidIn.getName())) {
			FluidRegistry.registerFluid(fluidIn);

			FluidRegistry.addBucketForFluid(fluidIn);
		} else {
			fluidIn = FluidRegistry.getFluid(fluidIn.getName());
		}

		return fluidIn;
	}

	private static void registerFluidBlock(BlockRegistryHelper blocks, Fluid fluidIn) {
		registerFluidBlock("molten_" + fluidIn.getName(), blocks, fluidIn);
	}

	private static void registerFluidBlock(String registryName, BlockRegistryHelper blocks, Fluid fluidIn) {
		Block block = new BlockTFFluid(fluidIn, Material.LAVA).setUnlocalizedName(TwilightForestMod.ID + "." + fluidIn.getName()).setLightLevel(1.0F);
		blocks.register(registryName, block);
	}
}
