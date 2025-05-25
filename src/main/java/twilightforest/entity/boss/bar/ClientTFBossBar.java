package twilightforest.entity.boss.bar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import org.joml.Matrix4f;

import java.util.UUID;
import java.util.function.Function;

public class ClientTFBossBar extends LerpingBossEvent {
	private int color;

	public ClientTFBossBar(UUID id, Component name, float progress, int color, BossBarOverlay overlay, boolean darkenScreen, boolean bossMusic, boolean worldFog) {
		super(id, name, progress, BossBarColor.WHITE, overlay, darkenScreen, bossMusic, worldFog);
		this.color = color;
	}

	public void setBarColor(int color) {
		this.color = color;
	}

	public long getSetTime() {
		return this.setTime;
	}

	public void setSetTime(long setTime) {
		this.setTime = setTime;
	}

	private static final ResourceLocation BAR_BACKGROUND = ResourceLocation.withDefaultNamespace("boss_bar/white_background");
	private static final ResourceLocation BAR_PROGRESS = ResourceLocation.withDefaultNamespace("boss_bar/white_progress");

	public void renderBossBar(GuiGraphics guiGraphics, int x, int y) {
		RenderSystem.enableBlend();

		this.blitSprite(guiGraphics, RenderType::guiTextured, BAR_BACKGROUND, x, y, 182, 5);
		if (this.overlay != BossEvent.BossBarOverlay.PROGRESS) this.blitSprite(guiGraphics, RenderType::guiTextured, BossHealthOverlay.OVERLAY_BACKGROUND_SPRITES[this.overlay.ordinal() - 1], x, y, 182, 5);
		int progress = Mth.lerpDiscrete(this.getProgress(), 0, 182);
		if (progress > 0) {
			this.blitSprite(guiGraphics, RenderType::guiTextured, BAR_PROGRESS, x, y, progress, 5);
			if (this.overlay != BossEvent.BossBarOverlay.PROGRESS) this.blitSprite(guiGraphics, RenderType::guiTextured, BossHealthOverlay.OVERLAY_PROGRESS_SPRITES[this.overlay.ordinal() - 1], x, y, progress, 5);
		}

		Component title = this.getName();
		int width = Minecraft.getInstance().font.width(title);
		int fontX = guiGraphics.guiWidth() / 2 - width / 2;
		int fontY = y - 9;
		guiGraphics.drawString(Minecraft.getInstance().font, title, fontX, fontY, 0xFFFFFF);

		RenderSystem.disableBlend();
	}

	public void blitSprite(GuiGraphics guiGraphics, Function<ResourceLocation, RenderType> renderTypeGetter, ResourceLocation location, float x, float y, int uWidth, int vHeight) {
		TextureAtlasSprite sprite = guiGraphics.sprites.getSprite(location);

		float minU = sprite.getU(0.0F);
		float maxU = sprite.getU(uWidth / 182.0F);
		float minV = sprite.getV(0.0F);
		float maxV = sprite.getV(vHeight / 5.0F);

		float r = ((this.color >> 16) & 255) / 255F, g = ((this.color >> 8) & 255) / 255F, b = (this.color & 255) / 255F, a = 1.0F;

		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		VertexConsumer vertex = guiGraphics.bufferSource.getBuffer(renderTypeGetter.apply(sprite.atlasLocation()));
		vertex.addVertex(matrix4f, x, y, 0.0F).setUv(minU, minV).setColor(r, g, b, a);
		vertex.addVertex(matrix4f, x, y + vHeight, 0.0F).setUv(minU, maxV).setColor(r, g, b, a);
		vertex.addVertex(matrix4f, x + uWidth, y + vHeight, 0.0F).setUv(maxU, maxV).setColor(r, g, b, a);
		vertex.addVertex(matrix4f, x + uWidth, y, 0.0F).setUv(maxU, minV).setColor(r, g, b, a);
	}
}
