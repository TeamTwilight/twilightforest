package twilightforest.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFDataAttachments;

import java.util.HashMap;
import java.util.Map;

public class GiantToolGroupingModifier extends LootModifier {
	public static Map<Block, Item> CONVERSIONS = new HashMap<>(); // Map of block-to-giant block conversions. Supposed to be similar to vanilla's AxeItem.STRIPPABLES Map

	public static final MapCodec<GiantToolGroupingModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).apply(inst, GiantToolGroupingModifier::new));

	public GiantToolGroupingModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (context.getParam(LootContextParams.THIS_ENTITY) instanceof Player player) {
			if (!generatedLoot.isEmpty() && generatedLoot.getFirst().getItem() instanceof BlockItem block) {
				if (CONVERSIONS.containsKey(block.getBlock())) { // Should be true but let's double-check
					var attachment = player.getData(TFDataAttachments.GIANT_PICKAXE_MINING);
					int blockConversion = attachment.getGiantBlockConversion(); // Get how many conversions are left
					attachment.setGiantBlockConversion(blockConversion - 1);
					if (blockConversion == 64)
						return ObjectArrayList.of(new ItemStack(CONVERSIONS.get(block.getBlock()))); // If it's the first conversion, drop our giant block
					else return new ObjectArrayList<>(); // Drop nothing, all gets converted into giant block
				}
			}
		}
		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return GiantToolGroupingModifier.CODEC;
	}
}
