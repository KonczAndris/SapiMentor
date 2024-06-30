'use strict';
document.getElementById("searchMyGroup").addEventListener("click", function () {
    window.location.href = "/myGroup";
});


const chatPage = document.querySelector('#chat-page');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message-input');
const chatArea = document.querySelector('#chat-box');

let stompClient = null;
let IdForUser = null;
let fullname = null;
let selectedUserId = null;

function connectToWebSocket() {
    var element = document.querySelector('[id^="large-message-box-for-"]');
    IdForUser = element.id.substring('large-message-box-for-'.length);

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
        console.log("sima ws-t hasznal");
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
        console.log("wss-t hasznal");
    }
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe(`/user/${IdForUser}/queue/messages`, onMessageReceived)
    stompClient.subscribe('/user/public/userStatusUpdate', function (message) {
        var userStatusUpdate = JSON.parse(message.body);
        handleUserStatusUpdate(userStatusUpdate.userId, userStatusUpdate.status);
    });
}

async function userItemClick(event) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

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

    document.querySelectorAll('.right-message-box .chat-header').forEach(header => {
        header.style.display = 'none';
    });

    const selectedHeader = document.getElementById('user-header-' + selectedUserId);
    if (selectedHeader) {
        selectedHeader.style.display = 'block';
    }

    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg-messenger');
    nbrMsg.classList.add('hiddenMsg');
    nbrMsg.textContent = '0';

    fetch('/myGroup/favorites/updateReadOrNotStatus', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': token
        },
        body: JSON.stringify({
            senderId: selectedUserId,
            recipientId: IdForUser,
        })
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Error in SSE fetch');
        }
    }).then(data => {
        if(data === 'OK') {
        } else if (data === 'ERROR') {
        }
    }).catch(error => {
        console.log("Error: ", error);
    });
    showHeartIcons(selectedUserId);
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
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

    const infoContainer = document.createElement('h3');

    if (localStorage.getItem('language') === 'hungarian') {
        infoContainer.textContent = "Ez az üzenet csak akkor marad megőrizve, ha mindketten kedvencei vagytok egymásnak! " +
            "Ha azt szeretnéd, hogy az üzenet megmaradjon, akkor adjátok hozzá egymást a kedvenceitek listájához!";
    }
    else {
        infoContainer.textContent = "If you both are not favorites to each other, the message will not be retained! " +
            "If you want the message to be retained and easily reach each other, then add each other to favorites!";
    }
    infoContainer.style.color = '#bd532c';
    infoContainer.style.textAlign = 'center';
    chatArea.appendChild(infoContainer);

    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
}

function onError(error) {
}

