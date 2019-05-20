package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import twilightforest.TwilightForestMod;
import twilightforest.util.TFEntityNames;
import twilightforest.util.VanillaEntityNames;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemTFTransformPowder extends ItemTF {

	private final Map<ResourceLocation, ResourceLocation> transformMap = new HashMap<>();

	protected ItemTFTransformPowder() {
		this.maxStackSize = 64;
		this.setCreativeTab(TFItems.creativeTab);

		addTwoWayTransformation(TFEntityNames.MINOTAUR,       VanillaEntityNames.ZOMBIE_PIGMAN);
		addTwoWayTransformation(TFEntityNames.DEER,           VanillaEntityNames.COW);
		addTwoWayTransformation(TFEntityNames.BIGHORN_SHEEP,  VanillaEntityNames.SHEEP);
		addTwoWayTransformation(TFEntityNames.WILD_BOAR,      VanillaEntityNames.PIG);
		addTwoWayTransformation(TFEntityNames.BUNNY,          VanillaEntityNames.RABBIT);
		addTwoWayTransformation(TFEntityNames.TINY_BIRD,      VanillaEntityNames.PARROT);
		addTwoWayTransformation(TFEntityNames.RAVEN,          VanillaEntityNames.BAT);
		addTwoWayTransformation(TFEntityNames.HOSTILE_WOLF,   VanillaEntityNames.WOLF);
		addTwoWayTransformation(TFEntityNames.PENGUIN,        VanillaEntityNames.CHICKEN);
		addTwoWayTransformation(TFEntityNames.HEDGE_SPIDER,   VanillaEntityNames.SPIDER);
		addTwoWayTransformation(TFEntityNames.SWARM_SPIDER,   VanillaEntityNames.CAVE_SPIDER);
		addTwoWayTransformation(TFEntityNames.WRAITH,         VanillaEntityNames.BLAZE);
		addTwoWayTransformation(TFEntityNames.REDCAP,         VanillaEntityNames.VILLAGER);
		addTwoWayTransformation(TFEntityNames.SKELETON_DRUID, VanillaEntityNames.WITCH);
	}

	private void addTwoWayTransformation(ResourceLocation from, ResourceLocation to) {
		transformMap.put(from, to);
		transformMap.put(to, from);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {

		if (target.isDead) return false;

		ResourceLocation location = transformMap.get(EntityList.getKey(target));
		if (location == null) return false;

		if (target.world.isRemote) {
			return EntityList.isRegistered(location);
		}

		Entity newEntity = EntityList.createEntityByIDFromName(location, target.world);
		if (newEntity == null) return false;

		newEntity.setLocationAndAngles(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
		if (newEntity instanceof EntityLiving) {
			((EntityLiving) newEntity).onInitialSpawn(target.world.getDifficultyForLocation(new BlockPos(target)), null);
		}

		try { // try copying what can be copied
			UUID uuid = newEntity.getUniqueID();
			newEntity.readFromNBT(target.writeToNBT(newEntity.writeToNBT(new NBTTagCompound())));
			newEntity.setUniqueId(uuid);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data: {}", e);
		}

		target.world.spawnEntity(newEntity);
		target.setDead();
		stack.shrink(1);

		if (target instanceof EntityLiving) {
			((EntityLiving) target).spawnExplosionParticle();
			((EntityLiving) target).spawnExplosionParticle();
		}
		target.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + itemRand.nextFloat(), itemRand.nextFloat() * 0.7F + 0.3F);

		return true;
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
		Vec3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

		return new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}
}
