package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.fabricators_of_create.porting_lib.util.BlockSnapshot;
import io.github.fabricators_of_create.porting_lib.util.NetworkHooks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import net.ynov.createnuclear.multiblock.energy.ReactorOutput;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.tools.HorizontalDirectionalReactorBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ReactorControllerBlock extends HorizontalDirectionalReactorBlock implements IWrenchable, IBE<ReactorControllerBlockEntity> {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    private boolean powered;
    private List<CNIconButton> switchButtons;

    public ReactorControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ASSEMBLED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(ASSEMBLED, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        Item item = player.getItemInHand(handIn).getItem();

        if (CNItems.WELDING_KIT.is(item)) { //Si le weldingKit est dans la main
            if (Boolean.TRUE.equals(state.getValue(ASSEMBLED))) {
                player.sendSystemMessage(Component.literal("Multiblock déjà assemblé").withStyle(ChatFormatting.YELLOW));
                return InteractionResult.SUCCESS;
            }
            player.sendSystemMessage(Component.literal("Analyse multiBlock"));

            var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(worldIn, pos);
            if (result != null) {
                player.sendSystemMessage(Component.literal("MultiBlock assemblé.").withStyle(ChatFormatting.BLUE));
                worldIn.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
            } else {
                player.sendSystemMessage(Component.literal("Erreur dans l'assemblage du multiBlock").withStyle(ChatFormatting.RED));
            }
            return InteractionResult.SUCCESS;
        }


        if (Boolean.FALSE.equals(state.getValue(ASSEMBLED))) {
            player.sendSystemMessage(Component.literal("Multiblock not assembled").withStyle(ChatFormatting.RED));
        }else {
            withBlockEntityDo(worldIn, pos, be -> NetworkHooks.openScreen((ServerPlayer) player, be, be::sendToMenu)); // Ouvre le menu de reactor controller
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;

        withBlockEntityDo(worldIn, pos, be -> ItemHelper.dropContents(worldIn, pos, be.inventory));
        worldIn.removeBlockEntity(pos);

        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(worldIn, pos);
        if (!entity.created)
            return;
        controller.Rotate(state, pos.below(3), worldIn, 0);
        List<? extends Player> players = worldIn.players();
        for (Player p : players) {
            p.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
        }
    }

    public boolean isPowered() {
       return powered; // les variables ne sont pas sauvegarder lors d'un déchargement/rechargement de monde (donc passer par le blockState/ou trouver une autre methode)
    }
    public void setPowered(boolean power) {
        powered = power;
//        worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, power));
    }

    public List<CNIconButton> getSwitchButtons() {
        return switchButtons;
    }

    public void setSwitchButtons(List<CNIconButton> switchButtons) {
        this.switchButtons = switchButtons;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (Boolean.TRUE.equals(state.getValue(ASSEMBLED)))
            return;
        List<? extends Player> players = level.players();
        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        controller.Verify(state, pos, level, players, true);
        for (Player p : players) {
            p.sendSystemMessage(Component.literal("controller is "));
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos);
        if (!entity.created)
            return;
        controller.Rotate(state, pos.below(3), level, 0);
        List<? extends Player> players = level.players();
        for (Player p : players) {
            p.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
        }
    }

    // this is the Function that verifies if the pattern is correct (as a test, we added the energy output)
    public void Verify(BlockState state, BlockPos pos, Level level, List<? extends Player> players, boolean create){
        ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(pos).getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos);
        var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos); // control the pattern
        if (result != null) { // the pattern is correct
            CreateNuclear.LOGGER.info("structure verified, SUCCESS to create multiblock");

            for (Player player : players) {
                if (create && !entity.created)                 {
                    player.sendSystemMessage(Component.literal("WARNING : Reactor Assembled"));
                    level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
                    entity.created = true;
                    entity.destroyed = false;
                }
            }
            return;
        }

        // the pattern is incorrect
        CreateNuclear.LOGGER.info("structure not verified, FAILED to create multiblock");
        for (Player player : players) {
            if (!create && !entity.destroyed)
            {
                player.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
                level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, false));
                entity.created = false;
                entity.destroyed = true;
                Rotate(state, pos.below(3), level, 0);
            }
        }
    }
    public void Rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get())) {
            ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
            ReactorOutputEntity entity = Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos));

            if (entity.getDir() == 1)
                rotation = -rotation;

            if (Boolean.TRUE.equals(state.getValue(ASSEMBLED)) && rotation != 0) { // Starting the energy
                entity.speed = rotation;
                entity.setSpeed2(Math.abs(entity.speed), level, pos.below(3));
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
            } else { // stopping the energy
                entity.setSpeed2(0, level, pos.below(3));
                entity.speed = 0;
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
            }

            CreateNuclear.LOGGER.info("SPEED : " + entity.getSpeed2() + "DIR : " + entity.getDir());
        }
    }

    @Override
    public Class<ReactorControllerBlockEntity> getBlockEntityClass() {
        return ReactorControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorControllerBlockEntity> getBlockEntityType() {
        return CNBlockEntities.REACTOR_CONTROLLER.get();
    }
}
