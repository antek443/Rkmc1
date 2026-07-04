package com.grimclient.modules.visual;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
public class ESP extends Module {
    public final Setting<Boolean> players  = new Setting<>("Players",  true,  "ESP na graczy");
    public final Setting<Boolean> mobs     = new Setting<>("Mobs",     true,  "ESP na moby");
    public final Setting<Boolean> animals  = new Setting<>("Animals",  false, "ESP na zwierzeta");
    public final Setting<Boolean> box      = new Setting<>("Box",      true,  "Rysuj box");
    public final Setting<Boolean> health   = new Setting<>("Health",   true,  "Pokazuj HP");
    public final Setting<Boolean> name     = new Setting<>("Name",     true,  "Pokazuj nick");
    public final Setting<Boolean> armor    = new Setting<>("Armor",    true,  "Pokazuj zbroje");
    public final Setting<Float>   lineW    = new Setting<>("LineWidth",1.5f,0.5f,4.0f,"Grubosc linii");
    public final Setting<Float>   range    = new Setting<>("Range",   64.0f,8.0f,128.0f,"Zasieg ESP");
    public ESP() { super("ESP", "Widz graczy i moby przez sciany", Category.VISUALS); }
}
