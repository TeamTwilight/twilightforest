package twilightforest.compat.jei.util;

import twilightforest.compat.jei.FakeEntityType;

public record TransformationRecipe(FakeEntityType input, FakeEntityType output, boolean isReversible) {
}
