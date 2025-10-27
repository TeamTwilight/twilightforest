package twilightforest;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

/**
 * Entrypoint for Twilight Forest's game-integrated tests
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = TwilightForestMod.ID)
public final class TFGameTests {
	public static final Logger LOGGER = LogManager.getLogger(TwilightForestMod.ID + "tests");

	@SubscribeEvent
	public static void registerBlockTests(RegisterGameTestsEvent event) {
		LOGGER.info("Starting registerBlockTests");
		event.register(TFGameTests.class);
	}

	@GameTestGenerator
	public static Collection<TestFunction> generateBlockTests() {
		return TFBlockTests.generateBlockRegistryTests();
	}
}
