package twilightforest.client.model.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public class AnimatedItemModel implements ItemModel {
	private final ItemModel baseModel;

	private AnimatedItemModel(ItemModel baseModel) {
		this.baseModel = baseModel;
	}

	@Override
	public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext context, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		this.baseModel.update(state, stack, resolver, context, level, owner, seed);
		state.setAnimated();
	}

	public record Unbaked(ItemModel.Unbaked baseModel) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ItemModels.CODEC.fieldOf("base_model").forGetter(Unbaked::baseModel))
			.apply(instance, Unbaked::new));

		@Override
		public ItemModel bake(BakingContext context, Matrix4fc transformation) {
			return new AnimatedItemModel(this.baseModel().bake(context, transformation));
		}

		@Override
		public void resolveDependencies(Resolver resolver) {
			this.baseModel.resolveDependencies(resolver);
		}

		@Override
		public MapCodec<? extends ItemModel.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}