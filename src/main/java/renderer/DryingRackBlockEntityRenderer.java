package renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import blockentity.DryingRackBlockEntity;
import blocks.DryingRack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {

	public DryingRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super();

	}

	@Override
	public void render(DryingRackBlockEntity BE, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,
			int packedLight, int packedOverlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		int i = (int) BE.getBlockPos().asLong();

		if (BE.inventory.getStackInSlot(0).isEmpty()) {
			return;
		}

		poseStack.pushPose();

		switch (BE.getBlockState().getValue(DryingRack.FACING)) {
		case EAST -> {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                    poseStack.translate(-0.5f, 0.45f, 0.1f);
                }

		case NORTH -> poseStack.translate(0.5f, 0.48f, 0.9f);
		case SOUTH -> {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-180));
                    poseStack.translate(-0.5f, 0.46f, -0.1f);
                }

		case WEST -> {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
                    poseStack.translate(0.5f, 0.44f, -0.9f);
                }

		}

		itemRenderer.renderStatic(BE.inventory.getStackInSlot(0), ItemTransforms.TransformType.FIXED, packedLight,
				packedOverlay, poseStack, bufferSource, i);

		poseStack.popPose();
	}
}
