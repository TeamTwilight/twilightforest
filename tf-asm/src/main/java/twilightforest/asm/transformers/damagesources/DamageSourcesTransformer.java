package twilightforest.asm.transformers.damagesources;

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
 * {@link twilightforest.asmhooks.DamageSourceHooks#getCustomDamageSource}
 */
public class DamageSourcesTransformer implements ITransformer<MethodNode> {
	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.ARETURN
		).forEach(target -> node.instructions.insertBefore(target, ASMAPI.listOf(
			// First in stack should be a DamageSource
			new VarInsnNode(Opcodes.ALOAD, 1), // Also add the parameter, a LivingEntity
			new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"twilightforest/asmhooks/DamageSourceHooks",
				"getCustomDamageSource",
				"(Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/damagesource/DamageSource;"
			)
		)));

		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext iTransformerVotingContext) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.damagesource.DamageSources",
			"mobAttack",
			"(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/damagesource/DamageSource;"
		), Target.targetMethod(
			"net.minecraft.world.damagesource.DamageSources",
			"playerAttack",
			"(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/damagesource/DamageSource;"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}
}
