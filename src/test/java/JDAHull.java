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

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.entities.impl.JDAImpl;

public class JDAHull extends JDAImpl {

    public JDAHull() {
        super(false, false, false);
    }

    public JDAHull addUser(User user) {
        userMap.put(user.getId(), user);
        return this;
    }

    public JDAHull addTextChannel(TextChannel chan) {
        textChannelMap.put(chan.getId(), chan);
        return this;
    }

    public JDAHull addVoiceChannel(VoiceChannel channel) {
        voiceChannelMap.put(channel.getId(), channel);
        return this;
    }

    public void clear() {
        userMap.clear();
        textChannelMap.clear();
        voiceChannelMap.clear();
    }
}
