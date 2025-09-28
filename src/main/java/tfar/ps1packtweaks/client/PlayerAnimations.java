package tfar.ps1packtweaks.client;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.Objects;

public class PlayerAnimations {
    public static final ResourceLocation ON_FIRE_FLAIL = PS1PackTweaks.id("onfireflail");

    public static void register() {
      /*  PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                PS1PackTweaks.id( "animation"),
                42,
                abstractClientPlayer -> new ModifierLayer<>());*/
    }

   /* public static void playAnimation(@NotNull ModifierLayer<IAnimation> modifierLayer, @NotNull ResourceLocation animation) {
        KeyframeAnimation anim = Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(animation));
        modifierLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.CONSTANT), new KeyframeAnimationPlayer(anim)
                .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
    }

    public static void stopAnimations(ModifierLayer<IAnimation> modifierLayer) {
        modifierLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.CONSTANT), null);
    }*/
}
