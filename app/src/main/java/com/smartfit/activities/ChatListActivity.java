package com.smartfit.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.smartfit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatListActivity extends BaseActivity {

    @Bind(R.id.container)
    FrameLayout container;


    EaseConversationListFragment easeConversationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);

        easeConversationListFragment = new EaseConversationListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, easeConversationListFragment).commit();
    }


}
