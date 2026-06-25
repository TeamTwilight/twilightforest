package twilightforest.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.Criterion;

import java.util.ArrayList;
import java.util.List;

public class AdvancementDataMultiRequirements {

	private Advancement.Builder builder;
	private final List<List<String>> requirements = new ArrayList<>();
	private List<String> currentGroup;

	public AdvancementBuilderWrap wrap(Advancement.Builder builder) {
		this.builder = builder;
		this.requirements.clear();
		this.currentGroup = new ArrayList<>();
		return new AdvancementBuilderWrap();
	}

	public class AdvancementBuilderWrap {
		public AdvancementBuilderWrap addCriterion(String name, Criterion<?> criterion) {
			builder.addCriterion(name, criterion);
			currentGroup.add(name);
			return this;
		}

		public AdvancementBuilderWrap and() {
			if (!currentGroup.isEmpty()) {
				requirements.add(new ArrayList<>(currentGroup));
				currentGroup.clear();
			}
			return this;
		}

		public Advancement.Builder requirements() {
			if (!currentGroup.isEmpty()) {
				requirements.add(new ArrayList<>(currentGroup));
				currentGroup.clear();
			}
			builder.requirements(new AdvancementRequirements(requirements));
			return builder;
		}
	}
}
