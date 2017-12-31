package twilightforest.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFRoots;
import twilightforest.block.TFBlocks;
import twilightforest.enums.RootVariant;

import javax.annotation.Nonnull;
import java.util.IdentityHashMap;
import java.util.Map;

public class ItemTFOreMeter extends ItemTF {

	protected ItemTFOreMeter() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		int useX = MathHelper.floor(player.posX);
		int useZ = MathHelper.floor(player.posZ);

		if (!world.isRemote) {
			countOreInArea(player, world, useX, useZ, 3);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	private void countOreInArea(EntityPlayer player, World world, int useX, int useZ, int radius) {
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
				Map<IBlockState, ScanResult> results = countBlocksInChunk(world, chunkX, chunkZ);

				countStone += results.entrySet().stream().filter(e -> e.getKey().getBlock() == Blocks.STONE).mapToInt(e -> e.getValue().count).sum();
				countDirt += results.entrySet().stream().filter(e -> e.getKey().getBlock() == Blocks.DIRT).mapToInt(e -> e.getValue().count).sum();
				countGravel += results.getOrDefault(Blocks.GRAVEL.getDefaultState(), dummy).count;

				countCoal += results.getOrDefault(Blocks.COAL_ORE.getDefaultState(), dummy).count;
				countIron += results.getOrDefault(Blocks.IRON_ORE.getDefaultState(), dummy).count;
				countGold += results.getOrDefault(Blocks.GOLD_ORE.getDefaultState(), dummy).count;
				countDiamond += results.getOrDefault(Blocks.DIAMOND_ORE.getDefaultState(), dummy).count;
				countLapis += results.getOrDefault(Blocks.LAPIS_ORE.getDefaultState(), dummy).count;
				countRedstone += results.getOrDefault(Blocks.REDSTONE_ORE.getDefaultState(), dummy).count + results.getOrDefault(Blocks.LIT_REDSTONE_ORE.getDefaultState(), dummy).count;
				countExposedDiamond += results.getOrDefault(Blocks.DIAMOND_ORE.getDefaultState(), dummy).exposedCount;

				countRoots += results.getOrDefault(TFBlocks.root.getDefaultState().withProperty(BlockTFRoots.VARIANT, RootVariant.ROOT), dummy).count;
				countOreRoots += results.getOrDefault(TFBlocks.root.getDefaultState().withProperty(BlockTFRoots.VARIANT, RootVariant.LIVEROOT), dummy).count;
			}
		}

		total = countStone + countDirt + countGravel + countCoal + countIron + countGold + countDiamond + countLapis + countRedstone + countRoots + countOreRoots;

		player.sendMessage(new TextComponentTranslation(getUnlocalizedName() + ".name").appendText("!"));
		player.sendMessage(new TextComponentTranslation(TwilightForestMod.ID + ".ore_meter.range", radius, chunkX, chunkZ));
		player.sendMessage(new TextComponentTranslation(Blocks.COAL_ORE.getUnlocalizedName() + ".name").appendText(" - " + countCoal + " " + percent(countCoal, total)));
		player.sendMessage(new TextComponentTranslation(Blocks.IRON_ORE.getUnlocalizedName() + ".name").appendText(" - " + countIron + " " + percent(countIron, total)));
		player.sendMessage(new TextComponentTranslation(Blocks.GOLD_ORE.getUnlocalizedName() + ".name").appendText(" - " + countGold + " " + percent(countGold, total)));
		player.sendMessage(new TextComponentTranslation(Blocks.DIAMOND_ORE.getUnlocalizedName() + ".name").appendText(" - " + countDiamond + " " + percent(countDiamond, total) + ", ").appendSibling(new TextComponentTranslation(TwilightForestMod.ID + ".ore_meter.exposed", countExposedDiamond)));
		player.sendMessage(new TextComponentTranslation(Blocks.LAPIS_ORE.getUnlocalizedName() + ".name").appendText(" - " + countLapis + " " + percent(countLapis, total)));
		player.sendMessage(new TextComponentTranslation(Blocks.REDSTONE_ORE.getUnlocalizedName() + ".name").appendText(" - " + countRedstone + " " + percent(countRedstone, total)));
		player.sendMessage(new TextComponentTranslation(new ItemStack(TFBlocks.root).getUnlocalizedName() + ".name").appendText(" - " + countRoots + " " + percent(countRoots, total)));
		player.sendMessage(new TextComponentTranslation(new ItemStack(TFBlocks.root, 1, 1).getUnlocalizedName() + ".name").appendText(" - " + countOreRoots + " " + percent(countOreRoots, total)));
	}

	private String percent(int count, int total) {
		return Float.toString((float) count / (float) total * 100F) + "%";
	}

	private Map<IBlockState, ScanResult> countBlocksInChunk(World world, int cx, int cz) {
		Map<IBlockState, ScanResult> ret = new IdentityHashMap<>();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int x = cx << 4; x < (cx << 4) + 16; x++) {
			for (int z = cz << 4; z < (cz << 4) + 16; z++) {
				for (int y = 0; y < 256; y++) {
					IBlockState state = world.getBlockState(pos.setPos(x, y, z));
					ScanResult res = ret.computeIfAbsent(state, s -> new ScanResult());
					res.count++;

					for (EnumFacing e : EnumFacing.VALUES) {
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
