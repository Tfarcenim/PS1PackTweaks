package tfar.ps1packtweaks.client;// Save this class in your mod and generate all required imports

import com.cursedcauldron.wildbackport.client.animation.api.Animation;
import com.cursedcauldron.wildbackport.client.animation.api.AnimationHelper;
import com.cursedcauldron.wildbackport.client.animation.api.Keyframe;
import com.cursedcauldron.wildbackport.client.animation.api.Transformation;

/**
 * Made with Blockbench 4.12.6
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class Coral_Head2Animation {
	public static final Animation Walk = Animation.Builder.create(4.0F).looping()
		.addBoneAnimation("left_arm", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.rotation(90.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.3333F, AnimationHelper.rotation(-89.6673F, 0.1129F, -0.0242F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.6667F, AnimationHelper.rotation(90.0F, 0.0F, -90.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.0F, AnimationHelper.rotation(-87.719F, 1.5237F, -45.1342F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.3333F, AnimationHelper.rotation(45.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.6667F, AnimationHelper.rotation(-59.3753F, 11.0311F, -48.2732F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.0F, AnimationHelper.rotation(90.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.3333F, AnimationHelper.rotation(-89.6673F, 0.1129F, -0.0242F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.6667F, AnimationHelper.rotation(90.0F, 0.0F, -90.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.0F, AnimationHelper.rotation(-87.719F, 1.5237F, -45.1342F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.3333F, AnimationHelper.rotation(42.7342F, 15.6999F, -16.3249F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.6667F, AnimationHelper.rotation(-59.1634F, -12.7256F, -62.2273F), Transformation.Interpolations.CATMULL),
			new Keyframe(4.0F, AnimationHelper.rotation(90.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL)
		))
		.addBoneAnimation("right_arm", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.rotation(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.3333F, AnimationHelper.rotation(90.3327F, 0.1129F, -0.0242F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.5F, AnimationHelper.rotation(0.1428F, 0.1367F, 22.7989F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.6667F, AnimationHelper.rotation(-90.0F, 0.0F, 90.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.0F, AnimationHelper.rotation(90.0F, 0.0F, 45.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.3333F, AnimationHelper.rotation(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.6667F, AnimationHelper.rotation(43.9987F, -8.7596F, 8.9847F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.0F, AnimationHelper.rotation(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.3333F, AnimationHelper.rotation(90.3327F, 0.1129F, -0.0242F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.6667F, AnimationHelper.rotation(-90.0F, 0.0F, 90.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.0F, AnimationHelper.rotation(90.0F, 0.0F, 45.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.3333F, AnimationHelper.rotation(-14.5108F, 43.0795F, 69.2464F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.6667F, AnimationHelper.rotation(35.2644F, -30.0F, 35.2644F), Transformation.Interpolations.CATMULL),
			new Keyframe(4.0F, AnimationHelper.rotation(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL)
		))
		.addBoneAnimation("left_leg", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.3333F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.6667F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.0F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.3333F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.6667F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.0F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.3333F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.6667F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.0F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.3333F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.6667F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(4.0F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL)
		))
		.addBoneAnimation("right_leg", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.3333F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(0.6667F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.0F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.3333F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(1.6667F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.0F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.3333F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(2.6667F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.0F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.3333F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(3.6667F, AnimationHelper.rotation(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL),
			new Keyframe(4.0F, AnimationHelper.rotation(60.0F, 0.0F, 0.0F), Transformation.Interpolations.CATMULL)
		))
		.addBoneAnimation("body", new Transformation(Transformation.Targets.TRANSLATE,
			new Keyframe(0.0F, AnimationHelper.translate(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAL)
		))
		.build();
}