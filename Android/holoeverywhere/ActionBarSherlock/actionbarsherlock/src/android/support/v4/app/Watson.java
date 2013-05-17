package android.support.v4.app;

import android.util.Log;
import android.view.View;
import android.view.Window;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnCreatePanelMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnMenuItemSelectedListener;
import com.actionbarsherlock.ActionBarSherlock.OnPreparePanelListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

/**
 * I'm in ur package. Stealing ur variables.
 */
public abstract class Watson extends FragmentActivity implements OnCreatePanelMenuListener, OnPreparePanelListener, OnMenuItemSelectedListener {
    private static final String TAG = "Watson";
    private ArrayList<Fragment> mCreatedMenus;

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (ActionBarSherlock.DEBUG)
            Log.d(TAG, "[onCreatePanelMenu] featureId: " + featureId + ", menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            boolean result = onCreateOptionsMenu(menu);
            if (ActionBarSherlock.DEBUG)
                Log.d(TAG, "[onCreatePanelMenu] activity create result: " + result);

            MenuInflater inflater = getSupportMenuInflater();
            boolean show = false;
            ArrayList<Fragment> newMenus;

            if (mFragments.mAdded != null) {
                newMenus = new ArrayList<Fragment>();
                show = onCreatePanelMenuCyclic(mFragments, newMenus, menu, inflater);
                if (newMenus.size() == 0) {
                    newMenus = null;
                }
            } else {
                newMenus = null;
            }


            if (mCreatedMenus != null) {
                for (int i = 0; i < mCreatedMenus.size(); i++) {
                    Fragment f = mCreatedMenus.get(i);
                    if (newMenus == null || !newMenus.contains(f)) {
                        f.onDestroyOptionsMenu();
                    }
                }
            }

            mCreatedMenus = newMenus;

            if (ActionBarSherlock.DEBUG)
                Log.d(TAG, "[onCreatePanelMenu] fragments create result: " + show);
            result |= show;

            if (ActionBarSherlock.DEBUG) Log.d(TAG, "[onCreatePanelMenu] returning " + result);
            return result;
        }
        return false;
    }

    private boolean onCreatePanelMenuCyclic(FragmentManagerImpl fm, ArrayList<Fragment> newMenus, Menu menu, MenuInflater inflater) {
        boolean show = false;
        for (int i = 0; fm.mAdded != null && i < fm.mAdded.size(); i++) {
            Fragment f = fm.mAdded.get(i);
            if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && f instanceof OnCreateOptionsMenuListener) {
                show |= true;
                ((OnCreateOptionsMenuListener) f).onCreateOptionsMenu(menu, inflater);
                newMenus.add(f);
            }
            if (f != null && f.mChildFragmentManager != null) {
                show |= onCreatePanelMenuCyclic(f.mChildFragmentManager, newMenus, menu, inflater);
            }
        }
        return show;
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (ActionBarSherlock.DEBUG)
            Log.d(TAG, "[onPreparePanel] featureId: " + featureId + ", view: " + view + " menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            boolean result = onPrepareOptionsMenu(menu);
            if (ActionBarSherlock.DEBUG)
                Log.d(TAG, "[onPreparePanel] activity prepare result: " + result);

            boolean show = onPreparePanelCyclic(mFragments, menu);

            if (ActionBarSherlock.DEBUG)
                Log.d(TAG, "[onPreparePanel] fragments prepare result: " + show);
            result |= show;

            result &= menu.hasVisibleItems();
            if (ActionBarSherlock.DEBUG) Log.d(TAG, "[onPreparePanel] returning " + result);
            return result;
        }
        return false;
    }

    private boolean onPreparePanelCyclic(FragmentManagerImpl fm, Menu menu) {
        boolean show = false;
        for (int i = 0; fm.mAdded != null && i < fm.mAdded.size(); i++) {
            Fragment f = fm.mAdded.get(i);
            if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && f instanceof OnPrepareOptionsMenuListener) {
                show |= true;
                ((OnPrepareOptionsMenuListener) f).onPrepareOptionsMenu(menu);
            }
            if (f != null && f.mChildFragmentManager != null) {
                show |= onPreparePanelCyclic(f.mChildFragmentManager, menu);
            }
        }
        return show;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Sherlock menu handling
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (ActionBarSherlock.DEBUG)
            Log.d(TAG, "[onMenuItemSelected] featureId: " + featureId + ", item: " + item);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            if (onOptionsItemSelected(item)) {
                return true;
            }

            if (onMenuItemSelectedCyclic(mFragments, item)) {
                return true;
            }
        }
        return false;
    }

    private boolean onMenuItemSelectedCyclic(FragmentManagerImpl fm, MenuItem menuItem) {
        for (int i = 0; fm.mAdded != null && i < fm.mAdded.size(); i++) {
            Fragment f = fm.mAdded.get(i);
            if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && f instanceof OnOptionsItemSelectedListener) {
                if (((OnOptionsItemSelectedListener) f).onOptionsItemSelected(menuItem)) {
                    return true;
                }
            }
            if (f != null && f.mChildFragmentManager != null && onMenuItemSelectedCyclic(f.mChildFragmentManager, menuItem)) {
                return true;
            }
        }
        return false;
    }

    public abstract boolean onCreateOptionsMenu(Menu menu);

    public abstract boolean onPrepareOptionsMenu(Menu menu);

    public abstract boolean onOptionsItemSelected(MenuItem item);

    public abstract MenuInflater getSupportMenuInflater();

    /**
     * Fragment interface for menu creation callback.
     */
    public interface OnCreateOptionsMenuListener {
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater);
    }

    /**
     * Fragment interface for menu preparation callback.
     */
    public interface OnPrepareOptionsMenuListener {
        public void onPrepareOptionsMenu(Menu menu);
    }

    /**
     * Fragment interface for menu item selection callback.
     */
    public interface OnOptionsItemSelectedListener {
        public boolean onOptionsItemSelected(MenuItem item);
    }
}
