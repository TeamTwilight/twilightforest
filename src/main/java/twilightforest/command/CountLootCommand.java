package twilightforest.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@tamaized.beanification.Component
public class CountLootCommand {
	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("count_loot").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.argument("filter_structure", ResourceKeyArgument.key(Registries.STRUCTURE)).executes(this::countLoot)
				.then(Commands.argument("show_common", BoolArgumentType.bool()).executes(this::countLootFiltered))
			);
	}

	private int countLoot(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		return this.countLoot(context, true);
	}

	private int countLootFiltered(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		return this.countLoot(context, BoolArgumentType.getBool(context, "show_common"));
	}

	private int countLoot(CommandContext<CommandSourceStack> context, boolean showCommon) throws CommandSyntaxException {
		Holder.Reference<Structure> structure = ResourceKeyArgument.getStructure(context, "filter_structure");

		if (!structure.isBound()) return 0;

		CommandSourceStack source = context.getSource();
		ServerLevel level = source.getLevel();
		BlockPos commandPos = BlockPos.containing(source.getPosition());

		StructureStart structureAt = level.structureManager().getStructureAt(commandPos, structure.value());

		BoundingBox totalStructureBox = structureAt.getBoundingBox();

		int chunkMinX = SectionPos.blockToSectionCoord(totalStructureBox.minX());
		int chunkMinZ = SectionPos.blockToSectionCoord(totalStructureBox.minZ());
		int chunkMaxX = SectionPos.blockToSectionCoord(totalStructureBox.maxX());
		int chunkMaxZ = SectionPos.blockToSectionCoord(totalStructureBox.maxZ());

		List<BoundingBox> structureBoxes = structureAt.getPieces().stream().map(StructurePiece::getBoundingBox).toList();

		Object2IntMap<Item> lootCounts = new Object2IntOpenHashMap<>();

		for (int chunkIndexZ = chunkMinZ; chunkIndexZ <= chunkMaxZ; chunkIndexZ++) {
			for (int chunkIndexX = chunkMinX; chunkIndexX <= chunkMaxX; chunkIndexX++) {
				if (!level.hasChunk(chunkIndexX, chunkIndexZ))
					continue;
				for (Map.Entry<BlockPos, BlockEntity> posBE : level.getChunk(chunkIndexX, chunkIndexZ).getBlockEntities().entrySet()) {
					if (this.isInsideStructure(structureBoxes, posBE.getKey())) {
						this.countItemsInContainer(lootCounts, posBE.getValue(), showCommon);
					}
				}
			}
		}

		for (Object2IntMap.Entry<Item> countedItem : lootCounts.object2IntEntrySet().stream().sorted(Comparator.comparing(Object2IntMap.Entry::getIntValue)).toList()) {
			MutableComponent suffixCount = Component.literal(": " + countedItem.getIntValue()).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE));

			Item lootItem = countedItem.getKey();

			var rarityColor = new ItemStack(lootItem).getRarity().getStyleModifier();

			Component message = Component.translatable(lootItem.getDescriptionId()).withStyle(rarityColor).append(suffixCount);

			context.getSource().sendSystemMessage(message);
		}

		return lootCounts.values().intStream().sum();
	}

	private boolean isInsideStructure(List<BoundingBox> structureBoxes, BlockPos pos) {
		for (BoundingBox structureBox : structureBoxes) {
			if (structureBox.isInside(pos)) {
				return true;
			}
		}

		return false;
	}

	private void countItemsInContainer(Object2IntMap<Item> lootCounts, BlockEntity blockEntity, boolean showCommon) {
		if (!(blockEntity instanceof Container itemContainer))
			return;

		if (blockEntity instanceof RandomizableContainer randomizableContainer)
			randomizableContainer.unpackLootTable(null);

		int containerSize = itemContainer.getContainerSize();

		for (int slotIndex = 0; slotIndex < containerSize; slotIndex++) {
			ItemStack stack = itemContainer.getItem(slotIndex);

			if (stack.isEmpty() || (!showCommon && stack.getRarity() == Rarity.COMMON))
				continue;

			Item item = stack.getItem();

			lootCounts.put(item, lootCounts.getOrDefault(item, 0) + stack.getCount());
		}
	}
}
