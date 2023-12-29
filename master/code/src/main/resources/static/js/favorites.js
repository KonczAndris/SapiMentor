document.getElementById("searchMyGroup").addEventListener("click", function () {
    window.location.href = "/myGroup";
});

$(document).ready(function() {
    // Emojik beszúrása az input mezőbe
    $('#message-input').emojioneArea({
        pickerPosition: 'top',
        tonesStyle: 'bullet',
        events: {
            // Amikor egy emoji kerül kiválasztásra, illeszd be az input mezőbe
            emojibtn_click: function(button, event) {
                $('#message-input').emojioneArea('insert', button.html());
            }
        }
    });

    // Egyéb műveletek, pl. üzenet elküldése gombra kattintáskor
    $('#send-button').click(function() {
        let message = $('#message-input').val();
        // Ide jöhet az üzenet küldése a megfelelő feldolgozással
        console.log("Elküldött üzenet: ", message);
        // Töröld az üzenetet az input mezőből
        $('#message-input').val('');
    });

    // Emoji gomb funkciója
    $('#emoji-button').click(function() {
        $('#message-input').emojioneArea('toggle');
    });
});