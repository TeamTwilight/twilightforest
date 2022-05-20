package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.world.registration.TFFeature;

import static twilightforest.TFConstants.MOD_ID;

public class NagaCourtyardHedgeLineComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgeLineComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
        super(ctx, NagaCourtyardPieces.TFNCLn, nbt, new ResourceLocation(MOD_ID, "courtyard/hedge_line"), new ResourceLocation(MOD_ID, "courtyard/hedge_line_big"));
    }

    public NagaCourtyardHedgeLineComponent(StructureManager manager, TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(manager, NagaCourtyardPieces.TFNCLn, feature, i, x, y, z, rotation, new ResourceLocation(MOD_ID, "courtyard/hedge_line"), new ResourceLocation(MOD_ID, "courtyard/hedge_line_big"));
    }
}
