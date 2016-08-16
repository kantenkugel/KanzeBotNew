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

import com.kantenkugel.kanzebot.api.command.Command;
import com.kantenkugel.kanzebot.api.config.AddonConfig;
import com.kantenkugel.kanzebot.api.config.GuildConfig;
import com.kantenkugel.kanzebot.api.group.Group;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.hooks.EventListener;

/**
 * This class requires all neccessary hooks to create/get entities.
 */
public interface KanzeHook {

    /**
     * Returns the GuildConfig for given Guild.
     *
     * @param guild
     *      The Guild of interest.
     * @return
     *      The Guild's GuildConfig
     */
    GuildConfig getGuildConfig(Guild guild);

    /**
     * Returns the Addon's specific configuration
     *
     * @return
     *      The Addon's specific configuration
     */
    AddonConfig getConfig();

    /**
     * Registers a Command
     *
     * @param cmd
     *      The Command to register.
     * @return
     *      <i>false</i> if there was a name-conflict, otherwise <i>true</i>
     */
    boolean registerCommand(Command cmd);

    /**
     * Un-Registers a Command
     *
     * @param cmd
     *      The Command to un-register.
     * @return
     *      <i>false</i> if there was a name- or permission-conflict, otherwise <i>true</i>
     */
    boolean unRegisterCommand(Command cmd);

    /**
     * Retrieves a Group with given name.
     *
     * @param name
     *      The name of the Group to retrieve
     * @return
     *      The Group or <i>null</i> if the Group didn't exist
     */
    Group getGroup(String name);

    /**
     * Registers a new Group
     *
     * @param group
     *      The Group to register
     * @return
     *      <i>false</i> if there was a name-conflict, otherwise <i>true</i>
     */
    boolean registerGroup(Group group);

    /**
     * Un-Registers a Group
     *
     * @param group
     *      The Group to un-register
     * @return
     *      <i>false</i> if there was a permission-conflict or the Group wasn't registered, otherwise <i>true</i>
     */
    boolean removeGroup(Group group);

    /**
     * Registers a custom Event-listener which listens to all JDA-Events
     *
     * @param listener
     *      The JDA-Listener to register
     */
    void registerJDAListener(EventListener listener);

    /**
     * Un-Registers a custom Event-listener which listens to all JDA-Events
     *
     * @param listener
     *      The JDA-Listener to un-register
     */
    void unRegisterJDAListener(EventListener listener);
}
