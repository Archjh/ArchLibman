package me.wavelength.baseclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {

    public static float[] getRotations(Entity entity) {
        double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double diffY;

        if (entity instanceof net.minecraft.entity.EntityLivingBase) {
            diffY = entity.posY + entity.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }

        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)-(Math.atan2(diffY, dist) * 180.0 / Math.PI);

        return new float[]{yaw, pitch};
    }

    public static float wrapAngleTo180(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
}