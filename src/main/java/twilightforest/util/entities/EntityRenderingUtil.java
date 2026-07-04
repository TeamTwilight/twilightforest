package twilightforest.util.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix3x2fc;
import org.joml.Quaternionf;
import twilightforest.TwilightForestMod;

import java.util.*;

public class EntityRenderingUtil {

	private static final Set<EntityType<?>> IGNORED_ENTITIES = new HashSet<>();
	public static final Map<EntityType<?>, Entity> ENTITY_MAP = new HashMap<>();

	@Nullable
	public static Entity fetchEntity(EntityType<?> type, @Nullable Level level) {
		if (level != null && !IGNORED_ENTITIES.contains(type)) {
			Entity entity;
			if (type == EntityType.PLAYER) {
				entity = Minecraft.getInstance().player;
			} else {
				entity = ENTITY_MAP.computeIfAbsent(type, t -> {
					Entity created = t.create(level, EntitySpawnReason.NATURAL);
					if (created != null) {
						created.setYRot(0.0F);
						created.setYHeadRot(0.0F);
						created.setYBodyRot(0.0F);
						created.hurtMarked = false;
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

	public static void renderEntity(GuiGraphicsExtractor graphics, EntityType<?> type, int size) {
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
				renderTheEntity(graphics, size / 2, size - 2, scale, living);
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing entity " + BuiltInRegistries.ENTITY_TYPE.getKey(type), e);
				IGNORED_ENTITIES.add(type);
				ENTITY_MAP.remove(type);
			}
		}
	}

	//[VanillaCopy] of InventoryScreen.renderEntityInInventory, with added rotations and some other modified values
	private static void renderTheEntity(GuiGraphicsExtractor graphics, int x, int y, int scale, LivingEntity entity) {
		Matrix3x2fStack posestack = graphics.pose();
		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
		quaternion.mul(quaternion1);
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
		posestack.pushMatrix();
		posestack.translate(x, y);
		applyAdditionalTransforms(entity.getType(), posestack);
		posestack.scale((float) scale, (float) scale);
		posestack.mul((Matrix3x2fc) quaternion);
		posestack.mul((Matrix3x2fc) Axis.XN.rotationDegrees(35.0F));
		posestack.mul((Matrix3x2fc) Axis.YN.rotationDegrees(145.0F));
		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		quaternion1.conjugate();
		posestack.popMatrix();
		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
	}

	//certain entities are a pain. This exists to fix vanilla cases.
	private static void applyAdditionalTransforms(EntityType<?> entity, Matrix3x2fStack stack) {
		if (entity == EntityType.GHAST) {
			stack.translate(0.0F, -12.5F);
			stack.scale(0.5F, 0.5F);
		}
		if (entity == EntityType.ENDER_DRAGON) stack.translate(0.0F, -4.0F);
		if (entity == EntityType.WITHER) stack.translate(0.0F, 8.0F);
		if (entity == EntityType.SQUID || entity == EntityType.GLOW_SQUID) stack.translate(0.0F, -19.0F);
		if (entity == EntityType.ELDER_GUARDIAN) stack.scale(0.6F, 0.6F);
	}

	public static void renderItemEntity(GuiGraphicsExtractor graphics, ItemStack stack, @Nullable Level level, float bobOffset) {
		Matrix3x2fStack posestack = graphics.pose();
		posestack.pushMatrix();
		posestack.translate(16.0F, 32.0F);
		posestack.scale(50.0F, 50.0F);
		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
		quaternion.mul(quaternion1);
		posestack.mul((Matrix3x2fc) quaternion);
		posestack.mul((Matrix3x2fc) Axis.XN.rotationDegrees(35.0F));
		posestack.mul((Matrix3x2fc) Axis.YN.rotationDegrees(145.0F));
		quaternion1.conjugate();
		ItemEntity item = (ItemEntity) fetchEntity(EntityType.ITEM, level);
		Objects.requireNonNull(item).setItem(stack);
		posestack.popMatrix();
	}

	//[VanillaCopy] of ItemEntityRenderer.render. I have to add my own bob offset and ticker since using the vanilla method has issues
	private static final ItemStackRenderState ITEM_RENDER_STATE = new ItemStackRenderState();

	private static void render(ItemEntity entity, float partialTicks, PoseStack stack, SubmitNodeCollector buffer, float bobOffset) {
		stack.pushPose();
		ItemStack itemstack = entity.getItem();

		var resolver = net.minecraft.client.Minecraft.getInstance().getItemModelResolver();

		resolver.updateForTopItem(
			ITEM_RENDER_STATE,
			itemstack,
			ItemDisplayContext.GROUND,
			entity.level(),
			entity,
			entity.getId()
		);

		float f1 = Mth.sin((Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().level).getGameTime() + partialTicks) / 10.0F + bobOffset) * 0.1F + 0.1F;

		stack.translate(0.0D, f1 + 0.25F, 0.0D);

		float f3 = getSpin(partialTicks, bobOffset);
		stack.mulPose(Axis.YP.rotation(f3));

		stack.pushPose();

		ITEM_RENDER_STATE.submit(stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, -1);

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
