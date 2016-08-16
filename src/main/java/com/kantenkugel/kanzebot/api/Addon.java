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

package com.kantenkugel.kanzebot.api;

import com.kantenkugel.kanzebot.api.config.AddonConfig;

/**
 * This class is the entry-point for all Addons and has to be implemented by every Addon.<br>
 * When loading a jar-file, it is searched for Classes implementing this Interface.
 */
public interface Addon {
    /**
     * Returns the identifier for this Addon.
     * The identifier has to be unique over Addons and is used to load/unload the Addon.
     *
     * @return
     *      The Addon-specific identifier
     */
    String getIdentifier();

    /**
     * Returns the config-version this Addon uses.
     * This is used to detect old config files and update them accordingly (see {@link com.kantenkugel.kanzebot.api.config.AddonConfig})
     *
     * @return
     *      The most recent config-version
     */
    int getConfigVersion();

    /**
     * This method is called once the Jar-file containing the Addon is read/loaded
     *
     * @param config
     *      The Addon-specific config-file
     */
    void init(AddonConfig config);

    /**
     * This method is called before the Jar-file containing the Addon is decoupled/unloaded.
     * All Entities created by this Addon and not already freed by either {@link #unload()} or this method are forcefully destroyed.
     */
    void destroy();

    /**
     * This method is called when the Addon is loaded via command.
     * This method should register all needed Entities like Commands, Groups, ...
     *
     * @param hook
     *      The class containing all hooks needed to create/get entities
     */
    void load(KanzeHook hook);

    /**
     * This method is called when the Addon is unloaded via command.
     * This method should unregister all Entities like Commands, Groups, ...
     */
    void unload();
}
