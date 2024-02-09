'use strict';
document.getElementById("searchMyGroup").addEventListener("click", function () {
    window.location.href = "/myGroup";
});

// itt kezdodik a chat funkcio megvalositasa

const chatPage = document.querySelector('#chat-page');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message-input');
//const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-box');

let stompClient = null;
let IdForUser = null;
let fullname = null;
let selectedUserId = null;

function connectToWebSocket() {
    var element = document.querySelector('[id^="large-message-box-for-"]');
    IdForUser = element.id.substring('large-message-box-for-'.length);
    console.log("Itt mar az userId: " + IdForUser);

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    // stompClient.connect({}, function (frame) {
    //     // Feliratkozás a /topic/userStatusUpdate-re
    //     stompClient.subscribe('/user/public/userStatusUpdate', function (message) {
    //         var userStatusUpdate = JSON.parse(message.body);
    //         handleUserStatusUpdate(userStatusUpdate.userId, userStatusUpdate.status);
    //     });
    // });

    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe(`/user/${IdForUser}/queue/messages`, onMessageReceived)
    stompClient.subscribe('/user/public/userStatusUpdate', function (message) {
        var userStatusUpdate = JSON.parse(message.body);
        handleUserStatusUpdate(userStatusUpdate.userId, userStatusUpdate.status);
    });
}

function userItemClick(event) {
    document.querySelectorAll('.user').forEach(item => {
        item.classList.remove('active');
    });

    if (messageForm.attributes.getNamedItem('style')) {
        messageForm.attributes.removeNamedItem('style');
    }

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    var selectedUser = clickedUser.getAttribute('id');
    selectedUserId = selectedUser.substring('user-'.length);
    console.log("selectedUserId: ", selectedUserId);
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hiddenMsg');
    nbrMsg.textContent = '0';
}

function displayMessage(senderId, content) {

    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    // console.log("senderId a display-nel: ", senderId);
    // console.log("IdForUser a display-nel: ", IdForUser);
    if (senderId.toString() === IdForUser) {
        messageContainer.classList.add('my-message');
    } else {
        messageContainer.classList.add('partner-message');
    }

    messageContainer.textContent = content;
    chatArea.appendChild(messageContainer);
    scrollToBottom();
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${IdForUser}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    //document.querySelector('.infoBox').classList.remove('hiddenInfo');
    const infoContainer = document.createElement('h3');
    infoContainer.classList.add('info');
    infoContainer.textContent = "If you both are not favorites to each other, the message will not be retained!" +
        "If you want the message to be retained and easily reach each other, then add each other to favorites!";
    infoContainer.style.color = '#bd532c';
    infoContainer.style.textAlign = 'center';
    chatArea.appendChild(infoContainer);

    userChat.forEach(chat => {
        console.log("chat: ", chat);
        displayMessage(chat.senderId, chat.content);
    });
}

