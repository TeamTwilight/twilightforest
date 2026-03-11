package twilightforest.client.model.block.giantblock;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

public class GiantBlockBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	public static <T extends ModelBuilder<T>> GiantBlockBuilder<T> begin(T parent, ExistingFileHelper helper) {
		return new GiantBlockBuilder<>(parent, helper);
	}

	private ResourceLocation parentBlock;

	protected GiantBlockBuilder(T parent, ExistingFileHelper helper) {
		super(TwilightForestMod.prefix("giant_block"), parent, helper, false);
	}

	public GiantBlockBuilder<T> parentBlock(Block block) {
		Preconditions.checkNotNull(block, "parent block must not be null");
		this.parentBlock = BuiltInRegistries.BLOCK.getKey(block);
		return this;
	}

	@Override
	public T end() {
		Preconditions.checkNotNull(this.parentBlock, "giant block must have a parent block");
		return super.end();
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);
		json.addProperty("parent_block", this.parentBlock.toString());
		return json;
	}
}
