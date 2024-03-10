'use strict';

let stompClient = null;
let IdForUserInIndexPage = null;
function connectToWebSocketForIndexPage() {
    var elementToGetUserId = document.querySelector('[id^="userIdForIndexPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInIndexPage = elementToGetUserId.id.split("-")[1];
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

    console.log("stompClient: ", stompClient);

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

    document.querySelectorAll('.nbr-msg').forEach(item => {
        item.classList.remove('hiddenMsg');
        item.textContent = '';
    });
}

// ez azert hogy a valosideju ertesiteseket is megkapjuk
// es ha frissitunk vagy ha csak siman ugy kapunk ertesitest hogy nem vagyunk
// belepve akkor is megkapjuk az ertesitest
document.addEventListener('DOMContentLoaded', () => {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    connectToWebSocketForIndexPage();

    var elementToGetProfileUserId = document.querySelector('[id^="userIdForIndexPage-"]');
    var profileUserId = elementToGetProfileUserId.id.split("-")[1];

    fetch(`/getIndexProfileNotificationStatus?userId=${profileUserId}`, {
        method: "GET",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Request failed!');
        }
    }).then(data => {
        if (data === "OK") {
            document.querySelectorAll('.nbr-msg').forEach(item => {
                item.classList.remove('hiddenMsg');
                item.textContent = '';
            });
        }
    }).catch((error) => {
        console.log("Error:" + error);
    });

});



