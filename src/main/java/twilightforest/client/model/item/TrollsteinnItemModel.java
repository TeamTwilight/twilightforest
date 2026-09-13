package twilightforest.client.model.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;
import twilightforest.block.TrollsteinnBlock;

// [VanillaCopy] ConditionalItemModel but use ItemOwner instead of livingEntity
public class TrollsteinnItemModel implements ItemModel {
    private final ItemModel onTrue;
    private final ItemModel onFalse;

    public TrollsteinnItemModel(ItemModel onTrue, ItemModel onFalse) {
        this.onTrue = onTrue;
        this.onFalse = onFalse;
    }

    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        output.appendModelIdentityElement(this);
        (get(level, owner) ? this.onTrue : this.onFalse).update(output, item, resolver, displayContext, level, owner, seed);
    }

    public boolean get(@Nullable ClientLevel level, @Nullable ItemOwner owner) {
        Entity itemEntity = owner instanceof ItemEntity item ? item : owner != null ? owner.asLivingEntity() : null;
        if (level == null || itemEntity == null) {
            return false;
        }
        int brightness = level.getMaxLocalRawBrightness(itemEntity.blockPosition(), level.getSkyDarken());
        return brightness > TrollsteinnBlock.LIGHT_THRESHOLD;
    }

    public record Unbaked(ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(
                ItemModels.CODEC.fieldOf("on_true").forGetter(Unbaked::onTrue),
                ItemModels.CODEC.fieldOf("on_false").forGetter(Unbaked::onFalse)
        ).apply(i, Unbaked::new));

        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            return new TrollsteinnItemModel(this.onTrue.bake(context, transformation), this.onFalse.bake(context, transformation));
        }

        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            this.onTrue.resolveDependencies(resolver);
            this.onFalse.resolveDependencies(resolver);
        }
    }
}