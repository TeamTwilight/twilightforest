package twilightforest.datagen.helpers.models;

import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import net.neoforged.neoforge.client.model.generators.blockstate.UnbakedMutator;

public class SimpleCustomBlockStateModelBuilder extends CustomBlockStateModelBuilder {
    private final CustomUnbakedBlockStateModel model;

    public SimpleCustomBlockStateModelBuilder(CustomUnbakedBlockStateModel model) {
        this.model = model;
    }

    @Override
    public CustomBlockStateModelBuilder with(VariantMutator variantMutator) {
        return this;
    }

    @Override
    public CustomBlockStateModelBuilder with(UnbakedMutator variantMutator) {
        return this;
    }

    @Override
    public CustomUnbakedBlockStateModel toUnbaked() {
        return model;
    }
}
