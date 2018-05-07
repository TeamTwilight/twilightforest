package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.TFPacketHandler;
import twilightforest.biomes.TFBiomes;
import twilightforest.item.ItemTFOreMagnet;
import twilightforest.item.TFItems;
import twilightforest.network.PacketChangeBiome;

import java.util.ArrayList;
import java.util.Random;

public class BlockTFMagicLogSpecial extends BlockTFMagicLog {
	protected BlockTFMagicLogSpecial() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public int tickRate(World par1World) {
		return 20;
	}

	@Override
	public void onBlockAdded(World par1World, BlockPos pos, IBlockState state) {
		par1World.scheduleUpdate(pos, this, this.tickRate(par1World));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
		return Item.getItemFromBlock(TFBlocks.magic_log);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (state.getValue(LOG_AXIS) != EnumAxis.NONE) return;

		if (!world.isRemote) {
		    switch (state.getValue(VARIANT)) {
                case TIME:
                    world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.1F, 0.5F);
                    doTreeOfTimeEffect(world, pos, rand);
                    break;
                case TRANS:
                    doTreeOfTransformationEffect(world, pos, rand);
                    break;
                case MINE:
                    doMinersTreeEffect(world, pos, rand);
                    break;
                case SORT:
                    doSortingTreeEffect(world, pos, rand);
                    break;
            }
        }

