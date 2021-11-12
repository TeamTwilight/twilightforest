package twilightforest.block;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.Item;
import net.minecraft.core.Direction;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.compat.TFCompat;
import twilightforest.enums.*;
import twilightforest.item.TFItems;
import twilightforest.world.components.feature.trees.growers.*;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

@SuppressWarnings({"WeakerAccess", "unused", "deprecation"})
@Nonnull
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TwilightForestMod.ID);

	public static final RegistryObject<TFPortalBlock> TWILIGHT_PORTAL = BLOCKS.register("twilight_portal", () -> new TFPortalBlock(BlockBehaviour.Properties.of(Material.PORTAL).strength(-1.0F).sound(SoundType.GLASS).lightLevel((state) -> 11).noCollission().noOcclusion().noDrops()));

	//misc.
	public static final RegistryObject<Block> HEDGE = BLOCKS.register("hedge", () -> new HedgeBlock(BlockBehaviour.Properties.of(Material.CACTUS).strength(2.0F, 10.0F).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> FIREFLY_JAR = BLOCKS.register("firefly_jar", () -> new JarBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F, 0.0F).sound(SoundType.WOOD).lightLevel((state) -> 15).noOcclusion()));
	public static final RegistryObject<Block> FIREFLY_SPAWNER = BLOCKS.register("firefly_particle_spawner", () -> new FireflySpawnerBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(1.0F, 0.0F).sound(SoundType.GLASS).lightLevel((state) -> 15).noOcclusion()));
	public static final RegistryObject<Block> CICADA_JAR = BLOCKS.register("cicada_jar", () -> new JarBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F, 0.0F).sound(SoundType.WOOD).noOcclusion().randomTicks()));
	public static final RegistryObject<Block> MOSS_PATCH = BLOCKS.register("moss_patch", () -> new PatchBlock(PlantVariant.MOSSPATCH, BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.MOSS).noCollission().noOcclusion()));
	public static final RegistryObject<Block> MAYAPPLE = BLOCKS.register("mayapple", () -> new TFPlantBlock(PlantVariant.MAYAPPLE, BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().noOcclusion()));
	public static final RegistryObject<Block> CLOVER_PATCH = BLOCKS.register("clover_patch", () -> new PatchBlock(PlantVariant.CLOVERPATCH, BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().noOcclusion()));
	public static final RegistryObject<Block> FIDDLEHEAD = BLOCKS.register("fiddlehead", () -> new TFPlantBlock(PlantVariant.FIDDLEHEAD, BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().noOcclusion()));
	public static final RegistryObject<Block> MUSHGLOOM = BLOCKS.register("mushgloom", () -> new TFPlantBlock(PlantVariant.MUSHGLOOM, BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.FUNGUS).noCollission().noOcclusion().lightLevel((state) -> 3)));
	public static final RegistryObject<Block> TORCHBERRY_PLANT = BLOCKS.register("torchberry_plant", () -> new TorchberryPlantBlock(PlantVariant.TORCHBERRY, BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.HANGING_ROOTS).noCollission().noOcclusion().lightLevel((state) -> (state.getValue(TorchberryPlantBlock.HAS_BERRIES) ? 8 : 0))));
	public static final RegistryObject<Block> ROOT_STRAND = BLOCKS.register("root_strand", () -> new TFPlantBlock(PlantVariant.ROOT_STRAND, BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.HANGING_ROOTS).noCollission().noOcclusion()));
	public static final RegistryObject<Block> FALLEN_LEAVES = BLOCKS.register("fallen_leaves", () -> new TFPlantBlock(PlantVariant.FALLEN_LEAVES, BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).strength(0.0F).sound(SoundType.AZALEA_LEAVES).noCollission().noOcclusion()));
	public static final RegistryObject<Block> ROOT_BLOCK = BLOCKS.register("root", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIVEROOT_BLOCK = BLOCKS.register("liveroot_block", () -> new LiverootBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> UNCRAFTING_TABLE = BLOCKS.register("uncrafting_table", UncraftingTableBlock::new);
	public static final RegistryObject<Block> SMOKER = BLOCKS.register("smoker", () -> new TFSmokerBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.GRASS).strength(1.5F, 0.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> ENCASED_SMOKER = BLOCKS.register("encased_smoker", () -> new EncasedSmokerBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).requiresCorrectToolForDrops().strength(1.5F, 0.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FIRE_JET = BLOCKS.register("fire_jet", () -> new FireJetBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.GRASS).strength(1.5F, 0.0F).sound(SoundType.WOOD).randomTicks().lightLevel((state) -> state.getValue(FireJetBlock.STATE) != FireJetVariant.FLAME ? 0 : 15)));
	public static final RegistryObject<Block> ENCASED_FIRE_JET = BLOCKS.register("encased_fire_jet", () -> new EncasedFireJetBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).requiresCorrectToolForDrops().strength(1.5F, 0.0F).sound(SoundType.WOOD).lightLevel((state) -> 15)));
	public static final RegistryObject<Block> FIREFLY = BLOCKS.register("firefly", () -> new FireflyBlock(BlockBehaviour.Properties.of(new Material.Builder(MaterialColor.GRASS).noCollider().nonSolid().build()).lightLevel((state) -> 15).sound(SoundType.SLIME_BLOCK).strength(0.0F).noCollission()));
	public static final RegistryObject<Block> CICADA = BLOCKS.register("cicada", () -> new CicadaBlock(BlockBehaviour.Properties.of(new Material.Builder(MaterialColor.GRASS).noCollider().nonSolid().build()).sound(SoundType.SLIME_BLOCK).strength(0.0F).noCollission()));
	public static final RegistryObject<Block> MOONWORM = BLOCKS.register("moonworm", () -> new MoonwormBlock(BlockBehaviour.Properties.of(new Material.Builder(MaterialColor.GRASS).noCollider().nonSolid().build()).lightLevel((state) -> 14).sound(SoundType.SLIME_BLOCK).strength(0.0F).noCollission()));
	public static final RegistryObject<HugeLilyPadBlock> HUGE_LILY_PAD = BLOCKS.register("huge_lily_pad", () -> new HugeLilyPadBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> HUGE_WATER_LILY = BLOCKS.register("huge_water_lily", () -> new HugeWaterLilyBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)));
	public static final RegistryObject<RotatedPillarBlock> SLIDER = BLOCKS.register("slider", SliderBlock::new);
	public static final RegistryObject<Block> IRON_LADDER = BLOCKS.register("iron_ladder", () -> new IronLadderBlock(BlockBehaviour.Properties.of(Material.DECORATION).requiresCorrectToolForDrops().strength(5.0F, 10.0F).sound(SoundType.METAL).noOcclusion()));

	//naga courtyard
	public static final RegistryObject<Block> NAGASTONE_HEAD = BLOCKS.register("nagastone_head", () -> new TFHorizontalBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> NAGASTONE = BLOCKS.register("nagastone", () -> new NagastoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> SPIRAL_BRICKS = BLOCKS.register("spiral_bricks", SpiralBrickBlock::new);
	public static final RegistryObject<Block> ETCHED_NAGASTONE = BLOCKS.register("etched_nagastone", () -> new EtchedNagastoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> NAGASTONE_PILLAR = BLOCKS.register("nagastone_pillar", NagastonePillarBlock::new);
	public static final RegistryObject<StairBlock> NAGASTONE_STAIRS_LEFT = BLOCKS.register("nagastone_stairs_left", () -> new StairBlock(ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(ETCHED_NAGASTONE.get())));
	public static final RegistryObject<StairBlock> NAGASTONE_STAIRS_RIGHT = BLOCKS.register("nagastone_stairs_right", () -> new StairBlock(ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(ETCHED_NAGASTONE.get())));
	public static final RegistryObject<Block> MOSSY_ETCHED_NAGASTONE = BLOCKS.register("mossy_etched_nagastone", () -> new EtchedNagastoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> MOSSY_NAGASTONE_PILLAR = BLOCKS.register("mossy_nagastone_pillar", NagastonePillarBlock::new);
	public static final RegistryObject<StairBlock> MOSSY_NAGASTONE_STAIRS_LEFT = BLOCKS.register("mossy_nagastone_stairs_left", () -> new StairBlock(MOSSY_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(MOSSY_ETCHED_NAGASTONE.get())));
	public static final RegistryObject<StairBlock> MOSSY_NAGASTONE_STAIRS_RIGHT = BLOCKS.register("mossy_nagastone_stairs_right", () -> new StairBlock(MOSSY_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(MOSSY_ETCHED_NAGASTONE.get())));
	public static final RegistryObject<Block> CRACKED_ETCHED_NAGASTONE = BLOCKS.register("cracked_etched_nagastone", () -> new EtchedNagastoneBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> CRACKED_NAGASTONE_PILLAR = BLOCKS.register("cracked_nagastone_pillar", NagastonePillarBlock::new);
	public static final RegistryObject<StairBlock> CRACKED_NAGASTONE_STAIRS_LEFT = BLOCKS.register("cracked_nagastone_stairs_left", () -> new StairBlock(CRACKED_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRACKED_ETCHED_NAGASTONE.get())));
	public static final RegistryObject<StairBlock> CRACKED_NAGASTONE_STAIRS_RIGHT = BLOCKS.register("cracked_nagastone_stairs_right", () -> new StairBlock(CRACKED_ETCHED_NAGASTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRACKED_ETCHED_NAGASTONE.get())));

	//lich tower
	//public static final RegistryObject<Block> TERRORCOTTA_CIRCLE = BLOCKS.register("terrorcotta_circle", () -> new BlockTFHorizontal(Block.Properties.create(Material.ROCK, MaterialColor.SAND).setRequiresTool().hardnessAndResistance(1.7F).sound(SoundType.STONE)));
	//public static final RegistryObject<Block> TERRORCOTTA_DIAGONAL = BLOCKS.register("terrorcotta_diagonal", () -> new BlockTFDiagonal(Block.Properties.create(Material.ROCK, MaterialColor.SAND).setRequiresTool().hardnessAndResistance(1.7F).sound(SoundType.STONE)));
	public static final RegistryObject<RotatedPillarBlock> TWISTED_STONE = BLOCKS.register("twisted_stone", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> TWISTED_STONE_PILLAR = BLOCKS.register("twisted_stone_pillar", () -> new WallPillarBlock(Material.STONE, 12, 16));
	//public static final RegistryObject<Block> LAPIS_BLOCK = BLOCKS.register("lapis_block", () -> new Block(Block.Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(3.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<KeepsakeCasketBlock> KEEPSAKE_CASKET = BLOCKS.register("keepsake_casket", KeepsakeCasketBlock::new);
	public static final RegistryObject<RotatedPillarBlock> BOLD_STONE_PILLAR = BLOCKS.register("bold_stone_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> DEATH_TOME_SPAWNER = BLOCKS.register("death_tome_spawner", () -> new TomeSpawnerBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> EMPTY_CANOPY_BOOKSHELF = BLOCKS.register("empty_canopy_bookshelf", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<CandelabraBlock> CANDELABRA = BLOCKS.register("candelabra", () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.STONE).lightLevel(state -> state.getValue(AbstractLightableBlock.LIGHTING) == AbstractLightableBlock.Lighting.NORMAL ? 15 : state.getValue(AbstractLightableBlock.LIGHTING) == AbstractLightableBlock.Lighting.OMINOUS ? 7 : 0)));
	public static final RegistryObject<AbstractSkullCandleBlock> ZOMBIE_SKULL_CANDLE = BLOCKS.register("zombie_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.ZOMBIE, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> ZOMBIE_WALL_SKULL_CANDLE = BLOCKS.register("zombie_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.ZOMBIE, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> SKELETON_SKULL_CANDLE = BLOCKS.register("skeleton_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.SKELETON, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> SKELETON_WALL_SKULL_CANDLE = BLOCKS.register("skeleton_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.SKELETON, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> WITHER_SKELE_SKULL_CANDLE = BLOCKS.register("wither_skeleton_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> WITHER_SKELE_WALL_SKULL_CANDLE = BLOCKS.register("wither_skeleton_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> CREEPER_SKULL_CANDLE = BLOCKS.register("creeper_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.CREEPER, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> CREEPER_WALL_SKULL_CANDLE = BLOCKS.register("creeper_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.CREEPER, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> PLAYER_SKULL_CANDLE = BLOCKS.register("player_skull_candle", () -> new SkullCandleBlock(SkullBlock.Types.PLAYER, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));
	public static final RegistryObject<AbstractSkullCandleBlock> PLAYER_WALL_SKULL_CANDLE = BLOCKS.register("player_wall_skull_candle", () -> new WallSkullCandleBlock(SkullBlock.Types.PLAYER, BlockBehaviour.Properties.copy(Blocks.ZOMBIE_HEAD)));

	//labyrinth
	public static final RegistryObject<Block> MAZESTONE = BLOCKS.register("mazestone", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> MAZESTONE_BRICK = BLOCKS.register("mazestone_brick", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> CUT_MAZESTONE = BLOCKS.register("cut_mazestone", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> DECORATIVE_MAZESTONE = BLOCKS.register("decorative_mazestone", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> CRACKED_MAZESTONE = BLOCKS.register("cracked_mazestone", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> MOSSY_MAZESTONE = BLOCKS.register("mossy_mazestone", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> MAZESTONE_MOSAIC = BLOCKS.register("mazestone_mosaic", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> MAZESTONE_BORDER = BLOCKS.register("mazestone_border", () -> new MazestoneBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 5.0F).sound(SoundType.STONE)));

	//stronghold
	public static final RegistryObject<Block> STRONGHOLD_SHIELD = BLOCKS.register("stronghold_shield", () -> new StrongholdShieldBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(-1.0F, 6000000.0F).sound(SoundType.METAL).noDrops()));
	public static final RegistryObject<Block> TROPHY_PEDESTAL = BLOCKS.register("trophy_pedestal", () -> new TrophyPedestalBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 2000.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> UNDERBRICK = BLOCKS.register("underbrick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WOOD).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> MOSSY_UNDERBRICK = BLOCKS.register("mossy_underbrick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WOOD).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> CRACKED_UNDERBRICK = BLOCKS.register("cracked_underbrick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WOOD).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> UNDERBRICK_FLOOR = BLOCKS.register("underbrick_floor", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.WOOD).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE)));

	//dark tower
	public static final RegistryObject<Block> TOWERWOOD = BLOCKS.register("towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> ENCASED_TOWERWOOD = BLOCKS.register("encased_towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).strength(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> CRACKED_TOWERWOOD = BLOCKS.register("cracked_towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MOSSY_TOWERWOOD = BLOCKS.register("mossy_towerwood", () -> new FlammableBlock(1, 0, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> INFESTED_TOWERWOOD = BLOCKS.register("infested_towerwood", () -> new InfestedTowerwoodBlock(1, 0, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(0.75F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> REAPPEARING_BLOCK = BLOCKS.register("reappearing_block", () -> new ReappearingBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).requiresCorrectToolForDrops().strength(10.0F, 35.0F).sound(SoundType.WOOD).lightLevel((state) -> 4)));
	// FIXME Split Vanishing block into regular breakable variant, and then the unbreakable variant
	public static final RegistryObject<Block> VANISHING_BLOCK = BLOCKS.register("vanishing_block", () -> new VanishingBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).strength(-1.0F, 35.0F).sound(SoundType.WOOD).lightLevel((state) -> state.getValue(VanishingBlock.ACTIVE) ? 4 : 0)));
	public static final RegistryObject<Block> LOCKED_VANISHING_BLOCK = BLOCKS.register("locked_vanishing_block", () -> new LockedVanishingBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).strength(-1.0F, 35.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> CARMINITE_BUILDER = BLOCKS.register("carminite_builder", () -> new BuilderBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).requiresCorrectToolForDrops().strength(10.0F, 35.0F).sound(SoundType.WOOD).lightLevel((state) -> state.getValue(BuilderBlock.STATE) == TowerDeviceVariant.BUILDER_ACTIVE ? 4 : 0)));
	public static final RegistryObject<Block> BUILT_BLOCK = BLOCKS.register("built_block", () -> new TranslucentBuiltBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(50.0F, 2000.0F).sound(SoundType.METAL).noOcclusion().noDrops()));
	public static final RegistryObject<Block> ANTIBUILDER = BLOCKS.register("antibuilder", () -> new AntibuilderBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).requiresCorrectToolForDrops().strength(10.0F, 35.0F).sound(SoundType.WOOD).lightLevel((state) -> 10)));
	public static final RegistryObject<Block> ANTIBUILT_BLOCK = BLOCKS.register("antibuilt_block", () -> new Block(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F, 2000.0F).sound(SoundType.METAL).noDrops().noOcclusion()));
	public static final RegistryObject<GhastTrapBlock> GHAST_TRAP = BLOCKS.register("ghast_trap", () -> new GhastTrapBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).requiresCorrectToolForDrops().strength(10.0F, 35.0F).sound(SoundType.WOOD).lightLevel((state) -> state.getValue(GhastTrapBlock.ACTIVE) ? 15 : 0)));
	public static final RegistryObject<Block> CARMINITE_REACTOR = BLOCKS.register("carminite_reactor", () -> new CarminiteReactorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).requiresCorrectToolForDrops().strength(10.0F, 35.0F).sound(SoundType.WOOD).lightLevel((state) -> state.getValue(CarminiteReactorBlock.ACTIVE) ? 15 : 0)));
	public static final RegistryObject<Block> REACTOR_DEBRIS = BLOCKS.register("reactor_debris", () -> new Block(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F, 2000.0F).sound(SoundType.METAL).noDrops().noOcclusion()));
	public static final RegistryObject<Block> FAKE_GOLD = BLOCKS.register("fake_gold", () -> new Block(BlockBehaviour.Properties.of(Material.GLASS).strength(50.0F, 2000.0F).sound(SoundType.METAL).noDrops()));
	public static final RegistryObject<Block> FAKE_DIAMOND = BLOCKS.register("fake_diamond", () -> new Block(BlockBehaviour.Properties.of(Material.GLASS).strength(50.0F, 2000.0F).sound(SoundType.METAL).noDrops()));
	public static final RegistryObject<Block> EXPERIMENT_115 = BLOCKS.register("experiment_115", Experiment115Block::new);

	//aurora palace
	public static final RegistryObject<Block> AURORA_BLOCK = BLOCKS.register("aurora_block", () -> new AuroraBrickBlock(BlockBehaviour.Properties.of(Material.ICE_SOLID).strength(10.0F, 10.0F)));
	public static final RegistryObject<RotatedPillarBlock> AURORA_PILLAR = BLOCKS.register("aurora_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.ICE_SOLID).requiresCorrectToolForDrops().strength(2.0F, 10.0F)));
	public static final RegistryObject<Block> AURORA_SLAB = BLOCKS.register("aurora_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.ICE_SOLID).requiresCorrectToolForDrops().strength(2.0F, 10.0F)));
	public static final RegistryObject<Block> AURORALIZED_GLASS = BLOCKS.register("auroralized_glass", () -> new AuroralizedGlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion()));

	//highlands/thornlands
	public static final RegistryObject<Block> BROWN_THORNS = BLOCKS.register("brown_thorns", () -> new ThornsBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(50.0F, 2000.0F).sound(SoundType.WOOD).noDrops()));
	public static final RegistryObject<Block> GREEN_THORNS = BLOCKS.register("green_thorns", () -> new ThornsBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PLANT).strength(50.0F, 2000.0F).sound(SoundType.WOOD).noDrops()));
	public static final RegistryObject<Block> BURNT_THORNS = BLOCKS.register("burnt_thorns", () -> new BurntThornsBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.STONE).strength(0.01F, 0.0F).sound(SoundType.SAND).noDrops()));
	public static final RegistryObject<Block> THORN_ROSE = BLOCKS.register("thorn_rose", () -> new ThornRoseBlock(BlockBehaviour.Properties.of(Material.PLANT).strength(10.0F, 0.0F).sound(SoundType.GRASS).noCollission()));
	public static final RegistryObject<Block> THORN_LEAVES = BLOCKS.register("thorn_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> BEANSTALK_LEAVES = BLOCKS.register("beanstalk_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> DEADROCK = BLOCKS.register("deadrock", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 6000000.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> CRACKED_DEADROCK = BLOCKS.register("cracked_deadrock", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 6000000.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> WEATHERED_DEADROCK = BLOCKS.register("weathered_deadrock", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(100.0F, 6000000.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> TROLLSTEINN = BLOCKS.register("trollsteinn", () -> new TrollsteinnBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(2.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> WISPY_CLOUD = BLOCKS.register("wispy_cloud", () -> new HalfTransparentBlock(BlockBehaviour.Properties.of(Material.TOP_SNOW).strength(0.3F).sound(SoundType.WOOL).noOcclusion()));
	public static final RegistryObject<Block> FLUFFY_CLOUD = BLOCKS.register("fluffy_cloud", () -> new Block(BlockBehaviour.Properties.of(Material.ICE_SOLID).strength(0.8F, 0.0F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> GIANT_COBBLESTONE = BLOCKS.register("giant_cobblestone", () -> new GiantBlock(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE).requiresCorrectToolForDrops().strength(128, 10)));
	public static final RegistryObject<Block> GIANT_LOG = BLOCKS.register("giant_log", () -> new GiantLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).requiresCorrectToolForDrops().strength(128, 0)));
	public static final RegistryObject<Block> GIANT_LEAVES = BLOCKS.register("giant_leaves", () -> new GiantLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).requiresCorrectToolForDrops().strength(0.2F * 64F, 0.0F).sound(SoundType.AZALEA_LEAVES).noOcclusion()));
	public static final RegistryObject<Block> GIANT_OBSIDIAN = BLOCKS.register("giant_obsidian", () -> new GiantBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).requiresCorrectToolForDrops().strength(50.0F * 64F * 64F, 2000.0F * 64F * 64F)));
	public static final RegistryObject<Block> UBEROUS_SOIL = BLOCKS.register("uberous_soil", () -> new UberousSoilBlock(BlockBehaviour.Properties.of(Material.DIRT).strength(0.6F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<RotatedPillarBlock> HUGE_STALK = BLOCKS.register("huge_stalk", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PLANT).strength(1.25F, 7.0F).sound(SoundType.STEM)));
	public static final RegistryObject<Block> HUGE_MUSHGLOOM = BLOCKS.register("huge_mushgloom", () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(0.2F).sound(SoundType.WOOD).lightLevel((state) -> 5)));
	public static final RegistryObject<Block> HUGE_MUSHGLOOM_STEM = BLOCKS.register("huge_mushgloom_stem", () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(0.2F).sound(SoundType.WOOD).lightLevel((state) -> 5)));
	public static final RegistryObject<Block> TROLLVIDR = BLOCKS.register("trollvidr", () -> new TrollRootBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS).noCollission()));
	public static final RegistryObject<Block> UNRIPE_TROLLBER = BLOCKS.register("unripe_trollber", () -> new UnripeTorchClusterBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<Block> TROLLBER = BLOCKS.register("trollber", () -> new TrollRootBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS).noCollission().lightLevel((state) -> 15)));

	//plateau castle
	public static final RegistryObject<Block> CASTLE_BRICK = BLOCKS.register("castle_brick", () -> new CastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> WORN_CASTLE_BRICK = BLOCKS.register("worn_castle_brick", () -> new CastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> CRACKED_CASTLE_BRICK = BLOCKS.register("cracked_castle_brick", () -> new CastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> CASTLE_ROOF_TILE = BLOCKS.register("castle_roof_tile", () -> new CastleBlock(MaterialColor.COLOR_GRAY));
	public static final RegistryObject<Block> MOSSY_CASTLE_BRICK = BLOCKS.register("mossy_castle_brick", () -> new CastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> THICK_CASTLE_BRICK = BLOCKS.register("thick_castle_brick", () -> new CastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> ENCASED_CASTLE_BRICK_PILLAR = BLOCKS.register("encased_castle_brick_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> ENCASED_CASTLE_BRICK_TILE = BLOCKS.register("encased_castle_brick_tile", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> BOLD_CASTLE_BRICK_PILLAR = BLOCKS.register("bold_castle_brick_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> BOLD_CASTLE_BRICK_TILE = BLOCKS.register("bold_castle_brick_tile", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<StairBlock> CASTLE_BRICK_STAIRS = BLOCKS.register("castle_brick_stairs", () -> new StairBlock(CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> WORN_CASTLE_BRICK_STAIRS = BLOCKS.register("worn_castle_brick_stairs", () -> new StairBlock(WORN_CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(WORN_CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> CRACKED_CASTLE_BRICK_STAIRS = BLOCKS.register("cracked_castle_brick_stairs", () -> new StairBlock(CRACKED_CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRACKED_CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> MOSSY_CASTLE_BRICK_STAIRS = BLOCKS.register("mossy_castle_brick_stairs", () -> new StairBlock(MOSSY_CASTLE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.copy(MOSSY_CASTLE_BRICK.get())));
	public static final RegistryObject<StairBlock> ENCASED_CASTLE_BRICK_STAIRS = BLOCKS.register("encased_castle_brick_stairs", () -> new StairBlock(ENCASED_CASTLE_BRICK_PILLAR.get().defaultBlockState(), BlockBehaviour.Properties.copy(ENCASED_CASTLE_BRICK_PILLAR.get())));
	public static final RegistryObject<StairBlock> BOLD_CASTLE_BRICK_STAIRS = BLOCKS.register("bold_castle_brick_stairs", () -> new StairBlock(BOLD_CASTLE_BRICK_PILLAR.get().defaultBlockState(), BlockBehaviour.Properties.copy(BOLD_CASTLE_BRICK_PILLAR.get())));
	public static final RegistryObject<Block> PINK_CASTLE_RUNE_BRICK = BLOCKS.register("pink_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> BLUE_CASTLE_RUNE_BRICK = BLOCKS.register("blue_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> YELLOW_CASTLE_RUNE_BRICK = BLOCKS.register("yellow_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> VIOLET_CASTLE_RUNE_BRICK = BLOCKS.register("violet_castle_rune_brick", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> VIOLET_FORCE_FIELD = BLOCKS.register("violet_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of(Material.BARRIER).strength(-1.0F).lightLevel((state) -> 2).noDrops().noOcclusion()));
	public static final RegistryObject<Block> PINK_FORCE_FIELD = BLOCKS.register("pink_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of(Material.BARRIER).strength(-1.0F).lightLevel((state) -> 2).noDrops().noOcclusion()));
	public static final RegistryObject<Block> ORANGE_FORCE_FIELD = BLOCKS.register("orange_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of(Material.BARRIER).strength(-1.0F).lightLevel((state) -> 2).noDrops().noOcclusion()));
	public static final RegistryObject<Block> GREEN_FORCE_FIELD = BLOCKS.register("green_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of(Material.BARRIER).strength(-1.0F).lightLevel((state) -> 2).noDrops().noOcclusion()));
	public static final RegistryObject<Block> BLUE_FORCE_FIELD = BLOCKS.register("blue_force_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of(Material.BARRIER).strength(-1.0F).lightLevel((state) -> 2).noDrops().noOcclusion()));
	public static final RegistryObject<Block> CINDER_FURNACE = BLOCKS.register("cinder_furnace", CinderFurnaceBlock::new);
	public static final RegistryObject<RotatedPillarBlock> CINDER_LOG = BLOCKS.register("cinder_log", () -> new TFLogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(1.0F)));
	public static final RegistryObject<Block> CINDER_WOOD = BLOCKS.register("cinder_wood", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(1.0F)));
	public static final RegistryObject<Block> YELLOW_CASTLE_DOOR = BLOCKS.register("yellow_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of(Material.STONE, (state) -> state.getValue(CastleDoorBlock.VANISHED) ? MaterialColor.NONE : MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(100.0F, 35.0F)));
	public static final RegistryObject<Block> VIOLET_CASTLE_DOOR = BLOCKS.register("violet_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of(Material.STONE, (state) -> state.getValue(CastleDoorBlock.VANISHED) ? MaterialColor.NONE : MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(100.0F, 35.0F)));
	public static final RegistryObject<Block> PINK_CASTLE_DOOR = BLOCKS.register("pink_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of(Material.STONE, (state) -> state.getValue(CastleDoorBlock.VANISHED) ? MaterialColor.NONE : MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(100.0F, 35.0F)));
	public static final RegistryObject<Block> BLUE_CASTLE_DOOR = BLOCKS.register("blue_castle_door", () -> new CastleDoorBlock(BlockBehaviour.Properties.of(Material.STONE, (state) -> state.getValue(CastleDoorBlock.VANISHED) ? MaterialColor.NONE : MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(100.0F, 35.0F)));

	//mini structures
	public static final RegistryObject<Block> TWILIGHT_PORTAL_MINIATURE_STRUCTURE = BLOCKS.register("twilight_portal_miniature_structure", MiniatureStructureBlock::new);
//	public static final RegistryObject<Block> HEDGE_MAZE_MINIATURE_STRUCTURE = BLOCKS.register("hedge_maze_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> HOLLOW_HILL_MINIATURE_STRUCTURE = BLOCKS.register("hollow_hill_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> QUEST_GROVE_MINIATURE_STRUCTURE = BLOCKS.register("quest_grove_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> MUSHROOM_TOWER_MINIATURE_STRUCTURE = BLOCKS.register("mushroom_tower_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> NAGA_COURTYARD_MINIATURE_STRUCTURE = BLOCKS.register("naga_courtyard_miniature_structure", MiniatureStructureBlock::new);
	public static final RegistryObject<Block> LICH_TOWER_MINIATURE_STRUCTURE = BLOCKS.register("lich_tower_miniature_structure", MiniatureStructureBlock::new);
//	public static final RegistryObject<Block> MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE = BLOCKS.register("minotaur_labyrinth_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> HYDRA_LAIR_MINIATURE_STRUCTURE = BLOCKS.register("hydra_lair_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE = BLOCKS.register("goblin_stronghold_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> DARK_TOWER_MINIATURE_STRUCTURE = BLOCKS.register("dark_tower_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> YETI_CAVE_MINIATURE_STRUCTURE = BLOCKS.register("yeti_cave_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> AURORA_PALACE_MINIATURE_STRUCTURE = BLOCKS.register("aurora_palace_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE = BLOCKS.register("troll_cave_cottage_miniature_structure", () -> new BlockTFMiniatureStructure());
//	public static final RegistryObject<Block> FINAL_CASTLE_MINIATURE_STRUCTURE = BLOCKS.register("final_castle_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> KNIGHTMETAL_BLOCK = BLOCKS.register("knightmetal_block", () -> new KnightmetalBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 41.0F).sound(SoundType.NETHERITE_BLOCK)));
	public static final RegistryObject<Block> IRONWOOD_BLOCK = BLOCKS.register("ironwood_block", () -> new CompressedBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(5.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FIERY_BLOCK = BLOCKS.register("fiery_block", () -> new CompressedBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(5.0F, 10.0F).sound(SoundType.METAL).noOcclusion().emissiveRendering((state, world, pos) -> true)));
	public static final RegistryObject<Block> STEELEAF_BLOCK = BLOCKS.register("steeleaf_block", () -> new CompressedBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.PLANT).strength(5.0F, 10.0F).sound(SoundType.MOSS)));
	public static final RegistryObject<Block> ARCTIC_FUR_BLOCK = BLOCKS.register("arctic_fur_block", () -> new CompressedBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.WOOL).strength(0.8F, 10.0F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> CARMINITE_BLOCK = BLOCKS.register("carminite_block", () -> new CompressedBlock(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_RED).strength(0.0F, 10.0F).sound(SoundType.SLIME_BLOCK)));

	//boss trophies and spawners
	public static final RegistryObject<Block> NAGA_BOSS_SPAWNER = BLOCKS.register("naga_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.NAGA));
	public static final RegistryObject<Block> LICH_BOSS_SPAWNER = BLOCKS.register("lich_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.LICH));
	public static final RegistryObject<Block> HYDRA_BOSS_SPAWNER = BLOCKS.register("hydra_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.HYDRA));
	public static final RegistryObject<Block> UR_GHAST_BOSS_SPAWNER = BLOCKS.register("ur_ghast_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.UR_GHAST));
	public static final RegistryObject<Block> KNIGHT_PHANTOM_BOSS_SPAWNER = BLOCKS.register("knight_phantom_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.KNIGHT_PHANTOM));
	public static final RegistryObject<Block> SNOW_QUEEN_BOSS_SPAWNER = BLOCKS.register("snow_queen_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.SNOW_QUEEN));
	public static final RegistryObject<Block> MINOSHROOM_BOSS_SPAWNER = BLOCKS.register("minoshroom_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.MINOSHROOM));
	public static final RegistryObject<Block> ALPHA_YETI_BOSS_SPAWNER = BLOCKS.register("alpha_yeti_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.ALPHA_YETI));
	public static final RegistryObject<Block> FINAL_BOSS_BOSS_SPAWNER = BLOCKS.register("final_boss_boss_spawner", () -> new BossSpawnerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1.0F).noOcclusion().noDrops(), BossVariant.FINAL_BOSS));
	public static final RegistryObject<TrophyBlock> NAGA_TROPHY = BLOCKS.register("naga_trophy", () -> new TrophyBlock(BossVariant.NAGA, 5));
	public static final RegistryObject<TrophyBlock> LICH_TROPHY = BLOCKS.register("lich_trophy", () -> new TrophyBlock(BossVariant.LICH, 6));
	public static final RegistryObject<TrophyBlock> HYDRA_TROPHY = BLOCKS.register("hydra_trophy", () -> new TrophyBlock(BossVariant.HYDRA, 12));
	public static final RegistryObject<TrophyBlock> UR_GHAST_TROPHY = BLOCKS.register("ur_ghast_trophy", () -> new TrophyBlock(BossVariant.UR_GHAST, 13));
	public static final RegistryObject<TrophyBlock> KNIGHT_PHANTOM_TROPHY = BLOCKS.register("knight_phantom_trophy", () -> new TrophyBlock(BossVariant.KNIGHT_PHANTOM, 8));
	public static final RegistryObject<TrophyBlock> SNOW_QUEEN_TROPHY = BLOCKS.register("snow_queen_trophy", () -> new TrophyBlock(BossVariant.SNOW_QUEEN, 14));
	public static final RegistryObject<TrophyBlock> MINOSHROOM_TROPHY = BLOCKS.register("minoshroom_trophy", () -> new TrophyBlock(BossVariant.MINOSHROOM, 7));
	public static final RegistryObject<TrophyBlock> ALPHA_YETI_TROPHY = BLOCKS.register("alpha_yeti_trophy", () -> new TrophyBlock(BossVariant.ALPHA_YETI, 9));
	public static final RegistryObject<TrophyBlock> QUEST_RAM_TROPHY = BLOCKS.register("quest_ram_trophy", () -> new TrophyBlock(BossVariant.QUEST_RAM, 1));
	public static final RegistryObject<TrophyWallBlock> NAGA_WALL_TROPHY = BLOCKS.register("naga_wall_trophy", () -> new TrophyWallBlock(BossVariant.NAGA));
	public static final RegistryObject<TrophyWallBlock> LICH_WALL_TROPHY = BLOCKS.register("lich_wall_trophy", () -> new TrophyWallBlock(BossVariant.LICH));
	public static final RegistryObject<TrophyWallBlock> HYDRA_WALL_TROPHY = BLOCKS.register("hydra_wall_trophy", () -> new TrophyWallBlock(BossVariant.HYDRA));
	public static final RegistryObject<TrophyWallBlock> UR_GHAST_WALL_TROPHY = BLOCKS.register("ur_ghast_wall_trophy", () -> new TrophyWallBlock(BossVariant.UR_GHAST));
	public static final RegistryObject<TrophyWallBlock> KNIGHT_PHANTOM_WALL_TROPHY = BLOCKS.register("knight_phantom_wall_trophy", () -> new TrophyWallBlock(BossVariant.KNIGHT_PHANTOM));
	public static final RegistryObject<TrophyWallBlock> SNOW_QUEEN_WALL_TROPHY = BLOCKS.register("snow_queen_wall_trophy", () -> new TrophyWallBlock(BossVariant.SNOW_QUEEN));
	public static final RegistryObject<TrophyWallBlock> MINOSHROOM_WALL_TROPHY = BLOCKS.register("minoshroom_wall_trophy", () -> new TrophyWallBlock(BossVariant.MINOSHROOM));
	public static final RegistryObject<TrophyWallBlock> ALPHA_YETI_WALL_TROPHY = BLOCKS.register("alpha_yeti_wall_trophy", () -> new TrophyWallBlock(BossVariant.ALPHA_YETI));
	public static final RegistryObject<TrophyWallBlock> QUEST_RAM_WALL_TROPHY = BLOCKS.register("quest_ram_wall_trophy", () -> new TrophyWallBlock(BossVariant.QUEST_RAM));

	// TODO Enumify all of the dang tree stuff

	//all tree related stuff
	public static final RegistryObject<BanisterBlock> OAK_BANISTER = BLOCKS.register("oak_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<BanisterBlock> SPRUCE_BANISTER = BLOCKS.register("spruce_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<BanisterBlock> BIRCH_BANISTER = BLOCKS.register("birch_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<BanisterBlock> JUNGLE_BANISTER = BLOCKS.register("jungle_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<BanisterBlock> ACACIA_BANISTER = BLOCKS.register("acacia_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<BanisterBlock> DARK_OAK_BANISTER = BLOCKS.register("dark_oak_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<BanisterBlock> CRIMSON_BANISTER = BLOCKS.register("crimson_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<BanisterBlock> WARPED_BANISTER = BLOCKS.register("warped_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));

	public static final WoodType TWILIGHT_OAK = WoodType.create("twilight_oak");
	public static final WoodType CANOPY = WoodType.create("canopy");
	public static final WoodType MANGROVE = WoodType.create("mangrove");
	public static final WoodType DARKWOOD = WoodType.create("darkwood");
	public static final WoodType TIMEWOOD = WoodType.create("timewood");
	public static final WoodType TRANSFORMATION = WoodType.create("transformation");
	public static final WoodType MINING = WoodType.create("mining");
	public static final WoodType SORTING = WoodType.create("sorting");

	public static final BlockBehaviour.Properties TWILIGHT_OAK_LOG_PROPS = logProperties(MaterialColor.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_LOG_PROPS = logProperties(MaterialColor.PODZOL, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_LOG_PROPS = logProperties(MaterialColor.DIRT, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_LOG_PROPS = logProperties(MaterialColor.COLOR_BROWN, MaterialColor.STONE).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_LOG_PROPS = logProperties(MaterialColor.DIRT, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_LOG_PROPS = logProperties(MaterialColor.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_LOG_PROPS = logProperties(MaterialColor.SAND, MaterialColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_LOG_PROPS = logProperties(MaterialColor.PODZOL, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);

	public static final BlockBehaviour.Properties TWILIGHT_OAK_BARK_PROPS = logProperties(MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_BARK_PROPS = logProperties(MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_BARK_PROPS = logProperties(MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_BARK_PROPS = logProperties(MaterialColor.STONE).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_BARK_PROPS = logProperties(MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_BARK_PROPS = logProperties(MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_BARK_PROPS = logProperties(MaterialColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_BARK_PROPS = logProperties(MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);

	public static final BlockBehaviour.Properties TWILIGHT_OAK_STRIPPED_PROPS = logProperties(MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_STRIPPED_PROPS = logProperties(MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_STRIPPED_PROPS = logProperties(MaterialColor.DIRT).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_STRIPPED_PROPS = logProperties(MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_STRIPPED_PROPS = logProperties(MaterialColor.DIRT).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_STRIPPED_PROPS = logProperties(MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_STRIPPED_PROPS = logProperties(MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_STRIPPED_PROPS = logProperties(MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);

	public static final RegistryObject<RotatedPillarBlock> TWILIGHT_OAK_LOG = BLOCKS.register("twilight_oak_log", () -> new TFLogBlock(TWILIGHT_OAK_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> CANOPY_LOG = BLOCKS.register("canopy_log", () -> new TFLogBlock(CANOPY_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MANGROVE_LOG = BLOCKS.register("mangrove_log", () -> new TFLogBlock(MANGROVE_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> DARK_LOG = BLOCKS.register("dark_log", () -> new TFLogBlock(DARK_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TIME_LOG = BLOCKS.register("time_log", () -> new TFLogBlock(TIME_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TRANSFORMATION_LOG = BLOCKS.register("transformation_log", () -> new TFLogBlock(TRANSFORMATION_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MINING_LOG = BLOCKS.register("mining_log", () -> new TFLogBlock(MINING_LOG_PROPS));
	public static final RegistryObject<RotatedPillarBlock> SORTING_LOG = BLOCKS.register("sorting_log", () -> new TFLogBlock(SORTING_LOG_PROPS));

	public static final RegistryObject<HollowLogHorizontal> HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL = BLOCKS.register("hollow_twilight_oak_log_horizontal", () -> new HollowLogHorizontal(TWILIGHT_OAK_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_CANOPY_LOG_HORIZONTAL = BLOCKS.register("hollow_canopy_log_horizontal", () -> new HollowLogHorizontal(CANOPY_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_MANGROVE_LOG_HORIZONTAL = BLOCKS.register("hollow_mangrove_log_horizontal", () -> new HollowLogHorizontal(MANGROVE_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_DARK_LOG_HORIZONTAL = BLOCKS.register("hollow_dark_log_horizontal", () -> new HollowLogHorizontal(DARK_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_TIME_LOG_HORIZONTAL = BLOCKS.register("hollow_time_log_horizontal", () -> new HollowLogHorizontal(TIME_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_TRANSFORMATION_LOG_HORIZONTAL = BLOCKS.register("hollow_transformation_log_horizontal", () -> new HollowLogHorizontal(TRANSFORMATION_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_MINING_LOG_HORIZONTAL = BLOCKS.register("hollow_mining_log_horizontal", () -> new HollowLogHorizontal(MINING_BARK_PROPS));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_SORTING_LOG_HORIZONTAL = BLOCKS.register("hollow_sorting_log_horizontal", () -> new HollowLogHorizontal(SORTING_BARK_PROPS));

	public static final RegistryObject<HollowLogVertical> HOLLOW_TWILIGHT_OAK_LOG_VERTICAL = BLOCKS.register("hollow_twilight_oak_log_vertical", () -> new HollowLogVertical(TWILIGHT_OAK_STRIPPED_PROPS, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_CANOPY_LOG_VERTICAL = BLOCKS.register("hollow_canopy_log_vertical", () -> new HollowLogVertical(CANOPY_STRIPPED_PROPS, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_MANGROVE_LOG_VERTICAL = BLOCKS.register("hollow_mangrove_log_vertical", () -> new HollowLogVertical(MANGROVE_STRIPPED_PROPS, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_DARK_LOG_VERTICAL = BLOCKS.register("hollow_dark_log_vertical", () -> new HollowLogVertical(DARK_STRIPPED_PROPS, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_TIME_LOG_VERTICAL = BLOCKS.register("hollow_time_log_vertical", () -> new HollowLogVertical(TIME_STRIPPED_PROPS, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_TRANSFORMATION_LOG_VERTICAL = BLOCKS.register("hollow_transformation_log_vertical", () -> new HollowLogVertical(TRANSFORMATION_STRIPPED_PROPS, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_MINING_LOG_VERTICAL = BLOCKS.register("hollow_mining_log_vertical", () -> new HollowLogVertical(MINING_STRIPPED_PROPS, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_SORTING_LOG_VERTICAL = BLOCKS.register("hollow_sorting_log_vertical", () -> new HollowLogVertical(SORTING_STRIPPED_PROPS, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE));

	public static final RegistryObject<HollowLogClimbable> HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE = BLOCKS.register("hollow_twilight_oak_log_climbable", () -> new HollowLogClimbable(TWILIGHT_OAK_STRIPPED_PROPS, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_CANOPY_LOG_CLIMBABLE = BLOCKS.register("hollow_canopy_log_climbable", () -> new HollowLogClimbable(CANOPY_STRIPPED_PROPS, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_MANGROVE_LOG_CLIMBABLE = BLOCKS.register("hollow_mangrove_log_climbable", () -> new HollowLogClimbable(MANGROVE_STRIPPED_PROPS, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_DARK_LOG_CLIMBABLE = BLOCKS.register("hollow_dark_log_climbable", () -> new HollowLogClimbable(DARK_STRIPPED_PROPS, TFBlocks.HOLLOW_DARK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_TIME_LOG_CLIMBABLE = BLOCKS.register("hollow_time_log_climbable", () -> new HollowLogClimbable(TIME_STRIPPED_PROPS, TFBlocks.HOLLOW_TIME_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_TRANSFORMATION_LOG_CLIMBABLE = BLOCKS.register("hollow_transformation_log_climbable", () -> new HollowLogClimbable(TRANSFORMATION_STRIPPED_PROPS, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_MINING_LOG_CLIMBABLE = BLOCKS.register("hollow_mining_log_climbable", () -> new HollowLogClimbable(MINING_STRIPPED_PROPS, TFBlocks.HOLLOW_MINING_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_SORTING_LOG_CLIMBABLE = BLOCKS.register("hollow_sorting_log_climbable", () -> new HollowLogClimbable(SORTING_STRIPPED_PROPS, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL));

	public static final RegistryObject<HollowLogHorizontal> HOLLOW_OAK_LOG_HORIZONTAL = BLOCKS.register("hollow_oak_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_SPRUCE_LOG_HORIZONTAL = BLOCKS.register("hollow_spruce_log_horizontal", () -> new HollowLogHorizontal((BlockBehaviour.Properties.copy(Blocks.SPRUCE_WOOD))));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_BIRCH_LOG_HORIZONTAL = BLOCKS.register("hollow_birch_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.BIRCH_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_JUNGLE_LOG_HORIZONTAL = BLOCKS.register("hollow_jungle_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.JUNGLE_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_ACACIA_LOG_HORIZONTAL = BLOCKS.register("hollow_acacia_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.ACACIA_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_DARK_OAK_LOG_HORIZONTAL = BLOCKS.register("hollow_dark_oak_log_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_WOOD)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_CRIMSON_STEM_HORIZONTAL = BLOCKS.register("hollow_crimson_stem_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.CRIMSON_HYPHAE)));
	public static final RegistryObject<HollowLogHorizontal> HOLLOW_WARPED_STEM_HORIZONTAL = BLOCKS.register("hollow_warped_stem_horizontal", () -> new HollowLogHorizontal(BlockBehaviour.Properties.copy(Blocks.WARPED_HYPHAE)));

	public static final RegistryObject<HollowLogVertical> HOLLOW_OAK_LOG_VERTICAL = BLOCKS.register("hollow_oak_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD), TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_SPRUCE_LOG_VERTICAL = BLOCKS.register("hollow_spruce_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_WOOD), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_BIRCH_LOG_VERTICAL = BLOCKS.register("hollow_birch_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_WOOD), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_JUNGLE_LOG_VERTICAL = BLOCKS.register("hollow_jungle_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_JUNGLE_WOOD), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_ACACIA_LOG_VERTICAL = BLOCKS.register("hollow_acacia_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_ACACIA_WOOD), TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_DARK_OAK_LOG_VERTICAL = BLOCKS.register("hollow_dark_oak_log_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_DARK_OAK_WOOD), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_CRIMSON_STEM_VERTICAL = BLOCKS.register("hollow_crimson_stem_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CRIMSON_HYPHAE), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE));
	public static final RegistryObject<HollowLogVertical> HOLLOW_WARPED_STEM_VERTICAL = BLOCKS.register("hollow_warped_stem_vertical", () -> new HollowLogVertical(BlockBehaviour.Properties.copy(Blocks.STRIPPED_WARPED_HYPHAE), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE));

	public static final RegistryObject<HollowLogClimbable> HOLLOW_OAK_LOG_CLIMBABLE = BLOCKS.register("hollow_oak_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD), TFBlocks.HOLLOW_OAK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_SPRUCE_LOG_CLIMBABLE = BLOCKS.register("hollow_spruce_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_SPRUCE_WOOD), TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_BIRCH_LOG_CLIMBABLE = BLOCKS.register("hollow_birch_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_BIRCH_WOOD), TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_JUNGLE_LOG_CLIMBABLE = BLOCKS.register("hollow_jungle_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_JUNGLE_WOOD), TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_ACACIA_LOG_CLIMBABLE = BLOCKS.register("hollow_acacia_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_ACACIA_WOOD), TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_DARK_OAK_LOG_CLIMBABLE = BLOCKS.register("hollow_dark_oak_log_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_DARK_OAK_WOOD), TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_CRIMSON_STEM_CLIMBABLE = BLOCKS.register("hollow_crimson_stem_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CRIMSON_HYPHAE), TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL));
	public static final RegistryObject<HollowLogClimbable> HOLLOW_WARPED_STEM_CLIMBABLE = BLOCKS.register("hollow_warped_stem_climbable", () -> new HollowLogClimbable(BlockBehaviour.Properties.copy(Blocks.STRIPPED_WARPED_HYPHAE), TFBlocks.HOLLOW_WARPED_STEM_VERTICAL));

	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TWILIGHT_OAK_LOG = BLOCKS.register("stripped_twilight_oak_log", () -> new TFLogBlock(TWILIGHT_OAK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CANOPY_LOG = BLOCKS.register("stripped_canopy_log", () -> new TFLogBlock(CANOPY_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MANGROVE_LOG = BLOCKS.register("stripped_mangrove_log", () -> new TFLogBlock(MANGROVE_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_DARK_LOG = BLOCKS.register("stripped_dark_log", () -> new TFLogBlock(DARK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TIME_LOG = BLOCKS.register("stripped_time_log", () -> new TFLogBlock(TIME_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TRANSFORMATION_LOG = BLOCKS.register("stripped_transformation_log", () -> new TFLogBlock(TRANSFORMATION_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MINING_LOG = BLOCKS.register("stripped_mining_log", () -> new TFLogBlock(MINING_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_SORTING_LOG = BLOCKS.register("stripped_sorting_log", () -> new TFLogBlock(SORTING_STRIPPED_PROPS));

	public static final RegistryObject<RotatedPillarBlock> TWILIGHT_OAK_WOOD = BLOCKS.register("twilight_oak_wood", () -> new TFLogBlock(TWILIGHT_OAK_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> CANOPY_WOOD = BLOCKS.register("canopy_wood", () -> new TFLogBlock(CANOPY_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MANGROVE_WOOD = BLOCKS.register("mangrove_wood", () -> new TFLogBlock(MANGROVE_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> DARK_WOOD = BLOCKS.register("dark_wood", () -> new TFLogBlock(DARK_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TIME_WOOD = BLOCKS.register("time_wood", () -> new TFLogBlock(TIME_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> TRANSFORMATION_WOOD = BLOCKS.register("transformation_wood", () -> new TFLogBlock(TRANSFORMATION_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> MINING_WOOD = BLOCKS.register("mining_wood", () -> new TFLogBlock(MINING_BARK_PROPS));
	public static final RegistryObject<RotatedPillarBlock> SORTING_WOOD = BLOCKS.register("sorting_wood", () -> new TFLogBlock(SORTING_BARK_PROPS));

	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TWILIGHT_OAK_WOOD = BLOCKS.register("stripped_twilight_oak_wood", () -> new TFLogBlock(TWILIGHT_OAK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_CANOPY_WOOD = BLOCKS.register("stripped_canopy_wood", () -> new TFLogBlock(CANOPY_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MANGROVE_WOOD = BLOCKS.register("stripped_mangrove_wood", () -> new TFLogBlock(MANGROVE_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_DARK_WOOD = BLOCKS.register("stripped_dark_wood", () -> new TFLogBlock(DARK_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TIME_WOOD = BLOCKS.register("stripped_time_wood", () -> new TFLogBlock(TIME_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_TRANSFORMATION_WOOD = BLOCKS.register("stripped_transformation_wood", () -> new TFLogBlock(TRANSFORMATION_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_MINING_WOOD = BLOCKS.register("stripped_mining_wood", () -> new TFLogBlock(MINING_STRIPPED_PROPS));
	public static final RegistryObject<RotatedPillarBlock> STRIPPED_SORTING_WOOD = BLOCKS.register("stripped_sorting_wood", () -> new TFLogBlock(SORTING_STRIPPED_PROPS));

	public static final RegistryObject<Block> TIME_LOG_CORE = BLOCKS.register("time_log_core", () -> new SpecialMagicLogBlock(TIME_LOG_PROPS, MagicWoodVariant.TIME));
	public static final RegistryObject<Block> TRANSFORMATION_LOG_CORE = BLOCKS.register("transformation_log_core", () -> new SpecialMagicLogBlock(TRANSFORMATION_LOG_PROPS, MagicWoodVariant.TRANS));
	public static final RegistryObject<Block> MINING_LOG_CORE = BLOCKS.register("mining_log_core", () -> new SpecialMagicLogBlock(MINING_LOG_PROPS, MagicWoodVariant.MINE));
	public static final RegistryObject<Block> SORTING_LOG_CORE = BLOCKS.register("sorting_log_core", () -> new SpecialMagicLogBlock(SORTING_LOG_PROPS, MagicWoodVariant.SORT));

	public static final RegistryObject<Block> MANGROVE_ROOT = BLOCKS.register("mangrove_root", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));

	public static final RegistryObject<Block> TWILIGHT_OAK_LEAVES = BLOCKS.register("twilight_oak_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> CANOPY_LEAVES = BLOCKS.register("canopy_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> MANGROVE_LEAVES = BLOCKS.register("mangrove_leaves", () -> new MangroveLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> DARK_LEAVES = BLOCKS.register("dark_leaves", () -> new DarkLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> HARDENED_DARK_LEAVES = BLOCKS.register("hardened_dark_leaves", () -> new HardenedDarkLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> RAINBOW_OAK_LEAVES = BLOCKS.register("rainbow_oak_leaves", () -> new TFLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES)));
	public static final RegistryObject<Block> TIME_LEAVES = BLOCKS.register("time_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion()));
	public static final RegistryObject<Block> TRANSFORMATION_LEAVES = BLOCKS.register("transformation_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion()));
	public static final RegistryObject<Block> MINING_LEAVES = BLOCKS.register("mining_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion()));
	public static final RegistryObject<Block> SORTING_LEAVES = BLOCKS.register("sorting_leaves", () -> new MagicLeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion()));

	public static final RegistryObject<SaplingBlock> TWILIGHT_OAK_SAPLING = BLOCKS.register("twilight_oak_sapling", () -> new SaplingBlock(new SmallOakTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> CANOPY_SAPLING = BLOCKS.register("canopy_sapling", () -> new SaplingBlock(new CanopyTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> MANGROVE_SAPLING = BLOCKS.register("mangrove_sapling", () -> new MangroveSaplingBlock(new MangroveTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> DARKWOOD_SAPLING = BLOCKS.register("darkwood_sapling", () -> new SaplingBlock(new DarkCanopyTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> HOLLOW_OAK_SAPLING = BLOCKS.register("hollow_oak_sapling", () -> new SaplingBlock(new HollowTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> TIME_SAPLING = BLOCKS.register("time_sapling", () -> new SaplingBlock(new TimeTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> TRANSFORMATION_SAPLING = BLOCKS.register("transformation_sapling", () -> new SaplingBlock(new TransformationTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> MINING_SAPLING = BLOCKS.register("mining_sapling", () -> new SaplingBlock(new MinersTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> SORTING_SAPLING = BLOCKS.register("sorting_sapling", () -> new SaplingBlock(new SortingTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));
	public static final RegistryObject<SaplingBlock> RAINBOW_OAK_SAPLING = BLOCKS.register("rainbow_oak_sapling", () -> new SaplingBlock(new RainboakTree(), BlockBehaviour.Properties.of(Material.PLANT).strength(0.0F).sound(SoundType.GRASS).noCollission().randomTicks()));

	// TODO chests? boats?
	public static final RegistryObject<Block> TWILIGHT_OAK_PLANKS = BLOCKS.register("twilight_oak_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> TWILIGHT_OAK_STAIRS = BLOCKS.register("twilight_oak_stairs", () -> new StairBlock(TWILIGHT_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> TWILIGHT_OAK_SLAB = BLOCKS.register("twilight_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> TWILIGHT_OAK_BUTTON = BLOCKS.register("twilight_oak_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TWILIGHT_OAK_FENCE = BLOCKS.register("twilight_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> TWILIGHT_OAK_GATE = BLOCKS.register("twilight_oak_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> TWILIGHT_OAK_PLATE = BLOCKS.register("twilight_oak_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> TWILIGHT_OAK_DOOR = BLOCKS.register("twilight_oak_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> TWILIGHT_OAK_TRAPDOOR = BLOCKS.register("twilight_oak_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> TWILIGHT_OAK_SIGN = BLOCKS.register("twilight_oak_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), TWILIGHT_OAK));
	public static final RegistryObject<WallSignBlock> TWILIGHT_WALL_SIGN = BLOCKS.register("twilight_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), TWILIGHT_OAK));
	public static final RegistryObject<BanisterBlock> TWILIGHT_OAK_BANISTER = BLOCKS.register("twilight_oak_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(TWILIGHT_OAK_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_PLANKS = BLOCKS.register("canopy_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> CANOPY_STAIRS = BLOCKS.register("canopy_stairs", () -> new StairBlock(CANOPY_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_SLAB = BLOCKS.register("canopy_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_BUTTON = BLOCKS.register("canopy_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> CANOPY_FENCE = BLOCKS.register("canopy_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_GATE = BLOCKS.register("canopy_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> CANOPY_PLATE = BLOCKS.register("canopy_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> CANOPY_DOOR = BLOCKS.register("canopy_door", () -> new DoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> CANOPY_TRAPDOOR = BLOCKS.register("canopy_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> CANOPY_SIGN = BLOCKS.register("canopy_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), CANOPY));
	public static final RegistryObject<WallSignBlock> CANOPY_WALL_SIGN = BLOCKS.register("canopy_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), CANOPY));
	public static final RegistryObject<Block> CANOPY_BOOKSHELF = BLOCKS.register("canopy_bookshelf", () -> new BookshelfBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(1.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<BanisterBlock> CANOPY_BANISTER = BLOCKS.register("canopy_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(CANOPY_PLANKS.get())));
	public static final RegistryObject<Block> MANGROVE_PLANKS = BLOCKS.register("mangrove_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.DIRT).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> MANGROVE_STAIRS = BLOCKS.register("mangrove_stairs", () -> new StairBlock(MANGROVE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));
	public static final RegistryObject<Block> MANGROVE_SLAB = BLOCKS.register("mangrove_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MANGROVE_BUTTON = BLOCKS.register("mangrove_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MANGROVE_FENCE = BLOCKS.register("mangrove_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));
	public static final RegistryObject<Block> MANGROVE_GATE = BLOCKS.register("mangrove_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));
	public static final RegistryObject<Block> MANGROVE_PLATE = BLOCKS.register("mangrove_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> MANGROVE_DOOR = BLOCKS.register("mangrove_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> MANGROVE_TRAPDOOR = BLOCKS.register("mangrove_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> MANGROVE_SIGN = BLOCKS.register("mangrove_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), MANGROVE));
	public static final RegistryObject<WallSignBlock> MANGROVE_WALL_SIGN = BLOCKS.register("mangrove_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), MANGROVE));
	public static final RegistryObject<BanisterBlock> MANGROVE_BANISTER = BLOCKS.register("mangrove_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(MANGROVE_PLANKS.get())));
	public static final RegistryObject<Block> DARK_PLANKS = BLOCKS.register("dark_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_ORANGE).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> DARK_STAIRS = BLOCKS.register("dark_stairs", () -> new StairBlock(DARK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(DARK_PLANKS.get())));
	public static final RegistryObject<Block> DARK_SLAB = BLOCKS.register("dark_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DARK_BUTTON = BLOCKS.register("dark_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DARK_FENCE = BLOCKS.register("dark_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get())));
	public static final RegistryObject<Block> DARK_GATE = BLOCKS.register("dark_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get())));
	public static final RegistryObject<Block> DARK_PLATE = BLOCKS.register("dark_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(DARK_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> DARK_DOOR = BLOCKS.register("dark_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> DARK_TRAPDOOR = BLOCKS.register("dark_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> DARKWOOD_SIGN = BLOCKS.register("darkwood_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), DARKWOOD));
	public static final RegistryObject<WallSignBlock> DARKWOOD_WALL_SIGN = BLOCKS.register("darkwood_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), DARKWOOD));
	public static final RegistryObject<BanisterBlock> DARKWOOD_BANISTER = BLOCKS.register("darkwood_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(DARK_PLANKS.get())));
	public static final RegistryObject<Block> TIME_PLANKS = BLOCKS.register("time_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.DIRT).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> TIME_STAIRS = BLOCKS.register("time_stairs", () -> new StairBlock(TIME_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(TIME_PLANKS.get())));
	public static final RegistryObject<Block> TIME_SLAB = BLOCKS.register("time_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_BUTTON = BLOCKS.register("time_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_FENCE = BLOCKS.register("time_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get())));
	public static final RegistryObject<Block> TIME_GATE = BLOCKS.register("time_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get())));
	public static final RegistryObject<Block> TIME_PLATE = BLOCKS.register("time_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(TIME_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> TIME_DOOR = BLOCKS.register("time_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> TIME_TRAPDOOR = BLOCKS.register("time_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> TIME_SIGN = BLOCKS.register("time_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), TIMEWOOD));
	public static final RegistryObject<WallSignBlock> TIME_WALL_SIGN = BLOCKS.register("time_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), TIMEWOOD));
	public static final RegistryObject<BanisterBlock> TIME_BANISTER = BLOCKS.register("time_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(TIME_PLANKS.get())));
	public static final RegistryObject<Block> TRANSFORMATION_PLANKS = BLOCKS.register("transformation_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> TRANSFORMATION_STAIRS = BLOCKS.register("transformation_stairs", () -> new StairBlock(TRANSFORMATION_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));
	public static final RegistryObject<Block> TRANSFORMATION_SLAB = BLOCKS.register("transformation_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRANSFORMATION_BUTTON = BLOCKS.register("transformation_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TRANSFORMATION_FENCE = BLOCKS.register("transformation_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));
	public static final RegistryObject<Block> TRANSFORMATION_GATE = BLOCKS.register("transformation_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));
	public static final RegistryObject<Block> TRANSFORMATION_PLATE = BLOCKS.register("transformation_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> TRANSFORMATION_DOOR = BLOCKS.register("transformation_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> TRANSFORMATION_TRAPDOOR = BLOCKS.register("transformation_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> TRANSFORMATION_SIGN = BLOCKS.register("transformation_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), TRANSFORMATION));
	public static final RegistryObject<WallSignBlock> TRANSFORMATION_WALL_SIGN = BLOCKS.register("transformation_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), TRANSFORMATION));
	public static final RegistryObject<BanisterBlock> TRANSFORMATION_BANISTER = BLOCKS.register("transformation_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(TRANSFORMATION_PLANKS.get())));
	public static final RegistryObject<Block> MINING_PLANKS = BLOCKS.register("mining_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> MINING_STAIRS = BLOCKS.register("mining_stairs", () -> new StairBlock(MINING_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(MINING_PLANKS.get())));
	public static final RegistryObject<Block> MINING_SLAB = BLOCKS.register("mining_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MINING_BUTTON = BLOCKS.register("mining_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MINING_FENCE = BLOCKS.register("mining_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get())));
	public static final RegistryObject<Block> MINING_GATE = BLOCKS.register("mining_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get())));
	public static final RegistryObject<Block> MINING_PLATE = BLOCKS.register("mining_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(MINING_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> MINING_DOOR = BLOCKS.register("mining_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> MINING_TRAPDOOR = BLOCKS.register("mining_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> MINING_SIGN = BLOCKS.register("mining_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), MINING));
	public static final RegistryObject<WallSignBlock> MINING_WALL_SIGN = BLOCKS.register("mining_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), MINING));
	public static final RegistryObject<BanisterBlock> MINING_BANISTER = BLOCKS.register("mining_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(MINING_PLANKS.get())));
	public static final RegistryObject<Block> SORTING_PLANKS = BLOCKS.register("sorting_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> SORTING_STAIRS = BLOCKS.register("sorting_stairs", () -> new StairBlock(SORTING_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));
	public static final RegistryObject<Block> SORTING_SLAB = BLOCKS.register("sorting_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SORTING_BUTTON = BLOCKS.register("sorting_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SORTING_FENCE = BLOCKS.register("sorting_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));
	public static final RegistryObject<Block> SORTING_GATE = BLOCKS.register("sorting_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));
	public static final RegistryObject<Block> SORTING_PLATE = BLOCKS.register("sorting_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<DoorBlock> SORTING_DOOR = BLOCKS.register("sorting_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<TrapDoorBlock> SORTING_TRAPDOOR = BLOCKS.register("sorting_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StandingSignBlock> SORTING_SIGN = BLOCKS.register("sorting_sign", () -> new TFSignBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), SORTING));
	public static final RegistryObject<WallSignBlock> SORTING_WALL_SIGN = BLOCKS.register("sorting_wall_sign", () -> new TFWallSignBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get()).strength(3.0F).sound(SoundType.WOOD).noOcclusion().noCollission(), SORTING));
	public static final RegistryObject<BanisterBlock> SORTING_BANISTER = BLOCKS.register("sorting_banister", () -> new BanisterBlock(BlockBehaviour.Properties.copy(SORTING_PLANKS.get())));

	public static final RegistryObject<TwilightChest> TWILIGHT_OAK_CHEST = BLOCKS.register("twilight_oak_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<TwilightChest> CANOPY_CHEST = BLOCKS.register("canopy_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<TwilightChest> MANGROVE_CHEST = BLOCKS.register("mangrove_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<TwilightChest> DARKWOOD_CHEST = BLOCKS.register("darkwood_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<TwilightChest> TIME_CHEST = BLOCKS.register("time_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<TwilightChest> TRANSFORMATION_CHEST = BLOCKS.register("transformation_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<TwilightChest> MINING_CHEST = BLOCKS.register("mining_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));
	public static final RegistryObject<TwilightChest> SORTING_CHEST = BLOCKS.register("sorting_chest", () -> new TwilightChest(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD)));

	//Flower Pots
	public static final RegistryObject<FlowerPotBlock> POTTED_TWILIGHT_OAK_SAPLING = BLOCKS.register("potted_twilight_oak_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, TWILIGHT_OAK_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_CANOPY_SAPLING = BLOCKS.register("potted_canopy_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CANOPY_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MANGROVE_SAPLING = BLOCKS.register("potted_mangrove_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MANGROVE_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_DARKWOOD_SAPLING = BLOCKS.register("potted_darkwood_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DARKWOOD_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_HOLLOW_OAK_SAPLING = BLOCKS.register("potted_hollow_oak_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HOLLOW_OAK_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_RAINBOW_OAK_SAPLING = BLOCKS.register("potted_rainbow_oak_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, RAINBOW_OAK_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_TIME_SAPLING = BLOCKS.register("potted_time_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, TIME_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_TRANSFORMATION_SAPLING = BLOCKS.register("potted_transformation_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, TRANSFORMATION_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MINING_SAPLING = BLOCKS.register("potted_mining_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MINING_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_SORTING_SAPLING = BLOCKS.register("potted_sorting_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, SORTING_SAPLING, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MAYAPPLE = BLOCKS.register("potted_mayapple", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MAYAPPLE, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_FIDDLEHEAD = BLOCKS.register("potted_fiddlehead", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, FIDDLEHEAD, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_MUSHGLOOM = BLOCKS.register("potted_mushgloom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MUSHGLOOM, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_THORN = BLOCKS.register("potted_thorn", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BROWN_THORNS, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_GREEN_THORN = BLOCKS.register("potted_green_thorn", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, GREEN_THORNS, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
	public static final RegistryObject<FlowerPotBlock> POTTED_DEAD_THORN = BLOCKS.register("potted_dead_thorn", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BURNT_THORNS, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

	@SubscribeEvent
	public static void registerItemblocks(RegistryEvent.Register<Item> evt) {
		TFBlockItems.registerBlockItems(evt);
		TFCompat.initCompatItems(evt);
	}

	private static BlockBehaviour.Properties logProperties(MaterialColor color) {
		return BlockBehaviour.Properties.of(Material.WOOD, color);
	}

	private static BlockBehaviour.Properties logProperties(MaterialColor top, MaterialColor side) {
		return BlockBehaviour.Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side);
	}

	public static void tfCompostables() {
		ComposterBlock.add(0.1F, FALLEN_LEAVES.get());
		ComposterBlock.add(0.3F, CANOPY_LEAVES.get());
		ComposterBlock.add(0.3F, CLOVER_PATCH.get());
		ComposterBlock.add(0.3F, DARK_LEAVES.get());
		ComposterBlock.add(0.3F, FIDDLEHEAD.get());
		ComposterBlock.add(0.3F, HEDGE.get());
		ComposterBlock.add(0.3F, MANGROVE_LEAVES.get());
		ComposterBlock.add(0.3F, MAYAPPLE.get());
		ComposterBlock.add(0.3F, MINING_LEAVES.get());
		ComposterBlock.add(0.3F, TWILIGHT_OAK_LEAVES.get());
		ComposterBlock.add(0.3F, RAINBOW_OAK_LEAVES.get());
		ComposterBlock.add(0.3F, ROOT_STRAND.get());
		ComposterBlock.add(0.3F, SORTING_LEAVES.get());
		ComposterBlock.add(0.3F, THORN_LEAVES.get());
		ComposterBlock.add(0.3F, TIME_LEAVES.get());
		ComposterBlock.add(0.3F, TRANSFORMATION_LEAVES.get());
		ComposterBlock.add(0.3F, TWILIGHT_OAK_SAPLING.get());
		ComposterBlock.add(0.3F, CANOPY_SAPLING.get());
		ComposterBlock.add(0.3F, MANGROVE_SAPLING.get());
		ComposterBlock.add(0.3F, DARKWOOD_SAPLING.get());
		ComposterBlock.add(0.3F, RAINBOW_OAK_SAPLING.get());
		ComposterBlock.add(0.5F, BEANSTALK_LEAVES.get());
		ComposterBlock.add(0.5F, MOSS_PATCH.get());
		ComposterBlock.add(0.5F, ROOT_BLOCK.get());
		ComposterBlock.add(0.5F, THORN_ROSE.get());
		ComposterBlock.add(0.5F, TROLLVIDR.get());
		ComposterBlock.add(0.5F, HOLLOW_OAK_SAPLING.get());
		ComposterBlock.add(0.5F, TIME_SAPLING.get());
		ComposterBlock.add(0.5F, TRANSFORMATION_SAPLING.get());
		ComposterBlock.add(0.5F, MINING_SAPLING.get());
		ComposterBlock.add(0.5F, SORTING_SAPLING.get());
		ComposterBlock.add(0.5F, TORCHBERRY_PLANT.get());
		ComposterBlock.add(0.65F, HUGE_MUSHGLOOM_STEM.get());
		ComposterBlock.add(0.65F, HUGE_WATER_LILY.get());
		ComposterBlock.add(0.65F, LIVEROOT_BLOCK.get());
		ComposterBlock.add(0.65F, MUSHGLOOM.get());
		ComposterBlock.add(0.65F, UBEROUS_SOIL.get());
		ComposterBlock.add(0.65F, HUGE_STALK.get());
		ComposterBlock.add(0.65F, UNRIPE_TROLLBER.get());
		ComposterBlock.add(0.65F, TROLLBER.get());
		ComposterBlock.add(0.85F, HUGE_LILY_PAD.get());
		ComposterBlock.add(0.85F, HUGE_MUSHGLOOM.get());

		//eh, we'll do items here too
		ComposterBlock.add(0.3F, TFItems.TORCHBERRIES.get());
		ComposterBlock.add(0.5F, TFItems.LIVEROOT.get());
		ComposterBlock.add(0.65F, TFItems.MAZE_WAFER.get());
		ComposterBlock.add(0.85F, TFItems.EXPERIMENT_115.get());
		ComposterBlock.add(0.85F, TFItems.MAGIC_BEANS.get());
	}

	public static void TFBurnables() {
		FireBlock fireblock = (FireBlock)Blocks.FIRE;
		fireblock.setFlammable(ROOT_BLOCK.get(), 5, 20);
		fireblock.setFlammable(LIVEROOT_BLOCK.get(), 5, 20);
		fireblock.setFlammable(EMPTY_CANOPY_BOOKSHELF.get(), 30, 20);
		fireblock.setFlammable(TWILIGHT_OAK_WOOD.get(), 5, 5);
		fireblock.setFlammable(TWILIGHT_OAK_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TWILIGHT_OAK_SLAB.get(), 5, 20);
		fireblock.setFlammable(TWILIGHT_OAK_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TWILIGHT_OAK_FENCE.get(), 5, 20);
		fireblock.setFlammable(TWILIGHT_OAK_GATE.get(), 5, 20);
		fireblock.setFlammable(CANOPY_WOOD.get(), 5, 5);
		fireblock.setFlammable(CANOPY_PLANKS.get(), 5, 20);
		fireblock.setFlammable(CANOPY_SLAB.get(), 5, 20);
		fireblock.setFlammable(CANOPY_STAIRS.get(), 5, 20);
		fireblock.setFlammable(CANOPY_FENCE.get(), 5, 20);
		fireblock.setFlammable(CANOPY_GATE.get(), 5, 20);
		fireblock.setFlammable(MANGROVE_WOOD.get(), 5, 5);
		fireblock.setFlammable(MANGROVE_PLANKS.get(), 5, 20);
		fireblock.setFlammable(MANGROVE_SLAB.get(), 5, 20);
		fireblock.setFlammable(MANGROVE_STAIRS.get(), 5, 20);
		fireblock.setFlammable(MANGROVE_FENCE.get(), 5, 20);
		fireblock.setFlammable(MANGROVE_GATE.get(), 5, 20);
		fireblock.setFlammable(DARK_WOOD.get(), 5, 5);
		fireblock.setFlammable(DARK_PLANKS.get(), 5, 20);
		fireblock.setFlammable(DARK_SLAB.get(), 5, 20);
		fireblock.setFlammable(DARK_STAIRS.get(), 5, 20);
		fireblock.setFlammable(DARK_FENCE.get(), 5, 20);
		fireblock.setFlammable(DARK_GATE.get(), 5, 20);
		fireblock.setFlammable(TIME_WOOD.get(), 5, 5);
		fireblock.setFlammable(TIME_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TIME_SLAB.get(), 5, 20);
		fireblock.setFlammable(TIME_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TIME_FENCE.get(), 5, 20);
		fireblock.setFlammable(TIME_GATE.get(), 5, 20);
		fireblock.setFlammable(TRANSFORMATION_WOOD.get(), 5, 5);
		fireblock.setFlammable(TRANSFORMATION_PLANKS.get(), 5, 20);
		fireblock.setFlammable(TRANSFORMATION_SLAB.get(), 5, 20);
		fireblock.setFlammable(TRANSFORMATION_STAIRS.get(), 5, 20);
		fireblock.setFlammable(TRANSFORMATION_FENCE.get(), 5, 20);
		fireblock.setFlammable(TRANSFORMATION_GATE.get(), 5, 20);
		fireblock.setFlammable(MINING_WOOD.get(), 5, 5);
		fireblock.setFlammable(MINING_PLANKS.get(), 5, 20);
		fireblock.setFlammable(MINING_SLAB.get(), 5, 20);
		fireblock.setFlammable(MINING_STAIRS.get(), 5, 20);
		fireblock.setFlammable(MINING_FENCE.get(), 5, 20);
		fireblock.setFlammable(MINING_GATE.get(), 5, 20);
		fireblock.setFlammable(SORTING_WOOD.get(), 5, 5);
		fireblock.setFlammable(SORTING_PLANKS.get(), 5, 20);
		fireblock.setFlammable(SORTING_SLAB.get(), 5, 20);
		fireblock.setFlammable(SORTING_STAIRS.get(), 5, 20);
		fireblock.setFlammable(SORTING_FENCE.get(), 5, 20);
		fireblock.setFlammable(SORTING_GATE.get(), 5, 20);
	}

	public static void TFPots() {
		FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;

		pot.addPlant(TWILIGHT_OAK_SAPLING.getId(), POTTED_TWILIGHT_OAK_SAPLING);
		pot.addPlant(CANOPY_SAPLING.getId(), POTTED_CANOPY_SAPLING);
		pot.addPlant(MANGROVE_SAPLING.getId(), POTTED_MANGROVE_SAPLING);
		pot.addPlant(DARKWOOD_SAPLING.getId(), POTTED_DARKWOOD_SAPLING);
		pot.addPlant(HOLLOW_OAK_SAPLING.getId(), POTTED_HOLLOW_OAK_SAPLING);
		pot.addPlant(RAINBOW_OAK_SAPLING.getId(), POTTED_RAINBOW_OAK_SAPLING);
		pot.addPlant(TIME_SAPLING.getId(), POTTED_TIME_SAPLING);
		pot.addPlant(TRANSFORMATION_SAPLING.getId(), POTTED_TRANSFORMATION_SAPLING);
		pot.addPlant(MINING_SAPLING.getId(), POTTED_MINING_SAPLING);
		pot.addPlant(SORTING_SAPLING.getId(), POTTED_SORTING_SAPLING);
		pot.addPlant(MAYAPPLE.getId(), POTTED_MAYAPPLE);
		pot.addPlant(FIDDLEHEAD.getId(), POTTED_FIDDLEHEAD);
		pot.addPlant(MUSHGLOOM.getId(), POTTED_MUSHGLOOM);
		pot.addPlant(BROWN_THORNS.getId(), POTTED_THORN);
		pot.addPlant(GREEN_THORNS.getId(), POTTED_GREEN_THORN);
		pot.addPlant(BURNT_THORNS.getId(), POTTED_DEAD_THORN);
	}
}
