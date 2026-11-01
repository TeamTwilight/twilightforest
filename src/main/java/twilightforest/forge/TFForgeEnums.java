package twilightforest.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import twilightforest.TFEnumExtensions;

/**
 * Les constantes d'enums que NeoForge ajoutait d'apres {@code META-INF/enumextensions.json}.
 *
 * <p>Forge 1.21.1 ne lit pas ce fichier. Il en couvre une partie autrement :</p>
 * <ul>
 *   <li>{@code GrassColorModifier} et {@code ItemDisplayContext} sont extensibles chez lui, par leur
 *   methode {@code create(...)} : c'est ici ;</li>
 *   <li>{@code DamageEffects} et {@code Rarity} ne le sont pas : mixins dans
 *   {@code twilightforest.mixin.enums} ;</li>
 *   <li>{@code Boat.Type} non plus, et NeoForge avait en plus modifie son constructeur (objet du
 *   bateau, du bateau a coffre...). Le mod obtient ses types par {@code Boat.Type.byName}, qui
 *   retombe sur le chene quand le nom est inconnu : les bateaux du mod sont donc des bateaux en
 *   chene tant qu'une entite et un rendu propres a Forge ne sont pas ecrits.</li>
 * </ul>
 *
 * <p>Les parametres viennent des memes methodes de {@link TFEnumExtensions} que sous NeoForge,
 * appelees avec le meme index.</p>
 */
public final class TFForgeEnums {

	private static boolean registered;

	private TFForgeEnums() {
	}

	/**
	 * A appeler en tete du constructeur du mod, avant le conteneur de composants : leurs champs
	 * lisent ces constantes par {@code valueOf}.
	 */
	public static synchronized void register() {
		if (registered) return;
		registered = true;

		grass("TWILIGHTFOREST_ENCHANTED_FOREST",
			TFEnumExtensions.GrassColorModifier_ENCHANTED_FOREST(0, String.class),
			TFEnumExtensions.GrassColorModifier_ENCHANTED_FOREST(1, GrassColorModifier.ColorModifier.class));
		grass("TWILIGHTFOREST_SWAMP",
			TFEnumExtensions.GrassColorModifier_SWAMP(0, String.class),
			TFEnumExtensions.GrassColorModifier_SWAMP(1, GrassColorModifier.ColorModifier.class));
		grass("TWILIGHTFOREST_DARK_FOREST",
			TFEnumExtensions.GrassColorModifier_DARK_FOREST(0, String.class),
			TFEnumExtensions.GrassColorModifier_DARK_FOREST(1, GrassColorModifier.ColorModifier.class));
		grass("TWILIGHTFOREST_DARK_FOREST_CENTER",
			TFEnumExtensions.GrassColorModifier_DARK_FOREST_CENTER(0, String.class),
			TFEnumExtensions.GrassColorModifier_DARK_FOREST_CENTER(1, GrassColorModifier.ColorModifier.class));
		grass("TWILIGHTFOREST_SPOOKY_FOREST",
			TFEnumExtensions.GrassColorModifier_SPOOKY_FOREST(0, String.class),
			TFEnumExtensions.GrassColorModifier_SPOOKY_FOREST(1, GrassColorModifier.ColorModifier.class));

		// NeoForge : (id numerique, nom, nom du contexte de repli). Forge : (nom de constante, nom
		// serialise, contexte de repli) — l'id numerique n'y a pas d'equivalent.
		ItemDisplayContext.create("TWILIGHTFOREST_JARRED",
			ResourceLocation.parse((String) TFEnumExtensions.ItemDisplayContext_JARRED(1, String.class)),
			ItemDisplayContext.valueOf((String) TFEnumExtensions.ItemDisplayContext_JARRED(2, String.class)));
	}

	private static void grass(String enumName, Object serializedName, Object modifier) {
		GrassColorModifier.create(enumName, (String) serializedName, (GrassColorModifier.ColorModifier) modifier);
	}
}
