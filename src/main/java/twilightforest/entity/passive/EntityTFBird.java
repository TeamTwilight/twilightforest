package twilightforest.entity.passive;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

import javax.annotation.Nonnull;

public abstract class EntityTFBird extends EntityAnimal {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/bird");

	// same items as EntityChicken / EntityParrot
	protected static final Ingredient SEEDS = Ingredient.fromItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);

	public float flapLength = 0.0F;
	public float flapIntensity = 0.0F;
	public float lastFlapIntensity;
	public float lastFlapLength;
	public float flapSpeed = 1.0F;

	public EntityTFBird(World world) {
		super(world);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.lastFlapLength = this.flapLength;
		this.lastFlapIntensity = this.flapIntensity;
		this.flapIntensity = (float) (this.flapIntensity + (this.onGround ? -1 : 4) * 0.3D);

		if (this.flapIntensity < 0.0F) {
			this.flapIntensity = 0.0F;
		}

		if (this.flapIntensity > 1.0F) {
			this.flapIntensity = 1.0F;
		}

		if (!this.onGround && this.flapSpeed < 1.0F) {
			this.flapSpeed = 1.0F;
		}

		this.flapSpeed = (float) (this.flapSpeed * 0.9D);

		// don't fall as fast
		if (!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		this.flapLength += this.flapSpeed * 2.0F;

//	    // rise up when we go fast?
//        if (this.getMoveHelper().getSpeed() > 0.39F && this.moveForward > 0.1F)
//        {
//        	this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
//        	this.moveForward *= 2F;
//        }
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
	}

	@Override
	public void fall(float dist, float damageMultiplier) {
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public EntityAnimal createChild(EntityAgeable entityanimal) {
		return null;
	}

	/**
	 * Overridden by flying birds
	 */
	public boolean isBirdLanded() {
		return true;
	}

}