package twilightforest.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import twilightforest.client.model.TFModelLayers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class TextureGeneratorReloadListener implements ResourceManagerReloadListener {

	public static final TextureGeneratorReloadListener INSTANCE = new TextureGeneratorReloadListener();
	// Get a default boat chest texture
	private final Identifier vanillaChestBoat = getTextureLocation(ModelLayers.OAK_CHEST_BOAT);
	private boolean registered;

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		if (registered)
			return;
		TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		for (ModelLayerLocation boat : TFModelLayers.getChestBoats()) {
			Identifier location = getTextureLocation(boat);
			textureManager.registerAndLoad(location, new ChestBoatTexture(location));
		}
		registered = true;
	}

	private Identifier getTextureLocation(ModelLayerLocation layer) {
		return layer.model().withPath(path -> "textures/entity/" + path + ".png");
	}

	private NativeImage generate(NativeImage tfImage, NativeImage vanillaImage) {
		int defaultScale = 128;
		int vanillaScale = vanillaImage.getWidth() / defaultScale;
		int tfScale = tfImage.getWidth() / defaultScale;

		for (int x = 0; x < 48 * tfScale; x++) {
			for (int y = 58 * tfScale; y < 96 * tfScale; y++) {
				// If the loaded tf boat chest texture has non-transparent pixels below the boat section of the texture, return
				if (tfImage.getPixel(x, y) != 0x00000000)
					return tfImage;
			}
		}

		if (vanillaScale > tfScale) {
			NativeImage newImage = new NativeImage(defaultScale * vanillaScale, defaultScale * vanillaScale, false);
			newImage.copyFrom(vanillaImage);
			for (int x = 0; x < 102 * vanillaScale; x++) {
				for (int y = 0; y < 52 * vanillaScale; y++) {
					newImage.setPixel(x, y, tfImage.getPixel(x / (vanillaScale / tfScale), y / (vanillaScale / tfScale)));
				}
			}
			tfImage.close();
			return newImage;
		}

		for (int x = 0; x < 48 * tfScale; x++) {
			for (int y = 58 * tfScale; y < 96 * tfScale; y++) {
				tfImage.setPixel(x, y, vanillaImage.getPixel(x / (tfScale / vanillaScale), y / (tfScale / vanillaScale)));
			}
		}
		return tfImage;
	}

	private class ChestBoatTexture extends ReloadableTexture {

		public ChestBoatTexture(Identifier resourceId) {
			super(resourceId);
		}

		@Override
		public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
			TextureContents tfContents = TextureContents.load(resourceManager, resourceId());
			Optional<Resource> vanillaResource = resourceManager.getResource(vanillaChestBoat);
			if (vanillaResource.isEmpty())
				return tfContents;

			NativeImage vanillaImage;
			try (InputStream vanillaStream = vanillaResource.get().open()) {
				vanillaImage = NativeImage.read(vanillaStream);
			} catch (IOException e) {
				// Fail silently, no boat texture bullshit here
				return tfContents;
			}

			try (vanillaImage) {
				return new TextureContents(generate(tfContents.image(), vanillaImage), tfContents.metadata());
			}
		}
	}
}
