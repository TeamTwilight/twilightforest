package twilightforest.test.util;

import twilightforest.util.ArmorUtil;
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

		when(entity.getItemBySlot(EquipmentSlot.HEAD))
			.thenReturn(ItemStack.EMPTY);

		ItemStack chestStack = mock(ItemStack.class);
		when(entity.getItemBySlot(EquipmentSlot.CHEST))
			.thenReturn(chestStack);

		when(entity.getItemBySlot(EquipmentSlot.LEGS))
			.thenReturn(ItemStack.EMPTY);

		ItemStack bootsStack = mock(ItemStack.class);
		when(bootsStack.get(TFDataComponents.EMPERORS_CLOTH.get())).thenReturn(Unit.INSTANCE);
		when(entity.getItemBySlot(EquipmentSlot.FEET)).thenReturn(bootsStack);

		float result = instance.getShroudedArmorPercentage(entity);

		assertEquals(0.25F, result);
	}
}
