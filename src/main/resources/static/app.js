var stompClient = null;

var chatRoomId = 1;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/' + chatRoomId, function (msg) {
            var result = JSON.parse(msg.body);
            showMessages(result.message, result.isRead);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMsg() {
    stompClient.send(
        "/app/chat." + chatRoomId,
        {},
        JSON.stringify({
            'sender': 'cherry1@cherry.com',
            'message': $("#msg").val(),
            'type': 'text'
        })
    );
}

function showMessages(message, isRead) {
    var state = (isRead === true) ? "읽음" : "안읽음";
    $("#messages").append("<tr><td>" + message + "</td></tr>")
        .append("<tr><td>" + state + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMsg(); });
});