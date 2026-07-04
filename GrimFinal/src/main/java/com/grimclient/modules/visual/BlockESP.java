package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class BlockESP extends Module {
    public final Setting<Float> range = new Setting<>("Range", 16.0f, 4.0f, 32.0f, "Zasieg");
    public BlockESP() { super("BlockESP", "ESP na konkretne bloki", Category.VISUALS); }
}
