package twilightforest.world.components.chunkgenerators;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.levelgen.structure.Structure;
import com.google.common.collect.MapMaker;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ControlledSpawnsCache extends SimplePreparableReloadListener<Object> {

	// Read from chunk worker threads (spawnOriginalMobs during chunk generation) and the server thread (NaturalSpawner) at once
	public static final ConcurrentMap<ChunkGeneratorTwilight, List<Structure>> CONTROLLED_SPAWNS = new MapMaker().weakKeys().makeMap();

	public static void reload(AddReloadListenerEvent event) {
		event.addListener(new ControlledSpawnsCache());
	}

	@Override
	protected Object prepare(ResourceManager manager, ProfilerFiller filler) {
		return 0;
	}

	@Override
	protected void apply(Object obj, ResourceManager manager, ProfilerFiller filler) {
		CONTROLLED_SPAWNS.clear();
	}
}
