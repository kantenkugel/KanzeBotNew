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

package com.kantenkugel.kanzebot.api.config;

import java.util.Set;

public interface GlobalConfig {
    GlobalConfig instance = null;
    static GlobalConfig getInstance() {
        return instance;
    }

    /**
     * Returns the ID of the configured BotOwner (all permissions)
     *
     * @return
     *      ID of bot-owner
     */
    String getBotOwner();

    /**
     * Returns a set of ID's representing the BotAdmins (all perms except addon commands)
     *
     * @return
     *      Set of bot-admin ids
     */
    Set<String> getBotAdmins();

    /**
     * Returns whether or not the Bot is running in Auth-Mode (Guilds need to be confirmed first before being able to invite).
     * By default, Auth-Mode is turned off (to invite to initial Guilds)
     *
     * @return
     *      <i>true</i> if in auth-mode
     */
    boolean inAuthMode();

    /**
     * Returns a set of ID's representing the whitelisted Guilds (for Auth-Mode)
     *
     * @return
     *      Set of whitelisted Guilds' ids
     */
    Set<String> getAuthedGuilds();
}
