package twilightforest.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;

import javax.annotation.Nullable;
import java.util.Random;


public class EntityTFBighorn extends EntitySheep {

	public EntityTFBighorn(World world) {
		super(world);
		setSize(0.9F, 1.3F);
	}

	public EntityTFBighorn(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	private static EnumDyeColor getRandomFleeceColor(Random random) {
		if (random.nextBoolean()) {
			return EnumDyeColor.BROWN;
		} else {
			return EnumDyeColor.byMetadata(random.nextInt(15));
		}
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setFleeceColor(getRandomFleeceColor(this.world.rand));
		return livingdata;
	}

	@Override
	public EntitySheep createChild(EntityAgeable entityanimal) {
		EntityTFBighorn otherParent = (EntityTFBighorn) entityanimal;
		EntityTFBighorn babySheep = new EntityTFBighorn(world);
		if (rand.nextBoolean()) {
			babySheep.setFleeceColor(getFleeceColor());
		} else {
			babySheep.setFleeceColor(otherParent.getFleeceColor());
		}
		return babySheep;
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer) {
			((EntityPlayer) par1DamageSource.getSourceOfDamage()).addStat(TFAchievementPage.twilightHunter);
		}
	}

}
