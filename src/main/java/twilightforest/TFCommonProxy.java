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

	public World getClientWorld() {
		return null;
	}

	public void spawnParticle(World world, TFParticleType particleType, double x, double y, double z, double velX, double velY, double velZ) {
	}

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

	public void doBlockAnnihilateEffect(World world, BlockPos pos) {
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
		GameRegistry.registerTileEntity(TileEntityTFFirefly.class, "firefly");
		GameRegistry.registerTileEntity(TileEntityTFCicada.class, "cicada");
		GameRegistry.registerTileEntity(TileEntityTFMoonworm.class, "moonworm");
	}
}
