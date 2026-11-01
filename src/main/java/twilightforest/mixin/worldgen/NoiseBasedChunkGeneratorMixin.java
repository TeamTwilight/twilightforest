package twilightforest.mixin.worldgen;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.WorldgenHooks;
import twilightforest.asmhooks.duck.CustomBeardifier;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {

	/** Remplit le beardifier du chunk avec les densites des structures du mod. */
	@Redirect(method = "createNoiseChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/Beardifier;forStructuresInChunk(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;"))
	private Beardifier twilightforest$gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		Beardifier beardifier = Beardifier.forStructuresInChunk(structureManager, chunkPos);
		((CustomBeardifier) (Object) beardifier).twilightforest$setCustomStructureDensities(WorldgenHooks.gatherCustomTerrain(structureManager, chunkPos));
		return beardifier;
	}
}
