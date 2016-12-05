package org.isoron.uhabits.activities.habits.edit;

import android.support.test.runner.*;
import android.test.suitebuilder.annotation.*;

import org.isoron.uhabits.*;
import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;
import android.support.test.InstrumentationRegistry;
import java.util.List;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TagDBTest extends BaseAndroidTest
{

    private TagDB tagDB;

    private Tag testTag1;
    private Tag testTag2;
    private Tag testTag3;

    @Before
    @Override
    public void setUp()
    {
        super.setUp();

        tagDB = new TagDB(InstrumentationRegistry.getTargetContext(), "tag database", null, 1);

        //tagDB.clearAllTags();

        testTag1 = new Tag(12345, "This is the first tag", 1);
        testTag2 = new Tag(67890, "This is the second tag", 2);
        testTag3 = new Tag(369, "This is the third tag", 3);
    }

    @Test
    public void testExists()
    {
        assertNotNull(tagDB);
    }

    @Test
    public void testAddTag()
    {
        tagDB.addTag(testTag1);
        tagDB.addTag(testTag2);
        tagDB.addTag(testTag3);

        assertEquals(5, tagDB.getTagCount());

        Tag returnedTag = tagDB.getTag(12345);

        assertEquals(12345, returnedTag.getId());
        assertEquals(1, returnedTag.getColor());
        assertEquals("This is the first tag", returnedTag.getName());


        returnedTag = tagDB.getTag(67890);

        assertEquals(67890, returnedTag.getId());
        assertEquals(2, returnedTag.getColor());
        assertEquals("This is the second tag", returnedTag.getName());


        returnedTag = tagDB.getTag(369);

        assertEquals(369, returnedTag.getId());
        assertEquals(3, returnedTag.getColor());
        assertEquals("This is the second tag", returnedTag.getName());
    }


    @Test
    public void testGetAllTag()
    {
        List<Tag> returnedTags = tagDB.getAllTags();

        Tag firstTag = returnedTags.get(0);
        Tag lastTag = returnedTags.get(2);

        assertEquals(12345, firstTag.getId());
        assertEquals(1, firstTag.getColor());
        assertEquals("This is the first tag", firstTag.getName());

        assertEquals(369, lastTag.getId());
        assertEquals(3, lastTag.getColor());
        assertEquals("This is the third tag", lastTag.getName());
    }


    @Test
    public void testDeleteTag() {

        assertEquals(3, tagDB.getTagCount());

        //tagDB.deleteTag("This is the first tag");

        assertEquals(2, tagDB.getTagCount());
    }

    @Test
    public void testClearAllTags() {

        assertEquals(3, tagDB.getTagCount());

        //tagDB.deleteTag("This is the first tag");

        assertEquals(2, tagDB.getTagCount());
    }

    @After
    public void tearDown()
    {
        //super.tearDown();

        tagDB.close();
    }
}
