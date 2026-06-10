package twilightforest.util;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tamaized.beanification.junit.MockitoFixer;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoFixer.class)
public class ArmorUtilTests {

	private ArmorUtil instance;

	@BeforeEach
	public void setup() {
		instance = new ArmorUtil();
	}

	@Test
	public void getShroudedArmorPercentage() {
		LivingEntity entity = mock(LivingEntity.class);

		ItemStack empty = ItemStack.EMPTY;
		ItemStack stick = new ItemStack((Items.STICK));
		ItemStack shrouded = new ItemStack(TFItems.ARCTIC_BOOTS, 1, DataComponentPatch.builder().set(TFDataComponents.EMPERORS_CLOTH.get(), Unit.INSTANCE).build());

		when(entity.getItemBySlot(EquipmentSlot.HEAD)).thenReturn(empty);
		when(entity.getItemBySlot(EquipmentSlot.CHEST)).thenReturn(stick);
		when(entity.getItemBySlot(EquipmentSlot.LEGS)).thenReturn(shrouded);
		when(entity.getItemBySlot(EquipmentSlot.FEET)).thenReturn(empty);

		float result = instance.getShroudedArmorPercentage(entity);

		assertEquals(1F / 3F, result);
	}

}