async function onMessageReceived(payload) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId.toString()) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;

        fetch('/myGroup/favorites/updateReadOrNotStatus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': token
            },
            body: JSON.stringify({
                senderId: message.senderId,
                recipientId: IdForUser,
            })
        }).then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Error in SSE fetch');
            }
        }).then(data => {
            if(data === 'OK') {
            } else if (data === 'ERROR') {
            }
        }).catch(error => {
            console.log("Error: ", error);
        });
    }

    if (selectedUserId) {
        document.querySelector(`#${'user-' + selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${'user-' + message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg-messenger');
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
        var userImgContainer = document.createElement('div');
        userImgContainer.classList.add('user-img-container');
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
            if (data.selectedUserImg === null) {
                userImage.src = "/img/anonym.jpg";
            } else {
                userImage.src = 'data:image/jpeg;base64,' + data.selectedUserImg[0][0];
            }
            userName.textContent = data.selectedUserImg[0][1] + " " + data.selectedUserImg[0][2];
            userName.style.color = 'rgb(20,115,100)'
        }).catch(error => {
            console.log("Error: ", error);
        });

        var nbrMsg = document.createElement('span');
        nbrMsg.classList.add('nbr-msg-messenger');
        nbrMsg.textContent = '';

        leftMessageBox.appendChild(user);
        user.appendChild(userImgContainer);
        userImgContainer.appendChild(userImage);
        userImgContainer.appendChild(nbrMsg);
        user.appendChild(userName);
        var chatPage = document.querySelector('#chat-page');
        console.log("chatPage: ", chatPage);

        var chatHeader = document.createElement('div');
        chatHeader.classList.add('chat-header');
        chatHeader.classList.add('hiddenHeader');
        chatHeader.id = 'user-header-' + message.senderId;
        chatHeader.style.borderBottom = '0px solid green';
        chatHeader.style.borderTop = '2px solid green';
        var favoriteId = document.createElement('span');
        favoriteId.classList.add('favorite-id');
        favoriteId.id = 'favoriteId-' + message.senderId;
        favoriteId.textContent = '0';
        favoriteId.style.display = 'none';
        var profileButtonContainer = document.createElement('div');
        profileButtonContainer.classList.add('profile-button-container');
        profileButtonContainer.id = 'profile-button-container-' + message.senderId;
        var profileButton = document.createElement('button');
        profileButton.classList.add('profile-button');
        profileButton.id = 'favoriteButton-' + message.senderId;
        var checkedHeart = document.createElement('img');
        checkedHeart.classList.add('profile-button-icon');
        checkedHeart.classList.add('checked-heart');
        checkedHeart.src = "/img/checked-heart.png";
        checkedHeart.alt = "Favorite";
        var uncheckedHeart = document.createElement('img');
        uncheckedHeart.classList.add('profile-button-icon');
        uncheckedHeart.classList.add('unchecked-heart');
        uncheckedHeart.src = "/img/unchecked-heart.png";
        uncheckedHeart.alt = "Favorite";
        var profileButtonContainerSure = document.createElement('div');
        profileButtonContainerSure.classList.add('profile-button-container-sure');
        profileButtonContainerSure.id = 'profile-button-container-sure-' + message.senderId;
        profileButtonContainerSure.style.display = 'none';
        var sureButton = document.createElement('button');
        sureButton.classList.add('checkmark-button-profile');
        sureButton.classList.add('profile-button');
        sureButton.id = 'sureButton-' + message.senderId;
        var profileButtonIconSure = document.createElement('img');
        profileButtonIconSure.classList.add('profile-button-icon');
        profileButtonIconSure.classList.add('profile-button-icon-sure');
        profileButtonIconSure.src = "/img/checkmark-icon.png";
        profileButtonIconSure.alt = "Sure";
        var cancelButton = document.createElement('button');
        cancelButton.classList.add('cancel-button-profile');
        cancelButton.classList.add('profile-button');
        cancelButton.id = 'cancelButton-' + message.senderId;
        var profileButtonIconCancel = document.createElement('img');
        profileButtonIconCancel.classList.add('profile-button-icon');
        profileButtonIconCancel.classList.add('profile-button-icon-sure');
        profileButtonIconCancel.src = "/img/cancel-icon.png";
        profileButtonIconCancel.alt = "Sure";

        chatPage.appendChild(chatHeader);
        chatHeader.appendChild(favoriteId);
        chatHeader.appendChild(profileButtonContainer);
        profileButtonContainer.appendChild(profileButton);
        profileButton.appendChild(checkedHeart);
        profileButton.appendChild(uncheckedHeart);
        chatHeader.appendChild(profileButtonContainerSure);
        profileButtonContainerSure.appendChild(sureButton);
        sureButton.appendChild(profileButtonIconSure);
        profileButtonContainerSure.appendChild(cancelButton);
        cancelButton.appendChild(profileButtonIconCancel);
    }
}


function handleUserStatusUpdate(userId, status) {
    var userElement = document.getElementById('user-' + userId);
    if (userElement) {
        var statusElement = userElement.querySelector('.user-status');
        if (statusElement) {
            statusElement.className = 'user-status ' + (status === 1 ? 'online' : 'offline');
        }
    }
}

function sendMessage() {
    var messageInput = $('#message-input').emojioneArea();
    var messageText = messageInput[0].emojioneArea.getText().trim();

    if (messageText !== '' && stompClient) {
        const chatMessage = {
            senderId: IdForUser,
            recipientId: selectedUserId,
            content: messageText,
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(IdForUser, messageText);
        messageInput[0].emojioneArea.setText('');
        messageInput[0].emojioneArea.editor.focus();
    }
    scrollToBottom();
}

function scrollToBottom() {
    var chatBox = document.getElementById('chat-box');
    chatBox.scrollTop = chatBox.scrollHeight;
}

$(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $('#message-input').emojioneArea({
        pickerPosition: 'top',
        tonesStyle: 'bullet',
        events: {
            emojibtn_click: function (button, event) {
                $('#message-input').emojioneArea('insert', button.html());
            },
            keydown: function (editor, event) {
                if (event.which === 13 && !event.shiftKey) {
                    event.preventDefault();
                    sendMessage();
                }
            }
        }
    });

    $('#send-button').click(function () {
        sendMessage();
    });

    $('#emoji-button').click(function () {
        $('#message-input').emojioneArea('toggle');
    });
    connectToWebSocket();

    fetch(`/myGroup/favorites/readOrnot?userId=${IdForUser}`, {
        method: 'GET',
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
    }).then(data => {
        if (data.chatMessageReadOrNotList !== null) {
            for (var i = 0; i < data.chatMessageReadOrNotList.length; i++){
                var senderUserId = data.chatMessageReadOrNotList[i][0];
                var recipientUserId = data.chatMessageReadOrNotList[i][1];
                var readOrNot = data.chatMessageReadOrNotList[i][2];
                if (readOrNot === 0) {
                    const notifiedUserElement = document.querySelector(`#${'user-' + senderUserId}`);
                    if (notifiedUserElement && !notifiedUserElement.classList.contains('active')) {
                        const nbrMsg = notifiedUserElement.querySelector('.nbr-msg-messenger');
                        nbrMsg.classList.remove('hiddenMsg');
                        nbrMsg.textContent = '';
                    } else if (notifiedUserElement === null) {
                        var leftMessageBox = document.querySelector('.left-message-box');
                        var userInfo = document.createElement('h3');
                        userInfo.textContent = "Don't forget to add this user to your favorites to keep the message, otherwise, it will be lost!";
                        userInfo.style.color = '#bd532c';
                        userInfo.style.textAlign = 'center';
                        var userElement = document.createElement('div');
                        userElement.classList.add('user');
                        userElement.id = 'user-' + senderUserId;
                        userElement.addEventListener('click', userItemClick);
                        var chatHeaderElement = document.getElementById('chat-page');
                        var chatHeaderDiv = document.createElement('div');
                        chatHeaderDiv.classList.add('chat-header');
                        chatHeaderDiv.classList.add('hiddenHeader');
                        chatHeaderDiv.id = 'user-header-' + senderUserId;
                        chatHeaderDiv.style.borderBottom = '0px solid green';
                        chatHeaderDiv.style.borderTop = '2px solid green';
                        chatHeaderElement.appendChild(chatHeaderDiv)
                        var favoriteIdElement = document.createElement('span');
                        favoriteIdElement.classList.add('favorite-id');
                        favoriteIdElement.id = 'favoriteId-' + senderUserId;
                        favoriteIdElement.textContent = 0;
                        favoriteIdElement.style.display = 'none';
                        chatHeaderDiv.appendChild(favoriteIdElement);
                        var profileButtonContainerElement = document.createElement('div');
                        profileButtonContainerElement.classList.add('profile-button-container');
                        profileButtonContainerElement.id = 'profile-button-container-' + senderUserId;
                        chatHeaderDiv.appendChild(profileButtonContainerElement);
                        var profileButtonElement = document.createElement('button');
                        profileButtonElement.classList.add('profile-button');
                        profileButtonElement.id = 'favoriteButton-' + senderUserId;
                        profileButtonContainerElement.appendChild(profileButtonElement);
                        var checkedHeartElement = document.createElement('img');
                        checkedHeartElement.classList.add('profile-button-icon');
                        checkedHeartElement.classList.add('checked-heart');
                        checkedHeartElement.src = "/img/checked-heart.png";
                        checkedHeartElement.alt = "Favorite";
                        profileButtonElement.appendChild(checkedHeartElement);
                        var uncheckedHeartElement = document.createElement('img');
                        uncheckedHeartElement.classList.add('profile-button-icon');
                        uncheckedHeartElement.classList.add('unchecked-heart');
                        uncheckedHeartElement.src = "/img/unchecked-heart.png";
                        uncheckedHeartElement.alt = "Favorite";
                        profileButtonElement.appendChild(uncheckedHeartElement);
                        var profileButtonContainerSureElement = document.createElement('div');
                        profileButtonContainerSureElement.classList.add('profile-button-container-sure');
                        profileButtonContainerSureElement.id = 'profile-button-container-sure-' + senderUserId;
                        profileButtonContainerSureElement.style.display = 'none';
                        chatHeaderDiv.appendChild(profileButtonContainerSureElement);
                        var sureButtonElement = document.createElement('button');
                        sureButtonElement.classList.add('checkmark-button-profile');
                        sureButtonElement.classList.add('profile-button');
                        sureButtonElement.id = 'sureButton-' + senderUserId;
                        profileButtonContainerSureElement.appendChild(sureButtonElement);
                        var profileButtonIconSureElement = document.createElement('img');
                        profileButtonIconSureElement.classList.add('profile-button-icon');
                        profileButtonIconSureElement.classList.add('profile-button-icon-sure');
                        profileButtonIconSureElement.src = "/img/checkmark-icon.png";
                        profileButtonIconSureElement.alt = "Sure";
                        sureButtonElement.appendChild(profileButtonIconSureElement);
                        var cancelButtonElement = document.createElement('button');
                        cancelButtonElement.classList.add('cancel-button-profile');
                        cancelButtonElement.classList.add('profile-button');
                        cancelButtonElement.id = 'cancelButton-' + senderUserId;
                        profileButtonContainerSureElement.appendChild(cancelButtonElement);
                        var profileButtonIconCancelElement = document.createElement('img');
                        profileButtonIconCancelElement.classList.add('profile-button-icon');
                        profileButtonIconCancelElement.classList.add('profile-button-icon-sure');
                        profileButtonIconCancelElement.src = "/img/cancel-icon.png";
                        profileButtonIconCancelElement.alt = "Sure";
                        cancelButtonElement.appendChild(profileButtonIconCancelElement);
                        var userImageContainerElement = document.createElement('div');
                        userImageContainerElement.classList.add('user-img-container');
                        var userImageElement = document.createElement('img');
                        var userName = document.createElement('span');

                        fetch(`/myGroup/favorites/getSenderUserImg?userId=${senderUserId}`, {
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
                            if (data.selectedUserImg[0][0] === null) {
                                userImageElement.src = "/img/anonym.jpg";
                            } else {
                                userImageElement.src = 'data:image/jpeg;base64,' + data.selectedUserImg[0][0];
                            }
                            userName.textContent = data.selectedUserImg[0][1] + " " + data.selectedUserImg[0][2];
                        }).catch(error => {
                            console.log("Error: ", error);
                        });
                        var nbrMsg = document.createElement('span');
                        nbrMsg.classList.add('nbr-msg-messenger');
                        nbrMsg.textContent = '';
                        //userElement.style.opacity = 0.75;
                        leftMessageBox.appendChild(userElement);

                        userElement.appendChild(userImageContainerElement);
                        userImageContainerElement.appendChild(userImageElement);
                        userImageContainerElement.appendChild(nbrMsg);
                        userElement.appendChild(userName);
                    }
                }
            }
        } else {
            console.log("Nincs ilyen adat");
        }
    }).catch(error => {
        console.log("Error: ", error);
    });
});

