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

package com.kantenkugel.kanzebot.api.command;

import com.kantenkugel.kanzebot.api.RequireType;
import com.kantenkugel.kanzebot.api.group.Group;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to specify Group/Permission requirement of a Command
 */
public class Requirement {
    private final RequireType type;
    private final Set<Group> groupSet;
    private final Set<Permission> permSet;

    /**
     * Creates a new Requirement which only checks for a single permission
     *
     * @param permission
     *      The permission that should be checked for
     */
    public Requirement(Permission permission) {
        type = null;
        permSet = new HashSet<>();
        permSet.add(permission);
        groupSet = null;
    }

    /**
     * Creates a new Requirement which checks for multiple permissions
     *
     * @param type
     *      Determines whether the permissions should be OR'ed (only one has to be met) or AND'ed (all must be present)
     * @param permissions
     *      The permissions that should be checked for
     */
    public Requirement(RequireType type, Permission... permissions) {
        this.type = type;
        permSet = new HashSet<>(Arrays.asList(permissions));
        groupSet = null;
    }

    /**
     * Creates a new Requirement which only checks for a single group
     *
     * @param group
     *      The group that should be checked for
     */
    public Requirement(Group group) {
        type = null;
        groupSet = new HashSet<>();
        groupSet.add(group);
        permSet = null;
    }

    /**
     * Creates a new Requirement which checks for multiple groups
     *
     * @param type
     *      Determines whether the groups should be OR'ed (only one has to be met) or AND'ed (all must be present)
     * @param groups
     *      The groups that should be checked for
     */
    public Requirement(RequireType type, Group... groups) {
        this.type = type;
        groupSet = new HashSet<>(Arrays.asList(groups));
        permSet = null;
    }

    public final boolean isMet(TextChannel channel, User user) {
        return true;//todo logic pls
    }
}
