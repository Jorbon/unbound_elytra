package edu.jorbonism.unbound_elytra.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import edu.jorbonism.unbound_elytra.UnboundElytra;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.math.Vec3d;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) { super(world, profile); }

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/ClientPlayNetworkHandler;Lnet/minecraft/stat/StatHandler;Lnet/minecraft/client/recipebook/ClientRecipeBook;ZZ)V")
	public void init(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting, CallbackInfo ci) {
		UnboundElytra.left = UnboundElytra.getAssumedLeft(this.getYaw());
	}

	public void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
		Vec3d facing = this.getRotationVecClient();

		if (!this.isFallFlying()) {
			// set left vector to the assumed upright left if not in flight
			UnboundElytra.left = UnboundElytra.getAssumedLeft(this.getYaw());
			super.changeLookDirection(cursorDeltaX, cursorDeltaY);
			return;
		}

		UnboundElytra.left = UnboundElytra.left.subtract(facing.multiply(UnboundElytra.left.dotProduct(facing))).normalize();

		// pitch
		facing = UnboundElytra.rotateAxisAngle(facing, UnboundElytra.left, -0.15 * cursorDeltaY * UnboundElytra.TORAD);
		
		if (this.isSneaking()) {
			// yaw
			Vec3d up = facing.crossProduct(UnboundElytra.left);
			facing = UnboundElytra.rotateAxisAngle(facing, up, 0.15 * cursorDeltaX * UnboundElytra.TORAD);
			UnboundElytra.left = UnboundElytra.rotateAxisAngle(UnboundElytra.left, up, 0.15 * cursorDeltaX * UnboundElytra.TORAD);
		} else {
			// roll
			UnboundElytra.left = UnboundElytra.rotateAxisAngle(UnboundElytra.left, facing, 0.15 * cursorDeltaX * UnboundElytra.TORAD);
		}

		
		double deltaY = -Math.asin(facing.getY()) * UnboundElytra.TODEG - this.getPitch();
		double deltaX = -Math.atan2(facing.getX(), facing.getZ()) * UnboundElytra.TODEG - this.getYaw();

		super.changeLookDirection(deltaX / 0.15, deltaY / 0.15);
    }
}
