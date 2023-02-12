package twilightforest.mixin;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import twilightforest.world.components.structures.start.CustomStructureData;

@Mixin(StructureStart.class)
public class StructureStartMixin {
    @Inject(at = @At(value = "RETURN", ordinal = 2, shift = At.Shift.BEFORE), method = "loadStaticStart(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Lnet/minecraft/nbt/CompoundTag;J)Lnet/minecraft/world/level/levelgen/structure/StructureStart;", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void replaceStaticStart(StructurePieceSerializationContext ctx, CompoundTag nbt, long packedChunkPos, CallbackInfoReturnable<StructureStart> cir, String s, Registry<Structure> registry, Structure structure, ChunkPos chunkpos, int references, ListTag listtag, PiecesContainer piecescontainer) {
        if (structure instanceof CustomStructureData customStructure) {
            cir.setReturnValue(customStructure.forDeserialization(structure, chunkpos, references, piecescontainer, nbt));
        }
    }
}
