package com.grimclient.modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private final String name, description;
    private final Category category;
    private boolean enabled=false;
    private int keybind=-1;
    private boolean keyWasDown=false;
    public enum Category {
        COMBAT("Combat","\u2694"),MOVEMENT("Movement","\u26A1"),
        PLAYER("Player","\uD83D\uDC64"),VISUALS("Visuals","\u2728"),CLIENT("Client","\uD83D\uDDA5");
        public final String label,icon;
        Category(String l,String i){label=l;icon=i;}
    }
    public Module(String n,String d,Category c){name=n;description=d;category=c;}
    public void onEnable(){}
    public void onDisable(){}
    public void onTick(){}
    public void onRenderHUD(DrawContext ctx,int x,int y){}
    public void checkKeybind(){
        if(keybind==-1||mc.currentScreen!=null)return;
        boolean isDown=InputUtil.isKeyPressed(mc.getWindow().getHandle(),keybind);
        if(isDown&&!keyWasDown)toggle();
        keyWasDown=isDown;
    }
    public void toggle(){enabled=!enabled;if(enabled)onEnable();else onDisable();}
    public void setEnabled(boolean e){boolean w=enabled;enabled=e;if(!w&&e)onEnable();else if(w&&!e)onDisable();}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public Category getCategory(){return category;}
    public boolean isEnabled(){return enabled;}
    public int getKeybind(){return keybind;}
    public void setKeybind(int k){keybind=k;}
}
