package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.TwilightForestMod;
import twilightforest.world.registration.TFFeature;

public class NagaCourtyardHedgeTJunctionComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgeTJunctionComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
        super(ctx, NagaCourtyardPieces.TFNCT, nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_t"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_t_big"));
    }

    public NagaCourtyardHedgeTJunctionComponent(StructureManager manager, TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(manager, NagaCourtyardPieces.TFNCT, feature, i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_t"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_t_big"));
    }
}