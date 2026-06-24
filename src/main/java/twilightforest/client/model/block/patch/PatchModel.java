package twilightforest.client.model.block.patch;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.block.PatchBlock;
import twilightforest.client.model.block.connected.UnbakedConnectedTextureModel;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.List;

public class PatchModel implements BakedModel {

	private final TextureAtlasSprite texture;
	private final boolean shaggify;
	private final TextureAtlasSprite particle;
	private final boolean usesAO;
	private final boolean usesBlockLight;
	private final ItemTransforms transforms;
	@Nullable
	private final ChunkRenderTypeSet blockRenderTypes;
	@Nullable
	private final RenderType itemRenderType;

	public PatchModel(TextureAtlasSprite texture, boolean shaggify, TextureAtlasSprite particle, boolean usesAO, boolean usesBlockLight, ItemTransforms transforms, RenderTypeGroup group) {
		this.texture = texture;
		this.shaggify = shaggify;
		this.particle = particle;
		this.usesAO = usesAO;
		this.usesBlockLight = usesBlockLight;
		this.transforms = transforms;
		this.blockRenderTypes = !group.isEmpty() ? ChunkRenderTypeSet.of(group.block()) : null;
		this.itemRenderType = !group.isEmpty() ? group.entity() : null;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
		if (state == null)
			return this.getQuads(false, false, false, false, random);
		else
			return this.getQuads(state.getValue(PatchBlock.NORTH), state.getValue(PatchBlock.EAST), state.getValue(PatchBlock.SOUTH), state.getValue(PatchBlock.WEST), random);
	}

	private List<BakedQuad> getQuads(boolean north, boolean east, boolean south, boolean west, RandomSource posRandom) {
		List<BakedQuad> list = new ArrayList<>();

		BoundingBox bb = PatchBlock.AABBFromRandom(posRandom);

		this.quadsFromAABB(list, west ? 0 : bb.minX(), bb.minY(), north ? 0 : bb.minZ(), east ? 16 : bb.maxX(), bb.maxY(), south ? 16 : bb.maxZ());

		if (!this.shaggify)
			return ImmutableList.copyOf(list);

		// Poll these seeds before entering branching code, otherwise placing neighbors will cause odd changes
		long westSeed = posRandom.nextLong();
		long eastSeed = posRandom.nextLong();
		long northSeed = posRandom.nextLong();
		long southSeed = posRandom.nextLong();

		int minY = bb.minY();
		int maxY = bb.maxY();

		// add on shaggy edges
		if (!west) {
			long seed = westSeed;
			seed = seed * seed * 42317861L + seed * 7L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minZ = bb.minZ() + num0;
			int maxZ = bb.maxZ();

			if (maxZ - ((num1 + num2 + num3)) > minZ) {
				// draw two blobs
				int innerZ = bb.maxZ() - num2;
				this.quadsFromAABB(list, bb.minX() - 1, minY, minZ, bb.minX(), maxY, minZ + num1);
				this.quadsFromAABB(list, bb.minX() - 1, minY, innerZ - num3, bb.minX(), maxY, innerZ);
			} else {
				//draw one blob
				this.quadsFromAABB(list, bb.minX() - 1, minY, minZ, bb.minX(), maxY, maxZ - num2);
			}
		}

		if (!east) {
			long seed = eastSeed;
			seed = seed * seed * 42317861L + seed * 17L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minZ = bb.minZ() + num0;
			int maxZ = bb.maxZ();

			if (maxZ - ((num1 + num2 + num3)) > minZ) {
				// draw two blobs
				int innerZ = maxZ - num2;
				this.quadsFromAABB(list, bb.maxX(), minY, minZ, bb.maxX() + 1, maxY, minZ + num1);
				this.quadsFromAABB(list, bb.maxX(), minY, innerZ - num3, bb.maxX() + 1, maxY, innerZ);
			} else {
				//draw one blob
				this.quadsFromAABB(list, bb.maxX(), minY, minZ, bb.maxX() + 1, maxY, maxZ - num2);
			}
		}

		if (!north) {
			long seed = northSeed;
			seed = seed * seed * 42317861L + seed * 23L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minX = bb.minX() + num0;
			int innerX = minX + num1;
			int maxX = bb.maxX() - num2;

			this.quadsFromAABB(list, minX, minY, bb.minZ() - 1, innerX, maxY, bb.minZ());
			this.quadsFromAABB(list, maxX - num3, minY, bb.minZ() - 1, maxX, maxY, bb.minZ());
		}

		if (!south) {
			long seed = southSeed;
			seed = seed * seed * 42317861L + seed * 11L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minX = bb.minX() + num0;
			int maxX = bb.maxX() - num2;

			this.quadsFromAABB(list, minX, minY, bb.maxZ(), minX + num1, maxY, bb.maxZ() + 1);
			this.quadsFromAABB(list, maxX - num3, minY, bb.maxZ(), maxX, maxY, bb.maxZ() + 1);
		}

		return ImmutableList.copyOf(list);
	}

	private void quadsFromAABB(List<BakedQuad> quads, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		quads.add(this.quadFromVectors(Direction.UP, minX, minY, minZ, maxX, maxY, maxZ));
		quads.add(this.quadFromVectors(Direction.NORTH, minX, minY, minZ, maxX, maxY, maxZ));
		quads.add(this.quadFromVectors(Direction.EAST, minX, minY, minZ, maxX, maxY, maxZ));
		quads.add(this.quadFromVectors(Direction.SOUTH, minX, minY, minZ, maxX, maxY, maxZ));
		quads.add(this.quadFromVectors(Direction.WEST, minX, minY, minZ, maxX, maxY, maxZ));
		quads.add(this.quadFromVectors(Direction.DOWN, minX, minY, minZ, maxX, maxY, maxZ));
	}

	private BakedQuad quadFromVectors(Direction direction, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		BlockElementFace face = new BlockElementFace(null, 0, this.texture.atlasLocation().toString(), switch (direction) {
			case NORTH -> new BlockFaceUV(new float[]{maxX, minZ + 1f, minX, minZ}, 0);
			case EAST -> new BlockFaceUV(new float[]{maxX, minZ, maxX - 1f, maxZ}, 90);
			case SOUTH -> new BlockFaceUV(new float[]{minX, maxZ, maxX, maxZ - 1f}, 0);
			case WEST -> new BlockFaceUV(new float[]{minX, maxZ, minX + 1f, minZ}, 90);
			default -> new BlockFaceUV(new float[]{minX, minZ, maxX, maxZ}, 0);
		});

		return FaceBakery.bakeQuad(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ), face, this.texture, direction, new SimpleModelState(Transformation.identity()), null, true, 0);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.usesAO;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return this.usesBlockLight;
	}

	@Override
	public ItemTransforms getTransforms() {
		return this.transforms;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.particle;
	}

	@NotNull
	@Override
	public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
		return this.blockRenderTypes != null ? this.blockRenderTypes : BakedModel.super.getRenderTypes(state, rand, data);
	}

	@Override
	public RenderType getRenderType(ItemStack stack) {
		return this.itemRenderType != null ? this.itemRenderType : BakedModel.super.getRenderType(stack);
	}
}
