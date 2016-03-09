package com.smartfit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.smartfit.R;
import com.smartfit.activities.CustomeMainActivity;
import com.smartfit.activities.LoginActivity;
import com.smartfit.activities.MainActivity;
import com.smartfit.activities.MainUserActivity;

/**
     * A placeholder fragment containing a simple view.
     */
    public  class CustomAnimationDemoFragment extends Fragment {

        public CustomAnimationDemoFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            int actionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
            int subActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
            View rootView = inflater.inflate(R.layout.fragment_menu_with_custom_animation, container, false);


            ImageView fabContent = new ImageView(getActivity());
            fabContent.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home));

            FloatingActionButton darkButton = new FloatingActionButton.Builder(getActivity())
                                                  .setTheme(FloatingActionButton.THEME_DARK)
                                                  .setContentView(fabContent)
                                                  .setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
                                                  .build();

            SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(getActivity())
                                                   .setTheme(SubActionButton.THEME_DARK);

          View main_menu = LayoutInflater.from(getActivity()).inflate(R.layout.menu_main_view, null);
            View class_menu = LayoutInflater.from(getActivity()).inflate(R.layout.menu_class_view, null);
            View dynamic_meu = LayoutInflater.from(getActivity()).inflate(R.layout.menu_dynamic_view, null);
            View msg_menu = LayoutInflater.from(getActivity()).inflate(R.layout.menu_info_view, null);
            View mine_meun = LayoutInflater.from(getActivity()).inflate(R.layout.menu_mine_view, null);
            FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(subActionButtonSize, subActionButtonSize);
            blueParams.gravity= Gravity.CENTER;
            rLSubBuilder.setLayoutParams(blueParams);


            // Set 4 SubActionButtons
            final FloatingActionMenu centerBottomMenu = new FloatingActionMenu.Builder(getActivity())
                    .setStartAngle(170)
                    .setEndAngle(280)
//                    .setAnimationHandler(new SlideInAnimationHandler())
                    .addSubActionView(rLSubBuilder.setContentView(main_menu).build())
                    .addSubActionView(rLSubBuilder.setContentView(class_menu).build())
                    .addSubActionView(rLSubBuilder.setContentView(dynamic_meu).build())
                    .addSubActionView(rLSubBuilder.setContentView(msg_menu).build())
                    .addSubActionView(rLSubBuilder.setContentView(mine_meun).build())
                    .attachTo(darkButton)
                    .setRadius(actionMenuRadius)
                    .build();

            centerBottomMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                @Override
                public void onMenuOpened(FloatingActionMenu floatingActionMenu) {

                }

                @Override
                public void onMenuClosed(FloatingActionMenu floatingActionMenu) {

                }
            });

            main_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerBottomMenu.close(false);
                    ((MainActivity) getActivity()).openActivity(LoginActivity.class);

                }
            });

            class_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerBottomMenu.close(false);
                    ((MainActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("课程");
                }
            });


            dynamic_meu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerBottomMenu.close(false);
                    ((MainActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("动态");
                }
            });


            msg_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerBottomMenu.close(false);
                    ((MainActivity) getActivity()).openActivity(MainUserActivity.class);
                }
            });

            mine_meun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerBottomMenu.close(false);
                    ((MainActivity)getActivity()).openActivity(CustomeMainActivity.class);
                }
            });




            return rootView;
        }
    }