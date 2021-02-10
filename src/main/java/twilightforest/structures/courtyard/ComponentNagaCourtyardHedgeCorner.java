package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardHedgeCorner extends ComponentNagaCourtyardHedgeAbstract {
    public ComponentNagaCourtyardHedgeCorner(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCCr, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_corner"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_corner_big"));
    }

    public ComponentNagaCourtyardHedgeCorner(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCCr, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_corner"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_corner_big"));
    }
}
