package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class FullBright extends Module {
    public final Setting<Float> gamma = new Setting<>("Gamma", 100.0f, 1.0f, 100.0f, "Poziom jasnosci");
    public FullBright() { super("FullBright", "Maksymalna jasnosc bez torchy", Category.VISUALS); }
}
