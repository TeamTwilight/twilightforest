package twilightforest.forge.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

/**
 * L'ingredient {@code neoforge:components} de NeoForge : des objets dont les composants doivent
 * correspondre (par exemple une potion de rapidite, {@code minecraft:potion_contents}).
 *
 * <p>Forge 1.21.1 n'a que {@code forge:partial_nbt} et {@code forge:nbt}, qui ne regardent que le
 * composant {@code custom_data} : ils ne savent pas reconnaitre une potion. Meme format JSON que
 * NeoForge ({@code items}, {@code components}, {@code strict}), sous le type
 * {@code twilightforest:components}.</p>
 */
public class ComponentsIngredient extends AbstractIngredient {

	public static final MapCodec<ComponentsIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			HolderSetCodec.create(Registries.ITEM, BuiltInRegistries.ITEM.holderByNameCodec(), false).fieldOf("items").forGetter(i -> i.items),
			DataComponentPredicate.CODEC.fieldOf("components").forGetter(i -> i.components),
			Codec.BOOL.optionalFieldOf("strict", false).forGetter(i -> i.strict)
	).apply(instance, ComponentsIngredient::new));

	public static final IIngredientSerializer<ComponentsIngredient> SERIALIZER = new IIngredientSerializer<>() {
		@Override
		public MapCodec<? extends ComponentsIngredient> codec() {
			return CODEC;
		}

		@Override
		public void write(RegistryFriendlyByteBuf buffer, ComponentsIngredient value) {
			ByteBufCodecs.holderSet(Registries.ITEM).encode(buffer, value.items);
			DataComponentPredicate.STREAM_CODEC.encode(buffer, value.components);
			buffer.writeBoolean(value.strict);
		}

		@Override
		public ComponentsIngredient read(RegistryFriendlyByteBuf buffer) {
			return new ComponentsIngredient(
					ByteBufCodecs.holderSet(Registries.ITEM).decode(buffer),
					DataComponentPredicate.STREAM_CODEC.decode(buffer),
					buffer.readBoolean());
		}
	};

	private final HolderSet<Item> items;
	private final DataComponentPredicate components;
	private final boolean strict;

	public ComponentsIngredient(HolderSet<Item> items, DataComponentPredicate components, boolean strict) {
		// Les objets montres (livre de recettes, JEI) portent deja les composants demandes.
		super(items.stream().map(item -> {
			ItemStack stack = new ItemStack(item);
			stack.applyComponents(components.asPatch());
			return new Ingredient.ItemValue(stack);
		}));
		this.items = items;
		this.components = components;
		this.strict = strict;
	}

	@Override
	public boolean test(@Nullable ItemStack stack) {
		if (stack == null || !this.items.contains(stack.getItemHolder())) {
			return false;
		}
		// Strict : l'objet ne doit rien porter d'autre que les composants demandes.
		return this.strict
				? stack.getComponentsPatch().equals(this.components.asPatch())
				: this.components.test(stack);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> serializer() {
		return SERIALIZER;
	}
}
