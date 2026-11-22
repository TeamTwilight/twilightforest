package twilightforest.datagen.data.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.material.VanillaMaterialConditions;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;

// FIXME Convert tfSurface into a registry bootstrap
public class TFMaterialRules {
	private static final MaterialRule BEDROCK = makeStateRule(Blocks.BEDROCK);
	private static final MaterialRule GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
	private static final MaterialRule DIRT = makeStateRule(Blocks.DIRT);
	private static final MaterialRule PODZOL = makeStateRule(Blocks.PODZOL);
	private static final MaterialRule COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
	private static final MaterialRule GRAVEL = makeStateRule(Blocks.GRAVEL);
	private static final MaterialRule SAND = makeStateRule(Blocks.SAND);
	private static final MaterialRule SANDSTONE = makeStateRule(Blocks.SANDSTONE);
	private static final MaterialRule SNOW = makeStateRule(Blocks.SNOW_BLOCK);
	private static final MaterialRule WEATHERED_DEADROCK = makeStateRule(TFBlocks.WEATHERED_DEADROCK.get());
	private static final MaterialRule CRACKED_DEADROCK = makeStateRule(TFBlocks.CRACKED_DEADROCK.get());
	private static final MaterialRule DEADROCK = makeStateRule(TFBlocks.DEADROCK.get());

	private static MaterialRule makeStateRule(Block block) {
		return MaterialRules.state(block.defaultBlockState());
	}

	public static Holder<MaterialRule> tfSurface(HolderGetter<Biome> biomes, HolderGetter<MaterialCondition> conditions) {
		MaterialRule bedrockLayer = MaterialRules.ifTrue(MaterialRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK);

		return Holder.direct(MaterialRules.sequence(
			bedrockLayer,
			highlandsSurface(biomes, conditions),
			deadrockSurface(biomes, conditions),
			snowyForestSurface(biomes, conditions),
			glacierSurface(biomes, conditions),
			overworldLikeFloor(biomes, conditions)
		));
	}

