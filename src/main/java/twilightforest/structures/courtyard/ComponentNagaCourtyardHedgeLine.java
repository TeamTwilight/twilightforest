package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardHedgeLine extends ComponentNagaCourtyardHedgeAbstract {
    public ComponentNagaCourtyardHedgeLine(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCLn, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_line"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_line_big"));
    }

    public ComponentNagaCourtyardHedgeLine(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCLn, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_line"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_line_big"));
    }
}
