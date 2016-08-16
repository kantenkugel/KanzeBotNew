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

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

/**
 * This represents a single command usable by KanzeBot
 */
public interface Command {

    /**
     * Returns the key (command name) of this command
     *
     * @return
     *      The command's key
     */
    String getKey();

    /**
     * Returns whether this command should be available globally (all guilds) or has to be enabled manually
     *
     * @return
     *      true if this command is global
     */
    boolean isGlobal();

    /**
     * Returns whether this command should accept PMs or only messages sent in guilds
     *
     * @return
     *      true if this command accepts PMs
     */
    boolean acceptsPM();

    /**
     * Gives the requirement that has to be met by the user invoking the command.<br>
     * This can be a collection of {@link com.kantenkugel.kanzebot.api.group.Group Groups} (AND/OR)
     * or a collection of {@link net.dv8tion.jda.Permission Permissions} (AND/OR)
     * or <b>null</b>.<br><br>
     *
     * <b>Note:</b> this requirement is not checked on commands issued via PM.
     *
     * @return
     *      The Requirement (group/permission) that the user invoking the command has to meet or null if there is no requirement
     */
    Requirement getRequirement();

    /**
     * Returns the command-usage (parameters, preferably with short parameter description)<br>
     * This gets printed if either a custom ArgParser fails parsing
     * or when the help of this method is displayed (either via help command
     * or the corresponding handleX method of the Command returns <i>false</i>)
     *
     * @return
     *      The usage-string
     */
    String getUsage();

    /**
     * Returns a short help-string that gets displayed in the command-list or when calling the help-command on this command
     *
     * @return
     *      A short one-line help describing this commands function
     */
    String getHelp();

    /**
     * This method is called when a command got called via guild-chat and all prerequisites are met.
     *
     * @param channel
     *      The TextChannel the command was invoked in
     * @param author
     *      The user invoking the command
     * @param fullMessage
     *      The full message-object (for advanced stuff)
     * @param args
     *      The full argument string (command name already stripped)
     * @param customArgs
     *      If no {@link ArgParser} was set, this is always <i>null</i>.
     *      Otherwise this is a Object-array that got produced by the custom {@link ArgParser}
     *
     * @return
     *      false if the help+usage should be displayed, otherwise true
     */
    boolean handleGuild(TextChannel channel, User author, Message fullMessage, String args, Object[] customArgs);

    /**
     * This method is called when a command got called via private-chat and all prerequisites are met.
     *
     * @param channel
     *      The PrivateChannel the command was invoked in
     * @param author
     *      The user invoking the command
     * @param fullMessage
     *      The full message-object (for advanced stuff)
     * @param args
     *      The full argument string (command name already stripped)
     * @param customArgs
     *      If no {@link ArgParser} was set, this is always <i>null</i>.
     *      Otherwise this is a Object-array that got produced by the custom {@link ArgParser}
     *
     * @return
     *      false if the help+usage should be displayed, otherwise true
     */
    boolean handlePrivate(PrivateChannel channel, User author, Message fullMessage, String args, Object[] customArgs);

    /**
     * Returns the custom {@link ArgParser} if one should be used. If no custom argument-parser is needed, this returns <i>null</i>
     *
     * @return
     *      The custom ArgParser or <i>null</i> if no argument-parser is needed
     */
    ArgParser getCustomParser();
}
