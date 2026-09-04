package twilightforest.util.multiparts;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Component;
import twilightforest.network.UpdateTFMultipartPacket;

@Component
public class MultipartEntityUtil {

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity.isMultipartEntity())
			PacketDistributor.sendToPlayersTrackingEntity(entity, new UpdateTFMultipartPacket(entity));
		return entity;
	}

}
