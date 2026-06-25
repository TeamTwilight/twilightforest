package twilightforest.asm.transformers.beardifier;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

public class BeardifierFillArrayTransformer extends SimpleMethodTransformer {
	public BeardifierFillArrayTransformer() {
		super("net.minecraft.world.level.levelgen.Beardifier", "fillArray", "([DLnet/minecraft/world/level/levelgen/DensityFunction$ContextProvider;)V");
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		InsnList inserted = new InsnList();
		LabelNode skipLabel = new LabelNode();

		inserted.add(new VarInsnNode(Opcodes.ALOAD, 0));
		inserted.add(new FieldInsnNode(
			Opcodes.GETFIELD,
			"net/minecraft/world/level/levelgen/Beardifier",
			"affectedBox",
			"Lnet/minecraft/world/level/levelgen/structure/BoundingBox;"
		));
		inserted.add(new JumpInsnNode(Opcodes.IFNONNULL, skipLabel));
		inserted.add(new VarInsnNode(Opcodes.ALOAD, 2));
		inserted.add(new VarInsnNode(Opcodes.ALOAD, 1));
		inserted.add(new VarInsnNode(Opcodes.ALOAD, 0));
		inserted.add(new MethodInsnNode(
			Opcodes.INVOKEINTERFACE,
			"net/minecraft/world/level/levelgen/DensityFunction$ContextProvider",
			"fillAllDirectly",
			"([DLnet/minecraft/world/level/levelgen/DensityFunction;)V",
			true
		));
		inserted.add(new InsnNode(Opcodes.RETURN));
		inserted.add(skipLabel);

		method.instructions.insert(inserted);
	}
}
