package org.isoron.uhabits.activities.habits.edit;

import android.support.test.runner.*;
import android.test.suitebuilder.annotation.*;

import org.isoron.uhabits.*;
import org.isoron.uhabits.models.Habit;
import org.isoron.uhabits.models.memory.MemoryModelFactory;
import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class HabitTest extends BaseAndroidTest
{
    private Habit habit;
    protected MemoryModelFactory modelFactory;
    private Tag testTag1;
    private Tag testTag2;

    @Before
    @Override
    public void setUp()
    {
        super.setUp();

        modelFactory = new MemoryModelFactory();
        habit = modelFactory.buildHabit();

        testTag1 = new Tag(123, "This is a tag", 7);
        testTag2 = new Tag(456, "This is another tag", 5);
    }

    @Test
    public void testSetTag()
    {
        habit.setTag(testTag1);

        Tag returnedTag = habit.getTag();

        assertEquals(123, returnedTag.getId());
        assertEquals(7, returnedTag.getColor());
        assertEquals("This is a tag", returnedTag.getName());

        habit.setTag(testTag2);

        returnedTag = habit.getTag();

        assertEquals(456, returnedTag.getId());
        assertEquals(5, returnedTag.getColor());
        assertEquals("This is another tag", returnedTag.getName());
    }
}

