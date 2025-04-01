package twilightforest.datagen;

import net.minecraft.DetectedVersion;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.datagen.generator.AssetsGenerator;
import twilightforest.datagen.generator.DataGenerator;

import java.util.Optional;

@Component
public class DataGenerators {

	@Autowired
	private AssetsGenerator assetsGenerator;

	@Autowired
	private DataGenerator dataGenerator;

	@PostConstruct
	private void register(IEventBus bus) {
		bus.addListener(GatherDataEvent.Client.class, event -> {
			assetsGenerator.generate(event);
			dataGenerator.generate(event);

			event.getGenerator().addProvider(true, new PackMetadataGenerator(event.getGenerator().getPackOutput())
				.add(PackMetadataSection.TYPE, new PackMetadataSection(
						net.minecraft.network.chat.Component.literal("Resources for Twilight Forest"),
						DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
						Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE))
					)
				)
			);
		});
	}

}
