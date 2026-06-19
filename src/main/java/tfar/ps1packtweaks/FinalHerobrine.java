package tfar.ps1packtweaks;

import corgitaco.enhancedcelestials.server.commands.LunarForecastCommand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import tfar.ps1packtweaks.client.PS1PackTweaksClient;
import tfar.ps1packtweaks.client.WorldLocker;
import tfar.ps1packtweaks.compat.ModIntegration;
import tfar.ps1packtweaks.entity.FinalHerobrineEntity;
import tfar.ps1packtweaks.network.ForgePacketHandler;
import tfar.ps1packtweaks.network.client.S2CEventPacket;
import vazkii.quark.content.building.entity.GlassItemFrame;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FinalHerobrine extends SavedData {

    private final ServerLevel level;
    public Stage herobrineStage = Stage.PREP;

    @Nullable
    FinalHerobrineEntity herobrine;

    @Nullable
    UUID herobrineUUID;
    BlockPos fire = BlockPos.ZERO;

    int tick;

    boolean hasPurgedBloodMoons;


    public FinalHerobrine(ServerLevel level) {
        this.level = level;
    }

    public void tick() {
        if (herobrineStage != Stage.PREP && herobrineStage != Stage.END) {
            ++this.tick;

            if (herobrineUUID != null && herobrine == null) {
                herobrine = (FinalHerobrineEntity) level.getEntity(herobrineUUID);
            }

            switch (herobrineStage) {
                case START -> {

                }
            }

            if (tick >= herobrineStage.length) {
                advanceStage();
            } else if (tick % 100 == 0) {
                setDirty();
            }
        }
    }

    private static void noOp() {
        //[MODS]

        //[CONFIGS]
        //config>fancymenu>customization>Title Screen Low Res.txt
        //- Change...
        //
        //customization {
        //  restart_on_load = false
        //  name = final_animation
        //  action = animatebackground
        //}
        //
        //...to
        //
        //customization {
        //  restart_on_load = false
        //  name = reversed
        //  action = animatebackground
        //}
        //
        //---------------------------------------------------------------------------------
        //
        //config>fancymenu>customization>LoadingWorld.txt
        //- Change...
        //
        //customization {
        //  orientation = top-left
        //  name = loading
        //  x = 0
        //  width = %guiwidth%
        //  actionid = 34a9efa1-e489-4830-9725-95049dd6add81729022291008
        //  action = addanimation
        //  y = 0
        //  height = %guiheight%
        //}
        //
        //...to
        //
        //customization {
        //  orientation = top-left
        //  name = loadingreversed
        //  x = 0
        //  width = %guiwidth%
        //  actionid = 34a9efa1-e489-4830-9725-95049dd6add81729022291008
        //  action = addanimation
        //  y = 0
        //  height = %guiheight%
        //}
        //
        //---------------------------------------------------------------------------------
        //
        //config>fancymenu>customization>Progress.txt
        //- Change...
        //
        //
        //customization {
        //  orientation = top-left
        //  name = loading
        //  x = 0
        //  width = %guiwidth%
        //  actionid = 2f3a0845-5349-4d42-bbd3-fb4f57ecd5aa1729022375927
        //  action = addanimation
        //  y = 0
        //  height = %guiheight%
        //}
        //
        //...to
        //
        //customization {
        //  orientation = top-left
        //  name = loadingreversed
        //  x = 0
        //  width = %guiwidth%
        //  actionid = 2f3a0845-5349-4d42-bbd3-fb4f57ecd5aa1729022375927
        //  action = addanimation
        //  y = 0
        //  height = %guiheight%
        //}

    }

    void advanceStage() {
        herobrineStage = Stage.values()[herobrineStage.ordinal()+1];
        tick = 0;

        switch (herobrineStage) {
            case SPLIT -> {
                herobrine.setDiscardFriction(true);
                herobrine.setDeltaMovement(0,-.03125,0);
                herobrine.playSound(SoundEvents.BEACON_DEACTIVATE,1,1);
            }

            case VIDEO -> {
                herobrine.discard();
                herobrine = null;
                herobrineUUID = null;
                level.getServer().getPlayerList().getPlayers().forEach(player -> {
                    ForgePacketHandler.sendToClient(S2CEventPacket.PLAY_VIDEO, player);
                    player.setInvulnerable(true);
                });
            }

            case END -> {
                level.getServer().getPlayerList().getPlayers().forEach(player -> {
                    player.setInvulnerable(false);
                    S2CEventPacket.END_FINAL_HEROBRINE.send(player);
                });
                level.setBlockAndUpdate(fire,Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION,4));
                level.setWeatherParameters(6000, 0, false, false);


                BlockEntity blockEntity = level.getBlockEntity(fire);
                if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                    signBlockEntity.setMessage(1, new TextComponent("Thank You."));
                }

                setDirty();

                MinecraftServer server = level.getServer();

                WorldLocker.unlock(WorldLocker.PURIFIED);
                if (!(level.getServer() instanceof DedicatedServer)) {
                    PS1PackTweaksClient.deleteSpecialWorlds();
                    PS1PackTweaksClient.modifyFancyMenuSettings();
                    PS1PackTweaksClient.disableResourcePacks();
                }

                File serverDirectory = server.getServerDirectory();

                try (InputStream resource = FinalHerobrine.class.getClassLoader()
                        .getResourceAsStream("config_changes/diamond.json")){
                    Files.copy(resource, serverDirectory.toPath().resolve("global_packs")
                            .resolve("required_data").resolve("FishyBusiness").resolve("data").resolve("fishingreal")
                            .resolve("fishing").resolve("diamond.json"), StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //replace midnightlurker config
                try (InputStream resource = FinalHerobrine.class.getClassLoader()
                        .getResourceAsStream("config_changes/midnightlurkerconfig.json")){
                    Files.copy(resource, serverDirectory.toPath().resolve("config")
                            .resolve("midnightlurkerconfig.json"), StandardCopyOption.REPLACE_EXISTING);
                }catch (Exception e) {
                    e.printStackTrace();
                }

                //disable bloodmoons
                try (InputStream resource = FinalHerobrine.class.getClassLoader()
                        .getResourceAsStream("config_changes/enhancedcelestials-blood_moon.json")){
                    Files.copy(resource, serverDirectory.toPath().resolve("config")
                                    .resolve(ModIntegration.enhancedcelestials.name())
                                    .resolve("minecraft").resolve("overworld").resolve("lunar")
                                    .resolve("events")
                            .resolve("enhancedcelestials-blood_moon.json"), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try (InputStream resource = FinalHerobrine.class.getClassLoader()
                        .getResourceAsStream("config_changes/enhancedcelestials-super_blood_moon.json")){
                    Files.copy(resource, serverDirectory.toPath().resolve("config")
                            .resolve(ModIntegration.enhancedcelestials.name())
                            .resolve("minecraft").resolve("overworld").resolve("lunar")
                            .resolve("events")
                            .resolve("enhancedcelestials-super_blood_moon.json"), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //mcred.png - Deleted
                //steve2.png - Deleted
                //steve3.png - Deleted
                    try {
                        for (String s : new String[]{"mcred.png","steve2.png","steve3.png"}) {
                            FileUtils.delete(serverDirectory.toPath().resolve(s).toFile());
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                try {
                    Path datapackLocation = serverDirectory.toPath().resolve("global_packs").resolve("required_data");
                    for (String s : new String[]{"LidnightMurker","Fuck the Fog"}) {
                        FileUtils.deleteDirectory(datapackLocation.resolve(s).toFile());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //handle server configs

                Path savePath = serverDirectory.toPath().resolve("saves");

                File[] saveFiles = savePath.toFile().listFiles();

                if (saveFiles != null) {
                    for (File saveFile : saveFiles) {
                        Path configFolder = saveFile.toPath().resolve("serverconfig");

                        try (InputStream resource = FinalHerobrine.class.getClassLoader()
                                .getResourceAsStream("config_changes/server/ps1packtweaks-server.toml")){
                            Files.copy(resource,configFolder
                                    .resolve("ps1packtweaks-server.toml"), StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //manually save
                server.saveAllChunks(false, true, false);
                //crash game
                level.addFreshEntity(null);
            }
        }
        setDirty();
    }

    public void reset() {
        hasPurgedBloodMoons = false;
        herobrineStage = Stage.PREP;
        if (herobrine != null) {
            herobrine.discard();
        }
        herobrine = null;
        herobrineUUID = null;

        WorldLocker.setCursed();

        setDirty();
    }


    public enum Stage {
        PREP(-1),
        START(200),
        SPLIT(500),
        VIDEO(94 * 20),
        END(-1);
        public final int length;

        Stage(int length) {

            this.length = length;
        }
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putInt("Tick",tick);
        pCompoundTag.putString("Stage",herobrineStage.name());
        if (herobrine != null) {
            pCompoundTag.putUUID("Herobrine",herobrine.getUUID());
        }

        pCompoundTag.put("Fire",NbtUtils.writeBlockPos(fire));

        pCompoundTag.putBoolean("hasPurgedBloodMoons",hasPurgedBloodMoons);

        return pCompoundTag;
    }

    public static FinalHerobrine loadStatic(ServerLevel level, CompoundTag tag) {
        FinalHerobrine fina = new FinalHerobrine(level);
        fina.tick = tag.getInt("Tick");
        fina.herobrineStage = Stage.valueOf(tag.getString("Stage"));
        if (tag.hasUUID("Herobrine")) {
            fina.herobrineUUID = tag.getUUID("Herobrine");
        }

        fina.fire = NbtUtils.readBlockPos(tag.getCompound("Fire"));

        fina.hasPurgedBloodMoons = tag.getBoolean("hasPurgedBloodMoons");

        return fina;
    }

    ///////////////////events

    public static void levelTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PS1PackTweaks.finalHerobrine.tick();
        }
    }

    public static void useFlintAndSteel(PlayerInteractEvent.RightClickBlock event){
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        BlockPos pos = event.getPos();
        Level level = player.level;
        if (!level.isClientSide && level.dimension() == Level.OVERWORLD && isShrineAllowed((ServerLevel) level, (ServerPlayer) player)) {

            ItemStack stack = player.getItemInHand(hand);
            BlockState state = level.getBlockState(pos);
            if (stack.is(Items.FLINT_AND_STEEL)&& state.is(Blocks.BEDROCK)) {
                if (PS1PackTweaks.finalHerobrine.herobrineStage != Stage.PREP) {
                    player.displayClientMessage(new TextComponent("Herobrine has already been summoned!"),false);
                    return;
                }

                if (hasCompleteStructure(player, (ServerLevel) level,pos)) {
                    //player.displayClientMessage(new TextComponent("Correct Structure"),false);
                    FinalHerobrineEntity herobrine = (FinalHerobrineEntity) Init.ModEntityTypes.FINAL_HEROBRINE.spawn((ServerLevel) level,null,null,pos.above(), MobSpawnType.EVENT,false,false);
                    if (herobrine != null) {
                        PS1PackTweaks.finalHerobrine.begin(herobrine,pos.above());
                    }
                }
            }
        }
    }

    static boolean isShrineAllowed(ServerLevel level, ServerPlayer player) {
        MinecraftServer server = level.getServer();
        if (!server.isSingleplayerOwner(player.getGameProfile())) {
            return PS1PackTweaksConfig.SERVER.allowHerobrineShrine.get();
        }
        return true;
    }

    void begin(FinalHerobrineEntity herobrine,BlockPos firePos) {
        this.herobrine = herobrine;
        this.fire = firePos;
        tick = 0;
        herobrineStage = Stage.START;

        herobrine.noClip = true;
        herobrine.setInvulnerable(true);
        herobrine.setNoGravity(true);

        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.moveTo(herobrine.position());
        lightningBolt.setVisualOnly(true);
        level.addFreshEntity(lightningBolt);
        level.setWeatherParameters(0, 6000, true, true);
        herobrine.playSound(SoundEvents.BEACON_AMBIENT,1,1);
        level.getServer().getPlayerList().getPlayers().forEach(S2CEventPacket.START_FINAL_HEROBRINE::send);
        setDirty();
    }

    public static boolean hasCompleteStructure(Player player, ServerLevel level, BlockPos bedrockPos) {

        for(Direction direction : new Direction[]{Direction.NORTH,Direction.EAST,Direction.SOUTH,Direction.WEST}) {
            BlockPos torchPos = bedrockPos.relative(direction);
            if (!level.getBlockState(torchPos).is(Blocks.SOUL_TORCH)) {
                player.displayClientMessage(new TextComponent("Missing soul torch(es)"),false);
                return false;
            }
        }


        BlockPos belowPos = bedrockPos.below();

        for (int z = -1; z <=1;z++) {
            for (int x = -1; x <=1;x++) {
                BlockPos check = belowPos.offset(x,0,z);
                BlockPos belowCheck = check.below();


                BlockState state = level.getBlockState(check);
                BlockState belowState = level.getBlockState(belowCheck);
                if (x == 0 && z== 0) {
                    if (!state.is(Blocks.SOUL_SAND)){
                        player.displayClientMessage(new TextComponent("Missing soul sand below bedrock"),false);
                        return false;
                    }

                    if (!belowState.is(Blocks.CRAFTING_TABLE)) {
                        player.displayClientMessage(new TextComponent("Missing crafting table"),false);
                        return false;
                    }

                } else {
                    if (!state.is(Blocks.DIAMOND_BLOCK)) {
                        player.displayClientMessage(new TextComponent("Missing diamond block(s)"),false);
                        return false;
                    }
                    if (!belowState.is(Blocks.POLISHED_ANDESITE)) {
                        player.displayClientMessage(new TextComponent("Missing polished andesite"),false);
                        return false;
                    }
                }
            }
        }

        BlockPos bottomPos = belowPos.below();

        BlockPos northEastCorner = bottomPos.offset(3,0,-3);
        BlockPos southEastCorner = bottomPos.offset(3,0,3);
        BlockPos southWestCorner = bottomPos.offset(-3,0,3);
        BlockPos northWestCorner = bottomPos.offset(-3,0,-3);

        BlockPos[] corners = new BlockPos[]{northEastCorner,northWestCorner,southEastCorner,southWestCorner};

        for (BlockPos pos : corners) {
            if (!level.getBlockState(pos).is(Blocks.POLISHED_ANDESITE)) {
                player.displayClientMessage(new TextComponent("Missing polished andesite"),false);
                return false;
            }
        }


            List<GlassItemFrame> foundFrames = new ArrayList<>();


        for (BlockPos pos : corners) {
            List<GlassItemFrame> frames = level.getEntitiesOfClass(GlassItemFrame.class,new AABB(pos.above()));
            if (frames.size() == 1) {
                foundFrames.add(frames.get(0));
            }
        }

        if (foundFrames.size() == 4) {
            List<ItemStack> stacks = foundFrames.stream().map(ItemFrame::getItem).toList();
            ItemStack head = ItemStack.EMPTY;
            ItemStack chestplate = ItemStack.EMPTY;
            ItemStack leggings = ItemStack.EMPTY;
            ItemStack boots = ItemStack.EMPTY;

            for (ItemStack stack : stacks) {
                if (stack.isEmpty()) {
                    player.displayClientMessage(new TextComponent("Missing items"),false);
                    return false;
                } else if (stack.is(Items.PLAYER_HEAD)) {
                    head = stack;
                } else if (stack.is(Items.LEATHER_CHESTPLATE)) {
                    chestplate = stack;
                } else if (stack.is(Items.LEATHER_LEGGINGS)) {
                    leggings = stack;
                } else if (stack.is(Items.LEATHER_BOOTS)) {
                    boots = stack;
                } else {
                    player.displayClientMessage(new TextComponent("Incorrect items"),false);
                    return false;
                }
            }

            if (head.isEmpty() || chestplate.isEmpty() || leggings.isEmpty() || boots.isEmpty()) {
                player.displayClientMessage(new TextComponent("Incorrect items"),false);
                return false;
            }

            //HEAD - /give @a player_head{AttributeModifiers:[{AttributeName:"generic.follow_range",Slot:head,UUID:[I;-125815,12854,194052,-25708],Name:1757983967781}],HideFlags:2}


            //LEGS (blue) - /give @a leather_leggings{AttributeModifiers:[{AttributeName:"generic.follow_range",Slot:legs,UUID:[I;-125815,7154,194052,-14308],Name:1757983525326}],HideFlags:2}
            //
            //FEET (light gray) - /give @a leather_boots{AttributeModifiers:[{AttributeName:"generic.follow_range",Slot:feet,UUID:[I;-125815,15254,194052,-30508],Name:1757984094289}],HideFlags:2}
            //
            //BODY (cyan) - /give @a leather_chestplate{AttributeModifiers:[{AttributeName:"generic.follow_range",Slot:chest,UUID:[I;-125815,10154,194052,-20308],Name:1757983744404}],HideFlags:2}


            if (!ItemStack.isSameItemSameTags(CORRECT_HEAD,head)) {
                player.displayClientMessage(new TextComponent("Incorrect head data"),false);
                return false;
            }

            if (!ItemStack.isSameItemSameTags(CORRECT_CHESTPLATE,chestplate)) {
                player.displayClientMessage(new TextComponent("Incorrect chestplate data"),false);
                return false;
            }

            if (!ItemStack.isSameItemSameTags(CORRECT_LEGGINGS,leggings)) {
                player.displayClientMessage(new TextComponent("Incorrect leggings data"),false);
                return false;
            }

            if (!ItemStack.isSameItemSameTags(CORRECT_BOOTS,boots)) {
                player.displayClientMessage(new TextComponent("Incorrect boots data"),false);
                return false;
            }

        } else {
            player.displayClientMessage(new TextComponent("Incorrect number of frames: "+foundFrames.size()+", expected 4"),false);
            return false;
        }

        for (GlassItemFrame frame : foundFrames) {
            frame.setItem(ItemStack.EMPTY);
        }

        return true;
    }

    public static final ItemStack CORRECT_HEAD = Items.PLAYER_HEAD.getDefaultInstance();
    public static final ItemStack CORRECT_CHESTPLATE = Items.LEATHER_CHESTPLATE.getDefaultInstance();
    public static final ItemStack CORRECT_LEGGINGS = Items.LEATHER_LEGGINGS.getDefaultInstance();
    public static final ItemStack CORRECT_BOOTS = Items.LEATHER_BOOTS.getDefaultInstance();

    static {
        appendSpecialData(CORRECT_HEAD, 0);
        appendSpecialData(CORRECT_CHESTPLATE, 0x169c9c);
        appendSpecialData(CORRECT_LEGGINGS, 0x3c44aa);
        appendSpecialData(CORRECT_BOOTS, 0x9d9d97);
    }

    static void appendSpecialData(ItemStack stack, int color) {
        stack.enchant(Enchantments.BINDING_CURSE,1);
        stack.setRepairCost(1);

        if (!stack.is(Items.PLAYER_HEAD)) {
            CompoundTag tag = stack.getTag();
            CompoundTag display = new CompoundTag();
            display.putInt("color",color);
            tag.put("display",display);
        }
    }
}
