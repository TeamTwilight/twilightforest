package twilightforest.entity.boss.bar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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

	private static final Identifier BAR_BACKGROUND = Identifier.withDefaultNamespace("boss_bar/white_background");
	private static final Identifier BAR_PROGRESS = Identifier.withDefaultNamespace("boss_bar/white_progress");

	public void renderBossBar(GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y) {
		// 26.1.2: RenderSystem.setShaderColor removed; use blitSprite color parameter for tinting
		int tintColor = 0xFF000000 | (this.color & 0x00FFFFFF);

		GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_BACKGROUND, x, y, 182, 5, tintColor);
		if (this.overlay != BossEvent.BossBarOverlay.PROGRESS) GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, BossHealthOverlay.OVERLAY_BACKGROUND_SPRITES[this.overlay.ordinal() - 1], 182, 5, 0, 0, x, y, 182, 5);
		int i = Mth.lerpDiscrete(this.getProgress(), 0, 182);
		if (i > 0) {
			GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_PROGRESS, x, y, i, 5, tintColor);
			if (this.overlay != BossEvent.BossBarOverlay.PROGRESS) GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, BossHealthOverlay.OVERLAY_PROGRESS_SPRITES[this.overlay.ordinal() - 1], 182, 5, 0, 0, x, y, i, 5);
		}

		Component title = this.getName();
		int l = Minecraft.getInstance().font.width(title);
		int i1 = GuiGraphicsExtractor.guiWidth() / 2 - l / 2;
		int j1 = y - 9;
		GuiGraphicsExtractor.text(Minecraft.getInstance().font, title, i1, j1, -1);
	}
}
