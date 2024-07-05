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
import twilightforest.TwilightForestMod;
import twilightforest.block.TrollsteinnBlock;

import javax.annotation.Nullable;

public class TrollsteinnModel extends BakedModelWrapper<BakedModel> {
	private static BakedModel litTrollsteinnModel;
	private final ItemOverrides overrides = new ItemOverrides() {
		@Override
		public BakedModel resolve(@NotNull BakedModel model, @NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
			if (litTrollsteinnModel == null)
				litTrollsteinnModel = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(TwilightForestMod.prefix("item/trollsteinn_light")));

			Entity itemEntity = (entity == null) ? stack.getEntityRepresentation() : entity;

			if (level == null || itemEntity == null)
				return super.resolve(originalModel, stack, level, entity, seed);

			int brightness = level.getMaxLocalRawBrightness(itemEntity.blockPosition(), TrollsteinnBlock.calculateServerSkyDarken(level));
			if (brightness > TrollsteinnBlock.LIGHT_THRESHOLD)
				return super.resolve(litTrollsteinnModel, stack, level, entity, seed);
			return super.resolve(originalModel, stack, level, entity, seed);
		}
	};
	@Override
	public @NotNull ItemOverrides getOverrides() {
		return overrides;
	}
	public TrollsteinnModel(BakedModel originalModel) {
		super(originalModel);
	}
}
