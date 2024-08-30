package twilightforest.client.model.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.TrollsteinnBlock;

public class TrollsteinnModel extends BakedModelWrapper<BakedModel> {
	public static final ModelResourceLocation LIT_TROLLSTEINN = ModelResourceLocation.standalone(TwilightForestMod.prefix("item/trollsteinn_light"));
	@Nullable
	private BakedModel litTrollsteinnModel;
	private final ItemOverrides overrides = new ItemOverrides() {
		@Override
		public BakedModel resolve(@NotNull BakedModel model, @NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
			if (TrollsteinnModel.this.litTrollsteinnModel == null)
				TrollsteinnModel.this.litTrollsteinnModel = Minecraft.getInstance().getModelManager().getModel(LIT_TROLLSTEINN);

			Entity itemEntity = (entity == null) ? stack.getEntityRepresentation() : entity;

			if (level == null || itemEntity == null) {
				return super.resolve(TrollsteinnModel.this.originalModel, stack, level, entity, seed);
			}

			int brightness = level.getMaxLocalRawBrightness(itemEntity.blockPosition(), TrollsteinnBlock.calculateServerSkyDarken(level));
			if (brightness > TrollsteinnBlock.LIGHT_THRESHOLD) {
				return super.resolve(TrollsteinnModel.this.litTrollsteinnModel, stack, level, entity, seed);
			} else {
				return super.resolve(TrollsteinnModel.this.originalModel, stack, level, entity, seed);
			}
		}
	};

	public TrollsteinnModel(BakedModel originalModel) {
		super(originalModel);
	}

	@Override
	public ItemOverrides getOverrides() {
		return overrides;
	}
}
