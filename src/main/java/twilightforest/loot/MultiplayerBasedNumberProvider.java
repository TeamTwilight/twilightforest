package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

public record MultiplayerBasedNumberProvider(ContextFloatProvider rollsPerPlayer, ContextFloatProvider defaultRolls) implements ContextFloatProvider {
	public static final MapCodec<MultiplayerBasedNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextFloatProviders.DIRECT_CODEC.fieldOf("per_player_rolls").forGetter(MultiplayerBasedNumberProvider::rollsPerPlayer),
			ContextFloatProviders.DIRECT_CODEC.fieldOf("default_rolls").forGetter(MultiplayerBasedNumberProvider::defaultRolls))
		.apply(instance, MultiplayerBasedNumberProvider::new)
	);

	@Override
	public MapCodec<? extends ContextFloatProvider> codec() {
		return MAP_CODEC;
	}

	public static MultiplayerBasedNumberProvider rollsForPlayers(ContextFloatProvider rollsPerPlayer, ContextFloatProvider defaultRolls) {
		return new MultiplayerBasedNumberProvider(rollsPerPlayer, defaultRolls);
	}

	@Override
	public float getFloatUnsafe(LootContext lootContext) throws ArithmeticException {
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()) {
			if (lootContext.getOptional(LootContextParams.THIS_ENTITY) instanceof Entity entity && entity.hasData(TFDataAttachments.MULTIPLAYER_FIGHT)) {
				int qualifiedPlayers = entity.getData(TFDataAttachments.MULTIPLAYER_FIGHT).getQualifiedPlayers().size();
				float total = this.defaultRolls.getFloat(lootContext);
				for (int i = 0; i < qualifiedPlayers - 1; i++) {
					total += Math.max(0, this.rollsPerPlayer.getFloat(lootContext));
				}
				return total;
			}
		}
		return this.defaultRolls.getFloat(lootContext);
	}

	@Override
	public void validate(ValidationContext validationContext) {
		this.rollsPerPlayer.validate(validationContext);
		this.defaultRolls.validate(validationContext);
	}
}
