package com.lming.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 向PC端前端推送消息
 */
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】- websocket connected .,total={}",webSocketSet.size());
    }

    @OnClose
    public void onClose()
    {
        this.session = session;
        webSocketSet.remove(this);
        log.info("【websocket消息】- websocket closed .,total={}",webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("【websocket消息】- 收到客户端消息，message={}",message);
    }


    public void sendMessage(String message){
        for(WebSocket webSocket :webSocketSet)
        {
            log.info("【websocket消息】- 发送消息，message={}",message);
            try{
                webSocket.session.getBasicRemote().sendText(message);
            }
            catch (IOException e)
            {
                log.error("【websocket消息】- 发送消息失败，message={}",message);
                e.printStackTrace();
            }

        }

    }
}
