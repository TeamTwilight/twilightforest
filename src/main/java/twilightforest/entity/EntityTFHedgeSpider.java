package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

/**
 * The hedge spider is just like a normal spider, but it can spawn in the daytime.
 *
 * @author Ben
 */
public class EntityTFHedgeSpider extends EntitySpider {
	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/hedge_spider");

	public EntityTFHedgeSpider(World world) {
		super(world);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

		// Remove default spider melee task
		this.tasks.taskEntries.removeIf(t -> t.action instanceof EntityAIAttackMelee);

		// Replace with one that doesn't become docile in light
		// [VanillaCopy] based on EntitySpider.AISpiderAttack
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1, true) {
			@Override
			protected double getAttackReachSqr(EntityLivingBase attackTarget) {
				return 4.0F + attackTarget.width;
			}
		});

		// Remove default spider target player task
		this.targetTasks.taskEntries.removeIf(t -> t.priority == 2 && t.action instanceof EntityAINearestAttackableTarget);
		// Replace with one that doesn't care about light
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected boolean isValidLightLevel() {
		int chunkX = MathHelper.floor(posX) >> 4;
		int chunkZ = MathHelper.floor(posZ) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return TFFeature.getNearestFeature(chunkX, chunkZ, world) == TFFeature.HEDGE_MAZE
				|| super.isValidLightLevel();
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
