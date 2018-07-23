package com.dawn.banana.distributelock.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Dawn on 2018/7/23.
 */
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onLineCount = 0;
    //concurrent包的线程安全的set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServerSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户发送数据
    private Session session;

    //接受UserId
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        //加入在线中
        webSocketServerSet.add(this);
        //在线数加1
        addOnlineCount();
        LOGGER.info("有新窗开始监听："+userId+",当前在线人数为"+getOnlineCount());
        this.userId = userId;
        try {
            sendMessage("连接成功");
        }catch (IOException e){
            LOGGER.error("websocket IO 异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        //从set中删除
        webSocketServerSet.remove(this);
        //在线数减一
        subOnlineCount();
        LOGGER.info("有一连接关闭！当前在线人数为"+getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message,Session session){
        LOGGER.info("收到来自窗口"+userId+"的信息："+message);
        //群发消息
        for (WebSocketServer item : webSocketServerSet){
            try {
                item.sendMessage(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    /**
     *
     * @param session
     * @param error
     */
    public void onError(Session session,Throwable error){
        LOGGER.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public  static void sendInfo(String message,@PathParam("userId") String userId){
        LOGGER.info("推送消息到窗口"+userId+",推送内容"+message);
        for (WebSocketServer item:webSocketServerSet){
            try {
                //这里可以设定只推送个这个userId的，为null则全部推送
                if (userId == null){
                    item.sendMessage(message);
                }else if (item.userId.equals(userId)){
                    item.sendMessage(message);
                }
            }catch (IOException e){
                continue;
            }
        }

    }

    public static synchronized  int getOnlineCount(){
        return onLineCount;
    }
    public static synchronized void addOnlineCount(){
        WebSocketServer.onLineCount++;
    }
    public static synchronized void subOnlineCount(){
        WebSocketServer.onLineCount--;
    }
}
