package tfar.ps1packtweaks.compat;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class DatapackLootTables {

    public static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();

    public static final ResourceLocation ARCHERYTOWER = register(kazs_end_village("chests/archerytower"));
    public static final ResourceLocation BATHHOUSE = register(kazs_end_village("chests/bathhouse"));
    public static final ResourceLocation BLACKSMITH = register(kazs_end_village("chests/blacksmith"));
    public static final ResourceLocation BUTCHER = register(kazs_end_village("chests/butcher"));
    public static final ResourceLocation CAFETERIA_CABINETS1 = register(kazs_end_village("chests/cafeteria_cabinets1"));
    public static final ResourceLocation CAFETERIA_CABINETS2 = register(kazs_end_village("chests/cafeteria_cabinets2"));
    public static final ResourceLocation CAFETERIA_CABINETS3 = register(kazs_end_village("chests/cafeteria_cabinets3"));
    public static final ResourceLocation CAFETERIA_ICEBOX = register(kazs_end_village("chests/cafeteria_icebox"));
    public static final ResourceLocation CAMP = register(kazs_end_village("chests/camp"));
    public static final ResourceLocation CARTOGRAPHER = register(kazs_end_village("chests/cartographer"));
    public static final ResourceLocation CEMETERY = register(kazs_end_village("chests/cemetery"));
    public static final ResourceLocation FARM = register(kazs_end_village("chests/farm"));
    public static final ResourceLocation HOSPITAL = register(kazs_end_village("chests/hospital"));
    public static final ResourceLocation HOUSE = register(kazs_end_village("chests/house"));
    public static final ResourceLocation LAB = register(kazs_end_village("chests/lab"));
    public static final ResourceLocation LAB1 = register(kazs_end_village("chests/lab1"));
    public static final ResourceLocation LAB2 = register(kazs_end_village("chests/lab2"));
    public static final ResourceLocation LABCRAFT = register(kazs_end_village("chests/labcraft"));
    public static final ResourceLocation LABEVIL = register(kazs_end_village("chests/labevil"));
    public static final ResourceLocation RITUALSITE = register(kazs_end_village("chests/ritualsite"));

    private static ResourceLocation register(ResourceLocation pId) {
        if (LOCATIONS.add(pId)) {
            return pId;
        } else {
            throw new IllegalArgumentException(pId + " is already a registered loot table");
        }
    }

    public static ResourceLocation kazs_end_village(String path) {
        return new ResourceLocation("kaz_end_village",path);
    }
}
