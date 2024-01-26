package twilightforest.advancements;

import net.minecraft.advancements.AdvancementRequirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public record CountRequirementsStrategy(int... sizes) implements AdvancementRequirements.Strategy {

	@Override
	public AdvancementRequirements create(Collection<String> strings) {
		List<List<String>> requirements = new ArrayList<>();
		List<String> criteriaCopy = new ArrayList<>(strings);
		int nextIndex = 0;
		for (int size : this.sizes) {
			List<String> section = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				section.add(criteriaCopy.get(nextIndex));
				nextIndex++;
			}
			requirements.add(section);
		}
		return new AdvancementRequirements(requirements);
	}
}

