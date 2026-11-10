package twilightforest.entity.passive.quest.ram;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.loot.TFLootTables;

import java.util.HashMap;
import java.util.Map;

public record QuestingRamContext(Map<DyeColor, Ingredient> questItems, ResourceKey<LootTable> lootTable) {

	public static final QuestingRamContext FALLBACK = new QuestingRamContext(ImmutableMap.<DyeColor, Ingredient>builder()
		.put(DyeColor.WHITE, Ingredient.of(Items.WOOL.white()))
		.put(DyeColor.LIGHT_GRAY, Ingredient.of(Items.WOOL.lightGray()))
		.put(DyeColor.GRAY, Ingredient.of(Items.WOOL.gray()))
		.put(DyeColor.BLACK, Ingredient.of(Items.WOOL.black()))
		.put(DyeColor.RED, Ingredient.of(Items.WOOL.red()))
		.put(DyeColor.ORANGE, Ingredient.of(Items.WOOL.orange()))
		.put(DyeColor.YELLOW, Ingredient.of(Items.WOOL.yellow()))
		.put(DyeColor.GREEN, Ingredient.of(Items.WOOL.green()))
		.put(DyeColor.LIME, Ingredient.of(Items.WOOL.lime()))
		.put(DyeColor.BLUE, Ingredient.of(Items.WOOL.blue()))
		.put(DyeColor.CYAN, Ingredient.of(Items.WOOL.cyan()))
		.put(DyeColor.LIGHT_BLUE, Ingredient.of(Items.WOOL.lightBlue()))
		.put(DyeColor.PURPLE, Ingredient.of(Items.WOOL.purple()))
		.put(DyeColor.MAGENTA, Ingredient.of(Items.WOOL.magenta()))
		.put(DyeColor.PINK, Ingredient.of(Items.WOOL.pink()))
		.put(DyeColor.BROWN, Ingredient.of(Items.WOOL.brown())).build(),
		TFLootTables.QUESTING_RAM_REWARDS);

	public static final Codec<QuestingRamContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.unboundedMap(DyeColor.CODEC, Ingredient.CODEC).validate(QuestingRamContext::validate).fieldOf("items").forGetter(QuestingRamContext::questItems),
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("reward").forGetter(QuestingRamContext::lootTable)
	).apply(instance, QuestingRamContext::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, QuestingRamContext> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.map(HashMap::new, DyeColor.STREAM_CODEC, Ingredient.CONTENTS_STREAM_CODEC), QuestingRamContext::questItems,
		ResourceKey.streamCodec(Registries.LOOT_TABLE), QuestingRamContext::lootTable,
		QuestingRamContext::new
	);

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
