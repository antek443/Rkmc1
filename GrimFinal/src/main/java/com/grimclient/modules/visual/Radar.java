package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class Radar extends Module {
    public final Setting<Integer> size    = new Setting<>("Size",  100, 50, 200, "Rozmiar radaru");
    public final Setting<Float>   range   = new Setting<>("Range", 64f, 16f, 128f, "Zasieg radaru");
    public final Setting<Boolean> players = new Setting<>("Players", true, "Pokazuj graczy");
    public final Setting<Boolean> mobs    = new Setting<>("Mobs",   false, "Pokazuj moby");
    public Radar() { super("Radar", "Mini mapa z lokalizacja graczy", Category.VISUALS); }
}
