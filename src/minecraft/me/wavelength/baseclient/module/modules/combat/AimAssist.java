package me.wavelength.baseclient.module.modules.combat;

import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AimAssist extends Module {

    private float range = 4.0f;
    private float speed = 2.0f;
    private float fov = 90.0f;
    private boolean ignoreWalls = false;
    private boolean lockOnClick = true;
    private boolean vertical = true;

    public AimAssist() {
        super("AimAssist", "Helps aim at nearby players", 0, Category.COMBAT,
                AntiCheat.VANILLA, AntiCheat.MINEMEM, AntiCheat.HYPIXEL);
    }

    @Override
    public void setup() {
        moduleSettings.addDefault("range", 4.0f);
        moduleSettings.addDefault("speed", 2.0f);
        moduleSettings.addDefault("fov", 90.0f);
        moduleSettings.addDefault("ignore_walls", false);
        moduleSettings.addDefault("lock_on_click", true);
        moduleSettings.addDefault("vertical", true);
    }

    @Override
    public void onEnable() {
        loadSettings();
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (lockOnClick && !mc.gameSettings.keyBindAttack.isKeyDown()) return;

        Entity target = findTarget();
        if (target != null) {
            aimAt(target);
        }
    }

    private Entity findTarget() {
        Entity target = null;
        float closestAngle = fov;

        for (Object entityObj : mc.theWorld.loadedEntityList) {
            if (!(entityObj instanceof EntityPlayer)) continue;

            EntityPlayer player = (EntityPlayer) entityObj;
            if (player == mc.thePlayer || player.isDead) continue;

            float distance = player.getDistanceToEntity(mc.thePlayer);
            if (distance > range) continue;

            if (!ignoreWalls && !mc.thePlayer.canEntityBeSeen(player)) continue;

            float[] rotations = RotationUtils.getRotations(player);
            float yawDiff = Math.abs(RotationUtils.wrapAngleTo180(mc.thePlayer.rotationYaw - rotations[0]));
            float pitchDiff = Math.abs(RotationUtils.wrapAngleTo180(mc.thePlayer.rotationPitch - rotations[1]));

            float angleDiff = (float) Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);

            if (angleDiff < closestAngle) {
                closestAngle = angleDiff;
                target = player;
            }
        }

        return target;
    }

    private void aimAt(Entity target) {
        float[] rotations = RotationUtils.getRotations(target);
        float targetYaw = rotations[0];
        float targetPitch = rotations[1];

        float currentYaw = mc.thePlayer.rotationYaw;
        float currentPitch = mc.thePlayer.rotationPitch;

        float yawDiff = RotationUtils.wrapAngleTo180(targetYaw - currentYaw);
        float pitchDiff = RotationUtils.wrapAngleTo180(targetPitch - currentPitch);

        float yawChange = yawDiff / speed;
        float pitchChange = vertical ? (pitchDiff / speed) : 0;

        mc.thePlayer.rotationYaw += yawChange;
        if (vertical) {
            mc.thePlayer.rotationPitch += pitchChange;
        }
    }

    private void loadSettings() {
        range = moduleSettings.getFloat("range");
        speed = moduleSettings.getFloat("speed");
        fov = moduleSettings.getFloat("fov");
        ignoreWalls = moduleSettings.getBoolean("ignore_walls");
        lockOnClick = moduleSettings.getBoolean("lock_on_click");
        vertical = moduleSettings.getBoolean("vertical");
    }
}