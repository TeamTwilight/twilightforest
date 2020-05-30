package twilightforest.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import twilightforest.TFFeature;
import twilightforest.entity.TFEntities;
import twilightforest.util.PlayerHelper;

import java.util.ArrayList;
import java.util.List;

public class TFBiomeBase extends Biome {

	protected final List<SpawnListEntry> undergroundMonsterList = new ArrayList<>();

	protected final ResourceLocation[] requiredAdvancements = getRequiredAdvancements();

	//FIXME: Blocked out due to null registry objects
//	public final TFFeature containedFeature = getContainedFeature();

	public TFBiomeBase(Builder props) {
		super(props);
	}

	public void addFeatures() {
		//TODO: This is just vanilla's; porting the current cave and ravine generators is not worth it, and should probably just copy-paste
		DefaultBiomeFeatures.addCarvers(this);
	}

	public void addSpawns() {
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.bighorn_sheep.get(), 12, 4, 4));
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.wild_boar.get(), 10, 4, 4));
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.CHICKEN, 10, 4, 4));
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.deer.get(), 15, 4, 5));
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.WOLF, 5, 4, 4));

		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.tiny_bird.get(), 15, 4, 8));
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.squirrel.get(), 10, 2, 4));
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.bunny.get(), 10, 4, 5));
		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(TFEntities.raven.get(), 10, 1, 2));

		//TODO: Lists like these aren't used anymore. Create EntityClassification
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SPIDER, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.ZOMBIE, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SKELETON, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.CREEPER, 1, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SLIME, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.ENDERMAN, 1, 1, 4));
		undergroundMonsterList.add(new SpawnListEntry(TFEntities.kobold.get(), 10, 4, 8));

		addSpawn(EntityClassification.AMBIENT, new SpawnListEntry(EntityType.BAT, 10, 8, 8));
		addSpawn(EntityClassification.AMBIENT, new SpawnListEntry(TFEntities.firefly.get(), 10, 8, 8));
	}

	@Override
	public float getSpawningChance() {
		// okay, 20% more animals
		return 0.12F;
	}

	/**
	 * Does the player have the advancements needed to be in this biome?
	 */
	public boolean doesPlayerHaveRequiredAdvancements(PlayerEntity player) {
		return PlayerHelper.doesPlayerHaveRequiredAdvancements(player, requiredAdvancements);
	}

	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[0];
	}

	/**
	 * Do something bad to a player in the wrong biome.
	 */
	public void enforceProgression(PlayerEntity player, World world) {}

	protected void trySpawnHintMonster(PlayerEntity player, World world) {
		if (world.rand.nextInt(4) == 0) {
			//containedFeature.trySpawnHintMonster(world, player);
		}
	}

	protected TFFeature getContainedFeature() {
		return TFFeature.NOTHING;
	}

	/**
	 * Returns the list of underground creatures.
	 */
	public List<SpawnListEntry> getUndergroundSpawnableList(EntityClassification type) {
		return type == EntityClassification.MONSTER ? this.undergroundMonsterList : getSpawns(type);
	}
}
