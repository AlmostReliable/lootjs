package com.almostreliable.lootjs.kube;

import com.almostreliable.lootjs.LookupProvider;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.resources.RegistryOps;

public class KubeOps extends RegistryOps<Object> {

    public static KubeOps create(RegistryAccessContainer reggistries) {
        return new KubeOps(RegistryOps.create(reggistries.java(), new LookupProvider(reggistries.access())));
    }

    public KubeOps(RegistryOps<Object> dynamicOps) {
        super(dynamicOps);
    }
}
