package twilightforest.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFHedgeSpider;
import twilightforest.entity.EntityTFHostileWolf;
import twilightforest.entity.EntityTFMinotaur;
import twilightforest.entity.EntityTFRedcap;
import twilightforest.entity.EntityTFSkeletonDruid;
import twilightforest.entity.EntityTFSwarmSpider;
import twilightforest.entity.EntityTFWraith;
import twilightforest.entity.passive.EntityTFBighorn;
import twilightforest.entity.passive.EntityTFBoar;
import twilightforest.entity.passive.EntityTFDeer;
import twilightforest.entity.passive.EntityTFPenguin;
import twilightforest.entity.passive.EntityTFRaven;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ItemTFTransformPowder extends ItemTF {
	private final Map<Class<? extends EntityLivingBase>, Class<? extends EntityLivingBase>> transformMap = new HashMap<>();

	protected ItemTFTransformPowder() {
		this.maxStackSize = 64;
		this.setCreativeTab(TFItems.creativeTab);

		addTwoWayTransformation(EntityTFMinotaur.class, EntityPigZombie.class);
		addTwoWayTransformation(EntityTFDeer.class, EntityCow.class);
		addTwoWayTransformation(EntityTFBighorn.class, EntitySheep.class);
		addTwoWayTransformation(EntityTFBoar.class, EntityPig.class);
		addTwoWayTransformation(EntityTFRaven.class, EntityBat.class);
		addTwoWayTransformation(EntityTFHostileWolf.class, EntityWolf.class);
		addTwoWayTransformation(EntityTFPenguin.class, EntityChicken.class);
		addTwoWayTransformation(EntityTFHedgeSpider.class, EntitySpider.class);
		addTwoWayTransformation(EntityTFSwarmSpider.class, EntityCaveSpider.class);
		addTwoWayTransformation(EntityTFWraith.class, EntityBlaze.class);
		addTwoWayTransformation(EntityTFRedcap.class, EntityVillager.class);
		addTwoWayTransformation(EntityTFSkeletonDruid.class, EntityWitch.class);
	}

	private void addTwoWayTransformation(Class<? extends EntityLivingBase> class1, Class<? extends EntityLivingBase> class2) {
		transformMap.put(class1, class2);
		transformMap.put(class2, class1);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		Class<?> transformClass = transformMap.get(target.getClass());

		if (transformClass != null) {
			if (target.world.isRemote) {
				if (target instanceof EntityLiving) {
					((EntityLiving) target).spawnExplosionParticle();
					((EntityLiving) target).spawnExplosionParticle();
				}

				target.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + itemRand.nextFloat(), itemRand.nextFloat() * 0.7F + 0.3F);
			} else {
				EntityLivingBase newMonster = null;
				try {
					newMonster = (EntityLivingBase) transformClass.getConstructor(World.class).newInstance(target.world);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (newMonster == null) {
					return false;
				} else {
					newMonster.setPositionAndRotation(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
					target.world.spawnEntity(newMonster);
					target.setDead();
					stack.shrink(1);
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		if (world.isRemote) {
			AxisAlignedBB fanBox = getEffectAABB(player);

			// particle effect
			for (int i = 0; i < 30; i++) {
				world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, fanBox.minX + world.rand.nextFloat() * (fanBox.maxX - fanBox.minX),
						fanBox.minY + world.rand.nextFloat() * (fanBox.maxY - fanBox.minY),
						fanBox.minZ + world.rand.nextFloat() * (fanBox.maxZ - fanBox.minZ),
						0, 0, 0);
			}

		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	private AxisAlignedBB getEffectAABB(EntityPlayer player) {
		double range = 2.0D;
		double radius = 1.0D;
		Vec3d srcVec = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3d lookVec = player.getLookVec();
		Vec3d destVec = srcVec.addVector(lookVec.xCoord * range, lookVec.yCoord * range, lookVec.zCoord * range);

		return new AxisAlignedBB(destVec.xCoord - radius, destVec.yCoord - radius, destVec.zCoord - radius, destVec.xCoord + radius, destVec.yCoord + radius, destVec.zCoord + radius);
	}

}