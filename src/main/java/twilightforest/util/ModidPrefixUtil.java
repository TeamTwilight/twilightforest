package twilightforest.util;

import twilightforest.TwilightForestMod;
import twilightforest.forge.beans.Component;

@Component
public class ModidPrefixUtil {

	public String stringPrefix(String suffix) {
		return TwilightForestMod.ID.concat(":").concat(suffix);
	}

}
