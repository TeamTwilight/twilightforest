package twilightforest.compat.rei.entries;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EntityEntryDefinition implements EntryDefinition<Entity>, EntrySerializer<Entity> {

	public static EntryType<Entity> ENTITY_TYPE = EntryType.deferred(TwilightForestMod.prefix("entity"));

	private final EntryRenderer<Entity> renderer;

	public EntityEntryDefinition() {
		this.renderer = new EntityRenderer();
	}

	@Override
	public Class<Entity> getValueType() {
		return Entity.class;
	}

	@Override
	public EntryType<Entity> getType() {
		return ENTITY_TYPE;
	}

	@Override
	public EntryRenderer<Entity> getRenderer() {
		return this.renderer;
	}

	@Override
	public @Nullable ResourceLocation getIdentifier(EntryStack<Entity> entry, Entity value) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(value.getType());
	}

	@Override
	public boolean isEmpty(EntryStack<Entity> entry, Entity value) {
		return false;
	}

	@Override
	public Entity copy(EntryStack<Entity> entry, Entity value) {
		CompoundTag tag = new CompoundTag();

		String string = value.getEncodeId();

		if (string != null) {
			tag.putString("id", string);
			value.saveWithoutId(tag);
		}

		Entity entity = value.getType().create(Minecraft.getInstance().level);

		entity.load(tag);

		return entity;
	}

	@Override
	public Entity normalize(EntryStack<Entity> entry, Entity value) {
		return this.copy(entry, value);
	}

	@Override
	public Entity wildcard(EntryStack<Entity> entry, Entity value) {
		return value.getType().create(Minecraft.getInstance().level);
	}

	@Override
	public @Nullable ItemStack cheatsAs(EntryStack<Entity> entry, Entity value) {
		SpawnEggItem egg = DeferredSpawnEggItem.byId(value.getType());
		if (egg != null) {
			return new ItemStack(egg);
		}
		return EntryDefinition.super.cheatsAs(entry, value);
	}

	@Override
	public @Nullable Entity add(Entity o1, Entity o2) {
		CompoundTag tag = new CompoundTag();
		o1.save(tag);
		o2.save(tag);
		Entity copy = o1.getType().create(Minecraft.getInstance().level);
		copy.load(tag);
		return copy;
	}

	@Override
	public long hash(EntryStack<Entity> entry, Entity value, ComparisonContext context) {
		int code = 1;
		code = 31 * code + System.identityHashCode(value);
		code = 31 * code + Long.hashCode(EntityComparatorImpl.INSTANCE.hashOf(context, value));
		return code;
	}

	@Override
	public boolean equals(Entity o1, Entity o2, ComparisonContext context) {
		if (o1 != o2)
			return false;
		return EntityComparatorImpl.INSTANCE.hashOf(context, o1) == EntityComparatorImpl.INSTANCE.hashOf(context, o2);
	}

	@Override
	public @Nullable EntrySerializer<Entity> getSerializer() {
		return null;
	}

	@Override
	public boolean supportSaving() {
		return true;
	}

	@Override
	public boolean supportReading() {
		return true;
	}

	@Override
	public CompoundTag save(EntryStack<Entity> entry, Entity value) {
		CompoundTag tag = new CompoundTag();
		String string = value.getEncodeId();

		if (string != null) {
			tag.putString("id", string);
			value.saveWithoutId(tag);
		}

		return tag;
	}

	@Override
	public Entity read(CompoundTag tag) {
		return EntityType.create(tag, Minecraft.getInstance().level).get();
	}

	@Override
	public Component asFormattedText(EntryStack<Entity> entry, Entity value) {
		return this.asFormattedText(entry, value, TooltipContext.of(Item.TooltipContext.EMPTY));
	}

	@Override
	public Component asFormattedText(EntryStack<Entity> entry, Entity value, TooltipContext context) {
		return value.getType().getDescription();
	}

	@Override
	public Stream<? extends TagKey<?>> getTagsFor(EntryStack<Entity> entry, Entity value) {
		return value.getType().builtInRegistryHolder().tags();
	}

	public static class EntityRenderer implements EntryRenderer<Entity> {

		@Override
		public void render(EntryStack<Entity> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
			if (!entry.isEmpty()) {
				graphics.pose().pushPose();
				graphics.pose().translate(bounds.getX(), bounds.getY(), -100.0D);
				EntityRenderingUtil.renderEntity(graphics, entry.getValue().getType(), 32);
				graphics.pose().popPose();
			}
		}

		@Override
		@Nullable
		public Tooltip getTooltip(EntryStack<Entity> entry, TooltipContext context) {
			if (entry.isEmpty()) return null;
			Tooltip tooltip = Tooltip.create();
			EntityRenderingUtil.getMobTooltip(entry.getValue().getType()).forEach(tooltip::add);
			return tooltip;
		}
	}

	public static class ItemEntityRenderer implements EntryRenderer<Entity> {
		private final float bobOffs;

		public ItemEntityRenderer() {
			this.bobOffs = RandomSource.create().nextFloat() * (float) Math.PI * 2.0F;
		}

		@Override
		public void render(EntryStack<Entity> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
			ItemStack item = ((ItemEntity) entry.getValue()).getItem();

			if (!entry.isEmpty()) {
				Level level = Minecraft.getInstance().level;

				graphics.pose().pushPose();
				graphics.pose().translate(bounds.getX(), bounds.getY(), 0);

				if (level != null) {
					try {
						EntityRenderingUtil.renderItemEntity(graphics, item, level, this.bobOffs);
					} catch (Exception e) {
						TwilightForestMod.LOGGER.error("Error drawing item in REI!", e);
					}
				}

				graphics.pose().popPose();
			}
		}

		@Override
		@Nullable
		public Tooltip getTooltip(EntryStack<Entity> entry, TooltipContext context) {
			ItemStack item = ((ItemEntity) entry.getValue()).getItem();

			List<Component> tooltip = new ArrayList<>();

			tooltip.add(item.getItem().getDescription());

			if (context.getFlag().isAdvanced()) {
				tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.getItem())).toString()).withStyle(ChatFormatting.DARK_GRAY));
			}

			return Tooltip.create(context.getPoint(), tooltip);
		}
	}
}
