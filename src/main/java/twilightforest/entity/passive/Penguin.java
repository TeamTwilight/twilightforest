package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import twilightforest.entity.TFEntities;

import java.util.Random;

public class Penguin extends Bird {
	public Penguin(EntityType<? extends Penguin> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new PanicGoal(this, 1.75F));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0F));
		goalSelector.addGoal(3, new TemptGoal(this, 0.75F, Ingredient.of(Items.COD), false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.15F));
		goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0F));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6F));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Penguin.class, 5F, 0.02F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	@Override
	public Animal getBreedOffspring(ServerLevel world, AgeableMob entityanimal) {
		return TFEntities.PENGUIN.create(world);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == Items.COD;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D);
	}

	public static boolean canSpawn(EntityType<? extends Penguin> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
		BlockPos blockpos = pos.below();
		return Mob.checkMobSpawnRules(type, world, reason, pos, rand)
				|| (world.getBlockState(blockpos).getBlock() == Blocks.ICE)
				|| (world.getBlockState(blockpos).getBlock() == Blocks.PACKED_ICE);
	}
}
