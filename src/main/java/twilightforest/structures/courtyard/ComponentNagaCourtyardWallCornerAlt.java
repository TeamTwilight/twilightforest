package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardWallCornerAlt extends ComponentNagaCourtyardWallAbstract {
    public ComponentNagaCourtyardWallCornerAlt(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCWA, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_corner_inner"), new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_corner_inner_decayed"));
    }

    public ComponentNagaCourtyardWallCornerAlt(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCWA, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_corner_inner"), new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_corner_inner_decayed"));
    }
}
