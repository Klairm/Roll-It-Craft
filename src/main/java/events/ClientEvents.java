package events;

import com.github.klairm.projectm.ProjectM;

import init.BlockEntityInit;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import renderer.DryingRackBlockEntityRenderer;

@Mod.EventBusSubscriber(modid = ProjectM.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BlockEntityInit.DRYING_RACK.get(), DryingRackBlockEntityRenderer::new);

	}

}
