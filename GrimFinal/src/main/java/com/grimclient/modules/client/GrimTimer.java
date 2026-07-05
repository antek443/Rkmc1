package com.grimclient.modules.client;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class GrimTimer extends Module {
    public final Setting<Float> speed = new Setting<>("Speed", 1.0f, 0.1f, 10.0f, "Predkosc gry");
    public GrimTimer() { super("Timer", "Przyspiesza lub spowalnia gre", Category.CLIENT); }
}
