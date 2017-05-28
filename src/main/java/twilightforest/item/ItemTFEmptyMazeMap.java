package twilightforest.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import twilightforest.TFAchievementPage;
import twilightforest.TFFeature;
import twilightforest.TFMazeMapData;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.world.WorldProviderTwilightForest;

public class ItemTFEmptyMazeMap extends ItemMapBase implements ModelRegisterCallback
{
	boolean mapOres; 
	
    protected ItemTFEmptyMazeMap(boolean mapOres)
    {
		this.setCreativeTab(TFItems.creativeTab);
        this.mapOres = mapOres;
    }

    // [VanillaCopy] ItemEmptyMap.onItemRightClick, edits noted
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        // TF - Use own string ID and MapData class
        ItemStack itemstack = new ItemStack(Items.FILLED_MAP, 1, worldIn.getUniqueDataId(ItemTFMazeMap.STR_ID));
        String s = ItemTFMazeMap.STR_ID + "_" + itemstack.getMetadata();
        TFMazeMapData mapdata = new TFMazeMapData(s);
        worldIn.setData(s, mapdata);
        mapdata.scale = 0;

        // TF - Center map exactly on player like in 1.7 and below, instead of snapping to grid
        int step = 128 * (1 << mapdata.scale);
        // need to fix center for feature offset
        if (worldIn.provider instanceof WorldProviderTwilightForest && TFFeature.getFeatureForRegion(MathHelper.floor(playerIn.posX) >> 4, MathHelper.floor(playerIn.posZ) >> 4, worldIn) == TFFeature.labyrinth) {
            BlockPos mc = TFFeature.getNearestCenterXYZ(MathHelper.floor(playerIn.posX) >> 4, MathHelper.floor(playerIn.posZ) >> 4, worldIn);
            mapdata.xCenter = mc.getX();
            mapdata.zCenter = mc.getZ();
            mapdata.yCenter = MathHelper.floor(playerIn.posY);
        } else {
            mapdata.xCenter = (int)(Math.round(playerIn.posX / step) * step) + 10; // mazes are offset slightly
            mapdata.zCenter = (int)(Math.round(playerIn.posZ / step) * step) + 10; // mazes are offset slightly
            mapdata.yCenter = MathHelper.floor(playerIn.posY);
        }

        mapdata.dimension = worldIn.provider.getDimension();
        mapdata.trackingPosition = true;
        mapdata.markDirty();
        --itemStackIn.stackSize;

        // TF - achievements
        if (itemstack.getItem() == TFItems.mazeMap) {
            playerIn.addStat(TFAchievementPage.twilightMazeMap);
        }

        if (itemstack.getItem() == TFItems.oreMap) {
            playerIn.addStat(TFAchievementPage.twilightOreMap);
        }

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
