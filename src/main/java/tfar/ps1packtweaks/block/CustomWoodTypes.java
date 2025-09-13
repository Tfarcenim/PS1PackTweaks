package tfar.ps1packtweaks.block;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.fml.loading.FMLLoader;
import tfar.ps1packtweaks.PS1PackTweaks;

import java.util.ArrayList;
import java.util.List;

public class CustomWoodTypes {

    public static final List<WoodType> LIST = new ArrayList<>();
    public static final WoodType EBONY = create("ebony");
    public static final WoodType ENDERVIOLET = create("enderviolet");

    public static WoodType create(String location) {
        WoodType woodType = WoodType.register(WoodType.create(PS1PackTweaks.id(location).toString()));

        LIST.add(woodType);
        return woodType;
    }
}
