package twilightforest.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.structures.darktower.StructureDecoratorDarkTower;
import twilightforest.structures.finalcastle.StructureTFDecoratorCastle;
import twilightforest.structures.icetower.StructureDecoratorIceTower;
import twilightforest.structures.mushroomtower.StructureDecoratorMushroomTower;
import twilightforest.structures.stronghold.StructureTFDecoratorStronghold;

/**
 * Stores information about what blocks to use in constructing this structure
 *
 * @author Ben
 */
public class StructureTFDecorator {
	public IBlockState blockState = Blocks.STONE.getDefaultState();
	public IBlockState accentState = Blocks.COBBLESTONE.getDefaultState();
	public IBlockState stairState = Blocks.STONE_STAIRS.getDefaultState();
	public IBlockState fenceState = Blocks.OAK_FENCE.getDefaultState();
	public IBlockState pillarState = Blocks.STONEBRICK.getDefaultState();
	public IBlockState platformState = Blocks.STONE_SLAB.getDefaultState();
	public IBlockState floorState = Blocks.STONEBRICK.getDefaultState();
	public IBlockState roofState = Blocks.STONEBRICK.getDefaultState();

	public StructureComponent.BlockSelector randomBlocks = new StructureTFStrongholdStones();

	public static String getDecoString(StructureTFDecorator deco) {
//TODO: Structure Disabled
		if (deco instanceof StructureDecoratorDarkTower) {
			return "DecoDarkTower";
		}
		if (deco instanceof StructureDecoratorIceTower) {
			return "DecoIceTower";
		}
		if (deco instanceof StructureDecoratorMushroomTower) {
			return "DecoMushroomTower";
		}
		if (deco instanceof StructureTFDecoratorStronghold) {
			return "DecoStronghold";
		}
		if (deco instanceof StructureTFDecoratorCastle) {
			return "DecoCastle";
		}

		return "";
	}

	public static StructureTFDecorator getDecoFor(String decoString) {
//TODO: Structure Disabled
		if (decoString.equals("DecoDarkTower")) {
			return new StructureDecoratorDarkTower();
		}
		if (decoString.equals("DecoIceTower")) {
			return new StructureDecoratorIceTower();
		}
		if (decoString.equals("DecoMushroomTower")) {
			return new StructureDecoratorMushroomTower();
		}
		if (decoString.equals("DecoStronghold")) {
			return new StructureTFDecoratorStronghold();
		}
		if (decoString.equals("DecoCastle")) {
			return new StructureTFDecoratorCastle();
		}

		return new StructureTFDecorator();
	}
}
