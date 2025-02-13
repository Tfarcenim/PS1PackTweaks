package tfar.ps1packtweaks;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PS1TweaksConfig {

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();

        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Server {
        public final ForgeConfigSpec.DoubleValue barnacleHealth;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("tweaks");
            builder.push("barnacle");
            barnacleHealth = builder.defineInRange("health",40, 1, 1023.);
            builder.pop();
            builder.pop();
        }
    }

    public static class Client {
        Client(ForgeConfigSpec.Builder builder) {

        }
    }

}
