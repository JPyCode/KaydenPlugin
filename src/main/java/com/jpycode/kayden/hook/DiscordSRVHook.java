package com.jpycode.kayden.hook;

import github.scarsz.discordsrv.DiscordSRV;

public class DiscordSRVHook {
    private static final DiscordSRVHook instance = new DiscordSRVHook();

    private DiscordSRVHook() {

    }
    public static void register() {
        DiscordSRV.api.subscribe(instance);
    }

    public static void unregister() {
        DiscordSRV.api.subscribe(instance);
    }
}
