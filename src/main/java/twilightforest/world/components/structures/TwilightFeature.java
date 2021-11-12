package twilightforest.world.components.structures;

import twilightforest.world.registration.TFFeature;

@Deprecated // Wait this isn't even needed. What the heck was this used for?
public interface TwilightFeature {
    @Deprecated // For Legacy Class usage
    default void setFeature(TFFeature type) {
    }

    @Deprecated // Remove this whole class - These TFFeature labels inside StructComps serve no purpose
    TFFeature getFeatureType();
}
