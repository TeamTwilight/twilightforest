package twilightforest.asm.transformers.map;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.MapHooks#resolveNearestNonRandomSpreadMapStructure}
 */
public class ResolveNearestNonRandomSpreadMapStructureTransformer extends SimpleMethodTransformer {

	public ResolveNearestNonRandomSpreadMapStructureTransformer() {
		super(
			"net.minecraft.world.level.chunk.ChunkGenerator",
			"findNearestMapStructure",
			"(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findLast(ASMUtil.findInstructions(
			method,
			Opcodes.ARETURN
		)).ifPresent(target -> method.instructions.insertBefore(
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

}
