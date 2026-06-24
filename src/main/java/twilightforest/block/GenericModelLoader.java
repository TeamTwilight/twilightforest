package twilightforest.block;

import com.google.common.base.Supplier;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.UnbakedModel;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public class GenericModelLoader<T extends UnbakedModel> implements UnbakedModelLoader<T> {

    private final Supplier<T> modelFactory;

    public GenericModelLoader(Supplier<T> modelFactory) {
        this.modelFactory = modelFactory;
    }

	@Override
	public T read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		return this.modelFactory.get();
	}
}