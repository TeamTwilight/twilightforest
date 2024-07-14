package twilightforest;

import net.minecraft.world.entity.Entity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import twilightforest.beans.multipart.MultipartEntityUtil;
import twilightforest.junit.MockBean;
import twilightforest.junit.MockitoFixer;
import twilightforest.junit.TFBeanContextJunitExtension;


import java.util.Collections;
import java.util.Iterator;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoFixer.class, TFBeanContextJunitExtension.class})
public class ASMHooksTests {

	@MockBean
	private MultipartEntityUtil multipartEntityUtil;

	@Test
	public void multipartBean() {
		Iterator<Entity> iter = Collections.emptyIterator();
		Entity entity = mock(Entity.class);

		ASMHooks.resolveEntitiesForRendering(iter);
		ASMHooks.resolveEntityRenderer(null, entity);
		ASMHooks.sendDirtyEntityData(entity);

		verify(multipartEntityUtil, times(1)).injectTFPartEntities(iter);
		verify(multipartEntityUtil, times(1)).tryLookupTFPartRenderer(null, entity);
		verify(multipartEntityUtil, times(1)).sendDirtyMultipartEntityData(entity);
	}

}
