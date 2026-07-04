package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class StorageESP extends Module {
    public final Setting<Float>   range    = new Setting<>("Range",   32.0f, 8.0f, 64.0f, "Zasieg");
    public final Setting<Boolean> chests   = new Setting<>("Chests",  true, "Skrzynie");
    public final Setting<Boolean> hoppers  = new Setting<>("Hoppers", true, "Lejki");
    public final Setting<Boolean> droppers = new Setting<>("Droppers",true, "Podajniki");
    public final Setting<Boolean> furnaces = new Setting<>("Furnaces",true, "Piece");
    public StorageESP() { super("StorageESP", "ESP na wszystkie pojemniki", Category.VISUALS); }
}
