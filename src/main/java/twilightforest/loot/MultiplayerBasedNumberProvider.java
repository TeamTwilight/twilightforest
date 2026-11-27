package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

public record MultiplayerBasedNumberProvider(ContextFloatProvider rollsPerPlayer, ContextFloatProvider defaultRolls) implements ContextIntProvider {
	public static final MapCodec<MultiplayerBasedNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextFloatProviders.DIRECT_CODEC.fieldOf("per_player_rolls").forGetter(MultiplayerBasedNumberProvider::rollsPerPlayer),
			ContextFloatProviders.DIRECT_CODEC.fieldOf("default_rolls").forGetter(MultiplayerBasedNumberProvider::defaultRolls))
		.apply(instance, MultiplayerBasedNumberProvider::new)
	);

	@Override
	public MapCodec<? extends ContextIntProvider> codec() {
		return MAP_CODEC;
	}

	public static MultiplayerBasedNumberProvider rollsForPlayers(Holder<ContextFloatProvider> rollsPerPlayer, Holder<ContextFloatProvider> defaultRolls) {
		return new MultiplayerBasedNumberProvider(rollsPerPlayer.value(), defaultRolls.value());
	}

	@Override
	public int getIntUnsafe(LootContext lootContext) throws ArithmeticException {
		float total = this.defaultRolls.getFloat(lootContext);
		if (TFConfig.multiplayerFightAdjuster.adjustsLootRolls()) {
			if (lootContext.getOptional(LootContextParams.THIS_ENTITY) instanceof Entity entity && entity.hasData(TFDataAttachments.MULTIPLAYER_FIGHT)) {
				int qualifiedPlayers = entity.getData(TFDataAttachments.MULTIPLAYER_FIGHT).getQualifiedPlayers().size();
				for (int i = 0; i < qualifiedPlayers - 1; i++) {
					total += Math.max(0, this.rollsPerPlayer.getFloat(lootContext));
				}
			}
		}
		return Math.round(total);
	}

	@Override
	public void validate(ValidationContext validationContext) {
		this.rollsPerPlayer.validate(validationContext);
		this.defaultRolls.validate(validationContext);
	}
}
