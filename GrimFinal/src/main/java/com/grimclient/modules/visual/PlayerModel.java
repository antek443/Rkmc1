package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class PlayerModel extends Module {
    public final Setting<Boolean> noArmor = new Setting<>("NoArmor", false, "Usun zbroje z modelu");
    public final Setting<Boolean> slim    = new Setting<>("Slim",    false, "Smukly model");
    public PlayerModel() { super("PlayerModel", "Modyfikuje wyglad modelu gracza", Category.VISUALS); }
}
