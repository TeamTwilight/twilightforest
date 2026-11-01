package twilightforest.mixin.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.MapHooks;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

	/**
	 * Les reperes de la Foret Crepusculaire se trouvent aussi a la boussole et sur les cartes.
	 *
	 * <p>TAIL vise la derniere sortie de la methode, celle qui rend le resultat trouve, et non les
	 * sorties anticipees qui rendent « rien trouve » pour de bonnes raisons.</p>
	 */
	@Inject(method = "findNearestMapStructure", at = @At("TAIL"), cancellable = true)
	private void twilightforest$resolveNearestLandmark(ServerLevel level, HolderSet<Structure> targets, BlockPos pos, int searchRadius, boolean skipKnown, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
		cir.setReturnValue(MapHooks.resolveNearestNonRandomSpreadMapStructure(cir.getReturnValue(), level, targets, pos, searchRadius, skipKnown));
	}
}
