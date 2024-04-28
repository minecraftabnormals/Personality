package com.teamabnormals.personality.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.personality.client.model.FishingHookModel;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHookRenderer.class)
public abstract class FishingHookRendererMixin extends EntityRenderer<FishingHook> {

	@Unique
	private static final FishingHookModel MODEL = new FishingHookModel(FishingHookModel.createBodyLayer().bakeRoot());
	@Unique
	private static final ResourceLocation TEXTURE = new ResourceLocation(Personality.MOD_ID, "textures/entity/fishing_bobber.png");
	@Unique
	private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);

	private FishingHookRendererMixin(EntityRendererProvider.Context renderManager) {
		super(renderManager);
	}

	@Shadow
	private static void stringVertex(float p_229104_0_, float p_229104_1_, float p_229104_2_, VertexConsumer p_229104_3_, PoseStack.Pose pose, float p_229104_4_, float p_229104_5_) {
		throw new AssertionError("Mixin failed");
	}

	@Shadow
	private static float fraction(int p_229105_0_, int p_229105_1_) {
		throw new AssertionError("Mixin failed");
	}


	@Inject(method = "render(Lnet/minecraft/world/entity/projectile/FishingHook;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
	public void render(FishingHook bobber, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
		Player player = bobber.getPlayerOwner();
		if (player != null) {
			ItemStack mainRod = player.getMainHandItem();
			ItemStack offHandRod = player.getOffhandItem();
			boolean enchanted = (mainRod.getItem() instanceof FishingRodItem && mainRod.isEnchanted()) || (offHandRod.getItem() instanceof FishingRodItem && offHandRod.isEnchanted());

			poseStack.pushPose();
			{
				poseStack.pushPose();
				{
					poseStack.scale(1, -1, -1);
					poseStack.translate(0, -1.5, 0);
					MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(buffer, RENDER_TYPE, false, enchanted), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1); // TODO: add glint config
				}
				poseStack.popPose();

				int hand = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
				if (!(mainRod.getItem() instanceof FishingRodItem)) {
					hand = -hand;
				}

				float swingProgress = player.getAttackAnim(partialTicks);
				float swingProgressSin = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
				float playerYaw = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * ((float) Math.PI / 180F);
				double yawSin = Mth.sin(playerYaw);
				double yawCos = Mth.cos(playerYaw);
				double handPos = (double) hand * 0.35D;
				double playerPosX;
				double playerPosY;
				double playerPosZ;
				float playerEyeHeight;
				if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
					double d7 = this.entityRenderDispatcher.options.fov().get() / 100.0D;
					Vec3 handVec = new Vec3((double) hand * -0.36D * d7, -0.045D * d7, 0.4D);
					handVec = handVec.xRot(-Mth.lerp(partialTicks, player.xRotO, player.getXRot()) * ((float) Math.PI / 180F));
					handVec = handVec.yRot(-Mth.lerp(partialTicks, player.yRotO, player.getYRot()) * ((float) Math.PI / 180F));
					handVec = handVec.yRot(swingProgressSin * 0.5F);
					handVec = handVec.xRot(-swingProgressSin * 0.7F);

					playerPosX = Mth.lerp(partialTicks, player.xo, player.getX()) + handVec.x;
					playerPosY = Mth.lerp(partialTicks, player.yo, player.getY()) + handVec.y;
					playerPosZ = Mth.lerp(partialTicks, player.zo, player.getZ()) + handVec.z;
					playerEyeHeight = player.getEyeHeight();
				} else {
					playerPosX = Mth.lerp(partialTicks, player.xo, player.getX()) - yawCos * handPos - yawSin * 0.8D;
					playerPosY = player.yo + (double) player.getEyeHeight() + (player.getY() - player.yo) * (double) partialTicks - 0.45D;
					playerPosZ = Mth.lerp(partialTicks, player.zo, player.getZ()) - yawSin * handPos + yawCos * 0.8D;
					playerEyeHeight = player.isCrouching() ? -0.1875F : 0.0F;
				}

				double posX = Mth.lerp(partialTicks, bobber.xo, bobber.getX());
				double posY = Mth.lerp(partialTicks, bobber.yo, bobber.getY()) + 0.25D;
				double posZ = Mth.lerp(partialTicks, bobber.zo, bobber.getZ());
				float stringPosX = (float) (playerPosX - posX);
				float stringPosY = (float) (playerPosY - posY) + playerEyeHeight;
				float stringPosZ = (float) (playerPosZ - posZ);
				VertexConsumer stringVertex = buffer.getBuffer(RenderType.lineStrip());
				Pose stringMatrix = poseStack.last();

				for (int i = 0; i <= 16; ++i) {
					stringVertex(stringPosX, stringPosY, stringPosZ, stringVertex, stringMatrix, fraction(i, 16), fraction(i + 1, 16));
				}
			}
			poseStack.popPose();

			super.render(bobber, yaw, partialTicks, poseStack, buffer, packedLight);
		}

		ci.cancel(); // TODO: config
	}
}