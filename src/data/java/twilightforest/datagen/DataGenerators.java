package twilightforest.datagen;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.datagen.generator.AssetsGenerator;
import twilightforest.datagen.generator.DataGenerator;

@Component
public class DataGenerators {

	@Autowired
	private AssetsGenerator assetsGenerator;

	@Autowired
	private DataGenerator dataGenerator;

	@PostConstruct
	private void register(IEventBus bus) {
		bus.addListener(GatherDataEvent.Client.class, event -> {
			this.dataGenerator.generate(event);
			this.assetsGenerator.generate(event);
		});
	}

}
