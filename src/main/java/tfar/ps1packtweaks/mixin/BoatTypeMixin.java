package tfar.ps1packtweaks.mixin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat.Type;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.ps1packtweaks.block.CustomBoatTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin({Type.class})
public class BoatTypeMixin {
    @Shadow
    @Mutable
    @Final
    private static Type[] $VALUES;

    public BoatTypeMixin() {
    }

    @Invoker("<init>")
    public static Type create(String internal, int id, Block planks, String name) {
        throw new AssertionError();
    }

    @Inject(
            method = {"<clinit>"},
            at = {@At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/vehicle/Boat$Type;$VALUES:[Lnet/minecraft/world/entity/vehicle/Boat$Type;",
                    shift = At.Shift.AFTER
            )}
    )
    private static void addBoat(CallbackInfo ci) {
        List<Type> types = new ArrayList<>(Arrays.asList($VALUES));
        Type last = types.get(types.size() - 1);
        types.add(CustomBoatTypes.EBONY = create("ebony", last.ordinal() + 1,
                Registry.BLOCK.get(new ResourceLocation("kazsend","ebony_planks")), "ebony"));
        types.add(CustomBoatTypes.ENDERVIOLET = create("enderviolet", last.ordinal() + 2,
                Registry.BLOCK.get(new ResourceLocation("kazsend","enderviolet_planks")), "enderviolet"));
        $VALUES = types.toArray(new Type[0]);
    }
}