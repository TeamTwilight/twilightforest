package twilightforest.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityTFBighorn extends SheepEntity {

	public EntityTFBighorn(EntityType<? extends EntityTFBighorn> type, World world) {
		super(type, world);
	}

	public EntityTFBighorn(World world, double x, double y, double z) {
		this(TFEntities.bighorn_sheep, world);
		this.setPosition(x, y, z);
	}

	@Override
	public ResourceLocation getLootTable() {
		if (this.getSheared()) {
			return this.getType().getLootTable();
		} else {
			switch(this.getFleeceColor()) {
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
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT dataTag) {
        livingdata = super.onInitialSpawn(worldIn, difficulty, reason, livingdata, dataTag);
        this.setFleeceColor(getRandomFleeceColor(this.world.rand));
        return livingdata;
    }

    @Override
	public SheepEntity func_241840_a(ServerWorld world, AgeableEntity ageable) {
		if (!(ageable instanceof EntityTFBighorn)) {
			TwilightForestMod.LOGGER.error("Code was called to breed a Bighorn with a non Bighorn! Cancelling!");
			return null;
		}

		EntityTFBighorn otherParent = (EntityTFBighorn) ageable;
		EntityTFBighorn babySheep = TFEntities.bighorn_sheep.create(world);
		babySheep.setFleeceColor(getDyeColorMixFromParents(this, otherParent));
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
