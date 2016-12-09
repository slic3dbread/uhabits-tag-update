package org.isoron.uhabits.activities.habits.edit;/*
 * Copyright (C) 2013 The Android Open Source Project
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
 * limitations under the License
 */


import org.isoron.uhabits.AppComponent;
import org.isoron.uhabits.HabitsApplication;
import org.isoron.uhabits.R;
import org.isoron.uhabits.activities.BaseActivity;
import org.isoron.uhabits.activities.common.dialogs.ColorPickerDialog;
import org.isoron.uhabits.activities.common.dialogs.ColorPickerDialogFactory;

import org.isoron.uhabits.preferences.Preferences;

import android.os.Bundle;
import android.support.v7.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.datetimepicker.time.RadialPickerLayout.OnValueSelectedListener;

//import butterknife.OnClick;

/**
 * Dialog to set a time.
 */
public class AddTagDialog extends AppCompatDialogFragment implements OnValueSelectedListener {
    private TagDB tagDataBase;

    private ColorPickerDialogFactory colorPickerDialogFactory;
    private Preferences prefs;
    private int tagColor = 5;



    @Override
    public void onValueSelected(int pickerIndex, int newValue, boolean autoAdvance) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tag_popup, null);

        BaseActivity activity = (BaseActivity) getActivity();
        colorPickerDialogFactory =
                activity.getComponent().getColorPickerDialogFactory();

        HabitsApplication app =
                (HabitsApplication) getContext().getApplicationContext();
        AppComponent appComponent = app.getComponent();
        prefs = appComponent.getPreferences();

        EditText tagMsg = (EditText) view.findViewById(R.id.tagName);
        Button saveBtn = (Button) view.findViewById(R.id.saveBtn);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.tagButtonPickColor);

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int color = 0;
                ColorPickerDialog picker = colorPickerDialogFactory.create(color);

                picker.setListener(c -> {
                    prefs.setDefaultHabitColor(c);
                    tagColor = c;

                });

                picker.show(getFragmentManager(), "picker");
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tagDataBase = new TagDB(getContext(), "tag database", null, 1);

                if (tagDataBase.getTagCount() == 0) {
                    tagDataBase.addTag(new Tag(1, "No Tag", 1));
                }

                Tag newTag = new Tag(tagDataBase.getTagCount() + 1, tagMsg.getEditableText().toString(), 5);
                if (tagColor != 5) {
                    newTag.setColor(tagColor);
                }
                tagDataBase.addTag(newTag); // 5 is the default color chosen in Habits class

                dismiss();

            }
        });

        Button cancelBtn = (Button) view.findViewById(R.id.discardBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dismiss();

            }
        });

        return view;


    }


}

