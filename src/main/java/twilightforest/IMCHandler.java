package twilightforest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import twilightforest.world.feature.TFGenCaveStalactite;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMCHandler {

	private static final ImmutableList.Builder<BlockState> ORE_BLOCKS_BUILDER = ImmutableList.builder();
	private static final ImmutableList.Builder<ItemStack> LOADING_ICONS_BUILDER = ImmutableList.builder();
	private static final ImmutableMultimap.Builder<BlockState, BlockState> CRUMBLE_BLOCKS_BUILDER = ImmutableMultimap.builder();
	private static final ImmutableMultimap.Builder<Integer, TFGenCaveStalactite.StalactiteEntry> STALACTITE_BUILDER = ImmutableMultimap.builder();

	/**
	 IMC NBT Format: You can send all of your requests as one big NBT list rather than needing to shotgun a ton of tiny NBT messages.
	 <pre>
	 root:
		 • "Ore_Blocks"                              - NBTTagList     : List of blockstates to add to Hollow Hills and Ore Magnets - May change this to a function in the future
			 • List Entry                            - CompoundNBT : An BlockState
				 • "Name"                            - String         : Resource location of block. Is not allowed to be Air.
				 • "Properties"                      - CompoundNBT : Blockstate Properties
					 • [String Property Key]         - String         : Key is nameable to a property key, and the string value attached to it is value to property.
	 			 • "Stalactite_Settings"             - CompoundNBT : Settings for stalactite generation, exclude if only used for Ore Magnets. May be empty for all default values.
	 			 	 • "Weight"                      - Integer        : Ore weight, decides how often the ore generates. Defaults to 15.
	 				 • "Hill_Size"                   - Integer        : Minimum hill size for the ore stalactite to generate. 1-small, 2-medium, 3-large. Defaults to 3.
	 				 • "Size"                        - Float          : Decides the maximum length of the stalactite relative to the space between hill floor and ceiling. Defaults to 0.7.
	 				 • "Max_Length"                  - Integer        : Maximum length of a stalactite in blocks. Defaults to 8.
	 				 • "Min_Height"                  - Integer        : Minimum space between the hill floor and the stalactite to let it generate. Defaults to 1.

		 • "Crumbling"                               - NBTTagList     : List of blockstates to add to crumble horn crumbling - May change this to a function in the future
			 • List Entry                            - CompoundNBT : An BlockState
				 • "Name"                            - String         : Resource location of block. Is not allowed to be Air.
				 • "Properties"                      - CompoundNBT : Blockstate Properties
				 • "Crumbles"                        - NBTTagList     : List of different blockstates that the blockstate can crumble into
					 • List Entry                    - CompoundNBT : An BlockState.
						 • "Name"                    - String         : Resource location of block. Can be Air.
						 • "Properties"              - CompoundNBT : Blockstate Properties
							 • [String Property Key] - String         : Key is nameable to a property key, and the string value attached to it is value to property.
	 </pre>
	 */
	@SubscribeEvent
	public static void onIMC(InterModProcessEvent event) {
		InterModComms.getMessages(TwilightForestMod.ID).forEach(message-> {
			Object thing = message.getMessageSupplier().get();
			if (thing instanceof CompoundNBT) {
				CompoundNBT imcCompound = ((CompoundNBT) thing);

				readFromTagList(imcCompound.getList("Ore_Blocks", Constants.NBT.TAG_COMPOUND), IMCHandler::handleOre);
				readFromTagList(imcCompound.getList("Crumbling",  Constants.NBT.TAG_COMPOUND), IMCHandler::handleCrumble);
			}

			if (thing instanceof ItemStack && message.getMethod().equals("Loading_Icon")) {
				LOADING_ICONS_BUILDER.add((ItemStack) thing);
			}
		});
	}

	private static void readFromTagList(ListNBT list, Consumer<CompoundNBT> consumer) {
		for (int i = 0; i < list.size(); i++) {
			consumer.accept(list.getCompound(i));
		}
	}

	private static void readStatesFromTagList(ListNBT list, Consumer<BlockState> consumer) {
		for (int i = 0; i < list.size(); i++) {
			BlockState state = NBTUtil.readBlockState(list.getCompound(i));
			if (state.getBlock() != Blocks.AIR) {
				consumer.accept(state);
			}
		}
	}

	private static void handleCrumble(CompoundNBT nbt) {
		BlockState key = NBTUtil.readBlockState(nbt);
		if (key.getBlock() != Blocks.AIR) {
			readStatesFromTagList(nbt.getList("Crumbling", Constants.NBT.TAG_COMPOUND), value -> CRUMBLE_BLOCKS_BUILDER.put(key, value));
		}
	}

	private static void handleOre(CompoundNBT nbt) {
		BlockState nbtState = NBTUtil.readBlockState(nbt);

		if (nbtState.getBlock() != Blocks.AIR) {
			ORE_BLOCKS_BUILDER.add(nbtState);

			if (nbt.contains("Stalactite_Settings", Constants.NBT.TAG_COMPOUND)) {
				CompoundNBT settings = nbt.getCompound("Stalactite_Settings");
				int weight    = readInt(settings, "Weight", 15);
				int hillSize  = readInt(settings, "Hill_Size", 3);
				float size    = readFloat(settings, "Size", 0.7f);
				int maxLength = readInt(settings, "Max_Length", 8);
				int minHeight = readInt(settings, "Min_Height", 1);
				STALACTITE_BUILDER.put(hillSize, new TFGenCaveStalactite.StalactiteEntry(nbtState, size, maxLength, minHeight, weight));
			}
		}
	}

	private static int readInt(CompoundNBT tag, String key, int defaultValue) {
		return tag.contains(key, Constants.NBT.TAG_ANY_NUMERIC) ? tag.getInt(key) : defaultValue;
	}

	private static float readFloat(CompoundNBT tag, String key, float defaultValue) {
		return tag.contains(key, Constants.NBT.TAG_ANY_NUMERIC) ? tag.getFloat(key) : defaultValue;
	}

	public static ImmutableList<BlockState> getOreBlocks() {
		return ORE_BLOCKS_BUILDER.build();
	}

	public static ImmutableList<ItemStack> getLoadingIconStacks() {
		return LOADING_ICONS_BUILDER.build();
	}

	public static ImmutableMultimap<BlockState, BlockState> getCrumblingBlocks() {
		return CRUMBLE_BLOCKS_BUILDER.build();
	}

	public static ImmutableMultimap<Integer, TFGenCaveStalactite.StalactiteEntry> getStalactites() {
		return STALACTITE_BUILDER.build();
	}
}
