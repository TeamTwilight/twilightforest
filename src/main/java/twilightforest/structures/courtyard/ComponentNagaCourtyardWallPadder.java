package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

public class ComponentNagaCourtyardWallPadder extends ComponentNagaCourtyardWallAbstract {
    public ComponentNagaCourtyardWallPadder(TemplateManager manager, CompoundNBT nbt) {
        super(manager, NagaCourtyardPieces.TFNCWP, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_padding"), new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_padding_decayed"));
    }

    public ComponentNagaCourtyardWallPadder(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(NagaCourtyardPieces.TFNCWP, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_padding"), new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_padding_decayed"));
    }
}
