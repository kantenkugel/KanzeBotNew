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

import com.kantenkugel.kanzebot.api.config.AddonConfig;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigImpl extends PropertiesConfiguration implements AddonConfig, ConfigurationListener {

    private static Pattern versionPattern = Pattern.compile(".*\\#config.version\\=(\\d*)\\;.*");

    private boolean hasChanged = false;
    private boolean isNew = false;

    private int versionChanged = -1;

    private PropertiesConfigurationLayout layout;

    public ConfigImpl(File file, String comment, int configVersion) throws ConfigurationException {
        super(file);
        layout = getLayout();
        super.addConfigurationListener(this);


        String version = "#config.version=" + configVersion + ";";
        String currentHeader = layout.getHeaderComment();
        String newHeader = version;

        if(comment != null) {
            newHeader = comment + " " + newHeader;
        }

        if(this.isEmpty()) {
            isNew = true;
        } else if(!currentHeader.contains(version)) {
            Matcher matcher = versionPattern.matcher(currentHeader);
            if(matcher.matches()) {
                versionChanged = Integer.parseInt(matcher.group(1));
            } else {
                versionChanged = 0;
            }
            hasChanged = true;
        }

        layout.setHeaderComment(newHeader);
    }

    @Override
    public <T extends Enum<T>> T getEnum(String key, T defaultValue, String comment) {
        key = "E_" + key;
        T out;
        String stringVal = getString(key);
        if(stringVal == null) {
            out = defaultValue;
            setProperty(key, out.toString());
        } else {
            try {
                out = T.valueOf(defaultValue.getDeclaringClass(), stringVal);
            } catch (IllegalArgumentException ex) {
                out = defaultValue;
                setProperty(key, out.toString());
            }
        }
        String header = Arrays.toString(defaultValue.getDeclaringClass().getEnumConstants());
        if(comment != null) {
            header = comment + " " + header;
        }
        layout.setComment(key, header);
        layout.setBlancLinesBefore(key, 1);

        return out;
    }

    @Override
    public <T extends Enum<T>> void setEnum(String key, T value, String comment) {
        key = "E_" + key;
        String header = Arrays.toString(value.getDeclaringClass().getEnumConstants());
        if(comment != null) {
            header = comment + " " + header;
        }
        set(key, value, header);
    }

    @Override
    public void deleteEnum(String key) {
        clearProperty("E_" + key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue, String comment) {
        key = "B_" + key;
        boolean out;
        try {
            out = getBoolean(key);
        } catch (NoSuchElementException ex) {
            out = defaultValue;
            super.setProperty(key, out);
        }
        if(comment != null) {
            layout.setComment(key, comment);
            layout.setBlancLinesBefore(key, 1);
        }
        return out;
    }

    @Override
    public void setBoolean(String key, boolean value, String comment) {
        set("B_" + key, value, comment);
    }

    @Override
    public void deleteBoolean(String key) {
        clearProperty("B_" + key);
    }

    @Override
    public int getInteger(String key, int defaultValue, String comment) {
        key = "I_" + key;
        int out;
        try {
            out = getInt(key);
        } catch (NoSuchElementException ex) {
            out = defaultValue;
            super.setProperty(key, out);
        }
        if(comment != null) {
            layout.setComment(key, comment);
            layout.setBlancLinesBefore(key, 1);
        }
        return out;
    }

    @Override
    public void setInteger(String key, int value, String comment) {
        set("I_" + key, value, comment);
    }

    @Override
    public void deleteFloat(String key) {
        clearProperty("F_" + key);
    }

    @Override
    public float getFloat(String key, float defaultValue, String comment) {
        key = "F_" + key;
        float out;
        try {
            out = getFloat(key);
        } catch (NoSuchElementException ex) {
            out = defaultValue;
            super.setProperty(key, out);
        }
        if(comment != null) {
            layout.setComment(key, comment);
            layout.setBlancLinesBefore(key, 1);
        }
        return out;
    }

    @Override
    public void setFloat(String key, float value, String comment) {
        set("F_" + key, value, comment);
    }

    @Override
    public void deleteInteger(String key) {
        clearProperty("I_" + key);
    }

    @Override
    public String getString(String key, String defaultValue, String comment) {
        key = "S_" + key;
        String out = getString(key);
        if(out == null) {
            out = defaultValue;
            setProperty(key, out);
        }
        if(comment != null) {
            layout.setComment(key, comment);
            layout.setBlancLinesBefore(key, 1);
        }
        return out;
    }

    @Override
    public void setString(String key, String value, String comment) {
        set("S_" + key, value, comment);
    }

    @Override
    public void deleteString(String key) {
        clearProperty("S_" + key);
    }

    @Override
    public List<String> getList(String key, List<String> defaultValue, String comment) {
        key = "L_" + key;
        List<String> out = getList(key).parallelStream().map(String.class::cast).collect(Collectors.toList());
        if(out.isEmpty()) {
            out = defaultValue;
            setProperty(key, out.isEmpty() ? "" : out);
        }
        if(comment != null) {
            layout.setComment(key, comment);
            layout.setBlancLinesBefore(key, 1);
        }
        return out;
    }

    @Override
    public void setList(String key, List<String> value, String comment) {
        set("L_" + key, value.isEmpty() ? "" : value, comment);
    }

    @Override
    public void deleteList(String key) {
        clearProperty("L_" + key);
    }

    @Override
    public Set<String> getSet(String key, Set<String> defaultValue, String comment) {
        key = "T_" + key;
        Set<String> out = getList(key).parallelStream().map(String.class::cast).collect(Collectors.toSet());
        if(out.isEmpty()) {
            out = defaultValue;
            setProperty(key, out.isEmpty() ? "" : out);
        }
        if(comment != null) {
            layout.setComment(key, comment);
            layout.setBlancLinesBefore(key, 1);
        }
        return out;
    }

    @Override
    public void setSet(String key, Set<String> value, String comment) {
        set("T_" + key, value.isEmpty() ? "" : value, comment);
    }

    @Override
    public void deleteSet(String key) {
        clearProperty("T_" + key);
    }

    @Override
    public void configurationChanged(ConfigurationEvent event) {
        hasChanged = true;
    }

    @Override
    public boolean hasChanged() {
        return hasChanged;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public boolean hasVersionChanged() {
        return versionChanged >= 0;
    }

    @Override
    public int getOldVersion() {
        return versionChanged;
    }

    private void set(String key, Object value, String comment) {
        setProperty(key, value);
        if(comment != null) {
            layout.setComment(key, comment);
            layout.setBlancLinesBefore(key, 1);
        }
    }
}