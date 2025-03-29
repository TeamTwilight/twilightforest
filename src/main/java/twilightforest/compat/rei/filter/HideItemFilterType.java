package twilightforest.compat.rei.filter;

import me.shedaniel.rei.api.client.entry.filtering.FilteringRuleType;
import net.minecraft.nbt.CompoundTag;

@SuppressWarnings("UnstableApiUsage")
public class HideItemFilterType implements FilteringRuleType<HideItemFilterRule> {
	public static HideItemFilterType INSTANCE = new HideItemFilterType();

	@Override
	public CompoundTag saveTo(HideItemFilterRule rule, CompoundTag tag) {
		return tag;
	}

	@Override
	public HideItemFilterRule readFrom(CompoundTag tag) {
		return HideItemFilterRule.INSTANCE;
	}

	@Override
	public HideItemFilterRule createNew() {
		return HideItemFilterRule.INSTANCE;
	}

	@Override
	public boolean isSingular() {
		return true;
	}
}
