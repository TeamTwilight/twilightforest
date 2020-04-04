package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

public class EntityTFIceExploder extends EntityTFIceMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/ice_exploder");
	private static final float EXPLOSION_RADIUS = 1;

	public EntityTFIceExploder(EntityType<? extends EntityTFIceExploder> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_DEATH;
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.6F;
	}

	@Override
	protected void onDeathUpdate() {
		++this.deathTime;

		if (this.deathTime == 60) // delay until 3 seconds
		{
			if (!world.isRemote) {
				boolean mobGriefing = ForgeEventFactory.getMobGriefingEvent(world, this);
				this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), EntityTFIceExploder.EXPLOSION_RADIUS, mobGriefing ? Explosion.Mode.BREAK : Explosion.Mode.DESTROY);

				if (mobGriefing) {
					this.transformBlocks();
				}
			}
			// Fake to trigger super's behaviour
			deathTime = 19;
			super.onDeathUpdate();
			deathTime = 60;
		}
	}

	private void transformBlocks() {
		int range = 4;

		BlockPos pos = new BlockPos(this);

		for (int dx = -range; dx <= range; dx++) {
			for (int dy = -range; dy <= range; dy++) {
				for (int dz = -range; dz <= range; dz++) {
					double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

					float randRange = range + (rand.nextFloat() - rand.nextFloat()) * 2F;

					if (distance < randRange) {
						this.transformBlock(pos.add(dx, dy, dz));
					}
				}
			}
		}
	}

	private void transformBlock(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		// check if we should even explode this
		// TODO: This requires an actual explosion instead of NULL
		if (block.getExplosionResistance(state, world, pos, this, null) < 8F && state.getBlockHardness(world, pos) >= 0) {
			// todo improve for blocks where state is known? or perhaps if a propertycolor is present
			int blockColor = state.getMaterialColor(world, pos).colorValue;

			// do appropriate transformation
			//TODO: 1.13 squished all this. Find out best method of approach
			// TODO: Best hting to do is to create a map of the result from getClosestDyeColor with stained glass and terracotta
			// TODO: and then just replace against those
			if (this.shouldTransformGlass(state, pos)) {
				this.world.setBlockState(pos, Blocks.STAINED_GLASS.getDefaultState().with(BlockStainedGlass.COLOR, getClosestDyeColor(blockColor)));
			} else if (this.shouldTransformClay(state, pos)) {
				this.world.setBlockState(pos, Blocks.STAINED_HARDENED_CLAY.getDefaultState().with(BlockColored.COLOR, getClosestDyeColor(blockColor)));
			}
		}
	}

	private boolean shouldTransformClay(BlockState state, BlockPos pos) {
		return state.getBlock().isNormalCube(state, this.world, pos);
	}

	private boolean shouldTransformGlass(BlockState state, BlockPos pos) {
		return state.getBlock() != Blocks.AIR && this.isBlockNormalBounds(state, pos) && (!state.getMaterial().isOpaque() || state.getMaterial() == Material.LEAVES || state.getBlock() == Blocks.ICE || state.getBlock() == TFBlocks.aurora_block.get());
	}

	private boolean isBlockNormalBounds(BlockState state, BlockPos pos) {
		return VoxelShapes.fullCube().equals(state.getShape(world, pos));
	}

	private DyeColor getClosestDyeColor(int blockColor) {
		int red = (blockColor >> 16) & 255;
		int green = (blockColor >> 8) & 255;
		int blue = blockColor & 255;


		DyeColor bestColor = DyeColor.WHITE;
		int bestDifference = 1024;

		for (DyeColor color : DyeColor.values()) {
			float[] iColor = color.getColorComponentValues();

			int iRed = (int) (iColor[0] * 255F);
			int iGreen = (int) (iColor[1] * 255F);
			int iBlue = (int) (iColor[2] * 255F);

			int difference = Math.abs(red - iRed) + Math.abs(green - iGreen) + Math.abs(blue - iBlue);

			if (difference < bestDifference) {
				bestColor = color;
				bestDifference = difference;
			}
		}

		return bestColor;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}
}
