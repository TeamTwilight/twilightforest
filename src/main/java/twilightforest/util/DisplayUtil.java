package twilightforest.util;

import com.mojang.math.Transformation;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.joml.Matrix4f;
import tamaized.beanification.Component;

import java.util.Optional;

@Component
public class DisplayUtil {
	public final String tag = "twilightforest_debug_display";

	// Instead of making methods in Display and Display.BlockDisplay public, this is a lazy way of compiling the NBT data then using it to initialize the entity
	public boolean spawnBlockDisplay(Level level, BoundingBox box, BlockState displayState, float padding) {
		Transformation transform = new Transformation(new Matrix4f().scale(box.getXSpan() + padding * 2, box.getYSpan() + padding * 2, box.getZSpan() + padding * 2));
		transform.scale(); // Dummy call to ensure the matrix is factorized, or else there will be nulls on serialization

		// See Display.addAdditionalSaveData()
		DataResult<Tag> serializedTransform = Transformation.EXTENDED_CODEC.encodeStart(NbtOps.INSTANCE, transform);

		if (serializedTransform.isError()) return false;

		CompoundTag entityNBT = new CompoundTag();
		entityNBT.put("transformation", serializedTransform.resultOrPartial().orElseGet(CompoundTag::new));

		// See Display.BlockDisplay.addAdditionalSaveData()
		entityNBT.put("block_state", NbtUtils.writeBlockState(displayState));

		entityNBT.put("Pos", this.newDoubleList(box.minX() - padding, box.minY() - padding, box.minZ() - padding));

		ListTag listtag = new ListTag();
		listtag.add(StringTag.valueOf(this.tag));
		entityNBT.put("Tags", listtag);

		entityNBT.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BLOCK_DISPLAY).toString());

		ProblemReporter.Collector entitySpawnReporter = new ProblemReporter.Collector();
		ValueInput valueInput = TagValueInput.create(entitySpawnReporter, level.registryAccess(), entityNBT);
		Optional<Entity> spawned = EntityType.create(valueInput, level, EntitySpawnReason.LOAD);

		if (spawned.isEmpty()) return false;
		Entity entity = spawned.get();

		return level.addFreshEntity(entity);
	}

	public ListTag newDoubleList(double... numbers) {
		ListTag listtag = new ListTag();

		for (double d0 : numbers) {
			listtag.add(DoubleTag.valueOf(d0));
		}

		return listtag;
	}

	public void setTextEntity(Level level, double x, double y, double z, Display.BillboardConstraints billboardConstraint, net.minecraft.network.chat.Component name) {
		CompoundTag entityNBT = new CompoundTag();

		entityNBT.put("Pos", this.newDoubleList(x, y, z));
		var registryOps = RegistryOps.create(JsonOps.INSTANCE, level.registryAccess());
		String jsonText = ComponentSerialization.CODEC.encodeStart(registryOps, name).getOrThrow().toString();
		entityNBT.putString("text", jsonText);

		DataResult<Tag> serializedAlignment = Display.TextDisplay.Align.CODEC.encodeStart(NbtOps.INSTANCE, Display.TextDisplay.Align.CENTER);
		if (serializedAlignment.isSuccess()) {
			entityNBT.put("alignment", serializedAlignment.getPartialOrThrow());
		}

		DataResult<Tag> serializedBillboard = Display.BillboardConstraints.CODEC.encodeStart(NbtOps.INSTANCE, billboardConstraint);
		if (serializedBillboard.isSuccess()) {
			entityNBT.put("billboard", serializedBillboard.getPartialOrThrow());
		}

		ListTag listtag = new ListTag();
		listtag.add(StringTag.valueOf(this.tag));
		entityNBT.put("Tags", listtag);

		entityNBT.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.TEXT_DISPLAY).toString());

		ProblemReporter.Collector entitySpawnReporter = new ProblemReporter.Collector();
		ValueInput valueInput = TagValueInput.create(entitySpawnReporter, level.registryAccess(), entityNBT);
		Optional<Entity> spawned = EntityType.create(valueInput, level, EntitySpawnReason.LOAD);

		if (spawned.isEmpty()) return;
		Entity entity = spawned.get();

		level.addFreshEntity(entity);
	}
}
