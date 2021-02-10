package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardHedgeCapPillar extends ComponentNagaCourtyardHedgeAbstract {
    public ComponentNagaCourtyardHedgeCapPillar(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCCpP, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar_big"));
    }

    public ComponentNagaCourtyardHedgeCapPillar(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCCpP, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar_big"));
    }
}