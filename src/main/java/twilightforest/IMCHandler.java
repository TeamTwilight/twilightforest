package twilightforest;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import twilightforest.world.feature.TFGenCaveStalactite;

import java.util.function.Consumer;

public class IMCHandler {
	private static final ImmutableSet.Builder<IBlockState> BLACKLIST_BUILDER = ImmutableSet.builder();
	private static final ImmutableList.Builder<IBlockState> ORE_BLOCKS_BUILDER = ImmutableList.builder();
	private static final ImmutableList.Builder<ItemStack> LOADING_ICONS_BUILDER = ImmutableList.builder();
	private static final ImmutableMultimap.Builder<IBlockState, IBlockState> CRUMBLE_BLOCKS_BUILDER = ImmutableMultimap.builder();
	private static final ImmutableMultimap.Builder<Integer, TFGenCaveStalactite.StalactiteEntry> STALACTITE_BUILDER = ImmutableMultimap.builder();

	/**
	 IMC NBT Format: You can send all of your requests as one big NBT list rather than needing to shotgun a ton of tiny NBT messages.
	 <pre>
	 root:
		 • "Blacklist"                               - NBTTagList     : List of blockstates to blacklist from blockbreaking (antibuilders, naga, hydra, etc)
			 • List Entry                            - NBTTagCompound : An IBlockState
				 • "Name"                            - String         : Resource location of block. Is not allowed to be Air.
				 • "Properties"                      - NBTTagCompound : Blockstate Properties
					 • [String Property Key]         - String         : Key is nameable to a property key, and the string value attached to it is value to property.

		 • "Ore_Blocks"                              - NBTTagList     : List of blockstates to add to Hollow Hills and Ore Magnets - May change this to a function in the future
			 • List Entry                            - NBTTagCompound : An IBlockState and/or an oredict name
				 • "Name"                            - String         : Resource location of block. Is not allowed to be Air.
				 • "Properties"                      - NBTTagCompound : Blockstate Properties
					 • [String Property Key]         - String         : Key is nameable to a property key, and the string value attached to it is value to property.
	 			 • "Stalactite_Settings"             - NBTTagCompound : Settings for stalactite generation, exclude if only used for Ore Magnets. May be empty for all default values.
	 			 	 • "Weight"                      - Integer        : Ore weight, decides how often the ore generates. Defaults to 15.
	 				 • "Hill_Size"                   - Integer        : Minimum hill size for the ore stalactite to generate. 1-small, 2-medium, 3-large. Defaults to 3.
	 				 • "Size"                        - Float          : Decides the maximum length of the stalactite relative to the space between hill floor and ceiling. Defaults to 0.7.
	 				 • "Max_Length"                  - Integer        : Maximum length of a stalactite in blocks. Defaults to 8.
	 				 • "Min_Height"                  - Integer        : Minimum space between the hill floor and the stalactite to let it generate. Defaults to 1.

		 • "Crumbling"                               - NBTTagList     : List of blockstates to add to crumble horn crumbling - May change this to a function in the future
			 • List Entry                            - NBTTagCompound : An IBlockState
				 • "Name"                            - String         : Resource location of block. Is not allowed to be Air.
				 • "Properties"                      - NBTTagCompound : Blockstate Properties
				 • "Crumbles"                        - NBTTagList     : List of different blockstates that the blockstate can crumble into
					 • List Entry                    - NBTTagCompound : An IBlockState.
						 • "Name"                    - String         : Resource location of block. Can be Air.
						 • "Properties"              - NBTTagCompound : Blockstate Properties
							 • [String Property Key] - String         : Key is nameable to a property key, and the string value attached to it is value to property.
	 </pre>
	 */

	public static void onIMC(FMLInterModComms.IMCEvent event) {
		for (FMLInterModComms.IMCMessage message : event.getMessages()) {
			if (message.isNBTMessage()) {
				NBTTagCompound imcCompound = message.getNBTValue();

				deserializeBlockstatesFromTagList(imcCompound.getTagList("Blacklist",  Constants.NBT.TAG_COMPOUND), BLACKLIST_BUILDER);
				readFromTagList(imcCompound.getTagList("Ore_Blocks", Constants.NBT.TAG_COMPOUND), IMCHandler::handleOre);
				readFromTagList(imcCompound.getTagList("Crumbling",  Constants.NBT.TAG_COMPOUND), IMCHandler::handleCrumble);
			}

			if (message.isItemStackMessage() && message.key.equals("Loading_Icon")) {
				LOADING_ICONS_BUILDER.add(message.getItemStackValue());
			}
		}
	}

	private static void readFromTagList(NBTTagList list, Consumer<NBTTagCompound> consumer) {
		for (int blockAt = 0; blockAt < list.tagCount(); blockAt++) {
			consumer.accept(list.getCompoundTagAt(blockAt));
		}
	}

	private static void deserializeBlockstatesFromTagList(NBTTagList list, ImmutableCollection.Builder<IBlockState> builder) {
		for (int blockAt = 0; blockAt < list.tagCount(); blockAt++) {
			IBlockState state = NBTUtil.readBlockState(list.getCompoundTagAt(blockAt));

			if (state.getBlock() != Blocks.AIR)
				builder.add(state);
		}
	}

	private static void handleCrumble(NBTTagCompound nbt) {
		IBlockState key = NBTUtil.readBlockState(nbt);

		if (key.getBlock() != Blocks.AIR) {
			NBTTagList crumbles = nbt.getTagList("Crumbling", Constants.NBT.TAG_COMPOUND);

			for (int crumble = 0; crumble < crumbles.tagCount(); crumble++) {
				IBlockState value = NBTUtil.readBlockState(crumbles.getCompoundTagAt(crumble));

				CRUMBLE_BLOCKS_BUILDER.put(key, value);
			}
		}
	}

	private static void handleOre(NBTTagCompound nbt) {
		IBlockState nbtState = NBTUtil.readBlockState(nbt);

		if (nbtState.getBlock() != Blocks.AIR) {
			ORE_BLOCKS_BUILDER.add(nbtState);
			
			if (nbt.hasKey("Stalactite_Settings")) {
				NBTTagCompound settings = nbt.getCompoundTag("Stalactite_Settings");
				int weight    = settings.hasKey("Weight")     ? settings.getInteger("Weight")     : 15;
				int hillSize  = settings.hasKey("Hill_Size")  ? settings.getInteger("Hill_Size")  : 3;
				float size    = settings.hasKey("Size")       ? settings.getFloat("Size")         : 0.7f;
				int maxLength = settings.hasKey("Max_Length") ? settings.getInteger("Max_Length") : 8;
				int minHeight = settings.hasKey("Min_Height") ? settings.getInteger("Min_Height") : 1;
				STALACTITE_BUILDER.put(hillSize, new TFGenCaveStalactite.StalactiteEntry(nbtState, size, maxLength, minHeight, weight));
			}
		}
	}

	public static ImmutableSet<IBlockState> getBlacklistedBlocks() {
		return BLACKLIST_BUILDER.build();
	}

	public static ImmutableList<IBlockState> getOreBlocks() {
		return ORE_BLOCKS_BUILDER.build();
	}

	public static ImmutableList<ItemStack> getLoadingIconStacks() {
		return LOADING_ICONS_BUILDER.build();
	}

	public static ImmutableMultimap<IBlockState, IBlockState> getCrumblingBlocks() {
		return CRUMBLE_BLOCKS_BUILDER.build();
	}

	public static ImmutableMultimap<Integer, TFGenCaveStalactite.StalactiteEntry> getStalactites() {
		return STALACTITE_BUILDER.build();
	}
}
