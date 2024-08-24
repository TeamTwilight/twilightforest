package twilightforest.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TopCompat implements Function<ITheOneProbe, Void> {
	@Override
	public Void apply(ITheOneProbe api) {
		api.registerElementFactory(QuestingRamWoolElement.Factory.INSTANCE);
		api.registerEntityProvider(TOPQuestingRamWoolProvider.INSTANCE);
		api.registerProvider(ChiseledBookshelfSpawnProvider.INSTANCE);
		return null;
	}
}
