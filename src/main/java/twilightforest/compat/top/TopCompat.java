package twilightforest.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TopCompat implements Function<ITheOneProbe, Void> {
	@Override
	public Void apply(ITheOneProbe api) {
		api.registerElementFactory(QuestRamWoolElement.Factory.INSTANCE);
		api.registerEntityProvider(TOPQuestRamWoolProvider.INSTANCE);
		api.registerProvider(ChiseledBookshelfSpawnProvider.INSTANCE);
		return null;
	}
}
