package twilightforest.entity.monster;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.ai.goal.RedcapPlantTNTGoal;
import twilightforest.init.TFItems;

public class RedcapSapper extends Redcap {

	public RedcapSapper(EntityType<? extends RedcapSapper> type, Level world) {
		super(type, world);
		this.heldPick = new ItemStack(TFItems.IRONWOOD_PICKAXE.get());
		this.heldTNT.setCount(3);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(random, difficulty);
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(TFItems.IRONWOOD_BOOTS.get()));
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new RedcapPlantTNTGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Redcap.registerAttributes()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.ARMOR, 2.0D);
	}
}
