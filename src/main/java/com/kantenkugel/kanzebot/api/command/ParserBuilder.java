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

public class ParserBuilder {
    private StringBuilder builder = new StringBuilder();
    private List<ParserToken> types = new LinkedList<>();

    private int optionalCount = 0;

    public ParserBuilder() {
    }

    public ParserBuilder addLiteral(String literal) {
        builder.append("\\s+").append(literal);
        return this;
    }

    public ParserBuilder addString() {
        builder.append("(?:\\s+(.*?))?");
        types.add(ParserToken.STRING);
        return this;
    }

    public ParserBuilder addSplitString() {
        builder.append("(?:\\s+(.*?))?");
        types.add(ParserToken.VAR_STRING);
        return this;
    }

    public ParserBuilder addInteger() {
        builder.append("(?:\\s+([+-]?\\d+))?");
        types.add(ParserToken.INTEGER);
        return this;
    }

    public ParserBuilder addFloat() {
        builder.append("(?:\\s+([+-]?\\d*\\.?\\d*))?");
        types.add(ParserToken.FLOAT);
        return this;
    }

    public ParserBuilder addUser() {
        builder.append("(?:\\s+(?:<@!?(\\d+)>|(.{3,32})))?");
        types.add(ParserToken.USER);
        return this;
    }

    public ParserBuilder addChannel() {
        builder.append("(?:\\s+(?:<#(\\d+)>|(.{3,32})))?");
        types.add(ParserToken.CHANNEL);
        return this;
    }

    public ParserBuilder startOptionalBlock() {
        builder.append("(?:");
        types.add(ParserToken.OPTIONAL_START);
        optionalCount++;
        return this;
    }

    public ParserBuilder endOptionalBlock() {
        if(optionalCount == 0) {
            throw new UnsupportedOperationException("Can't close a optional-block that was never opened!");
        }
        builder.append(")?");
        types.add(ParserToken.OPTIONAL_END);
        optionalCount--;
        return this;
    }

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
                return new ParserResult(null, "Incorrect usage");
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