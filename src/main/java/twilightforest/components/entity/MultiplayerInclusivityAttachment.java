package twilightforest.components.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.init.TFAdvancements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiplayerInclusivityAttachment {

	private final List<ServerPlayer> qualifiedPlayers = new ArrayList<>();

	public List<ServerPlayer> getQualifiedPlayers() {
		return Collections.unmodifiableList(this.qualifiedPlayers);
	}

	public void maybeAddQualifiedPlayer(Entity entity) {
		if (entity instanceof ServerPlayer player && !this.getQualifiedPlayers().contains(player)) {
			this.qualifiedPlayers.add(player);
		}
	}

	public void grantGroupAdvancement(LivingEntity boss) {
		for (ServerPlayer player : this.getQualifiedPlayers()) {
			TFAdvancements.HURT_BOSS.get().trigger(player, boss);
		}
	}
}
