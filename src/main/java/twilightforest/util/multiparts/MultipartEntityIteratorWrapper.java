package twilightforest.util.multiparts;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.TFPart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultipartEntityIteratorWrapper implements Iterator<Entity> {

	private final Iterator<Entity> delegate;
	private TFPart<?> @Nullable [] parts;
	private int partIndex;

	MultipartEntityIteratorWrapper(Iterator<Entity> iter) {
		this.delegate = iter;
	}

	@Override
	public boolean hasNext() {
		return parts != null || delegate.hasNext();
	}

	@Override
	public Entity next() {
		if (parts != null) {
			Entity next = parts[partIndex];
			partIndex++;
			if (partIndex >= parts.length)
				parts = null;
			return next;
		}
		Entity next = delegate.next();
		if (next.isMultipartEntity()) {
			PartEntity<?>[] arr = next.getParts();
			// getParts is nullable, the annotation is used incorrectly
			//noinspection ConstantValue
			if (arr != null) {
				List<TFPart<?>> tfParts = new ArrayList<>();
				for (PartEntity<?> partEntity : arr) {
					// Only add TFPart entities that have a custom renderer (not the default noop renderer).
					// Parts using the default renderer (like SpikeBlock from BlockChainGoblin)
					// are rendered by their parent entity's renderer and should not be added
					// as separate entities, otherwise their EntityRenderState will have the parent's
					// entity type, causing a ClassCastException in the submit phase.
					if (partEntity instanceof TFPart<?> part && !part.renderer().equals(TFPart.RENDERER)) {
						tfParts.add(part);
					}
				}
				if (!tfParts.isEmpty()) {
					partIndex = 0;
					parts = tfParts.toArray(new TFPart<?>[0]);
				}
			}
		}
		return next;
	}

	@Override
	public void remove() {
		if (parts == null || partIndex <= 0) {
			delegate.remove();
		} else {
			if (partIndex >= parts.length) {
				parts = null;
			} else {
				System.arraycopy(parts, partIndex, parts, partIndex - 1, parts.length - 1 - partIndex - 1);
			}
		}
	}

}
