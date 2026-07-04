package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class Nametags extends Module {
    public final Setting<Boolean> health = new Setting<>("Health", true, "HP nad graczem");
    public final Setting<Boolean> armor  = new Setting<>("Armor",  true, "Zbroja nad graczem");
    public final Setting<Boolean> ping   = new Setting<>("Ping",   true, "Ping gracza");
    public final Setting<Float>   scale  = new Setting<>("Scale",  1.0f, 0.5f, 2.0f, "Rozmiar tekstu");
    public Nametags() { super("Nametags", "Wlasne tagi z HP i ekwipunkiem", Category.VISUALS); }
}
