package twilightforest.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.TriConsumer;
import twilightforest.TFRegistries;
import twilightforest.world.components.structures.util.StructureHints;

public record Enforcement(TriConsumer<Player, ServerLevel, Restriction> consumer) {

	public static void enforceBiomeProgression(Player player, ServerLevel level) {
		Restriction.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player).ifPresent(restriction -> {
			Enforcement enforcement = TFRegistries.ENFORCEMENT.getValue(restriction.enforcement().location());
			if (enforcement != null) {
				enforcement.consumer().accept(player, level, restriction);
				if (restriction.hintStructureKey() != null) {
					StructureHints.tryHintForStructure(player, level, restriction.hintStructureKey());
				}
			}
		});
	}
}
