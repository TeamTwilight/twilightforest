package twilightforest.entity.passive.quest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Autowired;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

import java.util.Map;

public class QuestReloadListener extends SimpleJsonResourceReloadListener {

	@Autowired
	private static QuestingRamCurrentContext questingRamCurrentContext;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public QuestReloadListener() {
		super(GSON, "twilight/quests");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
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
