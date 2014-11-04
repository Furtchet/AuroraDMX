package com.AuroraByteSoftware.AuroraDMX;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.*;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.AuroraByteSoftware.AuroraDMX.ui.ManualServerIP;
import com.AuroraByteSoftware.Billing.util.IabHelper;
import com.AuroraByteSoftware.Billing.util.IabResult;
import com.AuroraByteSoftware.Billing.util.Purchase;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	public static final String channels = "channels";
	public static final String manualserver = "manualserver";
	public static final String serveraddress = "serveraddress";
	public static final String restoredefaults = "restoredefaults";
	private static Thread t;
	private static SettingsActivity settings;

	/** {@inheritDoc} */
	@Override
	public void onBuildHeaders(List<Header> target) {
		String protocol = MainActivity.sharedPref.getString("select_protocol", "");
		if ("SACNUNI".equals(protocol))
			loadHeadersFromResource(R.xml.pref_headers_sacn_unicast, target);
		else if ("SACN".equals(protocol))
			loadHeadersFromResource(R.xml.pref_headers_sacn, target);
		else
			loadHeadersFromResource(R.xml.pref_headers_artnet, target);
		settings = this;
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();
			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(
						preference.getKey(), ""));
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("fade_up_time"));
			bindPreferenceSummaryToValue(findPreference("fade_down_time"));
			bindPreferenceSummaryToValue(findPreference(channels));
			
			
			// The content view embeds two fragments; now retrieve them and attach
	        // their "hide" button.
			
			
			findPreference("unlock_channels").setOnPreferenceClickListener(new OnPreferenceClickListener() {
	             public boolean onPreferenceClick(Preference preference) {
	                 //open browser or intent here
	            	 //System.out.println("unlock_channels"); 
	            	 if(MainActivity.getmHelper()!=null){
	            		 MainActivity.getmHelper().flagEndAsync();
		            	 MainActivity.getmHelper().launchPurchaseFlow(getActivity(), MainActivity.ITEM_SKU, 1001,
		                         mPurchaseFinishedListener, "DanExtraData");
	            	 }
	            	 return true;
	             }
	         });
			
			findPreference("select_protocol").setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					settings.invalidateHeaders();//loads the header with only one protocol
					return true;
				}
			});
		}
	}

	private static final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase, Context context) {
			if (result.isFailure()) {
				// Handle error
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG).show();

            } else if (purchase.getSku().equals(MainActivity.ITEM_SKU)) {
				System.out.println("Item bought");
			}
		}
	};
	
	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	public static class ArtnetPreferenceFragment extends PreferenceFragment {
		CheckBoxPreference checkboxPrefManual;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_artnet);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			// bindPreferenceSummaryToValue(findPreference("ip_address"));
			setHasOptionsMenu(true);

			checkboxPrefManual = (CheckBoxPreference) findPreference("checkboxPrefManual");
			checkboxPrefManual.setSummary(MainActivity.sharedPref.getString(
					SettingsActivity.manualserver, "192.168.0.0"));
			// Set listener
			checkboxPrefManual.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					checkboxPrefManual.setChecked(true);
					PreferenceCategory targetCategory = (PreferenceCategory) findPreference("targetCategory");
					ArrayList<CheckBoxPreference> list = getPreferenceList(targetCategory,
							new ArrayList<CheckBoxPreference>());
					for (CheckBoxPreference p : list) {
						p.setChecked(false);// Uncheck the other boxes
					}
					if (newValue.equals(true))// Only ask for text when checking
												// not unchecking box
						ManualServerIP.askForString(getActivity(), checkboxPrefManual);
					return true;
				}
			});
			refreshServers();
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.server, menu);
		}

		/**
		 * Event Handling for Individual menu item selected Identify single menu
		 * item by it's id
		 * */
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			switch (item.getItemId()) {
			case R.id.menu_server_refresh:
				refreshServers();
			}
			return true;
		}

		public void refreshServers() {
			//System.out.println("Refresh");
			PreferenceCategory targetCategory = (PreferenceCategory) findPreference("targetCategory");
			if (targetCategory != null)
				targetCategory.removeAll();
			MainActivity.progressDialog = ProgressDialog.show(getActivity(), "",
					"Searching for ArtNet devices...");
			SendArtnetPoll poll = new SendArtnetPoll();
			poll.setContext(getActivity().getApplicationContext());
			t = new Thread(poll);
			t.start();// Start the scan thread

			// Start another thread to wait for t to finish
			(new Thread() {
				public void run() {
					while (t.isAlive()) {
						try {
							t.join(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					finishedSearch();
				}
			}).start();
		}

		/**
		 * Called when we are done waiting for an ArtnetPoll to come back. This
		 * updates the settings UI with the server list.
		 */
		public void finishedSearch() {
			// fetch the item where you wish to insert the CheckBoxPreference,
			// in this case a PreferenceCategory with key "targetCategory"
			PreferenceCategory targetCategory = (PreferenceCategory) findPreference("targetCategory");

			for (int srv = 0; srv < MainActivity.foundServers.size() - 1; srv += 2) {
				// create one check box for each setting you need
				CheckBoxPreference checkBoxPreference = new CheckBoxPreference(getActivity());
				checkBoxPreference.setTitle(MainActivity.foundServers.get(srv));
				checkBoxPreference.setSummary(MainActivity.foundServers.get(srv + 1));
				checkBoxPreference.setKey("keyName" + srv);// make sure each key
															// is unique
				checkBoxPreference.setChecked(false);

				targetCategory.addPreference(checkBoxPreference);
			}

			ArrayList<CheckBoxPreference> list = getPreferenceList(targetCategory,
					new ArrayList<CheckBoxPreference>());
			for (final CheckBoxPreference p : list) {
				// Set listener
				p.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference, Object newValue) {
						p.setChecked(true);
						PreferenceCategory targetCategory = (PreferenceCategory) findPreference("targetCategory");
						ArrayList<CheckBoxPreference> list = getPreferenceList(targetCategory,
								new ArrayList<CheckBoxPreference>());
						for (CheckBoxPreference others : list) {
							if (!others.getKey().equalsIgnoreCase(p.getKey()))
								others.setChecked(false);// Uncheck the other boxes
						}
						if (preference instanceof CheckBoxPreference)//Set server IP
							MainActivity.sharedPref.edit().putString(SettingsActivity.serveraddress, (String) preference.getTitle()).commit();


						checkboxPrefManual = (CheckBoxPreference) findPreference("checkboxPrefManual");
						checkboxPrefManual.setChecked(false);
						return true;
					}

				});

			}
		}

		private ArrayList<CheckBoxPreference> getPreferenceList(Preference p,
				ArrayList<CheckBoxPreference> list) {
			if (p instanceof PreferenceCategory || p instanceof PreferenceScreen) {
				PreferenceGroup pGroup = (PreferenceGroup) p;
				int pCount = pGroup.getPreferenceCount();
				for (int i = 0; i < pCount; i++) {
					getPreferenceList(pGroup.getPreference(i), list); // recursive
																		// call
				}
			} else {
				list.add((CheckBoxPreference) p);
			}
			return list;
		}
	}
	
	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	public static class SacnPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_sacn);
			bindPreferenceSummaryToValue(findPreference("protocol_sacn_universe"));
			
		}
	}
	
	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	public static class SacnUnicastPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_sacn_unicast);
			bindPreferenceSummaryToValue(findPreference("protocol_sacn_universe"));
			bindPreferenceSummaryToValue(findPreference("protocol_sacn_unicast_ip"));
		}
	}

	/**
	 * Required to be Android API19 compliant 
	 * http://securityintelligence.com/new-vulnerability-android-framework-fragment-injection/
	 * @param fragmentName class name
	 * @return true if valid
	 */
	protected boolean isValidFragment (String fragmentName)
	{
	  if(GeneralPreferenceFragment.class.getName().equals(fragmentName))
	      return true;
	  if(SacnPreferenceFragment.class.getName().equals(fragmentName))
	      return true;
	  if(ArtnetPreferenceFragment.class.getName().equals(fragmentName))
	      return true;
	  if(SacnUnicastPreferenceFragment.class.getName().equals(fragmentName))
	      return true;
	  return false;
	}
}