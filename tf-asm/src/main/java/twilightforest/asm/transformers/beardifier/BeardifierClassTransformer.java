package twilightforest.asm.transformers.beardifier;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleClassProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.Set;

public class BeardifierClassTransformer extends SimpleClassProcessor {

	private final Set<Target> targets;
	private final ProcessorName name;

	public BeardifierClassTransformer() {
		this.targets = Set.of(new Target("net.minecraft.world.level.levelgen.Beardifier"));
		this.name = new ProcessorName("twilightforest", "beardifier");
	}

	@Override
	public ProcessorName name() {
		return this.name;
	}

	@Override
	public Set<Target> targets() {
		return this.targets;
	}

	@Override
	public void transform(ClassNode classNode, SimpleTransformationContext context) {
		classNode.fields.add(new FieldNode(
			Opcodes.ACC_PUBLIC,
			"twilightforest_customStructureDensities",
			"Lit/unimi/dsi/fastutil/objects/ObjectList;",
			"Lit/unimi/dsi/fastutil/objects/ObjectList<Lnet/minecraft/world/level/levelgen/DensityFunction;>;",
			null
		));
	}

}
