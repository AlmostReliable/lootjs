package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LookupProvider;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.resources.RegistryOps;

public class KubeOps extends RegistryOps<Object> {

    public static KubeOps create(RegistryAccessContainer registries) {
        return new KubeOps(RegistryOps.create(registries.java(), new LookupProvider(registries.registryAccess())));
    }

    public KubeOps(RegistryOps<Object> dynamicOps) {
        super(dynamicOps);
    }
}