	private static MaterialRule highlandsSurface(HolderGetter<Biome> biomes, HolderGetter<MaterialCondition> conditions) {
		// Make sure it's not a block under the water level
		MaterialRule podzolFloor = MaterialRules.sequence(
			MaterialRules.ifTrue(MaterialRules.waterBlockCheck(-1, 0), PODZOL),
			DIRT
		);

		//highlands has a noise-based mixture of podzol and coarse dirt
		MaterialRule highlandsSoil = MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_FLOOR), MaterialRules.sequence(
			//mix coarse dirt and podzol with noise
			MaterialRules.ifTrue(surfaceNoiseAbove(2.25D), COARSE_DIRT),
			MaterialRules.ifTrue(surfaceNoiseAbove(-2.25D), podzolFloor)
		));

		//check if we're in the highlands
		return MaterialRules.ifTrue(MaterialRules.isBiome(biomes, TFBiomes.HIGHLANDS), highlandsSoil);
	}

	private static MaterialRule deadrockSurface(HolderGetter<Biome> biomes, HolderGetter<MaterialCondition> conditions) {
		//thornlands/plateau has no caves and deadrock instead of stone
		MaterialRule deadrockTerrain = MaterialRules.sequence(
			MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_FLOOR), WEATHERED_DEADROCK),
			MaterialRules.ifTrue(
				MaterialRules.waterStartCheck(-6, -1),
				MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.UNDER_FLOOR), CRACKED_DEADROCK)
			),
			DEADROCK
		);

		//check if we're in the deadrock biomes
		return MaterialRules.ifTrue(MaterialRules.isBiome(biomes, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU), deadrockTerrain);
	}

	private static MaterialRule snowyForestSurface(HolderGetter<Biome> biomes, HolderGetter<MaterialCondition> conditions) {
		// Make sure it's not a block under the water level
		MaterialRule snowFloor = MaterialRules.sequence(
			MaterialRules.ifTrue(MaterialRules.waterBlockCheck(-1, 0), SNOW),
			DIRT
		);

		//snowy forest is all snow on the top layers
		MaterialRule snowySoil = MaterialRules.sequence(
			MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_FLOOR), snowFloor),
			MaterialRules.ifTrue(
				MaterialRules.waterStartCheck(-6, -1),
				// This used to be dirt, but was changed to snow under the first block in later versions. Should this be kept to snow?
				// FIXME Why is the dirt only going 1~2 blocks deep? Should be 3. Looks ugly on Yeti Caves
				MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.UNDER_FLOOR), DIRT)
			)
		);

		//check if we're in the snowy forest
		return MaterialRules.ifTrue(MaterialRules.isBiome(biomes, TFBiomes.SNOWY_FOREST), snowySoil);
	}

	private static MaterialRule glacierSurface(HolderGetter<Biome> biomes, HolderGetter<MaterialCondition> conditions) {
		//glacier has gravel for a few layers, then stone. All blanketed under 30+ blocks of ice
		MaterialRule surfaceUnderPermafrost = MaterialRules.sequence(
			//surface and under is gravel
			MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_FLOOR), GRAVEL),
			MaterialRules.ifTrue(
				MaterialRules.waterStartCheck(-6, -1),
				MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.UNDER_FLOOR), GRAVEL)
			)
		);

		//check if we're in the glacier biome
		return MaterialRules.ifTrue(MaterialRules.isBiome(biomes, TFBiomes.GLACIER), surfaceUnderPermafrost);
	}

	private static MaterialRule overworldLikeFloor(HolderGetter<Biome> biomes, HolderGetter<MaterialCondition> conditions) {
		//lakes and rivers get sand
		MaterialRule riverLakeBeds = MaterialRules.ifTrue(MaterialRules.isBiome(biomes, TFBiomes.LAKE, TFBiomes.STREAM), MaterialRules.sequence(
			MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_CEILING), SANDSTONE),
			MaterialRules.ifTrue(MaterialRules.waterBlockCheck(-1, 0), GRASS_BLOCK),
			SAND
		));

		//make sure the swamps always get grass, they had weird stone patches sometimes
		MaterialRule swampBeds = MaterialRules.ifTrue(MaterialRules.isBiome(biomes, TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP), MaterialRules.sequence(
			MaterialRules.ifTrue(MaterialRules.waterBlockCheck(-1, 0), GRASS_BLOCK),
			DIRT
		));

		//check if we're above ground, so hollow hills dont have grassy floors
		MaterialRule grassAboveSeaLevel = MaterialRules.ifTrue(MaterialRules.yStartCheck(VerticalAnchor.absolute(-4), 1), GRASS_BLOCK);
		//make everything else grass
		MaterialRule grassSurface = MaterialRules.ifTrue(MaterialRules.waterBlockCheck(-1, 0), grassAboveSeaLevel);

		//if we're around the area hollow hill floors are, check if we're underwater. If so place some dirt.
		//This fixes streams having weird stone patches
		MaterialRule underwaterSurface = MaterialRules.ifTrue(
			MaterialRules.not(MaterialRules.yStartCheck(VerticalAnchor.absolute(-4), 1)),
			MaterialRules.ifTrue(
				MaterialRules.not(MaterialRules.waterBlockCheck(-1, 0)),
				DIRT
			)
		);

		// Twilight Forest's surface is based off the normal overworld surface
		MaterialRule onFloor = MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_FLOOR), MaterialRules.sequence(
			riverLakeBeds,
			swampBeds,
			grassSurface,
			underwaterSurface
		));

		//dirt goes under the grass of course!
		MaterialRule underFloor = MaterialRules.ifTrue(
			MaterialRules.waterStartCheck(-6, -1),
			//check if we're above ground, so hollow hills dont have dirt floors
			MaterialRules.ifTrue(
				MaterialRules.yStartCheck(VerticalAnchor.absolute(-4), 1),
				MaterialRules.ifTrue(MaterialRules.getCondition(conditions, VanillaMaterialConditions.UNDER_FLOOR), DIRT)
			)
		);

		return MaterialRules.sequence(onFloor, underFloor);
	}

	private static MaterialCondition surfaceNoiseAbove(double minRange) {
		return MaterialRules.noiseCondition2d(Noises.SURFACE, minRange / 8.25D);
	}
}
