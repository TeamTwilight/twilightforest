package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.ResourceLocation;
import twilightforest.datagen.helpers.ItemModelBuilders;

import java.util.function.BiConsumer;

public class ItemModelGenerator extends ItemModelBuilders {
	public ItemModelGenerator(ItemModelOutput output, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(output, modelOutput);
	}

	@Override
	public void run() {

	}
}
