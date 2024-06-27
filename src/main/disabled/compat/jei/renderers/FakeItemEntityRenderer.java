package twilightforest.compat.jei.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.jei.FakeItemEntity;
import twilightforest.util.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeItemEntityRenderer implements IIngredientRenderer<FakeItemEntity> {

	private final float bobOffs;
	private final int size;

	public FakeItemEntityRenderer(int size) {
		this.bobOffs = RandomSource.create().nextFloat() * (float) Math.PI * 2.0F;
		this.size = size;
	}

	@Override
	public int getWidth() {
		return this.size;
	}

	@Override
	public int getHeight() {
		return this.size;
	}

	@Override
	public void render(GuiGraphics graphics, @Nullable FakeItemEntity item) {
		Level level = Minecraft.getInstance().level;
		if (item != null && level != null) {
			try {
				PoseStack modelView = RenderSystem.getModelViewStack();
				modelView.pushPose();
				modelView.mulPoseMatrix(graphics.pose().last().pose());
				EntityRenderingUtil.renderItemEntity(item.stack(), level, this.bobOffs);
				modelView.popPose();
				RenderSystem.applyModelViewMatrix();
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing item in JEI!", e);
			}
		}
	}

	@Override
	public List<Component> getTooltip(FakeItemEntity item, TooltipFlag flag) {
		List<Component> tooltip = new ArrayList<>();
		tooltip.add(item.stack().getItem().getDescription());
		if (flag.isAdvanced()) {
			tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.stack().getItem())).toString()).withStyle(ChatFormatting.DARK_GRAY));
		}
		return tooltip;
	}


}
