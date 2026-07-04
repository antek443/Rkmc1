package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class ItemESP extends Module {
    public final Setting<Float> range = new Setting<>("Range", 32.0f, 8.0f, 64.0f, "Zasieg");
    public ItemESP() { super("ItemESP", "Podswietla itemy lezace na ziemi", Category.VISUALS); }
}
