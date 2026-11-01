package twilightforest.mixin.worldgen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.WorldgenHooks;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin {

	/**
	 * Relit les donnees propres aux structures du mod (progression, salles conquises).
	 *
	 * <p>Le detournement porte sur la construction de l'objet plutot que sur la sortie de la
	 * methode : celle-ci sort aussi par des chemins d'erreur, ou il n'y a rien a relire.</p>
	 */
	@Redirect(method = "loadStaticStart", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/levelgen/structure/Structure;Lnet/minecraft/world/level/ChunkPos;ILnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;"))
	private static StructureStart twilightforest$loadStaticStart(Structure structure, ChunkPos chunkPos, int references, PiecesContainer pieces, StructurePieceSerializationContext context, CompoundTag tag, long seed) {
		return WorldgenHooks.loadStaticStart(new StructureStart(structure, chunkPos, references, pieces), pieces, tag);
	}
}
