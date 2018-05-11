package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFCubeOfAnnihilation;

import javax.annotation.Nullable;
import java.util.UUID;

public class ItemTFCubeOfAnnihilation extends ItemTF {
	private static final String THROWN_UUID_KEY = "cubeEntity";

	protected ItemTFCubeOfAnnihilation() {
		this.maxStackSize = 1;
		this.setCreativeTab(TFItems.creativeTab);
		this.addPropertyOverride(new ResourceLocation(TwilightForestMod.ID, "thrown"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			@Override
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return getThrownUuid(stack) != null ? 1 : 0;
			}
		});
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity holder, int slot, boolean isSelected) {
		if (!world.isRemote && getThrownUuid(stack) != null && getThrownEntity(world, stack) == null) {
			stack.getTagCompound().removeTag(THROWN_UUID_KEY + "Most");
			stack.getTagCompound().removeTag(THROWN_UUID_KEY + "Least");
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (getThrownUuid(stack) != null)
			return ActionResult.newResult(EnumActionResult.PASS, stack);

		if (!world.isRemote) {
			EntityTFCubeOfAnnihilation launchedCube = new EntityTFCubeOfAnnihilation(world, player);
			world.spawnEntity(launchedCube);
			setThrownEntity(stack, launchedCube);
		}

		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Nullable
	private static UUID getThrownUuid(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasUniqueId(THROWN_UUID_KEY)) {
			return stack.getTagCompound().getUniqueId(THROWN_UUID_KEY);
		}

		return null;
	}

	@Nullable
	private static EntityTFCubeOfAnnihilation getThrownEntity(World world, ItemStack stack) {
		if (world instanceof WorldServer) {
			UUID id = getThrownUuid(stack);
			if (id != null) {
				Entity e = ((WorldServer) world).getEntityFromUuid(id);
				if (e instanceof EntityTFCubeOfAnnihilation) {
					return (EntityTFCubeOfAnnihilation) e;
				}
			}
		}

		return null;
	}

	private static void setThrownEntity(ItemStack stack, EntityTFCubeOfAnnihilation cube) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setUniqueId(THROWN_UUID_KEY, cube.getUniqueID());
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BLOCK;
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
		return true;
	}
}
