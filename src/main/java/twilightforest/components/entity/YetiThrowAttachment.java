package twilightforest.components.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.network.UpdateThrownPacket;

public class YetiThrowAttachment {

	private boolean thrown;
	@Nullable
	private LivingEntity thrower;

	public void tick(Player player) {
		if (this.getThrown()) {
			if (player.onGround() || player.isSwimming() || player.isInWater()) {
				this.setThrown(player, false, null);
			}
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

	private void sendUpdatePacket(Player player) {
		if (!player.level().isClientSide()) {
			int throwerID = 0;
			if (this.thrower != null) throwerID = this.thrower.getId();
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new UpdateThrownPacket(player.getId(), this.thrown, throwerID));
		}
	}
}
