package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import twilightforest.TFRegistries;
import twilightforest.init.TFBiomes;
import twilightforest.util.Restriction;

public class Restrictions {
	public static final Codec<Holder<Restriction>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC, false);

	public static final ResourceKey<Restriction> DARK_FOREST = makeKey(TFBiomes.DARK_FOREST.identifier());
	public static final ResourceKey<Restriction> DARK_FOREST_CENTER = makeKey(TFBiomes.DARK_FOREST_CENTER.identifier());
	public static final ResourceKey<Restriction> FINAL_PLATEAU = makeKey(TFBiomes.FINAL_PLATEAU.identifier());
	public static final ResourceKey<Restriction> FIRE_SWAMP = makeKey(TFBiomes.FIRE_SWAMP.identifier());
	public static final ResourceKey<Restriction> GLACIER = makeKey(TFBiomes.GLACIER.identifier());
	public static final ResourceKey<Restriction> HIGHLANDS = makeKey(TFBiomes.HIGHLANDS.identifier());
	public static final ResourceKey<Restriction> SNOWY_FOREST = makeKey(TFBiomes.SNOWY_FOREST.identifier());
	public static final ResourceKey<Restriction> SWAMP = makeKey(TFBiomes.SWAMP.identifier());
	public static final ResourceKey<Restriction> THORNLANDS = makeKey(TFBiomes.THORNLANDS.identifier());

	private static ResourceKey<Restriction> makeKey(Identifier name) {
		return ResourceKey.create(TFRegistries.Keys.RESTRICTIONS, name);
	}

}
