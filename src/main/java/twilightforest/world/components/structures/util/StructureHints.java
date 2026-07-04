package twilightforest.world.components.structures.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.Filterable;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataComponents;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface StructureHints {

	String CODEC_NAME = "hint_creature";
	String BOOK_AUTHOR = TwilightForestMod.ID + ".book.author";

	/**
	 * Create a hint book for the specified feature.  Only features with block protection will need this.
	 */
	default ItemStack createHintBook(RegistryAccess registryAccess) {
		ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
		this.addBookInformation(book);
		return book;
	}

	default void addBookInformation(ItemStack book) {
		addBookInformationStatic(book, "unknown", 2);
	}

	static void addBookInformationStatic(ItemStack book, @Nullable String name, int pageCount) {
		String key = name == null ? "unknown" : name;

		Function<Integer, Filterable<Component>> pageGenerationFunc = index -> Filterable.passThrough(Component.translatable(TwilightForestMod.ID + ".book." + key + "." + (index + 1)));

		List<Filterable<Component>> list = Stream.iterate(0, index -> index + 1)
			.limit(pageCount)
			.map(pageGenerationFunc)
			.toList();

		book.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(
			Filterable.passThrough(TwilightForestMod.ID + ".book." + key),
			BOOK_AUTHOR,
			3,
			list,
			true
		));

		book.set(TFDataComponents.TRANSLATABLE_BOOK, Unit.INSTANCE);
	}

	/**
	 * Try to spawn a hint monster near the specified player
	 */
	default void trySpawnHintMonster(Level world, Player player) {
		this.trySpawnHintMonster(world, player, player.blockPosition());
	}

	/**
	 * Try several times to spawn a hint monster
	 */
	void trySpawnHintMonster(Level world, Player player, BlockPos pos);

	static void tryHintForStructure(Player player, ServerLevel level, ResourceKey<Structure> forStructure) {
		Optional<Registry<Structure>> optStructureReg = level.registryAccess().lookup(Registries.STRUCTURE);

		if (optStructureReg.isEmpty() || !(optStructureReg.get().get(forStructure).get() instanceof StructureHints structureHints))
			return;

		structureHints.trySpawnHintMonster(level, player);
	}

	/**
	 * Try once to spawn a hint monster near the player.  Return true if we did.
	 * <p>
	 * We could change up the monster depending on what feature this is, but we currently are not doing that
	 */
	default boolean didSpawnHintMonster(Level world, Player player, BlockPos pos) {
		// find a target point
		int dx = world.getRandom().nextInt(16) - world.getRandom().nextInt(16);
		int dy = world.getRandom().nextInt(4) - world.getRandom().nextInt(4);
		int dz = world.getRandom().nextInt(16) - world.getRandom().nextInt(16);

		// make our hint monster
		Mob hinty = this.createHintMonster(world);

		if (hinty != null) {
			hinty.snapTo(pos.offset(dx, dy, dz), 0f, 0f);

			// check if the bounding box is clear
			if (hinty.checkSpawnObstruction(world) && hinty.getSensing().hasLineOfSight(player)) {
				// add items and hint book
				ItemStack book = this.createHintBook(world.registryAccess());

				if (!book.isEmpty()) {
					hinty.setItemSlot(EquipmentSlot.MAINHAND, book);
					hinty.setDropChance(EquipmentSlot.MAINHAND, 1.0F); //Mob.PRESERVE_ITEM_DROP_CHANCE_THRESHOLD = 1.0F
					//hinty.setDropItemsWhenDead(true);
				}

				world.addFreshEntity(hinty);
				return true;
			}
		}

		return false;
	}

	@Nullable
	Mob createHintMonster(Level world);

	record HintConfig(ItemStack hintItem, EntityType<? extends Mob> hintMob) {
		public static final Codec<HintConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.PASSTHROUGH.fieldOf("hint_item").xmap(
				dynamic -> {
					JsonElement json = (JsonElement) dynamic.getValue();
					DynamicOps<JsonElement> ops = net.minecraft.resources.RegistryOps.create(
						JsonOps.INSTANCE,
						RegistryAccess.EMPTY
					);
					return ItemStack.CODEC.parse(ops, json).result().orElse(ItemStack.EMPTY);
				},
				item -> {
					JsonObject obj = new JsonObject();
					obj.addProperty("id", "minecraft:written_book");
					obj.addProperty("count", 1);
					JsonObject components = new JsonObject();
					JsonObject content = new JsonObject();
					content.addProperty("title", "Hint Book");
					content.addProperty("author", "Lich");
					content.addProperty("generation", 0);
					content.add("pages", new JsonArray());
					components.add("minecraft:written_book_content", content);
					obj.add("components", components);
					return new Dynamic<>(JsonOps.INSTANCE, obj);
				}
			).forGetter(HintConfig::hintItem),
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().comapFlatMap(HintConfig::checkCastMob, entityType -> entityType).fieldOf("hint_mob").forGetter(HintConfig::hintMob)
		).apply(instance, HintConfig::new));

		@SuppressWarnings("unchecked")
		private static DataResult<EntityType<? extends Mob>> checkCastMob(EntityType<?> entityType) {
			if (!entityType.getBaseClass().isAssignableFrom(Mob.class))
				return DataResult.error(() -> "Configured Hint Entity " + entityType.toShortString() + " does not have a `Mob` superclass!");
			return DataResult.success((EntityType<? extends Mob>) entityType);
		}

		public static ItemStack defaultBook() {
			return book("unknown", 2);
		}

		public static ItemStack book(String name, int pageCount) {
			ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
			StructureHints.addBookInformationStatic(book, name, pageCount);
			return book;
		}
	}
}
