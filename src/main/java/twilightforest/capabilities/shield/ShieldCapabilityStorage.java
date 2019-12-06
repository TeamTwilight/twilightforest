package twilightforest.capabilities.shield;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ShieldCapabilityStorage implements Capability.IStorage<IShieldCapability> {

	@Override
	public CompoundNBT writeNBT(Capability<IShieldCapability> capability, IShieldCapability instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("tempshields", instance.temporaryShieldsLeft());
		tag.putInt("permshields", instance.permanentShieldsLeft());
		return tag;
	}

	@Override
	public void readNBT(Capability<IShieldCapability> capability, IShieldCapability instance, Direction side, INBT nbt) {
		if (nbt instanceof CompoundNBT && instance instanceof ShieldCapabilityHandler) {
			CompoundNBT tag = (CompoundNBT) nbt;
			((ShieldCapabilityHandler) instance).initShields(
					tag.getInt("tempshields"),
					tag.getInt("permshields")
			);
		}
	}
}
