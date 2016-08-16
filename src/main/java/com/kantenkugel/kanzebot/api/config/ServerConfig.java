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
 * Used to retrieve/store server-specific config entries
 */
public interface ServerConfig {

    String getPrefix();

    boolean isEveryoneStripped();

    String getString(String key, String defaultValue);

    void setString(String key, String value);

    boolean getBoolean(String key, boolean defaultValue);

    void setBoolean(String key, boolean value);

    int getInteger(String key, int defaultValue);

    void setInteger(String key, int value);

    List<String> getList(String key, List<String> defaultValue);

    void setList(String key, List<String> value);

    Set<String> getSet(String key, Set<String> defaultValue);

    void setSet(String key, Set<String> value);

    Map<String, String> getMap(String key, Map<String, String> defaultValue);

    void setMap(String key, Map<String, String> value);

    void updateMapEntry(String mapStoreKey, String key, String value);

    void remove(String key);

}
