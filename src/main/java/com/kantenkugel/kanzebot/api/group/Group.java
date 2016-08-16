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

import java.util.Set;

/**
 * A group contains a Collection of users and is used for access-check for commands.<br>
 * There are 2 kinds of Groups: a managed one (the Addon manages its members)
 * and a unmanaged one (server-owners can add/remove members via command)<br><br>
 *
 * Examples are: <br>
 * Managed: BotAdmin<br>
 * UnManaged: Admin, Mod<br><br>
 *
 * <b>Note:</b> Unless using one of the provided Group implementations,
 * you need to take care of storing the members to disk yourself!
 */
public interface Group {

    /**
     * Returns whether or not this Group is managed.
     * For an explanation see the Class docs
     *
     * @return
     *      If the Group is managed.
     */
    boolean isManaged();

    /**
     * Returns the name of the Group.
     * This name is used internally to store its members to disk and in case of unmanaged Groups: in the command to add/remove members
     *
     * @return
     *      This Group's name
     */
    String getName();

    /**
     * Returns whether or not given {@link User} is part of the group (also specifying the {@link Guild} of interest)
     *
     * @param guild
     *      The Guild for the check
     * @param user
     *      The User to check for membership
     * @return
     *      true, if the User is member of this Group
     */
    boolean isMember(Guild guild, User user);

    /**
     * This method is called on unmanaged Groups to add Users.
     * It is never called by KanzeBot for managed Groups.
     *
     * @param guild
     *      The Guild of interest
     * @param user
     *      The User to add.
     */
    void addUser(Guild guild, User user);

    /**
     * This method is called on unmanaged Groups to remove a User.
     * It is never called by KanzeBot for managed Groups.
     *
     * @param guild
     *      The Guild of interest
     * @param user
     *      The user to remove
     */
    void removeUser(Guild guild, User user);

}
