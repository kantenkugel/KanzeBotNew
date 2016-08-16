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
import net.dv8tion.jda.entities.TextChannel;

/**
 * This class represents a custom Argument-parser.<br>
 * It is used to provide a command with specially parsed arguments.<br>
 * If you want to easily create a custom Argument-parser, take a look at the {@link ParserBuilder}
 * @see ParserBuilder
 * @see Command#getCustomParser()
 */
@FunctionalInterface
public interface ArgParser {

    /**
     * This method is called each time a command was entered and the Command has a custom parser set
     *
     * @param jda
     *      The global JDA instance
     * @param optChannel
     *      The text-channel the command was sent in <b>or null</b> if the command was sent via PM
     * @param args
     *      The argument-string
     * @return
     *      The resulting {@link ParserResult}
     */
    ParserResult parseArgs(JDA jda, TextChannel optChannel, String args);

    /**
     * This class represents the result of a {@link ArgParser}
     */
    class ParserResult {
        private final Object[] args;
        private final String error;

        public ParserResult(Object[] args, String error) {
            this.args = args;
            this.error = error;
        }

        /**
         * Returns either the Object-array containing the custom arguments or null in case of a parser-error
         *
         * @return
         *      The custom arguments or null in case of error
         */
        public Object[] getArgs() {
            return args;
        }

        /**
         * Returns null or in case of an Error the error-string (gets displayed together with usage)
         *
         * @return
         *      The error or null
         */
        public String getError() {
            return error;
        }
    }
}
