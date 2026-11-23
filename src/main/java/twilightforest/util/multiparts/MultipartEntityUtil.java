package twilightforest.util.multiparts;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;
import twilightforest.network.UpdateTFMultipartPacket;

import java.util.Iterator;

@Component
public class MultipartEntityUtil {

	public Iterator<Entity> injectTFPartEntities(Iterator<Entity> iter) {
		return new MultipartEntityIteratorWrapper(iter);
	}

	@Nullable
	public EntityRenderer<?> tryLookupTFPartRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		if (entity instanceof TFPart<?> part)
			return BakedMultiPartRenderers.lookup(part.renderer());
		return renderer;
	}

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity.isMultipartEntity()) {
			UpdateTFMultipartPacket packet = UpdateTFMultipartPacket.delta(entity);
			if (packet != null)
				PacketDistributor.sendToPlayersTrackingEntity(entity, packet);
		}
		return entity;
	}

	/** Gives a new tracker the full part state; regular updates only carry what changed. */
	public static void sendFullMultipartDataOnStartTracking(PlayerEvent.StartTracking event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getTarget().isMultipartEntity()) {
			UpdateTFMultipartPacket packet = UpdateTFMultipartPacket.full(event.getTarget());
			if (packet != null)
				PacketDistributor.sendToPlayer(player, packet);
		}
	}

}
