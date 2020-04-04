package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

public class EntityTFMazeSlime extends SlimeEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/maze_slime");
	private static final AttributeModifier DOUBLE_HEALTH = new AttributeModifier("Maze slime double health", 1, AttributeModifier.Operation.MULTIPLY_BASE).setSaved(false);

	public EntityTFMazeSlime(EntityType<? extends EntityTFMazeSlime> type, World world) {
		super(type, world);
	}

	@Override
	protected SlimeEntity createInstance() {
		return new EntityTFMazeSlime(this.world);
	}

	@Override
	public void setSlimeSize(int size, boolean resetHealth) {
		super.setSlimeSize(size, resetHealth);
		this.experienceValue += 3;
	}

	//TODO: This goes into a factory
	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != Difficulty.PEACEFUL && this.world.checkNoEntityCollision(getBoundingBox())
				&& this.world.getCollisionBoxes(this, getBoundingBox()).isEmpty()
				&& !this.world.containsAnyLiquid(getBoundingBox()) && this.isValidLightLevel();
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(DOUBLE_HEALTH);
	}

	@Override
	protected boolean canDamagePlayer() {
		return true;
	}

	@Override
	protected int getAttackStrength() {
		return super.getAttackStrength() + 3;
	}

	@Override
	protected boolean spawnCustomParticles() {
		// [VanillaCopy] from super tick with own particles
		int i = getSlimeSize();
		for (int j = 0; j < i * 8; ++j) {
			float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
			float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
			float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
			float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
			World world = this.world;
			// ParticleTypes ParticleTypes = this.getParticleType();
			double d0 = this.getX() + (double) f2;
			double d1 = this.getZ() + (double) f3;
			BlockState state = TFBlocks.maze_stone_brick.get().getDefaultState();
			world.addParticle(ParticleTypes.BLOCK_CRACK, d0, this.getBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, Block.getStateId(state));
		}
		return true;
	}

	// [VanillaCopy] exact copy from EntityMob.isValidLightLevel
	private boolean isValidLightLevel() {
		BlockPos blockpos = new BlockPos(this.getX(), this.getBoundingBox().minY, this.getZ());

		if (this.world.getLightLevel(LightType.SKY, blockpos) > this.rand.nextInt(32)) {
			return false;
		} else {
			int i = this.world.getLightFromNeighbors(blockpos);

			if (this.world.isThundering()) {
				int j = this.world.getSkylightSubtracted();
				this.world.setSkylightSubtracted(10);
				i = this.world.getLightFromNeighbors(blockpos);
				this.world.setSkylightSubtracted(j);
			}

			return i <= this.rand.nextInt(8);
		}
	}

	@Override
	protected float getSoundVolume() {
		// OH MY GOD, SHUT UP
		return 0.1F * this.getSlimeSize();
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

}
