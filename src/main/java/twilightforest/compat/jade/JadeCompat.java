package twilightforest.compat.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.entity.passive.QuestingRam;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(QuestingRamWoolProvider.INSTANCE, QuestingRam.class);
		registration.registerBlockComponent(ChiseledBookshelfSpawnProvider.INSTANCE, ChiseledCanopyShelfBlock.class);
	}
}