function onError(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceived(payload) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    //await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    console.log("Message Received tole :" + selectedUserId)
    console.log("Message Received tole itt a masik:" + message.senderId)
    if (selectedUserId && selectedUserId === message.senderId.toString()) {
        console.log("Belep ide a displayMessage-be")
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${'user-' + selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${'user-' + message.senderId}`);
    console.log("notifiedUser: ", notifiedUser);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        console.log("Belep a notifiedUser-be")
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hiddenMsg');
        nbrMsg.textContent = '';
    } else if (notifiedUser === null) {
        var leftMessageBox = document.querySelector('.left-message-box');
        var userInfo = document.createElement('h3');
        userInfo.textContent = "Don't forget to add this user to your favorites to keep the message, otherwise, it will be lost!";
        userInfo.style.color = '#bd532c';
        userInfo.style.textAlign = 'center';
        var user = document.createElement('div');
        user.classList.add('user');
        user.id = 'user-' + message.senderId;
        user.addEventListener('click', userItemClick);
        var userImage = document.createElement('img');
        var userName = document.createElement('span');
        fetch(`/myGroup/favorites/getSenderUserImg?userId=${message.senderId}`, {
                method: "GET",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': token
                }
        }).then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Something went wrong');
            }
        }).then(data =>{
            console.log(data.selectedUserImg[0][0]);
            if (data.selectedUserImg === null) {
                userImage.src = "/img/anonym.jpg";
            } else {
                userImage.src = 'data:image/jpeg;base64,' + data.selectedUserImg[0][0];
            }
            console.log("Kep: " + userImage);
            userName.textContent = data.selectedUserImg[0][1] + " " + data.selectedUserImg[0][2];
        }).catch(error => {
            console.log("Error: ", error);
        });

        var userStatus = document.createElement('div');
        userStatus.classList.add('user-status');
        userStatus.classList.add('online');
        var nbrMsg = document.createElement('span');
        nbrMsg.classList.add('nbr-msg');
        nbrMsg.textContent = '';

        leftMessageBox.appendChild(user);
        user.appendChild(userImage);
        user.appendChild(userName);
        user.appendChild(userStatus);
        user.appendChild(nbrMsg);

        // var chatBox = document.querySelector('.chat-box');
        // chatBox.appendChild(userInfo);
    }
}


function handleUserStatusUpdate(userId, status) {
    // Kezelje a felhasználó státuszváltozását
    //console.log('Received user status update:', userId, status);
    // Implementáld a szükséges logikát a státuszváltozás kezelésére
    // Ebben a példában csak kiírjuk a konzolra az értesítést, de itt érdemes frissíteni az UI-t stb.
    var userElement = document.getElementById('user-' + userId);
    //console.log("userElement: ", userElement);
    if (userElement) {
        var statusElement = userElement.querySelector('.user-status');
        if (statusElement) {
            statusElement.className = 'user-status ' + (status === 1 ? 'online' : 'offline');
        }
    }
}

function sendMessage() {
    // Get the message input value
    var messageInput = $('#message-input').emojioneArea();
    var messageText = messageInput[0].emojioneArea.getText().trim();

    // Check if the message is not empty
    if (messageText !== '' && stompClient) {
        const chatMessage = {
            senderId: IdForUser,
            recipientId: selectedUserId,
            content: messageText,
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(IdForUser, messageText);

        // Clear the EmojioneArea text
        messageInput[0].emojioneArea.setText('');

        // Optional: Set focus back on the EmojioneArea
        messageInput[0].emojioneArea.editor.focus();
    }
    scrollToBottom();
}

function scrollToBottom() {
    var chatBox = document.getElementById('chat-box');
    chatBox.scrollTop = chatBox.scrollHeight;
}

// idaig tart a chat funkcio megvalositasa



$(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    fetch('/myGroup/favorites/updateUserStatusToOnline', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Something went wrong');
        }
    }).then(data => {
        if (data === 'ok') {
            console.log("Sikeres modositas");
        } else {
            throw new Error('Something went wrong');
        }
    }).catch(error => {
        console.log("Error: ", error);
    });

    // Emojik beszúrása az input mezőbe
    $('#message-input').emojioneArea({
        pickerPosition: 'top',
        tonesStyle: 'bullet',
        events: {
            // Amikor egy emoji kerül kiválasztásra, illeszd be az input mezőbe
            emojibtn_click: function (button, event) {
                $('#message-input').emojioneArea('insert', button.html());
            },
            // Az Enter lenyomását figyeljük meg
            keydown: function (editor, event) {
                if (event.which === 13 && !event.shiftKey) {
                    event.preventDefault();
                    sendMessage();
                }
            }
        }
    });

    // Egyéb műveletek, pl. üzenet elküldése gombra kattintáskor
    $('#send-button').click(function () {
        sendMessage();
    });

    // Emoji gomb funkciója
    $('#emoji-button').click(function () {
        $('#message-input').emojioneArea('toggle');
    });

    // WebSocket kapcsolat létrehozása
    connectToWebSocket();

});



window.addEventListener('beforeunload', function (e) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    fetch('/myGroup/favorites/updateUserStatusToOffline', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Something went wrong');
        }
    }).then(data => {
        if (data === 'ok') {
            console.log("Sikeres modositas");
        } else {
            throw new Error('Something went wrong');
        }
    }).catch(error => {
        console.log("Error: ", error);
    });
});


