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

import com.kantenkugel.kanzebot.api.util.MessageUtil;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a helper-class that helps at creating commands with sub-commands
 */
public abstract class CommandGroup implements Command {
    private Map<String, Pair<Command, ArgParser>> subCommands;

    public CommandGroup(Command... subCommands) {
        this.subCommands = new HashMap<>();
        for(Command subCommand : subCommands) {
            this.subCommands.put(subCommand.getKey(), Pair.of(subCommand, subCommand.getCustomParser()));
        }
    }

    @Override
    public boolean handleGuild(TextChannel channel, User author, Message fullMessage, String args, Object[] customArgs) {
        if(args.length() == 0)
            return false;
        String[] split = args.split("\\s+", 2);
        Pair<Command, ArgParser> sub = subCommands.get(split[0]);
        if(sub != null) {
            if(sub.getValue() != null) {
                ArgParser.ParserResult parserResult = sub.getValue().parseArgs(channel.getJDA(), channel, args);
                if(parserResult.getError() != null) {
                    MessageUtil.sendMessage(channel, parserResult.getError()+"\nUsage:\n"+sub.getKey().getUsage());
                    return true;
                }
                customArgs = parserResult.getArgs();
            }
            return sub.getKey().handleGuild(channel, author, fullMessage, args, customArgs);
        } else {
            return false;
        }
    }

    @Override
    public boolean handlePrivate(PrivateChannel channel, User author, Message fullMessage, String args, Object[] customArgs) {
        if(args.length() == 0)
            return false;
        String[] split = args.split("\\s+", 2);
        Pair<Command, ArgParser> sub = subCommands.get(split[0]);
        if(sub != null) {
            if(sub.getValue() != null) {
                ArgParser.ParserResult parserResult = sub.getValue().parseArgs(channel.getJDA(), null, args);
                if(parserResult.getError() != null) {
                    MessageUtil.sendMessage(channel, parserResult.getError()+"\nUsage:\n"+sub.getKey().getUsage());
                    return true;
                }
                customArgs = parserResult.getArgs();
            }
            return sub.getKey().handlePrivate(channel, author, fullMessage, args, customArgs);
        } else {
            return false;
        }
    }

    @Override
    public ArgParser getCustomParser() {
        return null;
    }
}
