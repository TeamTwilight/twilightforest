package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TFBlockEntityTypeTags {

	public static final TagKey<BlockEntityType<?>> RELOCATION_NOT_SUPPORTED = create("c", "relocation_not_supported");
	public static final TagKey<BlockEntityType<?>> IMMOVABLE = create("c", "immovable");

	private static TagKey<BlockEntityType<?>> create(String modid, String tagName) {
		return TagKey.create(Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(modid, tagName));
	}
}
