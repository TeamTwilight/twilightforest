package twilightforest.data.custom.stalactites;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.stalactites.entry.HillConfig;
import twilightforest.world.components.feature.BlockSpikeFeature;

import java.util.List;

public class StalactiteGenerator extends StalactiteProvider {
	public StalactiteGenerator(PackOutput output) {
		super(output, TwilightForestMod.ID);
	}

	@Override
	protected void createStalactites() {
		this.buildConfig(new HillBuilder(HillConfig.HillType.SMALL_HOLLOW_HILL, 1, 1, 0.6F)
				.addBaseStalactite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addStalagmite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addOreStalactite(this.makeStalactiteName("iron_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.IRON_ORE, 30), Pair.of(Blocks.RAW_IRON_BLOCK, 1)), 0.7F, 8, 40))
				.addOreStalactite(this.makeStalactiteName("coal_stalactite"), this.buildStalactite(Blocks.COAL_ORE, 0.8F, 12, 30))
				.addOreStalactite(this.makeStalactiteName("copper_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.COPPER_ORE, 30), Pair.of(Blocks.RAW_COPPER_BLOCK, 1)), 0.6F, 12, 25))
				.addOreStalactite(this.makeStalactiteName("glowstone_stalactite"), this.buildStalactite(Blocks.GLOWSTONE, 0.5F, 8, 20)));

		this.buildConfig(new HillBuilder(HillConfig.HillType.MEDIUM_HOLLOW_HILL, 1, 1, 0.75F)
				.addBaseStalactite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addStalagmite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addOreStalactite(this.makeStalactiteName("iron_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.IRON_ORE, 30), Pair.of(Blocks.RAW_IRON_BLOCK, 1)), 0.7F, 8, 40))
				.addOreStalactite(this.makeStalactiteName("coal_stalactite"), this.buildStalactite(Blocks.COAL_ORE, 0.8F, 12, 30))
				.addOreStalactite(this.makeStalactiteName("copper_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.COPPER_ORE, 30), Pair.of(Blocks.RAW_COPPER_BLOCK, 1)), 0.6F, 12, 25))
				.addOreStalactite(this.makeStalactiteName("glowstone_stalactite"), this.buildStalactite(Blocks.GLOWSTONE, 0.5F, 8, 20))
				.addOreStalactite(this.makeStalactiteName("gold_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.GOLD_ORE, 30), Pair.of(Blocks.RAW_GOLD_BLOCK, 1)), 0.6F, 6, 10))
				.addOreStalactite(this.makeStalactiteName("redstone_stalactite"), this.buildStalactite(Blocks.REDSTONE_ORE, 0.8F, 8, 20)));

		this.buildConfig(new HillBuilder(HillConfig.HillType.LARGE_HOLLOW_HILL, 1, 1, 0.85F)
				.addBaseStalactite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addStalagmite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addOreStalactite(this.makeStalactiteName("iron_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.IRON_ORE, 30), Pair.of(Blocks.RAW_IRON_BLOCK, 1)), 0.7F, 8, 40))
				.addOreStalactite(this.makeStalactiteName("coal_stalactite"), this.buildStalactite(Blocks.COAL_ORE, 0.8F, 12, 30))
				.addOreStalactite(this.makeStalactiteName("copper_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.COPPER_ORE, 30), Pair.of(Blocks.RAW_COPPER_BLOCK, 1)), 0.6F, 12, 25))
				.addOreStalactite(this.makeStalactiteName("glowstone_stalactite"), this.buildStalactite(Blocks.GLOWSTONE, 0.5F, 8, 20))
				.addOreStalactite(this.makeStalactiteName("gold_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.GOLD_ORE, 30), Pair.of(Blocks.RAW_GOLD_BLOCK, 1)), 0.6F, 6, 10))
				.addOreStalactite(this.makeStalactiteName("redstone_stalactite"), this.buildStalactite(Blocks.REDSTONE_ORE, 0.8F, 8, 20))
				.addOreStalactite(this.makeStalactiteName("diamond_stalactite"), this.buildStalactite(Blocks.DIAMOND_ORE, 0.5F, 4, 8))
				.addOreStalactite(this.makeStalactiteName("lapis_stalactite"), this.buildStalactite(Blocks.LAPIS_ORE, 0.8F, 8, 12))
				.addOreStalactite(this.makeStalactiteName("emerald_stalactite"), this.buildStalactite(Blocks.EMERALD_ORE, 0.5F, 3, 8)));

		this.buildConfig(new HillBuilder(HillConfig.HillType.HYDRA_LAIR, 1, 1f/16f, 1.0F)
				.addStalagmite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addOreStalactite(this.makeStalactiteName("iron_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.IRON_ORE, 30), Pair.of(Blocks.RAW_IRON_BLOCK, 1)), 0.7F, 8, 40))
				.addOreStalactite(this.makeStalactiteName("coal_stalactite"), this.buildStalactite(Blocks.COAL_ORE, 0.8F, 12, 30))
				.addOreStalactite(this.makeStalactiteName("copper_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.COPPER_ORE, 30), Pair.of(Blocks.RAW_COPPER_BLOCK, 1)), 0.6F, 12, 25))
				.addOreStalactite(this.makeStalactiteName("glowstone_stalactite"), this.buildStalactite(Blocks.GLOWSTONE, 0.5F, 8, 20))
				.addOreStalactite(this.makeStalactiteName("gold_stalactite"), this.buildStalactite(List.of(Pair.of(Blocks.GOLD_ORE, 30), Pair.of(Blocks.RAW_GOLD_BLOCK, 1)), 0.6F, 6, 10))
				.addOreStalactite(this.makeStalactiteName("redstone_stalactite"), this.buildStalactite(Blocks.REDSTONE_ORE, 0.8F, 8, 20)));

		this.buildConfig(new HillBuilder(HillConfig.HillType.YETI_CAVE, 1, 0, 0.0F)
				.addBaseStalactite(this.makeStalactiteName("ice_stalactite"), this.buildStalactite(Blocks.ICE, 0.6F, 10, 1))
				.addBaseStalactite(this.makeStalactiteName("packed_ice_stalactite"), this.buildStalactite(Blocks.PACKED_ICE, 0.5F, 9, 1))
				.addBaseStalactite(this.makeStalactiteName("blue_ice_stalactite"), this.buildStalactite(Blocks.BLUE_ICE, 1.0F, 8, 1)));

		this.buildConfig(new HillBuilder(HillConfig.HillType.TROLL_CAVES, 1, 0.25f, 0.0F)
				.addBaseStalactite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE)
				.addStalagmite(this.makeStalactiteName("hill_stone_stalactite"), BlockSpikeFeature.STONE_STALACTITE));
	}

	@Override
	public String getName() {
		return "Twilight Forest Hollow Hill Stalactites";
	}
}
