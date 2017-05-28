package twilightforest.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;

/**
 *	Stores information about what blocks to use in constructing this structure
 * 
 * @author Ben
 *
 */
public class StructureTFDecorator 
{
	public IBlockState blockState = Blocks.STONE.getDefaultState();
	public IBlockState accentState = Blocks.COBBLESTONE.getDefaultState();
	public IBlockState stairState = null;
	public IBlockState fenceState = null;
	public IBlockState pillarState = null;
	public IBlockState platformState = null;
	public IBlockState floorState = null;
	public IBlockState roofState = null;

	public StructureComponent.BlockSelector randomBlocks = new StructureTFStrongholdStones();
	
	public static String getDecoString(StructureTFDecorator deco)
	{
//TODO: Structure Disabled
//		if (deco instanceof StructureDecoratorDarkTower)
//		{
//			return "DecoDarkTower";
//		}
//		if (deco instanceof StructureDecoratorIceTower)
//		{
//			return "DecoIceTower";
//		}
//		if (deco instanceof StructureDecoratorMushroomTower)
//		{
//			return "DecoMushroomTower";
//		}
//		if (deco instanceof StructureTFDecoratorStronghold)
//		{
//			return "DecoStronghold";
//		}
//		if (deco instanceof StructureTFDecoratorCastle)
//		{
//			return "DecoCastle";
//		}
		
		return "";
	}
	
	public static StructureTFDecorator getDecoFor(String decoString)
	{
//TODO: Structure Disabled
//		if (decoString.equals("DecoDarkTower"))
//		{
//			return new StructureDecoratorDarkTower();
//		}
//		if (decoString.equals("DecoIceTower"))
//		{
//			return new StructureDecoratorIceTower();
//		}
//		if (decoString.equals("DecoMushroomTower"))
//		{
//			return new StructureDecoratorMushroomTower();
//		}
//		if (decoString.equals("DecoStronghold"))
//		{
//			return new StructureTFDecoratorStronghold();
//		}
//		if (decoString.equals("DecoCastle"))
//		{
//			return new StructureTFDecoratorCastle();
//		}
		
		return new StructureTFDecorator();
	}
}
