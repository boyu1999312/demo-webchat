package com.test.heartserver.server;


import com.test.common.util.JsonMapper;
import com.test.common.vo.MsgVo;
import lombok.Data;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@ServerEndpoint("/webServer/{sid}")
@Component
public class WebSocketServer {

    static Log log = LogFactory.getLog(WebSocketServer.class);
    //静态变量，记录在线数，应为线程安全
    private static int onlineCount = 0;
    //线程安全的Set
    private static CopyOnWriteArraySet<WebSocketServer> arraySet =
            new CopyOnWriteArraySet();
    //线程安全的list
    private static CopyOnWriteArrayList<String> list =
            new CopyOnWriteArrayList();
    //与客户端的连接
    private Session session;

    //存放历史消息
    private static List<String> msgList = new ArrayList<>(102);
    //接收sid
    private String sid = null;

    /**
     * 连接成功调用的方法
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){
        if(!arraySet.isEmpty()){

        }
        this.session = session;
        arraySet.add(this);
        addOnlineCount();
        log.info("有新连接开始监听：" + sid + "，当前在线人数：" + getOnlineCount());
        this.sid = sid;
        try {
            if(msgList.size() > 0){
                for (int i = msgList.size() - 1; i > -1; i--) {
                    String msg = msgList.get(i);
                    log.info("缓存消息：" + msg);
                    sendMessage(msg);
                }
            }
            sendMessage(JsonMapper.toJson(
                    new MsgVo()
                            .setType(1)
                            .setMsg("连接成功！")
                            .setTime(new Date())
            ));
        }catch (IOException e){
            log.error("websocket 异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        arraySet.remove(this);
        subOnlineCount();
        log.info("一个连接断开，当前在线人数：" + getOnlineCount());
    }

    /**
     * 收到客户端消息调用的方法
     * @param session
     * @param msg
     */
    @OnMessage
    public void onMessage(Session session, String msg){
        log.info("收到" + sid + ", session -> " + session + " 的消息：" + msg);
        MsgVo msgVo = JsonMapper.toObject(msg, MsgVo.class);

        //移除第100条消息
        if(msgVo.getType() == 2){
            log.info("缓存了消息：" + msg);
            msgList.add(0, msg);
        }
        if(msgList.size() > 100){
            msgList.remove(100);
        }
        //群发(除了本人)
        arraySet.forEach((item)->{
            try {
                if(item == this){
                    item.sendMessage(
                            JsonMapper.toJson(msgVo
                            .setType(1)
                            .setMsg(null)
                            .setTime(new Date())));
                }else {
                    item.sendMessage(msg);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public synchronized void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(String msg, @PathParam("sid")String sid){
        log.info("推送消息到窗口：" + sid + "，推送内容：" + msg);
        arraySet.forEach((item) -> {
            try {
                if(StringUtils.isEmpty(sid)){
                    item.sendMessage(msg);
                } else if(sid.equals(item.sid)){
                    item.sendMessage(msg);
                }
            } catch (IOException e) {
                log.error("发生错误：" + e);
            }
        });
    }



    private static synchronized int getOnlineCount() {
        return WebSocketServer.onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


}
