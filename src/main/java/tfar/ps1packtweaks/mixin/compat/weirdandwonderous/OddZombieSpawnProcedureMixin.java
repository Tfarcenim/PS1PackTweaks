package tfar.ps1packtweaks.mixin.compat.weirdandwonderous;

import crumbs.weirdandwonderous.procedures.OddZombieSpawnProcedure;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import tfar.ps1packtweaks.client.WorldLocker;

import javax.annotation.Nullable;

@Mixin(OddZombieSpawnProcedure.class)
public class OddZombieSpawnProcedureMixin {

    @Shadow(remap = false)
    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
    }
        /**
         * @author
         * @reason
         */
    @Overwrite(remap = false)
    public static void onEntitySpawned(EntityJoinWorldEvent event) {
        if (!WorldLocker.isPure()) {
            execute(event, event.getWorld(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
        }
    }
}
