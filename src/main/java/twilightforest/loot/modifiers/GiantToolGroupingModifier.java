package twilightforest.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.components.entity.GiantPickaxeMiningAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.item.GiantPickItem;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class GiantToolGroupingModifier extends LootModifier {
	public static Map<Block, Item> CONVERSIONS = new HashMap<>(); // Map of block-to-giant block conversions. Supposed to be similar to vanilla's AxeItem.STRIPPABLES Map

	public static final MapCodec<GiantToolGroupingModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).apply(inst, GiantToolGroupingModifier::new));

	public GiantToolGroupingModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof Player player) {
			BlockState state = context.getParameter(LootContextParams.BLOCK_STATE);
			if (CONVERSIONS.containsKey(state.getBlock())) { // Should be true but let's double-check
				var attachment = player.getData(TFDataAttachments.GIANT_PICKAXE_MINING);
				int blockConversion = attachment.getGiantBlockConversion(); // Get how many conversions are left
				attachment.setGiantBlockConversion(blockConversion - 1);
				if (blockConversion == 64)
					return ObjectArrayList.of(new ItemStack(CONVERSIONS.get(state.getBlock()))); // If it's the first conversion, drop our giant block
				else return new ObjectArrayList<>(); // Drop nothing, all gets converted into giant block
			}
		}
		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return GiantToolGroupingModifier.CODEC;
	}

	@SubscribeEvent
	public static void breakBlock(BlockEvent.BreakEvent event) {
		BlockPos pos = event.getPos();
		BlockState state = event.getState();

		if (event.getPlayer() instanceof ServerPlayer player && canHarvestWithGiantPick(player, state, pos)) {
			var attachment = player.getData(TFDataAttachments.GIANT_PICKAXE_MINING);

			if (shouldBreakGiantBlock(player, attachment)) {
				attachment.setBreaking(true); // Tell the capability that a block breaking loop is happening, so it knows to fail the if check above. Otherwise, this would go on forever

				boolean allTheSame = CONVERSIONS.containsKey(state.getBlock()); // If the blockstate is convertible, check if the rest are too
				for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
					if (allTheSame && !player.level().getBlockState(offsetPos).is(state.getBlock())) allTheSame = false;
				}
				attachment.setGiantBlockConversion(allTheSame ? 64 : 0); // NO IN-BETWEEN! Either the whole 64 get converted, or none do

				event.setCanceled(true); // We cancel this event, since we want to break the block we're looking at first
				player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				player.gameMode.destroyBlock(pos); // Brake the block we broke, for real this time

				// Break all the other blocks, if they're the same type
				for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
					if (!offsetPos.equals(pos) && player.level().getBlockState(offsetPos).is(state.getBlock())) {
						BlockPos newPos = new BlockPos(offsetPos); // This feels dumb, but without it, the client thinks the last block in the iterator is broken too
						player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(player.level().getBlockState(newPos)));
						player.gameMode.destroyBlock(newPos);
					}
				}

				attachment.setBreaking(false); // Tell the capability that the loop is over, and all is good in the world
			}
		}
	}

	private static boolean canHarvestWithGiantPick(Player player, BlockState state, BlockPos pos) {
		return player.getMainHandItem().getItem() instanceof GiantPickItem && EventHooks.doPlayerHarvestCheck(player, state, player.level(), pos);
	}

	private static boolean shouldBreakGiantBlock(Player player, GiantPickaxeMiningAttachment attachment) {
		return attachment.getMining() == player.level().getGameTime() && !attachment.getBreaking();
	}
}
