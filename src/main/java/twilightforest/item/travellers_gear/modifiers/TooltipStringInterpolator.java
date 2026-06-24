package twilightforest.item.travellers_gear.modifiers;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import twilightforest.init.TFKeyBinds;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class TooltipStringInterpolator {
	// pattern to find ${var}
	private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

	/**
	 * Renders all ${…} in "template".
	 */
	public static MutableComponent render(String translatableKey) {
		Matcher m = VAR_PATTERN.matcher(Component.translatable(translatableKey).getString());
		StringBuilder sb = new StringBuilder();
		while (m.find()) {
			String var = m.group(1);
			String replacement = lookupVariable(var);
			// quote in case the replacement contains $ or \
			m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
		}
		m.appendTail(sb);
		return Component.literal(sb.toString());
	}

	private static String lookupVariable(String var) {
		Map<String, Function<String, String>> twoPart = Map.of(
			"tfkeybinds", TooltipStringInterpolator::resolveTFKeybind);
		String[] parts = var.split("/");
		if (parts.length != 2)
			throw new IllegalArgumentException("The number of / in " + var + " is not 1");

		Function<String, String> resolver = twoPart.get(parts[0]);
		if (resolver == null) {
			throw new IllegalArgumentException("Unknown namespace: " + parts[0]);
		}
		return resolver.apply(parts[1]);
	}

	private static String resolveTFKeybind(String keyString) {
		Optional<KeyMapping> mapping = TFKeyBinds.KEY_MAPPINGS.stream()
			.filter(keyMapping -> keyMapping.getName().equals(keyString))
			.findFirst();
		if (mapping.isEmpty())
			return "Nonexistent key";
		InputConstants.Key key = mapping.get().getKey();
		return mapping.get().getKeyModifier().getCombinedName(key, key::getDisplayName).getString();
	}
}
