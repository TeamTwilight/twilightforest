package twilightforest.util;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.TriConsumer;
import twilightforest.TFRegistries;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Optional;

public record Enforcement(TriConsumer<Player, ServerLevel, Restriction> consumer) {

	public static void enforceBiomeProgression(Player player, ServerLevel level) {
		Restriction.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player).ifPresent(restriction -> {
			Optional<Holder.Reference<Enforcement>> enforcement = TFRegistries.ENFORCEMENT.get(restriction.enforcement().identifier());
			if (enforcement.isPresent()) {
				enforcement.get().value().consumer().accept(player, level, restriction);
				if (restriction.hintStructureKey() != null) {
					StructureHints.tryHintForStructure(player, level, restriction.hintStructureKey());
				}
			}
		});
	}
}
