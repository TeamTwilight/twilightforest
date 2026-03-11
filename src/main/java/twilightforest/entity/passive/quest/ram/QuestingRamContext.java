package twilightforest.entity.passive.quest.ram;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.loot.TFLootTables;

import java.util.Map;

public record QuestingRamContext(Map<DyeColor, Ingredient> questItems, ResourceKey<LootTable> lootTable) {

	public static final QuestingRamContext FALLBACK = new QuestingRamContext(ImmutableMap.<DyeColor, Ingredient>builder()
		.put(DyeColor.WHITE, Ingredient.of(Items.WHITE_WOOL))
		.put(DyeColor.LIGHT_GRAY, Ingredient.of(Items.LIGHT_GRAY_WOOL))
		.put(DyeColor.GRAY, Ingredient.of(Items.GRAY_WOOL))
		.put(DyeColor.BLACK, Ingredient.of(Items.BLACK_WOOL))
		.put(DyeColor.RED, Ingredient.of(Items.RED_WOOL))
		.put(DyeColor.ORANGE, Ingredient.of(Items.ORANGE_WOOL))
		.put(DyeColor.YELLOW, Ingredient.of(Items.YELLOW_WOOL))
		.put(DyeColor.GREEN, Ingredient.of(Items.GREEN_WOOL))
		.put(DyeColor.LIME, Ingredient.of(Items.LIME_WOOL))
		.put(DyeColor.BLUE, Ingredient.of(Items.BLUE_WOOL))
		.put(DyeColor.CYAN, Ingredient.of(Items.CYAN_WOOL))
		.put(DyeColor.LIGHT_BLUE, Ingredient.of(Items.LIGHT_BLUE_WOOL))
		.put(DyeColor.PURPLE, Ingredient.of(Items.PURPLE_WOOL))
		.put(DyeColor.MAGENTA, Ingredient.of(Items.MAGENTA_WOOL))
		.put(DyeColor.PINK, Ingredient.of(Items.PINK_WOOL))
		.put(DyeColor.BROWN, Ingredient.of(Items.BROWN_WOOL)).build(),
		TFLootTables.QUESTING_RAM_REWARDS);

	public static final Codec<QuestingRamContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.unboundedMap(DyeColor.CODEC, Ingredient.CODEC_NONEMPTY).validate(QuestingRamContext::validate).fieldOf("items").forGetter(QuestingRamContext::questItems),
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("reward").forGetter(QuestingRamContext::lootTable)
	).apply(instance, QuestingRamContext::new));

	private static DataResult<Map<DyeColor, Ingredient>> validate(Map<DyeColor, Ingredient> map) {
		int colorFlags = 0;
		for (var color : map.keySet()) {
			colorFlags |= (1 << color.getId());
		}
		if (Integer.bitCount(colorFlags) == 16) {
			return DataResult.success(map);
		}
		return DataResult.error(() -> "Questing Ram quest must contain all 16 dye colors");
	}
}
