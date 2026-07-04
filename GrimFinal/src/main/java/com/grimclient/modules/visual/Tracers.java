package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class Tracers extends Module {
    public final Setting<Boolean> players = new Setting<>("Players", true,  "Tracery na graczy");
    public final Setting<Boolean> mobs    = new Setting<>("Mobs",    false, "Tracery na moby");
    public final Setting<Float>   lineW   = new Setting<>("Width",   1.0f, 0.5f, 3.0f, "Grubosc linii");
    public Tracers() { super("Tracers", "Linie wskazujace lokalizacje graczy", Category.VISUALS); }
}
