package twilightforest.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.shield.ShieldCapabilityHandler;
import twilightforest.capabilities.shield.ShieldCapabilityStorage;

import javax.annotation.Nonnull;

public class CapabilityList {

	@CapabilityInject(IShieldCapability.class)
	public static final Capability<IShieldCapability> SHIELDS;

	static {
		SHIELDS = null;
	}

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(IShieldCapability.class, new ShieldCapabilityStorage(), ShieldCapabilityHandler::new);
		MinecraftForge.EVENT_BUS.register(CapabilityList.class);
	}

	@SubscribeEvent
	public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof LivingEntity) {
			e.addCapability(IShieldCapability.ID, new ICapabilitySerializable<CompoundNBT>() {

				IShieldCapability inst = SHIELDS.getDefaultInstance();

				{
					inst.setEntity((LivingEntity) e.getObject());
				}

				@Override
				public boolean hasCapability(@Nonnull Capability<?> capability, Direction facing) {
					return capability == SHIELDS;
				}

				@Override
				public <T> T getCapability(@Nonnull Capability<T> capability, Direction facing) {
					return capability == SHIELDS ? SHIELDS.<T>cast(inst) : null;
				}

				@Override
				public CompoundNBT serializeNBT() {
					return (CompoundNBT) SHIELDS.getStorage().writeNBT(SHIELDS, inst, null);
				}

				@Override
				public void deserializeNBT(CompoundNBT nbt) {
					SHIELDS.getStorage().readNBT(SHIELDS, inst, null, nbt);
				}

			});
		}
	}
}
