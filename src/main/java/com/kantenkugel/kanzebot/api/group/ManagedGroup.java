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

import com.kantenkugel.kanzebot.api.RequireType;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.Arrays;

public class ManagedGroup implements Group {
    private final String name;
    private final Permission[] perms;
    private final RequireType type;

    public ManagedGroup(String name, Permission p) {
        this.name = name;
        this.perms = new Permission[]{p};
        this.type = RequireType.AND;
    }

    public ManagedGroup(String name, RequireType type, Permission... perms) {
        this.name = name;
        this.type = type;
        this.perms = perms;
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
    public boolean isMember(TextChannel channel, User user) {
        if(channel == null)
            return false;
        if(type == RequireType.AND)
            return channel.checkPermission(user, perms);
        else
            return Arrays.stream(perms).anyMatch(p -> channel.checkPermission(user, p));
    }
}
