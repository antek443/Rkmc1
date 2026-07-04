package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class ChestESP extends Module {
    public final Setting<Float> range = new Setting<>("Range", 32.0f, 8.0f, 64.0f, "Zasieg");
    public final Setting<Boolean> chests  = new Setting<>("Chests",  true, "Skrzynie");
    public final Setting<Boolean> barrels = new Setting<>("Barrels", true, "Beczki");
    public final Setting<Boolean> hoppers = new Setting<>("Hoppers", true, "Lejki");
    public ChestESP() { super("ChestESP", "Podswietlenie skrzyn przez sciany", Category.VISUALS); }
}
