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

    @Before
    @Override
    public void setUp()
    {
        super.setUp();

        tagDB = new TagDB(InstrumentationRegistry.getTargetContext(), "tag database test", null, 1);

        testTag1 = new Tag(12345, "This is the first tag", 1);
        Tag testTag2 = new Tag(67890, "This is the second tag", 2);
        Tag testTag3 = new Tag(369, "This is the third tag", 3);

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
    public void testGetTag()
    {
        Tag returnedTag = tagDB.getTag(67890);

        assertEquals(67890, returnedTag.getId());
        assertEquals(2, returnedTag.getColor());
        assertEquals("This is the second tag", returnedTag.getName());
    }

    @Test
    public void testGetTagThatDoesNotExist()
    {
        try {
            Tag returnedTag = tagDB.getTag(5);
            fail("Exception was not thrown");
        } catch(Exception e) {

        }
    }

    @Test
    public void testGetTagByName()
    {
        Tag returnedTag = tagDB.getTagByName("This is the third tag");

        assertEquals(369, returnedTag.getId());
        assertEquals(3, returnedTag.getColor());
        assertEquals("This is the third tag", returnedTag.getName());
    }

    @Test
    public void testGetTagByNameThatDoesNotExist()
    {
        try {
            Tag returnedTag = tagDB.getTagByName("This name does not exist");
            fail("Exception was not thrown");
        } catch(Exception e) {

        }
    }

    @Test
    public void testGetAllTags()
    {
        List<Tag> returnedTags = tagDB.getAllTags();

        assertEquals(3, returnedTags.size());

        Tag firstTag = returnedTags.get(0);
        Tag lastTag = returnedTags.get(2);

        assertEquals(369, firstTag.getId());
        assertEquals(3, firstTag.getColor());
        assertEquals("This is the third tag", firstTag.getName());

        assertEquals(67890, lastTag.getId());
        assertEquals(2, lastTag.getColor());
        assertEquals("This is the second tag", lastTag.getName());
    }

    @Test
    public void testGetAllTagsWhenDatabaseEmpty()
    {
        tagDB.deleteTag("This is the first tag");
        tagDB.deleteTag("This is the second tag");
        tagDB.deleteTag("This is the third tag");

        List<Tag> returnedTags = tagDB.getAllTags();

        assertEquals(0, returnedTags.size());
    }

    @Test
    public void testGetAllTagsWhenDatabaseHasOneTag()
    {
        tagDB.deleteTag("This is the first tag");
        tagDB.deleteTag("This is the second tag");

        List<Tag> returnedTags = tagDB.getAllTags();

        assertEquals(1, returnedTags.size());

        Tag firstTag = returnedTags.get(0);

        assertEquals(369, firstTag.getId());
        assertEquals(3, firstTag.getColor());
        assertEquals("This is the third tag", firstTag.getName());
    }

    @Test
    public void testGetTagCount()
    {
        int count = tagDB.getTagCount();
        assertEquals(3, count);
    }

    @Test
    public void testGetTagCountWhenDatabaseEmpty()
    {
        int count = tagDB.getTagCount();
        assertEquals(3, count);

        tagDB.deleteTag("This is the first tag");
        tagDB.deleteTag("This is the second tag");
        tagDB.deleteTag("This is the third tag");

        count = tagDB.getTagCount();
        assertEquals(0, count);
    }

    @Test
    public void testDeleteTag()
    {
        assertEquals(3, tagDB.getTagCount());
        tagDB.deleteTag("This is the first tag");
        assertEquals(2, tagDB.getTagCount());
    }

    @Test
    public void testDeleteTagThatDoesNotExist()
    {
        assertEquals(3, tagDB.getTagCount());
        tagDB.deleteTag("This tag does not exist in the database");
        assertEquals(3, tagDB.getTagCount());
    }

    @Test
    public void testRefreshIndex()
    {
        tagDB.refreshIndex();

        Tag returnedTag = tagDB.getTagByName("This is the first tag");

        assertEquals(2, returnedTag.getId());
        assertEquals(1, returnedTag.getColor());
        assertEquals("This is the first tag", returnedTag.getName());

        returnedTag = tagDB.getTagByName("This is the second tag");

        assertEquals(3, returnedTag.getId());
        assertEquals(2, returnedTag.getColor());
        assertEquals("This is the second tag", returnedTag.getName());

        returnedTag = tagDB.getTagByName("This is the third tag");

        assertEquals(1, returnedTag.getId());
        assertEquals(3, returnedTag.getColor());
        assertEquals("This is the third tag", returnedTag.getName());
    }

    @Test
    public void testRefreshIndexWhenDatabaseEmpty()
    {
        tagDB.deleteTag("This is the first tag");
        tagDB.deleteTag("This is the second tag");
        tagDB.deleteTag("This is the third tag");

        tagDB.refreshIndex();

        assertEquals(0, tagDB.getTagCount());
    }

    @Test
    public void testRefreshIndexWhenDatabaseHasOneTag()
    {
        tagDB.deleteTag("This is the first tag");
        tagDB.deleteTag("This is the second tag");

        tagDB.refreshIndex();

        Tag returnedTag = tagDB.getTagByName("This is the third tag");

        assertEquals(1, returnedTag.getId());
        assertEquals(3, returnedTag.getColor());
        assertEquals("This is the third tag", returnedTag.getName());
    }

    @Test
    public void testDeleteTagAndRefresh()
    {
        assertEquals(3, tagDB.getTagCount());
        tagDB.deleteTagAndRefresh("This is the first tag");
        assertEquals(2, tagDB.getTagCount());

        Tag returnedTag = tagDB.getTagByName("This is the second tag");

        assertEquals(2, returnedTag.getId());
        assertEquals(2, returnedTag.getColor());
        assertEquals("This is the second tag", returnedTag.getName());

        returnedTag = tagDB.getTagByName("This is the third tag");

        assertEquals(1, returnedTag.getId());
        assertEquals(3, returnedTag.getColor());
        assertEquals("This is the third tag", returnedTag.getName());
    }

    @Test
    public void testChangeIDTag()
    {
        tagDB.changeIDTag(testTag1, 2468 );

        Tag returnedTag = tagDB.getTagByName("This is the first tag");

        assertEquals(2468, returnedTag.getId());
        assertEquals(1, returnedTag.getColor());
        assertEquals("This is the first tag", returnedTag.getName());
    }

    @Test
    public void testChangeIDTagOfTagThatDoesNotExist()
    {
        Tag newTag = new Tag(444, "Testing 1,2,3", 10);

        tagDB.changeIDTag(newTag, 2468);

        assertEquals(3, tagDB.getTagCount());
    }
}
