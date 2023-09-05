package twilightforest.util;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TwilightForestMod;

public class TFWoodTypes {

	public static final BlockSetType TWILIGHT_OAK_SET = new BlockSetType(TwilightForestMod.prefix("twilight_oak").toString());
	public static final BlockSetType CANOPY_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("canopy").toString());
	public static final BlockSetType MANGROVE_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("mangrove").toString());
	public static final BlockSetType DARKWOOD_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("dark").toString());
	public static final BlockSetType TIMEWOOD_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("timewood").toString());
	public static final BlockSetType TRANSFORMATION_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("transformation").toString());
	public static final BlockSetType MINING_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("mining").toString());
	public static final BlockSetType SORTING_WOOD_SET = new BlockSetType(TwilightForestMod.prefix("sorting").toString());

	public static final WoodType TWILIGHT_OAK_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("twilight_oak").toString(), TWILIGHT_OAK_SET));
	public static final WoodType CANOPY_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("canopy").toString(), CANOPY_WOOD_SET));
	public static final WoodType MANGROVE_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("mangrove").toString(), MANGROVE_WOOD_SET));
	public static final WoodType DARKWOOD_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("dark").toString(), DARKWOOD_WOOD_SET));
	public static final WoodType TIMEWOOD_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("timewood").toString(), TIMEWOOD_WOOD_SET));
	public static final WoodType TRANSFORMATION_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("transformation").toString(), TRANSFORMATION_WOOD_SET));
	public static final WoodType MINING_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("mining").toString(), MINING_WOOD_SET));
	public static final WoodType SORTING_WOOD_TYPE = WoodType.register(new WoodType(TwilightForestMod.prefix("sorting").toString(), SORTING_WOOD_SET));
}
