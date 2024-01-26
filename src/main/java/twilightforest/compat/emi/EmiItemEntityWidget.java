package twilightforest.compat.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;
import twilightforest.util.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;

public class EmiItemEntityWidget extends Widget {
	private final ItemStack stack;
	private final Bounds bounds;
	private final float bobOffs;

	public EmiItemEntityWidget(ItemLike item, int x, int y) {
		this(new ItemStack(item), x, y);
	}

	public EmiItemEntityWidget(ItemStack stack, int x, int y) {
		this.stack = stack;
		this.bounds = new Bounds(x, y, 32, 32);
		this.bobOffs = RandomSource.create().nextFloat() * (float) Math.PI * 2.0F;
	}

	@Override
	public Bounds getBounds() {
		return this.bounds;
	}

	@Override
	public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
		List<ClientTooltipComponent> tooltip = new ArrayList<>();
		tooltip.add(ClientTooltipComponent.create(Component.translatable(this.stack.getDescriptionId()).getVisualOrderText()));
		if (Minecraft.getInstance().options.advancedItemTooltips) {
			tooltip.add(ClientTooltipComponent.create(Component.literal(BuiltInRegistries.ITEM.getKey(this.stack.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText()));
		}
		tooltip.add(ClientTooltipComponent.create(Component.literal(EntityRenderingUtil.getModIdForTooltip(BuiltInRegistries.ITEM.getKey(this.stack.getItem()).getNamespace())).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC).getVisualOrderText()));
		return tooltip;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		graphics.pose().pushPose();
		graphics.pose().translate(this.bounds.x(), this.bounds.y(), 0.0D);
		PoseStack modelView = RenderSystem.getModelViewStack();

		modelView.pushPose();
		modelView.mulPoseMatrix(graphics.pose().last().pose());

		try {
			EntityRenderingUtil.renderItemEntity(this.stack, Minecraft.getInstance().level, this.bobOffs);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.error("Error drawing item in EMI!", e);
		}

		modelView.popPose();
		RenderSystem.applyModelViewMatrix();
		graphics.pose().popPose();
	}
}

