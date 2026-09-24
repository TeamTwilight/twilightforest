package twilightforest.init.custom;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.MagicPaintingVariant;

import java.util.HashMap;
import java.util.Map;

public class MagicPaintingVariants {

	public static final Map<Identifier, Pair<String, String>> MAGIC_PAINTING_LANG_HELPER = new HashMap<>();
	public static final Map<Identifier, MagicPaintingVariant> MAGIC_PAINTING_ATLAS_HELPER = new HashMap<>();

	public static final Codec<Holder<MagicPaintingVariant>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC, false);
	public static final StreamCodec<? super RegistryFriendlyByteBuf, Holder<MagicPaintingVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TFRegistries.Keys.MAGIC_PAINTINGS);

	public static final ResourceKey<MagicPaintingVariant> DARKNESS = makeKey(TwilightForestMod.prefix("darkness"));
	public static final ResourceKey<MagicPaintingVariant> LUCID_LANDS = makeKey(TwilightForestMod.prefix("lucid_lands"));
	public static final ResourceKey<MagicPaintingVariant> THE_HOSTILE_PARADISE = makeKey(TwilightForestMod.prefix("the_hostile_paradise"));
	public static final ResourceKey<MagicPaintingVariant> CASTAWAY_PARADISE = makeKey(TwilightForestMod.prefix("castaway_paradise"));
	public static final ResourceKey<MagicPaintingVariant> MUSIC_IN_THE_MIRE = makeKey(TwilightForestMod.prefix("music_in_the_mire"));

	public static final ResourceKey<MagicPaintingVariant> DEFAULT = MagicPaintingVariants.LUCID_LANDS; // FIXME Switch to a smaller one once available or create a blank 1x1 that's not accessible by normal means

	private static ResourceKey<MagicPaintingVariant> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.MAGIC_PAINTINGS, name);
	}

}
