package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardTerraceDuct extends ComponentNagaCourtyardTerraceAbstract {
    public ComponentNagaCourtyardTerraceDuct(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCDu, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_duct"));
    }

    public ComponentNagaCourtyardTerraceDuct(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCDu, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_duct"));
    }
}
