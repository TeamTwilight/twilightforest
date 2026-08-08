package twilightforest.client;

import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;

import java.util.*;

public class EntityRenderingUtil {

	public static void renderEntity(GuiGraphicsExtractor graphics, EntityType<?> type, int size) {
		Entity entity = EntityCache.fetchEntity(type);
		if (entity instanceof LivingEntity living) {
			// scale down large mobs, but don't scale up small ones
			int scale = size / 2;
			float height = entity.getBbHeight();
			float width = entity.getBbWidth();
			if (height > 2.25F || width > 2.25F) {
				scale = (int) (20 / Math.max(height, width));
			}
			// catch exceptions drawing the entity to be safe, any caught exceptions blacklist the entity
			try {
				renderTheEntity(graphics, size / 2, size - 2, scale, living);
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing entity " + BuiltInRegistries.ENTITY_TYPE.getKey(type), e);
				EntityCache.addEntityToBlacklist(type);
			}
		}
	}

	//[VanillaCopy] of InventoryScreen.renderEntityInInventory, with added rotations and some other modified values
	private static void renderTheEntity(GuiGraphicsExtractor graphics, int x, int y, int scale, LivingEntity entity) {
		Quaternionf rotation = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf xRotation = Axis.XP.rotationDegrees(20.0F);
		rotation.mul(xRotation);
		float f2 = entity.yBodyRot;
		float f3 = entity.getYRot();
		float f4 = entity.getXRot();
		float f5 = entity.yHeadRotO;
		float f6 = entity.yHeadRot;
		entity.yBodyRot = 0.0F;
		entity.setYRot(0.0F);
		entity.setXRot(0.0F);
		entity.yHeadRot = entity.getYRot();
		entity.yHeadRotO = entity.getYRot();

		EntityRenderState renderState = Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(entity, 0f);

		Vector3f translation = new Vector3f(entity.getBbWidth() > 1.0F ? -0.175F : 0.0F, renderState.boundingBoxHeight / 2.0F + 0.15F, 0.0F);
		scale = applyAdditionalTransforms(entity.getType(), translation, rotation, scale);

		graphics.entity(renderState, scale * 0.75F, translation, rotation, new Quaternionf(), x, y, x + 32, y + 32);

		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
	}

	//certain entities are a pain. This exists to fix vanilla cases.
	private static int applyAdditionalTransforms(EntityType<?> entity, Vector3f translation, Quaternionf rotation, float scale) {
		if (entity == EntityType.GHAST || entity == EntityType.HAPPY_GHAST) {
			translation.add(0.0F, -1.25F, 0.0F);
			scale *= 0.5F;
		}
		if (entity == EntityType.ENDER_DRAGON) {
			translation.add(0.0F, -2.0F, 0.0F);
			rotation.mul(Axis.YP.rotationDegrees(180.0F));
			scale *= 0.5F;
		}
		if (entity == EntityType.WITHER) translation.add(0.0F, 0.25F, 0.0F);
		if (entity == EntityType.SQUID || entity == EntityType.GLOW_SQUID) translation.add(0.0F, -0.75F, 0.0F);
		return Math.round(scale);
	}

	// TODO Will the ItemViewer mods need to manually provide x and y coords or is the MatrixStack good enough? Implementation dependent
	public static void renderItemEntity(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y, float bobOffset) {
		ItemEntity itemEntity = EntityCache.fetchItemEntity(stack);
		if (itemEntity == null)
			return;

		if (!(Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(itemEntity, 0f) instanceof ItemEntityRenderState renderState))
			return;

		renderState.bobOffset = bobOffset;

		// TODO verify that the rotations and translations are correct, see below commented method
		Quaternionf rotation = Axis.ZP.rotationDegrees(180.0F);
		rotation.mul(Axis.XN.rotationDegrees(15.0F));
		float partialTicks = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

		float spin = getSpin(partialTicks, bobOffset);
		rotation.mul(Axis.YP.rotation(spin - 145.0F));

		float yBob = Mth.sin((Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + partialTicks) / 10.0F + bobOffset) * 0.1F + 0.1F;
		ItemTransforms noTransforms = ItemTransforms.NO_TRANSFORMS; // ItemModel bakedmodel = Minecraft.getInstance().getModelManager().getItemModel(stack.get(DataComponents.ITEM_MODEL));
		float f2 = noTransforms.getTransform(ItemDisplayContext.GROUND).scale().y();

		Vector3f translation = new Vector3f(16, 32 + yBob + 0.25F * f2, 50);

		graphics.entity(renderState, 50, translation, rotation, null, x, y, x + 32, y + 32);
	}

	//[VanillaCopy] of ItemEntityRenderer.render. I have to add my own bob offset and ticker since using the vanilla method has issues
//	private static void render(ItemEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, float bobOffset) {
//		stack.pushPose();
//		ItemStack itemstack = entity.getItem();
//		BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, entity.level(), null, entity.getId());
//		float f1 = Mth.sin((Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + partialTicks) / 10.0F + bobOffset) * 0.1F + 0.1F;
//		float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
//		stack.translate(0.0D, f1 + 0.25F * f2, 0.0D);
//		float f3 = getSpin(partialTicks, bobOffset);
//		stack.mulPose(Axis.YP.rotation(f3));
//
//		stack.pushPose();
//
//		Minecraft.getInstance().getItemRenderer().render(itemstack, ItemDisplayContext.GROUND, false, stack, buffer, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, bakedmodel);
//		stack.popPose();
//
//
//		stack.popPose();
//	}

	private static float getSpin(float partialTicks, float bobOffset) {
		return (Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + partialTicks) / 20.0F + bobOffset;
	}

	public static List<Component> getMobTooltip(EntityType<?> type) {
		List<Component> components = new ArrayList<>();
		components.add(type.getDescription());
		if (Minecraft.getInstance().options.advancedItemTooltips) {
			components.add(Component.literal(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString()).withStyle(ChatFormatting.DARK_GRAY));
		}
		return components;
	}

	public static String getModIdForTooltip(String modId) {
		return ModList.get().getModContainerById(modId)
				.map(ModContainer::getModInfo)
				.map(IModInfo::getDisplayName)
				.orElseGet(() -> StringUtils.capitalize(modId));
	}
}
