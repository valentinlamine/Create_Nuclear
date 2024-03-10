package net.ynov.createnuclear;

import com.simibubi.create.AllBlocks;
import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.minecraft.world.level.block.Blocks;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.CNListMultiblock;

import net.ynov.createnuclear.multiblock.MultiBlockManagerBeta;

import static net.ynov.createnuclear.multiblock.CNListMultiblock.REACTOR;

public class CNMultiblock {
    public static final MultiBlockManagerBeta<CNListMultiblock> REGISTRATE_MULTIBLOCK = new MultiBlockManagerBeta<>();
    private static final String AAAAA = "AAAAA";
    private static final String AABAA = "AABAA";
    private static final String ADADA = "ADADA";
    private static final String BACAB = "BACAB";

    static {
        REGISTRATE_MULTIBLOCK.register("createnuclear:reactor",
                REACTOR,
                SimpleMultiBlockAislePatternBuilder.start()
                    .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle("AAOAA", ADADA, BACAB, ADADA, "AA*AA")
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                    .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                    .where('B', a -> a.getState().is(CNBlocks.REACTOR_MAIN_FRAME.get()))
                    .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                    .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLING_FRAME.get()))
                    .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                    .where('O', a -> a.getState().is(AllBlocks.CLUTCH.get()))
                .build()
        );

    }
}
