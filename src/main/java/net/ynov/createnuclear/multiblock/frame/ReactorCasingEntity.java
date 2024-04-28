package net.ynov.createnuclear.multiblock.frame;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.fabricators_of_create.porting_lib.block.CustomRenderBoundingBoxBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReactorCasingEntity extends SmartBlockEntity implements IMultiBlockEntityContainer.Inventory, IHaveGoggleInformation, CustomRenderBoundingBoxBlockEntity, SidedStorageBlockEntity {

    protected BlockPos controller;
    protected int width;
    protected int height;
    protected boolean updateConnectivity;
    protected BlockPos lastKnownPos;

    private static final int MAX_HEIGHT = 7;
    private static final int MAX_WIDTH = 3;//5

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;

    public ReactorCasingEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        updateConnectivity = false;
        height = 1;
        width = 1;
    }



    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide()) return;
        if (!isController()) return;
        ConnectivityHandler.formMulti(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync) sendData();
        }

        if (lastKnownPos == null) lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }

        if (updateConnectivity) updateConnectivity();
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null
                || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY()
                && worldPosition.getZ() == controller.getZ();
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level.isClientSide) invalidateRenderBoundingBox();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ReactorCasingEntity getControllerBE() {
        if (isController()) return this;
        BlockEntity blockEntity = level.getBlockEntity(controller);
        if (blockEntity instanceof ReactorCasingEntity) return (ReactorCasingEntity) blockEntity;
        return null;
    }

    @Override
    public void removeController(boolean keepContents) {
        if (level.isClientSide()) return;
        updateConnectivity = false;
        if (!keepContents) return;
        controller = null;
        width = height = 1;

        BlockState state = getBlockState();
        if (ReactorCasing.isCasing(state)) {
            state = state.setValue(ReactorCasing.BOTTOM, true);
            state = state.setValue(ReactorCasing.TOP, true);
            state = state.setValue(ReactorCasing.SHAPE, ReactorCasing.CNShape.NONE);
            getLevel().setBlock(worldPosition, state, 23);
        }

        setChanged();
        sendData();
    }

    public void sendDataImmediately() {
        syncCooldown = 0;
        queuedSync = false;
        sendData();
    }

    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }

    @Override
    public void setController(BlockPos pos) {
        if (level.isClientSide && !isVirtual()) return;
        if (controller.equals(this.controller)) return;
        this.controller = controller;
        setChanged();
        sendData();
    }

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        if (isController()) return super.createRenderBoundingBox().expandTowards(width - 1, height-1, width-1 );
        else return super.createRenderBoundingBox();
    }


    /*@Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        ReactorCasingEntity controllerBE = getControllerBE();
        if (controllerBE == null) return false;
        return controllerBE.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }*/

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public int getTotalReactorSize() {
        return width * height * width;
    }

    public static int getMaxHeightSize() {
        return MAX_HEIGHT;
    }

    public static int getMaxWidthSize() {
        return MAX_WIDTH;
    }

    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    public void queueConnectivityUpdate() {
        updateConnectivity = true;
    }


    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (ReactorCasing.isCasing(state)) {
            state = state.setValue(ReactorCasing.BOTTOM, getController().getY() == getBlockPos().getY());
            state = state.setValue(ReactorCasing.TOP, getController().getY() + height - 1 == getBlockPos().getY());
            level.setBlock(getBlockPos(), state, 6);
        }

        setChanged();
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y) return getHeight();
        return getWidth();
    }

    @Override
    public int getMaxWidth() {
        return MAX_WIDTH;
    }

    @Override
    public int getHeight() {
        return MAX_HEIGHT;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return MAX_WIDTH;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public @Nullable Storage<ItemVariant> getItemStorage(Direction side) {
        return SidedStorageBlockEntity.super.getItemStorage(side);
    }
}
