package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFLoot;

import java.util.List;

public class MultiplayerBasedAdditionLootFunction extends LootItemConditionalFunction {
	public static final MapCodec<MultiplayerBasedAdditionLootFunction> CODEC = RecordCodecBuilder.mapCodec(
		p_298131_ -> commonFields(p_298131_)
			.and(NumberProviders.CODEC.fieldOf("extra_count_per_player").forGetter(o -> o.value))
			.apply(p_298131_, MultiplayerBasedAdditionLootFunction::new)
	);

	private final NumberProvider value;

	public MultiplayerBasedAdditionLootFunction(List<LootItemCondition> predicates, NumberProvider value) {
		super(predicates);
		this.value = value;
	}

	public static MultiplayerBasedAdditionLootFunction.Builder addForAllParticipatingPlayers(NumberProvider additionPerPlayer) {
		return new MultiplayerBasedAdditionLootFunction.Builder(additionPerPlayer);
	}

	@Override
	public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
		return TFLoot.MULTIPLAYER_MULTIPLIER.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()) {
			if (context.hasParameter(LootContextParams.THIS_ENTITY) && context.getParameter(LootContextParams.THIS_ENTITY).hasData(TFDataAttachments.MULTIPLAYER_FIGHT)) {
				int qualifiedPlayers = context.getParameter(LootContextParams.THIS_ENTITY).getData(TFDataAttachments.MULTIPLAYER_FIGHT).getQualifiedPlayers().size();
				if (qualifiedPlayers > 1) {
					int participatingPlayers = qualifiedPlayers - 1;
					int extraItems = this.value.getInt(context) * participatingPlayers;
					stack.setCount(Mth.clamp(stack.getCount() + extraItems, 0, stack.getMaxStackSize()));
					TwilightForestMod.LOGGER.debug("{} extra players participated in a fight against {}, dropping {} extra {} for a total of {}.", participatingPlayers, context.getParameter(LootContextParams.THIS_ENTITY).getType().getDescription().getString(), extraItems, stack.getItem().getName().getString(), stack.getCount());
				}
			}
		}
		return stack;
	}

	public static class Builder extends LootItemConditionalFunction.Builder<MultiplayerBasedAdditionLootFunction.Builder> {
		private final NumberProvider count;

		public Builder(NumberProvider pLootingMultiplier) {
			this.count = pLootingMultiplier;
		}

		@Override
		protected MultiplayerBasedAdditionLootFunction.Builder getThis() {
			return this;
		}

		@Override
		public MultiplayerBasedAdditionLootFunction build() {
			return new MultiplayerBasedAdditionLootFunction(this.getConditions(), this.count);
		}
	}
}
