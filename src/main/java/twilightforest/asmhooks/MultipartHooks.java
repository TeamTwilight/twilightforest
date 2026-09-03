package twilightforest.asmhooks;

import net.minecraft.world.entity.Entity;
import tamaized.beanification.Autowired;
import twilightforest.asm.transformers.multipart.SendDirtyEntityDataTransformer;
import twilightforest.util.multiparts.MultipartEntityUtil;

@SuppressWarnings({"JavadocReference", "unused"})
public class MultipartHooks {

	@Autowired
	private static MultipartEntityUtil multipartEntityUtil;

	/**
	 * {@link SendDirtyEntityDataTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.server.level.ServerEntity#sendDirtyEntityData}
	 */
	public static Entity sendDirtyEntityData(Entity entity) {
		return multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}

}
