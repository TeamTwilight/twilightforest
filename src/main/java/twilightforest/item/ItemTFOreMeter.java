package twilightforest.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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
		int useX = MathHelper.floor(player.getX());
		int useZ = MathHelper.floor(player.getZ());

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

		int total = 0;

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
				countOreRoots += results.getOrDefault(TFBlocks.liveroot.get().getDefaultState(), dummy).count;
			}
		}

		total = countStone + countDirt + countGravel + countCoal + countIron + countGold + countDiamond + countLapis + countRedstone + countRoots + countOreRoots;

		player.sendMessage(new TranslationTextComponent(getTranslationKey() + ".name").appendText("!"));
		player.sendMessage(new TranslationTextComponent(TwilightForestMod.ID + ".ore_meter.range", radius, chunkX, chunkZ));
		player.sendMessage(new TranslationTextComponent(Blocks.COAL_ORE.getTranslationKey() + ".name").appendText(" - " + countCoal + " " + percent(countCoal, total)));
		player.sendMessage(new TranslationTextComponent(Blocks.IRON_ORE.getTranslationKey() + ".name").appendText(" - " + countIron + " " + percent(countIron, total)));
		player.sendMessage(new TranslationTextComponent(Blocks.GOLD_ORE.getTranslationKey() + ".name").appendText(" - " + countGold + " " + percent(countGold, total)));
		player.sendMessage(new TranslationTextComponent(Blocks.DIAMOND_ORE.getTranslationKey() + ".name").appendText(" - " + countDiamond + " " + percent(countDiamond, total) + ", ").appendSibling(new TranslationTextComponent(TwilightForestMod.ID + ".ore_meter.exposed", countExposedDiamond)));
		player.sendMessage(new TranslationTextComponent(Blocks.LAPIS_ORE.getTranslationKey() + ".name").appendText(" - " + countLapis + " " + percent(countLapis, total)));
		player.sendMessage(new TranslationTextComponent(Blocks.REDSTONE_ORE.getTranslationKey() + ".name").appendText(" - " + countRedstone + " " + percent(countRedstone, total)));
		player.sendMessage(new TranslationTextComponent(TFBlocks.root.get().getTranslationKey() + ".name").appendText(" - " + countRoots + " " + percent(countRoots, total)));
		player.sendMessage(new TranslationTextComponent(TFBlocks.liveroot.get().getTranslationKey() + ".name").appendText(" - " + countOreRoots + " " + percent(countOreRoots, total)));
	}

	private String percent(int count, int total) {
		return Float.toString((float) count / (float) total * 100F) + "%";
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
