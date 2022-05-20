package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.world.registration.TFFeature;

import static twilightforest.TFConstants.MOD_ID;

public class NagaCourtyardHedgeIntersectionComponent extends NagaCourtyardHedgeAbstractComponent {
    public NagaCourtyardHedgeIntersectionComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
        super(ctx, NagaCourtyardPieces.TFNCIs, nbt, new ResourceLocation(MOD_ID, "courtyard/hedge_intersection"), new ResourceLocation(MOD_ID, "courtyard/hedge_intersection_big"));
    }

    public NagaCourtyardHedgeIntersectionComponent(StructureManager manager, TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(manager, NagaCourtyardPieces.TFNCIs, feature, i, x, y, z, rotation, new ResourceLocation(MOD_ID, "courtyard/hedge_intersection"), new ResourceLocation(MOD_ID, "courtyard/hedge_intersection_big"));
    }
}
