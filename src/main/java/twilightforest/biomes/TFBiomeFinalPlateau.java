package twilightforest.biomes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.stats.Achievement;
import twilightforest.TFAchievementPage;
import twilightforest.block.BlockTFDeadrock;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.DeadrockVariant;


public class TFBiomeFinalPlateau extends TFBiomeBase {

	public TFBiomeFinalPlateau(BiomeProperties props) {
		super(props);

		this.topBlock = TFBlocks.deadrock.getDefaultState().withProperty(BlockTFDeadrock.VARIANT, DeadrockVariant.SURFACE);
		this.fillerBlock = TFBlocks.deadrock.getDefaultState().withProperty(BlockTFDeadrock.VARIANT, DeadrockVariant.CRACKED);

		((TFBiomeDecorator) decorator).hasCanopy = false;
		getTFBiomeDecorator().setTreesPerChunk(-999);

		this.decorator.generateLakes = false;

		// custom creature list.
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(twilightforest.entity.passive.EntityTFRaven.class, 10, 4, 4));
	}

	@Override
	public IBlockState getStoneReplacementState() {
		return TFBlocks.deadrock.getDefaultState().withProperty(BlockTFDeadrock.VARIANT, DeadrockVariant.SOLID);
	}

	@Override
	protected Achievement getRequiredAchievement() {
		return TFAchievementPage.twilightProgressGlacier;
	}
}
