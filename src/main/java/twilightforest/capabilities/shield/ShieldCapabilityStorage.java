package twilightforest.capabilities.shield;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ShieldCapabilityStorage implements Capability.IStorage<IShieldCapability> {

	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IShieldCapability> capability, IShieldCapability instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("shields", instance.shieldsLeft());
		return tag;
	}

	@Override
	public void readNBT(Capability<IShieldCapability> capability, IShieldCapability instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			instance.setShields(tag.getInteger("shields"));
		}
	}
}
