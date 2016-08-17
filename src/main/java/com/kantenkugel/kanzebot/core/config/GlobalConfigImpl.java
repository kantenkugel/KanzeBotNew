/*
 * Copyright 2016 Michael Ritter (Kantenkugel)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kantenkugel.kanzebot.core.config;

import com.kantenkugel.kanzebot.api.config.GlobalConfig;
import com.kantenkugel.kanzebot.core.util.Injector;
import org.apache.commons.configuration.ConfigurationException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class GlobalConfigImpl extends ConfigImpl implements GlobalConfig {
    private static final int VERSION = 1;
    private static final File FILE = new File("kanzebot.cfg");

    private String botOwner;
    private Set<String> botAdmins;
    private boolean authMode;
    private Set<String> authedGuilds;


    public GlobalConfigImpl() throws ConfigurationException {
        super(FILE, "This File contains the global configurations of KanzeBot", VERSION);
        read();
    }

    @Override
    public String getBotOwner() {
        return botOwner;
    }

    @Override
    public Set<String> getBotAdmins() {
        return botAdmins;
    }

    @Override
    public boolean inAuthMode() {
        return authMode;
    }

    @Override
    public Set<String> getAuthedGuilds() {
        return authedGuilds;
    }

    private void read() {
        botOwner = getString("BotOwnerId", "", "The ID of the BotOwner (Access to all Commands)");
        botAdmins = getSet("BotAdmins", new HashSet<>(0), "The Set of IDs of the BotAdmins (Access to (almost) all Commands)");
        authMode = getBoolean("InAuthMode", false, "Determines whether or not the Bot runs in Auth-Mode (instantly leaves guilds not first accepted by a BotAdmin)");
        authedGuilds = getSet("AuthedGuilds", new HashSet<>(0), "The set of authed guilds (is ignored if not in auth-mode)");
        if(hasChanged()) {
            try {
                save();
            } catch(ConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Injector.inject(GlobalConfig.class, "instance", new GlobalConfigImpl(), null);
            System.out.println(GlobalConfig.getInstance().inAuthMode());
        } catch(ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
