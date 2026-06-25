package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.TwilightForestMod;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public record TransferableComponentModifier(
	EquipmentSlotGroup group,
	DataComponentType<Unit> markerComponent,
	TypedDataComponent<?> transferableComponent,
	List<Component> description
) implements TransferableTravellersModifier {
	public static final MapCodec<TransferableComponentModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		EquipmentSlotGroup.CODEC.fieldOf("equipment_slots").validate(TravellersModifier::validateEquipment).forGetter(TransferableComponentModifier::group),
		DataComponentType.CODEC.fieldOf("component").forGetter(o -> o.markerComponent),
		DataComponentMap.CODEC.fieldOf("transferable_components").forGetter(o -> DataComponentMap.builder().set((DataComponentType<Object>) o.transferableComponent().type(), o.transferableComponent().value()).build()),
		ComponentSerialization.CODEC.listOf().optionalFieldOf("description", List.of()).forGetter(TransferableComponentModifier::description)
	).apply(instance, (group, markerComponentMap, transferableComponentsMap, description) -> {
		if (transferableComponentsMap.size() != 1)
			throw new IllegalArgumentException(String.format("Expected exactly one entry in this data component maps: %s", transferableComponentsMap));
		DataComponentType<Unit> marker = (DataComponentType<Unit>) markerComponentMap;
		TypedDataComponent<?> transferable = transferableComponentsMap.stream().findFirst().orElseThrow();
		return new TransferableComponentModifier(group, marker, transferable, description);
	}));

	public <T> TransferableComponentModifier(EquipmentSlotGroup group, DataComponentType<Unit> component, DataComponentType<T> transferableComponent, T defaultTransferableComponentValue, List<Component> description) {
		this(group, component, new TypedDataComponent<>(transferableComponent, defaultTransferableComponentValue), description);
	}

	@Override
	public MapCodec<? extends TravellersModifier> codec() {
		return CODEC;
	}

	@Override
	public boolean isAbility() {
		return false;
	}

	@Override
	public boolean addModifier(ItemStack stack) {
		stack.set(markerComponent, Unit.INSTANCE);
		stack.set((DataComponentType<Object>) transferableComponent.type(), transferableComponent.value());
		return true;
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		return stack.has(markerComponent) && stack.has(transferableComponent.type());
	}

	@Override
	public void removeModifier(ItemStack stack) {
		stack.remove(markerComponent);
		stack.remove(transferableComponent.type());
	}

	@Override
	public boolean transfer(ItemStack output, List<Ingredient> input) {
		List<ItemStack[]> dataComponentProviders = findDataComponentProviders(input);
		if (dataComponentProviders.isEmpty())
			return false;
		if (dataComponentProviders.size() > 1) {
			TwilightForestMod.LOGGER.error(String.format("A recipe with more than 2 dataComponentProviders was matched: %s. Please report to https://github.com/TeamTwilight/twilightforest/issues", input));
			return false;
		}
		ItemStack dataComponentProvider = dataComponentProviders.getFirst()[0];
		output.set(this.markerComponent, Unit.INSTANCE);
		output.set((DataComponentType<Object>) this.transferableComponent.type(), dataComponentProvider.get(transferableComponent.type()));
		return true;
	}

	public List<ItemStack[]> findDataComponentProviders(List<Ingredient> input) {
		return input.stream().map(Ingredient::items)
			.filter(holders -> holders.anyMatch(holder -> new ItemStack(holder.value()).has(transferableComponent.type())))
			.map(holders -> holders.map(holder -> new ItemStack(holder.value())).toArray(ItemStack[]::new))
			.toList();
	}

	@Override
	public List<Component> getDescription() {
		return this.description;
	}
}
