package twilightforest.entity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFRegistries;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.custom.MagicPaintingVariants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MagicPainting extends HangingEntity {
    private static final EntityDataAccessor<String> DATA_PAINTING_VARIANT_ID = SynchedEntityData.defineId(MagicPainting.class, EntityDataSerializers.STRING);
    public static final String EMPTY = "null";

    public MagicPainting(EntityType<? extends MagicPainting> entityType, Level level) {
        super(entityType, level);
    }

    private MagicPainting(Level level, BlockPos pos) {
        super(TFEntities.MAGIC_PAINTING.get(), level, pos);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_PAINTING_VARIANT_ID, EMPTY);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_PAINTING_VARIANT_ID.equals(pKey)) {
            this.recalculateBoundingBox();
        }
    }

    public void setVariant(String variant) {
        this.entityData.set(DATA_PAINTING_VARIANT_ID, variant);
    }

    public Optional<MagicPaintingVariant> getVariant() {
        String id = this.entityData.get(DATA_PAINTING_VARIANT_ID);
        if (id.equals(EMPTY)) return Optional.empty();
        return MagicPaintingVariant.getVariant(this.level().registryAccess(), id);
    }

    public static Optional<MagicPainting> create(Level level, BlockPos pos, Direction direction) {
        MagicPainting magicPainting = new MagicPainting(level, pos);
        List<MagicPaintingVariant> list = new ArrayList<>();
        RegistryAccess regAccess = level.registryAccess();
        regAccess.registryOrThrow(TFRegistries.Keys.MAGIC_PAINTINGS).forEach(list::add);
        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            magicPainting.setDirection(direction);
            list.removeIf((variant) -> {
                magicPainting.setVariant(MagicPaintingVariant.getVariantId(regAccess, variant));
                return !magicPainting.survives();
            });
            if (list.isEmpty()) {
                return Optional.empty();
            } else {
                int biggestPossibleArea = list.stream().mapToInt(MagicPainting::variantArea).max().orElse(0);
                list.removeIf((variantArea) -> variantArea(variantArea) < biggestPossibleArea);
                Optional<MagicPaintingVariant> optional = Util.getRandomSafe(list, magicPainting.random);
                if (optional.isEmpty()) {
                    return Optional.empty();
                } else {
                    magicPainting.setVariant(MagicPaintingVariant.getVariantId(regAccess, optional.get()));
                    magicPainting.setDirection(direction);
                    return Optional.of(magicPainting);
                }
            }
        }
    }

    private static int variantArea(MagicPaintingVariant variant) {
        return variant.width() * variant.height();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("variant", this.entityData.get(DATA_PAINTING_VARIANT_ID));
        tag.putByte("facing", (byte) this.direction.get2DDataValue());
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.setVariant(tag.contains("variant") ? tag.getString("variant") : EMPTY);
        this.direction = Direction.from2DDataValue(tag.getByte("facing"));
        super.readAdditionalSaveData(tag);
        this.setDirection(this.direction);
    }

    @Override
    public int getWidth() {
        return this.getVariant().map(MagicPaintingVariant::width).orElse(16);
    }

    @Override
    public int getHeight() {
        return this.getVariant().map(MagicPaintingVariant::height).orElse(16);
    }

    @Override
    public void dropItem(@Nullable Entity entity) {
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
            if (entity instanceof Player player) {
                if (player.getAbilities().instabuild) {
                    return;
                }
            }

            this.spawnAtLocation(Util.make(new ItemStack(TFItems.MAGIC_PAINTING.get()), stack -> {
                CompoundTag tag = stack.getOrCreateTagElement("EntityTag");
                tag.putString("variant", this.entityData.get(DATA_PAINTING_VARIANT_ID));
            }));
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDirection(Direction.from3DDataValue(packet.getData()));
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(TFItems.MAGIC_PAINTING.get());
    }
}