function showHeartIcons(favoriteUserId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var favoriteId = document.getElementById('favoriteId-' + favoriteUserId);
    console.log("favoriteId: ", favoriteId);

    if (favoriteId === null) {
        favoriteId = document.getElementById('user-' + favoriteUserId);
    }
        var idValue = parseInt(favoriteId.textContent);
        var parentCell = favoriteId.parentElement;
        var checkedHeart = parentCell.querySelector('.checked-heart');
        var uncheckedHeart = parentCell.querySelector('.unchecked-heart');

        if (idValue === 0 || isNaN(idValue)) {
            uncheckedHeart.classList.add('active');
            checkedHeart.classList.add('inactive');
            uncheckedHeart.style.display = 'inline';
            checkedHeart.style.display = 'none';
        } else if (idValue === 1) {
            uncheckedHeart.classList.add('inactive');
            checkedHeart.classList.add('active');
            uncheckedHeart.style.display = 'none';
            checkedHeart.style.display = 'inline';
        }

        var parentCellId = parentCell.id.split("-")[2];
        var favoriteButton = document.getElementById('favoriteButton-' + parentCellId);
        favoriteButton.addEventListener('click', function() {
            if (uncheckedHeart.classList.contains('active')) {
                uncheckedHeart.style.display = 'none';
                checkedHeart.style.display = 'inline';
                uncheckedHeart.classList.remove('active');
                uncheckedHeart.classList.add('inactive');
                checkedHeart.classList.remove('inactive');
                checkedHeart.classList.add('active');
                var urlforsave = '/myGroup/saveFavorite?favoriteUserId=' + parentCellId;

                fetch(urlforsave, {
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
                        console.log("Sikeres mentes");
                    } else {
                        throw new Error('Something went wrong');
                    }
                }).catch(error => {
                    console.log("Error: ", error);
                });
            }

            else if (checkedHeart.classList.contains('active')) {
                const sureButton = document.getElementById('sureButton-' + parentCellId);
                const cancelButton = document.getElementById('cancelButton-' + parentCellId);
                const profileButtonContainerSure = document.getElementById('profile-button-container-sure-' + parentCellId);
                const profileButtonContainer = document.getElementById('profile-button-container-' + parentCellId);
                if (profileButtonContainerSure) {
                    profileButtonContainerSure.style.display = 'flex';
                    profileButtonContainer.style.display = 'none';
                }

                sureButton.addEventListener('click', () => {
                    uncheckedHeart.style.display = 'inline';
                    checkedHeart.style.display = 'none';
                    checkedHeart.classList.remove('active');
                    checkedHeart.classList.add('inactive');
                    uncheckedHeart.classList.remove('inactive');
                    uncheckedHeart.classList.add('active');

                    var urlforrevoke = '/myGroup/revokeFavorite?favoriteUserId=' + parentCellId;

                    fetch(urlforrevoke, {
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
                            console.log("Sikeres visszavonas");
                        } else {
                            throw new Error('Something went wrong');
                        }
                    }).catch(error => {
                        console.log("Error: ", error);
                    });

                    const profileButtonContainerSure = document.getElementById('profile-button-container-sure-' + parentCellId);
                    if (profileButtonContainerSure) {
                        profileButtonContainerSure.style.display = 'none';
                        profileButtonContainer.style.display = 'flex';
                    }
                });

                cancelButton.addEventListener('click', () => {
                    if (profileButtonContainerSure) {
                        profileButtonContainerSure.style.display = 'none';
                        profileButtonContainer.style.display = 'flex';
                    }
                });
            }
        });
}

window.onload = function() {
    adjustChatBoxHeight();
};

window.addEventListener('resize', function() {
    adjustChatBoxHeight();
});

function adjustChatBoxHeight() {
    var inputBoxHeight = document.getElementById('chat-input-box').offsetHeight;
    var totalHeight = document.getElementById('chat-page').offsetHeight;
    var chatBoxHeight = totalHeight - inputBoxHeight;
    document.getElementById('chat-box').style.height = chatBoxHeight + 'px';
}



