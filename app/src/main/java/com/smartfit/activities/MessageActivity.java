package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.smartfit.MessageEvent.HxMessageList;
import com.smartfit.R;
import com.smartfit.adpters.ListMessageAllInfoAdaper;
import com.smartfit.beans.ListMessageAllInfoItem;
import com.smartfit.beans.MesageInfo;
import com.smartfit.beans.MessageAllInfo;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.commons.MessageType;
import com.smartfit.fragments.CustomAnimationDemoFragment;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.container)
    FrameLayout container;

    private final static int MSG_REFRESH = 2;

    private List<ListMessageAllInfoItem> listMessageAllInfoItems = new ArrayList<>();


    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();

    protected boolean isConflict;
    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
        addLisener();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CustomAnimationDemoFragment())
                    .commit();
        }

        eventBus = EventBus.getDefault();
        eventBus.register(this);

    }


    @Subscribe
    public void onEvent(Object event) {/* Do something */

        if (event instanceof HxMessageList) {
            getMessageList();

          /*  EMMessage item = ((HxMessageList) event).getMessages().get(0);

            ListMessageAllInfoItem msg = new ListMessageAllInfoItem();
            MesageInfo mesageInfo = new MesageInfo();
            mesageInfo.setContent(((EMMessage) item).getBody().toString().split(":")[1].replace("\"", ""));
            mesageInfo.setId(((EMMessage) item).getUserName());
            mesageInfo.setType("-1");//环信消息 类型
            mesageInfo.setUrl(Util.getFriendsInfoByUserid(((EMMessage) item).getUserName()).getUserPicUrl());
            mesageInfo.setTitle(Util.getFriendsInfoByUserid(((EMMessage) item).getUserName()).getNickName());
            mesageInfo.setTime(String.valueOf(((EMMessage) item).getMsgTime() / 1000));
            msg.setUnReadSysCount(String.valueOf(((HxMessageList) event).getMessages().size()));
            msg.setSysMessage(mesageInfo);
            LogUtil.w("dyc", "---");
            boolean isHaveInList = false;//消息是否存在列表中
            for (ListMessageAllInfoItem listMsg : listMessageAllInfoItems) {
                if (msg.getSysMessage().getType().equals(listMsg.getSysMessage().getType()) && msg.getSysMessage().getId().equals(listMsg.getSysMessage().getId())) {
//                    listMsg.setUnReadSysCount(String.valueOf(Integer.parseInt(listMsg.getUnReadSysCount())+((HxMessageList) event).getMessages().size()));
//                    listMsg.getSysMessage().setContent(msg.getSysMessage().getContent());
//                    listMsg.getSysMessage().setTime(msg.getSysMessage().getTime());
                    listMsg = msg;
                    listMsg.setUnReadSysCount(String.valueOf(Integer.parseInt(listMsg.getUnReadSysCount()) + ((HxMessageList) event).getMessages().size()));

                    isHaveInList = true;
                }
            }

            LogUtil.w("dyc", "---" + isHaveInList);
            if (!isHaveInList) {
                listMessageAllInfoItems.add(msg);
            }
            sortListItemByTime(listMessageAllInfoItems);
            listView.setAdapter(new ListMessageAllInfoAdaper(MessageActivity.this, listMessageAllInfoItems));*/
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getMessageList();

    }

    private void getMessageList() {
        listMessageAllInfoItems.clear();
//        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.MESSAGE_GETMESSAGEMAIN, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                MessageAllInfo messageAllInfo = JsonUtils.objectFromJson(response, MessageAllInfo.class);
                conversationList.clear();
                conversationList.addAll(loadConversationList());
                List<EMConversation> conversations = conversationList;
                if (conversations != null && conversations.size() > 0) {
                    if (conversationList.size() > 0) {
                        for (EMConversation item : conversationList) {
                            ListMessageAllInfoItem msg = new ListMessageAllInfoItem();
                            MesageInfo mesageInfo = new MesageInfo();
                            mesageInfo.setContent(item.getLastMessage().getBody().toString().split(":")[1].replace("\"", ""));
                            mesageInfo.setId(item.getUserName());
                            mesageInfo.setType("-1");//环信消息 类型
                            mesageInfo.setUrl(Util.getFriendsInfoByUserid(item.getUserName()).getUserPicUrl());
                            mesageInfo.setTitle(Util.getFriendsInfoByUserid(item.getUserName()).getNickName());
                            mesageInfo.setTime(String.valueOf(item.getLastMessage().getMsgTime() / 1000));
                            msg.setUnReadSysCount(String.valueOf(item.getUnreadMsgCount()));
                            msg.setSysMessage(mesageInfo);
                            listMessageAllInfoItems.add(msg);
                        }
                    }
                }

                if (messageAllInfo != null) {
                    if (!TextUtils.isEmpty(messageAllInfo.getUnReadSysCount()) && null != messageAllInfo.getSysMessage()) {
                        listMessageAllInfoItems.add(new ListMessageAllInfoItem(messageAllInfo.getUnReadSysCount(), messageAllInfo.getSysMessage()));
                    }
                    if (!TextUtils.isEmpty(messageAllInfo.getUnReadCourseCount()) && null != messageAllInfo.getCourseMessage()) {
                        listMessageAllInfoItems.add(new ListMessageAllInfoItem(messageAllInfo.getUnReadCourseCount(), messageAllInfo.getCourseMessage()));
                    }
                    if (!TextUtils.isEmpty(messageAllInfo.getUnReadFriendCount()) && null != messageAllInfo.getFriendMessage()) {
                        listMessageAllInfoItems.add(new ListMessageAllInfoItem(messageAllInfo.getUnReadFriendCount(), messageAllInfo.getFriendMessage()));
                    }

                    if (listMessageAllInfoItems.size() > 0) {
                        sortListItemByTime(listMessageAllInfoItems);
                        listView.setAdapter(new ListMessageAllInfoAdaper(MessageActivity.this, listMessageAllInfoItems));
                        listView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    } else {
                        listView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listMessageAllInfoItems.size() > 0) {
                    sortListItemByTime(listMessageAllInfoItems);
                    listView.setAdapter(new ListMessageAllInfoAdaper(MessageActivity.this, listMessageAllInfoItems));
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(this);
        mQueue.add(request);
    }


    private void initView() {
        tvTittle.setText("消息");
        ivBack.setVisibility(View.INVISIBLE);
    }


    private void addLisener() {


        /***
         * /**
         * 1-系统消息；2-课程邀请消息；3-预约成功消息；4-课程请求消息；5-好友邀请消息；6-订单成功消息；7拒绝课程；
         * 8请求代课－发给教练 9教练代课成功－发给用户确认 10教练代课确认－发送给请求代课的教练
         */


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListMessageAllInfoItem item = listMessageAllInfoItems.get(position);
                if (!TextUtils.isEmpty(item.getSysMessage().getType())) {
                    if (item.getSysMessage().getType().equals("-1") && null != Util.getFriendsInfoByUserid(item.getSysMessage().getId())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(EaseConstant.EXTRA_USER_ID, item.getSysMessage().getId());
                        //我要发起的用户信息
                        bundle.putString("name", Util.getFriendsInfoByUserid(item.getSysMessage().getId()).getNickName());
                        bundle.putString("icon", Util.getFriendsInfoByUserid(item.getSysMessage().getId()).getUserPicUrl());
                        String userInfo = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
                        UserInfoDetail userInfoDetail;
                        if (!TextUtils.isEmpty(userInfo)) {
                            userInfoDetail = JsonUtils.objectFromJson(userInfo, UserInfoDetail.class);
                            bundle.putString("user_icon", userInfoDetail.getUserPicUrl());
                        } else {
                            bundle.putString("user_icon", "");
                        }
                        openActivity(ChatActivity.class, bundle);
                    }
                    if (Integer.parseInt(item.getSysMessage().getType()) > 0) {
                        if (item.getSysMessage().getType().equals(MessageType.MESSAGE_TYPE_SYTEM)) {
                            openActivity(SystemMessageListActivity.class);
                        } else if (item.getSysMessage().getType().equals(MessageType.MESSAGE_TYPE_FRIEND_INVITE)) {
                            openActivity(FriendsMessageActivity.class);
                        } else {
                            openActivity(CourseMessageActivity.class);
                        }
                    }
                }
            }
        });
    }

    protected void setUpView() {
        conversationList.addAll(loadConversationList());
    }

    protected List<EaseUser> contactList;
    private Map<String, EaseUser> contactsMap;


    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * 刷新页面
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * 获取会话列表
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }


    private void sortListItemByTime(List<ListMessageAllInfoItem> listMessageAllInfoItems) {
        Collections.sort(listMessageAllInfoItems, new Comparator<ListMessageAllInfoItem>() {
            @Override
            public int compare(ListMessageAllInfoItem lhs, ListMessageAllInfoItem rhs) {
                if (Long.parseLong(lhs.getSysMessage().getTime()) == (Long.parseLong(rhs.getSysMessage().getTime()))) {
                    return 0;
                } else if (Long.parseLong(lhs.getSysMessage().getTime()) < (Long.parseLong(rhs.getSysMessage().getTime()))) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         *
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(EMConversation conversation);
    }


}
