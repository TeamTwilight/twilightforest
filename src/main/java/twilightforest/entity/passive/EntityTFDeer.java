package twilightforest.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
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
public class EntityTFDeer extends EntityCow {

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
	protected void initEntityAI() {
		super.initEntityAI();
		tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 16.0F, 1.5D, 1.8D));
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.7F;
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
	protected void playStepSound(BlockPos pos, Block block) {
	}

	@Override
	public boolean processInteract(EntityPlayer entityplayer, EnumHand hand) {
		ItemStack itemstack = entityplayer.getHeldItem(hand);
		if (itemstack.getItem() == Items.BUCKET) {
			// specifically do not respond to this
			return false;
		} else {
			return super.processInteract(entityplayer, hand);
		}
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public EntityCow createChild(EntityAgeable entityanimal) {
		return new EntityTFDeer(world);
	}
}
