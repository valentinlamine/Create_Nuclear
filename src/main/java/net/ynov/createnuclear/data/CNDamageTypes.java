package net.ynov.createnuclear.data;

import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.ynov.createnuclear.CreateNuclear;

public class CNDamageTypes {
    public static ResourceKey<DamageType>
        RADIATION = key("radiation");

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, CreateNuclear.asResource(name));
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        new DamageTypeBuilder(RADIATION).register(ctx);
    }
}
