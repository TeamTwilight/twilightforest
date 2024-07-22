package twilightforest.compat.jei.util;

import net.minecraft.world.entity.EntityType;
import twilightforest.compat.jei.FakeEntityType;

public record TransformationRecipe(FakeEntityType input, FakeEntityType output, boolean isReversible) {
}
