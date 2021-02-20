/*
import SockJS from "sockjs-client";
import Stomp from "stompjs";

connect() {
    console.log("Connection au WebSocket :");
    let ws = new SockJS(webSocketEndpoint);
    stompClient = Stomp.overWS(ws);

    // eslint-disable-next-line no-unused-vars
    stompClient.connect({}, (frame) => {
        stompClient.subscribe(topic, (sdkEvent) => {
            this.onMessageReceived(sdkEvent);
        })
    }, this.errorCallBack);
}
 */