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

//package com.android.datetimepicker.time;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import org.isoron.uhabits.R;
import org.isoron.uhabits.activities.common.dialogs.ColorPickerDialog;
import org.isoron.uhabits.activities.common.dialogs.ColorPickerDialogFactory;
import org.isoron.uhabits.models.Habit;
import org.isoron.uhabits.preferences.Preferences;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.*;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.*;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.datetimepicker.HapticFeedbackController;
import com.android.datetimepicker.Utils;
import com.android.datetimepicker.time.RadialPickerLayout.OnValueSelectedListener;

import butterknife.OnClick;

/**
 * Dialog to set a time.
 */
public class TagDialog extends AppCompatDialogFragment implements OnValueSelectedListener{
    private static final String TAG = "TagDialog";
    private TagDB tagDataBase;
    private FragmentActivity fragmentActivity;

    private ArrayList<String> tagNames = new ArrayList<String>();
    private ArrayAdapter<String> tagNamesAdapter;
    private Spinner tagSpinner;

    Habit modifiedHabit;
    ColorPickerDialogFactory colorPickerDialogFactory;
    Preferences prefs;
    BaseDialogHelper helper;
    int tagColor = 5;

    @Override
    public void onValueSelected(int pickerIndex, int newValue, boolean autoAdvance) {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tag_popup, null);

        EditText tagMsg = (EditText) view.findViewById(R.id.tagName);
        Button saveBtn = (Button) view.findViewById(R.id.saveBtn);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.tagButtonPickColor);

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int color = modifiedHabit.getColor();
                ColorPickerDialog picker = colorPickerDialogFactory.create(color);

                picker.setListener(c -> {
                    prefs.setDefaultHabitColor(c);
                    modifiedHabit.setColor(c);
                    tagColor = c;
//                    modifiedHabit.getTag().setColor(c);
                    helper.populateColor(c);
                    System.out.println("Color chosen: " + modifiedHabit.getTag().getColor());
                });

                picker.show(getFragmentManager(), "picker");
            }
        });







        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Tag newTag = new Tag(tagDataBase.getTagCount() + 1, tagMsg.getEditableText().toString(), 5);
                if(tagColor != 5){
                    newTag.setColor(tagColor);
                }
                tagDataBase.addTag(newTag); // 5 is the default color chosen in Habits class

                addTagToList(tagDataBase, tagNames, tagNamesAdapter, tagSpinner);
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


    public void setTagDB(TagDB newTagDB){
        this.tagDataBase = newTagDB;
    }
    public void setTagNames(ArrayList<String> newTagList){
        this.tagNames = newTagList;
    }
    public void setTagNamesAdapter(ArrayAdapter<String> newTagAdapter){
        this.tagNamesAdapter = newTagAdapter;
    }
    public void setTagSpinner(Spinner newSpinner){
        this.tagSpinner = newSpinner;
    }

    public void setFragmentActivity(FragmentActivity newActivity){
        this.fragmentActivity = newActivity;

        if(this.fragmentActivity != null){
//            EditText tagMsg = (EditText) fragmentActivity.findViewById(R.id.tagName);
//
//            Button saveBtn = (Button) fragmentActivity.findViewById(R.id.saveBtn);

        }
    }

    public void addTagToList(TagDB tagDB, ArrayList<String> tagList, ArrayAdapter<String> tagNamesAdapter, Spinner tagSpinner){
        tagList.add(tagDB.getTag(tagDB.getTagCount()).getName());

        tagNamesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,tagNames);

        tagSpinner.setAdapter(tagNamesAdapter);
    }
//    @OnClick(R.id.buttonPickColor)
//    void showTagColorPicker(Habit modifiedHabit, ColorPickerDialogFactory colorPickerDialogFactory, Preferences prefs, BaseDialogHelper helper)
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

    public void setColorPickerParams(Habit modifiedHabit, ColorPickerDialogFactory colorPickerDialogFactory, Preferences prefs, BaseDialogHelper helper){
        this.modifiedHabit = modifiedHabit;
        this.colorPickerDialogFactory = colorPickerDialogFactory;
        this.prefs = prefs;
        this.helper = helper;
    }

}

