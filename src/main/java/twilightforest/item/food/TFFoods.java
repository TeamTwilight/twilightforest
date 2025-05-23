package twilightforest.item.food;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;

public class TFFoods extends Foods {

	public static final FoodProperties TORCHBERRIES = new FoodProperties.Builder().alwaysEdible().build();
	public static final FoodProperties RAW_VENISON = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build();
	public static final FoodProperties VENISON_STEAK = new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build();
	public static final FoodProperties HYDRA_CHOP = new FoodProperties.Builder().nutrition(18).saturationModifier(2.0F).build();
	public static final FoodProperties RAW_MEEF = new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build();
	public static final FoodProperties MEEF_STEAK = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build();
	public static final FoodProperties MEEF_STROGANOFF = new FoodProperties.Builder().nutrition(8).saturationModifier(0.6F).alwaysEdible().build();
	public static final FoodProperties EXPERIMENT_115 = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).build();
}
