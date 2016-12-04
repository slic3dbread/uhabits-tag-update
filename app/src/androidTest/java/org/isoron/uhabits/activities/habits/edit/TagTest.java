package org.isoron.uhabits.activities.habits.edit;

import android.support.test.runner.*;
import android.test.suitebuilder.annotation.*;

import org.isoron.uhabits.*;
import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TagTest extends BaseAndroidTest
{
    private Tag tag;

    @Before
    @Override
    public void setUp()
    {
        super.setUp();

        tag = new Tag();

        System.out.println("Testing Tag Class");
    }

    @Test
    public void testSetId()
    {
        tag.setId(1234);
        int id = tag.getId();

        assertEquals(1234, id);
    }

    @Test
    public void testSetName()
    {
        tag.setName("My name is Tag");
        String name = tag.getName();

        assertEquals("My name is Tag", name);
    }

    @Test
    public void testSetColor()
    {
        tag.setColor(1);
        int colour = tag.getColor();

        assertEquals(1, colour);
    }
}
