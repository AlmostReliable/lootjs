package testmod.mixin;


import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.Map;

@Mixin(value = ScriptManager.class, remap = false)
public abstract class ScriptManagerMixin {

    @Shadow @Final public ScriptType scriptType;

    @Shadow @Final public Map<String, ScriptPack> packs;

    @Shadow
    protected abstract void loadFile(ScriptPack pack, ScriptFileInfo fileInfo);

    @Inject(method = "reload", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/script/ScriptManager;load()V"))
    private void testmod$test(CallbackInfo ci) {
        if (scriptType != ScriptType.SERVER) {
            return;
        }

        String prop = System.getProperty("lootjs.example_scripts");
        if (prop == null) {
            return;
        }

        Path p = Path.of(prop);
        var packInfo = new ScriptPackInfo("server_examples", "");
        var pack = new ScriptPack((ScriptManager) (Object) this, packInfo);
        KubeJS.loadScripts(pack, p, "");

        for (var script : pack.info.scripts) {
            loadFile(pack, script);
        }

        pack.scripts.sort(null);
        this.packs.put("server_examples", pack);
    }
}
