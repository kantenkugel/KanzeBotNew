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

import com.kantenkugel.kanzebot.api.command.ArgParser;
import com.kantenkugel.kanzebot.api.command.ParserBuilder;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.entities.impl.TextChannelImpl;
import net.dv8tion.jda.entities.impl.UserImpl;
import net.dv8tion.jda.entities.impl.VoiceChannelImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private static ArgParser stringParser;
    private static ArgParser stringParser2;

    private static ArgParser userParser;
    private static ArgParser userParser2;

    private static ArgParser channelParser;

    private static ArgParser intParser;
    private static ArgParser floatParser;

    private static JDAHull jda;
    private static User u1, u2, u3;
    private static TextChannel t1, t2, t3;
    private static VoiceChannel v1, v2, v3;

    @BeforeClass
    public static void setupParsers() {
        stringParser = new ParserBuilder().addString().startOptionalBlock().addLiteral("for").addSplitString().build();
        stringParser2 = new ParserBuilder().addString().addLiteral("for").addSplitString().build();

        userParser = new ParserBuilder().addUser().build();
        userParser2 = new ParserBuilder().addUser().addLiteral("for").addSplitString().build();

        channelParser = new ParserBuilder().addChannel().build();

        intParser = new ParserBuilder().addInteger().build();
        floatParser = new ParserBuilder().addFloat().build();

        jda = new JDAHull();
        u1 = new UserImpl("1", jda).setUserName("User");
        u2 = new UserImpl("2", jda).setUserName("User");
        u3 = new UserImpl("3", jda).setUserName("Userino");
        t1 = new TextChannelImpl("1", null).setName("Text");
        t2 = new TextChannelImpl("2", null).setName("Text");
        t3 = new TextChannelImpl("3", null).setName("Texterino");
        v1 = new VoiceChannelImpl("4", null).setName("Voice");
        v2 = new VoiceChannelImpl("5", null).setName("Voice");
        v3 = new VoiceChannelImpl("6", null).setName("Voicerino");
        jda.addUser(u1).addUser(u2).addUser(u3);
        jda.addTextChannel(t1).addTextChannel(t2).addTextChannel(t3);
        jda.addVoiceChannel(v1).addVoiceChannel(v2).addVoiceChannel(v3);
    }

    @Test
    public void testStrings() {
        //Hello World
        ArgParser.ParserResult parserResult = stringParser.parseArgs(null, null, "Hello World");
        assertNull(parserResult.getError());
        Object[] expected = new Object[] {"Hello World", new String[0]};
        assertArrayEquals(expected, parserResult.getArgs());

        //Hello World for everyone involved
        parserResult = stringParser.parseArgs(null, null, "Hello World for everyone involved");
        assertNull(parserResult.getError());
        expected = new Object[] {"Hello World", new String[] {"everyone", "involved"}};
        assertArrayEquals(expected, parserResult.getArgs());

        //Hello World for
        //Actually matches (thinks its still optional)
//        parserResult = stringParser.parseArgs(null, null, "Hello World for");
//        assertNull(parserResult.getArgs());
//        assertNotNull(parserResult.getError());
//        assertEquals("Argument 2 of type String is not optional", parserResult.getError());
    }


    @Test
    public void testStrings2() {
        ArgParser.ParserResult parserResult = stringParser2.parseArgs(null, null, "Hello World");
        assertNull(parserResult.getArgs());
        assertNotNull(parserResult.getError());
        assertEquals("Incorrect usage", parserResult.getError());

        parserResult = stringParser2.parseArgs(null, null, "Hello World for");
        assertNull(parserResult.getArgs());
        assertNotNull(parserResult.getError());
        assertEquals("Argument 2 of type String is not optional", parserResult.getError());

        parserResult = stringParser2.parseArgs(null, null, "Hello World for everyone involved");
        assertNull(parserResult.getError());
        assertNotNull(parserResult.getArgs());
        Object[] expected = new Object[] {"Hello World", new String[] {"everyone", "involved"}};
        assertArrayEquals(expected, parserResult.getArgs());
    }

    @Test
    public void testUser() {
        ArgParser.ParserResult result = userParser.parseArgs(jda, null, "<@1>");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{u1}, result.getArgs());

        result = userParser.parseArgs(jda, null, "<@!2>");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{u2}, result.getArgs());

        result = userParser.parseArgs(jda, null, "<@5>");
        assertNull(result.getArgs());
        assertEquals("User with given id could not be found by this Bot", result.getError());

        result = userParser.parseArgs(jda, null, "Use");
        assertNull(result.getArgs());
        assertEquals("User with given name was not found! Please check capitalisation or mention instead", result.getError());

        result = userParser.parseArgs(jda, null, "User");
        assertNull(result.getArgs());
        assertEquals("Multiple users with given name found! Please consider mentioning instead!", result.getError());

        result = userParser.parseArgs(jda, null, "Userino");
        assertNull(result.getError());
        assertArrayEquals(new Object[] {u3}, result.getArgs());

        //#### NOW WITH TEXT ####

        result = userParser2.parseArgs(jda, null, "<@1> for bla");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{u1, new String[]{"bla"}}, result.getArgs());

        result = userParser2.parseArgs(jda, null, "<@!2> for");
        assertNull(result.getArgs());
        assertEquals("Argument 2 of type String is not optional", result.getError());

        result = userParser2.parseArgs(jda, null, "<@!2>");
        assertNull(result.getArgs());
        assertEquals("Incorrect usage", result.getError());

        result = userParser2.parseArgs(jda, null, "<@5> for test");
        assertNull(result.getArgs());
        assertEquals("User with given id could not be found by this Bot", result.getError());

        result = userParser2.parseArgs(jda, null, "Use for test");
        assertNull(result.getArgs());
        assertEquals("User with given name was not found! Please check capitalisation or mention instead", result.getError());

        result = userParser2.parseArgs(jda, null, "User for test");
        assertNull(result.getArgs());
        assertEquals("Multiple users with given name found! Please consider mentioning instead!", result.getError());

        result = userParser2.parseArgs(jda, null, "Userino for test");
        assertNull(result.getError());
        assertArrayEquals(new Object[] {u3, new String[] {"test"}}, result.getArgs());

        result = userParser2.parseArgs(jda, null, "Userino for test test2");
        assertNull(result.getError());
        assertArrayEquals(new Object[] {u3, new String[] {"test", "test2"}}, result.getArgs());
    }

    @Test
    public void testChannels() {
        ArgParser.ParserResult result = channelParser.parseArgs(jda, null, "<#1>");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{t1}, result.getArgs());

        result = channelParser.parseArgs(jda, null, "<#4>");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{v1}, result.getArgs());

        result = channelParser.parseArgs(jda, null, "<#7>");
        assertNull(result.getArgs());
        assertEquals("Text/Voice-channel with given id could not be found by this Bot", result.getError());

        result = channelParser.parseArgs(jda, null, "Tex");
        assertNull(result.getArgs());
        assertEquals("Text/Voice-channel with given name was not found! Please check capitalisation or mention instead", result.getError());

        result = channelParser.parseArgs(jda, null, "Text");
        assertNull(result.getArgs());
        assertEquals("Multiple channels with given name found! Please consider mentioning instead!", result.getError());

        result = channelParser.parseArgs(jda, null, "Texterino");
        assertNull(result.getError());
        assertArrayEquals(new Object[] {t3}, result.getArgs());

        result = channelParser.parseArgs(jda, null, "Voice");
        assertNull(result.getArgs());
        assertEquals("Multiple channels with given name found! Please consider mentioning instead!", result.getError());

        result = channelParser.parseArgs(jda, null, "Voicerino");
        assertNull(result.getError());
        assertArrayEquals(new Object[] {v3}, result.getArgs());
    }

    @Test
    public void testNumbers() {
        ArgParser.ParserResult result = intParser.parseArgs(null, null, "512");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{512}, result.getArgs());

        result = intParser.parseArgs(null, null, "+512");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{512}, result.getArgs());

        result = intParser.parseArgs(null, null, "-512");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{-512}, result.getArgs());

        result = intParser.parseArgs(null, null, "");
        assertNull(result.getArgs());
        assertEquals("Argument 1 of type Integer is not optional", result.getError());

        result = intParser.parseArgs(null, null, "hey");
        assertNull(result.getArgs());
        assertEquals("Incorrect usage", result.getError());


        result = floatParser.parseArgs(null, null, "512");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{512F}, result.getArgs());

        result = floatParser.parseArgs(null, null, "8.5");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{8.5F}, result.getArgs());

        result = floatParser.parseArgs(null, null, "-8.5");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{-8.5F}, result.getArgs());

        result = floatParser.parseArgs(null, null, "+.5");
        assertNull(result.getError());
        assertArrayEquals(new Object[]{0.5F}, result.getArgs());

        result = floatParser.parseArgs(null, null, "");
        assertNull(result.getArgs());
        assertEquals("Argument 1 of type Float is not optional", result.getError());

        result = floatParser.parseArgs(null, null, "hey");
        assertNull(result.getArgs());
        assertEquals("Incorrect usage", result.getError());
    }
}
