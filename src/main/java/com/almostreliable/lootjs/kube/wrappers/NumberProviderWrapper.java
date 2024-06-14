package com.almostreliable.lootjs.kube.wrappers;

import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class NumberProviderWrapper {

    public static NumberProvider constant(int value) {
        return new ConstantValue(value);
    }

    public static NumberProvider uniform(NumberProvider min, NumberProvider max) {
        return new UniformGenerator(min, max);
    }

    public static NumberProvider binomial(NumberProvider n, NumberProvider p) {
        return new BinomialDistributionGenerator(n, p);
    }
}
