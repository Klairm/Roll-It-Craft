package renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import blockentity.DryingRackBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {

	public DryingRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super();

	}

	@Override
	public void render(DryingRackBlockEntity BE, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,
			int packedLight, int packedOverlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		int i = (int) BE.getBlockPos().asLong();

		if (!BE.inventory.getStackInSlot(0).isEmpty()) {
			poseStack.pushPose();

			poseStack.translate(0.5f, 0.45f, 0.9f);

			itemRenderer.renderStatic(BE.inventory.getStackInSlot(0), ItemTransforms.TransformType.FIXED, packedLight,
					packedOverlay, poseStack, bufferSource, i);

			poseStack.popPose();
		}

	}

}
