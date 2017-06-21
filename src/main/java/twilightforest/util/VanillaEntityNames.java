package twilightforest.util;

import net.minecraft.util.ResourceLocation;

public class VanillaEntityNames {
	public static final ResourceLocation CAVE_SPIDER = prefix("cave_spider");
	public static final ResourceLocation SPIDER = prefix("spider");
	public static final ResourceLocation ZOMBIE = prefix("zombie");
	public static final ResourceLocation SKELETON = prefix("skeleton");
	public static final ResourceLocation BLAZE = prefix("blaze");

	private static ResourceLocation prefix(String path) {
		return new ResourceLocation("minecraft", path);
	}
}
