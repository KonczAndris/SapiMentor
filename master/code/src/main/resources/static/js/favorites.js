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
   // console.log("Itt mar az userId: " + IdForUser);

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
    // const divelement = clickedUser.querySelector('.username-messenger');
    // divelement.style.color = 'white';
    // console.log("selectedUserName: ", divelement);
   // console.log("selectedUserId: ", selectedUserId);

    // Elrejtjük az összes üzenet-fejlécet
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

    // itt ha rakattint a userre akkor a readOrNot-ot 1-re allitjuk mivel elolvasta
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
       // console.log("data: ", data);
        if(data === 'OK') {
            //console.log("Sikeres modositas");
        } else if (data === 'ERROR') {
           // console.log("Sikertelen modositas");
        }
    }).catch(error => {
        console.log("Error: ", error);
    });
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
        //console.log("chat: ", chat);
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
   // console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    //console.log("Message Received tole :" + selectedUserId)
    //console.log("Message Received tole itt a masik:" + message.senderId)

    // ha belep ide akkor a read_Or_not-ot 1 -re allitjuk mivel elolvasta
    if (selectedUserId && selectedUserId === message.senderId.toString()) {
        //console.log("Belep ide a displayMessage-be")
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
            //console.log("data: ", data);
            if(data === 'OK') {
                //console.log("Sikeres modositas");
            } else if (data === 'ERROR') {
                //console.log("Sikertelen modositas");
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
    //console.log("notifiedUser: ", notifiedUser);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
       // console.log("Belep a notifiedUser-be")
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
            //console.log(data.selectedUserImg[0][0]);
            if (data.selectedUserImg === null) {
                userImage.src = "/img/anonym.jpg";
            } else {
                userImage.src = 'data:image/jpeg;base64,' + data.selectedUserImg[0][0];
            }
            //console.log("Kep: " + userImage);
            userName.textContent = data.selectedUserImg[0][1] + " " + data.selectedUserImg[0][2];
            userName.style.color = 'rgb(20,115,100)'
        }).catch(error => {
            console.log("Error: ", error);
        });

        // var userStatus = document.createElement('div');
        // userStatus.classList.add('user-status');
        // userStatus.classList.add('online');
        var nbrMsg = document.createElement('span');
        nbrMsg.classList.add('nbr-msg-messenger');
        nbrMsg.textContent = '';

        leftMessageBox.appendChild(user);
        user.appendChild(userImgContainer);
        userImgContainer.appendChild(userImage);
        userImgContainer.appendChild(nbrMsg);
        user.appendChild(userName);
        //user.appendChild(userStatus);


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


    // fetch('/myGroup/favorites/updateUserStatusToOnline', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/x-www-form-urlencoded',
    //         'X-CSRF-TOKEN': token
    //     }
    // }).then(response => {
    //     if (response.ok) {
    //         return response.text();
    //     } else {
    //         throw new Error('Something went wrong');
    //     }
    // }).then(data => {
    //     if (data === 'ok') {
    //         //console.log("Sikeres modositas");
    //     } else {
    //         throw new Error('Something went wrong');
    //     }
    // }).catch(error => {
    //     console.log("Error: ", error);
    // });

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
        //console.log("dataigennem: ", data.chatMessageReadOrNotList);
        if (data.chatMessageReadOrNotList !== null) {
            for (var i = 0; i < data.chatMessageReadOrNotList.length; i++){
                //console.log("senderId: ", data.chatMessageReadOrNotList[i][0]);
                //console.log("recipientId: ", data.chatMessageReadOrNotList[i][1]);
                //console.log("readOrNot: ", data.chatMessageReadOrNotList[i][2]);
                //console.log("readOrNot: ");
                var senderUserId = data.chatMessageReadOrNotList[i][0];
                var recipientUserId = data.chatMessageReadOrNotList[i][1];
                var readOrNot = data.chatMessageReadOrNotList[i][2];
                // ha 0 akkor nincs elolvasva es akkor
                // ilyenkor meg kell jeleniteni a felhasznalot
                if (readOrNot === 0) {
                    const notifiedUserElement = document.querySelector(`#${'user-' + senderUserId}`);
                   // console.log("notifiedUser: ", notifiedUserElement);
                    if (notifiedUserElement && !notifiedUserElement.classList.contains('active')) {
                        //console.log("Belep a notifiedUser-be")
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
                            //console.log(data.selectedUserImg[0][0]);
                            if (data.selectedUserImg === null) {
                                userImageElement.src = "/img/anonym.jpg";
                            } else {
                                userImageElement.src = 'data:image/jpeg;base64,' + data.selectedUserImg[0][0];
                            }
                            //console.log("Kep: " + userImageElement);
                            userName.textContent = data.selectedUserImg[0][1] + " " + data.selectedUserImg[0][2];
                            //userName.style.color = 'rgb(25, 150, 114)'
                        }).catch(error => {
                            console.log("Error: ", error);
                        });

                        // var userStatus = document.createElement('div');
                        // userStatus.classList.add('user-status');
                        // userStatus.classList.add('online');
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

function showHeartIcons() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var favoriteIds = document.querySelectorAll('.favorite-id');

    favoriteIds.forEach(function(favoriteId) {
        var idValue = parseInt(favoriteId.textContent);

        //console.log("Favorite id: ", idValue);

        var parentCell = favoriteId.parentElement;
        //console.log("Parent cell: ", parentCell);
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
        //console.log("Parent cell id: ", parentCellId);
        var favoriteButton = document.getElementById('favoriteButton-' + parentCellId);
        //console.log("Favorite button: ", favoriteButton);

        favoriteButton.addEventListener('click', function() {
            //console.log("Favorite " + parentCellId + " button clicked");
            if (uncheckedHeart.classList.contains('active')) {
                //console.log("Hozzaadom a kedvencekhez");
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
                    console.log("Kiveszem a kedvencek kozul");
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

                    // Hide the sure-modal after the action is performed
                    const profileButtonContainerSure = document.getElementById('profile-button-container-sure-' + parentCellId);
                    if (profileButtonContainerSure) {
                        profileButtonContainerSure.style.display = 'none';
                        profileButtonContainer.style.display = 'flex';
                    }
                });

                cancelButton.addEventListener('click', () => {
                    // Hide the sure-modal without performing any action
                    if (profileButtonContainerSure) {
                        profileButtonContainerSure.style.display = 'none';
                        profileButtonContainer.style.display = 'flex';
                    }
                });
            }


        });
    });
}

// Hívás a függvényre, például az oldal betöltésekor vagy más eseményre
showHeartIcons();

