'use strict';

let stompClientForProfileWebSocket = null;
let IdForUserInProfilePage = null;
function connectToWebSocketForProfilePage() {
    var elementToGetProfileUserId = document.querySelector('[id^="userIdForProfilePage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInProfilePage = elementToGetProfileUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    var socket = new SockJS('/ws');
    stompClientForProfileWebSocket = Stomp.over(socket);

    stompClientForProfileWebSocket.connect({}, onConnectedForProfilePage, onErrorInProfilePage);
}

function onConnectedForProfilePage() {
    stompClientForProfileWebSocket.subscribe(`/user/${IdForUserInProfilePage}/queue/messages`, onMessageReceivedNotificationInProfilePage)
}

function onErrorInProfilePage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceivedNotificationInProfilePage(payload) {
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

    connectToWebSocketForProfilePage();

    var elementToGetProfileProfileUserId = document.querySelector('[id^="userIdForProfilePage-"]');
    var profileprofileUserId = elementToGetProfileProfileUserId.id.split("-")[1];

    fetch(`/profile/getProfileNotificationStatus?userId=${profileprofileUserId}`, {
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

