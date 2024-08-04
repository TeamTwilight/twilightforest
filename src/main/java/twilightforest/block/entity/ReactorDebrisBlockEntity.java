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
import twilightforest.init.TFBlockEntities;

import java.util.Random;

public class ReactorDebrisBlockEntity extends BlockEntity {
	private static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.withDefaultNamespace("block/netherrack");
	private static final ResourceLocation[] TEXTURES = {
		ResourceLocation.withDefaultNamespace("block/netherrack"),
		ResourceLocation.withDefaultNamespace("block/bedrock"),
		ResourceLocation.withDefaultNamespace("block/nether_portal")
	};
	private static final double Z_FIGHTING_MIN = 0.001;
	private static final double Z_FIGHTING_MAX = 0.999;
	private static final Random RANDOM = new Random();
	public VoxelShape SHAPE = Shapes.empty();

	public ResourceLocation[] textures = new ResourceLocation[6];
	public Vec3 minPos = Vec3.ZERO;
	public Vec3 maxPos = new Vec3(1, 1, 1);

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
		minPos = new Vec3(aabb.minX, aabb.minY, aabb.minZ);
		maxPos = new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
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
			minPos = new Vec3(posTag.getFloat(0), posTag.getFloat(1), posTag.getFloat(2));
		}
		if (!new AABB(0, 0, 0, 1, 1, 1).contains(minPos)) {
			minPos = Vec3.ZERO;
		}

		ListTag sizeTag = tag.getList("sizes", Tag.TAG_FLOAT);
		if (sizeTag.size() == 3) {
			maxPos = new Vec3(sizeTag.getFloat(0), sizeTag.getFloat(1), sizeTag.getFloat(2)).add(minPos);
		}
		if (!new AABB(0, 0, 0, 1, 1, 1).contains(maxPos)) {
			maxPos = new Vec3(1, 1, 1);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		for (int i = 0; i < textures.length; i++) {
			tag.putString("texture" + i, textures[i].toString());
		}
		tag.put("pos", newDoubleList(minPos.x, minPos.y, minPos.z));
		tag.put("sizes", newDoubleList(maxPos.x - minPos.x, maxPos.y - minPos.y, maxPos.z - minPos.z));
	}

	protected ListTag newDoubleList(double... values) {
		ListTag listTag = new ListTag();
		for (double value : values) {
			listTag.add(DoubleTag.valueOf(value));
		}
		return listTag;
	}
}
