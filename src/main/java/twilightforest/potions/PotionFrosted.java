package twilightforest.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;

public class PotionFrosted extends Potion {

	public static final String MODIFIER_UUID = "CE9DBC2A-EE3F-43F5-9DF7-F7F1EE4915A9";

	private static final ResourceLocation sprite = new ResourceLocation(TwilightForestMod.ID, "textures/gui/frosty.png");

	public PotionFrosted(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		mc.renderEngine.bindTexture(sprite);
		net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		mc.renderEngine.bindTexture(sprite);
		net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
	}
}
