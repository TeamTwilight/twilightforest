package twilightforest.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.function.DoubleUnaryOperator;

public class EntityUtil {

	public static boolean canDestroyBlock(World world, BlockPos pos, Entity entity) {
		return canDestroyBlock(world, pos, world.getBlockState(pos), entity);
	}

	public static boolean canDestroyBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		float hardness = state.getBlockHardness(world, pos);
		return hardness >= 0f && hardness < 50f && !state.getBlock().isAir(state, world, pos)
				&& state.getBlock().canEntityDestroy(state, world, pos, entity)
				&& (/* rude type limit */!(entity instanceof EntityLivingBase)
				|| ForgeEventFactory.onEntityDestroyBlock((EntityLivingBase) entity, pos, state));
	}

	/**
	 * [VanillaCopy] Exact copy of Entity.rayTrace
	 * TODO: update it?
	 */
	@Nullable
	public static RayTraceResult rayTrace(Entity entity, double range) {
		Vec3d position = entity.getPositionEyes(1.0F);
		Vec3d look = entity.getLook(1.0F);
		Vec3d dest = position.add(look.x * range, look.y * range, look.z * range);
		return entity.world.rayTraceBlocks(position, dest);
	}

	@Nullable
	public static RayTraceResult rayTrace(EntityPlayer player) {
		return rayTrace(player, null);
	}

	@Nullable
	public static RayTraceResult rayTrace(EntityPlayer player, @Nullable DoubleUnaryOperator modifier) {
		double range = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		return rayTrace(player, modifier == null ? range : modifier.applyAsDouble(range));
	}
}
