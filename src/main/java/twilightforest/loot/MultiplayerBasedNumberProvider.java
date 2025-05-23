package twilightforest.loot;

import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFLoot;

import java.util.Set;

public record MultiplayerBasedNumberProvider(NumberProvider rollsPerPlayer, NumberProvider defaultRolls) implements NumberProvider {
	public static final MapCodec<MultiplayerBasedNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			NumberProviders.CODEC.fieldOf("per_player_rolls").forGetter(MultiplayerBasedNumberProvider::rollsPerPlayer),
			NumberProviders.CODEC.fieldOf("default_rolls").forGetter(MultiplayerBasedNumberProvider::defaultRolls))
		.apply(instance, MultiplayerBasedNumberProvider::new)
	);

	@Override
	public LootNumberProviderType getType() {
		return TFLoot.MULTIPLAYER_ROLLS.get();
	}

	public static MultiplayerBasedNumberProvider rollsForPlayers(NumberProvider rollsPerPlayer, NumberProvider defaultRolls) {
		return new MultiplayerBasedNumberProvider(rollsPerPlayer, defaultRolls);
	}

	@Override
	public float getFloat(LootContext context) {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()) {
			if (context.hasParameter(LootContextParams.THIS_ENTITY) && context.getParameter(LootContextParams.THIS_ENTITY).hasData(TFDataAttachments.MULTIPLAYER_FIGHT)) {
				int qualifiedPlayers = context.getParameter(LootContextParams.THIS_ENTITY).getData(TFDataAttachments.MULTIPLAYER_FIGHT).getQualifiedPlayers().size();
				float total = this.defaultRolls.getFloat(context);
				for (int i = 0; i < qualifiedPlayers - 1; i++) {
					total += Math.max(0, this.rollsPerPlayer.getFloat(context));
				}
				return total;
			}
		}
		return this.defaultRolls.getFloat(context);

	}

	/**
	 * Get the parameters used by this object.
	 */
	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return Sets.union(this.rollsPerPlayer.getReferencedContextParams(), this.defaultRolls.getReferencedContextParams());
	}
}
