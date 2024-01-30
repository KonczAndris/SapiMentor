document.getElementById("searchMyGroup").addEventListener("click", function () {
    window.location.href = "/myGroup";
});

var stompClient = null;

function connectToWebSocket() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Feliratkozás a /topic/userStatusUpdate-re
        stompClient.subscribe('/topic/userStatusUpdate', function (message) {
            var userStatusUpdate = JSON.parse(message.body);
            handleUserStatusUpdate(userStatusUpdate.userId, userStatusUpdate.status);
        });
    });
}

function handleUserStatusUpdate(userId, status) {
    // Kezelje a felhasználó státuszváltozását
    console.log('Received user status update:', userId, status);
    // Implementáld a szükséges logikát a státuszváltozás kezelésére
    // Ebben a példában csak kiírjuk a konzolra az értesítést, de itt érdemes frissíteni az UI-t stb.
    var userElement = document.getElementById('user-' + userId);
    console.log("userElement: ", userElement);
    if (userElement) {
        var statusElement = userElement.querySelector('.user-status');
        if (statusElement) {
            statusElement.className = 'user-status ' + (status === 1 ? 'online' : 'offline');
        }
    }
}



$(document).ready(function () {
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

function sendMessage() {
    // Get the message input value
    var messageInput = $('#message-input').emojioneArea();
    var messageText = messageInput[0].emojioneArea.getText().trim();

    // Check if the message is not empty
    if (messageText !== '') {
        // Create a new message element
        var newMessage = document.createElement('div');
        newMessage.className = 'message my-message';
        newMessage.textContent = messageText;

        // Append the new message to the chat box
        var chatBox = document.getElementById('chat-box');
        chatBox.appendChild(newMessage);

        // Clear the EmojioneArea text
        messageInput[0].emojioneArea.setText('');

        // Optional: Set focus back on the EmojioneArea
        messageInput[0].emojioneArea.editor.focus();
        // Görgetés az üzenetek aljára
        scrollToBottom();
    }
}

function scrollToBottom() {
    var chatBox = document.getElementById('chat-box');
    chatBox.scrollTop = chatBox.scrollHeight;
}
