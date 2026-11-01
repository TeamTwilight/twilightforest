package twilightforest.forge;

import net.minecraft.network.chat.Component;

/**
 * Une enumeration de configuration qui sait s'afficher dans la langue du joueur.
 *
 * <p>NeoForge fournit cette interface pour ses ecrans de configuration ; Forge non. Elle ne declare
 * qu'une methode, donc la redonner ne coute rien et garde les enumerations du mod inchangees.</p>
 */
public interface TranslatableEnum {
	Component getTranslatedName();
}
