'use strict';

let stompClient = null;
let IdForUserInTopicsPage = null;
function connectToWebSocketForTopicsPage() {
    var elementToGetTopicsUserId = document.querySelector('[id^="userIdForTopicsPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInTopicsPage = elementToGetTopicsUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
        console.log("sima ws-t hasznal");
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
        console.log("wss-t hasznal");
    }
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForTopicsPage, onErrorInTopicsPage);
}

function onConnectedForTopicsPage() {
    stompClient.subscribe(`/user/${IdForUserInTopicsPage}/queue/messages`, onMessageReceivedNotificationInTopicsPage)
}

function onErrorInTopicsPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceivedNotificationInTopicsPage(payload) {
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

    connectToWebSocketForTopicsPage();

    var elementToGetTopicsProfileUserId = document.querySelector('[id^="userIdForTopicsPage-"]');
    var topicsprofileUserId = elementToGetTopicsProfileUserId.id.split("-")[1];

    fetch(`/topics/getTopicsProfileNotificationStatus?userId=${topicsprofileUserId}`, {
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

