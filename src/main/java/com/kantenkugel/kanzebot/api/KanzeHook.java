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
import com.kantenkugel.kanzebot.api.config.ServerConfig;
import com.kantenkugel.kanzebot.api.group.Group;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.hooks.EventListener;

public interface KanzeHook {
    ServerConfig getServerConfig(Guild guild);

    AddonConfig getConfig();

    boolean registerCommand(Command cmd);

    boolean unRegisterCommand(Command cmd);

    Group getGroup(String name);

    boolean registerGroup(Group group);

    void removeGroup(Group group);

    void registerJDAListener(EventListener listener);

    void unRegisterJDAListener(EventListener listener);
}
