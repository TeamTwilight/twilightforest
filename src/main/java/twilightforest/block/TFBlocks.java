package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.TwilightForestMod;
import twilightforest.client.ISTER;
import twilightforest.enums.BossVariant;
import twilightforest.enums.MagicWoodVariant;
import twilightforest.enums.PlantVariant;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TFTileEntities;
import twilightforest.world.feature.tree.*;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
@Nonnull
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFBlocks {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, TwilightForestMod.ID);

	public static final RegistryObject<LogBlock> oak_log                    = BLOCKS.register("oak_log", () -> new BlockTFLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<LogBlock> canopy_log                 = BLOCKS.register("canopy_log", () -> new BlockTFLog(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<LogBlock> mangrove_log               = BLOCKS.register("mangrove_log", () -> new BlockTFLog(MaterialColor.DIRT, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<LogBlock> dark_log                   = BLOCKS.register("dark_log", () -> new BlockTFLog(MaterialColor.ADOBE, Block.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> oak_wood                   = BLOCKS.register("oak_wood", () -> new BlockFlammable(5, 5, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> canopy_wood                = BLOCKS.register("canopy_wood", () -> new BlockFlammable(5, 5, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> mangrove_wood              = BLOCKS.register("mangrove_wood", () -> new BlockFlammable(5, 5, Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> dark_wood                  = BLOCKS.register("dark_wood", () -> new BlockFlammable(5, 5, Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> oak_leaves                 = BLOCKS.register("oak_leaves", () -> new BlockTFLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().nonOpaque().sound(SoundType.PLANT)));
	public static final RegistryObject<Block> canopy_leaves              = BLOCKS.register("canopy_leaves", () -> new BlockTFLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().nonOpaque().sound(SoundType.PLANT)));
	public static final RegistryObject<Block> mangrove_leaves            = BLOCKS.register("mangrove_leaves", () -> new BlockTFLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().nonOpaque().sound(SoundType.PLANT)));
	public static final RegistryObject<Block> dark_leaves                = BLOCKS.register("dark_leaves", () -> new BlockTFDarkLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> rainboak_leaves            = BLOCKS.register("rainboak_leaves", () -> new BlockTFLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().nonOpaque().sound(SoundType.PLANT)));
	public static final RegistryObject<BlockTFPortal> twilight_portal    = BLOCKS.register("twilight_portal", () -> new BlockTFPortal(Block.Properties.create(Material.PORTAL).hardnessAndResistance(-1.0F).sound(SoundType.GLASS).lightValue(11).doesNotBlockMovement().nonOpaque().noDrops()));
	public static final RegistryObject<Block> maze_stone                 = BLOCKS.register("maze_stone", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> maze_stone_brick           = BLOCKS.register("maze_stone_brick", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> maze_stone_chiseled        = BLOCKS.register("maze_stone_chiseled", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> maze_stone_decorative      = BLOCKS.register("maze_stone_decorative", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> maze_stone_cracked         = BLOCKS.register("maze_stone_cracked", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> maze_stone_mossy           = BLOCKS.register("maze_stone_mossy", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> maze_stone_mosaic          = BLOCKS.register("maze_stone_mosaic", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> maze_stone_border          = BLOCKS.register("maze_stone_border", () -> new BlockTFMazestone(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 5.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> hedge                      = BLOCKS.register("hedge", () -> new BlockTFHedge(Block.Properties.create(Material.CACTUS).hardnessAndResistance(2.0F, 10.0F).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> boss_spawner               = BLOCKS.register("boss_spawner", () -> new BlockTFBossSpawner(Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F).noDrops()));
	public static final RegistryObject<Block> firefly_jar                = BLOCKS.register("firefly_jar", () -> new BlockTFFireflyJar(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F, 0.0F).sound(SoundType.WOOD).lightValue(15).nonOpaque()));
	public static final RegistryObject<Block> moss_patch                 = BLOCKS.register("moss_patch", () -> new BlockTFPlant(PlantVariant.MOSSPATCH, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> mayapple                   = BLOCKS.register("mayapple", () -> new BlockTFPlant(PlantVariant.MAYAPPLE, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> clover_patch               = BLOCKS.register("clover_patch", () -> new BlockTFPlant(PlantVariant.CLOVERPATCH, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> fiddlehead                 = BLOCKS.register("fiddlehead", () -> new BlockTFPlant(PlantVariant.FIDDLEHEAD, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> mushgloom                  = BLOCKS.register("mushgloom", () -> new BlockTFPlant(PlantVariant.MUSHGLOOM, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque().lightValue(3)));
	public static final RegistryObject<Block> forest_grass               = BLOCKS.register("forest_grass", () -> new BlockTFPlant(PlantVariant.FORESTGRASS, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> dead_bush                  = BLOCKS.register("dead_bush", () -> new BlockTFPlant(PlantVariant.DEADBUSH, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> torchberry                 = BLOCKS.register("torchberry", () -> new BlockTFPlant(PlantVariant.TORCHBERRY, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque().lightValue(8)));
	public static final RegistryObject<Block> root_strand                = BLOCKS.register("root_strand", () -> new BlockTFPlant(PlantVariant.ROOT_STRAND, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> fallen_leaves              = BLOCKS.register("fallen_leaves", () -> new BlockTFPlant(PlantVariant.FALLEN_LEAVES, Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement().nonOpaque()));
	public static final RegistryObject<Block> root                       = BLOCKS.register("root", () -> new BlockTFRoots());
	public static final RegistryObject<Block> liveroot                   = BLOCKS.register("liveroot", () -> new BlockTFRoots());
	public static final RegistryObject<Block> uncrafting_table           = BLOCKS.register("uncrafting_table", () -> new BlockTFUncraftingTable());
	public static final RegistryObject<Block> smoker                     = BLOCKS.register("smoker", () -> new BlockTFSmoker());
	public static final RegistryObject<Block> encased_smoker             = BLOCKS.register("encased_smoker", () -> new BlockTFEncasedSmoker());
	public static final RegistryObject<Block> fire_jet                   = BLOCKS.register("fire_jet", () -> new BlockTFFireJet());
	public static final RegistryObject<Block> encased_fire_jet           = BLOCKS.register("encased_fire_jet", () -> new BlockTFEncasedFireJet());
	public static final RegistryObject<Block> naga_stone_head            = BLOCKS.register("naga_stone_head", () -> new BlockTFNagastoneHead());
	public static final RegistryObject<Block> naga_stone                 = BLOCKS.register("naga_stone", () -> new BlockTFNagastone());
	public static final RegistryObject<SaplingBlock> oak_sapling            = BLOCKS.register("oak_sapling", () -> new BlockTFSapling(new SmallOakTree()));
	public static final RegistryObject<SaplingBlock> canopy_sapling         = BLOCKS.register("canopy_sapling", () -> new BlockTFSapling(new CanopyTree()));
	public static final RegistryObject<SaplingBlock> mangrove_sapling       = BLOCKS.register("mangrove_sapling", () -> new BlockTFSapling(new MangroveTree()));
	public static final RegistryObject<SaplingBlock> darkwood_sapling       = BLOCKS.register("darkwood_sapling", () -> new BlockTFSapling(new DarkCanopyTree()));
	public static final RegistryObject<SaplingBlock> hollow_oak_sapling     = BLOCKS.register("hollow_oak_sapling", () -> new BlockTFSapling(new HollowTree()));
	public static final RegistryObject<SaplingBlock> time_sapling           = BLOCKS.register("time_sapling", () -> new BlockTFSapling(new TimeTree()));
	public static final RegistryObject<SaplingBlock> transformation_sapling = BLOCKS.register("transformation_sapling", () -> new BlockTFSapling(new TransformationTree()));
	public static final RegistryObject<SaplingBlock> mining_sapling         = BLOCKS.register("mining_sapling", () -> new BlockTFSapling(new MinersTree()));
	public static final RegistryObject<SaplingBlock> sorting_sapling        = BLOCKS.register("sorting_sapling", () -> new BlockTFSapling(new SortingTree()));
	public static final RegistryObject<SaplingBlock> rainboak_sapling       = BLOCKS.register("rainboak_sapling", () -> new BlockTFSapling(new RainboakTree()));
	public static final RegistryObject<LogBlock> time_log                   = BLOCKS.register("time_log", () -> new BlockTFMagicLog(MaterialColor.DIRT, MaterialColor.OBSIDIAN));
	public static final RegistryObject<LogBlock> transformation_log         = BLOCKS.register("transformation_log", () -> new BlockTFMagicLog(MaterialColor.WOOD, MaterialColor.OBSIDIAN));
	public static final RegistryObject<LogBlock> mining_log                 = BLOCKS.register("mining_log", () -> new BlockTFMagicLog(MaterialColor.SAND, MaterialColor.QUARTZ));
	public static final RegistryObject<LogBlock> sorting_log                = BLOCKS.register("sorting_log", () -> new BlockTFMagicLog(MaterialColor.OBSIDIAN, MaterialColor.BROWN));
	public static final RegistryObject<Block> time_wood                  = BLOCKS.register("time_wood", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> transformation_wood        = BLOCKS.register("transformation_wood", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> mining_wood                = BLOCKS.register("mining_wood", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> sorting_wood               = BLOCKS.register("sorting_wood", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> time_log_core              = BLOCKS.register("time_log_core", () -> new BlockTFMagicLogSpecial(MaterialColor.DIRT, MaterialColor.OBSIDIAN, MagicWoodVariant.TIME));
	public static final RegistryObject<Block> transformation_log_core    = BLOCKS.register("transformation_log_core", () -> new BlockTFMagicLogSpecial(MaterialColor.WOOD, MaterialColor.OBSIDIAN, MagicWoodVariant.TRANS));
	public static final RegistryObject<Block> mining_log_core            = BLOCKS.register("mining_log_core", () -> new BlockTFMagicLogSpecial(MaterialColor.SAND, MaterialColor.QUARTZ, MagicWoodVariant.MINE));
	public static final RegistryObject<Block> sorting_log_core           = BLOCKS.register("sorting_log_core", () -> new BlockTFMagicLogSpecial(MaterialColor.OBSIDIAN, MaterialColor.BROWN, MagicWoodVariant.SORT));
	public static final RegistryObject<Block> time_leaves                = BLOCKS.register("time_leaves", () -> new BlockTFMagicLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).sound(SoundType.PLANT).tickRandomly().nonOpaque()));
	public static final RegistryObject<Block> transformation_leaves      = BLOCKS.register("transformation_leaves", () -> new BlockTFMagicLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).sound(SoundType.PLANT).tickRandomly().nonOpaque()));
	public static final RegistryObject<Block> mining_leaves              = BLOCKS.register("mining_leaves", () -> new BlockTFMagicLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).sound(SoundType.PLANT).tickRandomly().nonOpaque()));
	public static final RegistryObject<Block> sorting_leaves             = BLOCKS.register("sorting_leaves", () -> new BlockTFMagicLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).sound(SoundType.PLANT).tickRandomly().nonOpaque()));
	public static final RegistryObject<Block> firefly                    = BLOCKS.register("firefly", () -> new BlockTFFirefly(Block.Properties.create(Material.MISCELLANEOUS).lightValue(15).sound(SoundType.SLIME).hardnessAndResistance(0.0F).doesNotBlockMovement()));
	public static final RegistryObject<Block> cicada                     = BLOCKS.register("cicada", () -> new BlockTFCicada(Block.Properties.create(Material.MISCELLANEOUS).sound(SoundType.SLIME).hardnessAndResistance(0.0F).doesNotBlockMovement()));
	public static final RegistryObject<Block> moonworm                   = BLOCKS.register("moonworm", () -> new BlockTFMoonworm(Block.Properties.create(Material.MISCELLANEOUS).lightValue(14).sound(SoundType.SLIME).hardnessAndResistance(0.0F).doesNotBlockMovement()));
	public static final RegistryObject<Block> tower_wood                 = BLOCKS.register("tower_wood", () -> new BlockFlammable(1, 0, Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> tower_wood_encased         = BLOCKS.register("tower_wood_encased", () -> new BlockFlammable(1, 0, Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> tower_wood_cracked         = BLOCKS.register("tower_wood_cracked", () -> new BlockFlammable(1, 0, Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> tower_wood_mossy           = BLOCKS.register("tower_wood_mossy", () -> new BlockFlammable(1, 0, Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(40.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> tower_wood_infested        = BLOCKS.register("tower_wood_infested", () -> new BlockInfestedTowerWood(1, 0, Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(0.75F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> reappearing_block          = BLOCKS.register("reappearing_block", () -> new BlockTFReappearingBlock());
	public static final RegistryObject<Block> vanishing_block            = BLOCKS.register("vanishing_block", () -> new BlockTFVanishingBlock());
	public static final RegistryObject<Block> locked_vanishing_block     = BLOCKS.register("locked_vanishing_block", () -> new BlockTFLockedVanishing());
	public static final RegistryObject<Block> carminite_builder          = BLOCKS.register("carminite_builder", () -> new BlockTFBuilder());
	public static final RegistryObject<Block> antibuilder                = BLOCKS.register("antibuilder", () -> new BlockTFAntibuilder(Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(10.0F, 35.0F).sound(SoundType.WOOD).lightValue(10).noDrops()));
	public static final RegistryObject<BlockTFGhastTrap> ghast_trap      = BLOCKS.register("ghast_trap", () -> new BlockTFGhastTrap());
	public static final RegistryObject<Block> carminite_reactor          = BLOCKS.register("carminite_reactor", () -> new BlockTFReactor());
	public static final RegistryObject<Block> reappearing_translucent    = BLOCKS.register("reappearing_translucent", () -> new BlockTFReappearTranslucent());
	public static final RegistryObject<Block> builder_translucent        = BLOCKS.register("builder_translucent", () -> new BlockTFBuiltTranslucent());
	public static final RegistryObject<Block> reverter_replacement       = BLOCKS.register("reverter_replacement", () -> new BlockTFTowerTranslucent(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F, 2000.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> reactor_debris             = BLOCKS.register("reactor_debris", () -> new BlockTFTowerTranslucent(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F, 2000.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> fake_gold                  = BLOCKS.register("fake_gold", () -> new Block(Block.Properties.create(Material.GLASS).hardnessAndResistance(50.0F, 2000.0F).sound(SoundType.METAL).noDrops()));
	public static final RegistryObject<Block> fake_diamond               = BLOCKS.register("fake_diamond", () -> new Block(Block.Properties.create(Material.GLASS).hardnessAndResistance(50.0F, 2000.0F).sound(SoundType.METAL).noDrops()));
	public static final RegistryObject<BlockTFTrophy> naga_trophy           = BLOCKS.register("naga_trophy", () -> new BlockTFTrophy(BossVariant.NAGA));
	public static final RegistryObject<BlockTFTrophy> lich_trophy           = BLOCKS.register("lich_trophy", () -> new BlockTFTrophy(BossVariant.LICH));
	public static final RegistryObject<BlockTFTrophy> hydra_trophy          = BLOCKS.register("hydra_trophy", () -> new BlockTFTrophy(BossVariant.HYDRA));
	public static final RegistryObject<BlockTFTrophy> ur_ghast_trophy       = BLOCKS.register("ur_ghast_trophy", () -> new BlockTFTrophy(BossVariant.UR_GHAST));
	public static final RegistryObject<BlockTFTrophy> knight_phantom_trophy = BLOCKS.register("knight_phantom_trophy", () -> new BlockTFTrophy(BossVariant.KNIGHT_PHANTOM));
	public static final RegistryObject<BlockTFTrophy> snow_queen_trophy     = BLOCKS.register("snow_queen_trophy", () -> new BlockTFTrophy(BossVariant.SNOW_QUEEN));
	public static final RegistryObject<BlockTFTrophy> minoshroom_trophy     = BLOCKS.register("minoshroom_trophy", () -> new BlockTFTrophy(BossVariant.MINOSHROOM));
	public static final RegistryObject<BlockTFTrophy> quest_ram_trophy      = BLOCKS.register("quest_ram_trophy", () -> new BlockTFTrophy(BossVariant.QUEST_RAM));
	public static final RegistryObject<Block> stronghold_shield          = BLOCKS.register("stronghold_shield", () -> new BlockTFShield(Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 6000000.0F).sound(SoundType.METAL).noDrops()));
	public static final RegistryObject<Block> trophy_pedestal            = BLOCKS.register("trophy_pedestal", () -> new BlockTFTrophyPedestal());
	public static final RegistryObject<Block> aurora_block               = BLOCKS.register("aurora_block", () -> new BlockTFAuroraBrick(Block.Properties.create(Material.PACKED_ICE).hardnessAndResistance(2.0F, 10.0F)));
	public static final RegistryObject<Block> underbrick                 = BLOCKS.register("underbrick", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.WOOD).hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> underbrick_mossy           = BLOCKS.register("underbrick_mossy", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.WOOD).hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> underbrick_cracked         = BLOCKS.register("underbrick_cracked", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.WOOD).hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> underbrick_floor           = BLOCKS.register("underbrick_floor", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.WOOD).hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> brown_thorns               = BLOCKS.register("brown_thorns", () -> new BlockTFThorns(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(50.0F, 2000.0F).sound(SoundType.WOOD).noDrops()));
	public static final RegistryObject<Block> green_thorns               = BLOCKS.register("green_thorns", () -> new BlockTFThorns(Block.Properties.create(Material.WOOD, MaterialColor.FOLIAGE).hardnessAndResistance(50.0F, 2000.0F).sound(SoundType.WOOD).noDrops()));
	public static final RegistryObject<Block> burnt_thorns               = BLOCKS.register("burnt_thorns", () -> new BlockTFBurntThorns(Block.Properties.create(Material.WOOD, MaterialColor.STONE).hardnessAndResistance(0.01F, 0.0F).sound(SoundType.SAND).noDrops()));
	public static final RegistryObject<Block> thorn_rose                 = BLOCKS.register("thorn_rose", () -> new BlockTFThornRose());
	public static final RegistryObject<Block> thorn_leaves               = BLOCKS.register("thorn_leaves", () -> new BlockTFLeaves3());
	public static final RegistryObject<Block> beanstalk_leaves           = BLOCKS.register("beanstalk_leaves", () -> new BlockTFLeaves3());
	public static final RegistryObject<Block> deadrock                   = BLOCKS.register("deadrock", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 6000000.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> deadrock_cracked           = BLOCKS.register("deadrock_cracked", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 6000000.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> deadrock_weathered         = BLOCKS.register("deadrock_weathered", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(100.0F, 6000000.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> aurora_pillar              = BLOCKS.register("aurora_pillar", () -> new BlockTFPillar(Block.Properties.create(Material.PACKED_ICE).hardnessAndResistance(2.0F, 10.0F)));
	public static final RegistryObject<Block> aurora_slab                = BLOCKS.register("aurora_slab", () -> new SlabBlock(Block.Properties.create(Material.PACKED_ICE).hardnessAndResistance(2.0F, 10.0F)));
	public static final RegistryObject<Block> trollsteinn                = BLOCKS.register("trollsteinn", () -> new BlockTFTrollSteinn(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> wispy_cloud                = BLOCKS.register("wispy_cloud", () -> new BlockTFWispyCloud(Block.Properties.create(Material.SNOW).hardnessAndResistance(0.3F).sound(SoundType.CLOTH).nonOpaque()));
	public static final RegistryObject<Block> fluffy_cloud               = BLOCKS.register("fluffy_cloud", () -> new Block(Block.Properties.create(Material.PACKED_ICE).hardnessAndResistance(0.8F, 0.0F).sound(SoundType.CLOTH)));
	public static final RegistryObject<Block> giant_cobblestone          = BLOCKS.register("giant_cobblestone", () -> new BlockTFGiantCobble());
	public static final RegistryObject<Block> giant_log                  = BLOCKS.register("giant_log", () -> new BlockTFGiantLog());
	public static final RegistryObject<Block> giant_leaves               = BLOCKS.register("giant_leaves", () -> new BlockTFGiantLeaves());
	public static final RegistryObject<Block> giant_obsidian             = BLOCKS.register("giant_obsidian", () -> new BlockTFGiantObsidian());
	public static final RegistryObject<Block> uberous_soil               = BLOCKS.register("uberous_soil", () -> new BlockTFUberousSoil());
	public static final RegistryObject<Block> huge_stalk                 = BLOCKS.register("huge_stalk", () -> new BlockTFHugeStalk());
	public static final RegistryObject<Block> huge_mushgloom             = BLOCKS.register("huge_mushgloom", () -> new BlockTFHugeGloomBlock());
	public static final RegistryObject<Block> trollvidr                  = BLOCKS.register("trollvidr", () -> new BlockTFTrollRoot());
	public static final RegistryObject<Block> unripe_trollber            = BLOCKS.register("unripe_trollber", () -> new BlockTFUnripeTorchCluster());
	public static final RegistryObject<Block> trollber                   = BLOCKS.register("trollber", () -> new BlockTFRipeTorchCluster());
	public static final RegistryObject<Block> huge_lilypad               = BLOCKS.register("huge_lilypad", () -> new BlockTFHugeLilyPad());
	public static final RegistryObject<Block> huge_waterlily             = BLOCKS.register("huge_waterlily", () -> new BlockTFHugeWaterLily());
	public static final RegistryObject<Block> slider                     = BLOCKS.register("slider", () -> new BlockTFSlider());
	public static final RegistryObject<Block> castle_brick               = BLOCKS.register("castle_brick", () -> new BlockTFCastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> castle_brick_worn          = BLOCKS.register("castle_brick_worn", () -> new BlockTFCastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> castle_brick_cracked       = BLOCKS.register("castle_brick_cracked", () -> new BlockTFCastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> castle_brick_roof          = BLOCKS.register("castle_brick_roof", () -> new BlockTFCastleBlock(MaterialColor.GRAY));
	public static final RegistryObject<Block> castle_brick_mossy         = BLOCKS.register("castle_brick_mossy", () -> new BlockTFCastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> castle_brick_frame         = BLOCKS.register("castle_brick_frame", () -> new BlockTFCastleBlock(MaterialColor.QUARTZ));
	public static final RegistryObject<Block> castle_pillar_encased      = BLOCKS.register("castle_pillar_encased", () -> new RotatedPillarBlock(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> castle_pillar_encased_tile = BLOCKS.register("castle_pillar_encased_tile", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> castle_pillar_bold         = BLOCKS.register("castle_pillar_bold", () -> new RotatedPillarBlock(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> castle_pillar_bold_tile    = BLOCKS.register("castle_pillar_bold_tile", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 35.0F).sound(SoundType.STONE)));
	public static final RegistryObject<StairsBlock> castle_stairs_brick        = BLOCKS.register("castle_stairs_brick", () -> new BlockTFStairs(castle_brick.get().getDefaultState()));
	public static final RegistryObject<StairsBlock> castle_stairs_worn         = BLOCKS.register("castle_stairs_worn", () -> new BlockTFStairs(castle_brick_worn.get().getDefaultState()));
	public static final RegistryObject<StairsBlock> castle_stairs_cracked      = BLOCKS.register("castle_stairs_cracked", () -> new BlockTFStairs(castle_brick_cracked.get().getDefaultState()));
	public static final RegistryObject<StairsBlock> castle_stairs_mossy        = BLOCKS.register("castle_stairs_mossy", () -> new BlockTFStairs(castle_brick_mossy.get().getDefaultState()));
	public static final RegistryObject<StairsBlock> castle_stairs_encased      = BLOCKS.register("castle_stairs_encased", () -> new BlockTFStairs(castle_pillar_encased.get().getDefaultState()));
	public static final RegistryObject<StairsBlock> castle_stairs_bold         = BLOCKS.register("castle_stairs_bold", () -> new BlockTFStairs(castle_pillar_bold.get().getDefaultState()));
	public static final RegistryObject<Block> castle_rune_brick_pink     = BLOCKS.register("castle_rune_brick_pink", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> castle_rune_brick_blue     = BLOCKS.register("castle_rune_brick_blue", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> castle_rune_brick_yellow   = BLOCKS.register("castle_rune_brick_yellow", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> castle_rune_brick_purple   = BLOCKS.register("castle_rune_brick_purple", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(100.0F, 15.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> force_field_purple         = BLOCKS.register("force_field_purple", () -> new BlockTFForceField(Block.Properties.create(Material.BARRIER).hardnessAndResistance(-1.0F).lightValue(2).noDrops()));
	public static final RegistryObject<Block> force_field_pink           = BLOCKS.register("force_field_pink", () -> new BlockTFForceField(Block.Properties.create(Material.BARRIER).hardnessAndResistance(-1.0F).lightValue(2).noDrops()));
	public static final RegistryObject<Block> force_field_orange         = BLOCKS.register("force_field_orange", () -> new BlockTFForceField(Block.Properties.create(Material.BARRIER).hardnessAndResistance(-1.0F).lightValue(2).noDrops()));
	public static final RegistryObject<Block> force_field_green          = BLOCKS.register("force_field_green", () -> new BlockTFForceField(Block.Properties.create(Material.BARRIER).hardnessAndResistance(-1.0F).lightValue(2).noDrops()));
	public static final RegistryObject<Block> force_field_blue           = BLOCKS.register("force_field_blue", () -> new BlockTFForceField(Block.Properties.create(Material.BARRIER).hardnessAndResistance(-1.0F).lightValue(2).noDrops()));
	public static final RegistryObject<Block> cinder_furnace             = BLOCKS.register("cinder_furnace", () -> new BlockTFCinderFurnace());
	public static final RegistryObject<Block> cinder_log                 = BLOCKS.register("cinder_log", () -> new BlockTFCinderLog(Block.Properties.create(Material.WOOD, MaterialColor.GRAY).hardnessAndResistance(1.0F)));
	public static final RegistryObject<Block> cinder_wood                = BLOCKS.register("cinder_wood", () -> new BlockTFCinderLog(Block.Properties.create(Material.WOOD, MaterialColor.GRAY).hardnessAndResistance(1.0F)));
	public static final RegistryObject<Block> castle_door_yellow         = BLOCKS.register("castle_door_yellow", () -> new BlockTFCastleDoor(Block.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(100.0F, 35.0F)));
	public static final RegistryObject<Block> castle_door_purple         = BLOCKS.register("castle_door_purple", () -> new BlockTFCastleDoor(Block.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(100.0F, 35.0F)));
	public static final RegistryObject<Block> castle_door_pink           = BLOCKS.register("castle_door_pink", () -> new BlockTFCastleDoor(Block.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(100.0F, 35.0F)));
	public static final RegistryObject<Block> castle_door_blue           = BLOCKS.register("castle_door_blue", () -> new BlockTFCastleDoor(Block.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(100.0F, 35.0F)));
	public static final RegistryObject<Block> experiment_115             = BLOCKS.register("experiment_115", () -> new BlockTFExperiment115());
	public static final RegistryObject<Block> twilight_portal_miniature_structure    = BLOCKS.register("twilight_portal_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> hedge_maze_miniature_structure         = BLOCKS.register("hedge_maze_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> hollow_hill_miniature_structure        = BLOCKS.register("hollow_hill_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> quest_grove_miniature_structure        = BLOCKS.register("quest_grove_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> mushroom_tower_miniature_structure     = BLOCKS.register("mushroom_tower_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> naga_courtyard_miniature_structure     = BLOCKS.register("naga_courtyard_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> lich_tower_miniature_structure         = BLOCKS.register("lich_tower_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> minotaur_labyrinth_miniature_structure = BLOCKS.register("minotaur_labyrinth_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> hydra_lair_miniature_structure         = BLOCKS.register("hydra_lair_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> goblin_stronghold_miniature_structure  = BLOCKS.register("goblin_stronghold_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> dark_tower_miniature_structure         = BLOCKS.register("dark_tower_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> yeti_cave_miniature_structure          = BLOCKS.register("yeti_cave_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> aurora_palace_miniature_structure      = BLOCKS.register("aurora_palace_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> troll_cave_cottage_miniature_structure = BLOCKS.register("troll_cave_cottage_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> final_castle_miniature_structure       = BLOCKS.register("final_castle_miniature_structure", () -> new BlockTFMiniatureStructure());
	public static final RegistryObject<Block> knightmetal_block          = BLOCKS.register("knightmetal_block", () -> new BlockTFKnightmetalBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 41.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> ironwood_block = BLOCKS.register("ironwood_block", () -> new BlockTFCompressed(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> fiery_block = BLOCKS.register("fiery_block", () -> new BlockTFCompressed(Block.Properties.create(Material.IRON, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> steeleaf_block = BLOCKS.register("steeleaf_block", () -> new BlockTFCompressed(Block.Properties.create(Material.LEAVES, MaterialColor.FOLIAGE).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.PLANT)));
	public static final RegistryObject<Block> arctic_fur_block = BLOCKS.register("arctic_fur_block", () -> new BlockTFCompressed(Block.Properties.create(Material.WOOL, MaterialColor.WOOL).hardnessAndResistance(0.8F, 10.0F).sound(SoundType.CLOTH)));
	public static final RegistryObject<Block> carminite_block = BLOCKS.register("carminite_block", () -> new BlockTFCompressed(Block.Properties.create(Material.CLAY, MaterialColor.RED).hardnessAndResistance(0.0F, 10.0F).sound(SoundType.SLIME)));
	public static final RegistryObject<Block> spiral_bricks                    = BLOCKS.register("spiral_bricks", () -> new BlockTFSpiralBrick());
	public static final RegistryObject<Block> etched_nagastone                 = BLOCKS.register("etched_nagastone", () -> new BlockTFNagastoneEtched());
	public static final RegistryObject<Block> nagastone_pillar                 = BLOCKS.register("nagastone_pillar", () -> new BlockTFNagastonePillar());
	public static final RegistryObject<Block> nagastone_stairs_left            = BLOCKS.register("nagastone_stairs_left", () -> new BlockTFNagastoneStairs(etched_nagastone.get().getDefaultState()));
	public static final RegistryObject<Block> nagastone_stairs_right           = BLOCKS.register("nagastone_stairs_right", () -> new BlockTFNagastoneStairs(etched_nagastone.get().getDefaultState()));
	public static final RegistryObject<Block> etched_nagastone_mossy           = BLOCKS.register("etched_nagastone_mossy", () -> new BlockTFNagastoneEtched());
	public static final RegistryObject<Block> nagastone_pillar_mossy           = BLOCKS.register("nagastone_pillar_mossy", () -> new BlockTFNagastonePillar());
	public static final RegistryObject<Block> nagastone_stairs_mossy_left      = BLOCKS.register("nagastone_stairs_mossy_left", () -> new BlockTFNagastoneStairs(etched_nagastone_mossy.get().getDefaultState()));
	public static final RegistryObject<Block> nagastone_stairs_mossy_right     = BLOCKS.register("nagastone_stairs_mossy_right", () -> new BlockTFNagastoneStairs(etched_nagastone_mossy.get().getDefaultState()));
	public static final RegistryObject<Block> etched_nagastone_weathered       = BLOCKS.register("etched_nagastone_weathered", () -> new BlockTFNagastoneEtched());
	public static final RegistryObject<Block> nagastone_pillar_weathered       = BLOCKS.register("nagastone_pillar_weathered", () -> new BlockTFNagastonePillar());
	public static final RegistryObject<Block> nagastone_stairs_weathered_left  = BLOCKS.register("nagastone_stairs_weathered_left", () -> new BlockTFNagastoneStairs(etched_nagastone_weathered.get().getDefaultState()));
	public static final RegistryObject<Block> nagastone_stairs_weathered_right = BLOCKS.register("nagastone_stairs_weathered_right", () -> new BlockTFNagastoneStairs(etched_nagastone_weathered.get().getDefaultState()));
	public static final RegistryObject<Block> auroralized_glass          = BLOCKS.register("auroralized_glass", () -> new BlockTFAuroralizedGlass(Block.Properties.create(Material.ICE)));
	public static final RegistryObject<Block> iron_ladder                = BLOCKS.register("iron_ladder", () -> new BlockTFLadderBars(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(5.0F, 10.0F).sound(SoundType.METAL).nonOpaque()));
	public static final RegistryObject<Block> terrorcotta_circle         = BLOCKS.register("terrorcotta_circle", () -> new BlockTFHorizontal(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.7F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> terrorcotta_diagonal       = BLOCKS.register("terrorcotta_diagonal", () -> new BlockTFDiagonal(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.7F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> stone_twist                = BLOCKS.register("stone_twist", () -> new BlockTFPillar(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 10.0F)));
	public static final RegistryObject<Block> stone_twist_thin           = BLOCKS.register("stone_twist_thin", () -> new BlockTFWallPillar(Material.ROCK, 12, 16));
	public static final RegistryObject<Block> lapis_block                = BLOCKS.register("lapis_block", () -> new BlockTFLapisBlock());

	// TODO chests? boats?
	public static final RegistryObject<Block> twilight_oak_planks   = BLOCKS.register("twilight_oak_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> twilight_oak_stairs   = BLOCKS.register("twilight_oak_stairs", () -> new BlockTFStairs(twilight_oak_planks.get().getDefaultState()));
	public static final RegistryObject<Block> twilight_oak_slab     = BLOCKS.register("twilight_oak_slab", () -> new SlabBlock(Block.Properties.from(twilight_oak_planks.get())));
	public static final RegistryObject<Block> twilight_oak_button   = BLOCKS.register("twilight_oak_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> twilight_oak_fence    = BLOCKS.register("twilight_oak_fence", () -> new FenceBlock(Block.Properties.from(twilight_oak_planks.get())));
	public static final RegistryObject<Block> twilight_oak_gate     = BLOCKS.register("twilight_oak_gate", () -> new FenceGateBlock(Block.Properties.from(twilight_oak_planks.get())));
	public static final RegistryObject<Block> twilight_oak_plate    = BLOCKS.register("twilight_oak_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> twilight_oak_door     = BLOCKS.register("twilight_oak_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> twilight_oak_trapdoor = BLOCKS.register("twilight_oak_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.WOOD));
	public static final RegistryObject<Block> canopy_planks         = BLOCKS.register("canopy_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> canopy_stairs         = BLOCKS.register("canopy_stairs", () -> new BlockTFStairs(canopy_planks.get().getDefaultState()));
	public static final RegistryObject<Block> canopy_slab           = BLOCKS.register("canopy_slab", () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> canopy_button         = BLOCKS.register("canopy_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> canopy_fence          = BLOCKS.register("canopy_fence", () -> new FenceBlock(Block.Properties.from(canopy_planks.get())));
	public static final RegistryObject<Block> canopy_gate           = BLOCKS.register("canopy_gate", () -> new FenceGateBlock(Block.Properties.from(canopy_planks.get())));
	public static final RegistryObject<Block> canopy_plate          = BLOCKS.register("canopy_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> canopy_door           = BLOCKS.register("canopy_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> canopy_trapdoor       = BLOCKS.register("canopy_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.OBSIDIAN));
	public static final RegistryObject<Block> mangrove_planks       = BLOCKS.register("mangrove_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> mangrove_stairs       = BLOCKS.register("mangrove_stairs", () -> new BlockTFStairs(mangrove_planks.get().getDefaultState()));
	public static final RegistryObject<Block> mangrove_slab         = BLOCKS.register("mangrove_slab", () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> mangrove_button       = BLOCKS.register("mangrove_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> mangrove_fence        = BLOCKS.register("mangrove_fence", () -> new FenceBlock(Block.Properties.from(mangrove_planks.get())));
	public static final RegistryObject<Block> mangrove_gate         = BLOCKS.register("mangrove_gate", () -> new FenceGateBlock(Block.Properties.from(mangrove_planks.get())));
	public static final RegistryObject<Block> mangrove_plate        = BLOCKS.register("mangrove_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> mangrove_door         = BLOCKS.register("mangrove_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> mangrove_trapdoor     = BLOCKS.register("mangrove_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.DIRT));
	public static final RegistryObject<Block> dark_planks           = BLOCKS.register("dark_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> dark_stairs           = BLOCKS.register("dark_stairs", () -> new BlockTFStairs(dark_planks.get().getDefaultState()));
	public static final RegistryObject<Block> dark_slab             = BLOCKS.register("dark_slab", () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> dark_button           = BLOCKS.register("dark_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> dark_fence            = BLOCKS.register("dark_fence", () -> new FenceBlock(Block.Properties.from(dark_planks.get())));
	public static final RegistryObject<Block> dark_gate             = BLOCKS.register("dark_gate", () -> new FenceGateBlock(Block.Properties.from(dark_planks.get())));
	public static final RegistryObject<Block> dark_plate            = BLOCKS.register("dark_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> dark_door             = BLOCKS.register("dark_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.ADOBE).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> dark_trapdoor         = BLOCKS.register("dark_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.ADOBE));
	public static final RegistryObject<Block> time_planks           = BLOCKS.register("time_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> time_stairs           = BLOCKS.register("time_stairs", () -> new BlockTFStairs(time_planks.get().getDefaultState()));
	public static final RegistryObject<Block> time_slab             = BLOCKS.register("time_slab", () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> time_button           = BLOCKS.register("time_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> time_fence            = BLOCKS.register("time_fence", () -> new FenceBlock(Block.Properties.from(time_planks.get())));
	public static final RegistryObject<Block> time_gate             = BLOCKS.register("time_gate", () -> new FenceGateBlock(Block.Properties.from(time_planks.get())));
	public static final RegistryObject<Block> time_plate            = BLOCKS.register("time_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> time_door             = BLOCKS.register("time_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.DIRT).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> time_trapdoor         = BLOCKS.register("time_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.DIRT));
	public static final RegistryObject<Block> trans_planks          = BLOCKS.register("trans_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> trans_stairs          = BLOCKS.register("trans_stairs", () -> new BlockTFStairs(trans_planks.get().getDefaultState()));
	public static final RegistryObject<Block> trans_slab            = BLOCKS.register("trans_slab", () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> trans_button          = BLOCKS.register("trans_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> trans_fence           = BLOCKS.register("trans_fence", () -> new FenceBlock(Block.Properties.from(trans_planks.get())));
	public static final RegistryObject<Block> trans_gate            = BLOCKS.register("trans_gate", () -> new FenceGateBlock(Block.Properties.from(trans_planks.get())));
	public static final RegistryObject<Block> trans_plate           = BLOCKS.register("trans_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> trans_door            = BLOCKS.register("trans_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> trans_trapdoor        = BLOCKS.register("trans_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.WOOD));
	public static final RegistryObject<Block> mine_planks           = BLOCKS.register("mine_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> mine_stairs           = BLOCKS.register("mine_stairs", () -> new BlockTFStairs(mine_planks.get().getDefaultState()));
	public static final RegistryObject<Block> mine_slab             = BLOCKS.register("mine_slab", () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> mine_button           = BLOCKS.register("mine_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> mine_fence            = BLOCKS.register("mine_fence", () -> new FenceBlock(Block.Properties.from(mine_planks.get())));
	public static final RegistryObject<Block> mine_gate             = BLOCKS.register("mine_gate", () -> new FenceGateBlock(Block.Properties.from(mine_planks.get())));
	public static final RegistryObject<Block> mine_plate            = BLOCKS.register("mine_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> mine_door             = BLOCKS.register("mine_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> mine_trapdoor         = BLOCKS.register("mine_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.SAND));
	public static final RegistryObject<Block> sort_planks           = BLOCKS.register("sort_planks", () -> new Block(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairsBlock> sort_stairs           = BLOCKS.register("sort_stairs", () -> new BlockTFStairs(sort_planks.get().getDefaultState()));
	public static final RegistryObject<Block> sort_slab             = BLOCKS.register("sort_slab", () -> new SlabBlock(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> sort_button           = BLOCKS.register("sort_button", () -> new BlockTFButtonWood());
	public static final RegistryObject<Block> sort_fence            = BLOCKS.register("sort_fence", () -> new FenceBlock(Block.Properties.from(sort_planks.get())));
	public static final RegistryObject<Block> sort_gate             = BLOCKS.register("sort_gate", () -> new FenceGateBlock(Block.Properties.from(sort_planks.get())));
	public static final RegistryObject<Block> sort_plate            = BLOCKS.register("sort_plate", () -> new BlockTFPressurePlate(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(0.5F).sound(SoundType.WOOD).doesNotBlockMovement()));
	public static final RegistryObject<DoorBlock> sort_door             = BLOCKS.register("sort_door", () -> new BlockTFDoor(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<TrapDoorBlock> sort_trapdoor         = BLOCKS.register("sort_trapdoor", () -> new BlockTFTrapDoor(MaterialColor.OBSIDIAN));
	
	@SubscribeEvent
	public static void registerItemblocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		List<RegistryObject<? extends Block>> standard = Arrays.asList(
						tower_wood, tower_wood_encased, tower_wood_cracked, tower_wood_mossy, tower_wood_infested,
						fake_gold, fake_diamond,
						underbrick, underbrick_cracked, underbrick_mossy, underbrick_floor,
						wispy_cloud, fluffy_cloud,
						castle_brick, castle_brick_worn, castle_brick_cracked, castle_brick_mossy, castle_brick_roof, castle_brick_frame,
						castle_pillar_encased, castle_pillar_encased_tile, castle_pillar_bold, castle_pillar_bold_tile,
						castle_stairs_brick, castle_stairs_worn, castle_stairs_cracked, castle_stairs_mossy, castle_stairs_encased, castle_stairs_bold,
						castle_rune_brick_yellow, castle_rune_brick_purple, castle_rune_brick_pink, castle_rune_brick_blue,
						castle_door_yellow, castle_door_purple, castle_door_pink, castle_door_blue,
						twilight_portal_miniature_structure, naga_courtyard_miniature_structure, lich_tower_miniature_structure,
						knightmetal_block, ironwood_block, fiery_block, steeleaf_block, arctic_fur_block, carminite_block,
						firefly_jar, iron_ladder,
						oak_leaves, canopy_leaves, mangrove_leaves, dark_leaves, time_leaves, transformation_leaves, mining_leaves, sorting_leaves,
						rainboak_leaves, rainboak_sapling,
						oak_log, canopy_log, mangrove_log, dark_log, time_log, transformation_log, mining_log, sorting_log,
						oak_wood, canopy_wood, mangrove_wood, dark_wood, time_wood, transformation_wood, mining_wood, sorting_wood,
						oak_sapling, canopy_sapling, mangrove_sapling, darkwood_sapling, time_sapling, transformation_sapling, mining_sapling, sorting_sapling,
						twilight_oak_planks, twilight_oak_stairs, twilight_oak_slab, twilight_oak_button, twilight_oak_fence, twilight_oak_gate, twilight_oak_plate, twilight_oak_trapdoor,
						canopy_planks, canopy_stairs, canopy_slab, canopy_button, canopy_fence, canopy_gate, canopy_plate, canopy_trapdoor,
						mangrove_planks, mangrove_stairs, mangrove_slab, mangrove_button, mangrove_fence, mangrove_gate, mangrove_plate, mangrove_trapdoor,
						dark_planks, dark_stairs, dark_slab, dark_button, dark_fence, dark_gate, dark_plate, dark_trapdoor,
						time_planks, time_stairs, time_slab, time_button, time_fence, time_gate, time_plate, time_trapdoor,
						trans_planks, trans_stairs, trans_slab, trans_button, trans_fence, trans_gate, trans_plate, trans_trapdoor,
						mine_planks, mine_stairs, mine_slab, mine_button, mine_fence, mine_gate, mine_plate, mine_trapdoor,
						sort_planks, sort_stairs, sort_slab, sort_button, sort_fence, sort_gate, sort_plate, sort_trapdoor
		);
		for (RegistryObject<? extends Block> b : standard) {
			r.register(new BlockItem(b.get(), TFItems.defaultBuilder()).setRegistryName(b.get().getRegistryName()));
		}
		
		List<Block> doors = Arrays.asList(twilight_oak_door.get(), canopy_door.get(), mangrove_door.get(), dark_door.get(), time_door.get(), trans_door.get(), mine_door.get(), sort_door.get());
		for (Block b : doors) {
			r.register(new TallBlockItem(b, TFItems.defaultBuilder()).setRegistryName(b.getRegistryName()));
		}

		r.register(new BlockItem(firefly.get(), TFItems.defaultBuilder().setISTER(() -> () -> new ISTER(TFTileEntities.FIREFLY.getId())))
						.setRegistryName(firefly.getId()));
		r.register(new BlockItem(moonworm.get(), TFItems.defaultBuilder().setISTER(() -> () -> new ISTER(TFTileEntities.MOONWORM.getId())))
						.setRegistryName(moonworm.getId()));
		r.register(new BlockItem(cicada.get(), TFItems.defaultBuilder().setISTER(() -> () -> new ISTER(TFTileEntities.CICADA.getId())))
						.setRegistryName(cicada.getId()));
	}
}
