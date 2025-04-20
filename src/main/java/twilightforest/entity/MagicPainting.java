package twilightforest.entity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFDataSerializers;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.custom.MagicPaintingVariants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MagicPainting extends HangingEntity {
	private static final EntityDataAccessor<Holder<MagicPaintingVariant>> MAGIC_PAINTING_VARIANT = SynchedEntityData.defineId(MagicPainting.class, TFDataSerializers.MAGIC_PAINTING_VARIANT.value());

	public MagicPainting(EntityType<? extends MagicPainting> entityType, Level level) {
		super(entityType, level);
	}

	private MagicPainting(Level level, BlockPos pos) {
		super(TFEntities.MAGIC_PAINTING.get(), level, pos);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(MAGIC_PAINTING_VARIANT, this.getReg().getOrThrow(MagicPaintingVariants.DEFAULT));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		if (MAGIC_PAINTING_VARIANT.equals(pKey)) {
			this.recalculateBoundingBox();
		}
	}

	public void setVariant(Holder<MagicPaintingVariant> variant) {
		this.getEntityData().set(MAGIC_PAINTING_VARIANT, variant);
	}

	public Holder<MagicPaintingVariant> getVariant() {
		return this.getEntityData().get(MAGIC_PAINTING_VARIANT);
	}

	public static Optional<MagicPainting> create(Level level, BlockPos pos, Direction direction) {
		MagicPainting magicPainting = new MagicPainting(level, pos);
		List<Holder<MagicPaintingVariant>> list = new ArrayList<>();
		level.registryAccess().lookupOrThrow(TFRegistries.Keys.MAGIC_PAINTINGS).listElements().forEach(list::add);
		if (list.isEmpty()) {
			return Optional.empty();
		} else {
			magicPainting.setDirection(direction);
			list.removeIf((variant) -> {
				magicPainting.setVariant(variant);
				return !magicPainting.survives();
			});
			if (list.isEmpty()) {
				return Optional.empty();
			} else {
				int biggestPossibleArea = list.stream().mapToInt(MagicPainting::variantArea).max().orElse(0);
				list.removeIf((variantArea) -> variantArea(variantArea) < biggestPossibleArea);
				Optional<Holder<MagicPaintingVariant>> optional = Util.getRandomSafe(list, magicPainting.random);
				if (optional.isEmpty()) {
					return Optional.empty();
				} else {
					magicPainting.setVariant(optional.get());
					magicPainting.setDirection(direction);
					return Optional.of(magicPainting);
				}
			}
		}
	}

	private static int variantArea(Holder<MagicPaintingVariant> variant) {
		return variantArea(variant.value());
	}

	private static int variantArea(MagicPaintingVariant variant) {
		return variant.width() * variant.height();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		ResourceLocation location = this.getReg().getKey(this.getVariant().value());
		if (location != null) tag.putString("variant", location.toString());
		tag.putByte("facing", (byte) this.direction.get2DDataValue());
		super.addAdditionalSaveData(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("variant")) {
			ResourceLocation location = ResourceLocation.tryParse(tag.getString("variant"));
			if (location != null) {
				this.setVariant(this.getReg().get(location).orElse(this.getReg().getOrThrow(MagicPaintingVariants.DEFAULT)));
			}
		}

		this.direction = Direction.from2DDataValue(tag.getByte("facing"));
		super.readAdditionalSaveData(tag);
		this.setDirection(this.direction);
	}

	protected Registry<MagicPaintingVariant> getReg() {
		return this.registryAccess().lookupOrThrow(TFRegistries.Keys.MAGIC_PAINTINGS);
	}

	@Override
	protected AABB calculateBoundingBox(BlockPos pos, Direction direction) {
		Vec3 vec3 = Vec3.atCenterOf(pos).relative(direction, -0.46875D);
		MagicPaintingVariant variant = this.getVariant().value();
		double widthOffset = this.offsetForPaintingSize(variant.width());
		double heightOffset = this.offsetForPaintingSize(variant.height());
		Vec3 vec31 = vec3.relative(direction.getCounterClockWise(), widthOffset).relative(Direction.UP, heightOffset);
		Direction.Axis axis = direction.getAxis();
		double scale = 1.0D / 16.0D;
		double x = axis == Direction.Axis.X ? 0.0625D : variant.width() * scale;
		double y = variant.height() * scale;
		double z = axis == Direction.Axis.Z ? 0.0625D : variant.width() * scale;
		return AABB.ofSize(vec31, x, y, z);
	}

	private double offsetForPaintingSize(int size) {
		return size % 32 == 0 ? 0.5D : 0.0D;
	}

	@Override
	public void dropItem(ServerLevel level, @Nullable Entity entity) {
		if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
			if (entity instanceof Player player) {
				if (player.getAbilities().instabuild) {
					return;
				}
			}

			this.spawnAtLocation(level, this.getPickResult());
		}
	}

	@Override
	public void playPlacementSound() {
		this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);//FIXME
	}

	@Override
	public void moveTo(double x, double y, double z, float yaw, float pitch) {
		this.setPos(x, y, z);
	}

	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		this.setPos(x, y, z);
	}

	@Override
	public Vec3 trackingPosition() {
		return Vec3.atLowerCornerOf(this.pos);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		this.setDirection(Direction.from3DDataValue(packet.getData()));
	}

	@Override
	@NotNull
	public ItemStack getPickResult() {
		ItemStack itemStack = new ItemStack(TFItems.MAGIC_PAINTING.get());
		itemStack.set(TFDataComponents.MAGIC_PAINTING_VARIANT, this.getVariant());
		return itemStack;
	}

	@Override
	public void setDirection(Direction faceDirection) {
		super.setDirection(faceDirection);
	}
}