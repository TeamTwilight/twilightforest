package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.MagicPainting;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import javax.annotation.Nullable;
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

            CompoundTag compoundtag = stack.getTag();
            if (compoundtag != null) {
                if (!level.isClientSide()) {
                    EntityType.updateCustomEntityTag(level, player, painting, compoundtag);
                } else if (compoundtag.contains("EntityTag", 10)) {
                    // Fixes vanilla bug with painting items that have a set variant, where if you were to try to place it where it can't be placed, it would consume the item anyway
                    painting.setVariant(compoundtag.getCompound("EntityTag").getString("variant"));
                }
            }

            if (painting.survives()) {
                if (!level.isClientSide) {
                    painting.playPlacementSound();
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, painting.position());
                    level.addFreshEntity(painting);
                }

                stack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    protected boolean mayPlace(Player player, Direction direction, ItemStack stack, BlockPos pos) {
        return !direction.getAxis().isVertical() && player.mayUseItemAt(pos, direction, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);
        if (level != null) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("EntityTag", 10)) {
                CompoundTag entityTag = tag.getCompound("EntityTag");
                MagicPaintingVariant.getVariant(level.registryAccess(), entityTag.getString("variant")).ifPresent((variant) -> {
                    ResourceLocation location = new ResourceLocation(entityTag.getString("variant"));
                    components.add(Component.translatable(location.toLanguageKey("magic_painting", "title")).withStyle(ChatFormatting.YELLOW));
                    components.add(Component.translatable(location.toLanguageKey("magic_painting", "author")).withStyle(ChatFormatting.GRAY));
                    components.add(Component.translatable("painting.dimensions", Mth.positiveCeilDiv(variant.width(), 16), Mth.positiveCeilDiv(variant.height(), 16)));
                });
            }
        }
    }
}
