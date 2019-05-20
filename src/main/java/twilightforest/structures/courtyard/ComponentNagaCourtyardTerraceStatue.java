package twilightforest.structures.courtyard;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardTerraceStatue extends ComponentNagaCourtyardTerraceAbstract {
    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardTerraceStatue() {
        super(new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_statue"));
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardTerraceStatue(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_statue"));
    }
}
