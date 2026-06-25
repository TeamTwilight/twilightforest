package twilightforest.util.entities;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
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
					Entity created = t.create(level, EntitySpawnReason.LOAD);
					if (created != null) {
						created.setYRot(0.0F);
						created.setYHeadRot(0.0F);
						created.setYBodyRot(0.0F);
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
			int scale = size / 2;
			float height = entity.getBbHeight();
			float width = entity.getBbWidth();
			if (height > 2.25F || width > 2.25F) {
				scale = (int) (20 / Math.max(height, width));
			}
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
	// 26.1.2: Rewritten to use the new EntityRenderState-based rendering pipeline via GuiGraphicsExtractor.entity()
	private static void renderTheEntity(GuiGraphicsExtractor graphics, int x, int y, int scale, LivingEntity entity) {
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

		// 26.1.2: Extract EntityRenderState via the new rendering pipeline
		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		EntityRenderer<? super LivingEntity, ?> renderer = dispatcher.getRenderer(entity);
		EntityRenderState renderState = renderer.createRenderState(entity, 1.0F);
		renderState.shadowPieces.clear();
		renderState.outlineColor = 0;

		if (renderState instanceof LivingEntityRenderState livingRenderState) {
			livingRenderState.bodyRot = 0.0F;
			livingRenderState.yRot = 0.0F;
			livingRenderState.xRot = 0.0F;
		}

		// 26.1.2: Apply entity-specific transforms (model scale / translation)
		Vector3f additionalTranslation = new Vector3f();
		applyAdditionalTransforms(entity.getType(), renderState, additionalTranslation);

		quaternion.mul(Axis.XN.rotationDegrees(35.0F));
		quaternion.mul(Axis.YN.rotationDegrees(145.0F));

		quaternion1.conjugate();

		// Calculate area bounds for the entity rendering in the GUI
		int areaSize = scale * 4;
		int x0 = Math.max(0, x - areaSize / 2);
		int x1 = x + areaSize / 2;
		int y0 = Math.max(0, y - areaSize);
		int y1 = y + areaSize / 4;

		Vector3f translation = new Vector3f(
			additionalTranslation.x,
			additionalTranslation.y,
			50.0F + additionalTranslation.z
		);

		// 26.1.2: Use GuiGraphicsExtractor.entity() to render the entity into the GUI via PIP system.
		// Replaces the old Lighting.setupForEntityInInventory(), EntityRenderDispatcher.render(), graphics.bufferSource()/flush() approach.
		graphics.entity(renderState, scale, translation, quaternion, quaternion1, x0, y0, x1, y1);

		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
	}

	//certain entities are a pain. This exists to fix vanilla cases.
	// 26.1.2: Adapted to use EntityRenderState instead of PoseStack.
	private static void applyAdditionalTransforms(EntityType<?> entityType, EntityRenderState renderState, Vector3f translationOut) {
		if (renderState instanceof LivingEntityRenderState livingState) {
			if (entityType == EntityType.GHAST) {
				translationOut.add(0.0F, -12.5F, 0.0F);
				livingState.scale *= 0.5F;
			}
			if (entityType == EntityType.ENDER_DRAGON) {
				translationOut.add(0.0F, -4.0F, 0.0F);
			}
			if (entityType == EntityType.WITHER) {
				translationOut.add(0.0F, 8.0F, 0.0F);
			}
			if (entityType == EntityType.SQUID || entityType == EntityType.GLOW_SQUID) {
				translationOut.add(0.0F, -19.0F, 0.0F);
			}
			if (entityType == EntityType.ELDER_GUARDIAN) {
				livingState.scale *= 0.6F;
			}
		}
	}

	public static void renderItemEntity(GuiGraphicsExtractor graphics, ItemStack stack, @Nullable Level level, float bobOffset) {
		// 26.1.2: Rewritten to use EntityRenderState pipeline.
		// The old custom PoseStack + ItemEntityRenderer approach is replaced by graphics.entity().
		ItemEntity item = (ItemEntity) fetchEntity(EntityType.ITEM, level);
		Objects.requireNonNull(item).setItem(stack);
		// item.bobOffs is final in 26.1.2; set on render state below

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		EntityRenderer<? super ItemEntity, ?> renderer = dispatcher.getRenderer(item);
		float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
		EntityRenderState renderState = renderer.createRenderState(item, partialTick);
		renderState.shadowPieces.clear();
		if (renderState instanceof ItemEntityRenderState itemState) {
			itemState.bobOffset = bobOffset;
			itemState.shouldBob = true;
		}

		Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
		Quaternionf quaternion1 = Axis.XP.rotationDegrees(20.0F);
		quaternion.mul(quaternion1);
		quaternion.mul(Axis.XN.rotationDegrees(35.0F));
		quaternion.mul(Axis.YN.rotationDegrees(145.0F));
		quaternion1.conjugate();

		// 26.1.2: Uses graphics.entity() with the item's render state.
		// Lighting, bufferSource, flush, and getItemRenderer() are all handled internally by the PIP system.
		graphics.entity(
			renderState,
			50,
			new Vector3f(0.0F, 0.0F, 50.0F),
			quaternion,
			quaternion1,
			0, 0, 32, 64
		);
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
