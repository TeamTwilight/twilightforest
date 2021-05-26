package twilightforest.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import javax.annotation.Nonnull;
import java.util.IdentityHashMap;
import java.util.Map;

public class ItemTFOreMeter extends Item {

	protected ItemTFOreMeter(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		int useX = MathHelper.floor(player.getPosX());
		int useZ = MathHelper.floor(player.getPosZ());

		if (!world.isRemote) {
			countOreInArea(player, world, useX, useZ, 3);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	private void countOreInArea(PlayerEntity player, World world, int useX, int useZ, int radius) {
		int chunkX = useX >> 4;
		int chunkZ = useZ >> 4;

		int countStone = 0;
		int countDirt = 0;
		int countGravel = 0;

		int countCoal = 0;
		int countIron = 0;
		int countGold = 0;
		int countDiamond = 0;
		int countLapis = 0;
		int countRedstone = 0;
		int countExposedDiamond = 0;

		int countRoots = 0;
		int countOreRoots = 0;

		int total;

		ScanResult dummy = new ScanResult();
		for (int cx = chunkX - radius; cx <= chunkX + radius; cx++) {
			for (int cz = chunkZ - radius; cz <= chunkZ + radius; cz++) {
				Map<BlockState, ScanResult> results = countBlocksInChunk(world, chunkX, chunkZ);

				countStone += results.entrySet().stream().filter(e -> e.getKey().getBlock() == Blocks.STONE).mapToInt(e -> e.getValue().count).sum();
				countDirt += results.entrySet().stream().filter(e -> e.getKey().getBlock() == Blocks.DIRT).mapToInt(e -> e.getValue().count).sum();
				countGravel += results.getOrDefault(Blocks.GRAVEL.getDefaultState(), dummy).count;

				countCoal += results.getOrDefault(Blocks.COAL_ORE.getDefaultState(), dummy).count;
				countIron += results.getOrDefault(Blocks.IRON_ORE.getDefaultState(), dummy).count;
				countGold += results.getOrDefault(Blocks.GOLD_ORE.getDefaultState(), dummy).count;
				countDiamond += results.getOrDefault(Blocks.DIAMOND_ORE.getDefaultState(), dummy).count;
				countLapis += results.getOrDefault(Blocks.LAPIS_ORE.getDefaultState(), dummy).count;
				countRedstone += results.getOrDefault(Blocks.REDSTONE_ORE.getDefaultState(), dummy).count;
				countExposedDiamond += results.getOrDefault(Blocks.DIAMOND_ORE.getDefaultState(), dummy).exposedCount;

				countRoots += results.getOrDefault(TFBlocks.root.get().getDefaultState(), dummy).count;
				countOreRoots += results.getOrDefault(TFBlocks.liveroot_block.get().getDefaultState(), dummy).count;
			}
		}

		total = countStone + countDirt + countGravel + countCoal + countIron + countGold + countDiamond + countLapis + countRedstone + countRoots + countOreRoots;

		player.sendMessage(new TranslationTextComponent(getTranslationKey()).appendString("!"), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(TwilightForestMod.ID + ".ore_meter.range", radius, chunkX, chunkZ), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(Blocks.COAL_ORE.getTranslationKey()).appendString(" - " + countCoal + " " + percent(countCoal, total)), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(Blocks.IRON_ORE.getTranslationKey()).appendString(" - " + countIron + " " + percent(countIron, total)), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(Blocks.GOLD_ORE.getTranslationKey()).appendString(" - " + countGold + " " + percent(countGold, total)), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(Blocks.DIAMOND_ORE.getTranslationKey()).appendString(" - " + countDiamond + " " + percent(countDiamond, total) + ", ").append(new TranslationTextComponent(TwilightForestMod.ID + ".ore_meter.exposed", countExposedDiamond)), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(Blocks.LAPIS_ORE.getTranslationKey()).appendString(" - " + countLapis + " " + percent(countLapis, total)), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(Blocks.REDSTONE_ORE.getTranslationKey()).appendString(" - " + countRedstone + " " + percent(countRedstone, total)), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(TFBlocks.root.get().getTranslationKey()).appendString(" - " + countRoots + " " + percent(countRoots, total)), Util.DUMMY_UUID);
		player.sendMessage(new TranslationTextComponent(TFBlocks.liveroot_block.get().getTranslationKey()).appendString(" - " + countOreRoots + " " + percent(countOreRoots, total)), Util.DUMMY_UUID);
	}

	private String percent(int count, int total) {
		return (float) count / (float) total * 100F + "%";
	}

	private Map<BlockState, ScanResult> countBlocksInChunk(World world, int cx, int cz) {
		Map<BlockState, ScanResult> ret = new IdentityHashMap<>();
		BlockPos.Mutable pos = new BlockPos.Mutable();
		for (int x = cx << 4; x < (cx << 4) + 16; x++) {
			for (int z = cz << 4; z < (cz << 4) + 16; z++) {
				for (int y = 0; y < 256; y++) {
					BlockState state = world.getBlockState(pos.setPos(x, y, z));
					ScanResult res = ret.computeIfAbsent(state, s -> new ScanResult());
					res.count++;

					for (Direction e : Direction.values()) {
						if (world.isAirBlock(pos.setPos(x, y, z).move(e))) {
							res.exposedCount++;
							break;
						}
					}
				}
			}
		}

		return ret;
	}

	private static class ScanResult {
		int count;
		int exposedCount;
	}
}
