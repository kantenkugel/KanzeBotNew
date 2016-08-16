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

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class is used to create a custom {@link ArgParser} to be used in {@link Command Commands}
 */
public class ParserBuilder {
    private StringBuilder builder = new StringBuilder();
    private List<ParserToken> types = new LinkedList<>();

    private int optionalCount = 0;

    public ParserBuilder() {
    }

    /**
     * Adds a literal (string) to the parsed sequence.<br>
     * This literal will not be returned as argument but is strongly recommended
     * after any input with possible spaces like User, String
     *
     * @param literal
     *      The literal to add
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder addLiteral(String literal) {
        builder.append("\\s+").append(literal);
        return this;
    }

    /**
     * Adds a String to be parsed from the input.
     * This will either add a object of type String or <i>null</i> (in case of optional) to the arguments
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder addString() {
        builder.append("(?:\\s+(.*?))?");
        types.add(ParserToken.STRING);
        return this;
    }

    /**
     * Adds a String to be parsed from the input and then split on whitespaces.
     * This will either add a String array or String[0] (in case of optional) to the arguments
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder addSplitString() {
        builder.append("(?:\\s+(.*?))?");
        types.add(ParserToken.VAR_STRING);
        return this;
    }

    /**
     * Adds a Integer to be parsed from the input.
     * This will either add a <i>int</i> value or <i>null</i> (in case of optional) to the arguments
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder addInteger() {
        builder.append("(?:\\s+([+-]?\\d+))?");
        types.add(ParserToken.INTEGER);
        return this;
    }

    /**
     * Adds a Float to be parsed from the input.
     * This will either add a <i>float</i> value or <i>null</i> (in case of optional) to the arguments
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder addFloat() {
        builder.append("(?:\\s+([+-]?\\d*\\.?\\d*))?");
        types.add(ParserToken.FLOAT);
        return this;
    }

    /**
     * Adds a {@link User} to be parsed from the input.
     * This will either add a {@link User} object or <i>null</i> (in case of optional) to the arguments.<br><br>
     *
     * <b>Note:</b> this tries to first match a mention and if no mention is present a username.
     * It is strongly recommended to use {@link #addLiteral(String)} if other arguments will be added to prevent some wrong matching due to spaces in usernames
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder addUser() {
        builder.append("(?:\\s+(?:<@!?(\\d+)>|(.{3,32})))?");
        types.add(ParserToken.USER);
        return this;
    }

    /**
     * Adds a {@link Channel} to be parsed from the input.
     * This will either add a {@link Channel} ({@link TextChannel} or {@link VoiceChannel}) object or <i>null</i> (in case of optional) to the arguments.<br><br>
     *
     * <b>Note:</b> this tries to first match a mention and if no mention is present a username.
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder addChannel() {
        builder.append("(?:\\s+(?:<#(\\d+)>|(\\S{3,32})))?");
        types.add(ParserToken.CHANNEL);
        return this;
    }

    /**
     * Signals the start of a optional block.
     * Inputs that are not optional will automatically give the user a error if not specified.
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder startOptionalBlock() {
        builder.append("(?:");
        types.add(ParserToken.OPTIONAL_START);
        optionalCount++;
        return this;
    }

    /**
     * Signals the end of a optional block.
     * Inputs that are not optional will automatically give the user a error if not specified.
     *
     * @return
     *      The ParserBuilder instance for chaining.
     */
    public ParserBuilder endOptionalBlock() {
        if(optionalCount == 0) {
            throw new UnsupportedOperationException("Can't close a optional-block that was never opened!");
        }
        builder.append(")?");
        types.add(ParserToken.OPTIONAL_END);
        optionalCount--;
        return this;
    }

    /**
     * Closes all still open optional-blocks and builds the ArgParser
     *
     * @return
     *      The built ArgParser.
     */
    public ArgParser build() {
        while(optionalCount > 0) {
            endOptionalBlock();
        }
        if(builder.length() != 0) {
            if(builder.charAt(0) == '(') {
                builder.delete(3, 6);                   // (?:\\s+ <- 3-7
            } else {
                builder.delete(0, 3);                   // \\s+
            }
        }
        return new ParserImpl(Pattern.compile(builder.toString()), types);
    }

