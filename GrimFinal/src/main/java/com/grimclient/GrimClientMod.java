package com.grimclient;

import com.grimclient.client.GrimCore;
import com.grimclient.protection.GrimProtection;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrimClientMod implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("grimclient");
    public static final String NAME    = "Grim Client";
    public static final String VERSION = "1.0.0";
    public static final String AUTHOR  = "GrimTeam";

    public static GrimCore core;

    @Override
    public void onInitializeClient() {
        // Weryfikacja integralności — zabezpieczenie przed przeróbkami
        GrimProtection.verify();

        LOGGER.info("[GrimClient] Loading {} v{}...", NAME, VERSION);
        core = new GrimCore();
        core.init();
        LOGGER.info("[GrimClient] Loaded! Press P to open GUI.");
    }
}
