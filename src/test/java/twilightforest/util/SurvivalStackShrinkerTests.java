package twilightforest.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import twilightforest.junit.MockitoFixer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoFixer.class)
public class SurvivalStackShrinkerTests {

	private SurvivalStackShrinker instance;

	@BeforeEach
	public void setup() {
		instance = new SurvivalStackShrinker();
	}

	@Test
	public void shrink() {
		ItemStack stack = mock(ItemStack.class);
		Player player = mock(Player.class);

		when(player.isCreative()).thenReturn(false);

		instance.shrink(stack, player, 1);

		verify(stack, only()).shrink(1);
	}

	@Test
	public void shrinkCreative() {
		ItemStack stack = mock(ItemStack.class);
		Player player = mock(Player.class);

		when(player.isCreative()).thenReturn(true);

		instance.shrink(stack, player, 1);

		verifyNoInteractions(stack);
	}

}