    private enum ParserToken {
        STRING, INTEGER, FLOAT, USER, CHANNEL, OPTIONAL_START, OPTIONAL_END, VAR_STRING
    }

    private static class ParserImpl implements ArgParser {
        private final Pattern pattern;
        private final List<ParserToken> types;

        private ParserImpl(Pattern pattern, List<ParserToken> types) {
            this.pattern = pattern;
            this.types = types;
        }

        @Override
        public ParserResult parseArgs(JDA jda, TextChannel optChannel, String args) {
            Matcher matcher = pattern.matcher(args);
            if(!matcher.matches()) {
                StringBuilder reqArgs = new StringBuilder();
                for(ParserToken type : types) {
                    switch(type) {
                        case STRING:
                            reqArgs.append(" STRING");
                            break;
                        case INTEGER:
                            reqArgs.append(" INTEGER");
                            break;
                        case FLOAT:
                            reqArgs.append(" FLOAT");
                            break;
                        case USER:
                            reqArgs.append(" USER(mention or name)");
                            break;
                        case CHANNEL:
                            reqArgs.append(" CHANNEL(mention or name)");
                            break;
                        case OPTIONAL_START:
                            reqArgs.append(" [");
                            break;
                        case OPTIONAL_END:
                            reqArgs.append("]");
                            break;
                        case VAR_STRING:
                            reqArgs.append(" STRING");
                            break;
                    }
                }
                reqArgs.delete(0, 0);
                return new ParserResult(null, "Incorrect usage. Required Arguments: `" + reqArgs.toString() + "`");
            }
            int count = matcher.groupCount();
            List<Object> out = new LinkedList<>();
            int groupIndex = 0;

            Stack<Boolean> optionalStack = new Stack<>();

            String string, string2;

            for(ParserToken type : types) {
                string = matcher.group(Math.min(groupIndex + 1, count));
                switch(type) {
                    case STRING:
                        if(string == null || string.isEmpty()) {
                            if(optionalStack.isEmpty() || optionalStack.peek()) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type String is not optional");
                            } else {
                                out.add(null);
                            }
                        } else {
                            out.add(string);
                            if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                optionalStack.pop();
                                optionalStack.push(true);
                            }
                        }
                        break;
                    case INTEGER:
                        if(string == null || string.isEmpty()) {
                            if(optionalStack.isEmpty() || optionalStack.peek()) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type Integer is not optional");
                            } else {
                                out.add(null);
                            }
                        } else {
                            try {
                                out.add(Integer.parseInt(string));
                                if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                    optionalStack.pop();
                                    optionalStack.push(true);
                                }
                            } catch(NumberFormatException ex) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type Integer could not get parsed");
                            }
                        }
                        break;
                    case FLOAT:
                        if(string == null || string.isEmpty()) {
                            if(optionalStack.isEmpty() || optionalStack.peek()) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type Float is not optional");
                            } else {
                                out.add(null);
                            }
                        } else {
                            try {
                                out.add(Float.parseFloat(string));
                                if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                    optionalStack.pop();
                                    optionalStack.push(true);
                                }
                            } catch(NumberFormatException ex) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type Float could not get parsed");
                            }
                        }
                        break;
                    case USER:
                        string2 = matcher.group(Math.min((++groupIndex + 1), count));
                        if((string == null || string.isEmpty()) && (string2 == null || string2.isEmpty())) {
                            if(optionalStack.isEmpty() || optionalStack.peek()) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type User is not optional");
                            } else {
                                out.add(null);
                            }
                        } else {
                            try {
                                //searching by id first
                                //noinspection ResultOfMethodCallIgnored
                                Long.parseLong(string);
                                User u = jda.getUserById(string);
                                if(u == null) {
                                    return new ParserResult(null, "User with given id could not be found by this Bot");
                                } else {
                                    out.add(u);
                                    if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                        optionalStack.pop();
                                        optionalStack.push(true);
                                    }
                                }
                            } catch(NumberFormatException ex) {
                                //searching by name
                                List<User> usersByName = jda.getUsersByName(string2);
                                if(usersByName.size() == 0)
                                    return new ParserResult(null, "User with given name was not found! Please check capitalisation or mention instead");
                                if(usersByName.size() > 1) {
                                    if(optChannel == null)
                                        return new ParserResult(null, "Multiple users with given name found! Please consider mentioning instead!");
                                    usersByName = usersByName.stream().filter(user -> optChannel.getGuild().getUsers().contains(user)).collect(Collectors.toList());
                                    if(usersByName.size() != 1)
                                        return new ParserResult(null, "Multiple users with given name found! Please consider mentioning instead!");
                                }
                                out.add(usersByName.get(0));
                                if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                    optionalStack.pop();
                                    optionalStack.push(true);
                                }
                            }
                        }
                        break;
                    case CHANNEL:
                        string2 = matcher.group(Math.min((++groupIndex + 1), count));
                        if((string == null || string.isEmpty()) && (string2 == null || string2.isEmpty())) {
                            if(optionalStack.isEmpty() || optionalStack.peek()) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type Channel is not optional");
                            } else {
                                out.add(null);
                            }
                        } else {
                            try {
                                //searching by id first
                                //noinspection ResultOfMethodCallIgnored
                                Long.parseLong(string);
                                Channel c = jda.getTextChannelById(string);
                                if(c == null)
                                    c = jda.getVoiceChannelById(string);
                                if(c == null) {
                                    return new ParserResult(null, "Text/Voice-channel with given id could not be found by this Bot");
                                } else {
                                    out.add(c);
                                    if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                        optionalStack.pop();
                                        optionalStack.push(true);
                                    }
                                }
                            } catch(NumberFormatException ex) {
                                //searching by name
                                List<Channel> channelsByName = new LinkedList<>();
                                channelsByName.addAll(jda.getTextChannelsByName(string2));
                                channelsByName.addAll(jda.getVoiceChannelByName(string2));
                                if(channelsByName.size() == 0)
                                    return new ParserResult(null, "Text/Voice-channel with given name was not found! Please check capitalisation or mention instead");
                                if(channelsByName.size() > 1) {
                                    if(optChannel == null)
                                        return new ParserResult(null, "Multiple channels with given name found! Please consider mentioning instead!");
                                    channelsByName = channelsByName.stream()
                                            .filter(channel -> (channel instanceof TextChannel) ?
                                                    optChannel.getGuild().getTextChannels().contains((TextChannel) channel) :
                                                    optChannel.getGuild().getVoiceChannels().contains((VoiceChannel) channel))
                                            .collect(Collectors.toList());
                                    if(channelsByName.size() != 1)
                                        return new ParserResult(null, "Multiple channels with given name found! Please consider mentioning instead!");
                                }
                                out.add(channelsByName.get(0));
                                if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                    optionalStack.pop();
                                    optionalStack.push(true);
                                }
                            }
                        }
                        break;
                    case OPTIONAL_START:
                        optionalStack.push(false);
                        groupIndex--;
                        break;
                    case OPTIONAL_END:
                        optionalStack.pop();
                        groupIndex--;
                        break;
                    case VAR_STRING:
                        if(string == null || string.isEmpty()) {
                            if(optionalStack.isEmpty() || optionalStack.peek()) {
                                return new ParserResult(null, "Argument " + (out.size() + 1) + " of type String is not optional");
                            } else {
                                out.add(new String[0]);
                            }
                        } else {
                            out.add(string.split("\\s+"));
                            if(!optionalStack.isEmpty() && !optionalStack.peek()) {
                                optionalStack.pop();
                                optionalStack.push(true);
                            }
                        }
                        break;
                }
                groupIndex++;
            }
            return new ParserResult(out.toArray(), null);
        }

    }
}