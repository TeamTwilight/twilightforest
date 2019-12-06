package twilightforest.entity.passive;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

/**
 * Deer are like quiet, non-milkable cows!
 * <p>
 * Also they look like deer
 *
 * @author Ben
 */
public class EntityTFDeer extends CowEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/deer");

	public EntityTFDeer(World world) {
		super(world);
		setSize(0.7F, 2.3F);
	}

	public EntityTFDeer(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PlayerEntity.class, 16.0F, 1.5D, 1.8D));
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.7F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.DEER_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.DEER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.DEER_DEATH;
	}

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
	public boolean processInteract(PlayerEntity entityplayer, Hand hand) {
		ItemStack itemstack = entityplayer.getHeldItem(hand);
		if (itemstack.getItem() == Items.BUCKET) {
			// specifically do not respond to this
			return false;
		} else {
			return super.processInteract(entityplayer, hand);
		}
	}

	//TODO: Move to loot table
	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public CowEntity createChild(AgeableEntity entityanimal) {
		return new EntityTFDeer(world);
	}
}
