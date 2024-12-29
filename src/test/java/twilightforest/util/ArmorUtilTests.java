package twilightforest.util;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.junit.MockitoFixer;

import java.util.List;
import java.util.OptionalInt;

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
		when(entity.getArmorSlots()).thenReturn(List.of(
			ItemStack.EMPTY,
			new ItemStack(Items.STICK),
			new ItemStack(TFItems.ARCTIC_BOOTS, 1, DataComponentPatch.builder().set(TFDataComponents.EMPERORS_CLOTH.get(), Unit.INSTANCE).build())
		));

		float result = instance.getShroudedArmorPercentage(entity);

		assertEquals(1F / 3F, result);
	}

}
