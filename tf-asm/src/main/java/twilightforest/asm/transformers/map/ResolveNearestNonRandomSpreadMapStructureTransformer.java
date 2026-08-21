package twilightforest.asm.transformers.map;

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
 * {@link twilightforest.asmhooks.MapHooks#resolveNearestNonRandomSpreadMapStructure}
 */
public class ResolveNearestNonRandomSpreadMapStructureTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("resolve_nearest_non_random_spread_map_structure");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findLast(ASMUtil.findInstructions(
			node,
			Opcodes.ARETURN
		)).ifPresent(target -> node.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1), // ServerLevel from params
				new VarInsnNode(Opcodes.ALOAD, 2), // HolderSet from params
				new VarInsnNode(Opcodes.ALOAD, 3), // BlockPos from params
				new VarInsnNode(Opcodes.ILOAD, 4), // int from params
				new VarInsnNode(Opcodes.ILOAD, 5), // boolean from params
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/MapHooks",
					"resolveNearestNonRandomSpreadMapStructure",
					"(Lcom/mojang/datafixers/util/Pair;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.level.chunk.ChunkGenerator",
			"findNearestMapStructure",
			"(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;"
		));
	}

}
