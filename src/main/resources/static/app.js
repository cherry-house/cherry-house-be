var stompClient = null;
var chatRoomId = 1;
var accessToken = null;

function setConnected(connected) {
    console.log('set connected');

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
    console.log('connect - Access Token:', accessToken);
    if (!accessToken) {
        alert('no Access Token');
        return;
    }

    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({ Authorization: accessToken }, function (frame) {
        setConnected(true);

        stompClient.heartbeat.outgoing = 10000;
        stompClient.heartbeat.incoming = 10000;

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
    var state = (isRead === true) ? "0" : "1";
    $("#messages").append("<tr><td>" + message + "</td></tr>")
        .append("<tr><td>" + state + "</td></tr>");
}

$(function () {
    $("#tokenBtn").click(function () {
        accessToken = $("#token").val();
        console.log('Access Token set:', accessToken);
        alert('ok');
    });
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMsg(); });
});