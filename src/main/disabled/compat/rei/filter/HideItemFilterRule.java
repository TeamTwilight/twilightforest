package twilightforest.compat.rei.filter;

import com.google.common.collect.Streams;
import me.shedaniel.rei.api.client.entry.filtering.*;
import me.shedaniel.rei.api.common.entry.EntryStack;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFItems;

import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class HideItemFilterRule implements FilteringRule<Object> {
	public static HideItemFilterRule INSTANCE = new HideItemFilterRule();

	@Override
	public FilteringRuleType<? extends FilteringRule<Object>> getType() {
		return HideItemFilterType.INSTANCE;
	}

	@Override
	public FilteringResult processFilteredStacks(FilteringContext context, FilteringResultFactory resultFactory, Object o, boolean async) {
		FilteringResult filteringResult = resultFactory.create();

		Stream<EntryStack<?>> all = Streams.concat(context.getShownStacks().stream(), context.getUnsetStacks().stream());

		filteringResult.hide(all.filter(stack -> TFItems.MAGIC_PAINTING.getId().equals(stack.getIdentifier())
			|| TFItems.FOUR_LEAF_CLOVER.getId().equals(stack.getIdentifier()) || TFItems.STALE_BREAD.getId().equals(stack.getIdentifier())
			|| stack.getTagsFor().anyMatch(tagKey -> tagKey.equals(TFItemTags.WIP))
		).toList());

		return filteringResult;
	}
}
