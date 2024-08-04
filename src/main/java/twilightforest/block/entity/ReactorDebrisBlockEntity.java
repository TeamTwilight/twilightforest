package twilightforest.block.entity;

import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;
import twilightforest.init.TFBlockEntities;

import java.util.Random;

public class ReactorDebrisBlockEntity extends BlockEntity {
	private static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.withDefaultNamespace("block/netherrack");
	private static final ResourceLocation[] TEXTURES = {
		ResourceLocation.withDefaultNamespace("block/netherrack"),
		ResourceLocation.withDefaultNamespace("block/bedrock"),
		ResourceLocation.withDefaultNamespace("block/nether_portal")
	};
	private static final float Z_FIGHTING_MIN = 0.001F;
	private static final float Z_FIGHTING_MAX = 0.999F;
	private static final Random RANDOM = new Random();
	public VoxelShape SHAPE = Shapes.empty();

	public ResourceLocation[] textures = new ResourceLocation[6];
	public Vector3f minPos = new Vector3f(Z_FIGHTING_MIN);
	public Vector3f maxPos = new Vector3f(Z_FIGHTING_MAX);

	public ReactorDebrisBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.REACTOR_DEBRIS.get(), pos, blockState);
		randomizeTextures();
		randomizeDimensions();
	}

	private void randomizeTextures() {
		for (int i = 0; i < textures.length; i++) {
			textures[i] = TEXTURES[RANDOM.nextInt(TEXTURES.length)];
		}
	}

	private void randomizeDimensions() {
		this.SHAPE = calculateVoxelShape();
		AABB aabb = SHAPE.bounds();
		minPos = new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ);
		maxPos = new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
	}

	public static VoxelShape calculateVoxelShape() {
		float minX = RANDOM.nextInt(16) / 16F;
		float minY = RANDOM.nextInt(16) / 16F;
		float minZ = RANDOM.nextInt(16) / 16F;
		float lengthX = RANDOM.nextInt((int) (17 - minX * 16)) / 16F;
		float lengthY = RANDOM.nextInt((int) (17 - minY * 16)) / 16F;
		float lengthZ = RANDOM.nextInt((int) (17 - minZ * 16)) / 16F;

		if (lengthX * lengthY * lengthZ < 1 / 6.0) {
			return calculateVoxelShape();
		}

		return Shapes.box(clampToSmallerCube(minX), clampToSmallerCube(minY), clampToSmallerCube(minZ),
			clampToSmallerCube(minX + lengthX), clampToSmallerCube(minY + lengthY), clampToSmallerCube(minZ + lengthZ));
	}

	private static double clampToSmallerCube(double value) {
		return Math.min(Math.max(value, Z_FIGHTING_MIN), Z_FIGHTING_MAX);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		for (int i = 0; i < textures.length; i++) {
			textures[i] = MoreObjects.firstNonNull(ResourceLocation.tryParse(tag.getString("texture" + i)), DEFAULT_TEXTURE);
		}
		ListTag posTag = tag.getList("pos", Tag.TAG_FLOAT);
		if (posTag.size() == 3) {
			minPos = new Vector3f(posTag.getFloat(0), posTag.getFloat(1), posTag.getFloat(2));
		}
		if (!new AABB(0, 0, 0, 1, 1, 1).contains(minPos.x, minPos.y, minPos.z)) {
			minPos = new Vector3f();
		}

		ListTag sizeTag = tag.getList("sizes", Tag.TAG_FLOAT);
		if (sizeTag.size() == 3) {
			maxPos = new Vector3f(sizeTag.getFloat(0), sizeTag.getFloat(1), sizeTag.getFloat(2)).add(minPos);
		}
		if (!new AABB(0, 0, 0, 1, 1, 1).contains(minPos.x, minPos.y, minPos.z)) {
			maxPos = new Vector3f(1);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		for (int i = 0; i < textures.length; i++) {
			tag.putString("texture" + i, textures[i].toString());
		}
		tag.put("pos", newFloatList(minPos.x, minPos.y, minPos.z));
		tag.put("sizes", newFloatList(maxPos.x - minPos.x, maxPos.y - minPos.y, maxPos.z - minPos.z));
	}

	protected ListTag newFloatList(float... values) {
		ListTag listTag = new ListTag();
		for (double value : values) {
			listTag.add(DoubleTag.valueOf(value));
		}
		return listTag;
	}
}
