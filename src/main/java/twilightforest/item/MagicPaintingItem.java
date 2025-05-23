package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.MagicPainting;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.List;
import java.util.Optional;

public class MagicPaintingItem extends Item {
	public MagicPaintingItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Direction face = context.getClickedFace();
		BlockPos relative = context.getClickedPos().relative(face);
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		if (player != null && !this.mayPlace(player, face, stack, relative)) {
			return InteractionResult.FAIL;
		} else {
			Level level = context.getLevel();
			Optional<MagicPainting> optional = MagicPainting.create(level, relative, face);
			if (optional.isEmpty()) return InteractionResult.CONSUME;
			MagicPainting painting = optional.get();

			Holder<MagicPaintingVariant> magicPaintingVariantHolder = stack.get(TFDataComponents.MAGIC_PAINTING_VARIANT);

			if (magicPaintingVariantHolder != null) {
				painting.setVariant(magicPaintingVariantHolder);
			}

			if (painting.survives()) {
				if (!level.isClientSide) {
					painting.playPlacementSound();
					level.gameEvent(player, GameEvent.ENTITY_PLACE, painting.position());
					level.addFreshEntity(painting);
				}

				stack.shrink(1);
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		}
	}

	protected boolean mayPlace(Player player, Direction direction, ItemStack stack, BlockPos pos) {
		return !direction.getAxis().isVertical() && player.mayUseItemAt(pos, direction, stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, context, components, isAdvanced);
		Holder<MagicPaintingVariant> magicPainting = stack.get(TFDataComponents.MAGIC_PAINTING_VARIANT);

		if (magicPainting != null) {
			MagicPaintingVariant painting = magicPainting.value();
			ResourceLocation location = magicPainting.unwrapKey().orElse(MagicPaintingVariants.DEFAULT).location();
			components.add(Component.translatable(location.toLanguageKey("magic_painting", "title")).withStyle(ChatFormatting.YELLOW));
			components.add(Component.empty().withStyle(ChatFormatting.GRAY).append(painting.author()));
			components.add(Component.translatable("painting.dimensions", Mth.positiveCeilDiv(painting.width(), 16), Mth.positiveCeilDiv(painting.height(), 16)));
		}
	}
}
