package org.isoron.uhabits.activities.habits.edit;

import android.support.test.runner.*;
import android.test.suitebuilder.annotation.*;

import org.isoron.uhabits.*;
import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
@MediumTest
public class TagDialogTest extends BaseAndroidTest
{
    private TagDialog tagDialog;


    @Before
    @Override
    public void setUp()
    {
        super.setUp();

        tagDialog = new TagDialog();
    }

    @Test
    public void testShowTagEdit()
    {


    }
}
