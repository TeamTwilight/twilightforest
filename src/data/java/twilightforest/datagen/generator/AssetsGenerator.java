package twilightforest.datagen.generator;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import twilightforest.datagen.assets.*;
import twilightforest.datagen.assets.models.ModelGenerator;

@Component
public class AssetsGenerator {

	public void generate(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();

		generator.addProvider(true, new AtlasGenerator(output, event.getLookupProvider()));
		generator.addProvider(true, new EquipmentAssetsGenerator(output));
		generator.addProvider(true, new ModelGenerator(output));
		generator.addProvider(true, new ParticleGenerator(output));
		generator.addProvider(true, new SoundGenerator(output));
		//run last because of subtitles
		generator.addProvider(true, new LangGenerator(output, event.getLookupProvider()));
	}
}
