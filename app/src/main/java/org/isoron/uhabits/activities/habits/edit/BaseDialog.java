/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.activities.habits.edit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.*;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.text.format.*;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.activeandroid.DatabaseHelper;
import com.activeandroid.util.Log;
import com.android.datetimepicker.time.*;

import org.isoron.uhabits.*;
import org.isoron.uhabits.activities.*;
import org.isoron.uhabits.activities.common.dialogs.*;
import org.isoron.uhabits.activities.habits.list.ListHabitsScreen;
import org.isoron.uhabits.commands.*;
import org.isoron.uhabits.models.*;
import org.isoron.uhabits.preferences.*;

import android.support.annotation.*;
import android.view.*;

import org.isoron.uhabits.*;
import org.isoron.uhabits.models.*;
import org.isoron.uhabits.activities.*;
import org.isoron.uhabits.activities.habits.list.model.*;
import org.isoron.uhabits.preferences.*;

import javax.inject.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import butterknife.*;

public abstract class BaseDialog extends AppCompatDialogFragment {
    @Nullable
    protected Habit originalHabit;

    @Nullable
    protected Habit modifiedHabit;

    @Nullable
    protected BaseDialogHelper helper;

    protected Preferences prefs;

    protected CommandRunner commandRunner;

    protected HabitList habitList;

    protected AppComponent appComponent;

    protected ModelFactory modelFactory;

    private ColorPickerDialogFactory colorPickerDialogFactory;

    private ArrayList<String> tagNames = new ArrayList<String>();

    private ArrayAdapter<String> tagNamesAdapter;

    private Spinner tagSpinner;

    private Integer tagSelection;

    private TagDB tagDB;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Relating to colour changes here
        BaseActivity activity = (BaseActivity) getActivity();
        colorPickerDialogFactory =
                activity.getComponent().getColorPickerDialogFactory();

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_habit, container, false);

        HabitsApplication app =
                (HabitsApplication) getContext().getApplicationContext();

        appComponent = app.getComponent();
        prefs = appComponent.getPreferences();
        habitList = appComponent.getHabitList();
        commandRunner = appComponent.getCommandRunner();
        modelFactory = appComponent.getModelFactory();

        ButterKnife.bind(this, view);

        helper = new BaseDialogHelper(this, view);
        getDialog().setTitle(getTitle());
        initializeHabits();
        restoreSavedInstance(savedInstanceState);
        helper.populateForm(modifiedHabit);

        tagDB = new TagDB(this.getContext(), "tag database", null, 1);

        if (tagDB.getTagCount() == 0) {
            tagDB.addTag(new Tag(1, "No Tag", 1));
        }

        for (int i = 0; i < tagDB.getTagCount(); i++) {
            tagNames.add(i, tagDB.getTag(i + 1).getName());
        }

        tagSpinner = (Spinner) view.findViewById(R.id.tagList);

        tagNamesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tagNames);

        tagSpinner.setAdapter(tagNamesAdapter);

        tagSpinner.setVisibility(View.VISIBLE);

        if (modifiedHabit.getTag() != null) {
            tagSpinner.setSelection(modifiedHabit.getTag().getId() - 1);
        }

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // save
                modifiedHabit.setTag(tagDB.getTag(position + 1));
                tagSelection = tagDB.getTag(position + 1).getId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Should do nothing
            }
        });

        return view;
    }

    public void showTagEdit(TagDB tagdb, FragmentActivity activity, Habit modifiedHabit, ColorPickerDialogFactory colorPickerDialogFactory, Preferences prefs, BaseDialogHelper helper) {
        TagDialog tagDialog = new TagDialog();
        tagDialog.setTagDB(tagdb);

//        tagDialog.setTagSpinner(tagSpinner);
//        tagDialog.setTagNamesAdapter(tagNamesAdapter);
        tagDialog.setFragmentActivity(activity);
        tagDialog.setColorPickerParams(modifiedHabit, colorPickerDialogFactory, prefs, helper);


        tagDialog.show(getFragmentManager(), "tagEdit");
    }

