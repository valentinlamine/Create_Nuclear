package net.ynov.createnuclear.blockentity;

import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputRenderer;
import net.ynov.createnuclear.multiblock.frame.ReactorCasingEntity;
import net.ynov.createnuclear.multiblock.input.ReactorInputEntity;

public class CNBlockEntities {
    public static final BlockEntityEntry<ReactorControllerBlockEntity> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.blockEntity("reactor_controller", ReactorControllerBlockEntity::new)
            .validBlocks(CNBlocks.REACTOR_CONTROLLER)
            .register();

    public static final BlockEntityEntry<ReactorInputEntity> REACTOR_INPUT =
            CreateNuclear.REGISTRATE.blockEntity("test", ReactorInputEntity::new)
                    .validBlocks(CNBlocks.REACTOR_INPUT)
                    .register();

    public static final BlockEntityEntry<ReactorOutputEntity> REACTOR_OUTPUT =
            CreateNuclear.REGISTRATE.blockEntity("reactor_output", ReactorOutputEntity::new)
                    .validBlocks(CNBlocks.REACTOR_OUTPUT)
                    .renderer(() -> ReactorOutputRenderer::new)
                    .register();

    public static final BlockEntityEntry<ReactorCasingEntity> REACTOR_CASING =
            CreateNuclear.REGISTRATE.blockEntity("reactor_casing", ReactorCasingEntity::new)
                    .validBlocks(CNBlocks.REACTOR_CASING)
                    .register();


    public static void register() {
    }
}
