package twilightforest.datagen.helpers;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BlockToItemConverter implements TagAppender<Block> {
	private final TagAppender<Item> itemAppender;

	public BlockToItemConverter(TagAppender<Item> itemAppender) {
		this.itemAppender = itemAppender;
	}

	private static TagKey<Item> blockTagToItemTag(TagKey<Block> blockTag) {
		return TagKey.create(Registries.ITEM, blockTag.location());
	}

	@Override
	public TagAppender<Block> add(ResourceKey<Block> element) {
		this.itemAppender.add(ResourceKey.create(Registries.ITEM, element.identifier()));
		return this;
	}

	@Override
	public TagAppender<Block> addOptional(ResourceKey<Block> element) {
		this.itemAppender.addOptional(ResourceKey.create(Registries.ITEM, element.identifier()));
		return this;
	}

	@Override
	public TagAppender<Block> addTag(TagKey<Block> tag) {
		this.itemAppender.addTag(blockTagToItemTag(tag));
		return this;
	}

	@Override
	public TagAppender<Block> addOptionalTag(TagKey<Block> tag) {
		this.itemAppender.addOptionalTag(blockTagToItemTag(tag));
		return this;
	}

	@Override
	public TagAppender<Block> add(TagEntry entry) {
		this.itemAppender.add(entry);
		return this;
	}

	@Override
	public TagAppender<Block> replace(boolean value) {
		this.itemAppender.replace(value);
		return this;
	}

	@Override
	public TagAppender<Block> remove(ResourceKey<Block> element) {
		this.itemAppender.remove(ResourceKey.create(Registries.ITEM, element.identifier()));
		return this;
	}

	@Override
	public TagAppender<Block> remove(TagKey<Block> tag) {
		this.itemAppender.remove(blockTagToItemTag(tag));
		return this;
	}
}
