package com.github.llytho.lootjs_test;

import com.github.llytho.lootjs_test.tests.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AllTests {
    public static HashMap<String, SimpleTest> TESTS = new LinkedHashMap<>();

    public static void loadAll() {
        TESTS.clear();
        MixinTests.loadTests();
        ConditionTests.loadTests();
        BuilderTests.loadTests();
        PredicateTests.loadTests();
        LootContextJSTest.loadTests();
        ActionTests.loadTests();
        ConditionsContainerTests.loadTests();
        OtherTests.loadTests();
    }

    public static void add(String name, SimpleTest test) {
        TESTS.put(name, test);
    }
}
