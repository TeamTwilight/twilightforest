package twilightforest.capabilities.shield;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class ShieldCapabilityStorage implements Capability.IStorage<IShieldCapability> {

	@Override
	public NBTTagCompound writeNBT(Capability<IShieldCapability> capability, IShieldCapability instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("tempshields", instance.temporaryShieldsLeft());
		tag.setInteger("permshields", instance.permanentShieldsLeft());
		return tag;
	}

	@Override
	public void readNBT(Capability<IShieldCapability> capability, IShieldCapability instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound && instance instanceof ShieldCapabilityHandler) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			((ShieldCapabilityHandler) instance).initShields(
					tag.getInteger("tempshields"),
					tag.getInteger("permshields")
			);
		}
	}
}
