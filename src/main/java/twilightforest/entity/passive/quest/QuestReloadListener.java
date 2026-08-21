package twilightforest.entity.passive.quest;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Autowired;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

import java.util.Map;

public class QuestReloadListener extends SimpleJsonResourceReloadListener<QuestingRamContext> {

	@Autowired
	private static QuestingRamCurrentContext questingRamCurrentContext;

	public QuestReloadListener() {
		super(QuestingRamContext.CODEC, FileToIdConverter.json("twilight/quests"));
	}

	@Override
	protected void apply(Map<Identifier, QuestingRamContext> object, ResourceManager resourceManager, ProfilerFiller profiler) {
		boolean found = false;
		for (var entry : object.entrySet()) {
			if (entry.getKey().getPath().equals("questing_ram")) {
				questingRamCurrentContext.setContext(entry.getValue());
				TwilightForestMod.LOGGER.debug("Questing Ram quest set by mod {}", entry.getKey().getNamespace());
				found = true;
			}
		}

		if (!found) {
			TwilightForestMod.LOGGER.error("Questing Ram quest file not found. Defaulting to fallback");
			questingRamCurrentContext.setContext(QuestingRamContext.FALLBACK);
		}
	}
}
