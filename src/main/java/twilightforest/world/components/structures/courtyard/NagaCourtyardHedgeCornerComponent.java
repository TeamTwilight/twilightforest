package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.world.registration.TFFeature;

import static twilightforest.TFConstants.MOD_ID;

public class NagaCourtyardHedgeCornerComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgeCornerComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
        super(ctx, NagaCourtyardPieces.TFNCCr, nbt, new ResourceLocation(MOD_ID, "courtyard/hedge_corner"), new ResourceLocation(MOD_ID, "courtyard/hedge_corner_big"));
    }

    public NagaCourtyardHedgeCornerComponent(StructureManager manager, TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(manager, NagaCourtyardPieces.TFNCCr, feature, i, x, y, z, rotation, new ResourceLocation(MOD_ID, "courtyard/hedge_corner"), new ResourceLocation(MOD_ID, "courtyard/hedge_corner_big"));
    }
}
