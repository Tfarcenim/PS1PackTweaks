package tfar.ps1packtweaks.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityMixin {


        @Inject(
                method = "load",
                at = {@At("TAIL")}
        )
        public void load(CompoundTag nbt, CallbackInfo info) {//note, should only run on client
            PS1PackTweaksClient.onJukeboxLoad((JukeboxBlockEntity)(Object)this);
        }

}
