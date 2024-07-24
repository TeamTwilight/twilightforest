package twilightforest.asm.transformers.shroom;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.ASMHooks#modifySoilDecisionForMushroomBlockSurvivability}
 */
public class ModifySoilDecisionForMushroomBlockSurvivabilityTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/level/block/state/BlockState",
			"canSustainPlant",
			"(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/neoforged/neoforge/common/util/TriState;"
		).forEach(target -> node.instructions.insert(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 2),
				new VarInsnNode(Opcodes.ALOAD, 3),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/ASMHooks",
					"modifySoilDecisionForMushroomBlockSurvivability",
					"(Lnet/neoforged/neoforge/common/util/TriState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Lnet/neoforged/neoforge/common/util/TriState;"
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
			"net.minecraft.world.level.block.MushroomBlock",
			"canSurvive",
			"(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}
