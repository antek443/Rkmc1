package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class HoleESP extends Module {
    public final Setting<Float>   range  = new Setting<>("Range",  16.0f, 4.0f, 32.0f, "Zasieg");
    public final Setting<Boolean> obsidian = new Setting<>("Obsidian", true, "Dziury w obsydanie");
    public final Setting<Boolean> bedrock  = new Setting<>("Bedrock",  true, "Dziury w bedrocku");
    public HoleESP() { super("HoleESP", "Pokazuje bezpieczne dziury do krycia", Category.VISUALS); }
}
