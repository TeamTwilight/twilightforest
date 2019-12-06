package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.MeleeAttackGoal;
import net.minecraft.entity.ai.HurtByTargetGoal;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

public class EntityTFIceExploder extends EntityTFIceMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/ice_exploder");
	private static final float EXPLOSION_RADIUS = 1;

	public EntityTFIceExploder(World world) {
		super(world);
		this.setSize(0.8F, 1.8F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(1, new MeleeAttackGoal(this, 1.0D, false));
		this.tasks.addTask(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.tasks.addTask(3, new LookAtGoal(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new LookRandomlyGoal(this));
		this.targetTasks.addTask(1, new HurtByTargetGoal(this, true));
		this.targetTasks.addTask(2, new NearestAttackableTargetGoal<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
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
	public float getEyeHeight() {
		return this.height * 0.6F;
	}

	@Override
	protected void onDeathUpdate() {
		++this.deathTime;

		if (this.deathTime == 60) // delay until 3 seconds
		{
			if (!world.isRemote) {
				boolean mobGriefing = ForgeEventFactory.getMobGriefingEvent(world, this);
				this.world.createExplosion(this, this.posX, this.posY, this.posZ, EntityTFIceExploder.EXPLOSION_RADIUS, mobGriefing);

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
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		// check if we should even explode this
		if (block.getExplosionResistance(this) < 8F && state.getBlockHardness(world, pos) >= 0) {
			// todo improve for blocks where state is known? or perhaps if a propertycolor is present
			int blockColor = state.getMapColor(world, pos).colorValue;

			// do appropriate transformation
			if (this.shouldTransformGlass(state, pos)) {
				this.world.setBlockState(pos, Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR, getClosestDyeColor(blockColor)));
			} else if (this.shouldTransformClay(state, pos)) {
				this.world.setBlockState(pos, Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, getClosestDyeColor(blockColor)));
			}
		}
	}


	private boolean shouldTransformClay(IBlockState state, BlockPos pos) {
		return state.getBlock().isNormalCube(state, this.world, pos);
	}


	private boolean shouldTransformGlass(IBlockState state, BlockPos pos) {
		return state.getBlock() != Blocks.AIR && this.isBlockNormalBounds(state, pos) && (!state.getMaterial().isOpaque() || state.getBlock().isLeaves(state, this.world, pos) || state.getBlock() == Blocks.ICE || state.getBlock() == TFBlocks.aurora_block);
	}


	private boolean isBlockNormalBounds(IBlockState state, BlockPos pos) {
		return Block.FULL_BLOCK_AABB.equals(state.getBoundingBox(world, pos));
	}


	private EnumDyeColor getClosestDyeColor(int blockColor) {
		int red = (blockColor >> 16) & 255;
		int green = (blockColor >> 8) & 255;
		int blue = blockColor & 255;


		EnumDyeColor bestColor = EnumDyeColor.WHITE;
		int bestDifference = 1024;

		for (EnumDyeColor color : EnumDyeColor.values()) {
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
