package net.ynov.createnuclear.blockentity;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.crate.CreativeCrateBlockEntity;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import com.simibubi.create.content.schematics.cannon.SchematicannonInstance;
import com.simibubi.create.content.schematics.cannon.SchematicannonRenderer;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.groups.CNGroup;
import net.ynov.createnuclear.tools.EnrichingCampfireBlockEntity;

import static com.simibubi.create.Create.REGISTRATE;
public class CNBlockEntity {
    static {
        CreateNuclear.REGISTRATE.useCreativeTab(CNGroup.MAIN_KEY);
    }
    /*public static final BlockEntityEntry<EnrichingCampfireBlockEntity> ENRICHING_CAMPFIRE_ENTITY = CreateNuclear.REGISTRATE
            .blockEntity("enriching_campfire_entity", EnrichingCampfireBlockEntity::new)
            //.instance(() -> EnrichingCampfireBlockEntity::new)
            .validBlock(CNBlocks.ENRICHING_CAMPFIRE)
            //.initialProperties(SharedProperties::wooden)
            .renderer(() -> Test::new)
            .register();*/

    public static void register() {}
}
