$(function () {
    socket.init();
});
var basePath="ws://localhost:8080/seckill";
socket = {
    webSocket : "",
    init : function () {
        //userId:自行追加
        if ('webSocket' in window){
            webSocket = new WebSocket(basePath+'webSocket/1');
        }
        else if ('MozWebSocket' in window){
            webSocket = new MozWebSocket(basePath+'webSocket/1');
        }
        else{
            webSocket =new SockJS(basePath+'sockjs/webSocket');
        };
        webSocket.onerror= function (event) {
            alert("websocket连接发生错误，请刷新页面重试！")
        };
        webSocket.onopen = function (event) {

        };
        webSocket.onmessage = function (event) {
            var message = event.data;
            //判断秒杀是否成功，自行处理逻辑
            alert(message);
        };
    }
}