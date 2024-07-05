package twilightforest.data.helpers;

import twilightforest.TwilightForestMod;

import java.text.ChoiceFormat;
import java.util.List;

/**
 * Various methods were borrowed from <a href="https://github.com/mekanism/Mekanism/blob/1.20.x/src/datagen/main/java/mekanism/client/lang/UpsideDownLanguageProvider.java">Mekanism</a> <br>
 */
public class LangConversionHelper {

	private static final String normal = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + ",.?!;\"'`&_^()[]{}<>";
	private static final char[] upside_down = ("ɐqɔpǝɟᵷɥᴉɾʞꞁɯuodbɹsʇnʌʍxʎz" + "ⱯᗺƆᗡƎℲ⅁HIՐꞰꞀWNOԀꝹᴚS⟘∩ΛMX⅄Z" + "0⥝ᘔƐ߈ϛ9ㄥ86" + "'˙¿¡؛„,,⅋‾v)(][}{><").toCharArray();

	public static char flip(char c) {
		int index = normal.indexOf(c);
		return index == -1 ? c : upside_down[index];
	}

	public static String convertFormattingComponent(LangFormatSplitter.FormatComponent component, int curIndex, int numArguments) {
		if (component instanceof LangFormatSplitter.MessageFormatComponent messageFormatComponent) {
			//Convert a MessageFormat styled formatting code
			return convertMessageFormatCode(messageFormatComponent);
		}
		String formattingCode = component.contents();
		//Convert a % styled formatting code
		String ending;
		int storedIndex = curIndex;
		//A formatting code can have at most one $ and if it has one then it is the first "argument" after the %
		String[] split = formattingCode.split("\\$");
		if (split.length == 2) {
			//It already has an index, so read that as the stored index
			ending = split[1];
			storedIndex = Integer.parseInt(split[0].substring(1));
		} else {
			//No index stored in the formatting code
			ending = formattingCode.substring(1);
		}
		//Compare the index the argument currently has with the index it will have afterwards
		// If they are the same we don't need to include the index argument
		if (storedIndex == numArguments - curIndex + 1) {
			return "%" + ending;
		}
		return "%" + storedIndex + "$" + ending;
	}

	/**
	 * Reverses order of inner arguments of a MessageFormat styled formatting code
	 */
	public static String convertMessageFormatCode(LangFormatSplitter.MessageFormatComponent component) {
		String formatStyle = component.getFormatStyle();
		if (formatStyle != null && component.isChoice()) {
			//The formatting style is a choice, and we want to invert any "excess" text that is part of it
			String newFormatStyle = invertChoice(formatStyle);
			try {
				new ChoiceFormat(newFormatStyle);
			} catch (IllegalArgumentException e) {
				TwilightForestMod.LOGGER.warn("Failed to convert '{}' to an upside down choice format. Got: '{}' which was invalid.", formatStyle, newFormatStyle);
				//Safety check for if we failed to convert it into a valid choice format just fallback to leaving the format as is
				return component.contents();
			}
			return "{" + component.getArgumentIndex() + "," + component.getFormatType() + "," + newFormatStyle + "}";
		}
		//If we don't have a style we don't need to invert it so just return what we have
		// or our style is not a choice as only choice's need to have further processing done
		return component.contents();
	}

	public static String invertChoice(String choice) {
		StringBuilder converted = new StringBuilder();
		StringBuilder literalBuilder = new StringBuilder();
		StringBuilder textBuilder = new StringBuilder();
		char[] exploded = choice.toCharArray();
		int leftBrackets = 0;
		boolean inLiteral = true;
		for (char c : exploded) {
			if (inLiteral) {
				literalBuilder.append(c);
				if (c == '#' || c == '<' || c == '≤') {
					//#, <, and less than equal are valid comparisons for ChoiceFormat
					// after we hit one, we are no longer in a literal though so mark it as such
					inLiteral = false;
					converted.append(literalBuilder);
					literalBuilder = new StringBuilder();
				}
			} else {
				if (c == '{') {
					leftBrackets++;
				} else if (c == '}') {
					leftBrackets--;
				} else if (c == '|' && leftBrackets == 0) {
					inLiteral = true;
					//Note: We directly use MessageFormat because forge does not use MessageFormat at all if it has valid % formatting codes
					converted.append(convertComponents(LangFormatSplitter.splitMessageFormat(textBuilder.toString())));
					textBuilder = new StringBuilder();
				}
				if (inLiteral) {
					literalBuilder.append(c);
				} else {
					textBuilder.append(c);
				}
			}
		}
		if (inLiteral) {
			converted.append(literalBuilder);
		} else {
			//Note: We directly use MessageFormat because forge does not use MessageFormat at all if it has valid % formatting codes
			converted.append(convertComponents(LangFormatSplitter.splitMessageFormat(textBuilder.toString())));
		}
		return converted.toString();
	}

	public static String convertComponents(List<LangFormatSplitter.Component> splitText) {
		int numArguments = (int) splitText.stream().filter(component -> component instanceof LangFormatSplitter.FormatComponent).count();
		StringBuilder converted = new StringBuilder();
		int curIndex = numArguments;
		for (int i = splitText.size() - 1; i >= 0; i--) {
			LangFormatSplitter.Component component = splitText.get(i);
			if (component instanceof LangFormatSplitter.FormatComponent formatComponent) {
				//Insert the full code directly
				converted.append(convertFormattingComponent(formatComponent, curIndex--, numArguments));
			} else {
				//Convert each character to being upside down and then insert at end
				char[] toConvertArr = component.contents().toCharArray();
				for (int j = toConvertArr.length - 1; j >= 0; j--) {
					converted.append(flip(toConvertArr[j]));
				}
			}
		}
		return new String(converted);
	}
}
