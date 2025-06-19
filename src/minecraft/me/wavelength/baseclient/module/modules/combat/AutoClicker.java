package me.wavelength.baseclient.module.modules.combat;

import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.event.events.UpdateEvent;
import me.wavelength.baseclient.utils.Timer;
import net.minecraft.client.settings.KeyBinding;

public class AutoClicker extends Module {

    private Timer timer;
    private int minCPS = 12;
    private int maxCPS = 14;
    private boolean rightClick = false;
    private boolean breakBlocks = false;
    private boolean weaponOnly = true;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you", 0, Category.COMBAT,
                AntiCheat.VANILLA, AntiCheat.MINEMEM, AntiCheat.HYPIXEL);
        this.timer = new Timer();
    }

    @Override
    public void setup() {
        moduleSettings.addDefault("min_cps", 12);
        moduleSettings.addDefault("max_cps", 14);
        moduleSettings.addDefault("right_click", false);
        moduleSettings.addDefault("break_blocks", false);
        moduleSettings.addDefault("weapon_only", true);
    }

    @Override
    public void onEnable() {
        loadSettings();
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (weaponOnly && !isHoldingWeapon()) return;

        if (timer.hasReached(1000 / getRandomCPS())) {
            if (rightClick) {
                KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            } else {
                KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
            }
            timer.reset();
        }
    }

    private int getRandomCPS() {
        return minCPS + (int)(Math.random() * (maxCPS - minCPS + 1));
    }

    private boolean isHoldingWeapon() {
        return mc.thePlayer.getCurrentEquippedItem() != null &&
                mc.thePlayer.getCurrentEquippedItem().getItem() instanceof net.minecraft.item.ItemSword;
    }

    private void loadSettings() {
        minCPS = moduleSettings.getInt("min_cps");
        maxCPS = moduleSettings.getInt("max_cps");
        rightClick = moduleSettings.getBoolean("right_click");
        breakBlocks = moduleSettings.getBoolean("break_blocks");
        weaponOnly = moduleSettings.getBoolean("weapon_only");
    }
}
