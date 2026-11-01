package net.neoforged.api.distmarker;

/**
 * Le marqueur de cote de NeoForge, fourni ici pour que le compilateur puisse lire les annotations
 * de Beanification.
 *
 * <p>La bibliotheque d'injection de dependances declare un attribut de ce type dans ses annotations.
 * Meme sans jamais s'en servir, javac doit pouvoir resoudre le type pour lire l'annotation : sans
 * cette classe il abandonne avec un {@code CompletionFailure}, sans message utile.</p>
 *
 * <p>Elle n'est jamais utilisee a l'execution : les usages de l'attribut ont ete retires du mod.
 * Elle disparaitra le jour ou Beanification sera remplace ou construit pour Forge.</p>
 */
public enum Dist {
	CLIENT,
	DEDICATED_SERVER
}
