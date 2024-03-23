package gui;

import com.github.klairm.projectm.ProjectM;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class DrugMachineScreen extends AbstractContainerScreen<DrugMachineMenu> {

	
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectM.MOD_ID,
			"textures/gui/drug_machine.png");
	
	

	public DrugMachineScreen(DrugMachineMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		 RenderSystem.setShader(GameRenderer::getPositionTexShader);
	      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	      RenderSystem.setShaderTexture(0, TEXTURE);
	      int i = this.leftPos;
	      int j = this.topPos;
	      this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	      

	      
	      
	}

	@Override
	public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
		renderBackground(pPoseStack);
		super.render(pPoseStack, mouseX, mouseY, delta);
		renderTooltip(pPoseStack, mouseX, mouseY);
	}
	
	

}
