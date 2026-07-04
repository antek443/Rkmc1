package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class Glow extends Module {
    public final Setting<Boolean> players = new Setting<>("Players", true,  "Glow na graczy");
    public final Setting<Boolean> mobs    = new Setting<>("Mobs",    false, "Glow na moby");
    public Glow() { super("Glow", "Podswietlenie graczy efektem glow", Category.VISUALS); }
}
