package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class TargetHUD extends Module {
    public final Setting<Integer> x = new Setting<>("X", 10, 0, 1920, "Pozycja X");
    public final Setting<Integer> y = new Setting<>("Y", 10, 0, 1080, "Pozycja Y");
    public final Setting<Boolean> showHealth = new Setting<>("Health", true, "Pokazuj HP");
    public final Setting<Boolean> showArmor  = new Setting<>("Armor",  true, "Pokazuj zbroje");
    public TargetHUD() { super("TargetHUD", "HUD z informacjami o aktualnym celu", Category.VISUALS); }
}
