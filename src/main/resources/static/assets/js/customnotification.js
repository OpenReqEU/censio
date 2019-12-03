'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

$(document).ready(function() {
    console.log("Connect to notification");
    connect();
})

function connect(event) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/user/notification', onMessageReceived);
}


function onError(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}


function onMessageReceived(payload) {
    /**ON Receive**/
    var message = JSON.parse(payload.body)
    console.log(message)

    $('#notificationbubble').text(message["newmessages"]);

    $('#numberofnotifications').text(message["newmessages"] + " " + (message["newmessages"] > 1? "messages" : "message"));
    $('#numberofapps').text(message["numberofapps"] + " " +  (message["numberofapps"] > 1? "projects" : "project"));

}



