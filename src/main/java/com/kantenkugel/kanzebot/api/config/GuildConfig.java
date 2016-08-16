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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Used to retrieve/store server-specific config entries<br><br>
 *
 * <b>Note:</b> all type getters/setters are specific to the Addon using them to prevent conflicts.<br>
 * <b>Note 2:</b> other than the {@link AddonConfig} changes made here are applied instantly.
 */
public interface GuildConfig {

    /**
     * Returns KanzeBot's configured Prefix for this Guild
     *
     * @return
     *      The configured command prefix
     */
    String getPrefix();

    /**
     * Returns whether or not KanzeBot is configured to escape @everyone and @here (default: true)
     *
     * @return
     *      true if KanzeBot escapes @everyone and @here in this guild
     */
    boolean isEveryoneEscaped();

    /**
     * Retrieves a String from the configuration.
     * If the configuration didn't contain the given String,
     * the default will be returned and written into the configuration. No need to call the setter
     *
     * @param key
     *      The key where the string is stored.
     * @param defaultValue
     *      A default-value if the key didn't exist before.
     * @return
     *      The string from the config-file or the default value if it didn't exist
     */
    String getString(String key, String defaultValue);

    /**
     * Stores a string into the configuration
     *
     * @param key
     *      The key where the string should be stored
     * @param value
     *      The value that should be stored
     */
    void setString(String key, String value);

    /**
     * Retrieves a boolean from the configuration.
     * If the configuration didn't contain the given boolean,
     * the default will be returned and written into the configuration. No need to call the setter
     *
     * @param key
     *      The key where the boolean is stored.
     * @param defaultValue
     *      A default-value if the key didn't exist before.
     * @return
     *      The boolean from the config-file or the default value if it didn't exist
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Stores a boolean into the configuration
     *
     * @param key
     *      The key where the boolean should be stored
     * @param value
     *      The value that should be stored
     */
    void setBoolean(String key, boolean value);

    /**
     * Retrieves a integer from the configuration.
     * If the configuration didn't contain the given integer,
     * the default will be returned and written into the configuration. No need to call the setter
     *
     * @param key
     *      The key where the integer is stored.
     * @param defaultValue
     *      A default-value if the key didn't exist before.
     * @return
     *      The integer from the config-file or the default value if it didn't exist
     */
    int getInteger(String key, int defaultValue);

    /**
     * Stores a integer into the configuration
     *
     * @param key
     *      The key where the integer should be stored
     * @param value
     *      The value that should be stored
     */
    void setInteger(String key, int value);

    /**
     * Retrieves a List from the configuration.
     * If the configuration didn't contain the given List,
     * the default will be returned and written into the configuration. No need to call the setter
     *
     * @param key
     *      The key where the List is stored.
     * @param defaultValue
     *      A default-value if the key didn't exist before.
     * @return
     *      The List from the config-file or the default value if it didn't exist
     */
    List<String> getList(String key, List<String> defaultValue);

    /**
     * Stores a List into the configuration
     *
     * @param key
     *      The key where the List should be stored
     * @param value
     *      The value that should be stored
     */
    void setList(String key, List<String> value);

    /**
     * Retrieves a Set from the configuration.
     * If the configuration didn't contain the given Set,
     * the default will be returned and written into the configuration. No need to call the setter
     *
     * @param key
     *      The key where the Set is stored.
     * @param defaultValue
     *      A default-value if the key didn't exist before.
     * @return
     *      The Set from the config-file or the default value if it didn't exist
     */
    Set<String> getSet(String key, Set<String> defaultValue);

    /**
     * Stores a Set into the configuration
     *
     * @param key
     *      The key where the Set should be stored
     * @param value
     *      The value that should be stored
     */
    void setSet(String key, Set<String> value);

    /**
     * Retrieves a Map from the configuration.
     * If the configuration didn't contain the given Map,
     * the default will be returned and written into the configuration. No need to call the setter
     *
     * @param key
     *      The key where the Map is stored.
     * @param defaultValue
     *      A default-value if the key didn't exist before.
     * @return
     *      The Map from the config-file or the default value if it didn't exist
     */
    Map<String, String> getMap(String key, Map<String, String> defaultValue);

    /**
     * Stores a Map into the configuration
     *
     * @param key
     *      The key where the Map should be stored
     * @param value
     *      The value that should be stored
     */
    void setMap(String key, Map<String, String> value);

    /**
     * Updates a single map-entry instead of wrinting the whole map to disk again
     *
     * @param mapStoreKey
     *      The key where the Map is stored
     * @param key
     *      The key of the Map-entry you want to change
     * @param value
     *      The new value of the Map-entry
     */
    void updateMapEntry(String mapStoreKey, String key, String value);

    /**
     * Removes any variable with given key from the configuration
     *
     * @param key
     *      The key of the configuration to remove
     */
    void remove(String key);

}
