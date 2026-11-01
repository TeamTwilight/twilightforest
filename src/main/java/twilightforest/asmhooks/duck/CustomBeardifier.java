package twilightforest.asmhooks.duck;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.Nullable;

/**
 * Le champ que le coremod NeoForge ajoutait a {@code Beardifier}.
 *
 * <p>Un mixin ne peut pas ajouter un champ « visible » de l'exterieur : il ajoute le champ, et
 * cette interface sert de poignee pour y acceder depuis le reste du mod. {@code Beardifier}
 * l'implemente a l'execution, par le mixin.</p>
 */
public interface CustomBeardifier {

	void twilightforest$setCustomStructureDensities(@Nullable ObjectListIterator<DensityFunction> densities);

	@Nullable
	ObjectListIterator<DensityFunction> twilightforest$getCustomStructureDensities();
}
