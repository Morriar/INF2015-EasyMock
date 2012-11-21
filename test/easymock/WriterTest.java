/*
 * Copyright 2012 Alexandre Terrasa <alexandre@moz-code.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package easymock;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.easymock.EasyMock.*;

public class WriterTest {

    private FileWriter mock;
    private Writer testee;

    public WriterTest() {
    }

    @Before
    public void setUp() {
        mock = createMock(FileWriter.class);
        testee = new Writer(mock);
    }

    @Test
    public void testWriteSomething() throws Exception {
        System.out.println("writeSomething");
        mock.write("something"); // le mock s'attend à recevoir 1 appel à write avec comme argument "something"
        replay(mock); // bascule le mock en mode replay

        // a partir de là le mock attend qu'on lui envoi des messages
        testee.writeSomething("something");

        verify(mock); // on vérifie si le mock à reçu tous les messages qu'on attendait
    }

    @Test
    public void testWriteArray() throws Exception {
        System.out.println("writeArray");

        ArrayList<String> things = new ArrayList<>();
        int times = 10;
        for (int i = 0; i < times; i++) {
            things.add("something");
        }

        mock.write("something");
        expectLastCall().times(times); // on attend times appels à write

        replay(mock);
        testee.writeArray(things);
        verify(mock);
    }

    @Test
    public void testIsEncoding() {
        System.out.println("isEncoding");
        expect(mock.getEncoding()).andReturn("UTF-8"); // getEnconding retourne "UTF-8"
        expectLastCall().atLeastOnce(); // l'appel est attendu au moins une fois
        replay(mock);

        String encoding = "UTF-8";
        boolean expResult = true;
        boolean result = testee.isEncoding(encoding);
        assertEquals(expResult, result);

        encoding = "ISO-8859-1";
        expResult = false;
        result = testee.isEncoding(encoding);
        assertEquals(expResult, result);

        verify(mock);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        System.out.println("IOException");

        mock.write("test");
        expectLastCall().andThrow(new IOException());
        replay(mock);

        testee.writeSomething("test");
    }
}
