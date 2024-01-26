package twilightforest.util;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import twilightforest.TwilightForestMod;

import java.util.*;

public class EntityRenderingUtil {

	private static final Set<EntityType<?>> IGNORED_ENTITIES = new HashSet<>();
	private static final Map<EntityType<?>, Entity> ENTITY_MAP = new HashMap<>();

	@Nullable
	public static Entity fetchEntity(EntityType<?> type, @Nullable Level level) {
		if (level != null && !IGNORED_ENTITIES.contains(type)) {
			Entity entity;
			if (type == EntityType.PLAYER) {
				entity = Minecraft.getInstance().player;
			} else {
				entity = ENTITY_MAP.computeIfAbsent(type, t -> {
					Entity created = t.create(level);
					if (created != null) {
						created.setYRot(0.0F);
						created.setYHeadRot(0.0F);
						created.setYBodyRot(0.0F);
						created.hasImpulse = false;
						if (created instanceof Mob mob) {
							mob.setNoAi(true);
						}
					}
					return created;
				});
			}
			return entity;
		}
		return null;
	}

	public static void renderEntity(PoseStack stack, EntityType<?> type, int size) {
		Entity entity = fetchEntity(type, Minecraft.getInstance().level);
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
				PoseStack modelView = RenderSystem.getModelViewStack();
				modelView.pushPose();
				modelView.mulPoseMatrix(stack.last().pose());
				renderTheEntity(size / 2, size - 2, scale, living);
				modelView.popPose();
				RenderSystem.applyModelViewMatrix();
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing entity " + BuiltInRegistries.ENTITY_TYPE.getKey(type), e);
				IGNORED_ENTITIES.add(type);
				ENTITY_MAP.remove(type);
			}
		}
	}

	//[VanillaCopy] of InventoryScreen.renderEntityInInventory, with added rotations and some other modified values
	private static void renderTheEntity(int x, int y, int scale, LivingEntity entity) {
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(x, y, 1050.0D);
		applyAdditionalTransforms(entity.getType(), posestack);
		posestack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		posestack1.translate(0.0D, 0.0D, 1000.0D);
		if (entity instanceof EnderDragon) {
			posestack1.mulPose(Axis.YP.rotationDegrees(180.0F));
			posestack1.mulPose(Axis.XP.rotationDegrees(-30.0F));
		}
		posestack1.scale((float) scale, (float) scale, (float) scale);
		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
		quaternion.mul(quaternion1);
		posestack1.mulPose(quaternion);
		posestack1.mulPose(Axis.XN.rotationDegrees(35.0F));
		posestack1.mulPose(Axis.YN.rotationDegrees(145.0F));
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
		Lighting.setupForEntityInInventory();
		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		quaternion1.conjugate();
		dispatcher.overrideCameraOrientation(quaternion1);
		boolean hitboxes = dispatcher.shouldRenderHitBoxes();
		dispatcher.setRenderShadow(false);
		dispatcher.setRenderHitBoxes(false);
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, posestack1, multibuffersource$buffersource, 15728880));
		multibuffersource$buffersource.endBatch();
		dispatcher.setRenderShadow(true);
		dispatcher.setRenderHitBoxes(hitboxes);
		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}

	//certain entities are a pain. This exists to fix vanilla cases.
	private static void applyAdditionalTransforms(EntityType<?> entity, PoseStack stack) {
		if (entity == EntityType.GHAST) {
			stack.translate(0.0D, -12.5D, 0.0D);
			stack.scale(0.5F, 0.5F, 0.5F);
		}
		if (entity == EntityType.ENDER_DRAGON) stack.translate(0.0D, -4.0D, 0.0D);
		if (entity == EntityType.WITHER) stack.translate(0.0D, 8.0D, 0.0D);
		if (entity == EntityType.SQUID || entity == EntityType.GLOW_SQUID) stack.translate(0.0D, -19.0D, 0.0D);
		if (entity == EntityType.ELDER_GUARDIAN) stack.scale(0.6F, 0.6F, 0.6F);
	}

	public static void renderItemEntity(ItemStack stack, @Nullable Level level, float bobOffset) {
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(16.0D, 32.0D, 1050.0D);
		posestack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		posestack1.translate(0.0D, 0.0D, 1000.0D);
		posestack1.scale(50.0F, 50.0F, 50.0F);
		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
		quaternion.mul(quaternion1);
		posestack1.mulPose(quaternion);
		posestack1.mulPose(Axis.XN.rotationDegrees(35.0F));
		posestack1.mulPose(Axis.YN.rotationDegrees(145.0F));
		Lighting.setupForEntityInInventory();
		quaternion1.conjugate();
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		ItemEntity item = (ItemEntity) fetchEntity(EntityType.ITEM, level);
		Objects.requireNonNull(item).setItem(stack);
		RenderSystem.runAsFancy(() ->
				render(item, Minecraft.getInstance().getDeltaFrameTime(), posestack1, buffer, bobOffset));
		buffer.endBatch();
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		Lighting.setupFor3DItems();
	}

	//[VanillaCopy] of ItemEntityRenderer.render. I have to add my own bob offset and ticker since using the vanilla method has issues
	private static void render(ItemEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, float bobOffset) {
		stack.pushPose();
		ItemStack itemstack = entity.getItem();
		BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(itemstack, entity.level(), null, entity.getId());
		float f1 = Mth.sin((Objects.requireNonNull(Minecraft.getInstance().level).getGameTime() + partialTicks) / 10.0F + bobOffset) * 0.1F + 0.1F;
		float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
		stack.translate(0.0D, f1 + 0.25F * f2, 0.0D);
		float f3 = getSpin(partialTicks, bobOffset);
		stack.mulPose(Axis.YP.rotation(f3));

		stack.pushPose();

		Minecraft.getInstance().getItemRenderer().render(itemstack, ItemDisplayContext.GROUND, false, stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
		stack.popPose();


		stack.popPose();
	}

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
