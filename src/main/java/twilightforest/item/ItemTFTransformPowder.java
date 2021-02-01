package twilightforest.item;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemTFTransformPowder extends Item {

	private final Map<EntityType<?>, EntityType<?>> transformMap = new HashMap<>();

	protected ItemTFTransformPowder(Properties props) {
		super(props);
	}

	public void initTransformations() {
		addTwoWayTransformation(TFEntities.minotaur,       EntityType.ZOMBIFIED_PIGLIN);
		addTwoWayTransformation(TFEntities.deer,           EntityType.COW);
		addTwoWayTransformation(TFEntities.bighorn_sheep,  EntityType.SHEEP);
		addTwoWayTransformation(TFEntities.wild_boar,      EntityType.PIG);
		addTwoWayTransformation(TFEntities.bunny,          EntityType.RABBIT);
		addTwoWayTransformation(TFEntities.tiny_bird,      EntityType.PARROT);
		addTwoWayTransformation(TFEntities.raven,          EntityType.BAT);
		addTwoWayTransformation(TFEntities.hostile_wolf,   EntityType.WOLF);
		addTwoWayTransformation(TFEntities.penguin,        EntityType.CHICKEN);
		addTwoWayTransformation(TFEntities.hedge_spider,   EntityType.SPIDER);
		addTwoWayTransformation(TFEntities.swarm_spider,   EntityType.CAVE_SPIDER);
		addTwoWayTransformation(TFEntities.wraith,         EntityType.BLAZE);
		addTwoWayTransformation(TFEntities.redcap,         EntityType.VILLAGER);
		addTwoWayTransformation(TFEntities.skeleton_druid, EntityType.WITCH);
	}

	private void addTwoWayTransformation(EntityType<?> from, EntityType<?> to) {
		transformMap.put(from, to);
		transformMap.put(to, from);
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (!target.isAlive()) {
			return ActionResultType.PASS;
		}

		EntityType<?> type = transformMap.get(target.getType());
		if (type == null) {
			return ActionResultType.PASS;
		}

		Entity newEntity = type.create(player.world);
		if (newEntity == null) {
			return ActionResultType.PASS;
		}

		newEntity.setLocationAndAngles(target.getPosX(), target.getPosY(), target.getPosZ(), target.rotationYaw, target.rotationPitch);
		if (newEntity instanceof MobEntity && target.world instanceof IServerWorld) {
			IServerWorld world = (IServerWorld) target.world;
			((MobEntity) newEntity).onInitialSpawn(world, target.world.getDifficultyForLocation(target.getPosition()), SpawnReason.CONVERSION, null, null);
		}

		try { // try copying what can be copied
			UUID uuid = newEntity.getUniqueID();
			newEntity.read(target.writeWithoutTypeId(newEntity.writeWithoutTypeId(new CompoundNBT())));
			newEntity.setUniqueId(uuid);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
		}

		target.world.addEntity(newEntity);
		target.remove();
		stack.shrink(1);

		if (target instanceof MobEntity) {
			((MobEntity) target).spawnExplosionParticle();
			((MobEntity) target).spawnExplosionParticle();
		}
		target.playSound(TFSounds.POWDER_USE, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);

		return ActionResultType.SUCCESS;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (world.isRemote) {
			AxisAlignedBB area = getEffectAABB(player);

			// particle effect
			for (int i = 0; i < 30; i++) {
				world.addParticle(ParticleTypes.CRIT, area.minX + world.rand.nextFloat() * (area.maxX - area.minX),
						area.minY + world.rand.nextFloat() * (area.maxY - area.minY),
						area.minZ + world.rand.nextFloat() * (area.maxZ - area.minZ),
						0, 0, 0);
			}

		}

		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	private AxisAlignedBB getEffectAABB(PlayerEntity player) {
		double range = 2.0D;
		double radius = 1.0D;
		Vector3d srcVec = new Vector3d(player.getPosX(), player.getPosY() + player.getEyeHeight(), player.getPosZ());
		Vector3d lookVec = player.getLookVec();
		Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

		return new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}
}
