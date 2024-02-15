'use strict';

let stompClient = null;
let IdForUserInIndexPage = null;
function connectToWebSocketForIndexPage() {
    var elementToGetUserId = document.querySelector('[id^="userIdForIndexPage-"]');
    console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInIndexPage = elementToGetUserId.id.split("-")[1];
    console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForIndexPage, onError);
}

function onConnectedForIndexPage() {
    stompClient.subscribe(`/user/${IdForUserInIndexPage}/queue/messages`, onMessageReceivedNotification)

}

function onError(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceivedNotification(payload) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    console.log("Message Received tole itt a masik:" + message.senderId);

    document.querySelectorAll('.nbr-msg').forEach(item => {
        item.classList.remove('hiddenMsg');
        item.textContent = '';
    });
    

}

connectToWebSocketForIndexPage();