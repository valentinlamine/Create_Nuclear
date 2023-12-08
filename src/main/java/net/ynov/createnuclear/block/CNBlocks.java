package net.ynov.createnuclear.block;

import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.groups.CNGroup;
import net.ynov.createnuclear.tools.UraniumFireBlock;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.minecraft.world.level.block.Blocks.litBlockEmission;

public class CNBlocks {

    static {
        CreateNuclear.REGISTRATE.useCreativeTab(CNGroup.MAIN_KEY);
    }

    public static final BlockEntry<Block> DEEPSLATE_URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("deepslate_uranium_ore", Block::new)
                    .initialProperties(SharedProperties::netheriteMetal)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("uranium_ore", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> RAW_URANIUM_BLOCK =
            CreateNuclear.REGISTRATE.block("raw_uranium_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> ENRICHED_SOUL_SOIL =
            CreateNuclear.REGISTRATE.block("enriched_soul_soil", Block::new)
                    .initialProperties(CNBlocks::SOUL_SOIL)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final Block ENRICHING_FIRE = registerBlockNoItem("enriching_fire", new UraniumFireBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollission().instabreak().lightLevel(state -> 15).sound(SoundType.MOSS).pushReaction(PushReaction.DESTROY)));
    public static final Block ENRICHING_CAMPFIRE = registerBlock("enriching_campfire", new CampfireBlock(false, 5, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).lightLevel(litBlockEmission(10)).noOcclusion().ignitedByLava()));


    public static Block SOUL_SOIL() { return Blocks.SOUL_SOIL; }

    private static void AddBlockToCreateNuclearItemGroup(FabricItemGroupEntries entries) { entries.accept(ENRICHING_CAMPFIRE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY); }



    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, name), block);
    }
     private static Block registerBlockNoItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateNuclear.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }


    public static void registerModBlocks() {
        CreateNuclear.LOGGER.info("Registering ModBlocks for " + CreateNuclear.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(CNGroup.MAIN_KEY).register(CNBlocks::AddBlockToCreateNuclearItemGroup);
    }
}
