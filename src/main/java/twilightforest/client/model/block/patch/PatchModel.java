package twilightforest.client.model.block.patch;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.model.quad.MutableQuad;
import twilightforest.block.PatchBlock;

import java.util.ArrayList;
import java.util.List;

public class PatchModel implements UnbakedGeometry {

	private final boolean shaggify;

	private TextureAtlasSprite texture;
	private boolean usesAO;
	private boolean usesBlockLight;

	public PatchModel(boolean shaggify) {
		this.shaggify = shaggify;
	}

	public PatchModel(TextureAtlasSprite texture, boolean shaggify, boolean usesAO, boolean usesBlockLight) {
		this.texture = texture;
		this.shaggify = shaggify;
		this.usesAO = usesAO;
		this.usesBlockLight = usesBlockLight;
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
		MutableQuad mutableQuad = new MutableQuad();

		mutableQuad.setDirection(direction);
		mutableQuad.setShade(this.usesAO);

		if (this.usesBlockLight) {
			mutableQuad.setLightEmission(15);
		}

		for (int v = 0; v < 4; v++) {
			float x = (v == 1 || v == 2) ? maxX / 16.0f : minX / 16.0f;
			float y = (v == 2 || v == 3) ? maxY / 16.0f : minY / 16.0f;
			float z = (direction.getAxis() == net.minecraft.core.Direction.Axis.Z) ? maxZ / 16.0f : minZ / 16.0f;

			mutableQuad.setPosition(v, new org.joml.Vector3f(x, y, z));

			float u = this.texture.getU(x * 16.0f);
			float vCoord = this.texture.getV(y * 16.0f);

			if (direction == net.minecraft.core.Direction.EAST || direction == net.minecraft.core.Direction.WEST) {
				mutableQuad.setUv(v, vCoord, u);
			} else {
				mutableQuad.setUv(v, u, vCoord);
			}
		}

		return mutableQuad.toBakedQuad();
	}

	@Override
	public QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName modelDebugName) {
		QuadCollection.Builder builder = new QuadCollection.Builder();

		RandomSource staticRandom = RandomSource.create(42L);
		List<BakedQuad> myPatchQuads = this.getQuads(false, false, false, false, staticRandom);

		for (BakedQuad quad : myPatchQuads) {
			if (quad.direction() != null) {
				builder.addCulledFace(quad.direction(), quad);
			} else {
				builder.addUnculledFace(quad);
			}
		}

		return builder.build();
	}
}
