package twilightforest.compat.jei.renderers;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.jei.FakeItemEntity;
import twilightforest.client.EntityRenderingUtil;

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
	public void render(GuiGraphicsExtractor graphics, FakeItemEntity item) {
		Level level = Minecraft.getInstance().level;
		if (level != null) {
			try {
				Matrix3x2fStack pose = graphics.pose();
				EntityRenderingUtil.renderItemEntity(graphics, item.stack(), (int) pose.m20(), (int) pose.m21(), this.bobOffs);
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing item in JEI!", e);
			}
		}
	}

	@SuppressWarnings("removal") //the interface still declares this one abstract
	@Override
	public List<Component> getTooltip(FakeItemEntity item, TooltipFlag flag) {
		return List.of();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FakeItemEntity item, Item.TooltipContext context, @Nullable Player player, TooltipFlag flag) {
		tooltip.add(item.stack().getHoverName());
		if (flag.isAdvanced()) {
			tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.stack().getItem())).toString()).withStyle(ChatFormatting.DARK_GRAY));
		}
	}
}
