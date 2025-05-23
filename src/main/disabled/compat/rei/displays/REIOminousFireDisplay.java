package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REIOminousFireCategory;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class REIOminousFireDisplay extends BasicDisplay {

	private REIOminousFireDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
	}

	private REIOminousFireDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, CompoundTag tag) {
		this(inputs, outputs);
	}

	@Nullable
	public static REIOminousFireDisplay of(RecipeViewerConstants.OminousFireInfo recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		List<EntryIngredient> outputs = new ArrayList<>();

		getEntity(recipe.input(), Minecraft.getInstance().level).ifPresent(entity -> {
			inputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem inputEgg = DeferredSpawnEggItem.byId(entity.getType());
			if (inputEgg != null) {
				inputs.add(EntryIngredients.of(inputEgg));
			}
		});

		getEntity(recipe.output(), Minecraft.getInstance().level).ifPresent(entity -> {
			outputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem outputEgg = DeferredSpawnEggItem.byId(entity.getType());
			if (outputEgg != null) {
				outputs.add(EntryIngredients.of(outputEgg));
			}
		});

		if (!inputs.isEmpty() && !outputs.isEmpty()) {

			return new REIOminousFireDisplay(inputs, outputs);
		}

		return null;
	}

	public static Optional<Entity> getEntity(EntityType<?> type, @Nullable Level level) {
		return Optional.ofNullable(EntityRenderingUtil.fetchEntity(type, level));
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIOminousFireCategory.OMINOUS_FIRE;
	}

	public static Serializer<REIOminousFireDisplay> serializer() {
		return Serializer.ofRecipeLess(REIOminousFireDisplay::new, (display, tag) -> {});
	}
}
