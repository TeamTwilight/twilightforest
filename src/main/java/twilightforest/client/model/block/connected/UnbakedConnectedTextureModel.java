package twilightforest.client.model.block.connected;

import com.mojang.datafixers.util.Either;
import com.mojang.math.Quadrant;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.*;
import java.util.function.Predicate;

/**
 * Blockstate-level definition of a 5-texture connected texture block model.
 * <p>
 * Every face is quartered into 8x8 (model-space) squares. Each square picks one of five textures depending on which of its
 * two in-plane neighbors (and the diagonal between them) are connectable blocks: the plain overlay texture when nothing
 * connects, otherwise one of the four quadrants of the packed {@code overlay_connected} sheet (see {@link ConnectionLogic}).
 * An optional {@code base_texture} is rendered underneath the overlay on every face, without any connection logic.
 * <p>
 * Used in a blockstate file as {@code {"type": "twilightforest:connected_texture_block", "textures": {...}, ...}}.
 */
public record UnbakedConnectedTextureModel(Textures textures, List<Either<TagKey<Block>, Block>> connectableBlocks, OverlaySettings overlay, LayerSettings base, Element element, boolean ambientOcclusion) implements CustomUnbakedBlockStateModel {

	private static final Codec<Either<TagKey<Block>, Block>> CONNECTABLE_CODEC = Codec.either(TagKey.hashedCodec(Registries.BLOCK), BuiltInRegistries.BLOCK.byNameCodec());

