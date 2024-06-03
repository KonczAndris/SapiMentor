'use strict';

let stompClient = null;
let IdForUserInUsersPage = null;
function connectToWebSocketForUsersPage() {
    var elementToGetUserId = document.querySelector('[id^="userIdForUsersPage-"]');
    IdForUserInUsersPage = elementToGetUserId.id.split("-")[1];

    var socket;
    if (window.location.protocol === "http:") {
        socket = new SockJS('http://' + window.location.host + '/ws');
        //console.log("http" + socket);
    } else {
        socket = new SockJS('https://' + window.location.host + '/ws');
        //console.log("https" + socket);
    }
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForUsersPage, onErrorForUsersPage);
}

function onConnectedForUsersPage() {
    stompClient.subscribe('/user/public/userStatusUpdate', function (message) {
        var userStatusUpdate = JSON.parse(message.body);
        handleUserStatusUpdateInUsersPage(userStatusUpdate.userId, userStatusUpdate.status, userStatusUpdate.online_at, userStatusUpdate.signed_in);
    });
}

function handleUserStatusUpdateInUsersPage(userId, status, online_at, signed_in) {
    let userElement = document.getElementById('user-row-' + userId);
    if (userElement) {
        var statusElement = userElement.querySelector('.user-status');
        if (statusElement) {
            statusElement.className = 'user-status ' + (status === 1 ? 'online' : 'offline');
        }

        let onlineAtElement = userElement.querySelector('.user-online_at');
        if(onlineAtElement && online_at !== null){
            onlineAtElement.textContent = online_at.year
                + "-" + online_at.monthValue
                + "-" + online_at.dayOfMonth
                + "T" + online_at.hour
                + ":" + online_at.minute
                + ":" + online_at.second;
        }

        let entriesElement = userElement.querySelector('.user-signed_in');
            if (signed_in === true){
                console.log("signed_in === true");
                let currentEntires = parseInt(entriesElement.textContent);
                let newEntries = currentEntires + 1;
                entriesElement.textContent = newEntries;
            }
    }
}

function onErrorForUsersPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

document.addEventListener('DOMContentLoaded', () => {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    connectToWebSocketForUsersPage();
});