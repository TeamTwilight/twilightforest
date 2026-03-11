package twilightforest.client.model.block.connected;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

//let the magic begin.
//as far as I understand, CTM texture sheets are laid out in 4 pieces:
// - cornerless connections in the top left
// - vertical connections in the top right
// - horizontal connections in the bottom left
// - corner connection in the bottom right
//each quadrant is composed of a 4x4 piece for each rotation that the model can pick based on where on the block the connection is happening.
//to avoid confusion: cornerless means that no corner piece renders. This happens when you have a block horizontally, vertically, and filling that corner space between the 2.
// **
// **
//while a corner means that a corner piece will render. This happens when you have a block horizontally and vertically, but theres no block in the corner.
//  *
// **
public enum ConnectionLogic {

	NONE(0, 0, 0, 16, 16),
	CORNERLESS(1, 0, 0, 8, 8),
	VERTICAL(1, 0, 8, 8, 16),
	HORIZONTAL(1, 8, 0, 16, 8),
	CORNER(1, 8, 8, 16, 16);

	private final int texture;
	private final int u0;
	private final int v0;
	private final int u1;
	private final int v1;

	public static final Direction[][] AXIS_PLANE_DIRECTIONS = new Direction[][]{
		{Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH},
		{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST},
		{Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST}
	};

	ConnectionLogic(int texture, int u0, int v0, int u1, int v1) {
		this.texture = texture;
		this.u0 = u0;
		this.v0 = v0;
		this.u1 = u1;
		this.v1 = v1;
	}

	public static ConnectionLogic of(boolean horizontal, boolean vertical, boolean corner) {
		if (corner) {
			return CORNERLESS;
		} else if (horizontal) {
			return vertical ? CORNER : HORIZONTAL;
		} else {
			return vertical ? VERTICAL : NONE;
		}
	}

	public TextureAtlasSprite chooseTexture(TextureAtlasSprite[] sprites) {
		return sprites[this.texture];
	}

	public float[] remapUVs(float[] uvs) {
		return new float[]{this.getU(uvs[0]), this.getV(uvs[1]), this.getU(uvs[2]), this.getV(uvs[3])};
	}

	public float getU(float delta) {
		return (float) this.u0 + (float) (this.u1 - this.u0) * (delta / 16.0F);
	}

	public float getV(float delta) {
		return (float) this.v0 + (float) (this.v1 - this.v0) * (delta / 16.0F);
	}
}
