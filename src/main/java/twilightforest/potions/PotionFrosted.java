package twilightforest.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.GuiUtils;

import java.util.UUID;

public class PotionFrosted extends Potion {

	public static final UUID MODIFIER_UUID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE4915A9");

	private static final ResourceLocation sprite = TwilightForestMod.prefix("textures/gui/frosty.png");

	public PotionFrosted(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		Minecraft.getMinecraft().renderEngine.bindTexture(sprite);
		GuiUtils.drawModalRectWithCustomSizedTexture(x + 3, y + 3, z, 0, 0, 18, 18, 18, 18);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		Minecraft.getMinecraft().renderEngine.bindTexture(sprite);
		GuiUtils.drawModalRectWithCustomSizedTexture(x + 6, y + 7, z, 0, 0, 18, 18, 18, 18);
	}
}
