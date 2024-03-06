package net.ynov.createnuclear.HautingRecipe;

import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.RegisteredObjects;

import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.item.CNItems;

public class CNHauntingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

            BRASS_BELL = CNconvert(() -> Ingredient.of(AllBlocks.PECULIAR_BELL.get()), AllBlocks.HAUNTED_BELL::get),
    ENRICHING_CAMPFIRE = CNconvert(Items.CAMPFIRE, CNBlocks.ENRICHING_CAMPFIRE);

    public CNHauntingRecipeGen(FabricDataOutput generator) {
        super(generator);
    }

    public GeneratedRecipe CNconvert(ItemLike input, ItemLike result) {
        return CNconvert(() -> Ingredient.of(input), () -> result);
    }

    public GeneratedRecipe CNconvert(Supplier<Ingredient> input, Supplier<ItemLike> result) {
        return create(Create.asResource(RegisteredObjects.getKeyOrThrow(result.get()
                                .asItem())
                        .getPath()),
                p -> p.withItemIngredients(input.get())
                        .output(result.get()));
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.HAUNTING;
    }
    public static void register() {}

}
