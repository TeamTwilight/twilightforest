package twilightforest.world.components.structures;

public interface SpawnIndexProvider {
	int getSpawnIndex();

	interface Deny extends SpawnIndexProvider {
		@Override
		default int getSpawnIndex() {
			return -1;
		}
	}
}
