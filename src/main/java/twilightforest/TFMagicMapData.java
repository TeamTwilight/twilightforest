package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.world.registration.TFFeature;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFMagicMapData extends MapItemSavedData {
	private static final Map<String, TFMagicMapData> CLIENT_DATA = new HashMap<>();

	public final Set<TFMapDecoration> tfDecorations = new HashSet<>();

	public TFMagicMapData(int x, int z, byte scale, boolean trackpos, boolean unlimited, boolean locked, ResourceKey<Level> dim) {
		super(x, z, scale, trackpos, unlimited, locked, dim);
	}

	public static TFMagicMapData load(CompoundTag nbt) {
		MapItemSavedData data = MapItemSavedData.load(nbt);
		final boolean trackingPosition = !nbt.contains("trackingPosition", 1) || nbt.getBoolean("trackingPosition");
		final boolean unlimitedTracking = nbt.getBoolean("unlimitedTracking");
		final boolean locked = nbt.getBoolean("locked");
		TFMagicMapData tfdata = new TFMagicMapData(data.x, data.z, data.scale, trackingPosition, unlimitedTracking, locked, data.dimension);

		tfdata.colors = data.colors;
		tfdata.bannerMarkers.putAll(data.bannerMarkers);
		tfdata.decorations.putAll(data.decorations);
		tfdata.frameMarkers.putAll(data.frameMarkers);
		tfdata.trackedDecorationCount = data.trackedDecorationCount;

		byte[] featureStorage = nbt.getByteArray("features");
		if (featureStorage.length > 0) {
			tfdata.deserializeFeatures(featureStorage);
		}

		return tfdata;
	}

	@Override
	public CompoundTag save(CompoundTag cmp) {
		cmp = super.save(cmp);

		if (this.tfDecorations.size() > 0) {
			cmp.putByteArray("features", serializeFeatures());
		}

		return cmp;
	}

	/**
	 * Checks existing features against the feature cache changes wrong ones
	 */
	public void checkExistingFeatures(Level world) {
		List<TFMapDecoration> toRemove = new ArrayList<>();
		List<TFMapDecoration> toAdd = new ArrayList<>();

		for (TFMapDecoration coord : tfDecorations) {
			int worldX = (coord.getX() << this.scale - 1) + this.x;
			int worldZ = (coord.getY() << this.scale - 1) + this.z;

			int trueId = TFMapDecoration.ICONS_FLIPPED.getInt(TFFeature.getFeatureAt(worldX, worldZ, (ServerLevel) world));
			if (coord.featureId != trueId) {
				toRemove.add(coord);
				toAdd.add(new TFMapDecoration(trueId, coord.getX(), coord.getY(), coord.getRot()));
			}
		}

		toRemove.forEach(tfDecorations::remove);
		tfDecorations.addAll(toAdd);
	}

	public void deserializeFeatures(byte[] arr) {
		this.tfDecorations.clear();

		for (int i = 0; i < arr.length / 3; ++i) {
			byte featureId = arr[i * 3];
			byte mapX = arr[i * 3 + 1];
			byte mapZ = arr[i * 3 + 2];
			byte mapRotation = 8;
			this.tfDecorations.add(new TFMapDecoration(featureId, mapX, mapZ, mapRotation));
		}
	}

	public byte[] serializeFeatures() {
		byte[] storage = new byte[this.tfDecorations.size() * 3];

		int i = 0;
		for (TFMapDecoration featureCoord : tfDecorations) {
			storage[i * 3] = (byte) featureCoord.featureId;
			storage[i * 3 + 1] = featureCoord.getX();
			storage[i * 3 + 2] = featureCoord.getY();
			i++;
		}

		return storage;
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMagicMapData getMagicMapData(Level world, String name) {
		if (world.isClientSide) {
			return CLIENT_DATA.get(name);
		} else {
			return world.getServer().overworld().getDataStorage().get(TFMagicMapData::load, name);
		}
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMagicMapData(Level world, TFMagicMapData data, String id) {
		if (world.isClientSide) {
			CLIENT_DATA.put(id, data);
		} else {
			world.getServer().overworld().getDataStorage().set(id, data);
		}
	}

	public static class TFMapDecoration extends MapDecoration {

		private static final Int2ObjectArrayMap<TFFeature> ICONS = new Int2ObjectArrayMap<>(){{
			defaultReturnValue(TFFeature.NOTHING);
			put(0, TFFeature.NOTHING);
			put(1, TFFeature.SMALL_HILL);
			put(2, TFFeature.MEDIUM_HILL);
			put(3, TFFeature.LARGE_HILL);
			put(4, TFFeature.HEDGE_MAZE);
			put(5, TFFeature.NAGA_COURTYARD);
			put(6, TFFeature.LICH_TOWER);
			put(7, TFFeature.ICE_TOWER);
			put(9, TFFeature.QUEST_GROVE);
			put(12, TFFeature.HYDRA_LAIR);
			put(13, TFFeature.LABYRINTH);
			put(14, TFFeature.DARK_TOWER);
			put(15, TFFeature.KNIGHT_STRONGHOLD);
			put(17, TFFeature.YETI_CAVE);
			put(18, TFFeature.TROLL_CAVE);
			put(19, TFFeature.FINAL_CASTLE);
		}};
		private static final Object2IntArrayMap<TFFeature> ICONS_FLIPPED = new Object2IntArrayMap<>(){{
			ICONS.forEach((k, v) -> put(v, k.intValue()));
		}};

		@OnlyIn(Dist.CLIENT)
		public static class RenderContext {
			private static final RenderType MAP_ICONS = RenderType.text(TwilightForestMod.prefix("textures/gui/mapicons.png"));
			public static PoseStack stack;
			public static MultiBufferSource buffer;
			public static int light;
		}

		final int featureId;

		public TFMapDecoration(TFFeature featureId, byte xIn, byte yIn, byte rotationIn) {
			this(ICONS_FLIPPED.getInt(featureId), xIn, yIn, rotationIn);
		}

		public TFMapDecoration(int featureId, byte xIn, byte yIn, byte rotationIn) {
			super(Type.TARGET_X, xIn, yIn, rotationIn, new TranslatableComponent("map.magic.text")); //TODO: Shush for now
			this.featureId = featureId;
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public boolean render(int idx) {
			// TODO: Forge needs to pass in the ms and buffers, but for now this works
			if (ICONS.get(featureId).isStructureEnabled) {
				RenderContext.stack.pushPose();
				RenderContext.stack.translate(0.0F + getX() / 2.0F + 64.0F, 0.0F + getY() / 2.0F + 64.0F, -0.02F);
				RenderContext.stack.mulPose(Vector3f.ZP.rotationDegrees(getRot() * 360 / 16.0F));
				RenderContext.stack.scale(4.0F, 4.0F, 3.0F);
				RenderContext.stack.translate(-0.125D, 0.125D, 0.0D);
				float f1 = featureId % 8 / 8.0F;
				float f2 = featureId / 8 / 8.0F;
				float f3 = (featureId % 8 + 1) / 8.0F;
				float f4 = (featureId / 8 + 1) / 8.0F;
				Matrix4f matrix4f1 = RenderContext.stack.last().pose();
				VertexConsumer ivertexbuilder1 = RenderContext.buffer.getBuffer(RenderContext.MAP_ICONS);
				ivertexbuilder1.vertex(matrix4f1, -1.0F, 1.0F, idx * -0.001F).color(255, 255, 255, 255).uv(f1, f2).uv2(RenderContext.light).endVertex();
				ivertexbuilder1.vertex(matrix4f1, 1.0F, 1.0F, idx * -0.001F).color(255, 255, 255, 255).uv(f3, f2).uv2(RenderContext.light).endVertex();
				ivertexbuilder1.vertex(matrix4f1, 1.0F, -1.0F, idx * -0.001F).color(255, 255, 255, 255).uv(f3, f4).uv2(RenderContext.light).endVertex();
				ivertexbuilder1.vertex(matrix4f1, -1.0F, -1.0F, idx * -0.001F).color(255, 255, 255, 255).uv(f1, f4).uv2(RenderContext.light).endVertex();
				RenderContext.stack.popPose();
			}
			return true;
		}

		@Override
		public boolean equals(Object o) {
			if (super.equals(o) && o instanceof TFMapDecoration) {
				TFMapDecoration other = (TFMapDecoration) o;
				return this.featureId == other.featureId;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return super.hashCode() * 31 + featureId;
		}
	}
}
