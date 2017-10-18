<html>
    <head></head>
    <body>
        <div>
        <#list orderDTOPage.content as orderDTO>
            ${orderDTO}

        </#list>
        </div>
        <div>
            <audio id="notice" loop="loop">
                <source src="/sell/mp3/order.mp3" type="audio/mpeg" />
            </audio>

        </div>
    </body>
    <script>
        var websocket = null;
        if('WebSocket' in window)
        {
            websocket = new WebSocket("ws://localhost/sell/webSocket")

        }else
        {
            alert("该浏览器不支持websocket!");
        }

        websocket.onopen = function (event) {
            console.log("建立连接");
            
        }
        websocket.onclose = function (event) {
            console.log("断开连接");

        }

        websocket.onmessage = function (event) {
            console.log("接受消息："+event.data);
            document.getElementById("notice").play();
            alert(event.data);

        }

        window.onbeforeunload = function () {
            websocket.close();
        }
    </script>



</html>