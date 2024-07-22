package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import twilightforest.junit.MockitoFixer;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoFixer.class)
public class BlockCapabilityDirectionalCacheTests {

	private BlockCapabilityDirectionalCache<?> instance;

	@BeforeEach
	public void setup() {
		instance = new BlockCapabilityDirectionalCache<>();
	}

	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void get() {
		try (MockedStatic<BlockCapabilityCache> blockCapabilityCacheMockedStatic = mockStatic(BlockCapabilityCache.class)) {
			BlockCapability blockCapability = mock(BlockCapability.class);
			ServerLevel level = mock(ServerLevel.class);
			BlockPos pos = mock(BlockPos.class);
			Direction direction = mock(Direction.class);

			BlockCapabilityCache cache = mock(BlockCapabilityCache.class);
			blockCapabilityCacheMockedStatic.when(() -> BlockCapabilityCache.create(blockCapability, level, pos, direction)).thenReturn(cache);

			Object check = mock(Object.class);
			when(cache.getCapability()).thenReturn(check);

			assertSame(check, instance.get(blockCapability, level, pos, direction));
			// Once more to verify the caching
			assertSame(check, instance.get(blockCapability, level, pos, direction));

			blockCapabilityCacheMockedStatic.verify(() -> BlockCapabilityCache.create(blockCapability, level, pos, direction), times(1));
		}
	}

}
