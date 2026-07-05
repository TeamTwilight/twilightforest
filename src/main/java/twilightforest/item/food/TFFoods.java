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
	public static final FoodProperties BERRY = new FoodProperties.Builder().nutrition(1).saturationModifier(0.4F).build();
	public static final FoodProperties BERRY_MEDLEY = new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build();
	public static final FoodProperties MOSS_SOUP = new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build();
	public static final FoodProperties SHIKA_SENBEI = new FoodProperties.Builder().nutrition(4).saturationModifier(0.2F).build();

	public static final FoodProperties MONSTER_JERKY = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).build();
	public static final FoodProperties BEEF_JERKY = new FoodProperties.Builder().nutrition(6).saturationModifier(1.1F).build();
	public static final FoodProperties CHICKEN_JERKY = new FoodProperties.Builder().nutrition(4).saturationModifier(1.0F).build();
	public static final FoodProperties PORK_JERKY = new FoodProperties.Builder().nutrition(6).saturationModifier(1.1F).build();
	public static final FoodProperties MUTTON_JERKY = new FoodProperties.Builder().nutrition(4).saturationModifier(1.3F).build();
	public static final FoodProperties RABBIT_JERKY = new FoodProperties.Builder().nutrition(4).saturationModifier(0.9F).build();
	public static final FoodProperties COD_JERKY = new FoodProperties.Builder().nutrition(4).saturationModifier(0.8F).build();
	public static final FoodProperties SALMON_JERKY = new FoodProperties.Builder().nutrition(4).saturationModifier(1.3F).build();
	public static final FoodProperties TROPICAL_FISH_JERKY = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
	public static final FoodProperties FUGU_JERKY = new FoodProperties.Builder().nutrition(2).saturationModifier(0.6F).build();
	public static final FoodProperties VENISON_JERKY = new FoodProperties.Builder().nutrition(6).saturationModifier(1.1F).build();
	public static final FoodProperties MEEF_JERKY = new FoodProperties.Builder().nutrition(6).saturationModifier(1.1F).build();

	public static final FoodProperties SLIME_DROP = new FoodProperties.Builder().nutrition(1).saturationModifier(0.2F).build();
	public static final FoodProperties MAZE_SLIME_DROP = new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build();
}
