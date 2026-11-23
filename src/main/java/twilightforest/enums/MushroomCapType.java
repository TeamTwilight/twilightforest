package twilightforest.enums;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum MushroomCapType implements StringRepresentable{
	VANILLA,
	SMOOTH,
	SPHEROID,
	FLAT;

	public static final Codec<MushroomCapType> CODEC = StringRepresentable.fromEnum(MushroomCapType::values);

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
