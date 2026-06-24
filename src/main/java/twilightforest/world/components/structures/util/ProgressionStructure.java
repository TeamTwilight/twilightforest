package twilightforest.world.components.structures.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

// Landmark structure with a progression lock; Lich Tower/Labyrinth/Hydra Lair/Final Castle/etc
public abstract class ProgressionStructure extends ConquerableStructure implements AdvancementLockedStructure, StructureHints {
	protected static <S extends ProgressionStructure> Products.P6<RecordCodecBuilder.Mu<S>, AdvancementLockConfig, Optional<HintConfig>, Optional<DecorationConfig>, Boolean, Optional<Holder<MapDecorationType>>, StructureSettings> progressionCodec(RecordCodecBuilder.Instance<S> instance) {
		return instance.group(
			AdvancementLockConfig.CODEC.fieldOf(AdvancementLockedStructure.CODEC_NAME).forGetter(s -> s.advancementLockConfig),
			HintConfig.CODEC.optionalFieldOf(StructureHints.CODEC_NAME).forGetter(s -> s.hintConfig)
		).and(landmarkCodec(instance));
	}

	protected static <S extends ProgressionStructure> Products.P5<RecordCodecBuilder.Mu<S>, AdvancementLockConfig, Optional<HintConfig>, Optional<DecorationConfig>, Boolean, Optional<Holder<MapDecorationType>>> progressionCodecNoSettings(RecordCodecBuilder.Instance<S> instance) {
		return instance.group(
			AdvancementLockConfig.CODEC.fieldOf(AdvancementLockedStructure.CODEC_NAME).forGetter(s -> s.advancementLockConfig),
			HintConfig.CODEC.optionalFieldOf(StructureHints.CODEC_NAME).forGetter(s -> s.hintConfig)
		).and(landmarkCodecNoSettings(instance));
	}

	protected final AdvancementLockConfig advancementLockConfig;
	protected final Optional<HintConfig> hintConfig;

	public ProgressionStructure(AdvancementLockConfig advancementLockConfig, Optional<HintConfig> hintConfig, Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(decorationConfig, centerInChunk, structureIcon, structureSettings);

		this.advancementLockConfig = advancementLockConfig;
		this.hintConfig = hintConfig;
	}

	@Override
	public List<Identifier> getRequiredAdvancements() {
		return this.advancementLockConfig.requiredAdvancements();
	}

	/**
	 * Try several times to spawn a hint monster
	 */
	private long lastSpawnedHintMonsterTime;

	@Override
	public void trySpawnHintMonster(Level world, Player player, BlockPos pos) {
		// check if the timer is valid
		long currentTime = world.getGameTime();

		// if someone set the time backwards, fix the spawn timer
		if (currentTime < this.lastSpawnedHintMonsterTime) {
			this.lastSpawnedHintMonsterTime = 0;
		}

		if (currentTime - this.lastSpawnedHintMonsterTime > 1200) {
			// okay, time is good, try several times to spawn one
			for (int i = 0; i < 20; i++) {
				if (didSpawnHintMonster(world, player, pos)) {
					this.lastSpawnedHintMonsterTime = currentTime;
					break;
				}
			}
		}
	}

	@Override
	public ItemStack createHintBook(RegistryAccess registryAccess) {
		return this.hintConfig.map(config -> config.hintItem().copy()).orElse(ItemStack.EMPTY);
	}

	@Override
	@Nullable
	public Mob createHintMonster(Level world) {
		return this.hintConfig.map(config -> config.hintMob().create(world, EntitySpawnReason.STRUCTURE)).orElse(null);
	}
}
