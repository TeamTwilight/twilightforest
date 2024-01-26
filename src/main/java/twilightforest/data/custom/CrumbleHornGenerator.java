package twilightforest.data.custom;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

public class CrumbleHornGenerator extends CrumbleHornProvider {

	public CrumbleHornGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, TwilightForestMod.ID, helper);
	}

	@Override
	public void registerTransforms() {
		addTransform(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
		addTransform(Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
		addTransform(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
		addTransform(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE);
		addTransform(Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS);
		addTransform(Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS);
		addTransform(Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES);
		addTransform(TFBlocks.MAZESTONE_BRICK.get(), TFBlocks.CRACKED_MAZESTONE.get());
		addTransform(TFBlocks.UNDERBRICK.get(), TFBlocks.CRACKED_UNDERBRICK.get());
		addTransform(TFBlocks.DEADROCK.get(), TFBlocks.CRACKED_DEADROCK.get());
		addTransform(TFBlocks.CRACKED_DEADROCK.get(), TFBlocks.WEATHERED_DEADROCK.get());
		addTransform(TFBlocks.TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get());
		addTransform(TFBlocks.CASTLE_BRICK.get(), TFBlocks.CRACKED_CASTLE_BRICK.get());
		addTransform(TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get());
		addTransform(TFBlocks.NAGASTONE_PILLAR.get(), TFBlocks.CRACKED_NAGASTONE_PILLAR.get());
		addTransform(TFBlocks.ETCHED_NAGASTONE.get(), TFBlocks.CRACKED_ETCHED_NAGASTONE.get());
		addTransform(TFBlocks.CASTLE_BRICK_STAIRS.get(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get());
		addTransform(TFBlocks.NAGASTONE_STAIRS_LEFT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get());
		addTransform(TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());
		addTransform(Blocks.STONE, Blocks.COBBLESTONE);
		addTransform(Blocks.COBBLESTONE, Blocks.GRAVEL);
		addTransform(Blocks.SANDSTONE, Blocks.SAND);
		addTransform(Blocks.RED_SANDSTONE, Blocks.RED_SAND);
		addTransform(Blocks.GRASS_BLOCK, Blocks.DIRT);
		addTransform(Blocks.PODZOL, Blocks.DIRT);
		addTransform(Blocks.MYCELIUM, Blocks.DIRT);
		addTransform(Blocks.COARSE_DIRT, Blocks.DIRT);
		addTransform(Blocks.ROOTED_DIRT, Blocks.DIRT);
		addTransform(Blocks.OXIDIZED_COPPER, Blocks.WEATHERED_COPPER);
		addTransform(Blocks.WEATHERED_COPPER, Blocks.EXPOSED_COPPER);
		addTransform(Blocks.EXPOSED_COPPER, Blocks.COPPER_BLOCK);
		addTransform(Blocks.OXIDIZED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER);
		addTransform(Blocks.WEATHERED_CUT_COPPER, Blocks.EXPOSED_CUT_COPPER);
		addTransform(Blocks.EXPOSED_CUT_COPPER, Blocks.CUT_COPPER);
		addDissolve(Blocks.GRAVEL);
		addDissolve(Blocks.DIRT);
		addDissolve(Blocks.SAND);
		addDissolve(Blocks.RED_SAND);
		addDissolve(Blocks.CLAY);
		addDissolve(Blocks.ANDESITE);
		addDissolve(Blocks.DIORITE);
		addDissolve(Blocks.GRANITE);
	}
}
