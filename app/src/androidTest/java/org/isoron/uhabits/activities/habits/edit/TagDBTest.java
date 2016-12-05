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

        tagDB = new TagDB(InstrumentationRegistry.getTargetContext(), "tag database test", null, 1);

        testTag1 = new Tag(12345, "This is the first tag", 1);
        testTag2 = new Tag(67890, "This is the second tag", 2);
        testTag3 = new Tag(369, "This is the third tag", 3);

        tagDB.addTag(testTag1);
        tagDB.addTag(testTag2);
        tagDB.addTag(testTag3);
    }

    @After
    public void tearDown()
    {
        tagDB.close();
        InstrumentationRegistry.getTargetContext().deleteDatabase("tag database test");
    }

    @Test
    public void testExists()
    {
        assertNotNull(tagDB);
    }

    @Test
    public void testAddTag()
    {
        Tag newTag = new Tag(444, "Testing 1,2,3", 10);
        tagDB.addTag(newTag);

        Tag returnedTag = tagDB.getTag(444);

        assertEquals(444, returnedTag.getId());
        assertEquals(10, returnedTag.getColor());
        assertEquals("Testing 1,2,3", returnedTag.getName());
    }

    @Test
    public void testGetTagCount()
    {
        int count = tagDB.getTagCount();
        assertEquals(3, count);
    }

    @Test
    public void testGetAllTag()
    {
        List<Tag> returnedTags = tagDB.getAllTags();

        assertEquals(3, returnedTags.size());

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
        tagDB.deleteTag("This is the first tag");
        assertEquals(2, tagDB.getTagCount());
    }

}
