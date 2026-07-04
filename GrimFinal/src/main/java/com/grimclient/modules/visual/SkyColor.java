package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class SkyColor extends Module {
    public final Setting<Float> red   = new Setting<>("Red",   0.5f, 0.0f, 1.0f, "Czerwony");
    public final Setting<Float> green = new Setting<>("Green", 0.7f, 0.0f, 1.0f, "Zielony");
    public final Setting<Float> blue  = new Setting<>("Blue",  1.0f, 0.0f, 1.0f, "Niebieski");
    public SkyColor() { super("SkyColor", "Zmienia kolor nieba", Category.VISUALS); }
}
