package twilightforest.capabilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.network.MovePlayerPacket;
import twilightforest.network.UpdateThrownPacket;

public class YetiThrowAttachment {

	public static final int THROW_COOLDOWN = 200;

	private boolean thrown;
	@Nullable
	private LivingEntity thrower;
	private int throwCooldown;
	private Vec3 throwVector = Vec3.ZERO;

	public void tick(Player player) {
		if (this.getThrown()) {
			if (player.onGround() || player.isSwimming() || player.isInWater()) {
				this.setThrown(player, false, null);
			}
		}
		if (this.throwCooldown > 0) {
			if (!player.level().isClientSide() && this.throwCooldown == THROW_COOLDOWN - 1) { // Actually throw the victim
				player.push(this.throwVector.x(), this.throwVector.y(), this.throwVector.z());

				if (player instanceof ServerPlayer server) {
					PacketDistributor.PLAYER.with(server).send(new MovePlayerPacket(this.throwVector.x(), this.throwVector.y(), this.throwVector.z()));
				}
				this.throwVector = Vec3.ZERO;
			}
			this.throwCooldown--;
		}
	}

	public boolean getThrown() {
		return this.thrown;
	}

	public void setThrown(Player player, boolean thrown, @Nullable LivingEntity thrower) {
		this.thrown = thrown;
		this.thrower = thrower;
		this.sendUpdatePacket(player);
	}

	public @Nullable LivingEntity getThrower() {
		return this.thrower;
	}

	public int getThrowCooldown() {
		return this.throwCooldown;
	}

	public void setThrowCooldown(Player player, int cooldown) {
		this.throwCooldown = cooldown;
		this.sendUpdatePacket(player);
	}

	public void setThrowVector(Vec3 vector) {
		this.throwVector = vector;
	}

	private void sendUpdatePacket(Player player) {
		if (!player.level().isClientSide()) {
			int throwerID = 0;
			if (this.thrower != null) throwerID = this.thrower.getId();
			PacketDistributor.TRACKING_ENTITY_AND_SELF.with(player).send(new UpdateThrownPacket(player.getId(), this.thrown, throwerID, this.throwCooldown));
		}
	}
}
