package tfar.ps1packtweaks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SerializableUUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import tfar.ps1packtweaks.entity.HerobrineEntity;
import vazkii.quark.content.building.entity.GlassItemFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FinalHerobrine {



    public static void useFlintAndSteel(PlayerInteractEvent.RightClickBlock event){
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        BlockPos pos = event.getPos();
        Level level = player.level;
        if (!level.isClientSide && level.dimension() == Level.OVERWORLD) {
            ItemStack stack = player.getItemInHand(hand);
            BlockState state = level.getBlockState(pos);
            if (stack.is(Items.FLINT_AND_STEEL)&& state.is(Blocks.BEDROCK)) {
                if (hasCompleteStructure(player, (ServerLevel) level,pos)) {
                    player.displayClientMessage(new TextComponent("Correct Structure"),false);
                    HerobrineEntity herobrine = (HerobrineEntity) Init.ModEntityTypes.HEROBRINE.spawn((ServerLevel) level,null,null,pos, MobSpawnType.EVENT,false,false);
                    if (herobrine != null) {
                        herobrine.setInvulnerable(true);
                    }
                }
            }
        }
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


        return true;
    }

    public static final ItemStack CORRECT_HEAD = Items.PLAYER_HEAD.getDefaultInstance();
    public static final ItemStack CORRECT_CHESTPLATE = Items.LEATHER_CHESTPLATE.getDefaultInstance();
    public static final ItemStack CORRECT_LEGGINGS = Items.LEATHER_LEGGINGS.getDefaultInstance();
    public static final ItemStack CORRECT_BOOTS = Items.LEATHER_BOOTS.getDefaultInstance();

    static {
        appendSpecialData(CORRECT_HEAD,"1757983967781",new int[]{-125815,12854,194052,-25708},EquipmentSlot.HEAD,0);
        appendSpecialData(CORRECT_CHESTPLATE,"1757983744404",new int[]{-125815,10154,194052,-20308},EquipmentSlot.CHEST,1481884);
        appendSpecialData(CORRECT_LEGGINGS,"1757983525326",new int[]{-125815,7154,194052,-14308},EquipmentSlot.LEGS,3949738);
        appendSpecialData(CORRECT_BOOTS,"1757984094289",new int[]{-125815,15254,194052,-30508},EquipmentSlot.FEET,10329495);
    }

    static void appendSpecialData(ItemStack stack,String attrName,int[] uuid,EquipmentSlot slot,int color) {
        stack.addAttributeModifier(Attributes.FOLLOW_RANGE,new AttributeModifier(SerializableUUID.uuidFromIntArray(uuid),
                attrName,0, AttributeModifier.Operation.ADDITION), slot);

        stack.hideTooltipPart(ItemStack.TooltipPart.MODIFIERS);

        ListTag attributeModifiers = stack.getTag().getList("AttributeModifiers", CompoundTag.TAG_COMPOUND);
        CompoundTag first = attributeModifiers.getCompound(0);
        first.remove("Amount");
        first.remove("Operation");
        first.putString("AttributeName","generic.follow_range");
        if (!stack.is(Items.PLAYER_HEAD)) {
            CompoundTag tag = stack.getTag();
            CompoundTag display = new CompoundTag();
            display.putInt("color",color);
            tag.put("display",display);
        }
    }
}
