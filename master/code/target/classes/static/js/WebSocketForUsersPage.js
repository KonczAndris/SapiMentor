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
        handleUserStatusUpdateInUsersPage(userStatusUpdate.userId, userStatusUpdate.status, userStatusUpdate.online_at);
    });
}

function handleUserStatusUpdateInUsersPage(userId, status, online_at) {
    var userElement = document.getElementById('user-row-' + userId);
    if (userElement) {
        var statusElement = userElement.querySelector('.user-status');
        if (statusElement) {
            statusElement.className = 'user-status ' + (status === 1 ? 'online' : 'offline');
        }

        // itt kell majd beallitani az online_at erteket
        var onlineAtElement = userElement.querySelector('.user-online_at');
        if(onlineAtElement && online_at !== null){
            onlineAtElement.textContent = online_at.year
                + "-" + online_at.monthValue
                + "-" + online_at.dayOfMonth
                + "T" + online_at.hour
                + ":" + online_at.minute
                + ":" + online_at.second;

            console.log(online_at.year.length);
        }
    }
}

function onErrorForUsersPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

// ez azert hogy a valosideju ertesiteseket is megkapjuk
// es ha frissitunk vagy ha csak siman ugy kapunk ertesitest hogy nem vagyunk
// belepve akkor is megkapjuk az ertesitest
document.addEventListener('DOMContentLoaded', () => {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    connectToWebSocketForUsersPage();

    // var elementToGetProfileProfileUserId = document.querySelector('[id^="userIdForProfilePage-"]');
    // var profileprofileUserId = elementToGetProfileProfileUserId.id.split("-")[1];
    //
    // fetch(`/profile/getProfileNotificationStatus?userId=${profileprofileUserId}`, {
    //     method: "GET",
    //     headers: {
    //         'Content-Type': 'application/x-www-form-urlencoded',
    //         'X-CSRF-TOKEN': token
    //     }
    // }).then(response => {
    //     if (response.ok) {
    //         return response.text();
    //     } else {
    //         throw new Error('Request failed!');
    //     }
    // }).then(data => {
    //     if (data === "OK") {
    //         document.querySelectorAll('.nbr-msg').forEach(item => {
    //             item.classList.remove('hiddenMsg');
    //             item.textContent = '';
    //         });
    //     }
    // }).catch((error) => {
    //     console.log("Error:" + error);
    // });

});