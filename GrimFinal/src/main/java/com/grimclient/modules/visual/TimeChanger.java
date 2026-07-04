package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class TimeChanger extends Module {
    public final Setting<Integer> time = new Setting<>("Time", 6000, 0, 24000, "Czas dnia (0-24000)");
    public TimeChanger() { super("TimeChanger", "Zmienia czas dnia po stronie klienta", Category.VISUALS); }
}
