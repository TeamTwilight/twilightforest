package twilightforest.asm.transformers.chunk;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.WorldgenHooks#chunkBlanketing}
 */
public class ChunkStatusTaskTransformer extends SimpleMethodTransformer {

	public ChunkStatusTaskTransformer() {
		super(
			"net.minecraft.world.level.chunk.status.ChunkStatusTasks",
			"generateSurface",
			"(Lnet/minecraft/world/level/chunk/status/WorldGenContext;Lnet/minecraft/world/level/chunk/status/ChunkStep;Lnet/minecraft/util/StaticCache2D;Lnet/minecraft/world/level/chunk/ChunkAccess;)Ljava/util/concurrent/CompletableFuture;"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/level/chunk/ChunkGenerator",
			"buildSurface",
			"(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V"
		).findFirst().ifPresent(target -> method.instructions.insert(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 3), // ChunkAccess param
				new VarInsnNode(Opcodes.ALOAD, 5), // WorldGenRegion variable
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/WorldgenHooks",
					"chunkBlanketing",
					"(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/server/level/WorldGenRegion;)V"
				)
			)
		));
	}

}
