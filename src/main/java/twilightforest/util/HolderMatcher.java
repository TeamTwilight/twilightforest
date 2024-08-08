package twilightforest.util;

import net.minecraft.core.Holder;
import twilightforest.beans.Component;

@Component
public class HolderMatcher {

	public <T> boolean match(Holder<T> a, Holder<T> b) {
		if (a.kind() == Holder.Kind.DIRECT || b.kind() == Holder.Kind.DIRECT) {
			return a.value() == b.value();
		}
		return b.unwrapKey().map(a::is).orElseGet(() -> a.unwrapKey().map(b::is).orElse(false));
	}

}
