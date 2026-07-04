package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class Breadcrumbs extends Module {
    public final Setting<Integer> maxPoints = new Setting<>("MaxPoints", 100, 10, 500, "Max punktow");
    public final Setting<Float>   lineW     = new Setting<>("Width", 1.0f, 0.5f, 3.0f, "Grubosc");
    public Breadcrumbs() { super("Breadcrumbs", "Slad pokazujacy twoja trase", Category.VISUALS); }
}
