package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class FreeCam extends Module {
    public final Setting<Float> speed = new Setting<>("Speed", 1.0f, 0.1f, 5.0f, "Predkosc kamery");
    public FreeCam() { super("FreeCam", "Odlacz kamere od gracza", Category.VISUALS); }
}
