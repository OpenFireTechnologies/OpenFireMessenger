/*
 * Copyright (c) 2013. Alexander Martinz @ OpenFire Security
 */

package net.openfiresecurity.messenger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import net.openfiresecurity.fragments.Chat;
import net.openfiresecurity.fragments.Contacts;
import net.openfiresecurity.fragments.PagerAdapter;
import net.openfiresecurity.fragments.TabDepthTransformer;
import net.openfiresecurity.helper.AlertDialogManager;
import net.openfiresecurity.helper.Constants;
import net.openfiresecurity.messenger.push.ConnectionDetector;
import net.openfiresecurity.messenger.push.ServerUtilities;

import java.util.HashMap;

import static net.openfiresecurity.messenger.MainService.chat;
import static net.openfiresecurity.messenger.push.CommonUtilities.SENDER_ID;

public class MainView extends ActionBarActivity implements TabHost.OnTabChangeListener,
        ViewPager.OnPageChangeListener {

    private final int REQ_CONTACT = 10;

    private TabHost mTabHost;
    public static ViewPager mViewPager;

    private final HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainView.TabInfo>();
    private PagerAdapter mPagerAdapter;

    private static Context currentInstance;

    // Asyntask
    private AsyncTask<Void, Void, Void> mRegisterTask;

    // Alert dialog manager
    private final AlertDialogManager alert = new AlertDialogManager();

    // Accounts
    public static Account account;

    // Connection detector
    private ConnectionDetector cd;

    // UPDATE
    private DownloadManager mgr;
    private Request req;

    public static String nameOwn;
    public static String emailOwn;

    /* Sherlock Actionbar */
    public static ActionBar ac;

    private class TabInfo {
        private final String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            clss = clazz;
            this.args = args;
        }

    }

    class TabFactory implements TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {

            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    public static void makeToast(String msg) {
        Toast.makeText(currentInstance, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mainview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MainService.isActive = true;
        ac = getSupportActionBar();
        currentInstance = MainView.this;

        cd = new ConnectionDetector(getApplicationContext());

        mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        // Check if Internet present
        if (cd.isConnectingToInternet()) {
            alert.showAlertDialog(MainView.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        }

        // Accounts
        account = AccountManager.get(getBaseContext()).getAccountsByType(
                "net.openfiresecurity.messenger")[0];

        registerGCM();

        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        intialiseViewPager();

        if (MainService.prefs.getBoolean("showChangelog")) {
            if (MainService.prefs.getString("lastversion").isEmpty()
                    || (Integer.parseInt(MainService.prefs
                    .getString("lastversion")) < Integer
                    .parseInt(getVersionNumber()))) {
                new AlertDialogManager().showAlertDialog(MainView.this,
                        MainService.res.getString(R.string.changelog),
                        MainService.res.getString(R.string.changelogcontent),
                        true);
                MainService.prefs.setString("lastversion", getVersionNumber());
            }
        }
        if (MainService.prefs.getBoolean("autoupdate")) {
            new CheckVersion(MainView.this).execute();
        }
    }

	/* START MENU */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_mainview, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected Identify single menu
     * item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(
            MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add_contact:
                startActivityForResult(new Intent(MainView.this, AddContact.class),
                        REQ_CONTACT);
                return true;
            case R.id.menu_preferences:
                startActivity(new Intent(MainView.this,
                        net.openfiresecurity.data.Preferences.class));
                return true;
            case R.id.menu_exit:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

	/* END MENU */

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d("MainView", "Getting Extras");
        if (extras != null) {
            Log.d("MainView", "Extras not null");
            if (extras.containsKey("contact")) {
                Log.d("MainView", "Got contact");
                Log.d("MainView", "changing page");
                mViewPager.setCurrentItem(0);
                Log.d("MainView", "change contact");
                int emailId = MainService.contactsDB.getId(intent.getExtras()
                        .getString("contact").trim());
                Log.d("MainView", "emailId: " + emailId + " | contact: "
                        + intent.getExtras().getString("contact").trim());
                changeContact(emailId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQ_CONTACT) && (resultCode == RESULT_OK)) {
            String user = data.getStringExtra("user");
            String email = data.getStringExtra("email");
            MainService.createContact(email, user);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainService.isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainService.isActive = false;
    }

    private void registerGCM() {

        nameOwn = AccountManager.get(getBaseContext()).getUserData(account,
                "user");
        emailOwn = AccountManager.get(getBaseContext()).getUserData(account,
                "email");

        GCMRegistrar.checkDevice(this);

        // GCMRegistrar.checkManifest(this);

        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            if (!GCMRegistrar.isRegisteredOnServer(this)) {

                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        ServerUtilities.register(context, nameOwn, emailOwn,
                                regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag());
        super.onSaveInstanceState(outState);
    }

    private void intialiseViewPager() {

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                MainService.getFragments());
        mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        if (MainService.prefs.getBoolean("useAnimation")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mViewPager.setPageTransformer(true, new TabDepthTransformer());
            }
        }
    }

    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        TabInfo tabInfo;
        MainView.AddTab(this, mTabHost, mTabHost.newTabSpec("Contacts")
                .setIndicator("Contacts"), (tabInfo = new TabInfo("Contacts",
                Contacts.class, args)));
        mapTabInfo.put(tabInfo.tag, tabInfo);
        MainView.AddTab(this, mTabHost, mTabHost.newTabSpec("Chat")
                .setIndicator("Chat"), (tabInfo = new TabInfo("Chat",
                Chat.class, args)));
        mapTabInfo.put(tabInfo.tag, tabInfo);
        mTabHost.setOnTabChangedListener(this);

    }

    private static void AddTab(MainView activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tag) {
        int pos = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(pos);
        if (mViewPager.getCurrentItem() == 0) {
            MainService.hideSoftKeyboard(MainView.this);
            ac.setTitle(MainService.res.getString(R.string.contacts));
        } else if (mViewPager.getCurrentItem() == 1) {
            MainService.chat.changeActionBarTitle();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /* FRAGMENT CHAT */
    public void changeContact(int email) {
        chat.changeContact(email);
    }

	/* UPDATE */

    private String getVersionNumber() {
        int version = -1;
        try {
            version = getPackageManager().getPackageInfo(
                    "net.openfiresecurity.messenger", 0).versionCode;
        } catch (Exception e) {
            Log.e("MainView", "Error: " + e.getMessage());
        }
        return ("" + version);
    }

    public void update(final String result) {
        try {

            String version = getVersionNumber();
            if (Integer.parseInt(version) < Integer.parseInt(result)) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Update Available!").setMessage(
                        "A new Update is available!\nUpdate now?\n\n(Version Code "
                                + result + ")");
                dialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {

                                String appname = "OpenFireMessenger.apk";
                                req = new Request(Uri.parse(Constants.urls
                                        + appname));

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                }
                                req.setDescription("Updating!");
                                req.setTitle(appname);
                                req.setMimeType("application/vnd.android.package-archive");
                                req.setDestinationInExternalPublicDir(
                                        Environment.DIRECTORY_DOWNLOADS,
                                        appname);

                                mgr.enqueue(req);
                                makeToast("Downloading!");
                            }
                        });
                dialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                dialog.show();

            }
        } catch (Exception exc) {
            Log.d("MESSENGER", exc.getLocalizedMessage());
        }
    }
    /* END UPDATE */
}
