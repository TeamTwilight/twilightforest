package twilightforest.client.event;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import twilightforest.TwilightForestMod;
import twilightforest.block.ClimbableHollowLogBlock;
import twilightforest.client.properties.PotionFlaskTintSource;
import twilightforest.client.properties.SpawnEggTintSource;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ColorHandler {
	public static final Int2IntFunction CANOPY_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0x469A66) / 2);
	public static final Int2IntFunction MANGROVE_COLORIZER = color -> 0xFF000000 | (((color & 0xFEFEFE) + 0xC0E694) / 2);

	protected static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
		BlockColors blockColors = event.getBlockColors();

		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return auroraTint(null, null);
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return auroraTint(level, pos);
			}
		}), TFBlocks.AURORA_BLOCK.get());
		event.register(List.of(state -> {
			//TODO: Verify this. It's kind of how you get the color
			int normalColor = blockColors.getTintSource(TFBlocks.AURORA_BLOCK.get().defaultBlockState(), 0).color(TFBlocks.AURORA_BLOCK.get().defaultBlockState());

			int red = (normalColor >> 16) & 255;
			int blue = normalColor & 255;
			int green = (normalColor >> 8) & 255;

			float[] hsb = ColorUtil.rgbToHSV(red, green, blue);

			return 0xFF000000 | ColorUtil.hsvToRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}), TFBlocks.AURORA_PILLAR.get(), TFBlocks.AURORA_SLAB.get(), TFBlocks.AURORALIZED_GLASS.get());
		event.register(List.of(BlockTintSources.grass()), TFBlocks.SMOKER.get(), TFBlocks.FIRE_JET.get()); //TODO: This got the block tint from Grass, but this is about the same?
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 2129968;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return 0xFF000000 | 7455580;
			}
		}), TFBlocks.HUGE_LILY_PAD.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 106 << 16 | 156 << 8 | 23;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				int red, green, blue;

				int fade = pos.getX() * 16 + pos.getY() * 16 + pos.getZ() * 16;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 106 + fall * 251);
				green = (int) (spring * 156 + fall * 108);
				blue = (int) (spring * 23 + fall * 27);

				return 0xFF000000 | red << 16 | green << 8 | blue;
			}
		}), TFBlocks.TIME_LEAVES.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 108 << 16 | 204 << 8 | 234;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				int red, green, blue;

				int fade = pos.getX() * 27 + pos.getY() * 63 + pos.getZ() * 39;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 108 + fall * 96);
				green = (int) (spring * 204 + fall * 107);
				blue = (int) (spring * 234 + fall * 121);

				return 0xFF000000 | red << 16 | green << 8 | blue;
			}
		}), TFBlocks.TRANSFORMATION_LEAVES.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 252 << 16 | 241 << 8 | 68;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				int red, green, blue;

				int fade = pos.getX() * 31 + pos.getY() * 33 + pos.getZ() * 32;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 252 + fall * 237);
				green = (int) (spring * 241 + fall * 172);
				blue = (int) (spring * 68 + fall * 9);

				return 0xFF000000 | red << 16 | green << 8 | blue;
			}
		}), TFBlocks.MINING_LEAVES.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return 0xFF000000 | 54 << 16 | 76 << 8 | 3;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				int red, green, blue;

				int fade = pos.getX() * 63 + pos.getY() * 63 + pos.getZ() * 63;
				if ((fade & 256) != 0) {
					fade = 255 - (fade & 255);
				}
				fade &= 255;

				float spring = (255 - fade) / 255F;
				float fall = fade / 255F;

				red = (int) (spring * 54 + fall * 168);
				green = (int) (spring * 76 + fall * 199);
				blue = (int) (spring * 3 + fall * 43);

				return 0xFF000000 | red << 16 | green << 8 | blue;
			}
		}), TFBlocks.SORTING_LEAVES.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return -1;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				float f = SimplexNoiseHelper.rippleFractalNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
				return 0xFF000000 | ColorUtil.hsvToRGB(0.1f, 1f - f, (f + 2f) / 3f);
			}
		}), TFBlocks.TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.INFESTED_TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return FoliageColor.FOLIAGE_DEFAULT;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter getter, BlockPos pos) {
				return BiomeColors.getAverageFoliageColor(getter, pos);
			}
		}), TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.HARDENED_DARK_LEAVES.get(), TFBlocks.GIANT_LEAVES.get(), TFBlocks.FALLEN_LEAVES.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return FoliageColor.FOLIAGE_EVERGREEN;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter getter, BlockPos pos) {
				return CANOPY_COLORIZER.applyAsInt(BiomeColors.getAverageFoliageColor(getter, pos));
			}
		}), TFBlocks.CANOPY_LEAVES.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return FoliageColor.FOLIAGE_BIRCH;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter getter, BlockPos pos) {
				return MANGROVE_COLORIZER.applyAsInt(BiomeColors.getAverageFoliageColor(getter, pos));
			}
		}), TFBlocks.MANGROVE_LEAVES.get());
		event.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return FoliageColor.FOLIAGE_DEFAULT;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				// RAINBOW!
				int red = pos.getX() * 32 + pos.getY() * 16;
				if ((red & 256) != 0) {
					red = 255 - (red & 255);
				}
				red &= 255;

				int green = pos.getY() * 32 + pos.getZ() * 16;
				if ((green & 256) != 0) {
					green = 255 - (green & 255);
				}
				green ^= 255;

				int blue = pos.getX() * 16 + pos.getZ() * 32;
				if ((blue & 256) != 0) {
					blue = 255 - (blue & 255);
				}
				blue &= 255;

				return 0xFF000000 | red << 16 | green << 8 | blue;

			}
		}), TFBlocks.RAINBOW_OAK_LEAVES.get());
		event.register(List.of(BlockTintSources.constant(FoliageColor.FOLIAGE_EVERGREEN)), TFBlocks.BEANSTALK_LEAVES.get(), TFBlocks.THORN_LEAVES.get());
		event.register(List.of(BlockTintSources.grass()), TFBlocks.FIDDLEHEAD.get(), TFBlocks.POTTED_FIDDLEHEAD.get());
		event.register(List.of(BlockTintSources.grass()), TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(), //TODO: For datagen: apply to correct layer
			TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(),
			TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get());
		event.register(List.of(new BlockTintSource() {
			//TODO: For datagen: apply to correct layer
			@Override
			public int color(BlockState state) {
				return FoliageColor.FOLIAGE_DEFAULT;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				if (state.getValue(ClimbableHollowLogBlock.VARIANT) == HollowLogVariants.Climbable.VINE) {
					return BiomeColors.getAverageFoliageColor(level, pos);
				}
				return BlockTintSource.super.colorInWorld(state, level, pos);
			}
		}), TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(),
			TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(),
			TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get());
		event.register(List.of(BlockTintSources.foliage()), TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get()); //TODO: For datagen: apply to correct layer
		event.register(List.of(BlockTintSources.constant(0xFFFF00FF)), TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), TFBlocks.PINK_CASTLE_DOOR.get());
		event.register(List.of(BlockTintSources.constant(0xFF00FFFF)), TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.BLUE_CASTLE_DOOR.get());
		event.register(List.of(BlockTintSources.constant(0xFFFFFF00)), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_DOOR.get());
		event.register(List.of(BlockTintSources.constant(0xFF4B0082)), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_DOOR.get());
		event.register(List.of(BlockTintSources.constant(0xFF5C1074)), TFBlocks.VIOLET_FORCE_FIELD.get());
		event.register(List.of(BlockTintSources.constant(0xFFFA057E)), TFBlocks.PINK_FORCE_FIELD.get());
		event.register(List.of(BlockTintSources.constant(0xFFFF5B02)), TFBlocks.ORANGE_FORCE_FIELD.get());
		event.register(List.of(BlockTintSources.constant(0xFF89E701)), TFBlocks.GREEN_FORCE_FIELD.get());
		event.register(List.of(BlockTintSources.constant(0xFF0DDEFF)), TFBlocks.BLUE_FORCE_FIELD.get());
	}

	private static int auroraTint(@Nullable BlockAndTintGetter getter, @Nullable BlockPos pos) {
		return 0xFF000000 | ColorUtil.hsvToRGB(getter == null ? 0.45F : SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos != null ? pos.above(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
	}

	protected static void registerItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
		event.register(TwilightForestMod.prefix("potion_flask"), PotionFlaskTintSource.TYPE);
		event.register(TwilightForestMod.prefix("spawn_egg_primary"), SpawnEggTintSource.Primary.MAP_CODEC);
		event.register(TwilightForestMod.prefix("spawn_egg_secondary"), SpawnEggTintSource.Secondary.MAP_CODEC);
	}
}
