package twilightforest.data.helpers;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.Criterion;
import twilightforest.beans.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdvancementDataMultiRequirements {

	public MultiRequirementsBuilder wrap(Advancement.Builder parent) {
		return new MultiRequirementsBuilder(parent);
	}

	public static final class MultiRequirementsBuilder {

		private final Advancement.Builder parent;

		private List<String> currentRequirements = new ArrayList<>();
		private final List<List<String>> totalRequirements = new ArrayList<>();

		private MultiRequirementsBuilder(Advancement.Builder parent) {
			this.parent = parent;
		}

		public MultiRequirementsBuilder addCriterion(String key, Criterion<?> criterion) {
			parent.addCriterion(key, criterion);
			currentRequirements.add(key);
			return this;
		}

		public MultiRequirementsBuilder and() {
			totalRequirements.add(currentRequirements);
			// Don't use #clear otherwise the current reference in totalRequirements will also be cleared, instead we construct a new reference entirely
			currentRequirements = new ArrayList<>();
			return this;
		}

		public Advancement.Builder requirements() {
			and();
			return parent.requirements(new AdvancementRequirements(totalRequirements));
		}

	}

}
