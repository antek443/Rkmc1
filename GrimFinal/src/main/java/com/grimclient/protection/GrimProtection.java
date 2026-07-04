package com.grimclient.protection;

import com.grimclient.GrimClientMod;
import net.minecraft.client.MinecraftClient;

/**
 * Grim Client Protection System
 * Prevents unauthorized rebranding, redistribution and modification.
 *
 * © GrimTeam - All Rights Reserved
 * Unauthorized modification, redistribution or reselling is PROHIBITED.
 */
public class GrimProtection {

    // Chronione stałe — zmiana tych wartości powoduje crash
    private static final String EXPECTED_NAME    = "Grim Client";
    private static final String EXPECTED_AUTHOR  = "GrimTeam";
    private static final long   MAGIC_HASH       = 0x472D524D434C4E54L; // "G-RMCLNT"
    private static final String WATERMARK        = "\u00A78[\u00A7dGrimClient\u00A78] \u00A77\u00A9 GrimTeam - All Rights Reserved";

    private static boolean verified = false;

    public static void verify() {
        // Sprawdź czy mod nie został przemianowany
        if (!GrimClientMod.NAME.equals(EXPECTED_NAME)) {
            crash("Integrity check failed: Name mismatch. Unauthorized modification detected.");
        }

        // Sprawdź autora
        if (!GrimClientMod.AUTHOR.equals(EXPECTED_AUTHOR)) {
            crash("Integrity check failed: Author mismatch. Unauthorized modification detected.");
        }

        // Sprawdź hash
        long nameHash = hashString(GrimClientMod.NAME + GrimClientMod.AUTHOR);
        if (nameHash != MAGIC_HASH) {
            // Soft check — nie crashuj ale loguj
            GrimClientMod.LOGGER.warn("[GrimClient] Protection: Integrity warning!");
        }

        verified = true;
        GrimClientMod.LOGGER.info("[GrimClient] Protection: Verified OK");
    }

    // Crash JVM z komunikatem
    private static void crash(String reason) {
        GrimClientMod.LOGGER.error("[GrimClient] PROTECTION: {}", reason);
        throw new RuntimeException(
            "\n\n" +
            "╔══════════════════════════════════════════╗\n" +
            "║         GRIM CLIENT - PROTECTION         ║\n" +
            "╠══════════════════════════════════════════╣\n" +
            "║  Unauthorized modification detected!     ║\n" +
            "║  This client is protected by GrimTeam.   ║\n" +
            "║  Redistribution is strictly prohibited.  ║\n" +
            "╚══════════════════════════════════════════╝\n"
        );
    }

    // Prosta funkcja hashująca
    private static long hashString(String s) {
        long hash = 0x472D524D434C4E54L;
        for (char c : s.toCharArray()) {
            hash = hash ^ ((long) c << (hash % 32));
        }
        return hash;
    }

    // Watermark w tytule okna
    public static String getWindowTitle(String original) {
        return original + " | Grim Client v" + GrimClientMod.VERSION;
    }

    // Wyświetl watermark w chacie
    public static void sendWatermark() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.sendMessage(
                net.minecraft.text.Text.literal(WATERMARK),
                true
            );
        }
    }

    public static boolean isVerified() { return verified; }
}
