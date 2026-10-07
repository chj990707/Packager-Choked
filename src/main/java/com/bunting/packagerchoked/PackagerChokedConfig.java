package com.bunting.packagerchoked;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class PackagerChokedConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

//    public static final ModConfigSpec.BooleanValue HEURISTIC_UNWRAP = BUILDER
//            .comment("Whether to use heuristic optimizer on Create's Packager")
//            .define("heuristicUnwrap", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
