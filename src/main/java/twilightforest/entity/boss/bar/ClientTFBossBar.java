package twilightforest.entity.boss.bar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;

import java.util.UUID;

public class ClientTFBossBar extends LerpingBossEvent {
	private int color;

	public ClientTFBossBar(UUID pId, Component pName, float pProgress, int pColor, BossBarOverlay pOverlay, boolean pDarkenScreen, boolean pBossMusic, boolean pWorldFog) {
		super(pId, pName, pProgress, BossBarColor.WHITE, pOverlay, pDarkenScreen, pBossMusic, pWorldFog);
		this.color = pColor;
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
		RenderSystem.setShaderColor(((this.color >> 16) & 255) / 255F, ((this.color >> 8) & 255) / 255F, (this.color & 255) / 255F, 1.0F);

		guiGraphics.blitSprite(BAR_BACKGROUND, 182, 5, 0, 0, x, y, 182, 5);
		if (this.overlay != BossEvent.BossBarOverlay.PROGRESS) guiGraphics.blitSprite(BossHealthOverlay.OVERLAY_BACKGROUND_SPRITES[this.overlay.ordinal() - 1], 182, 5, 0, 0, x, y, 182, 5);
		int i = Mth.lerpDiscrete(this.getProgress(), 0, 182);
		if (i > 0) {
			guiGraphics.blitSprite(BAR_PROGRESS, 182, 5, 0, 0, x, y, i, 5);
			if (this.overlay != BossEvent.BossBarOverlay.PROGRESS) guiGraphics.blitSprite(BossHealthOverlay.OVERLAY_PROGRESS_SPRITES[this.overlay.ordinal() - 1], 182, 5, 0, 0, x, y, i, 5);
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		Component title = this.getName();
		int l = Minecraft.getInstance().font.width(title);
		int i1 = guiGraphics.guiWidth() / 2 - l / 2;
		int j1 = y - 9;
		guiGraphics.drawString(Minecraft.getInstance().font, title, i1, j1, 16777215);

		RenderSystem.disableBlend();
	}
}
