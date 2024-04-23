package net.ynov.createnuclear.multiblock;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.multiblock.frame.ReactorCasing;

public class ReactorGenerator extends SpecialBlockStateGen {
    private String prefix;

    public ReactorGenerator(String prefix) {
        this.prefix = prefix;
    }
    public ReactorGenerator() {
        this("");
    }

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        Boolean top = state.getValue(ReactorCasing.TOP);
        Boolean bottom = state.getValue(ReactorCasing.BOTTOM);
        ReactorCasing.CNShape shape = state.getValue(ReactorCasing.SHAPE);

        String shapeName = "middle";
        if (top && bottom) shapeName = "single";
        else if (top) shapeName = "top";
        else if (bottom) shapeName = "bottom";

        String modelName = shapeName + (shape == ReactorCasing.CNShape.NONE ? "" : "_" + shape.getSerializedName());

        if (!prefix.isEmpty()) return prov.models()
                .withExistingParent(prefix + modelName, prov.modLoc("block/reactor/bloc_" + modelName))
                .texture("0", prov.modLoc("block/" + prefix + "casing"))
                .texture("1", prov.modLoc("block/" + prefix + "reactor"))
                .texture("3", prov.modLoc("block/" + prefix + "reactor_window"))
                .texture("4", prov.modLoc("block/" + prefix + "casing"))
                .texture("5", prov.modLoc("block/" + prefix + "reactor_window_single"))
                .texture("particle", prov.modLoc("block/" + prefix + "reactor"));



        return AssetLookup.partialBaseModel(ctx, prov, modelName);
    }
}
