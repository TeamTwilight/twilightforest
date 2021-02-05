package twilightforest.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import twilightforest.TFSounds;
import twilightforest.worldgen.biomes.BiomeKeys;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class EntityTFMosquitoSwarm extends MonsterEntity {

	public EntityTFMosquitoSwarm(EntityType<? extends EntityTFMosquitoSwarm> type, World world) {
		super(type, world);

		this.stepHeight = 2.1f;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 12.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MOSQUITO;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			if (entity instanceof LivingEntity) {
				int duration;
				switch (world.getDifficulty()) {
					case EASY:
						duration = 7;
						break;
					default:
					case NORMAL:
						duration = 15;
						break;
					case HARD:
						duration = 30;
						break;
				}

				((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.HUNGER, duration * 20, 0));
			}

			return true;
		} else {
			return false;
		}
	}

	public static boolean canSpawn(EntityType<? extends MonsterEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		Optional<RegistryKey<Biome>> key = world.func_242406_i(pos);
		if (Objects.equals(key, Optional.of(BiomeKeys.SWAMP))) {
			// no light level check
			return world.getDifficulty() != Difficulty.PEACEFUL && MobEntity.canSpawnOn(type, world, reason, pos, rand);
		} else {
			return MonsterEntity.canMonsterSpawn(type, world, reason, pos, rand);
		}
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

}
