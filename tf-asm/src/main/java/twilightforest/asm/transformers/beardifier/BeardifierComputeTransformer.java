package twilightforest.asm.transformers.beardifier;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * After each {@link Beardifier#compute} returns, inject a call to {@link twilightforest.asmhooks.WorldgenHooks#getCustomDensity}
 * to sum the densities of custom structure-provided density functions.
 * <br/>
 * Injection target is right before DRETURN.
 */
public class BeardifierComputeTransformer extends SimpleMethodTransformer {
	public BeardifierComputeTransformer() {
		super("net.minecraft.world.level.levelgen.Beardifier", "compute", "(Lnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;)D");
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(method, Opcodes.DRETURN).forEach(target -> method.instructions.insertBefore(target, ASMUtil.listOf(
			new VarInsnNode(Opcodes.ALOAD, 1),
			new VarInsnNode(Opcodes.ALOAD, 0),
			new FieldInsnNode(
				Opcodes.GETFIELD,
				"net/minecraft/world/level/levelgen/Beardifier",
				"twilightforest_customStructureDensities",
				"Lit/unimi/dsi/fastutil/objects/ObjectList;"
			),
			new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"twilightforest/asmhooks/WorldgenHooks",
				"getCustomDensity",
				"(DLnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;Lit/unimi/dsi/fastutil/objects/ObjectList;)D",
				false
			)
		)));
	}
}
