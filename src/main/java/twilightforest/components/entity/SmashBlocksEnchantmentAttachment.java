package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SmashBlocksEnchantmentAttachment {

	public static final MapCodec<SmashBlocksEnchantmentAttachment> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.INT.fieldOf("blocks_smashed").forGetter(o -> o.blocksSmashed))
		.apply(instance, SmashBlocksEnchantmentAttachment::new));

	private int blocksSmashed;

	public SmashBlocksEnchantmentAttachment() {
		this(0);
	}

	public SmashBlocksEnchantmentAttachment(int blocksSmashed) {
		this.blocksSmashed = blocksSmashed;
	}

	public void setBlocksSmashed(int blocksSmashed) {
		this.blocksSmashed = blocksSmashed;
	}

	public int getBlocksSmashed() {
		return this.blocksSmashed;
	}
}
