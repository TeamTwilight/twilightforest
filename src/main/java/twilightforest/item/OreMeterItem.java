package twilightforest.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import javax.annotation.Nonnull;
import java.util.IdentityHashMap;
import java.util.Map;

public class OreMeterItem extends Item {

	public OreMeterItem(Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
		int useX = Mth.floor(player.getX());
		int useZ = Mth.floor(player.getZ());

		if (!level.isClientSide()) {
			this.countOreInArea(player, level, useX, useZ, 3);
		}

		return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
	}

	private void countOreInArea(Player player, Level world, int useX, int useZ, int radius) {
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
		int countCopper = 0;
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
				countGravel += results.getOrDefault(Blocks.GRAVEL.defaultBlockState(), dummy).count;

				countCoal += results.getOrDefault(Blocks.COAL_ORE.defaultBlockState(), dummy).count;
				countIron += results.getOrDefault(Blocks.IRON_ORE.defaultBlockState(), dummy).count;
				countGold += results.getOrDefault(Blocks.GOLD_ORE.defaultBlockState(), dummy).count;
				countDiamond += results.getOrDefault(Blocks.DIAMOND_ORE.defaultBlockState(), dummy).count;
				countLapis += results.getOrDefault(Blocks.LAPIS_ORE.defaultBlockState(), dummy).count;
				countRedstone += results.getOrDefault(Blocks.REDSTONE_ORE.defaultBlockState(), dummy).count;
				countCopper += results.getOrDefault(Blocks.COPPER_ORE.defaultBlockState(), dummy).count;
				countExposedDiamond += results.getOrDefault(Blocks.DIAMOND_ORE.defaultBlockState(), dummy).exposedCount;

				countRoots += results.getOrDefault(TFBlocks.ROOT_BLOCK.get().defaultBlockState(), dummy).count;
				countOreRoots += results.getOrDefault(TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState(), dummy).count;
			}
		}

		total = countStone + countDirt + countGravel + countCoal + countIron + countGold + countDiamond + countLapis + countRedstone + countCopper + countRoots + countOreRoots;

		player.sendSystemMessage(Component.translatable(this.getDescriptionId()).append("!"));
		player.sendSystemMessage(Component.translatable("misc.twilightforest.ore_meter_range", radius, chunkX, chunkZ));
		player.sendSystemMessage(Component.translatable(Blocks.COAL_ORE.getDescriptionId()).append(" - " + countCoal + " " + percent(countCoal, total)));
		player.sendSystemMessage(Component.translatable(Blocks.IRON_ORE.getDescriptionId()).append(" - " + countIron + " " + percent(countIron, total)));
		player.sendSystemMessage(Component.translatable(Blocks.GOLD_ORE.getDescriptionId()).append(" - " + countGold + " " + percent(countGold, total)));
		player.sendSystemMessage(Component.translatable(Blocks.DIAMOND_ORE.getDescriptionId()).append(" - " + countDiamond + " " + percent(countDiamond, total) + ", ").append(Component.translatable("misc.twilightforest.ore_meter_exposed", countExposedDiamond)));
		player.sendSystemMessage(Component.translatable(Blocks.LAPIS_ORE.getDescriptionId()).append(" - " + countLapis + " " + percent(countLapis, total)));
		player.sendSystemMessage(Component.translatable(Blocks.REDSTONE_ORE.getDescriptionId()).append(" - " + countRedstone + " " + percent(countRedstone, total)));
		player.sendSystemMessage(Component.translatable(Blocks.COPPER_ORE.getDescriptionId()).append(" - " + countCopper + " " + percent(countCopper, total)));
		player.sendSystemMessage(Component.translatable(TFBlocks.ROOT_BLOCK.get().getDescriptionId()).append(" - " + countRoots + " " + percent(countRoots, total)));
		player.sendSystemMessage(Component.translatable(TFBlocks.LIVEROOT_BLOCK.get().getDescriptionId()).append(" - " + countOreRoots + " " + percent(countOreRoots, total)));
	}

	private String percent(int count, int total) {
		return (float) count / (float) total * 100F + "%";
	}

	private Map<BlockState, ScanResult> countBlocksInChunk(Level level, int cx, int cz) {
		Map<BlockState, ScanResult> ret = new IdentityHashMap<>();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int x = cx << 4; x < (cx << 4) + 16; x++) {
			for (int z = cz << 4; z < (cz << 4) + 16; z++) {
				for (int y = 0; y < 256; y++) {
					BlockState state = level.getBlockState(pos.set(x, y, z));
					ScanResult res = ret.computeIfAbsent(state, s -> new ScanResult());
					res.count++;

					for (Direction e : Direction.values()) {
						if (level.isEmptyBlock(pos.set(x, y, z).move(e))) {
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