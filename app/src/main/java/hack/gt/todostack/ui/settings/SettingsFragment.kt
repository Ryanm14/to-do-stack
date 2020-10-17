package hack.gt.todostack.ui.settings

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import hack.gt.todostack.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // val taskPreference: DropDownPreference? = findPreference("task_priority")

        val preferences: MutableList<EditTextPreference?> = mutableListOf()
        preferences.add(findPreference("sunday_time"))
        preferences.add(findPreference("monday_time"))
        preferences.add(findPreference("tuesday_time"))
        preferences.add(findPreference("wednesday_time"))
        preferences.add(findPreference("thursday_time"))
        preferences.add(findPreference("friday_time"))
        preferences.add(findPreference("saturday_time"))

        // val mondayPreference: EditTextPreference? = findPreference("mondays")
        // configureWeekdayPreference(mondayPreference)
        for (preference in preferences) {
            configureWeekdayPreference(preference)
        }
    }

    private fun configureWeekdayPreference(preference: EditTextPreference?) {
        var defaultTime = 1.0
        preference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        preference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> { editText ->
            var time = defaultTime
            val text = editText.text
            var nullableTime: Double? = null

            if (text != null) {
                nullableTime = text.toDoubleOrNull()
            }
            if (nullableTime != null) {
                time = nullableTime
            }

            /*
            if (TextUtils.isEmpty(text)) {
                "Not set"
            } else {
                text + " hour(s)"
            }
             */
            "" + time + " hour(s)"
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}