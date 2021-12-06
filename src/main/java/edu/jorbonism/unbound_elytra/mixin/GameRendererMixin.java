package edu.jorbonism.unbound_elytra.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import edu.jorbonism.unbound_elytra.UnboundElytra;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Final @Shadow private MinecraftClient client;
	
	@Inject(at = @At("HEAD"), method = "renderWorld")
	public void renderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
		if (this.client.player == null || !this.client.player.isFallFlying()) return;

		double angle = -Math.acos(UnboundElytra.left.dotProduct(UnboundElytra.getAssumedLeft(this.client.player.getYaw()))) * UnboundElytra.TODEG;
		if (UnboundElytra.left.getY() < 0) angle *= -1;
		matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float)angle));
	}
}
