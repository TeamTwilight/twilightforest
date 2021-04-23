package twilightforest.dispenser;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TransformationDispenseBehavior extends DefaultDispenseItemBehavior {

    boolean fired = false;
    private final Map<EntityType<?>, EntityType<?>> transformMap = new HashMap<>();

    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        this.initTransformations();
        World world = source.getWorld();
        Random random = world.getRandom();
        BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
       if(!world.isRemote) {
           for (LivingEntity livingentity : world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(blockpos), EntityPredicates.NOT_SPECTATING)) {
               if(transformMap.containsValue(livingentity.getType())) {
                   EntityType<?> type = transformMap.get(livingentity.getType());
                   Entity newEntity = type.create(world);
                   if(type != null && newEntity != null) {
                       newEntity.setLocationAndAngles(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), livingentity.rotationYaw, livingentity.rotationPitch);
                       if (newEntity instanceof MobEntity && livingentity.world instanceof IServerWorld) {
                           IServerWorld sworld = (IServerWorld) livingentity.world;
                           ((MobEntity) newEntity).onInitialSpawn(sworld, livingentity.world.getDifficultyForLocation(livingentity.getPosition()), SpawnReason.CONVERSION, null, null);
                       }

                       try {
                           UUID uuid = newEntity.getUniqueID();
                           newEntity.read(livingentity.writeWithoutTypeId(newEntity.writeWithoutTypeId(new CompoundNBT())));
                           newEntity.setUniqueId(uuid);
                       } catch (Exception e) {
                           TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
                       }

                       livingentity.world.addEntity(newEntity);
                       livingentity.remove();

                       if (livingentity instanceof MobEntity) {
                           ((MobEntity) livingentity).spawnExplosionParticle();
                           ((MobEntity) livingentity).spawnExplosionParticle();
                       }
                       livingentity.playSound(TFSounds.POWDER_USE, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
                       stack.shrink(1);
                       fired = true;
                   }
               }
           }
       }
        return stack;
    }

    @Override
    protected void playDispenseSound(IBlockSource source) {
        if(fired) {
            super.playDispenseSound(source);
        } else {
            source.getWorld().playEvent(1001, source.getBlockPos(), 0);
        }
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
}
