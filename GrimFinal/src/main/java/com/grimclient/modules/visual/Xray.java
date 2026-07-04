package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class Xray extends Module {
    public final Setting<Boolean> diamond  = new Setting<>("Diamond",  true, "Diamentowa ruda");
    public final Setting<Boolean> emerald  = new Setting<>("Emerald",  true, "Szmaragdowa ruda");
    public final Setting<Boolean> gold     = new Setting<>("Gold",     true, "Zlota ruda");
    public final Setting<Boolean> iron     = new Setting<>("Iron",     true, "Zelazna ruda");
    public final Setting<Boolean> coal     = new Setting<>("Coal",    false, "Weglowa ruda");
    public final Setting<Boolean> redstone = new Setting<>("Redstone", true, "Redstone");
    public Xray() { super("Xray", "Widocznosc rud przez bloki", Category.VISUALS); }
}
