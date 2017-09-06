package twilightforest.block.enums;

import net.minecraft.util.IStringSerializable;
import twilightforest.block.EnumTFPlantType;

import java.util.Locale;

public enum PlantVariant implements IStringSerializable {
	MOSSPATCH(false, EnumTFPlantType.CAVE),
	MAYAPPLE(false, EnumTFPlantType.STANDARD),
	CLOVERPATCH(false, EnumTFPlantType.STANDARD),
	FIDDLEHEAD(true, EnumTFPlantType.STANDARD),
	MUSHGLOOM(false, EnumTFPlantType.CAVE),
	FORESTGRASS(true, EnumTFPlantType.DARKNESS_RESISTENT),
	DEADBUSH(false, EnumTFPlantType.DARKNESS_RESISTENT),
	TORCHBERRY(false, EnumTFPlantType.HANGING),
	ROOT_STRAND(false, EnumTFPlantType.HANGING);

	public final boolean isGrassColored;
	public final EnumTFPlantType plantType;

	PlantVariant(boolean isGrassColored, EnumTFPlantType plantType) {
		this.isGrassColored = isGrassColored;
		this.plantType = plantType;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
