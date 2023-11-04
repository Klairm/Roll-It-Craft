package com.github.klairm.projectm;

import gui.DrugMachineScreen;
import gui.ModMenuTypes;
import init.BlockEntityInit;
import init.BlockInit;
import init.ItemInit;
import init.RecipeTypeInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ProjectM.MOD_ID)
public class ProjectM {

	public static final String MOD_ID = "projectm";

	public ProjectM() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::setup);

		ItemInit.ITEMS.register(modEventBus);
		BlockInit.BLOCKS.register(modEventBus);
		BlockEntityInit.TILE_ENTITY_TYPES.register(modEventBus);
		RecipeTypeInit.SERIALIZERS.register(modEventBus);
		RecipeTypeInit.RECIPE_TYPES.register(modEventBus);
		ModMenuTypes.MENUS.register(modEventBus);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {

	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {

			MenuScreens.register(ModMenuTypes.DRUG_MACHINE_MENU.get(), DrugMachineScreen::new);
		}
	}

}
