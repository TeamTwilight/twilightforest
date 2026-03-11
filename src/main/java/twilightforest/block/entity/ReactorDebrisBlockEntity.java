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
import org.jetbrains.annotations.NotNull;
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
	private boolean rerolls = false;
	private boolean willDisappear = true;
	private byte timeAlive = 0;
	public VoxelShape shape = Shapes.empty();

	public ResourceLocation[] textures = new ResourceLocation[6];
	public Vector3f minPos = new Vector3f(Z_FIGHTING_MIN);
	public Vector3f maxPos = new Vector3f(Z_FIGHTING_MAX);

	public ReactorDebrisBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.REACTOR_DEBRIS.get(), pos, blockState);
	}

	public void randomizeTextures() {
		for (int i = 0; i < this.textures.length; i++) {
			this.textures[i] = TEXTURES[RANDOM.nextInt(TEXTURES.length)];
		}
	}

	public void randomizeDimensions() {
		this.shape = calculateVoxelShape();
		AABB aabb = this.shape.bounds();
		this.minPos = new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ);
		this.maxPos = new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
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
		if (reactorDebrisBlockEntity.willDisappear && reactorDebrisBlockEntity.timeAlive == 5 ||
			reactorDebrisBlockEntity.rerolls && RANDOM.nextInt(5) == 0) {
			reactorDebrisBlockEntity.randomizeDimensions();
			reactorDebrisBlockEntity.randomizeTextures();
		}

		if (!reactorDebrisBlockEntity.willDisappear)
			return;
		reactorDebrisBlockEntity.timeAlive++;
		if (reactorDebrisBlockEntity.timeAlive >= 60) {
			level.destroyBlock(blockPos, false);
		}
	}

	@NotNull
	private static ResourceLocation nonEmptyNotNull(String texturesString) {
		if (texturesString.isBlank()) {
			return ReactorDebrisBlockEntity.DEFAULT_TEXTURE;
		}
		return MoreObjects.firstNonNull(ResourceLocation.tryParse(texturesString), ReactorDebrisBlockEntity.DEFAULT_TEXTURE);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		CompoundTag textures = tag.getCompound("textures");
		this.textures[0] = nonEmptyNotNull(textures.getString("west"));
		this.textures[1] = nonEmptyNotNull(textures.getString("east"));
		this.textures[2] = nonEmptyNotNull(textures.getString("bottom"));
		this.textures[3] = nonEmptyNotNull(textures.getString("top"));
		this.textures[4] = nonEmptyNotNull(textures.getString("north"));
		this.textures[5] = nonEmptyNotNull(textures.getString("south"));

		ListTag posTag = tag.getList("pos", Tag.TAG_FLOAT);
		if (posTag.size() == 3) {
			this.minPos = new Vector3f(posTag.getFloat(0), posTag.getFloat(1), posTag.getFloat(2));
		}
		if (!new AABB(0, 0, 0, 1, 1, 1).contains(this.minPos.x, this.minPos.y, this.minPos.z)) {
			this.minPos = new Vector3f();
		}

		ListTag sizeTag = tag.getList("sizes", Tag.TAG_FLOAT);
		if (sizeTag.size() == 3) {
			this.maxPos = new Vector3f(sizeTag.getFloat(0), sizeTag.getFloat(1), sizeTag.getFloat(2)).add(this.minPos);
		}
		if (!new AABB(0, 0, 0, 1, 1, 1).contains(this.minPos.x, this.minPos.y, this.minPos.z)) {
			this.maxPos = new Vector3f(1);
		}

		this.shape = Shapes.box(this.minPos.x, this.minPos.y, this.minPos.z, this.maxPos.x, this.maxPos.y, this.maxPos.z);
		this.rerolls = tag.getBoolean("rerolls");
		this.willDisappear = tag.getBoolean("will_disappear");
		this.timeAlive = tag.getByte("timeAlive");
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		CompoundTag textures = new CompoundTag();
		textures.putString("west", this.textures[0].toString());
		textures.putString("east", this.textures[1].toString());
		textures.putString("bottom", this.textures[2].toString());
		textures.putString("top", this.textures[3].toString());
		textures.putString("north", this.textures[4].toString());
		textures.putString("south", this.textures[5].toString());
		tag.put("textures", textures);
		tag.put("pos", this.newFloatList(this.minPos.x, this.minPos.y, this.minPos.z));
		tag.put("sizes", this.newFloatList(this.maxPos.x - this.minPos.x, this.maxPos.y - this.minPos.y, this.maxPos.z - this.minPos.z));
		tag.putBoolean("rerolls", this.rerolls);
		tag.putBoolean("will_disappear", this.willDisappear);
		tag.putByte("timeAlive", this.timeAlive);
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
