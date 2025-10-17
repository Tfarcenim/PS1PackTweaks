package tfar.ps1packtweaks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import vazkii.quark.content.building.entity.GlassItemFrame;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(PS1PackTweaks.MOD_ID)
                .requires(sourceStack -> sourceStack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("create_shrine")
                        .executes(ModCommands::createShrine)
                )
                .then(Commands.literal("reset")
                        .executes(ModCommands::reset)
                )
        );
    }

    static int reset(CommandContext<CommandSourceStack> context) {
        PS1PackTweaks.finalHerobrine.reset();
        return 1;

    }

    static int createShrine(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();

        ServerLevel level = player.getLevel();

        if (level.dimension() != Level.OVERWORLD) {
            source.sendFailure(new TextComponent("Must be in overworld!"));
            return 0;
        }

        BlockPos pos = player.blockPosition().below();
        if (pos.getY() < (level.getMinBuildHeight() + 4) || pos.getY() > (level.getMaxBuildHeight()+1)) {
            source.sendFailure(new TextComponent("Must be in bounds"));
            return 0;
        }

        level.setBlockAndUpdate(pos, Blocks.BEDROCK.defaultBlockState());


        for(Direction direction : new Direction[]{Direction.NORTH,Direction.EAST,Direction.SOUTH,Direction.WEST}) {
            BlockPos torchPos = pos.relative(direction);
            level.setBlockAndUpdate(torchPos,Blocks.SOUL_TORCH.defaultBlockState());
        }


        BlockPos below = pos.below();
        for (int z = -1; z <=1;z++) {
            for (int x = -1; x <=1;x++) {
                if (x == 0 && z == 0) {
                    level.setBlockAndUpdate(below,Blocks.SOUL_SAND.defaultBlockState());
                    level.setBlockAndUpdate(below.below(),Blocks.CRAFTING_TABLE.defaultBlockState());
                } else {
                    level.setBlockAndUpdate(below.offset(x,0,z),Blocks.DIAMOND_BLOCK.defaultBlockState());
                    level.setBlockAndUpdate(below.below().offset(x,0,z),Blocks.POLISHED_ANDESITE.defaultBlockState());
                }
            }
        }


        BlockPos bottomPos = below.below();

        BlockPos northEastCorner = bottomPos.offset(3,0,-3);
        BlockPos southEastCorner = bottomPos.offset(3,0,3);
        BlockPos southWestCorner = bottomPos.offset(-3,0,3);
        BlockPos northWestCorner = bottomPos.offset(-3,0,-3);

        BlockPos[] corners = new BlockPos[]{northEastCorner,northWestCorner,southEastCorner,southWestCorner};

        for (BlockPos corner : corners) {
            level.setBlockAndUpdate(corner,Blocks.POLISHED_ANDESITE.defaultBlockState());
        }

        GlassItemFrame glassItemFrameNE = new GlassItemFrame(level,northEastCorner.above(), Direction.UP);
        glassItemFrameNE.setItem(FinalHerobrine.CORRECT_HEAD);
        level.addFreshEntity(glassItemFrameNE);

        GlassItemFrame glassItemFrameSE = new GlassItemFrame(level,southEastCorner.above(), Direction.UP);
        glassItemFrameSE.setItem(FinalHerobrine.CORRECT_CHESTPLATE);
        level.addFreshEntity(glassItemFrameSE);

        GlassItemFrame glassItemFrameSW = new GlassItemFrame(level,southWestCorner.above(), Direction.UP);
        glassItemFrameSW.setItem(FinalHerobrine.CORRECT_LEGGINGS);
        level.addFreshEntity(glassItemFrameSW);

        GlassItemFrame glassItemFrameNW = new GlassItemFrame(level,northWestCorner.above(), Direction.UP);
        glassItemFrameNW.setItem(FinalHerobrine.CORRECT_BOOTS);
        level.addFreshEntity(glassItemFrameNW);


        return 1;
    }

}
