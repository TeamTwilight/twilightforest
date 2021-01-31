package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardTerraceStatue extends ComponentNagaCourtyardTerraceAbstract {
    public ComponentNagaCourtyardTerraceStatue(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCSt, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_statue"));
    }

    public ComponentNagaCourtyardTerraceStatue(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCSt, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_statue"));
    }
}
