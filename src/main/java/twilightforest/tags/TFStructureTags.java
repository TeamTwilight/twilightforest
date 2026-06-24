package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;

public class TFStructureTags {

	// Add structures to this tag to show on the Magic Map, detected by worldgen features avoiding landmarks and progression lock behavior
	public static final TagKey<Structure> LANDMARK = create("landmark");

	private static TagKey<Structure> create(String tagName) {
		return TagKey.create(Registries.STRUCTURE, TwilightForestMod.prefix(tagName));
	}
}
