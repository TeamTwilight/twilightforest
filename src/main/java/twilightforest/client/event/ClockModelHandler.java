package twilightforest.client.event;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ModelEvent;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDimension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ClockModelHandler {

    @SuppressWarnings("unchecked")
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ItemModel clockModel = event.getBakingResult().itemStackModels().get(Identifier.withDefaultNamespace("clock"));
        if (!(clockModel instanceof SelectItemModel<?> selectModel)) return;

        try {
            Field modelsField = SelectItemModel.class.getDeclaredField("models");
            modelsField.setAccessible(true);
            SelectItemModel.ModelSelector<Object> originalSelector = (SelectItemModel.ModelSelector<Object>) modelsField.get(selectModel);

            Field propertyField = SelectItemModel.class.getDeclaredField("property");
            propertyField.setAccessible(true);
            SelectItemModelProperty<Object> property = (SelectItemModelProperty<Object>) propertyField.get(selectModel);

            ResourceKey<Level> overworldKey = Level.OVERWORLD;
            ResourceKey<Level> tfKey = TFDimension.DIMENSION_KEY;

            ItemModel overworldModel = originalSelector.get(overworldKey, null);
            if (overworldModel == null) return;

            SelectItemModel.ModelSelector<Object> newSelector = (value, context) -> {
                if (value instanceof ResourceKey<?> key && key.identifier().equals(tfKey.identifier())) {
                    return overworldModel;
                }
                return originalSelector.get(value, context);
            };

            Constructor<SelectItemModel<Object>> constructor = (Constructor<SelectItemModel<Object>>) (Constructor<?>) SelectItemModel.class.getDeclaredConstructor(
                SelectItemModelProperty.class, SelectItemModel.ModelSelector.class
            );
            constructor.setAccessible(true);
            ItemModel newModel = constructor.newInstance(property, newSelector);

            event.getBakingResult().itemStackModels().put(Identifier.withDefaultNamespace("clock"), newModel);
        } catch (Exception e) {
            TwilightForestMod.LOGGER.error("[TF] Failed to patch clock model for Twilight Forest dimension", e);
        }
    }
}
