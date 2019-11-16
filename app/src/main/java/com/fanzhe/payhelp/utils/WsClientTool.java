package com.fanzhe.payhelp.utils;

import com.fanzhe.payhelp.config.App;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WsClientTool implements WebSocketListener {
    private static WsClientTool singleton;

    private WebSocket ws;

    private String url;

    private String token;

    private WsClientTool() {

    }

    public static WsClientTool getInstance(){
        if (singleton == null) {
            singleton = new WsClientTool();
        }
        return singleton;
    }
    public void connect(String serverUrl) {
        url = serverUrl;
        try {
            if (null == ws) {
                ws = new WebSocketFactory().createSocket(serverUrl);
                ws.addHeader("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                ws.setPingInterval(24 * 60 * 60 * 1000);
                ws.addListener(this);
                L.d( "ws.connectAsynchronously() is null=" + ws.toString());
            } else {
                ws = ws.recreate();
                L.d( "ws.connectAsynchronously() is not null=" + ws.toString());
            }
            ws.connectAsynchronously();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        if (null != ws && !ws.isOpen()) {
            ws.disconnect();
            ws = null;
            connect(url);
        }
    }

    public void disconnect() {
        if (null != ws) {
            ws.disconnect();
        }
    }

    public boolean isConnected() {
        return null != ws && ws.isOpen();
    }

    public void sendText(String content) {
        if (null != ws && ws.isOpen()) {
            L.d( "wsclient sendText =" + content);
            ws.sendText(content);
        }
    }

    @Override
    public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
        L.d( "ws.onStateChanged newState=" + newState.name());
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        L.d( "connected");


        sendText("链接成功");
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
        L.d( "connect error");
        cause.printStackTrace();
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        L.d( "disconnected");
    }

    @Override
    public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        L.d( "onFrame: " + frame.toString());
    }

    @Override
    public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        L.d( "onTextFrame: " + frame.toString());
    }

    @Override
    public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    @Override
    public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    @Override
    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    @Override
    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        L.d( "onTextMessage: " + text);
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

    }

    @Override
    public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        L.d( "onSendingFrame");
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        L.d( "onFrameSent");
    }

    @Override
    public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        L.d( "onFrameUnsent");
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        cause.printStackTrace();
        L.d( "onError");
    }

    @Override
    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        L.d( "onFrameError");
    }

    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
        L.d( "ws.onMessageError ");
    }

    @Override
    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
        cause.printStackTrace();
        L.d( "ws.onMessageDecompressionError ");
    }

    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
        L.d( "ws.onTextMessageError ");
    }

    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        L.d( "ws.onSendError ");
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        cause.printStackTrace();
        L.d( "ws.onUnexpectedError ");
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        cause.printStackTrace();
        L.d( "ws.handleCallbackError ");
    }

    @Override
    public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

    }
}
