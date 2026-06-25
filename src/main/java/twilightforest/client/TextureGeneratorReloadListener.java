package twilightforest.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TextureGeneratorReloadListener implements ResourceManagerReloadListener {
	public static final TextureGeneratorReloadListener INSTANCE = new TextureGeneratorReloadListener();
	private static final Map<String, AbstractTexture> BOAT_CACHE = new HashMap<>();
	private static final AtomicReference<NativeImage> ref = new AtomicReference<>();

	private static final List<String> BOAT_TYPES = List.of(
		"canopy", "dark", "mangrove", "mining", "sorting", "time", "transformation", "twilight_oak"
	);

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		// Get a default boat chest texture
		Identifier oak = Identifier.withDefaultNamespace("textures/entity/chest_boat/oak.png");

		manager.getResource(oak).ifPresent(vanillaResource -> {
			try (InputStream vanillaStream = vanillaResource.open()) {
				try (NativeImage vanillaImage = NativeImage.read(vanillaStream)) {
					int defaultScale = 128;
					int vanillaScale = vanillaImage.getWidth() / defaultScale;
					for (String type : BOAT_TYPES) {
						Identifier location = getTFTextureLocation(type);
						if (location.getNamespace().equals(TwilightForestMod.ID)) { // We only want to do this to our boats
							manager.getResource(location).ifPresent(tfResource -> {
								try (InputStream tfStream = tfResource.open()) {
									try (NativeImage tfImage = NativeImage.read(tfStream)) {
										int tfScale = tfImage.getWidth() / defaultScale;

										for (int x = 0; x < 48 * tfScale; x++) {
											for (int y = 58 * tfScale; y < 96 * tfScale; y++) {
												// If the loaded tf boat chest texture has non-transparent pixels below the boat section of the texture, return
												// 26.1.2: getPixel() returns ARGB format (replaced getPixelRGBA)
												if (tfImage.getPixel(x, y) != 0x00000000) return;
											}
										}

										if (vanillaScale > tfScale) {
											// 26.1.2: DynamicTexture takes ownership of NativeImage, so newImage must NOT be in try-with-resources
											NativeImage newImage = new NativeImage(defaultScale * vanillaScale, defaultScale * vanillaScale, false);
											newImage.copyFrom(vanillaImage);
											for (int x = 0; x < 102 * vanillaScale; x++) {
												for (int y = 0; y < 52 * vanillaScale; y++) {
													newImage.setPixel(x, y, tfImage.getPixel(x / (vanillaScale / tfScale), y / (vanillaScale / tfScale)));
												}
											}

											ref.set(newImage);
											BOAT_CACHE.compute(type, (key, existing) -> {
												DynamicTexture texture = new DynamicTexture(() -> "twilightforest:boat_" + key, ref.getAndSet(null));
												Minecraft.getInstance().getTextureManager().register(location, texture);
												return texture;
											});
										} else {
											for (int x = 0; x < 48 * tfScale; x++) {
												for (int y = 58 * tfScale; y < 96 * tfScale; y++) {
													// 26.1.2: getPixel()/setPixel() replaced getPixelRGBA/setPixelRGBA (return ARGB format)
												tfImage.setPixel(x, y, vanillaImage.getPixel(x / (tfScale / vanillaScale), y / (tfScale / vanillaScale)));
												}
											}

											ref.set(tfImage);
											BOAT_CACHE.compute(type, (key, existing) -> {
												DynamicTexture texture = new DynamicTexture(() -> "twilightforest:boat_" + key, ref.getAndSet(null));
												Minecraft.getInstance().getTextureManager().register(location, texture);
												return texture;
											});
										}
									}
								} catch (IOException e) {
									// Fail silently, no boat texture bullshit here
								}
							});
						}
					}
				}
			} catch (IOException e) {
				// Fail silently, no boat texture bullshit here
			}
		});
		ref.set(null);
	}

	private static Identifier getTFTextureLocation(String type) {
		return Identifier.fromNamespaceAndPath(TwilightForestMod.ID, "textures/entity/chest_boat/" + type + ".png");
	}
}
