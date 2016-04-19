package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartfit.MessageEvent.UpdateRoom;
import com.smartfit.R;
import com.smartfit.adpters.ChangeRoomAdapter;
import com.smartfit.beans.IdleClassListInfo;
import com.smartfit.commons.Constants;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择教室
 */
public class SelectVenueRoomsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    ListView listView;

    private IdleClassListInfo idleClassListInfo;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_venue_rooms);
        eventBus = EventBus.getDefault();

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTittle.setText("更换教室");
        idleClassListInfo = (IdleClassListInfo) getIntent().getSerializableExtra(Constants.PASS_IDLE_CLASS_INFO);

        listView.setAdapter(new ChangeRoomAdapter(this,idleClassListInfo.getClassroomList()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventBus.post(new UpdateRoom(position));
                finish();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
