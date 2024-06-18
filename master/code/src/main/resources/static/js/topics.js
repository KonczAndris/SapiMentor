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

}

async function onMessageReceivedNotificationInTopicsPage(payload) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    document.querySelectorAll('.nbr-msg').forEach(item => {
        item.classList.remove('hiddenMsg');
        item.textContent = '';
    });
}

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

document.getElementById('info-navigation-card').addEventListener('click', function() {
    document.getElementById('large-topic-div').classList.remove('active');
    document.getElementById('large-info-div').classList.add('active');

});

document.getElementById('engineering-navigation-card').addEventListener('click', function() {
    document.getElementById('large-topic-div').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('languages-navigation-card').addEventListener('click', function() {
    document.getElementById('large-topic-div').classList.remove('active');
    document.getElementById('large-languages-div').classList.add('active');
});

document.getElementById('back-from-info').addEventListener('click', function() {
    document.getElementById('large-info-div').classList.remove('active');
    document.getElementById('large-topic-div').classList.add('active');
});

document.getElementById('back-from-engineering').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('large-topic-div').classList.add('active');
});

document.getElementById('back-from-languages').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('large-topic-div').classList.add('active');
});

document.getElementById('small-info-div').addEventListener('click', function() {
    document.getElementById('large-info-div').classList.remove('active');
    document.getElementById('info-site').classList.add('active');
});

document.getElementById('back-from-info-site').addEventListener('click', function() {
    document.getElementById('info-site').classList.remove('active');
    document.getElementById('large-info-div').classList.add('active');
});

document.getElementById('small-computer-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('computer-site').classList.add('active');
});

document.getElementById('back-from-computer-site').addEventListener('click', function() {
    document.getElementById('computer-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-automation-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('automation-site').classList.add('active');
});

document.getElementById('back-from-automation-site').addEventListener('click', function() {
    document.getElementById('automation-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-machine-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('machine-site').classList.add('active');
});

document.getElementById('back-from-machine-site').addEventListener('click', function() {
    document.getElementById('machine-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-mecha-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('mecha-site').classList.add('active');
});

document.getElementById('back-from-mecha-site').addEventListener('click', function() {
    document.getElementById('mecha-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-telecommunication-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('telecommunication-site').classList.add('active');
});

document.getElementById('back-from-telecommunication-site').addEventListener('click', function() {
    document.getElementById('telecommunication-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-landscape-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('landscape-site').classList.add('active');
});

document.getElementById('back-from-landscape-site').addEventListener('click', function() {
    document.getElementById('landscape-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-horticulture-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('horticulture-site').classList.add('active');
});

document.getElementById('back-from-horticulture-site').addEventListener('click', function() {
    document.getElementById('horticulture-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-communication-div').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('communication-site').classList.add('active');
});

document.getElementById('back-from-communication-site').addEventListener('click', function() {
    document.getElementById('communication-site').classList.remove('active');
    document.getElementById('large-languages-div').classList.add('active');
});

document.getElementById('small-translation-div').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('translation-site').classList.add('active');
});

document.getElementById('back-from-translation-site').addEventListener('click', function() {
    document.getElementById('translation-site').classList.remove('active');
    document.getElementById('large-languages-div').classList.add('active');
});