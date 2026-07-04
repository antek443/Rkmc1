package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class Zoom extends Module {
    public final Setting<Float>   level  = new Setting<>("Level",  4.0f, 1.5f, 20.0f, "Poziom zoomu");
    public final Setting<Boolean> smooth = new Setting<>("Smooth", true, "Plynny zoom");
    public Zoom() { super("Zoom", "Zoom jak w Optifine", Category.VISUALS); }
}
