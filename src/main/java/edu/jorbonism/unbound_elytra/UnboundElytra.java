package edu.jorbonism.unbound_elytra;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.math.Vec3d;

/*
to do:
add config with:
	sensitivity
	inversions
	yaw strength
add yaw mechanic
*/

public class UnboundElytra implements ClientModInitializer {
	public void onInitializeClient() {
		
	}

	public static Vec3d left;
	public static final double TORAD = Math.PI / 180;
	public static final double TODEG = 1 / TORAD;

	public static Vec3d getAssumedLeft(float yaw) {
		yaw *= TORAD;
		return new Vec3d(-Math.cos(yaw), 0, -Math.sin(yaw));
	}

	public static Vec3d rotateAxisAngle(Vec3d v, Vec3d axis, double angle) {
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double t = 1.0 - c;

		//double l = axis.lengthSquared();
		//if (l == 0) return v;
		//if (l != 1) axis = axis.multiply(1/Math.sqrt(l));

		double x = (c + axis.x*axis.x*t) * v.getX(),
			   y = (c + axis.y*axis.y*t) * v.getY(),
			   z = (c + axis.z*axis.z*t) * v.getZ(),
		tmp1 = axis.x*axis.y*t,
		tmp2 = axis.z*s;
		y += (tmp1 + tmp2) * v.getX();
		x += (tmp1 - tmp2) * v.getY();
		tmp1 = axis.x*axis.z*t;
		tmp2 = axis.y*s;
		z += (tmp1 - tmp2) * v.getX();
		x += (tmp1 + tmp2) * v.getZ();
		tmp1 = axis.y*axis.z*t;
		tmp2 = axis.x*s;
		z += (tmp1 + tmp2) * v.getY();
		y += (tmp1 - tmp2) * v.getZ();
		
		return new Vec3d(x, y, z);
	}

	
}
