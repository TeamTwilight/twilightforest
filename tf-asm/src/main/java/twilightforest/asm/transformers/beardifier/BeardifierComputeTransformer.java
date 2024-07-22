package twilightforest.asm.transformers.beardifier;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.ASMHooks#getCustomDensity}
 */
public class BeardifierComputeTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.DRETURN
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMAPI.listOf(
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
					"twilightforest/ASMHooks",
					"getCustomDensity",
					"(DLnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;Lit/unimi/dsi/fastutil/objects/ObjectListIterator;)D"
				)
			)
		));
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.level.levelgen.Beardifier",
			"compute",
			"(Lnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;)D"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
