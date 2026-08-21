package twilightforest.asm.transformers.beardifier;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.WorldgenHooks#getCustomDensity}
 */
public class BeardifierComputeTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("beardifier_compute");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.DRETURN
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1), // DensityFunction$FunctionContext from params
				new VarInsnNode(Opcodes.ALOAD, 0), // Beardifier.this
				new FieldInsnNode(
					Opcodes.GETFIELD,
					"net/minecraft/world/level/levelgen/Beardifier",
					"twilightforest_customStructureDensities",
					"Lit/unimi/dsi/fastutil/objects/ObjectListIterator;"
				),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/WorldgenHooks",
					"getCustomDensity",
					"(DLnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;Lit/unimi/dsi/fastutil/objects/ObjectListIterator;)D"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.level.levelgen.Beardifier",
			"compute",
			"(Lnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;)D"
		));
	}

}