		world.scheduleUpdate(pos, this, this.tickRate(world));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumHand hand, EnumFacing side, float par7, float par8, float par9) {
		if (state.getValue(LOG_AXIS) != EnumAxis.NONE) {
			world.setBlockState(pos, state.withProperty(LOG_AXIS, EnumAxis.NONE));
			world.scheduleUpdate(pos, this, this.tickRate(world));
			return true;
		} else if (state.getValue(LOG_AXIS) == EnumAxis.NONE) {
			world.setBlockState(pos, state.withProperty(LOG_AXIS, EnumAxis.Y));
			return true;
		}

		return false;
	}

	/**
	 * The tree of time adds extra ticks to blocks, so that they have twice the normal chance to get a random tick
	 */
	private void doTreeOfTimeEffect(World world, BlockPos pos, Random rand) {
		int numticks = 8 * 3 * this.tickRate(world);

		for (int i = 0; i < numticks; i++) {
			BlockPos dPos = pos.add(
					rand.nextInt(32) - 16,
					rand.nextInt(32) - 16,
					rand.nextInt(32) - 16
			);

			IBlockState thereState = world.getBlockState(dPos);
			Block thereID = thereState.getBlock();

			if (thereID != Blocks.AIR && thereID.getTickRandomly()) {
				thereID.updateTick(world, dPos, thereState, rand);

                TileEntity te = world.getTileEntity(pos);
				if (te instanceof ITickable) ((ITickable) te).update();
			}
		}
	}

	/**
	 * The tree of transformation transforms the biome in the area near it into the enchanted forest biome.
	 * <p>
	 * TODO: also change entities
	 */
	private void doTreeOfTransformationEffect(World world, BlockPos pos, Random rand) {
		for (int i = 0; i < 1; i++) {
			BlockPos dPos = pos.add(rand.nextInt(32) - 16, 0, rand.nextInt(32) - 16);

			world.playSound(null, pos, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 0.1F, rand.nextFloat() * 2F);

			if (dPos.distanceSq(pos) < 256) {
				Biome biomeAt = world.getBiome(dPos);

				if (biomeAt != TFBiomes.enchantedForest) {
					Chunk chunkAt = world.getChunkFromBlockCoords(dPos);
					chunkAt.getBiomeArray()[(dPos.getZ() & 15) << 4 | (dPos.getX() & 15)] = (byte) Biome.getIdForBiome(TFBiomes.enchantedForest);

					if (world instanceof WorldServer) {
						sendChangedBiome(world, dPos);
					}
				}
			}
		}
	}

	/**
	 * Send a tiny update packet to the client to inform it of the changed biome
	 */
	private void sendChangedBiome(World world, BlockPos pos) {
		IMessage message = new PacketChangeBiome(pos, (byte) Biome.getIdForBiome(TFBiomes.enchantedForest));

		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), 128, pos.getZ(), 128);

		TFPacketHandler.CHANNEL.sendToAllAround(message, targetPoint);
	}

	/**
	 * The miner's tree generates the ore magnet effect randomly every second
	 */
	private void doMinersTreeEffect(World world, BlockPos pos, Random rand) {
        BlockPos dPos = pos.add(
				rand.nextInt(64) - 32,
				rand.nextInt(64) - 32,
				rand.nextInt(64) - 32
		);

		//world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.click", 0.1F, 0.5F);

		int moved = ItemTFOreMagnet.doMagnet(world, pos, dPos);

		if (moved > 0) {
			world.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.1F, 1.0F);
		}
	}

	/**
	 * The sorting tree finds two chests nearby and then attempts to sort a random item.
	 */
	private void doSortingTreeEffect(World world, BlockPos pos, Random rand) {
		// find all the chests nearby
		int XSEARCH = 16;
		int YSEARCH = 16;
		int ZSEARCH = 16;

		ArrayList<IInventory> chests = new ArrayList<IInventory>();
		int itemCount = 0;

		for (int sx = pos.getX() - XSEARCH; sx < pos.getX() + XSEARCH; sx++) {
			for (int sy = pos.getY() - YSEARCH; sy < pos.getY() + YSEARCH; sy++) {
				for (int sz = pos.getZ() - ZSEARCH; sz < pos.getZ() + ZSEARCH; sz++) {
					BlockPos iterPos = new BlockPos(sx, sy, sz);
					if (world.getBlockState(iterPos).getBlock() == Blocks.CHEST) {
						IInventory thisChest = Blocks.CHEST.getLockableContainer(world, iterPos);

						// make sure we haven't counted this chest
						if (thisChest != null && !checkIfChestsContains(chests, (IInventory) world.getTileEntity(iterPos))) {
							int itemsInChest = 0;

							// count items
							for (int i = 0; i < thisChest.getSizeInventory(); i++) {
								if (!thisChest.getStackInSlot(i).isEmpty()) {
									itemsInChest++;
									itemCount++;
								}
							}

							// only add non-empty chests
							if (itemsInChest > 0) {
								chests.add(thisChest);
							}
						}
					}
				}
			}
		}

		//FMLLog.info("Found " + chests.size() + " non-empty chests, containing " + itemCount + " items");

		// find a random item in one of the chests
		ItemStack beingSorted = ItemStack.EMPTY;
		int sortedChestNum = -1;
		int sortedSlotNum = -1;

		if (itemCount > 0) {
			int itemNumber = rand.nextInt(itemCount);
			int currentNumber = 0;

			for (int i = 0; i < chests.size(); i++) {
				IInventory chest = chests.get(i);
				for (int slotNum = 0; slotNum < chest.getSizeInventory(); slotNum++) {
					ItemStack currentItem = chest.getStackInSlot(slotNum);

					if (!currentItem.isEmpty()) {
						if (currentNumber++ == itemNumber) {
							beingSorted = currentItem;
							sortedChestNum = i;
							sortedSlotNum = slotNum;
						}
					}
				}
			}
		}

		//FMLLog.info("Decided to sort item " + beingSorted);

		if (!beingSorted.isEmpty()) {
			int matchChestNum = -1;
			int matchCount = 0;

			// decide where to put it, if anywhere
			for (int chestNum = 0; chestNum < chests.size(); chestNum++) {
				IInventory chest = chests.get(chestNum);
				int currentChestMatches = 0;

				for (int slotNum = 0; slotNum < chest.getSizeInventory(); slotNum++) {

					ItemStack currentItem = chest.getStackInSlot(slotNum);
					if (!currentItem.isEmpty() && isSortingMatch(beingSorted, currentItem)) {
						currentChestMatches += currentItem.getCount();
					}
				}

				if (currentChestMatches > matchCount) {
					matchCount = currentChestMatches;
					matchChestNum = chestNum;
				}
			}

			// soooo, did we find a better match?
			if (matchChestNum >= 0 && matchChestNum != sortedChestNum) {
				IInventory moveChest = chests.get(matchChestNum);
				IInventory oldChest = chests.get(sortedChestNum);

				// is there an empty inventory slot in the new chest?
				int moveSlot = getEmptySlotIn(moveChest);

				if (moveSlot >= 0) {
					// remove old item
					oldChest.setInventorySlotContents(sortedSlotNum, ItemStack.EMPTY);

					// add new item
					moveChest.setInventorySlotContents(moveSlot, beingSorted);

					//FMLLog.info("Moved sorted item " + beingSorted + " to chest " + matchChestNum + ", slot " + moveSlot);
				}
			}

			// if the stack is not full, combine items from other stacks
			if (beingSorted.getCount() < beingSorted.getMaxStackSize()) {
				for (IInventory chest : chests) {
					for (int slotNum = 0; slotNum < chest.getSizeInventory(); slotNum++) {
						ItemStack currentItem = chest.getStackInSlot(slotNum);

						if (!currentItem.isEmpty() && currentItem != beingSorted && beingSorted.isItemEqual(currentItem)) {
							if (currentItem.getCount() <= (beingSorted.getMaxStackSize() - beingSorted.getCount())) {
								chest.setInventorySlotContents(slotNum, ItemStack.EMPTY);
								beingSorted.grow(currentItem.getCount());
								currentItem.setCount(0);
							}
						}
					}
				}
			}

		}
	}

	private boolean isSortingMatch(ItemStack beingSorted, ItemStack currentItem) {
		return beingSorted.getItem().getCreativeTab() == currentItem.getItem().getCreativeTab();
	}

	/**
	 * Is the chest we're testing part of our chest list already?
	 */
	private boolean checkIfChestsContains(ArrayList<IInventory> chests, IInventory testChest) {
		for (IInventory chest : chests) {
			if (chest == testChest) {
				return true;
			}

			if (chest instanceof InventoryLargeChest && ((InventoryLargeChest) chest).isPartOfLargeChest(testChest)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return an empty slot number in the chest, or -1 if the chest is full
	 */
	private int getEmptySlotIn(IInventory chest) {
		for (int i = 0; i < chest.getSizeInventory(); i++) {
			if (chest.getStackInSlot(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}

	@Override
	@Deprecated
	public int getLightValue(IBlockState state) {
		return 15;
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}
}
