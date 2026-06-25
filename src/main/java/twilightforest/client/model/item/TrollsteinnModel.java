package twilightforest.client.model.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import twilightforest.TwilightForestMod;
import twilightforest.block.TrollsteinnBlock;

public class TrollsteinnModel implements ItemModel {
	public static final Identifier LIT_TROLLSTEINN = TwilightForestMod.prefix("item/trollsteinn_light");
	public static final ModelDebugName DEBUG_NAME = () -> "TrollsteinnModel";

	private final ItemModel baseModel;
	@Nullable
	private ItemModel litTrollsteinnModel;

	public TrollsteinnModel(ItemModel baseModel) {
		this.baseModel = baseModel;
	}

	@Override
	public void update(
		ItemStackRenderState output,
		@NotNull ItemStack stack,
		ItemModelResolver resolver,
		ItemDisplayContext displayContext,
		@Nullable ClientLevel level,
		@Nullable ItemOwner owner,
		int seed
	) {
		if (this.litTrollsteinnModel == null)
			this.litTrollsteinnModel = Minecraft.getInstance().getModelManager().getItemModel(LIT_TROLLSTEINN);

		Entity itemEntity = (owner instanceof LivingEntity living) ? living : (owner instanceof Entity entity ? entity : null);

		if (level == null || itemEntity == null || this.litTrollsteinnModel == null) {
			this.baseModel.update(output, stack, resolver, displayContext, level, owner, seed);
			return;
		}

		int brightness = level.getMaxLocalRawBrightness(itemEntity.blockPosition(), level.getSkyDarken());
		if (brightness > TrollsteinnBlock.LIGHT_THRESHOLD) {
			this.litTrollsteinnModel.update(output, stack, resolver, displayContext, level, owner, seed);
		} else {
			this.baseModel.update(output, stack, resolver, displayContext, level, owner, seed);
		}
	}

	// ======================== 内部 Unbaked 记录 ========================

	public record Unbaked(ItemModel.Unbaked baseModel) implements ItemModel.Unbaked {

		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
				ItemModels.CODEC.fieldOf("model").forGetter(Unbaked::baseModel)
			).apply(instance, Unbaked::new)
		);

		@Override
		public ItemModel bake(BakingContext context, Matrix4fc transformation) {
			return new TrollsteinnModel(this.baseModel().bake(context, transformation));
		}

		@Override
		public void resolveDependencies(Resolver resolver) {
			this.baseModel().resolveDependencies(resolver);
			resolver.markDependency(LIT_TROLLSTEINN);
		}

		@Override
		public MapCodec<? extends ItemModel.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}