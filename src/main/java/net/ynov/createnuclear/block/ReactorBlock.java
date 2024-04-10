package net.ynov.createnuclear.block;

import io.github.fabricators_of_create.porting_lib.event.common.GrindstoneEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.apache.logging.log4j.Level.getLevel;

public class ReactorBlock extends Block {
    public ReactorBlock(Properties properties) {
        super(properties);
    }

    @Override // Called when the block is placed on the world
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, true);
    }

    @Override // called when the player destroys the block, with or without a tool
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, false);
    }

    public ReactorController FindController(BlockPos blockPos, Level level, List<? extends Player> players, boolean first){ // Function that checks the surrounding blocks in order
        BlockPos newBlock;                                                   // to find the controller and verify the pattern
        Vec3i pos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for (int y = pos.getY()-3; y != pos.getY()+4; y+=1) {
            for (int x = pos.getX()-5; x != pos.getX()+5; x+=1) {
                for (int z = pos.getZ()-5; z != pos.getZ()+5; z+=1) {
                    newBlock = new BlockPos(x, y, z);
                    if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())) { // verifying the pattern
                        CreateNuclear.LOGGER.info("ReactorController FOUND!!!!!!!!!!: ");      // from the controller
                        ReactorController controller = (ReactorController) level.getBlockState(newBlock).getBlock();
                        controller.Verify(newBlock, level, players, first);
                        return controller;
                    }
                    //else CreateNuclear.LOGGER.info("newBlock: " + level.getBlockState(newBlock).getBlock());
                }
            }
        }
        return null;
    }
}
