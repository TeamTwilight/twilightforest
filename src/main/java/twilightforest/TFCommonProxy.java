package twilightforest;

import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.client.particle.TFParticleType;
import twilightforest.inventory.ContainerTFUncrafting;
import net.minecraftforge.fml.common.network.IGuiHandler;
import twilightforest.tileentity.TileEntityTFCinderFurnace;

public class TFCommonProxy implements IGuiHandler {
	
	/**
	 * Called during the pre-load step.  Register stuff here.  
	 * Obviously most stuff in the common category will be just registered in the mod file
	 */
	public void doPreLoadRegistration() {
		;
	}
	
	/**
	 * Called during the load step.  Register stuff here.  
	 * Obviously most stuff in the common category will be just registered in the mod file
	 */
	public void doOnLoadRegistration() {
		;
	}

	public int getCritterBlockRenderID() {
		return 0;
	}

	public int getPlantBlockRenderID() {
		return 0;
	}

	public int getComplexBlockRenderID() {
		return 0;
	}

	public int getNagastoneBlockRenderID() {
		return 0;
	}
	
	public int getMagicLeavesBlockRenderID() {
		return 0;
	}
	
	public int getPedestalBlockRenderID() {
		return 0;
	}
	
	public int getThornsBlockRenderID() {
		return 0;
	}
	
	public int getKnightmetalBlockRenderID() {
		return 0;
	}
	
	public int getHugeLilyPadBlockRenderID() {
		return 0;
	}
	
	public int getCastleMagicBlockRenderID() {
		return 0;
	}
	
	public World getClientWorld() {
		return null;
	}
	
	public void spawnParticle(World world, TFParticleType particleType, double x, double y, double z, double velX, double velY, double velZ) {}

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
	
	public void doBlockAnnihilateEffect(World world, BlockPos pos) { }

}
