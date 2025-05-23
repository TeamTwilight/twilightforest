package twilightforest.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TFBlockEntityTypeTags {

	public static final TagKey<BlockEntityType<?>> RELOCATION_NOT_SUPPORTED = create("c", "relocation_not_supported");
	public static final TagKey<BlockEntityType<?>> IMMOVABLE = create("c", "immovable");

	private static TagKey<BlockEntityType<?>> create(String modid, String tagName) {
		return TagKey.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modid, tagName));
	}
}
