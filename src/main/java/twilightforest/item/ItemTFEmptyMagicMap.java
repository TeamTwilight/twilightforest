package twilightforest.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import twilightforest.TFAchievementPage;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFEmptyMagicMap extends ItemMapBase implements ModelRegisterCallback
{
    protected ItemTFEmptyMagicMap()
    {
		this.setCreativeTab(TFItems.creativeTab);
    }

    // [VanillaCopy] ItemEmptyMap.onItemRightClick, edits noted
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        // TF - own string id
        ItemStack itemstack = new ItemStack(Items.FILLED_MAP, 1, worldIn.getUniqueDataId(ItemTFMagicMap.STR_ID));
        String s = ItemTFMagicMap.STR_ID + "_" + itemstack.getMetadata();
        MapData mapdata = new MapData(s);
        worldIn.setData(s, mapdata);
        mapdata.scale = 4; // TF - hardcode scale
        mapdata.calculateMapCenter(playerIn.posX, playerIn.posZ, mapdata.scale);
        mapdata.dimension = worldIn.provider.getDimension();
        mapdata.trackingPosition = true;
        mapdata.markDirty();
        --itemStackIn.stackSize;

        // TF - achievement
        playerIn.addStat(TFAchievementPage.twilightMagicMap);

        if (itemStackIn.stackSize <= 0)
        {
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            if (!playerIn.inventory.addItemStackToInventory(itemstack.copy()))
            {
                playerIn.dropItem(itemstack, false);
            }

            playerIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        }
    }
}
