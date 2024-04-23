package net.ynov.createnuclear;

import com.simibubi.create.foundation.data.TagGen.CreateTagsProvider;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.level.block.Block;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.tags.CNTag;

public class CreateNucleairRegistrateTags {
    public static void addGenerators() {
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, CreateNucleairRegistrateTags::genBlockTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        CreateTagsProvider<Block> prov = new CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

        prov.tag(CNTag.BlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag)
                .add(CNBlocks.ENRICHED_SOUL_SOIL.get())
        ;

        for (CNTag.BlockTags tag : CNTag.BlockTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }
}
