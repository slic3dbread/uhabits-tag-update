package org.isoron.uhabits.activities.habits.edit;

import com.google.auto.factory.AutoFactory;

import org.isoron.uhabits.R;
import org.isoron.uhabits.commands.Command;
import org.isoron.uhabits.models.Frequency;

/**
 * Created by tpootool on 03/12/16.
 */

@AutoFactory(allowSubclasses = true)
public class CreateTagDialog extends TagDialog
{
//    @Override
//    protected int getTitle()
//    {
//        return R.string.pref_toggle_title;
//    }
//
//    @Override
//    protected void initializeHabits()
//    {
//        modifiedHabit = modelFactory.buildHabit();
//        modifiedHabit.setFrequency(Frequency.DAILY);
//        modifiedHabit.setColor(
//                prefs.getDefaultHabitColor(modifiedHabit.getColor()));
//    }
//
//    @Override
//    protected void saveHabit()
//    {
//        Command command = appComponent
//                .getCreateHabitCommandFactory()
//                .create(habitList, modifiedHabit);
//        commandRunner.execute(command, null);
//    }
}
