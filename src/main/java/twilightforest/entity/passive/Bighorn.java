package twilightforest.entity.passive;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.item.DyeColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;

import javax.annotation.Nullable;
import java.util.Random;

public class Bighorn extends Sheep {

	public Bighorn(EntityType<? extends Bighorn> type, Level world) {
		super(type, world);
	}

	public Bighorn(Level world, double x, double y, double z) {
		this(TFEntities.BIGHORN_SHEEP, world);
		this.setPos(x, y, z);
	}

	@Override
	public ResourceLocation getDefaultLootTable() {
		if (this.isSheared()) {
			return this.getType().getDefaultLootTable();
		} else {
			switch(this.getColor()) {
				case WHITE:
				default:
					return TFTreasure.BIGHORN_SHEEP_WHITE;
				case ORANGE:
					return TFTreasure.BIGHORN_SHEEP_ORANGE;
				case MAGENTA:
					return TFTreasure.BIGHORN_SHEEP_MAGENTA;
				case LIGHT_BLUE:
					return TFTreasure.BIGHORN_SHEEP_LIGHT_BLUE;
				case YELLOW:
					return TFTreasure.BIGHORN_SHEEP_YELLOW;
				case LIME:
					return TFTreasure.BIGHORN_SHEEP_LIME;
				case PINK:
					return TFTreasure.BIGHORN_SHEEP_PINK;
				case GRAY:
					return TFTreasure.BIGHORN_SHEEP_GRAY;
				case LIGHT_GRAY:
					return TFTreasure.BIGHORN_SHEEP_LIGHT_GRAY;
				case CYAN:
					return TFTreasure.BIGHORN_SHEEP_CYAN;
				case PURPLE:
					return TFTreasure.BIGHORN_SHEEP_PURPLE;
				case BLUE:
					return TFTreasure.BIGHORN_SHEEP_BLUE;
				case BROWN:
					return TFTreasure.BIGHORN_SHEEP_BROWN;
				case GREEN:
					return TFTreasure.BIGHORN_SHEEP_GREEN;
				case RED:
					return TFTreasure.BIGHORN_SHEEP_RED;
				case BLACK:
					return TFTreasure.BIGHORN_SHEEP_BLACK;
			}
		}
	}

	private static DyeColor getRandomFleeceColor(Random random) {
		return random.nextBoolean()
				? DyeColor.BROWN
				: DyeColor.byId(random.nextInt(16));
	}

	@Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
        livingdata = super.finalizeSpawn(worldIn, difficulty, reason, livingdata, dataTag);
        this.setColor(getRandomFleeceColor(this.level.random));
        return livingdata;
    }

    @Override
	public Sheep getBreedOffspring(ServerLevel world, AgeableMob ageable) {
		if (!(ageable instanceof Bighorn)) {
			TwilightForestMod.LOGGER.error("Code was called to breed a Bighorn with a non Bighorn! Cancelling!");
			return null;
		}

		Bighorn otherParent = (Bighorn) ageable;
		Bighorn babySheep = TFEntities.BIGHORN_SHEEP.create(world);
		babySheep.setColor(getOffspringColor(this, otherParent));
		return babySheep;
	}
    
    @Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.BIGHORN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.BIGHORN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.BIGHORN_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(TFSounds.BIGHORN_STEP, 0.15F, 1.0F);
	}
}
