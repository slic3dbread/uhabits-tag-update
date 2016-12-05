
package org.isoron.uhabits.activities.habits.edit;

import android.support.test.runner.*;
import android.test.suitebuilder.annotation.*;

import org.isoron.uhabits.*;
import org.junit.*;
import org.junit.runner.*;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
@MediumTest
public class EditHabitDialogTest extends BaseAndroidTest
{
    private EditHabitDialog editDialog;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        editDialog = new EditHabitDialog();

    }

    @Test
    public void testShowTagEdit() {


    }
}