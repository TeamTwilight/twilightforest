package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

import java.util.Optional;

public class MultiplayerBasedAdditionLootFunction extends LootItemConditionalFunction {
	public static final MapCodec<MultiplayerBasedAdditionLootFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
		instance -> commonFields(instance)
			.and(ContextIntProviders.DIRECT_CODEC.fieldOf("extra_count_per_player").forGetter(o -> o.value))
			.apply(instance, MultiplayerBasedAdditionLootFunction::new)
	);

	private final ContextIntProvider value;

	public MultiplayerBasedAdditionLootFunction(Optional<Holder<LootItemCondition>> condition, ContextIntProvider value) {
		super(condition);
		this.value = value;
	}

	public static MultiplayerBasedAdditionLootFunction.Builder addForAllParticipatingPlayers(ContextIntProvider additionPerPlayer) {
		return new MultiplayerBasedAdditionLootFunction.Builder(additionPerPlayer);
	}

	@Override
	public MapCodec<? extends LootItemConditionalFunction> codec() {
		return MAP_CODEC;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()) {
			if (context.getOptional(LootContextParams.THIS_ENTITY) instanceof Entity entity && entity.hasData(TFDataAttachments.MULTIPLAYER_FIGHT)) {
				int qualifiedPlayers = entity.getData(TFDataAttachments.MULTIPLAYER_FIGHT).getQualifiedPlayers().size();
				if (qualifiedPlayers > 1) {
					int participatingPlayers = qualifiedPlayers - 1;
					int extraItems = this.value.getInt(context) * participatingPlayers;
					stack.setCount(Mth.clamp(stack.getCount() + extraItems, 0, stack.getMaxStackSize()));
					TwilightForestMod.LOGGER.debug("{} extra players participated in a fight against {}, dropping {} extra {} for a total of {}.", participatingPlayers, entity.getType().getDescription().getString(), extraItems, stack.getItemName().getString(), stack.getCount());
				}
			}
		}
		return stack;
	}

	public static class Builder extends LootItemConditionalFunction.Builder<MultiplayerBasedAdditionLootFunction.Builder> {
		private final ContextIntProvider count;

		public Builder(ContextIntProvider pLootingMultiplier) {
			this.count = pLootingMultiplier;
		}

		@Override
		protected MultiplayerBasedAdditionLootFunction.Builder getThis() {
			return this;
		}

		@Override
		public MultiplayerBasedAdditionLootFunction build() {
			return new MultiplayerBasedAdditionLootFunction(this.getCondition(), this.count);
		}
	}
}
