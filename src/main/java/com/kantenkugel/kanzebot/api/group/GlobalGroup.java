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

package com.kantenkugel.kanzebot.api.group;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.HashSet;
import java.util.Set;

/**
 * This Group is used for Global groups (like BotAdmin) and takes care of storing its members to disk automatically
 */
//TODO: actually do persistence stuff
public class GlobalGroup implements Group {
    private final String name;
    private final Set<String> members = new HashSet<>();

    public GlobalGroup(String name) {
        this.name = name;
    }

    @Override
    public boolean isManaged() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isMember(Guild guild, User user) {
        return members.contains(user.getId());
    }

    @Override
    public void addUser(Guild guild, User user) {
        members.add(user.getId());
    }

    @Override
    public void removeUser(Guild guild, User user) {
        members.remove(user.getId());
    }

    public void addUser(User user) {
        addUser(null, user);
    }

    public void removeUser(User user) {
        removeUser(null, user);
    }
}
