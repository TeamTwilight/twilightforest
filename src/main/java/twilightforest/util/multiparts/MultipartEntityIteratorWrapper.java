package twilightforest.util.multiparts;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.TFPart;

import java.util.Iterator;

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
				int size = 0;
				for (PartEntity<?> partEntity : arr) {
					if (partEntity instanceof TFPart<?>)
						size++;
				}
				if (size > 0) {
					partIndex = 0;
					parts = new TFPart<?>[size];
					int index = 0;
					for (PartEntity<?> partEntity : arr) {
						if (partEntity instanceof TFPart<?> part) {
							parts[index] = part;
							index++;
						}
					}
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
