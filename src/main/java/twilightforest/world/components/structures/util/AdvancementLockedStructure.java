package twilightforest.world.components.structures.util;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import twilightforest.util.PlayerHelper;

import java.util.List;

public interface AdvancementLockedStructure {

	String CODEC_NAME = "advancements_required";

	default boolean doesPlayerHaveRequiredAdvancements(Player player) {
		return PlayerHelper.playerHasRequiredAdvancements(player, this.getRequiredAdvancements());
	}

	List<Identifier> getRequiredAdvancements();

	record AdvancementLockConfig(List<Identifier> requiredAdvancements) {
		public static final Codec<AdvancementLockConfig> CODEC = Identifier.CODEC.listOf().xmap(AdvancementLockConfig::new, AdvancementLockConfig::requiredAdvancements);
	}
}
