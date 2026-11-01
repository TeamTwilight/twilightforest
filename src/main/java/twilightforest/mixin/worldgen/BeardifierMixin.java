package twilightforest.mixin.worldgen;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.WorldgenHooks;
import twilightforest.asmhooks.duck.CustomBeardifier;

/**
 * Les structures du mod modelent le terrain autour d'elles.
 *
 * <p>Le « beardifier » est ce qui aplanit le sol sous une structure. Le mod y ajoute ses propres
 * fonctions de densite : le champ qui les porte est ajoute ici, et le mixin sur le generateur de
 * chunks le remplit au moment ou le chunk se genere.</p>
 */
@Mixin(Beardifier.class)
public abstract class BeardifierMixin implements CustomBeardifier {

	@Unique
	@Nullable
	private ObjectListIterator<DensityFunction> twilightforest$customStructureDensities;

	@Override
	public void twilightforest$setCustomStructureDensities(@Nullable ObjectListIterator<DensityFunction> densities) {
		this.twilightforest$customStructureDensities = densities;
	}

	@Override
	@Nullable
	public ObjectListIterator<DensityFunction> twilightforest$getCustomStructureDensities() {
		return this.twilightforest$customStructureDensities;
	}

	@Inject(method = "compute", at = @At("RETURN"), cancellable = true)
	private void twilightforest$getCustomDensity(DensityFunction.FunctionContext context, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(WorldgenHooks.getCustomDensity(cir.getReturnValueD(), context, this.twilightforest$customStructureDensities));
	}
}
