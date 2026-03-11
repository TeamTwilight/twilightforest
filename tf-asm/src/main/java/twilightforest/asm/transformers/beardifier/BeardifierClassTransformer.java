package twilightforest.asm.transformers.beardifier;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.Set;

/**
 * New Fields:<br/>
 * {@code private it.unimi.dsi.fastutil.objects.ObjectListIterator<net.minecraft.world.level.levelgen.DensityFunction> twilightforest_customStructureDensities;}
 */
public class BeardifierClassTransformer implements ITransformer<ClassNode> {

	@Override
	public @NotNull ClassNode transform(ClassNode node, ITransformerVotingContext context) {
		node.fields.add(new FieldNode(
			Opcodes.ACC_PUBLIC,
			"twilightforest_customStructureDensities",
			"Lit/unimi/dsi/fastutil/objects/ObjectListIterator;",
			"Lit/unimi/dsi/fastutil/objects/ObjectListIterator<net.minecraft.world.level.levelgen.DensityFunction>;",
			null
		));
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<ClassNode>> targets() {
		return Set.of(Target.targetClass("net.minecraft.world.level.levelgen.Beardifier"));
	}

	@Override
	public @NotNull TargetType<ClassNode> getTargetType() {
		return TargetType.CLASS;
	}

}
