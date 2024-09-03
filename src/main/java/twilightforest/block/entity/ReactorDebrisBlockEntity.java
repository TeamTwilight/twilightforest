package twilightforest.block.entity;

import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;
import twilightforest.init.TFBlockEntities;

import java.util.Random;

public class ReactorDebrisBlockEntity extends BlockEntity {
	private static final ResourceLocation[] TEXTURES = {
		ResourceLocation.withDefaultNamespace("block/netherrack"),
		ResourceLocation.withDefaultNamespace("block/bedrock"),
		ResourceLocation.withDefaultNamespace("block/nether_portal"),
		ResourceLocation.withDefaultNamespace("block/obsidian"),
	};
	public static final ResourceLocation DEFAULT_TEXTURE = TEXTURES[0];
	private static final float Z_FIGHTING_MIN = 0.008F;
	private static final float Z_FIGHTING_MAX = 1 - 0.008F;
	private static final Random RANDOM = new Random();
	private boolean REROLLS = false;
	private boolean WILL_DISAPPEAR = true;
	private byte timeAlive = 0;
	public VoxelShape SHAPE = Shapes.empty();

	public ResourceLocation[] textures = new ResourceLocation[6];
	public Vector3f minPos = new Vector3f(Z_FIGHTING_MIN);
	public Vector3f maxPos = new Vector3f(Z_FIGHTING_MAX);

	public ReactorDebrisBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.REACTOR_DEBRIS.get(), pos, blockState);
	}

	public void randomizeTextures() {
		for (int i = 0; i < textures.length; i++) {
			textures[i] = TEXTURES[RANDOM.nextInt(TEXTURES.length)];
		}
	}

	public void randomizeDimensions() {
		this.SHAPE = calculateVoxelShape();
		AABB aabb = SHAPE.bounds();
		minPos = new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ);
		maxPos = new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
	}

	public static VoxelShape calculateVoxelShape() {
		float minX = RANDOM.nextInt(16) / 16F;
		float minY = RANDOM.nextInt(16) / 16F;
		float minZ = RANDOM.nextInt(16) / 16F;
		float lengthX = RANDOM.nextInt(1, (int) (17 - minX * 16)) / 16F;
		float lengthY = RANDOM.nextInt(1, (int) (17 - minY * 16)) / 16F;
		float lengthZ = RANDOM.nextInt(1, (int) (17 - minZ * 16)) / 16F;

		if (lengthX * lengthY * lengthZ < 1 / 8.0) {
			return calculateVoxelShape();
		}

		return Shapes.box(clampToSmallerCube(minX), clampToSmallerCube(minY), clampToSmallerCube(minZ),
			clampToSmallerCube(minX + lengthX), clampToSmallerCube(minY + lengthY), clampToSmallerCube(minZ + lengthZ));
	}

	private static double clampToSmallerCube(double value) {
		return Math.min(Math.max(value, Z_FIGHTING_MIN), Z_FIGHTING_MAX);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, ReactorDebrisBlockEntity reactorDebrisBlockEntity) {
		if (reactorDebrisBlockEntity.WILL_DISAPPEAR && reactorDebrisBlockEntity.timeAlive == 5 ||
			reactorDebrisBlockEntity.REROLLS && RANDOM.nextInt(5) == 0) {
			reactorDebrisBlockEntity.randomizeDimensions();
			reactorDebrisBlockEntity.randomizeTextures();
		}

		if (!reactorDebrisBlockEntity.WILL_DISAPPEAR)
			return;
		reactorDebrisBlockEntity.timeAlive++;
		if (reactorDebrisBlockEntity.timeAlive >= 60) {
			level.destroyBlock(blockPos, false);
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		CompoundTag textures = tag.getCompound("textures");
		this.textures[0] = MoreObjects.firstNonNull(ResourceLocation.tryParse(textures.get("west").getAsString()), DEFAULT_TEXTURE);
		this.textures[1] = MoreObjects.firstNonNull(ResourceLocation.tryParse(textures.get("east").getAsString()), DEFAULT_TEXTURE);
		this.textures[2] = MoreObjects.firstNonNull(ResourceLocation.tryParse(textures.get("bottom").getAsString()), DEFAULT_TEXTURE);
		this.textures[3] = MoreObjects.firstNonNull(ResourceLocation.tryParse(textures.get("top").getAsString()), DEFAULT_TEXTURE);
		this.textures[4] = MoreObjects.firstNonNull(ResourceLocation.tryParse(textures.get("north").getAsString()), DEFAULT_TEXTURE);
		this.textures[5] = MoreObjects.firstNonNull(ResourceLocation.tryParse(textures.get("south").getAsString()), DEFAULT_TEXTURE);

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

		SHAPE = Shapes.box(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z);
		REROLLS = tag.getBoolean("rerolls");
		WILL_DISAPPEAR = tag.getBoolean("will_disappear");
		timeAlive = tag.getByte("timeAlive");
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		CompoundTag textures = new CompoundTag();
		textures.put("west", StringTag.valueOf(this.textures[0].getPath()));
		textures.put("east", StringTag.valueOf(this.textures[1].getPath()));
		textures.put("bottom", StringTag.valueOf(this.textures[2].getPath()));
		textures.put("top", StringTag.valueOf(this.textures[3].getPath()));
		textures.put("north", StringTag.valueOf(this.textures[4].getPath()));
		textures.put("south", StringTag.valueOf(this.textures[5].getPath()));
		tag.put("textures", textures);
		tag.put("pos", newFloatList(minPos.x, minPos.y, minPos.z));
		tag.put("sizes", newFloatList(maxPos.x - minPos.x, maxPos.y - minPos.y, maxPos.z - minPos.z));
		tag.putBoolean("rerolls", REROLLS);
		tag.putBoolean("will_disappear", WILL_DISAPPEAR);
		tag.putByte("timeAlive", timeAlive);
	}

	protected ListTag newFloatList(float... values) {
		ListTag listTag = new ListTag();
		for (float value : values) {
			listTag.add(FloatTag.valueOf(value));
		}
		return listTag;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}
}
