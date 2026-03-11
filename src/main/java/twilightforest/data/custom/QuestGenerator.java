package twilightforest.data.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class QuestGenerator implements DataProvider {

	private final PackOutput output;

	public QuestGenerator(PackOutput output) {
		this.output = output;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		Function<String, Path> questPath = (s) -> this.output.getOutputFolder().resolve(String.format("data/%s/%s/%s/%s.json", TwilightForestMod.ID, "twilight", "quests", s));

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		futuresBuilder.add(DataProvider.saveStable(output, QuestingRamContext.CODEC.encodeStart(JsonOps.INSTANCE, QuestingRamContext.FALLBACK).resultOrPartial(TwilightForestMod.LOGGER::error).orElseThrow(), questPath.apply("questing_ram")));
		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Twilight Forest Quests";
	}
}
