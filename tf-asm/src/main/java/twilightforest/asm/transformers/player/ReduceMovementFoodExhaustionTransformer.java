package twilightforest.asm.transformers.player;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;
import java.util.stream.Stream;

/**
 * {@link twilightforest.asmhooks.PlayerHooks#getFoodExhaustion}
 */
public class ReduceMovementFoodExhaustionTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("reduce_movement_food_exhaustion");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		Stream.concat(
			ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL,
				"net/minecraft/world/entity/player/Player",
				"causeFoodExhaustion",
				"(F)V"
			),
			ASMUtil.findMethodInstructions(node, Opcodes.INVOKEVIRTUAL,
				"net/minecraft/server/level/ServerPlayer",
				"causeFoodExhaustion",
				"(F)V"
			)
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/PlayerHooks",
					"getFoodExhaustion",
					"(FLnet/minecraft/world/entity/player/Player;)F"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
				"net/minecraft/server/level/ServerPlayer",
				"checkMovementStatistics",
				"(DDD)V"
			),
			new Target(
				"net/minecraft/world/entity/player/Player",
				"jumpFromGround",
				"()V"
			)
		);
	}
}
