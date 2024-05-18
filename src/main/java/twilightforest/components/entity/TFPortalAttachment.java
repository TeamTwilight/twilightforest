package twilightforest.components.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import twilightforest.block.TFPortalBlock;

public class TFPortalAttachment {
	public static int MAX_TICKS = 60;
	protected boolean isInsidePortal = false;
	protected int portalTimer = 0;

	public void setInPortal(boolean inPortal) {
		this.isInsidePortal = inPortal;
	}

	public boolean isInsidePortal() {
		return this.isInsidePortal;
	}

	public int getPortalTimer() {
		return this.portalTimer;
	}

	public void tick(Player player) {
		if (this.isInsidePortal()) {
			this.portalTimer = Math.min(this.portalTimer + 1, MAX_TICKS);

			if (!player.isInWall()) {
				BlockPos pos = player.blockPosition();
				if (!(player.level().getBlockState(pos).getBlock() instanceof TFPortalBlock) && !(player.level().getBlockState(pos.below()).getBlock() instanceof TFPortalBlock)) {
					this.isInsidePortal = false;
				}
			}
		} else if (this.getPortalTimer() > 0) this.portalTimer -= 2;

		if (player instanceof LocalPlayer local) {
			Minecraft minecraft = Minecraft.getInstance();
			if (this.isInsidePortal()) {
				if (minecraft.screen != null && !minecraft.screen.isPauseScreen() && !(minecraft.screen instanceof DeathScreen)) {
					if (minecraft.screen instanceof AbstractContainerScreen) local.closeContainer();
					minecraft.setScreen(null);
				}
				this.isInsidePortal = false;
			}
		}
	}
}