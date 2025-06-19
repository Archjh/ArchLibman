package me.wavelength.baseclient.module.modules.player;

import java.awt.Color;
import me.wavelength.baseclient.event.events.Render2DEvent;
import me.wavelength.baseclient.module.AntiCheat;
import me.wavelength.baseclient.module.Category;
import me.wavelength.baseclient.module.Module;
import me.wavelength.baseclient.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Input;
import org.newdawn.slick.opengl.renderer.SGL;

/* loaded from: ArchLibman.jar:me/wavelength/baseclient/module/modules/player/ModKeystrokes.class */
public class ModKeystrokes extends Module {
    public static int dif;
    public static boolean showCps = true;
    private Render2DEvent pos;
    private KeystrokesMode mode;
    public static int modifer;

    public ModKeystrokes() {
        super("Keystrokes", "Keystrokes", 37, Category.PLAYER, AntiCheat.VANILLA, AntiCheat.AAC);
        this.mode = KeystrokesMode.WASD_MOUSE;
    }

    /* loaded from: ArchLibman.jar:me/wavelength/baseclient/module/modules/player/ModKeystrokes$KeystrokesMode.class */
    public enum KeystrokesMode {
        WASD(Key.W, Key.A, Key.S, Key.D),
        WASD_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, Key.SPACE),
        WASD_SPRINT(Key.W, Key.A, Key.S, Key.D, new Key("Sprint", Minecraft.getMinecraft().gameSettings.keyBindSprint, 1, 41, 58, 18, "SPRINT")),
        WASD_SPRINT_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, new Key("Sprint", Minecraft.getMinecraft().gameSettings.keyBindSprint, 1, 61, 58, 18, "SPINTMOUSE"));

        private final Key[] keys;
        private int width;
        private int height;

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        public Key[] getKeys() {
            return this.keys;
        }

        KeystrokesMode(Key... keysIn) {
            this.width = 0;
            this.height = 0;
            this.keys = keysIn;
            for (Key key : this.keys) {
                this.width = Math.max(this.width, key.getX() + key.getWidth());
                this.height = Math.max(this.height, key.getY() + key.getHeight());
            }
        }
    }

    /* loaded from: ArchLibman.jar:me/wavelength/baseclient/module/modules/player/ModKeystrokes$Key.class */
    public static class Key {
        private static final Key W = new Key("W", Minecraft.getMinecraft().gameSettings.keyBindForward, 21 - ModKeystrokes.dif, 1 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, "W");
        private static final Key A = new Key("A", Minecraft.getMinecraft().gameSettings.keyBindLeft, 1 - ModKeystrokes.dif, 21 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, "A");
        private static final Key S = new Key("S", Minecraft.getMinecraft().gameSettings.keyBindBack, 21 - ModKeystrokes.dif, 21 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, "S");
        private static final Key D = new Key("D", Minecraft.getMinecraft().gameSettings.keyBindRight, 41 - ModKeystrokes.dif, 21 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, "D");
        private static final Key LMB = new Key("LMB", Minecraft.getMinecraft().gameSettings.keyBindAttack, 1 - ModKeystrokes.dif, 41 - ModKeystrokes.dif, 28 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, "LMB");
        private static final Key RMB = new Key("RMB", Minecraft.getMinecraft().gameSettings.keyBindUseItem, 31 - ModKeystrokes.dif, 41 - ModKeystrokes.dif, 28 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, "RMB");
        private static final Key SPACE = new Key("JUMP", Minecraft.getMinecraft().gameSettings.keyBindJump, 1 - ModKeystrokes.dif, 62 - ModKeystrokes.dif, 58 - ModKeystrokes.dif, 18 - ModKeystrokes.dif, "SB");
        private String name;
        private final KeyBinding keyBind;
        private final int x;
        private final int y;
        private int width;
        private int height;
        private String id;

        public Key(String name, KeyBinding keyBind, int x, int y, int width, int height, String id) {
            this.name = name;
            this.keyBind = keyBind;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDown() {
            return this.keyBind.isKeyDown();
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public String getName() {
            return this.name;
        }

        public String getId() {
            return this.id;
        }
    }

    public void setMode(KeystrokesMode mode) {
        this.mode = mode;
    }

    @Override // me.wavelength.baseclient.module.Module
    public int getWidth() {
        return this.mode.getWidth();
    }

    @Override // me.wavelength.baseclient.module.Module
    public int getHeight() {
        return this.mode.getHeight();
    }

    // 在ModKeystrokes.java中修改onRender2D方法
    @Override
    public void onRender2D(Render2DEvent pos) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for (Key key : this.mode.getKeys()) {
            int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(key.getName());
            Gui.drawRect(pos.getAbsoluteX() + key.getX(), pos.getAbsoluteY() + key.getY(),
                    pos.getAbsoluteX() + key.getX() + key.getWidth(),
                    pos.getAbsoluteY() + key.getY() + key.getHeight(),
                    getWhatColorShouldBe(key));

            if (showCps && key.getId().contains("MB")) {
                // 获取CPS模块实例
                CPS cpsModule = (CPS) ModuleManager.getModule(CPS.class);
                if (cpsModule != null) {
                    if (key.getId().equals("RMB")) {
                        key.setName(String.valueOf(cpsModule.getRightCPS()));
                    }
                    if (key.getId().equals("LMB")) {
                        key.setName(String.valueOf(cpsModule.getLeftCPS()));
                    }
                }
            }

            Minecraft.getMinecraft().fontRendererObj.drawString(
                    key.getName(),
                    ((pos.getAbsoluteX() + key.getX() + (key.getWidth() / 2)) - (textWidth / 2)) - modifer,
                    ((pos.getAbsoluteY() + key.getY() + (key.getHeight() / 2)) - 4) - modifer,
                    key.isDown() ? Color.BLACK.getRGB() : Color.WHITE.getRGB()
            );
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override // me.wavelength.baseclient.module.Module
    public void render(Render2DEvent pos) {
        GL11.glPushMatrix();
        GL11.glEnable(SGL.GL_BLEND);
        for (Key key : this.mode.getKeys()) {
            int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(key.getName());
            Gui.drawRect(pos.getAbsoluteX() + key.getX(), pos.getAbsoluteY() + key.getY(), pos.getAbsoluteX() + key.getX() + key.getWidth(), pos.getAbsoluteY() + key.getY() + key.getHeight(), getWhatColorShouldBe(key));
            Minecraft.getMinecraft().fontRendererObj.drawString(key.getName(), (((pos.getAbsoluteX() + key.getX()) + (key.getWidth() / 2)) - (textWidth / 2)) - modifer, (((pos.getAbsoluteY() + key.getY()) + (key.getHeight() / 2)) - 4) - modifer, key.isDown() ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
        }
        GL11.glDisable(SGL.GL_BLEND);
        GL11.glPopMatrix();
    }

    // 修改颜色方法
    public int getWhatColorShouldBe(Key key) {
        if (key.isDown()) {
            return new Color(255, 255, 255, 150).getRGB(); // 半透明白色
        }
        return new Color(0, 0, 0, 150).getRGB(); // 半透明黑色
    }
}