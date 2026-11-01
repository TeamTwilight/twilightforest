package twilightforest.mixin.client;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.BlockHooks;

/**
 * La teinte du feuillage suit les biomes du mod.
 *
 * <p>La cible est la lambda qui sert de resolveur de couleur de feuillage. Verifie dans le jar du
 * jeu : c'est bien {@code lambda$static$0} qui alimente FOLIAGE_COLOR_RESOLVER, et non
 * {@code lambda$static$1} qui alimente l'eau.</p>
 *
 * <p>Les deux noms sont donnes exprès. Une lambda n'a pas de correspondance dans les mappings :
 * l'outil de compilation ne peut pas la traduire, et le nom change entre le jeu de developpement
 * ({@code lambda$static$0}) et le jeu publie ({@code m_108807_}). En les listant tous les deux,
 * avec {@code require = 1}, le mixin trouve sa cible dans les deux cas — et proteste s'il n'en
 * trouve aucune, plutot que de s'appliquer a moitie en silence.</p>
 */
@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {

	@Inject(method = {"lambda$static$0", "m_108807_"}, at = @At(value = "RETURN", ordinal = 0), cancellable = true, remap = false, require = 1)
	private static void twilightforest$resolveFoliageColor(Biome biome, double x, double z, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(BlockHooks.resolveFoliageColor(cir.getReturnValueI(), biome, x, z));
	}
}
