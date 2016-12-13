package com.sok.socketudp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.Bind;
import butterknife.ButterKnife;
import tcppackage.TcpServer;
import udppackage.DeviceBean;
import udppackage.DeviceSearcher;

/**
 * Created by user on 2016/12/13.
 */

public class ServerActivity extends Activity {
    private static final String TAG = "ServerActivity";

    private static final int MESSAGE_SEARCH_START = 1;//搜索开始
    private static final int MESSAGE_SEARCH_FINISH = 2;//搜索结束
    private static final int MESSAGE_TCP_CLIENT_CONNECED = 3;//TCP客户端 链接上来了

    @Bind(R.id.srv_devlist)
    public ListView srv_devlist;
    @Bind(R.id.srv_edit)
    public EditText srv_edit;

    private ReentrantLock lock = new ReentrantLock();
    private BaseAdapter adpter;
    private List<DeviceBean> mDeviceList;

    private Handler mHandler;
    private TcpServer tcpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        ButterKnife.bind(this);
        initListView();
        initSocketServer();
        startSearcherDEV();
    }

    private void initSocketServer() {
        tcpServer = new TcpServer() {
            @Override
            public void addClient(Socket client) {
                if (client != null) {
                    try {
                        lock.lock();
                        String ip = client.getInetAddress().getHostAddress();
                        Log.i(TAG, " tcp 连接 - 客户端IP :" + ip);
                        for (DeviceBean dev : mDeviceList) {
                            if (ip.equals(dev.getIp()) && !dev.isConnect()) {
                                Log.i(TAG, "客户端IP :" + ip +"加入成功");
                                dev.setSocket(client);
                                dev.setConnect(true);
                            }
                        }
                        mHandler.sendEmptyMessage(MESSAGE_TCP_CLIENT_CONNECED);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        };
        tcpServer.start();
    }

    //初始化listview
    private void initListView() {
        mDeviceList = new ArrayList<>();
        adpter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (mDeviceList.size() > 0) {
                    return mDeviceList.size();
                }
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(ServerActivity.this).inflate(R.layout.server_devlist, null);
                    holder = new ViewHolder();
                    holder.devName = (TextView) convertView.findViewById(R.id.sev_dev_name);
                    holder.devIp = (TextView) convertView.findViewById(R.id.sev_dev_ip);
                    holder.state = (TextView) convertView.findViewById(R.id.sev_dev_state);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.devName.setText(mDeviceList.get(position).getName() + " - " + mDeviceList.get(position).getRoom());
                holder.devIp.setText(mDeviceList.get(position).getIp() + ":" + mDeviceList.get(position).getPort());
                holder.state.setText(mDeviceList.get(position).isConnect() ? "在线" : "离线");
                return convertView;
            }

            class ViewHolder {
                public TextView devName;
                public TextView devIp;
                public TextView state;
            }
        };
        //设置适配器
        srv_devlist.setAdapter(adpter);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SEARCH_START:

                        break;
                    case MESSAGE_SEARCH_FINISH:
                        adpter.notifyDataSetChanged();
                        startSearcherDEV();
                    case MESSAGE_TCP_CLIENT_CONNECED:
                        adpter.notifyDataSetChanged();
                        break;
                }
            }
        };
    }


    private void startSearcherDEV() {
        new DeviceSearcher() {
            @Override
            public void onSearchStart() {
                // 主要用于在UI上展示正在搜索
                mHandler.sendEmptyMessage(MESSAGE_SEARCH_START);
            }

            @Override
            public void onSearchFinish(Set deviceSet) {
                try {
                    lock.lock();
                    Iterator<DeviceBean> it = deviceSet.iterator();
                    while (it.hasNext()) {
                        DeviceBean dev = it.next();
                        if (!mDeviceList.contains(dev)) {
                            mDeviceList.add(dev);
                        }
                    }
                    mHandler.sendEmptyMessage(MESSAGE_SEARCH_FINISH);
                } finally {
                    lock.unlock();
                }
            }
        }.start();
    }


    //发送消息
    public void sendMsg(View view) {
        String msg = srv_edit.getText().toString();
        if (!"".equals(msg)){
            for (DeviceBean dev :mDeviceList){
                dev.sendMessage(msg);
            }
        }
    }

}
