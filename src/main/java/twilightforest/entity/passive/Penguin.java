package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFEntities;

public class Penguin extends Bird {
	public Penguin(EntityType<? extends Penguin> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new PanicGoal(this, 1.75F));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0F));
		goalSelector.addGoal(3, new TemptGoal(this, 0.75F, Ingredient.of(ItemTagGenerator.PENGUIN_TEMPT_ITEMS), false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.15F));
		goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0F));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6F));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Penguin.class, 5F, 0.02F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	@Override
	public Animal getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
		return TFEntities.PENGUIN.get().create(level);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(ItemTagGenerator.PENGUIN_TEMPT_ITEMS);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D);
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType type) {
		return true;
	}

	public static boolean canSpawn(EntityType<? extends Penguin> type, LevelAccessor accessor, MobSpawnType reason, BlockPos pos, RandomSource rand) {
		return accessor.getBlockState(pos.below()).is(BlockTagGenerator.PENGUINS_SPAWNABLE_ON);
	}
}
