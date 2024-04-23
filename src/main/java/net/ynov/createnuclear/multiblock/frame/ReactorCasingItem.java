package net.ynov.createnuclear.multiblock.frame;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;


public class ReactorCasingItem extends BlockItem {
    public ReactorCasingItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Internal
    public static boolean IS_PLACING_NBT = false;

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        IS_PLACING_NBT = ReactorCasingItem.checkPlacingNbt(ctx);
        InteractionResult initialResult = super.place(ctx);
        IS_PLACING_NBT = false;
        if (!initialResult.consumesAction()) return initialResult;
        tryMultiPlace(ctx);
        return initialResult;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        MinecraftServer minecraftServer = level.getServer();
        if (minecraftServer == null) return false;
        CompoundTag nbt = stack.getTagElement("BlockEntityTag");
        if (nbt != null) {
            nbt.remove("Controller");
            nbt.remove("Size");
            nbt.remove("Height");
        }

        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    private void tryMultiPlace(BlockPlaceContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null) return;
        if (player.isShiftKeyDown()) return;
        Direction face = ctx.getClickedFace();
        if (!face.getAxis().isVertical()) return;
        ItemStack stack = ctx.getItemInHand();
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockPos placedOnPos = pos.relative(face.getOpposite());
        BlockState placedOnState = world.getBlockState(placedOnPos);

        if (!ReactorCasing.isCasing(placedOnState)) return;
        ReactorCasingEntity reactorAt = ConnectivityHandler.partAt(CNBlockEntities.REACTOR_CASING.get(), world, placedOnPos);

        if (reactorAt == null) return;

        int width = 5;
    }

    public static boolean checkPlacingNbt(BlockPlaceContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        return BlockItem.getBlockEntityData(stack) != null;
    }
}
