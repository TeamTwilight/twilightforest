package twilightforest.mixin.worldgen;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.levelgen.RandomState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.WorldgenHooks;

@Mixin(ChunkStatusTasks.class)
public abstract class ChunkStatusTasksMixin {

	/**
	 * Recouvre le chunk de neige, de racines ou de feuilles selon le biome du mod.
	 *
	 * <p>La region de generation n'est qu'une variable locale de la methode. En detournant l'appel
	 * qui la consomme, on la recoit en parametre au lieu d'aller la lire dans la table des
	 * variables locales, qui change au moindre recompilage du jeu.</p>
	 */
	@Redirect(method = "generateSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V"))
	private static void twilightforest$chunkBlanketing(ChunkGenerator generator, WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
		generator.buildSurface(region, structureManager, randomState, chunk);
		WorldgenHooks.chunkBlanketing(chunk, region);
	}
}
