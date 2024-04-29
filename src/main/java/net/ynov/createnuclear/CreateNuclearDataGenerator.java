package net.ynov.createnuclear;

import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.ynov.createnuclear.data.CNGeneratedEntriesProvider;
import net.ynov.createnuclear.datagen.CNWorldGenerator;
import net.ynov.createnuclear.world.CNConfiguredFeatures;
import net.ynov.createnuclear.world.CNPlacedFeatures;

import java.util.function.BiConsumer;

public class CreateNuclearDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		CreateNuclear.REGISTRATE.setupDatagen(pack, helper);
		getherData(pack, helper);

		pack.addProvider(CNWorldGenerator::new);
	}

	public static void getherData(FabricDataGenerator.Pack pack, ExistingFileHelper helper) {
		addExtraRgeistrateData();

		pack.addProvider(CNGeneratedEntriesProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONFIGURED_FEATURE, CNConfiguredFeatures::boostrap);
		registryBuilder.add(Registries.PLACED_FEATURE, CNPlacedFeatures::boostrap);
	}

	private static void addExtraRgeistrateData() {
		CreateNucleairRegistrateTags.addGenerators();

		CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;
			providerPonderLang(langConsumer);
		});

	}

	private static void providerPonderLang(BiConsumer<String, String> consumer) {
		PonderLocalization.provideLang(CreateNuclear.MOD_ID, consumer);
	}
}