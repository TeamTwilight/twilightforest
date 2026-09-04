package twilightforest.asm.transformers.snow;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.BlockHooks#keepSnowyStateForSnowloggableBlocks}
 */
public class KeepGrassSnowyForSnowloggableBlocksTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("keep_grass_snowy_for_snowloggable_blocks");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.IRETURN
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"keepSnowyStateForSnowloggableBlocks",
					"(ZLnet/minecraft/world/level/block/state/BlockState;)Z"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.level.block.SnowyDirtBlock",
			"isSnowySetting",
			"(Lnet/minecraft/world/level/block/state/BlockState;)Z"
		));
	}
}
