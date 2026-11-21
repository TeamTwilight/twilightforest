package twilightforest.compat.jei.renderers;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import twilightforest.compat.jei.FakeEntityType;
import twilightforest.client.EntityRenderingUtil;

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
	public void render(GuiGraphicsExtractor graphics, FakeEntityType type) {
		Matrix3x2fStack pose = graphics.pose();
		EntityRenderingUtil.renderEntity(graphics, type.type(), this.size, (int) pose.m20(), (int) pose.m21());
	}

	@SuppressWarnings("removal") //the interface still declares this one abstract
	@Override
	public List<Component> getTooltip(FakeEntityType type, TooltipFlag flag) {
		return List.of();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FakeEntityType type, Item.TooltipContext context, @Nullable Player player, TooltipFlag flag) {
		tooltip.addAll(EntityRenderingUtil.getMobTooltip(type.type()));
	}
}
