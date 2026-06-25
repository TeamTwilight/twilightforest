package twilightforest.asm.transformers.conquered;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.WorldgenHooks#loadStaticStart}
 */
public class StructureStartLoadStaticTransformer extends SimpleMethodTransformer {

	public StructureStartLoadStaticTransformer() {
		super(
			"net.minecraft.world.level.levelgen.structure.StructureStart",
			"loadStaticStart",
			"(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Lnet/minecraft/nbt/CompoundTag;J)Lnet/minecraft/world/level/levelgen/structure/StructureStart;"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKESPECIAL,
			"net/minecraft/world/level/levelgen/structure/StructureStart",
			"<init>",
			"(Lnet/minecraft/world/level/levelgen/structure/Structure;Lnet/minecraft/world/level/ChunkPos;ILnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)V"
		).findFirst().ifPresent(target -> method.instructions.insert(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 10),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/WorldgenHooks",
					"loadStaticStart",
					"(Lnet/minecraft/world/level/levelgen/structure/StructureStart;Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;"
				)
			)
		));
	}

}
