package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.enums.CastleBrickVariant;
import twilightforest.enums.MagicWoodVariant;
import twilightforest.enums.WoodVariant;
import twilightforest.item.TFItems;
import twilightforest.util.IMapColorSupplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public final class RegisterBlockEvent {
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		BlockRegistryHelper blocks = new BlockRegistryHelper(event.getRegistry());

		blocks.register("twilight_log", "Log", new BlockTFLog());
		blocks.register("root", "Roots", new BlockTFRoots());
		blocks.register("twilight_leaves", "Leaves", new BlockTFLeaves());
		blocks.register("firefly", "Firefly", new BlockTFFirefly());
		blocks.register("cicada", "Cicada", new BlockTFCicada());
		blocks.register("twilight_portal", "Portal", new BlockTFPortal());
		blocks.register("maze_stone", "Mazestone", new BlockTFMazestone());
		blocks.register("hedge", "Hedge", new BlockTFHedge());
		blocks.register("boss_spawner", "BossSpawner", new BlockTFBossSpawner().setBlockUnbreakable());
		blocks.register("firefly_jar", "FireflyJar", new BlockTFFireflyJar());
		blocks.register("twilight_plant", "Plant", new BlockTFPlant());
		blocks.register("uncrafting_table", "UncraftingTable", new BlockTFUncraftingTable());
		blocks.register("fire_jet", "FireJet", new BlockTFFireJet());
		blocks.register("naga_stone", "Nagastone", new BlockTFNagastone());
		blocks.register("twilight_sapling", "Sapling", new BlockTFSapling());
		blocks.register("moonworm", "Moonworm", new BlockTFMoonworm());
		blocks.register("magic_log", "MagicLog", new BlockTFMagicLog());
		blocks.register("magic_leaves", "MagicLeaves", new BlockTFMagicLeaves());
		blocks.register("magic_log_core", "MagicLogSpecial", new BlockTFMagicLogSpecial());
		blocks.register("tower_wood", "TowerStone", new BlockTFTowerWood());
		blocks.register("tower_device", "TowerDevice", new BlockTFTowerDevice());
		blocks.register("tower_translucent", "TowerTranslucent", new BlockTFTowerTranslucent());
		blocks.register("trophy", "Trophy", new BlockTFTrophy());
		blocks.register("stronghold_shield", "Shield", new BlockTFShield());
		blocks.register("trophy_pedestal", "TrophyPedestal", new BlockTFTrophyPedestal());
		blocks.register("aurora_block", "AuroraBrick", new BlockTFAuroraBrick());
		blocks.register("underbrick", "UnderBrick", new BlockTFUnderBrick());
		blocks.register("thorns", "Thorns", new BlockTFThorns());
		blocks.register("burnt_thorns", "BurntThorns", new BlockTFBurntThorns());
		blocks.register("thorn_rose", "ThornRose", new BlockTFThornRose());
		blocks.register("twilight_leaves_3", "Leaves3", new BlockTFLeaves3());
		blocks.register("deadrock", "Deadrock", new BlockTFDeadrock());
		blocks.register("dark_leaves", "DarkLeaves", new BlockTFDarkLeaves());
		blocks.register("aurora_pillar", "AuroraPillar", new BlockTFPillar(Material.PACKED_ICE).setHardness(2.0F).setResistance(10.0F));
		blocks.register("aurora_slab", "AuroraSlab", new BlockTFAuroraSlab(false));
		blocks.register("double_aurora_slab", "AuroraDoubleSlab", new BlockTFAuroraSlab(true));
		blocks.register("trollsteinn", "TrollSteinn", new BlockTFTrollSteinn());
		blocks.register("wispy_cloud", "WispyCloud", new BlockTFWispyCloud());
		blocks.register("fluffy_cloud", "FluffyCloud", new BlockTFFluffyCloud());
		blocks.register("giant_cobblestone", "GiantCobble", new BlockTFGiantCobble());
		blocks.register("giant_log", "GiantLog", new BlockTFGiantLog());
		blocks.register("giant_leaves", "GiantLeaves", new BlockTFGiantLeaves());
		blocks.register("giant_obsidian", "GiantObsidian", new BlockTFGiantObsidian());
		blocks.register("uberous_soil", "UberousSoil", new BlockTFUberousSoil());
		blocks.register("huge_stalk", "HugeStalk", new BlockTFHugeStalk());
		blocks.register("huge_mushgloom", "HugeGloomBlock", new BlockTFHugeGloomBlock());
		blocks.register("trollvidr", "TrollVidr", new BlockTFTrollRoot());
		blocks.register("unripe_trollber", "UnripeTrollBer", new BlockTFUnripeTorchCluster());
		blocks.register("trollber", "TrollBer", new BlockTFRipeTorchCluster());
		blocks.register("knightmetal_block", "KnightmetalBlock", new BlockTFKnightmetalBlock());
		blocks.register("huge_lilypad", "HugeLilyPad", new BlockTFHugeLilyPad());
		blocks.register("huge_waterlily", "HugeWaterLily", new BlockTFHugeWaterLily());
		blocks.register("slider", "Slider", new BlockTFSlider());
		Block castleBrick = new BlockTFCastleBlock();
		IBlockState castleState = castleBrick.getDefaultState();
		blocks.register("castle_brick", "CastleBrick", castleBrick);
		blocks.register("castle_stairs_brick", "CastleStairsBrick", new BlockTFStairs(castleState));
		blocks.register("castle_stairs_worn", "CastleStairsWorn", new BlockTFStairs(castleState.withProperty(BlockTFCastleBlock.VARIANT, CastleBrickVariant.WORN)));
		blocks.register("castle_stairs_cracked", "CastleStairsCracked", new BlockTFStairs(castleState.withProperty(BlockTFCastleBlock.VARIANT, CastleBrickVariant.CRACKED)));
		blocks.register("castle_stairs_mossy", "CastleStairsMossy", new BlockTFStairs(castleState.withProperty(BlockTFCastleBlock.VARIANT, CastleBrickVariant.MOSSY)));
		Block castlePillar = new BlockTFCastlePillar();
		blocks.register("castle_pillar", "CastlePillar", castlePillar);
		blocks.register("castle_stairs", "CastleStairs", new BlockTFCastleStairs(castlePillar.getDefaultState()));
		blocks.register("castle_rune_brick", "CastleMagic", new BlockTFCastleMagic());
		blocks.register("force_field", "ForceField", new BlockTFForceField());
		blocks.register("cinder_furnace", "CinderFurnaceIdle", new BlockTFCinderFurnace(false));
		blocks.register("cinder_furnace_lit", "CinderFurnaceLit", new BlockTFCinderFurnace(true));
		blocks.register("cinder_log", "CinderLog", new BlockTFCinderLog());
		blocks.register("castle_door", "CastleDoor", new BlockTFCastleDoor(false));
		blocks.register("castle_door_vanished", "CastleDoorVanished", new BlockTFCastleDoor(true));
		blocks.register("castle_unlock", "CastleUnlock", new BlockTFCastleUnlock());
		blocks.register("experiment_115", "experiment115", new BlockTFExperiment115().setHardness(0.5F));
		blocks.register("miniature_structure", "MiniatureStructure", new BlockTFMiniatureStructure().setHardness(0.75F));
		blocks.register("block_storage", "BlockOfStorage", new BlockTFCompressed());
		blocks.register("lapis_block", "BlockOfLapisTF", new BlockTFLapisBlock());
		blocks.register("spiral_bricks", "SpiralBricks", new BlockTFSpiralBrick());
		Block etchedNagastone = new BlockTFNagastoneEtched().setHardness(1.5F).setResistance(10.0F);
		blocks.register("etched_nagastone", "EtchedNagastone", etchedNagastone);
		blocks.register("nagastone_stairs", "NagastoneStairs", new BlockTFNagastoneStairs(etchedNagastone.getDefaultState()).setHardness(1.5F).setResistance(10.0F));
		blocks.register("nagastone_pillar", "NagastonePillar", new BlockTFNagastonePillar().setHardness(1.5F).setResistance(10.0F));
		Block etchedNagastoneMossy = new BlockTFNagastoneEtched().setHardness(1.5F).setResistance(10.0F);
		blocks.register("etched_nagastone_mossy", "EtchedNagastoneMossy", etchedNagastoneMossy);
		blocks.register("nagastone_stairs_mossy", "NagastoneStairsMossy", new BlockTFNagastoneStairs(etchedNagastoneMossy.getDefaultState()).setHardness(1.5F).setResistance(10.0F));
		blocks.register("nagastone_pillar_mossy", "NagastonePillarMossy", new BlockTFNagastonePillar().setHardness(1.5F).setResistance(10.0F));
		Block etchedNagastoneWeathered = new BlockTFNagastoneEtched().setHardness(1.5F).setResistance(10.0F);
		blocks.register("etched_nagastone_weathered", "EtchedNagastoneWeathered", etchedNagastoneWeathered);
		blocks.register("nagastone_stairs_weathered", "NagastoneStairsWeathered", new BlockTFNagastoneStairs(etchedNagastoneWeathered.getDefaultState()).setHardness(1.5F).setResistance(10.0F));
		blocks.register("nagastone_pillar_weathered", "NagastonePillarWeathered", new BlockTFNagastonePillar().setHardness(1.5F).setResistance(10.0F));
		blocks.register("auroralized_glass", "AuroralizedGlass", new BlockTFAuroralizedGlass());
		blocks.register("iron_ladder", "IronLadder", new BlockTFLadderBars().setSoundType(SoundType.METAL).setHardness(5.0F).setResistance(10.0F));

		registerWoodVariants(blocks, BlockTFLog.VARIANT, WoodVariant.values());
		registerWoodVariants(blocks, BlockTFMagicLog.VARIANT, MagicWoodVariant.values());

		blocks.register("terrorcotta_circle", "TerrorCottaCircle", new BlockTFHorizontal(Material.ROCK, MapColor.SAND).setSoundType(SoundType.STONE).setHardness(1.7F));
		blocks.register("terrorcotta_diagonal", "TerrorCottaDiagonal", new BlockTFDiagonal(Material.ROCK, MapColor.SAND).setSoundType(SoundType.STONE).setHardness(1.7F));
		blocks.register("stone_twist", "StonePillar", new BlockTFPillar(Material.ROCK).setHardness(1.5F).setResistance(10.0F));
		blocks.register("stone_twist_thin", "StonePillarThin", new BlockTFWallPillar(Material.ROCK, 12, 16).setHardness(1.5F).setResistance(10.0F));

		registerFluidBlock(blocks, moltenFiery);
		registerFluidBlock(blocks, moltenKnightmetal);
		registerFluidBlock("fiery_essence", blocks, essenceFiery);
	}

	private static <T extends Enum<T> & IStringSerializable & IMapColorSupplier> void registerWoodVariants(BlockRegistryHelper blocks, final IProperty<T> key, final T[] types) {
		for (T woodType : types) {
			String woodName = woodType.getName();
			String woodNameCapitalized;

			if ("oak".equals(woodName)) { // Not really going to rename that enum entry just yet
				woodName = "twilight_oak";
				woodNameCapitalized = "TwilightOak";
			} else {
				woodNameCapitalized = woodName.substring(0, 1).toUpperCase() + woodName.substring(1);
			}

			// Not going to initialize these two over 60 times
			final String inventory = "inventory";
			final IProperty[] noProperty = new IProperty[0];

			// Make special property key that allows ONLY one value because slabs are special level of stupid
			final PropertyEnum<T> restrictedKey = PropertyEnum.create("variant", key.getValueClass(), input -> input == woodType);

			Block planks = blocks.register(woodName + "_planks", woodNameCapitalized + "Planks", new BlockTF(Material.WOOD, woodType.supplyMapColor()) { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, noProperty); }}).setSoundType(SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
			blocks.register(woodName + "_stairs"    , woodNameCapitalized + "Stairs"  , new BlockTFStairs(planks.getDefaultState()) { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, noProperty); }}); // No hardness/soundType, that's derived from IBlockState in
			Block singleSlab = blocks.register(woodName + "_slab", woodNameCapitalized + "Slab", new BlockTFSlab<T>(Material.WOOD, woodType.supplyMapColor(), woodType) { @Override public boolean isDouble() { return false; } @Override protected Block getSingle() { return this; } @Override public IProperty<T> getVariantProperty() { return restrictedKey; } @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, restrictedKey); }}).setSoundType(SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
			blocks.register(woodName + "_doubleslab", woodNameCapitalized + "Slab"    , new BlockTFSlab<T>(Material.WOOD, woodType.supplyMapColor(), woodType) { @Override public boolean isDouble() { return true; } @Override protected Block getSingle() { return singleSlab; } @Override public IProperty<T> getVariantProperty() { return restrictedKey; } @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, restrictedKey); }}).setSoundType(SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
			blocks.register(woodName + "_button"    , woodNameCapitalized + "Button"  , new BlockTFButtonWood() { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, noProperty); }}).setSoundType(SoundType.WOOD).setHardness(0.5F);
			blocks.register(woodName + "_door"      , woodNameCapitalized + "Door"    , new BlockTFDoor(Material.WOOD, woodType.supplyMapColor(), new ResourceLocation(TwilightForestMod.ID, woodName + "_door")) { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, BlockTFDoor.POWERED); } { setSoundType(SoundType.WOOD); }}).setHardness(3.0F);
			blocks.register(woodName + "_trapdoor"  , woodNameCapitalized + "TrapDoor", new BlockTFTrapDoor(Material.WOOD, woodType.supplyMapColor()) { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, noProperty); }}).setSoundType(SoundType.WOOD).setHardness(3.0F);
			blocks.register(woodName + "_fence"     , woodNameCapitalized + "Fence"   , new BlockTFFence(Material.WOOD, woodType.supplyMapColor()) { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, noProperty); }}).setSoundType(SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
			blocks.register(woodName + "_gate"      , woodNameCapitalized + "Gate"    , new BlockTFFenceGate(woodType.supplyPlankColor()) { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, BlockFenceGate.POWERED); }}).setSoundType(SoundType.WOOD).setHardness(2.0F).setResistance(5.0F);
			blocks.register(woodName + "_plate"     , woodNameCapitalized + "Plate"   , new BlockTFPressurePlate(Material.WOOD, woodType.supplyMapColor(), BlockPressurePlate.Sensitivity.EVERYTHING) { @SideOnly(Side.CLIENT) @Override public void registerModel() { ModelUtils.registerIncludingItemModels(this, inventory, noProperty); }}).setSoundType(SoundType.WOOD).setHardness(0.5F);

			// TODO chests? boats?
		}
	}

	public static List<ModelRegisterCallback> getBlockModels() {
		return Collections.unmodifiableList(BlockRegistryHelper.blockModels);
	}

	private static class BlockRegistryHelper {

		static final List<ModelRegisterCallback> blockModels = new ArrayList<>();

		private final IForgeRegistry<Block> registry;

		BlockRegistryHelper(IForgeRegistry<Block> registry) {
			this.registry = registry;
		}

		<T extends Block> T register(String registryName, String translationKey, T block) {
			block.setTranslationKey(TwilightForestMod.ID + "." + translationKey);
			register(registryName, block);
			return block;
		}

		<T extends Block> T register(String registryName, T block) {
			block.setRegistryName(TwilightForestMod.ID, registryName);
			if (block instanceof ModelRegisterCallback) {
				blockModels.add((ModelRegisterCallback) block);
			}
			block.setCreativeTab(TFItems.creativeTab);
			registry.register(block);
			return block;
		}
	}

	// our internal fluid instances
	public static final Fluid moltenFiery;
	public static final Fluid moltenKnightmetal;
	public static final Fluid essenceFiery;

	public static final ResourceLocation moltenFieryStill = TwilightForestMod.prefix("blocks/molten_fiery_still");
	public static final ResourceLocation moltenFieryFlow  = TwilightForestMod.prefix("blocks/molten_fiery_flow");

	public static final ResourceLocation moltenKnightmetalStill = TwilightForestMod.prefix("blocks/molten_knightmetal_still");
	public static final ResourceLocation moltenKnightmetalFlow  = TwilightForestMod.prefix("blocks/molten_knightmetal_flow");

	public static final ResourceLocation essenceFieryStill = TwilightForestMod.prefix("blocks/fluid_fiery_still");
	public static final ResourceLocation essenceFieryFlow  = TwilightForestMod.prefix("blocks/fluid_fiery_flow");

	static {
		moltenFiery       = registerFluid(new Fluid("fierymetal" , moltenFieryStill, moltenFieryFlow).setTemperature(1000).setLuminosity(15));
		moltenKnightmetal = registerFluid(new Fluid("knightmetal", moltenKnightmetalStill, moltenKnightmetalFlow).setTemperature(1000).setLuminosity(15));
		essenceFiery      = registerFluid(new Fluid("fiery_essence", essenceFieryStill, essenceFieryFlow).setTemperature(1000));
	}

	private static Fluid registerFluid(Fluid fluid) {
		fluid.setUnlocalizedName(fluid.getName());
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	private static void registerFluidBlock(BlockRegistryHelper blocks, Fluid fluid) {
		registerFluidBlock("molten_" + fluid.getName(), blocks, fluid);
	}

	private static void registerFluidBlock(String registryName, BlockRegistryHelper blocks, Fluid fluid) {
		Block block = new BlockTFFluid(fluid, Material.LAVA).setTranslationKey(TwilightForestMod.ID + "." + fluid.getName()).setLightLevel(1.0F);
		blocks.register(registryName, block);
	}
}
