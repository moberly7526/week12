package info.androidhive.loginandregistration;



import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import info.androidhive.loginandregistration.manager.ParentActivity2;
public class SettingsActivity extends ParentActivity2{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref_settings);

		//setOnPreferenceChange(findPreference("userName"));
		//setOnPreferenceChange(findPreference("userNameOpen"));
		setOnPreferenceChange(findPreference("useNotesAlert_ringtone"));
	}

	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// NavUtils.navigateUpFromSameTask(this);//누르게되면 아예 첫화면으로 가게되버림
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	};

	private void setOnPreferenceChange(Preference mPreference) {
		mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);

		onPreferenceChangeListener.onPreferenceChange(
				mPreference,
				PreferenceManager.getDefaultSharedPreferences(
						mPreference.getContext()).getString(
						mPreference.getKey(), ""));
	}

	private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			String stringValue = newValue.toString();

			if (preference instanceof EditTextPreference) {
				preference.setSummary(stringValue);

			} else if (preference instanceof ListPreference) {
		

				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

			} else if (preference instanceof RingtonePreference) {
		

				if (TextUtils.isEmpty(stringValue)) {
					
					preference.setSummary("무음");

				} else {
					Ringtone ringtone = RingtoneManager.getRingtone(
							preference.getContext(), Uri.parse(stringValue));

					if (ringtone == null) {
						// Clear the summary if there was a lookup error.
						preference.setSummary(null);

					} else {
						String name = ringtone
								.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}
			}

			return true;
		}

	};

}