	public static final MapCodec<UnbakedConnectedTextureModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Textures.CODEC.fieldOf("textures").forGetter(UnbakedConnectedTextureModel::textures),
		CONNECTABLE_CODEC.listOf().optionalFieldOf("connectable_blocks", List.of()).forGetter(UnbakedConnectedTextureModel::connectableBlocks),
		OverlaySettings.CODEC.optionalFieldOf("connected_texture", OverlaySettings.DEFAULT).forGetter(UnbakedConnectedTextureModel::overlay),
		LayerSettings.CODEC.optionalFieldOf("base", LayerSettings.DEFAULT).forGetter(UnbakedConnectedTextureModel::base),
		Element.CODEC.optionalFieldOf("element", Element.FULL_BLOCK).forGetter(UnbakedConnectedTextureModel::element),
		Codec.BOOL.optionalFieldOf("ambient_occlusion", true).forGetter(UnbakedConnectedTextureModel::ambientOcclusion)
	).apply(instance, UnbakedConnectedTextureModel::new));

	private static final int CENTER = 8;

	@Override
	public BlockStateModel bake(ModelBaker baker) {
		ModelDebugName debugName = () -> "Connected texture model with overlay " + this.textures.overlay().sprite();
		MaterialBaker materials = baker.materials();

		//index 0 is used by ConnectionLogic.NONE, index 1 by everything else
		Material.Baked[] overlaySprites = new Material.Baked[]{materials.get(this.textures.overlay(), debugName), materials.get(this.textures.overlayConnected(), debugName)};
		@Nullable Material.Baked baseSprite = this.textures.base().map(material -> materials.get(material, debugName)).orElse(null);
		Material.Baked particle = materials.get(this.textures.particle().or(this.textures::base).orElse(this.textures.overlay()), debugName);

		Vector3fc from = this.element.from();
		Vector3fc to = this.element.to();

		Map<Direction, BakedQuad[]> baseQuads = new EnumMap<>(Direction.class);
		Map<Direction, BakedQuad[][]> connectedQuads = new EnumMap<>(Direction.class);
		Set<Direction> unculledFaces = EnumSet.noneOf(Direction.class);
		int materialFlags = 0;

		for (Direction face : Direction.values()) {
			Direction cull = getCullface(face, from, to);
			if (cull == null) unculledFaces.add(face);

			Direction[] planeDirections = ConnectionLogic.AXIS_PLANE_DIRECTIONS[face.getAxis().ordinal()];
			BakedQuad[] faceBaseQuads = new BakedQuad[4];
			BakedQuad[][] faceConnectedQuads = new BakedQuad[4][ConnectionLogic.values().length];

			for (int quadrant = 0; quadrant < 4; quadrant++) {
				//each quadrant is the 8x8x8 corner of the block shared by this face and two of its in-plane neighbors, clipped to the element bounds
				Vec3i corner = face.getUnitVec3i().offset(planeDirections[quadrant].getUnitVec3i()).offset(planeDirections[(quadrant + 1) % 4].getUnitVec3i()).offset(1, 1, 1).multiply(CENTER);
				Vector3f quadrantFrom = new Vector3f(
					Math.clamp(Math.min(CENTER - (16.0F - to.x()), corner.getX() + from.x()), 0.0F, 16.0F),
					Math.clamp(Math.min(CENTER - (16.0F - to.y()), corner.getY() + from.y()), 0.0F, 16.0F),
					Math.clamp(Math.min(CENTER - (16.0F - to.z()), corner.getZ() + from.z()), 0.0F, 16.0F));
				Vector3f quadrantTo = new Vector3f(
					to.x() < CENTER ? to.x() : Math.max(CENTER, corner.getX() - (16.0F - to.x())),
					to.y() < CENTER ? to.y() : Math.max(CENTER, corner.getY() - (16.0F - to.y())),
					to.z() < CENTER ? to.z() : Math.max(CENTER, corner.getZ() - (16.0F - to.z())));
				CuboidFace.UVs uvs = FaceBakery.defaultFaceUV(quadrantFrom, quadrantTo, face);

				if (baseSprite != null) {
					BakedQuad quad = bakeQuad(baker, quadrantFrom, quadrantTo, face, cull, ConnectionLogic.NONE.remapUVs(uvs), baseSprite, this.base);
					faceBaseQuads[quadrant] = quad;
					materialFlags |= quad.materialInfo().flags();
				}

				for (ConnectionLogic logic : ConnectionLogic.values()) {
					BakedQuad quad = bakeQuad(baker, quadrantFrom, quadrantTo, face, cull, logic.remapUVs(uvs), logic.chooseTexture(overlaySprites), this.overlay.layer());
					faceConnectedQuads[quadrant][logic.ordinal()] = quad;
					materialFlags |= quad.materialInfo().flags();
				}
			}

			if (baseSprite != null) baseQuads.put(face, faceBaseQuads);
			connectedQuads.put(face, faceConnectedQuads);
		}

		return new ConnectedTextureModel(this.overlay.faces(), unculledFaces, this.overlay.renderOnAllFaces(), this.connectionPredicate(), baseQuads, connectedQuads, this.ambientOcclusion, particle, materialFlags);
	}

	private static BakedQuad bakeQuad(ModelBaker baker, Vector3fc from, Vector3fc to, Direction face, @Nullable Direction cull, CuboidFace.UVs uvs, Material.Baked material, LayerSettings layer) {
		CuboidFace cuboidFace = new CuboidFace(cull, layer.tintIndex(), material.sprite().contents().name().toString(), uvs, Quadrant.R0);
		return FaceBakery.bakeQuad(baker, from, to, cuboidFace, material, face, BlockModelRotation.IDENTITY, null, true, layer.lightEmission());
	}

	/**
	 * A face may only be culled against its neighbor when the element actually touches that side of the block.
	 */
	@Nullable
	private static Direction getCullface(Direction face, Vector3fc from, Vector3fc to) {
		boolean onBoundary = switch (face) {
			case DOWN -> from.y() == 0.0F;
			case UP -> to.y() == 16.0F;
			case NORTH -> from.z() == 0.0F;
			case SOUTH -> to.z() == 16.0F;
			case WEST -> from.x() == 0.0F;
			case EAST -> to.x() == 16.0F;
		};

		return onBoundary ? face : null;
	}

	/**
	 * Tags are checked at render time rather than resolved here, as tags are not bound yet while models bake.
	 */
	private Predicate<BlockState> connectionPredicate() {
		List<Block> blocks = new ArrayList<>();
		List<TagKey<Block>> tags = new ArrayList<>();
		for (Either<TagKey<Block>, Block> connectable : this.connectableBlocks) {
			connectable.ifLeft(tags::add).ifRight(blocks::add);
		}

		return state -> {
			for (Block block : blocks) if (state.is(block)) return true;
			for (TagKey<Block> tag : tags) if (state.is(tag)) return true;
			return false;
		};
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
	}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MAP_CODEC;
	}

	public static Builder builder(Material overlay) {
		return builder(overlay, "_ctm");
	}

	public static Builder builder(Material overlay, String suffix) {
		return builder(overlay, new Material(overlay.sprite().withSuffix(suffix), overlay.forceTranslucent()));
	}

	public static Builder builder(Material overlay, Material overlayConnected) {
		return new Builder(overlay, overlayConnected);
	}

	/**
	 * @param overlay          the plain texture, used where no connection happens
	 * @param overlayConnected the packed 2x2 sheet of connection textures, see {@link ConnectionLogic}
	 * @param base             optional texture rendered underneath the overlay on all faces
	 * @param particle         defaults to the base texture, or the overlay if there is no base
	 */
	public record Textures(Material overlay, Material overlayConnected, Optional<Material> base, Optional<Material> particle) {
		public static final Codec<Textures> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Material.CODEC.fieldOf("overlay_texture").forGetter(Textures::overlay),
			Material.CODEC.fieldOf("overlay_connected").forGetter(Textures::overlayConnected),
			Material.CODEC.optionalFieldOf("base_texture").forGetter(Textures::base),
			Material.CODEC.optionalFieldOf("particle").forGetter(Textures::particle)
		).apply(instance, Textures::new));
	}

	public record LayerSettings(int tintIndex, int lightEmission) {
		public static final LayerSettings DEFAULT = new LayerSettings(-1, 0);
		public static final MapCodec<LayerSettings> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.INT.optionalFieldOf("tint_index", -1).forGetter(LayerSettings::tintIndex),
			Codec.intRange(0, 15).optionalFieldOf("emissivity", 0).forGetter(LayerSettings::lightEmission)
		).apply(instance, LayerSettings::new));
		public static final Codec<LayerSettings> CODEC = MAP_CODEC.codec();
	}

	/**
	 * @param faces            the faces that run connection logic
	 * @param renderOnAllFaces whether faces outside {@code faces} still render the (unconnected) overlay
	 */
	public record OverlaySettings(Set<Direction> faces, LayerSettings layer, boolean renderOnAllFaces) {
		private static final Codec<Set<Direction>> FACES_CODEC = Direction.CODEC.listOf().xmap(list -> list.isEmpty() ? EnumSet.noneOf(Direction.class) : EnumSet.copyOf(list), List::copyOf);
		public static final OverlaySettings DEFAULT = new OverlaySettings(EnumSet.allOf(Direction.class), LayerSettings.DEFAULT, true);
		public static final Codec<OverlaySettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			FACES_CODEC.optionalFieldOf("faces", EnumSet.allOf(Direction.class)).forGetter(OverlaySettings::faces),
			LayerSettings.MAP_CODEC.forGetter(OverlaySettings::layer),
			Codec.BOOL.optionalFieldOf("always_render_overlay", true).forGetter(OverlaySettings::renderOnAllFaces)
		).apply(instance, OverlaySettings::new));
	}

	public record Element(Vector3fc from, Vector3fc to) {
		public static final Element FULL_BLOCK = new Element(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F));
		public static final Codec<Element> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.VECTOR3F.fieldOf("from").forGetter(Element::from),
			ExtraCodecs.VECTOR3F.fieldOf("to").forGetter(Element::to)
		).apply(instance, Element::new));
	}

	public static final class Builder {
		private final Material overlay;
		private final Material overlayConnected;
		@Nullable
		private Material base;
		@Nullable
		private Material particle;
		private final List<Either<TagKey<Block>, Block>> connectables = new ArrayList<>();
		private Set<Direction> faces = EnumSet.allOf(Direction.class);
		private boolean renderOverlayOnAllFaces = true;
		private int overlayTintIndex = -1;
		private int overlayEmissivity = 0;
		private int baseTintIndex = -1;
		private int baseEmissivity = 0;
		private Element element = Element.FULL_BLOCK;
		private boolean ambientOcclusion = true;

		private Builder(Material overlay, Material overlayConnected) {
			this.overlay = overlay;
			this.overlayConnected = overlayConnected;
		}

		public Builder base(Material base) {
			this.base = base;
			return this;
		}

		public Builder particle(Material particle) {
			this.particle = particle;
			return this;
		}

		public Builder connectsTo(Block... blocks) {
			for (Block block : blocks) this.connectables.add(Either.right(block));
			return this;
		}

		@SafeVarargs
		public final Builder connectsTo(TagKey<Block>... tags) {
			for (TagKey<Block> tag : tags) this.connectables.add(Either.left(tag));
			return this;
		}

		public Builder connectionFaces(Direction... faces) {
			this.faces = faces.length == 0 ? EnumSet.noneOf(Direction.class) : EnumSet.copyOf(List.of(faces));
			return this;
		}

		public Builder disableOverlayOnUnconnectedFaces() {
			this.renderOverlayOnAllFaces = false;
			return this;
		}

		public Builder overlayTintIndex(int index) {
			this.overlayTintIndex = index;
			return this;
		}

		public Builder overlayEmissivity(int emissivity) {
			this.overlayEmissivity = emissivity;
			return this;
		}

		public Builder baseTintIndex(int index) {
			this.baseTintIndex = index;
			return this;
		}

		public Builder baseEmissivity(int emissivity) {
			this.baseEmissivity = emissivity;
			return this;
		}

		public Builder element(Vector3fc from, Vector3fc to) {
			this.element = new Element(from, to);
			return this;
		}

		public Builder noAmbientOcclusion() {
			this.ambientOcclusion = false;
			return this;
		}

		public UnbakedConnectedTextureModel build() {
			return new UnbakedConnectedTextureModel(
				new Textures(this.overlay, this.overlayConnected, Optional.ofNullable(this.base), Optional.ofNullable(this.particle)),
				List.copyOf(this.connectables),
				new OverlaySettings(this.faces, new LayerSettings(this.overlayTintIndex, this.overlayEmissivity), this.renderOverlayOnAllFaces),
				new LayerSettings(this.baseTintIndex, this.baseEmissivity),
				this.element,
				this.ambientOcclusion);
		}
	}
}
