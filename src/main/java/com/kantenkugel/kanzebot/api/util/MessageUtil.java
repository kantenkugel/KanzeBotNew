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

package com.kantenkugel.kanzebot.api.util;

import net.dv8tion.jda.entities.MessageChannel;

/**
 * Contains a lot of important functions having to do with Messages
 */
public class MessageUtil {
    private MessageUtil(){}

    /**
     * Sends a message to given channel
     * following the {@link net.dv8tion.jda.entities.Guild Guild's} escapeEveryone configuration (if not PM).
     * This will send the message asynchronously and will not block until the message was sent.
     *
     * @param channel
     *      The channel to send the message to
     * @param message
     *      The message to send
     */
    public static void sendMessage(MessageChannel channel, String message) {
        //todo
    }

    //TODO: Add a lot more
}
