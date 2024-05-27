'use strict';

let stompClient = null;
let IdForUserInUsersPage = null;
function connectToWebSocketForUsersPage() {
    var elementToGetUserId = document.querySelector('[id^="userIdForUsersPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInUsersPage = elementToGetUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    var socket;
    if (window.location.protocol === "http:") {
        socket = new SockJS('http://' + window.location.host + '/ws');
        console.log("http" + socket);
    } else {
        socket = new SockJS('https://' + window.location.host + '/ws');
        console.log("https" + socket);
    }
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForUsersPage, onErrorForUsersPage);
}

function onConnectedForUsersPage() {
    stompClient.subscribe(`/user/${IdForUserInUsersPage}/queue/messages`, onMessageReceived)
    stompClient.subscribe('/user/public/userStatusUpdate', function (message) {
        var userStatusUpdate = JSON.parse(message.body);
        handleUserStatusUpdateInUsersPage(userStatusUpdate.userId, userStatusUpdate.status, userStatusUpdate.online_at);
    });
}

function handleUserStatusUpdateInUsersPage(userId, status, online_at) {
    var userElement = document.getElementById('user-' + userId);
    if (userElement) {
        var statusElement = userElement.querySelector('.user-status');
        if (statusElement) {
            statusElement.className = 'user-status ' + (status === 1 ? 'online' : 'offline');
        }

        // itt kell majd beallitani az online_at erteket
    }
}

function onErrorForUsersPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}