package twilightforest;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import twilightforest.client.particle.TFParticleType;
import twilightforest.inventory.ContainerTFUncrafting;
import twilightforest.tileentity.TileEntityTFCinderFurnace;
import twilightforest.tileentity.critters.*;

public class TFCommonProxy implements IGuiHandler {

	public void preInit() {}

	public void init() {}

	public void spawnParticle(TFParticleType particleType, double x, double y, double z, double vx, double vy, double vz) {}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == TwilightForestMod.GUI_ID_UNCRAFTING) {
			return new ContainerTFUncrafting(player.inventory, world, x, y, z);
		} else if (id == TwilightForestMod.GUI_ID_FURNACE) {
			return new ContainerFurnace(player.inventory, (TileEntityTFCinderFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else {
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == TwilightForestMod.GUI_ID_UNCRAFTING) {
			return new twilightforest.client.GuiTFGoblinCrafting(player.inventory, world, x, y, z);
		} else if (id == TwilightForestMod.GUI_ID_FURNACE) {
			return new GuiFurnace(player.inventory, (TileEntityTFCinderFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		} else {
			return null;
		}
	}

	public ModelBiped getKnightlyArmorModel(EntityEquipmentSlot armorSlot) {
		return null;
	}

	public ModelBiped getPhantomArmorModel(EntityEquipmentSlot armorSlot) {
		return null;
	}

	public ModelBiped getYetiArmorModel(EntityEquipmentSlot armorSlot) {
		return null;
	}

	public ModelBiped getArcticArmorModel(EntityEquipmentSlot armorSlot) {
		return null;
	}

	public ModelBiped getFieryArmorModel(EntityEquipmentSlot armorSlot) {
		return null;
	}

	public boolean doesPlayerHaveAdvancement(EntityPlayer player, ResourceLocation advId) {
		if (player instanceof EntityPlayerMP) {
			Advancement adv = ((EntityPlayerMP) player).getServerWorld().getAdvancementManager().getAdvancement(advId);
			return adv != null && ((EntityPlayerMP) player).getAdvancements().getProgress(adv).isDone();
		}
		return false;
	}

	public TileEntityTFCicada getNewCicadaTE() {
		return new TileEntityTFCicada();
	}

	public TileEntityTFFirefly getNewFireflyTE() {
		return new TileEntityTFFirefly();
	}

	public TileEntityTFMoonworm getNewMoonwormTE() {
		return new TileEntityTFMoonworm();
	}

	public void registerCritterTileEntities() {
		GameRegistry.registerTileEntity(TileEntityTFFirefly.class,  prefix("firefly" ));
		GameRegistry.registerTileEntity(TileEntityTFCicada.class,   prefix("cicada"  ));
		GameRegistry.registerTileEntity(TileEntityTFMoonworm.class, prefix("moonworm"));
	}

	protected static ResourceLocation prefix(String name) {
		return new ResourceLocation(TwilightForestMod.ID, name);
	}
}
