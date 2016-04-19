package com.smartfit.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.CustomeCoachActivity;
import com.smartfit.activities.CustomeDynamicActivity;
import com.smartfit.activities.CustomeMainActivity;
import com.smartfit.activities.LoginActivity;
import com.smartfit.activities.MainActivity;
import com.smartfit.activities.MainBusinessActivity;
import com.smartfit.activities.MessageActivity;
import com.smartfit.commons.Constants;
import com.smartfit.utils.IntentUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.pathmenu.FilterMenu;
import com.smartfit.views.pathmenu.FilterMenuLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class CustomAnimationDemoFragment extends Fragment {

    public CustomAnimationDemoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_with_custom_animation, container, false);
        FilterMenuLayout filterMenuLayout = (FilterMenuLayout) rootView.findViewById(R.id.filter_menu);
        attachMenu(filterMenuLayout);
        filterMenuLayout.collapse(true);
        return rootView;
    }

    private FilterMenu attachMenu(FilterMenuLayout layout) {
        return new FilterMenu.Builder(getActivity())
                .addItem(R.mipmap.icon_home5, getString(R.string.menu_main))
                .addItem(R.mipmap.icon_home4, getString(R.string.menu_class))
                .addItem(R.mipmap.icon_home1, getString(R.string.menu_dynamic))
                .addItem(R.mipmap.icon_home2, getString(R.string.menu_info))
                .addItem(R.mipmap.icon_home3, getString(R.string.menu_mine))
                .attach(layout)
                .withListener(listener)
                .build();
    }

    FilterMenu.OnMenuChangeListener listener = new FilterMenu.OnMenuChangeListener() {
        @Override
        public void onMenuItemClick(View view, int position) {
            switch (position) {
                case 0:
                    if (!MainActivity.class.getName().equals(IntentUtils.getRunningActivityName(getActivity()))) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
//
                    break;
                case 1:
                    if (!MainBusinessActivity.class.getName().equals(IntentUtils.getRunningActivityName(getActivity()))) {
                        ((BaseActivity) getActivity()).openActivity(MainBusinessActivity.class);
                    }
                    break;
                case 2:
                    if (!CustomeDynamicActivity.class.getName().equals(IntentUtils.getRunningActivityName(getActivity()))) {
                        ((BaseActivity) getActivity()).openActivity(CustomeDynamicActivity.class);
                    }
                    break;
                case 3:
//                    ((BaseActivity) getActivity()).openActivity(CustomeMainActivity.class);
//                    ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("敬请期待", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    ((BaseActivity) getActivity()).openActivity(MessageActivity.class);
                    break;
                case 4:
                    if (NetUtil.isLogin(getActivity())) {
                        if (!CustomeMainActivity.class.getName().equals(IntentUtils.getRunningActivityName(getActivity()))) {
                            String isICF = SharedPreferencesUtils.getInstance().getString(Constants.IS_ICF, "0");
                            if (isICF.equals("1")) {
                                ((BaseActivity) getActivity()).openActivity(CustomeCoachActivity.class);
                            } else {
                                ((BaseActivity) getActivity()).openActivity(CustomeMainActivity.class);
                            }
                        }
                    } else {
                        ((BaseActivity) getActivity()).openActivity(LoginActivity.class);
                    }
                    break;
            }
        }

        @Override
        public void onMenuCollapse() {

        }

        @Override
        public void onMenuExpand() {

        }
    };
}