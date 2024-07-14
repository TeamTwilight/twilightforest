package twilightforest.beans.multipart;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import twilightforest.entity.TFPart;
import twilightforest.junit.MockitoFixer;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoFixer.class)
public class MultipartEntityIteratorWrapperTests {

	private Entity mockEntity(PartEntity<?>... parts) {
		Entity entity = mock(Entity.class);
		when(entity.isMultipartEntity()).thenReturn(parts.length > 0);
		when(entity.getParts()).thenReturn(parts);
		return entity;
	}

	@Test
	public void noPartEntities() {
		Iterator<Entity> result = new MultipartEntityIteratorWrapper(List.of(
			mockEntity(),
			mockEntity(),
			mockEntity()
		).iterator());

		assertNotNull(result);

		// Since these are mocks, we need to use #getSuperclass
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());

		assertFalse(result.hasNext());
	}

	@Test
	public void noTFPartEntities() {
		Iterator<Entity> result = new MultipartEntityIteratorWrapper(List.of(
			mockEntity(),
			mockEntity(mock(PartEntity.class)),
			mockEntity(mock(PartEntity.class)),
			mockEntity(),
			mockEntity(mock(PartEntity.class), mock(PartEntity.class)),
			mockEntity()
		).iterator());

		assertNotNull(result);

		// Since these are mocks, we need to use #getSuperclass
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());

		assertFalse(result.hasNext());
	}

	@Test
	public void withTFPartEntities() {
		Iterator<Entity> result = new MultipartEntityIteratorWrapper(List.of(
			mockEntity(),
			mockEntity(mock(PartEntity.class)),
			mockEntity(mock(TFPart.class)),
			mockEntity(),
			mockEntity(mock(TFPart.class), mock(TFPart.class)),
			mockEntity()
		).iterator());

		assertNotNull(result);

		// Since these are mocks, we need to use #getSuperclass
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(TFPart.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(TFPart.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(TFPart.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());

		assertFalse(result.hasNext());
	}

}
