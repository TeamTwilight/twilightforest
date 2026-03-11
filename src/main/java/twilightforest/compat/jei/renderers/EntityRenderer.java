package twilightforest.compat.jei.renderers;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.jei.FakeEntityType;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.List;

public class EntityRenderer implements IIngredientRenderer<FakeEntityType> {

	private final int size;

	public EntityRenderer(int size) {
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
	public void render(GuiGraphics graphics, @Nullable FakeEntityType type) {
		if (type != null) {
			EntityRenderingUtil.renderEntity(graphics, type.type(), this.size);
		}
	}

	@SuppressWarnings("removal") //we are absolutely forced to use this
	@Override
	public List<Component> getTooltip(FakeEntityType type, TooltipFlag flag) {
		return List.of();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FakeEntityType type, TooltipFlag flag) {
		tooltip.addAll(EntityRenderingUtil.getMobTooltip(type.type()));
	}
}