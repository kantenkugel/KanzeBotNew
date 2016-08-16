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

import org.apache.commons.configuration.ConfigurationException;

import java.util.List;

/**
 * Given to every Addon to store its own configuration
 */
public interface AddonConfig {
    /**
     * Retrieves an enum value from the configuration
     *
     * @param <T>          the Enum
     * @param key          the key of the enum value
     * @param defaultValue the default value that should be used if the key didnt exist in the configuration
     * @param comment      the comment that should be set if the key didnt exist yet, or null
     * @return the enum value as read from the configuration
     */
    public <T extends Enum<T>> T getEnum(String key, T defaultValue, String comment);

    /**
     * Sets an enum value in the configuration
     *
     * @param <T>     the Enum
     * @param key     the key of the enum value
     * @param value   the value that should be used
     * @param comment the comment that should be set, or null
     */
    public <T extends Enum<T>> void setEnum(String key, T value, String comment);

    /**
     * Deletes a enum-value from the configuration (if it existed)
     *
     * @param key the key of the enum-value
     */
    public void deleteEnum(String key);

    /**
     * Retrieves a boolean value from the configuration
     *
     * @param key          the key of the boolean value
     * @param defaultValue the default value that should be used if the key didnt exist in the configuration
     * @param comment      the comment that should be set if the key didnt exist yet, or null
     * @return the boolean value as read from the configuration
     */
    public boolean getBoolean(String key, boolean defaultValue, String comment);

    /**
     * Sets an boolean value in the configuration
     *
     * @param key     the key of the boolean value
     * @param value   the value that should be used
     * @param comment the comment that should be set, or null
     */
    public void setBoolean(String key, boolean value, String comment);

    /**
     * Deletes a boolean value from the configuration (if it existed)
     *
     * @param key the key of the boolean value
     */
    public void deleteBoolean(String key);

    /**
     * Retrieves an integer value from the configuration
     *
     * @param key          the key of the integer value
     * @param defaultValue the default value that should be used if the key didnt exist in the configuration
     * @param comment      the comment that should be set if the key didnt exist yet, or null
     * @return the integer value as read from the configuration
     */
    public int getInteger(String key, int defaultValue, String comment);

    /**
     * Sets an integer value in the configuration
     *
     * @param key     the key of the integer value
     * @param value   the value that should be used
     * @param comment the comment that should be set, or null
     */
    public void setInteger(String key, int value, String comment);

    /**
     * Deletes a integer value from the configuration (if it existed)
     *
     * @param key the key of the integer value
     */
    public void deleteInteger(String key);

    /**
     * Retrieves a float value from the configuration
     *
     * @param key          the key of the float value
     * @param defaultValue the default value that should be used if the key didnt exist in the configuration
     * @param comment      the comment that should be set if the key didnt exist yet, or null
     * @return the float value as read from the configuration
     */
    public float getFloat(String key, float defaultValue, String comment);

    /**
     * Sets a float value in the configuration
     *
     * @param key     the key of the float value
     * @param value   the value that should be used
     * @param comment the comment that should be set, or null
     */
    public void setFloat(String key, float value, String comment);

    /**
     * Deletes a float value from the configuration (if it existed)
     *
     * @param key the key of the float value
     */
    public void deleteFloat(String key);

    /**
     * Retrieves a String value from the configuration
     *
     * @param key          the key of the String value
     * @param defaultValue the default value that should be used if the key didnt exist in the configuration
     * @param comment      the comment that should be set if the key didnt exist yet, or null
     * @return the String value as read from the configuration
     */
    public String getString(String key, String defaultValue, String comment);

    /**
     * Sets a String value in the configuration
     *
     * @param key     the key of the String value
     * @param value   the value that should be used
     * @param comment the comment that should be set, or null
     */
    public void setString(String key, String value, String comment);

    /**
     * Deletes a String value from the configuration (if it existed)
     *
     * @param key the key of the String value
     */
    public void deleteString(String key);

    /**
     * Retrieves a List of strings from the configuration
     *
     * @param key          the key of the String-list
     * @param defaultValue the default value that should be used if the key didnt exist in the configuration
     * @param comment      the comment that should be set if the key didnt exist yet, or null
     * @return the String-list as read from the configuration
     */
    public List<String> getList(String key, List<String> defaultValue, String comment);

    /**
     * Sets a String-list value in the configuration
     *
     * @param key     the key of the String-list
     * @param value   the list that should be used
     * @param comment the comment that should be set, or null
     */
    public void setList(String key, List<String> value, String comment);

    /**
     * Deletes a String-list from the configuration (if it existed)
     *
     * @param key the key of the String-list
     */
    public void deleteList(String key);

    /**
     * Returns whether this configuration has changed in memory, and is out of date with the file
     *
     * @return boolean true if the internal configuration object is not the same as the original file
     */
    public boolean hasChanged();

    /**
     * Returns whether this configuration object was created for the first time (there will be no entries)
     *
     * @return boolean true if this configuration was newly created
     */
    public boolean isNew();

    /**
     * Returns whether the read configuration has an older version than this one
     * Can be used to update missing data or to rename old entry-names
     *
     * @return boolean true if the version has changed
     */
    public boolean hasVersionChanged();

    /**
     * This method return the version of the old configuration file
     * See the method hasVersionChanged() for more details
     *
     * @return int version-number
     */
    public int getOldVersion();

    /**
     * Tries to save the configuration to the disk
     *
     * @throws ConfigurationException if an error occures
     */
    public void save() throws ConfigurationException;

}
