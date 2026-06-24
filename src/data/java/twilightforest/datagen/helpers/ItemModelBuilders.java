package twilightforest.datagen.helpers;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public abstract class ItemModelBuilders extends ItemModelGenerators {

	public ItemModelBuilders(ItemModelOutput output, BiConsumer<Identifier, ModelInstance> modelOutput) {
		super(output, modelOutput);
	}

	@Override
	public abstract void run();
}
