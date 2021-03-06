package org.isoron.uhabits.activities.habits.edit;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.isoron.uhabits.AppComponent;
import org.isoron.uhabits.HabitsApplication;
import org.isoron.uhabits.R;
import org.isoron.uhabits.models.Habit;
import org.isoron.uhabits.models.HabitList;

import java.util.ArrayList;

public class DeleteTagDialog extends AppCompatDialogFragment {


    private static final String TAG = "TagDialog";

    private ArrayList<String> tagNames = new ArrayList<String>();
    private Spinner tagSpinner;

    private TagDB tagDB;

    private HabitList habitList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.delete_tag_popup, null);

        tagSpinner = (Spinner) view.findViewById(R.id.deleteTagSpinner);

        tagDB = new TagDB(this.getContext(), "tag database", null, 1);

        HabitsApplication app =
                (HabitsApplication) getContext().getApplicationContext();

        AppComponent appComponent = app.getComponent();

        habitList = appComponent.getHabitList();


        for (int i = 0; i < tagDB.getTagCount()-1; i++) {
            tagNames.add(i, tagDB.getTag(i + 2).getName());
        }

        ArrayAdapter<String> tagNamesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tagNames);

        tagSpinner.setAdapter(tagNamesAdapter);
        tagSpinner.setVisibility(View.VISIBLE);

        Button deleteBtn = (Button) view.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
                                         public void onClick(View v) {
                                             if (tagSpinner.getCount() != 0) {
                                                 String tagName = tagSpinner.getSelectedItem().toString();

                                                 Habit tempHabit;
                                                 int tempPos = 0;
                                                 int tagTempPos = 0;

                                                 tempPos = tagDB.getTagByName(tagName).getId();
                                                 for (int i = 0; i < habitList.size(); i++) {
                                                     tempHabit = habitList.getByPosition(i);

                                                     if (tempHabit.getTag() != null) {
                                                         if (tempHabit.getTag().getName().equals(tagName)) {
                                                             tempHabit.setTag(tagDB.getTag(1));
                                                             habitList.update(tempHabit);
                                                         }
                                                     }
                                                 }
                                                     tagDB.deleteTagAndRefresh(tagName);

                                                 for (int i = 0; i < habitList.size(); i++) {
                                                     tempHabit = habitList.getByPosition(i);
                                                     if (tempHabit.getTag() != null) {
                                                         tagTempPos = tempHabit.getTag().getId();
                                                         if (tagTempPos > tempPos) {
                                                             System.out.println(tagTempPos);
                                                             System.out.println(tempPos);
                                                             tempHabit.setTag(tagDB.getTag(tagTempPos - 1));
                                                             habitList.update(tempHabit);
                                                         }
                                                     }
                                                 }


                                             }
                                             dismiss();
                                         }
                                     }
        );

        Button cancelBtn = (Button) view.findViewById(R.id.discardBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
                                         public void onClick(View v) {
                                             dismiss();
                                         }
                                     }
        );

        return view;
    }
}
