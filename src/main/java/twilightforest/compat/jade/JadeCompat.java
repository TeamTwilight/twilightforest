package twilightforest.compat.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.entity.passive.QuestRam;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(QuestRamWoolProvider.INSTANCE, QuestRam.class);
		registration.registerBlockComponent(ChiseledBookshelfSpawnProvider.INSTANCE, ChiseledCanopyShelfBlock.class);
	}
}
