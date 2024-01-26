package twilightforest.compat.jei.renderers;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.EntityRenderingUtil;

import java.util.List;

@SuppressWarnings("rawtypes")
public class EntityRenderer implements IIngredientRenderer<EntityType> {

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
	public void render(GuiGraphics graphics, @Nullable EntityType type) {
		if (type != null) {
			EntityRenderingUtil.renderEntity(graphics.pose(), type, this.size);
		}
	}

	@Override
	public List<Component> getTooltip(EntityType type, TooltipFlag flag) {
		return EntityRenderingUtil.getMobTooltip(type);
	}
}