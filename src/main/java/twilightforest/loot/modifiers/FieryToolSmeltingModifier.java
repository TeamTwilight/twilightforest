package twilightforest.loot.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class FieryToolSmeltingModifier extends LootModifier {
	public static final MapCodec<FieryToolSmeltingModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).apply(inst, FieryToolSmeltingModifier::new));

	public FieryToolSmeltingModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

		List<Pair<ItemStack, Float>> list = generatedLoot.stream().map(stack -> {
			var input = new SingleRecipeInput(stack);
			return context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, input, context.getLevel())
				.map(holder -> {
					ItemStack result = holder.value().assemble(input, context.getLevel().registryAccess()).copy();
					result.setCount(stack.getCount() * result.getCount());
					return Pair.of(result, holder.value().experience());
				})
				.filter(pair -> !pair.getLeft().isEmpty())
				.orElse(Pair.of(stack, 0.0F));
		}).toList();

		float xp = (float) list.stream().mapToDouble(Pair::getRight).sum();
		if (xp > 0.0F && context.getOptionalParameter(LootContextParams.THIS_ENTITY) != null) {
			ExperienceOrb.award(context.getLevel(), context.getParameter(LootContextParams.THIS_ENTITY).position(), Math.round(xp));
		}

		return list.stream().map(Pair::getLeft).collect(Collectors.toCollection(ObjectArrayList::new));
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return FieryToolSmeltingModifier.CODEC;
	}
}
