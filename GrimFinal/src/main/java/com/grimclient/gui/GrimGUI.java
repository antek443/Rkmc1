package com.grimclient.gui;

import com.grimclient.GrimClientMod;
import com.grimclient.modules.Module;
import com.grimclient.modules.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrimGUI extends Screen {

    // Layout
    private static final int PW = 820, PH = 520;
    private static final int SW = 158;  // sidebar
    private static final int HH = 44;   // header
    private static final int SP = 220;  // settings panel width

    // Colors
    private static final int C_BG      = 0xFF111114;
    private static final int C_SB      = 0xFF0D0D10;
    private static final int C_HDR     = 0xFF09090C;
    private static final int C_CARD    = 0xFF161619;
    private static final int C_CARD_H  = 0xFF1C1C22;
    private static final int C_CARD_ON = 0xFF17172A;
    private static final int C_ACC     = 0xFF7B6FFF;
    private static final int C_ACC2    = 0xFF5A4DCC;
    private static final int C_WHITE   = 0xFFEEEEFF;
    private static final int C_GRAY    = 0xFF8888AA;
    private static final int C_DGRAY   = 0xFF3A3A50;
    private static final int C_SEP     = 0xFF1A1A22;
    private static final int C_SRCH    = 0xFF141418;
    private static final int C_SPANEL  = 0xFF0E0E14;

    // State
    private int px, py;
    private Module.Category selCat = Module.Category.COMBAT;
    private int scroll = 0, maxScroll = 0;
    private boolean drag = false;
    private int dragX, dragY;

    // Search
    private String searchQuery = "";
    private boolean searchMode = false;
    private boolean searchFocused = false;

    // Settings panel
    private Module selectedModule = null;
    private int settingsScroll = 0;

    // Keybind
    private Module bindingModule = null;
    private boolean waitingForKey = false;

    // Slider dragging
    private Setting<?> draggingSetting = null;
    private int sliderStartX = 0;

    public GrimGUI() {
        super(Text.literal("Grim Client"));
    }

    @Override
    protected void init() {
        px = (width - PW) / 2;
        py = (height - PH) / 2;
        scroll = 0;
        settingsScroll = 0;
    }

    @Override
    public boolean shouldPause() { return false; }

    // =========================================================
    //  RENDER
    // =========================================================
    @Override
    public void render(DrawContext c, int mx, int my, float dt) {
        c.fill(0, 0, width, height, 0xBB000000);

        boolean showSettings = selectedModule != null;
        int mainW = showSettings ? PW - SP : PW;

        // Panel BG
        c.fill(px, py, px+PW, py+PH, C_BG);

        // Sidebar
        c.fill(px, py, px+SW, py+PH, C_SB);
        c.fill(px+SW, py, px+SW+1, py+PH, C_SEP);

        // Header
        c.fill(px, py, px+PW, py+HH, C_HDR);
        c.fill(px, py+HH, px+PW, py+HH+1, C_SEP);
        c.fill(px, py, px+PW, py+2, C_ACC);

        // Logo
        drawSkull(c, px+10, py+8);
        c.drawText(textRenderer, "Grim Client", px+36, py+9, C_WHITE, false);
        c.drawText(textRenderer, "Version: 1.0", px+37, py+21, C_DGRAY, false);

        // Search
        int sbx = px+SW+10, sby = py+(HH-18)/2;
        c.fill(sbx, sby, sbx+180, sby+18, C_SRCH);
        c.fill(sbx, sby+17, sbx+180, sby+18, searchFocused ? C_ACC : C_DGRAY);
        String sd = searchQuery.isEmpty() && !searchFocused ? "Search modules..." : searchQuery+(searchFocused?"|":"");
        c.drawText(textRenderer, sd, sbx+6, sby+5, searchQuery.isEmpty() ? C_DGRAY : C_WHITE, false);

        int en = GrimClientMod.core.moduleManager.getEnabled().size();
        String enStr = en+" active";
        c.drawText(textRenderer, enStr, px+PW-textRenderer.getWidth(enStr)-10, py+HH-13, C_ACC, false);
        c.drawText(textRenderer, "[P]", px+PW-22, py+8, C_DGRAY, false);

        // Sidebar cats
        c.drawText(textRenderer, "FEATURES", px+10, py+HH+8, C_DGRAY, false);
        int ty = py+HH+22;
        Module.Category[] mainCats = {Module.Category.COMBAT, Module.Category.MOVEMENT, Module.Category.PLAYER, Module.Category.VISUALS};
        for (Module.Category cat : mainCats) {
            renderCatTab(c, mx, my, cat, ty);
            ty += 28;
        }
        ty += 4; c.fill(px+8, ty, px+SW-8, ty+1, C_SEP); ty += 8;
        c.drawText(textRenderer, "OTHER", px+10, ty, C_DGRAY, false); ty += 14;
        renderCatTab(c, mx, my, Module.Category.CLIENT, ty); ty += 28;

        String[] extra = {"👥 Friends", "⚙ Configs", "🖥 Hud"};
        for (String e2 : extra) {
            if (in(mx,my, px, ty, SW, 26)) c.fill(px, ty, px+SW, ty+26, 0xFF131318);
            c.drawText(textRenderer, e2, px+12, ty+8, C_DGRAY, false); ty += 28;
        }

        // User
        c.fill(px, py+PH-34, px+SW, py+PH, 0xFF0A0A0D);
        c.fill(px, py+PH-35, px+SW, py+PH-34, C_SEP);
        drawSkull(c, px+8, py+PH-26);
        c.drawText(textRenderer, "Player", px+30, py+PH-28, C_GRAY, false);
        c.drawText(textRenderer, "UID: 1", px+30, py+PH-18, C_DGRAY, false);

        // Module grid
        List<Module> mods = searchMode && !searchQuery.isEmpty()
            ? GrimClientMod.core.moduleManager.search(searchQuery)
            : GrimClientMod.core.moduleManager.getByCategory(selCat);

        int cx0 = px+SW+8;
        int cy0 = py+HH+8;
        int contW = showSettings ? PW-SW-SP-16 : PW-SW-16;
        int contH = PH-HH-16;
        int cols = showSettings ? 1 : 2;
        int cardW = cols == 1 ? contW-4 : (contW-6)/2;
        int cardH = 46, gap = 5;
        int rows = (mods.size()+cols-1)/cols;
        maxScroll = Math.max(0, rows*(cardH+gap)-contH+8);
        if (scroll > maxScroll) scroll = maxScroll;

        for (int i = 0; i < mods.size(); i++) {
            Module mod = mods.get(i);
            int col = i%cols, row = i/cols;
            int cx = cx0 + col*(cardW+(cols>1?6:0));
            int cy = cy0 + row*(cardH+gap) - scroll;
            if (cy+cardH < cy0 || cy > cy0+contH) continue;

            boolean on  = mod.isEnabled();
            boolean hov = in(mx,my, cx, cy, cardW, cardH);
            boolean sel = mod == selectedModule;
            boolean bind= mod == bindingModule;

            int bg = bind ? 0xFF1A1A3A : sel ? 0xFF1E1E35 : on ? C_CARD_ON : hov ? C_CARD_H : C_CARD;
            c.fill(cx, cy, cx+cardW, cy+cardH, bg);

            if (sel || bind) {
                c.fill(cx, cy, cx+cardW, cy+1, C_ACC);
                c.fill(cx, cy+cardH-1, cx+cardW, cy+cardH, C_ACC);
            }
            if (on) c.fill(cx, cy, cx+3, cy+cardH, C_ACC);
            c.fill(cx, cy, cx+cardW, cy+1, C_SEP);

            c.drawText(textRenderer, mod.getName(), cx+10, cy+9, on ? C_WHITE : C_GRAY, false);

            String desc = mod.getDescription();
            if (textRenderer.getWidth(desc) > cardW-24) {
                while (textRenderer.getWidth(desc+"..") > cardW-24 && desc.length()>0)
                    desc = desc.substring(0, desc.length()-1);
                desc += "..";
            }
            c.drawText(textRenderer, desc, cx+10, cy+23, C_DGRAY, false);

            // Keybind badge
            int kb = mod.getKeybind();
            if (kb != -1) {
                String kn = "["+InputUtil.fromKeyCode(kb, 0).getLocalizedText().getString()+"]";
                int kw = textRenderer.getWidth(kn);
                c.fill(cx+cardW-kw-12, cy+cardH-14, cx+cardW-6, cy+cardH-4, 0xFF1A1A2A);
                c.drawText(textRenderer, kn, cx+cardW-kw-10, cy+cardH-13, C_ACC, false);
            }

            // Status dot
            c.fill(cx+cardW-12, cy+9, cx+cardW-6, cy+15, on ? C_ACC : C_DGRAY);
        }

        // Scrollbar
        if (maxScroll > 0) {
            int sbX2=px+SW+contW+8, sbY2=cy0, sbH2=contH;
            c.fill(sbX2, sbY2, sbX2+3, sbY2+sbH2, 0xFF111118);
            int th=Math.max(20, sbH2*sbH2/(sbH2+maxScroll));
            int tY=sbY2+(int)((float)scroll/maxScroll*(sbH2-th));
            c.fill(sbX2, tY, sbX2+3, tY+th, C_ACC);
        }

        // Settings panel
        if (showSettings) {
            int spx = px+PW-SP;
            c.fill(spx, py, px+PW, py+PH, C_SPANEL);
            c.fill(spx, py, spx+1, py+PH, C_ACC2);
            c.fill(spx, py+HH, px+PW, py+HH+1, C_SEP);

            // Header settings
            c.fill(spx, py, px+PW, py+HH, C_HDR);
            c.drawText(textRenderer, selectedModule.getName(), spx+10, py+9, C_WHITE, false);
            c.drawText(textRenderer, "Settings", spx+10, py+21, C_DGRAY, false);

            // Close settings [X]
            boolean hX = in(mx,my, px+PW-20, py+6, 14, 14);
            c.fill(px+PW-20, py+6, px+PW-6, py+20, hX ? 0xFF2A1A1A : 0xFF1A1A1A);
            c.drawText(textRenderer, "x", px+PW-16, py+9, hX ? 0xFFFF6666 : C_DGRAY, false);

            // Settings list
            List<Setting<?>> settings = getSettings(selectedModule);
            int sy = py+HH+10 - settingsScroll;

            for (Setting<?> s : settings) {
                if (sy > py+PH-10) break;
                if (sy+36 < py+HH) { sy += 42; continue; }

                c.fill(spx+6, sy, px+PW-6, sy+38, 0xFF131318);

                c.drawText(textRenderer, s.getName(), spx+12, sy+5, C_WHITE, false);
                c.drawText(textRenderer, s.getDescription(), spx+12, sy+16, C_DGRAY, false);

                if (s.getType() == Setting.Type.BOOLEAN) {
                    boolean val = (Boolean) s.getValue();
                    int pillX = px+PW-46, pillY = sy+11;
                    c.fill(pillX, pillY, pillX+28, pillY+13, val ? C_ACC : 0xFF252535);
                    c.fill(val ? pillX+17 : pillX+2, pillY+2, (val ? pillX+17 : pillX+2)+9, pillY+11, val ? C_WHITE : C_DGRAY);
                    // Klik
                    if (in(mx,my, pillX, pillY, 28, 13)) {
                        // handled in mouseClicked
                    }
                } else if (s.getType() == Setting.Type.FLOAT || s.getType() == Setting.Type.INTEGER) {
                    // Slider
                    int slW = SP-24, slH = 4;
                    int slX = spx+12, slY = sy+28;
                    c.fill(slX, slY, slX+slW, slY+slH, 0xFF252535);
                    float pct = s.getType() == Setting.Type.FLOAT ? s.getPercent() : s.getPercentInt();
                    int fillW = (int)(pct * slW);
                    c.fill(slX, slY, slX+fillW, slY+slH, C_ACC);
                    // Knob
                    int knobX = slX+fillW-4;
                    c.fill(knobX, slY-3, knobX+8, slY+slH+3, C_WHITE);
                    // Value
                    String valStr = s.getType() == Setting.Type.FLOAT
                        ? String.format("%.1f", (Float)s.getValue())
                        : String.valueOf((Integer)s.getValue());
                    c.drawText(textRenderer, valStr, px+PW-6-textRenderer.getWidth(valStr), sy+5, C_ACC, false);
                }

                sy += 42;
            }
        }

        // Keybind popup
        if (waitingForKey && bindingModule != null) {
            int popW=220, popH=72;
            int popX=width/2-popW/2, popY=height/2-popH/2;
            c.fill(popX-1, popY-1, popX+popW+1, popY+popH+1, C_ACC);
            c.fill(popX, popY, popX+popW, popY+popH, C_HDR);
            String title="Bind: "+bindingModule.getName();
            c.drawText(textRenderer, title, popX+(popW-textRenderer.getWidth(title))/2, popY+10, C_WHITE, false);
            String hint="Wcisnij klawisz...";
            c.drawText(textRenderer, hint, popX+(popW-textRenderer.getWidth(hint))/2, popY+26, C_ACC, false);
            String esc="[ESC] = usun bind";
            c.drawText(textRenderer, esc, popX+(popW-textRenderer.getWidth(esc))/2, popY+42, C_DGRAY, false);
            if (bindingModule.getKeybind() != -1) {
                String cur="Obecny: ["+InputUtil.fromKeyCode(bindingModule.getKeybind(),0).getLocalizedText().getString()+"]";
                c.drawText(textRenderer, cur, popX+(popW-textRenderer.getWidth(cur))/2, popY+56, C_DGRAY, false);
            }
        }
    }

    private void renderCatTab(DrawContext c, int mx, int my, Module.Category cat, int ty) {
        boolean sel = !searchMode && cat == selCat;
        boolean hov = !searchMode && in(mx,my, px, ty, SW, 26);
        if (sel) { c.fill(px, ty, px+SW, ty+26, 0xFF18182A); c.fill(px, ty, px+3, ty+26, C_ACC); }
        else if (hov) c.fill(px, ty, px+SW, ty+26, 0xFF131318);
        c.drawText(textRenderer, cat.icon+" "+cat.label, px+12, ty+8, sel ? C_WHITE : C_GRAY, false);
        long enCat = GrimClientMod.core.moduleManager.getByCategory(cat).stream().filter(Module::isEnabled).count();
        if (enCat > 0) {
            c.fill(px+SW-22, ty+6, px+SW-4, ty+20, 0xFF2D2850);
            String b=String.valueOf(enCat);
            c.drawText(textRenderer, b, px+SW-13-textRenderer.getWidth(b)/2, ty+9, C_ACC, false);
        }
    }

    // =========================================================
    //  INPUT
    // =========================================================
    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (waitingForKey) { waitingForKey=false; bindingModule=null; return true; }

        // Close settings X
        if (selectedModule != null && in((int)mx,(int)my, px+PW-20, py+6, 14, 14)) {
            selectedModule = null; return true;
        }

        // Settings panel clicks
        if (selectedModule != null) {
            int spx = px+PW-SP;
            List<Setting<?>> settings = getSettings(selectedModule);
            int sy = py+HH+10 - settingsScroll;
            for (Setting<?> s : settings) {
                if (s.getType() == Setting.Type.BOOLEAN) {
                    int pillX = px+PW-46, pillY = sy+11;
                    if (in((int)mx,(int)my, pillX, pillY, 28, 13)) {
                        ((Setting<Boolean>)s).setValue(!(Boolean)s.getValue());
                        return true;
                    }
                } else if (s.getType() == Setting.Type.FLOAT || s.getType() == Setting.Type.INTEGER) {
                    int slW = SP-24, slX = spx+12, slY = sy+28;
                    if (in((int)mx,(int)my, slX-4, slY-4, slW+8, slH()+10)) {
                        draggingSetting = s;
                        sliderStartX = slX;
                        updateSlider(s, (int)mx, slX, slW);
                        return true;
                    }
                }
                sy += 42;
            }
        }

        // Drag header
        if (btn==0 && in((int)mx,(int)my, px, py, PW, HH) && (selectedModule==null || (int)mx < px+PW-SP)) {
            drag=true; dragX=(int)mx-px; dragY=(int)my-py; return true;
        }

        // Search box
        int sbx=px+SW+10, sby=py+(HH-18)/2;
        if (btn==0 && in((int)mx,(int)my, sbx, sby, 180, 18)) { searchFocused=true; searchMode=true; return true; }
        if (searchFocused) searchFocused=false;

        // Category clicks
        if (!searchMode) {
            int tty=py+HH+22;
            for (Module.Category cat : new Module.Category[]{
                Module.Category.COMBAT, Module.Category.MOVEMENT,
                Module.Category.PLAYER, Module.Category.VISUALS}) {
                if (btn==0 && in((int)mx,(int)my, px, tty, SW, 26)) {
                    selCat=cat; scroll=0; searchMode=false; searchQuery="";
                    selectedModule=null; return true;
                }
                tty+=28;
            }
            tty+=12;
            if (btn==0 && in((int)mx,(int)my, px, tty, SW, 26)) {
                selCat=Module.Category.CLIENT; scroll=0; selectedModule=null; return true;
            }
        }

        // Module cards
        List<Module> mods = searchMode && !searchQuery.isEmpty()
            ? GrimClientMod.core.moduleManager.search(searchQuery)
            : GrimClientMod.core.moduleManager.getByCategory(selCat);

        int cx0=px+SW+8, cy0=py+HH+8;
        int contW = selectedModule!=null ? PW-SW-SP-16 : PW-SW-16;
        int cols = selectedModule!=null ? 1 : 2;
        int cardW = cols==1 ? contW-4 : (contW-6)/2;
        int cardH=46, gap=5;

        for (int i=0; i<mods.size(); i++) {
            int cx=cx0+(i%cols)*(cardW+(cols>1?6:0));
            int cy=cy0+(i/cols)*(cardH+gap)-scroll;
            if (in((int)mx,(int)my, cx, cy, cardW, cardH)) {
                if (btn==0) { mods.get(i).toggle(); return true; }
                if (btn==1) { selectedModule = mods.get(i); settingsScroll=0; return true; } // prawy klik
                if (btn==2) { bindingModule=mods.get(i); waitingForKey=true; return true; }  // środkowy
            }
        }

        return super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (drag) {
            px=Math.max(0,Math.min((int)mx-dragX, width-PW));
            py=Math.max(0,Math.min((int)my-dragY, height-PH));
        }
        if (draggingSetting != null) {
            int spx=px+PW-SP;
            int slW=SP-24, slX=spx+12;
            updateSlider(draggingSetting, (int)mx, slX, slW);
        }
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int btn) {
        drag=false; draggingSetting=null;
        return super.mouseReleased(mx, my, btn);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double h, double v) {
        if (selectedModule != null && mx > px+PW-SP) {
            settingsScroll = Math.max(0, settingsScroll-(int)(v*12));
        } else {
            scroll=Math.max(0,Math.min(maxScroll, scroll-(int)(v*12)));
        }
        return true;
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (waitingForKey && bindingModule!=null) {
            if (key==256) bindingModule.setKeybind(-1);
            else bindingModule.setKeybind(key);
            waitingForKey=false; bindingModule=null; return true;
        }
        if (key==80||key==256) {
            if (searchFocused) { searchFocused=false; return true; }
            if (selectedModule!=null) { selectedModule=null; return true; }
            this.client.setScreen(null); return true;
        }
        if (searchFocused) {
            if (key==259&&!searchQuery.isEmpty()) {
                searchQuery=searchQuery.substring(0,searchQuery.length()-1);
                if (searchQuery.isEmpty()) searchMode=false;
                return true;
            }
            if (key==256) { searchFocused=false; searchMode=false; searchQuery=""; return true; }
        }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public boolean charTyped(char c, int mods) {
        if (searchFocused&&c>=32) { searchQuery+=c; searchMode=true; scroll=0; return true; }
        return false;
    }

    // =========================================================
    //  Helpers
    // =========================================================
    private void updateSlider(Setting<?> s, int mx, int slX, int slW) {
        float pct = Math.max(0f, Math.min(1f, (float)(mx-slX)/slW));
        if (s.getType() == Setting.Type.FLOAT) {
            float mn=(Float)s.getMin(), range=(Float)s.getMax()-mn;
            float val = mn + pct*range;
            val = Math.round(val*10f)/10f;
            ((Setting<Float>)s).setValue(val);
        } else if (s.getType() == Setting.Type.INTEGER) {
            int mn=(Integer)s.getMin(), range=(Integer)s.getMax()-mn;
            ((Setting<Integer>)s).setValue(mn + (int)(pct*range));
        }
    }

    private int slH() { return 4; }

    private List<Setting<?>> getSettings(Module mod) {
        List<Setting<?>> list = new ArrayList<>();
        for (Field f : mod.getClass().getFields()) {
            if (Setting.class.isAssignableFrom(f.getType())) {
                try { list.add((Setting<?>)f.get(mod)); } catch (Exception ignored) {}
            }
        }
        return list;
    }

    private boolean in(int mx, int my, int x, int y, int w, int h) {
        return mx>=x&&mx<x+w&&my>=y&&my<y+h;
    }

    private void drawSkull(DrawContext c, int x, int y) {
        c.fill(x+2,y+1,  x+14,y+11, 0xFFDDDDCC);
        c.fill(x,  y+3,  x+2, y+9,  0xFFDDDDCC);
        c.fill(x+14,y+3, x+16,y+9,  0xFFDDDDCC);
        c.fill(x+2,y,    x+14,y+2,  0xFF111111);
        c.fill(x,  y+2,  x+16,y+4,  0xFF111111);
        c.fill(x+3,y+4,  x+6, y+7,  0xFF111111);
        c.fill(x+10,y+4, x+13,y+7,  0xFF111111);
        c.fill(x+4,y+5,  x+5, y+6,  0xFFCC2200);
        c.fill(x+11,y+5, x+12,y+6,  0xFFCC2200);
    }
}