//public void showTagEdit(TagDB tagdb, FragmentActivity activity, ArrayList<String> tagList, ArrayAdapter<String> tagNamesAdapter, Spinner tagSpinner, Habit modifiedHabit, ColorPickerDialogFactory colorPickerDialogFactory, Preferences prefs, BaseDialogHelper helper) {
//        TagDialog tagDialog = new TagDialog();
//        tagDialog.setTagDB(tagdb);
//        tagDialog.setTagNames(tagList);
//        tagDialog.setTagSpinner(tagSpinner);
//        tagDialog.setTagNamesAdapter(tagNamesAdapter);
//        tagDialog.setFragmentActivity(activity);
//
//        tagDialog.setColorPickerParams(modifiedHabit, colorPickerDialogFactory, prefs, helper);
//        tagDialog.show(getFragmentManager(), "tagEdit");
//    }


    public void pipeColorPicker(Habit modifiedHabit, ColorPickerDialogFactory colorPickerDialogFactory, Preferences prefs, BaseDialogHelper helper) {

    }

    @OnItemSelected(R.id.sFrequency)
    public void onFrequencySelected(int position) {
        if (position < 0 || position > 4) throw new IllegalArgumentException();
        int freqNums[] = {1, 1, 2, 5, 3};
        int freqDens[] = {1, 7, 7, 7, 7};
        modifiedHabit.setFrequency(
                new Frequency(freqNums[position], freqDens[position]));
        helper.populateFrequencyFields(modifiedHabit);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("color", modifiedHabit.getColor());
        if (modifiedHabit.hasReminder()) {
            Reminder reminder = modifiedHabit.getReminder();
            outState.putInt("reminderMin", reminder.getMinute());
            outState.putInt("reminderHour", reminder.getHour());
            outState.putInt("reminderDays", reminder.getDays().toInteger());
        }
    }

    protected abstract int getTitle();

    protected abstract void initializeHabits();

    protected void restoreSavedInstance(@Nullable Bundle bundle) {
        if (bundle == null) return;
        // Colour related code
        modifiedHabit.setColor(
                bundle.getInt("color", modifiedHabit.getColor()));

        modifiedHabit.setReminder(null);

        int hour = (bundle.getInt("reminderHour", -1));
        int minute = (bundle.getInt("reminderMin", -1));
        int days = (bundle.getInt("reminderDays", -1));

        if (hour >= 0 && minute >= 0) {
            Reminder reminder =
                    new Reminder(hour, minute, new WeekdayList(days));
            modifiedHabit.setReminder(reminder);
        }
    }

    protected abstract void saveHabit();


    @OnClick(R.id.buttonDiscard)
    void onButtonDiscardClick() {
        dismiss();
    }

    @OnClick(R.id.tvReminderTime)
    @SuppressWarnings("ConstantConditions")
    void onDateSpinnerClick() {
        int defaultHour = 8;
        int defaultMin = 0;

        if (modifiedHabit.hasReminder()) {
            Reminder reminder = modifiedHabit.getReminder();
            defaultHour = reminder.getHour();
            defaultMin = reminder.getMinute();
        }

        showTimePicker(defaultHour, defaultMin);
    }

    @OnClick(R.id.buttonSave)
    void onSaveButtonClick() {
        helper.parseFormIntoHabit(modifiedHabit);
        modifiedHabit.setTag(tagDB.getTag(tagSelection));
        if (!helper.validate(modifiedHabit)) return;
        saveHabit();
        dismiss();
    }

    @OnClick(R.id.tvReminderDays)
    @SuppressWarnings("ConstantConditions")
    void onWeekdayClick() {
        if (!modifiedHabit.hasReminder()) return;
        Reminder reminder = modifiedHabit.getReminder();

        WeekdayPickerDialog dialog = new WeekdayPickerDialog();
        dialog.setListener(new OnWeekdaysPickedListener());
        dialog.setSelectedDays(reminder.getDays().toArray());
        dialog.show(getFragmentManager(), "weekdayPicker");
    }

    // Actual colour picker code
//    @OnClick(R.id.buttonPickColor)
//    void showColorPicker()
//    {
//        int color = modifiedHabit.getColor();
//        ColorPickerDialog picker = colorPickerDialogFactory.create(color);
//
//        picker.setListener(c -> {
//            prefs.setDefaultHabitColor(c);
//            modifiedHabit.setColor(c);
//            helper.populateColor(c);
//        });
//
//        picker.show(getFragmentManager(), "picker");
//    }


    private void showTimePicker(int defaultHour, int defaultMin) {
        boolean is24HourMode = DateFormat.is24HourFormat(getContext());
        TimePickerDialog timePicker =
                TimePickerDialog.newInstance(new OnTimeSetListener(), defaultHour,
                        defaultMin, is24HourMode);
        timePicker.show(getFragmentManager(), "timePicker");
    }

    private class OnTimeSetListener
            implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeCleared(RadialPickerLayout view) {
            modifiedHabit.clearReminder();
            helper.populateReminderFields(modifiedHabit);
        }

        @Override
        public void onTimeSet(RadialPickerLayout view, int hour, int minute) {
            Reminder reminder =
                    new Reminder(hour, minute, WeekdayList.EVERY_DAY);
            modifiedHabit.setReminder(reminder);
            helper.populateReminderFields(modifiedHabit);
        }
    }

    private class OnWeekdaysPickedListener
            implements WeekdayPickerDialog.OnWeekdaysPickedListener {
        @Override
        public void onWeekdaysPicked(boolean[] selectedDays) {
            if (isSelectionEmpty(selectedDays)) Arrays.fill(selectedDays, true);

            Reminder oldReminder = modifiedHabit.getReminder();
            modifiedHabit.setReminder(
                    new Reminder(oldReminder.getHour(), oldReminder.getMinute(),
                            new WeekdayList(selectedDays)));
            helper.populateReminderFields(modifiedHabit);
        }

        private boolean isSelectionEmpty(boolean[] selectedDays) {
            for (boolean d : selectedDays) if (d) return false;
            return true;
        }
    }
}
