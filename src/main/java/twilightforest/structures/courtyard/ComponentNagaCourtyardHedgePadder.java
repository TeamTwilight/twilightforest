package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardHedgePadder extends ComponentNagaCourtyardHedgeAbstract {
    public ComponentNagaCourtyardHedgePadder(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCPd, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between_big"));
    }

    public ComponentNagaCourtyardHedgePadder(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCPd, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between_big"));
    }
}
