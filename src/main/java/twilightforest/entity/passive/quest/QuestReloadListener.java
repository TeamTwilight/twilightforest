package twilightforest.entity.passive.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Autowired;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

import java.util.Map;

public class QuestReloadListener extends SimpleJsonResourceReloadListener<JsonElement> {

	@Autowired
	private static QuestingRamCurrentContext questingRamCurrentContext;

	public QuestReloadListener() {
		super(ExtraCodecs.JSON, FileToIdConverter.json("twilight/quests"));
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
		boolean found = false;
		for (var entry : object.entrySet()) {
			if (entry.getKey().getPath().equals("questing_ram")) {
				questingRamCurrentContext.setContext(QuestingRamContext.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(JsonParseException::new));
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
