package twilightforest.data.helpers;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.Criterion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import twilightforest.junit.MockitoFixer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoFixer.class)
public class AdvancementDataMultiRequirementsTests {

	private AdvancementDataMultiRequirements instance;

	@BeforeEach
	public void setup() {
		instance = new AdvancementDataMultiRequirements();
	}

	@Test
	public void wrap() {
		Advancement.Builder builder = mock(Advancement.Builder.class);
		Criterion<?> criterion = mock(Criterion.class);

		instance.wrap(builder)
			.addCriterion("A", criterion)
			.and()
			.addCriterion("B", criterion)
			.addCriterion("C", criterion)
			.and()
			.addCriterion("D", criterion)
			.addCriterion("E", criterion)
			.addCriterion("F", criterion)
			.requirements();

		ArgumentCaptor<AdvancementRequirements> captor = ArgumentCaptor.captor();
		verify(builder, times(1)).requirements(captor.capture());

		AdvancementRequirements result = captor.getValue();

		assertNotNull(result);
		assertNotNull(result.requirements());
		assertEquals(3, result.requirements().size());

		assertEquals(1, result.requirements().getFirst().size());
		assertEquals("A", result.requirements().getFirst().getFirst());

		assertEquals(2, result.requirements().get(1).size());
		assertEquals("B", result.requirements().get(1).getFirst());
		assertEquals("C", result.requirements().get(1).get(1));

		assertEquals(3, result.requirements().get(2).size());
		assertEquals("D", result.requirements().get(2).getFirst());
		assertEquals("E", result.requirements().get(2).get(1));
		assertEquals("F", result.requirements().get(2).get(2));
	}

}
