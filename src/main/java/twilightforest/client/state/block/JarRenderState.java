package twilightforest.client.state.block;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public class JarRenderState extends BlockEntityRenderState {
	public BlockModelRenderState jarModel = new BlockModelRenderState();
	public BlockModelRenderState lidModel = new BlockModelRenderState();
	public Item lid = null;
	public float wobbleAmount;
	public float wobbleAmplitude;
	@Nullable
	public ItemStackRenderState itemStack;
	public int itemRotation;
}
