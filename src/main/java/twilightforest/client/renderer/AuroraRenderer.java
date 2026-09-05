package twilightforest.client.renderer;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.MeshData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DynamicUniformStorage;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Supplier;

public class AuroraRenderer {

	private final Supplier<DynamicUniformStorage<AuroraUniform>> uniformStorage = Suppliers.memoize(() ->
		new DynamicUniformStorage<>("Twilight Forest Aurora UBO", AuroraUniform.SIZE, 2)
	);

	public void endFrame() {
		this.uniformStorage.get().endFrame();
	}

	public void draw(MeshData mesh, float alpha, int seed, float x, float y, float z) {
		GpuDevice device = RenderSystem.getDevice();
		GpuTextureView colorTexture = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
		GpuTextureView depthTexture = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();

		try (mesh) {
			if (colorTexture == null)
				return;

			MeshData.DrawState drawState = mesh.drawState();
			RenderSystem.AutoStorageIndexBuffer sequential = RenderSystem.getSequentialBuffer(drawState.mode());
			GpuBuffer indexBuffer = sequential.getBuffer(drawState.indexCount());
			GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
				.writeTransform(RenderSystem.getModelViewMatrix(), new Vector4f(1.0F, 1.0F, 1.0F, alpha), new Vector3f(), new Matrix4f());
			GpuBufferSlice aurora = this.uniformStorage.get().writeUniform(new AuroraUniform(seed, x, y, z));

			try (
				GpuBuffer vertexBuffer = device.createBuffer(
					() -> "Twilight Forest aurora vertices",
					GpuBuffer.USAGE_VERTEX,
					mesh.vertexBuffer()
				);
				RenderPass pass = device.createCommandEncoder().createRenderPass(
					() -> "Aurora",
					colorTexture,
					OptionalInt.empty(),
					depthTexture,
					OptionalDouble.empty()
				)
			) {
				pass.setPipeline(TFRenderPipelines.AURORA);
				RenderSystem.bindDefaultUniforms(pass);
				pass.setUniform("DynamicTransforms", transforms);
				pass.setUniform(TFRenderPipelines.AURORA_UNIFORM, aurora);
				pass.setVertexBuffer(0, vertexBuffer);
				pass.setIndexBuffer(indexBuffer, sequential.type());
				pass.drawIndexed(0, 0, drawState.indexCount(), 1);
			}
		}
	}

	private record AuroraUniform(int seed, float x, float y, float z) implements DynamicUniformStorage.DynamicUniform {

		private static final int SIZE = new Std140SizeCalculator().putInt().putVec3().get();

		@Override
		public void write(ByteBuffer buffer) {
			Std140Builder.intoBuffer(buffer).putInt(this.seed).putVec3(this.x, this.y, this.z);
		}

	}

}
