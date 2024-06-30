'use strict';
let stompClient = null;
let IdForUserInLinksPage = null;

function connectToWebSocketForLinksPage() {
    var elementToGetLinksUserId = document.querySelector('[id^="userIdForLinksPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInLinksPage = elementToGetLinksUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
        console.log("sima ws-t hasznal");
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
        console.log("wss-t hasznal");
    }
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForLinksPage, onErrorInLinksPage);
}

function onConnectedForLinksPage() {
    stompClient.subscribe(`/user/${IdForUserInLinksPage}/queue/messages`, onMessageReceivedNotificationInLinksPage)
}

function onErrorInLinksPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceivedNotificationInLinksPage(payload) {
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

    connectToWebSocketForLinksPage();

    var elementToGetLinksProfileUserId = document.querySelector('[id^="userIdForLinksPage-"]');
    var profileLinksUserId = elementToGetLinksProfileUserId.id.split("-")[1];

    fetch(`/resources/getLinksProfileNotificationStatus?userId=${profileLinksUserId}`, {
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

document.getElementById("diplomaTheses").addEventListener("click", function () {
    window.location.href = "/resources/diplomaTheses";
});

document.getElementById("examExamples").addEventListener("click", function () {
    window.location.href = "/resources/examExamples";
});

document.getElementById("diplomaThesesDrop").addEventListener("click", function () {
    window.location.href = "/resources/diplomaTheses";
});

document.getElementById("examExamplesDrop").addEventListener("click", function () {
    window.location.href = "/resources/examExamples";
});

function resetInputValue() {
    const input = document.getElementById('filter-input');
    const sugList = document.querySelector('#suggestion-list');
    const topicList = document.querySelector('.topic-checkboxes');
    if (input) {
        sugList.style.display = 'none';
        topicList.style.display = 'none';
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const dropBtn = document.querySelector('.dropbtn');
    if (dropBtn) {
        dropBtn.addEventListener('click', resetInputValue);
        dropBtn.addEventListener('mouseenter', resetInputValue);
    }
});

function toggleFilterDropdown() {
    var dropdownContent = document.getElementById("filter-myDropdown");
    dropdownContent.classList.toggle("active");
}

function closeDropdown(selectedItem) {
    var dropdownContent = document.getElementById("filter-myDropdown");
    dropdownContent.classList.remove("active");
}

// ez az uj amivel bezarja a dropdownot
function closeDropdownTopics(selectedItem) {
    var dropdownContent = document.getElementById("topic-myDropdown");
    dropdownContent.style.display = "none";
}

function closeDropdownModifyTopics(resourceId, selectedItem) {
    var dropdownContentId = "topic-myDropdown-modify-" + resourceId;
    var dropdownContent = document.getElementById(dropdownContentId);

    dropdownContent.style.display = "none";
}

document.addEventListener("DOMContentLoaded", function () {
    const dropdownLinks = document.querySelectorAll(".filter-dropdown-content a");
    const dropdownButton = document.querySelector(".filter-dropbtn");
    const selectedValueInput = document.getElementById("filter-selectedValue");

    dropdownLinks.forEach((link) => {
        link.addEventListener("click", function (event) {
            event.preventDefault();
            selectedValueInput.value = link.textContent;
            dropdownButton.textContent = link.textContent;

            if (selectedValueInput.value !== "Choose a filter") {
                dropdownButton.style.backgroundColor = "rgb(50, 189, 149)";
                dropdownButton.style.color = "white";
            } else {
                dropdownButton.style.backgroundColor = "";
                dropdownButton.style.color = "";
            }
        });
    });

});

function toggleDropdown() {
    const checkboxContainer = document.getElementById("topic-myCheckboxes");

    if (checkboxContainer.style.display === "flex") {
        checkboxContainer.style.display = "none";
    } else {
        checkboxContainer.style.display = "flex";

    }
}

function showFilterContainer() {
    const filterContainer = document.querySelector(".filter-container");
    const openSearchButtons = document.querySelector(".open-search-buttons");

    filterContainer.style.display = "flex";
    openSearchButtons.style.display = "none";
}

function cancelFilterWindow(){
    const filterContainer = document.querySelector(".filter-container");
    const openSearchButtons = document.querySelector(".open-search-buttons");
    filterContainer.style.display = "none";
    openSearchButtons.style.display = "flex";

}

let isWindowAbove1000 = window.innerWidth > 1000;

function adjustLayout() {
    const filterContainer = document.querySelector(".filter-container");
    const openSearchButtons = document.querySelector(".open-search-buttons");

    if (window.innerWidth > 1000 && !isWindowAbove1000) {
        filterContainer.style.display = "flex";
        openSearchButtons.style.display = "none";
        isWindowAbove1000 = true;
    } else if (window.innerWidth <= 1000 && isWindowAbove1000) {
        filterContainer.style.display = "none";
        openSearchButtons.style.display = "flex";
        isWindowAbove1000 = false;
    }
}

adjustLayout();
window.addEventListener("resize", adjustLayout);

// function setupResourceModal() {
//     var modal = document.getElementById("myResourceModal");
//
//     if (modal) {
//         var btn1 = document.getElementById("upload-upload");
//         var span = document.getElementsByClassName("close-resource")[0];
//
//         if (btn1) {
//             btn1.onclick = function() {
//                 modal.style.display = "flex";
//             }
//         }
//
//         if (span) {
//             span.onclick = function() {
//                 modal.style.display = "none";
//             }
//         }
//     }
// }
//
// function closeModalOnClickOutside() {
//     var modal1 = document.getElementById("myResourceModal");
//
//     window.addEventListener("click", function(event) {
//         if (event.target == modal1) {
//             modal1.style.display = "none";
//         }
//     });
// }
//
// setupResourceModal();
// closeModalOnClickOutside();

function setupModifyResourceModal(resourceId) {
    console.log("resourceId: " + resourceId);
    var modalId = "resourceModifyModal-" + resourceId;
    var modal = document.getElementById(modalId);
    console.log("modal: " + modal);
    console.log("modalId: " + modalId);

    if (modal) {
        var btnId = "modifyIcon";
        var btn1 = document.getElementById(btnId);
        console.log("btn1: " + btn1);
        console.log("btnId: " + btnId);
        modal.style.display = "flex";
    }
}

function setupDeleteResourceModal(resourceId) {
    console.log("resourceId: " + resourceId);
    var modalId = "resourceDeleteModal-" + resourceId;
    var modal = document.getElementById(modalId);

    if (modal) {
        var btnId = "deleteIcon";
        var btn1 = document.getElementById(btnId);
        modal.style.display = "flex";
    }
}

function toggleDropdownModal() {
    var dropdown = document.getElementById("topic-myDropdown");
    if (dropdown.style.display === "block") {
        dropdown.style.display = "none";
    } else {
        dropdown.style.display = "block";
    }
}

function closeDropdownModal(option) {
    var dropdown = document.getElementById("topic-myDropdown");
    var button = document.querySelector(".topic-dropbtn-modal");
    dropdown.style.display = "none";
    document.getElementById("topic-selected-modal").value = option;
    button.innerHTML = option;
}

function toggleDropdownModifyModal(resourceId) {
    var dropdownId = "topic-myDropdown-modify-" + resourceId;
    var dropdown = document.getElementById(dropdownId);

    if (dropdown.style.display === "block") {
        dropdown.style.display = "none";
    } else {
        dropdown.style.display = "block";
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const dropdownLinks = document.querySelectorAll(".topic-dropdown-content a");
    const dropdownButton = document.querySelector(".topic-dropbtn-modal");
    const selectedValueInput = document.getElementById("topic-selected-modal");

    dropdownLinks.forEach((link) => {
        link.addEventListener("click", function (event) {
            event.preventDefault();
            selectedValueInput.value = link.textContent;
            dropdownButton.textContent = link.textContent;

            if (selectedValueInput.value !== "Choose a topic") {
                dropdownButton.style.backgroundColor = "rgb(50, 189, 149)";
                dropdownButton.style.color = "white";
            } else {
                dropdownButton.style.backgroundColor = "";
                dropdownButton.style.color = "";
            }
        });
    });

});

document.addEventListener("DOMContentLoaded", function () {
    const dropdownLinks = document.querySelectorAll(".topic-dropdown-content-modify a");

    dropdownLinks.forEach((link) => {
        link.addEventListener("click", function (event) {
            event.preventDefault();
            const resourceId = link.dataset.resourceId;
            const selectedValueInputId = "topic-selected-modal-modify-" + resourceId;
            const dropdownButtonId = "topic-dropbtn-modal-modify-" + resourceId;
            const selectedValueInput = document.getElementById(selectedValueInputId);
            const dropdownButton = document.getElementById(dropdownButtonId);

            selectedValueInput.value = link.textContent;
            dropdownButton.textContent = link.textContent;

            if (resourceId && resourceId !== "Choose a topic") {
                dropdownButton.style.backgroundColor = "rgb(50, 189, 149)";
                dropdownButton.style.color = "white";
            } else {
                dropdownButton.style.backgroundColor = "";
                dropdownButton.style.color = "";
            }
        });
    });
});

function setupSkillsModal() {
    var modal = document.getElementById("resourceModal");

    var btn1 = document.getElementById("upload-upload");
    var btn2 = document.getElementById("upload-hidden");
    var span = document.getElementsByClassName("close-resource")[0];

    if (btn1) {
        btn1.onclick = function() {
            modal.style.display = "flex";
        }
    }

    if (btn2) {
        btn2.onclick = function() {
            modal.style.display = "flex";
        }
    }

    if (span) {
        span.onclick = function() {
            modal.style.display = "none";
        }
    }
}

function closeModalOnClickOutside() {
    var modal1 = document.getElementById("resourceModal");

    window.addEventListener("click", function(event) {
        if (event.target == modal1) {
            modal1.style.display = "none";
        }
    });
}

setupSkillsModal();
closeModalOnClickOutside();

function closeModifyModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.style.display = "none";
    } else {
        console.error("Modal with id '" + id + "' not found.");
    }
}

//NEW
var linkCounter = 0;
function addLinkRow() {
    try {
        var selectedLink = document.getElementById("resourceLink-edit").value;
        var selectedName = document.getElementById("resourceName-edit").value;
        var selectedTopic = document.getElementById("topic-selected-modal").value;

        if (selectedTopic !== "" && selectedLink !== "" && selectedName !== "") {
            var tableContainer = document.querySelector(".table-container table tbody");

            // Create a new row
            var row = document.createElement("tr");

            // Create cells for the row
            var linkNumber = document.createElement("td");
            linkNumber.textContent = linkCounter;

            // Create a clickable link (anchor) for linkName
            var linkName = document.createElement("td");
            var linkNameAnchor = document.createElement("a");
            linkNameAnchor.textContent = selectedName;
            linkNameAnchor.href = selectedLink;
            linkName.appendChild(linkNameAnchor);

            var linkTopic = document.createElement("td");
            linkTopic.textContent = selectedTopic;

            var linkUser = document.createElement("td");
            linkUser.textContent = "User";

            var linkLikes = document.createElement("td");
            linkLikes.textContent = "Likes";

            var linkButtons = document.createElement("td");

            // Create Like and Dislike buttons
            var likeButton = document.createElement("button");
            likeButton.textContent = "Like";
            likeButton.className = "like-button-link";

            var dislikeButton = document.createElement("button");
            dislikeButton.textContent = "Dislike";
            dislikeButton.className = "dislike-button-link";

            // Append Like and Dislike buttons to the linkButtons cell
            linkButtons.appendChild(likeButton);
            linkButtons.appendChild(dislikeButton);

            // Append cells to the row
            row.appendChild(linkNumber);
            row.appendChild(linkName);
            row.appendChild(linkTopic);
            row.appendChild(linkUser);
            row.appendChild(linkLikes);
            row.appendChild(linkButtons);

            // Append the row to the table
            tableContainer.appendChild(row);

            // Increment the linkCounter (assuming linkCounter is declared and initialized)
            linkCounter++;
        }
    } catch (error) {
        console.error('An error occurred:', error);
    }
}

function saveResourceDataToServer() {
    var data = [];
    var link = document.getElementById("resourceLink-edit").value;
    var linkName = document.getElementById("resourceName-edit").value;
    var linkTopic = document.getElementById("topic-selected-modal").value;

    data.push({
        name: linkName,
        link: link,
        topic_name: linkTopic,
    });

    if(link === "" || linkName === "" || linkTopic === "Choose a topic"){
        showErrorMessage("Please fill all the fields!");
    } else {
        sendResourcesDataToServer(data);
    }

}

function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block"; // Megjelenítjük a modal ablakot
}

// ezt is andrisnak
function hideLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "none"; // Elrejtjük a modal ablakot
}

// link feltoltese a szerverre (JSON)
function sendResourcesDataToServer(data) {
    // ezt is andrisnak
    showLoadingModal(); // Megjelenítjük a modal ablakot

    var resourcesUploadDataItems = JSON.stringify(data);

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    fetch('/resources/uploadResources', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token,
        },
        body: 'resourcesUploadDataItems=' + encodeURIComponent(resourcesUploadDataItems)
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text();
            // throw new Error('Hiba történt a válaszban');
        }
    }).then(data => {
        //hideLoadingModal(); // Elrejtjük a modal ablakot
        // Kell kezelni a valaszt es megjeleniteni a hibauzeneteket
        //console.log(data);
        // itt kell majd andrisnak megmondani hogy mit csinaljon
        if (data === "Success") {
            location.reload();
        } else if (data === "NotSafe") {
            showErrorMessage("The link is not safe!");
        } else {
            showErrorMessage("An error occurred." +
                " Please try again later."); // Egyéb hiba esetén
        }

    }).catch(error => {
            // ezt is andrisnak
            hideLoadingModal(); // Elrejtjük a modal ablakot
            console.error('Hiba történt:', error);
    });
}

// Funkció a piros szöveg megjelenítésére
function showErrorMessage(message) {
    var errorMessageElement = document.getElementById('error-message-modal-content');
    errorMessageElement.innerText = message;
    // További stílusok vagy műveletek hozzáadhatók a látványosság érdekében
}

function sendDataToServer(data) {
    var resourceDataItems = JSON.stringify(data);
    document.getElementById("resourceDataItems").value = resourceDataItems;
    //console.log(resourceDataItems);


    // Most küldd el az űrlapot
    document.getElementById("resource-form").submit();
}

function validateLink() {
    var linkInput = document.getElementById("resourceLink-edit");
    var linkValue = linkInput.value.trim();
    var urlRegex = /^(https?:\/\/)?([a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}([:][0-9]+)?)(\/[^\s]*)?$/;
    var uploadButton = document.getElementById("upload-button-modal");

    if (!urlRegex.test(linkValue)) {
        linkInput.classList.add("highlight");
        uploadButton.disabled = true;
    } else {
        linkInput.classList.remove("highlight");
        uploadButton.disabled = false;
        uploadButton.opacity = 1;
    }
}

function validateName() {
    var nameInput = document.getElementById("resourceName-edit");
    var nameValue = nameInput.value.trim();
    var uploadButton = document.getElementById("upload-button-modal");
    if (!/.{3,}/.test(nameValue)) {
        nameInput.classList.add("highlight");
        uploadButton.disabled = true;
        uploadButton.style.opacity = 0.5;
        uploadButton.style.cursor = "not-allowed";
    } else {
        nameInput.classList.remove("highlight");
        uploadButton.disabled = false;
        uploadButton.style.opacity = 1;
    }
}

function validateModifiedName(resourceId) {
    var nameInput = document.getElementById("resourceName-edit-modify-" + resourceId);
    var nameValue = nameInput.value.trim();
    var modifyButton = document.getElementById("modify-button-modal-" + resourceId);
    if (!/.{3,}/.test(nameValue)) {
        nameInput.classList.add("highlight");
        modifyButton.disabled = true;
        modifyButton.style.opacity = 0.5;
        modifyButton.style.cursor = "not-allowed";
    } else {
        nameInput.classList.remove("highlight");
        modifyButton.disabled = false;
        modifyButton.style.opacity = 1;
        modifyButton.style.cursor = "pointer";
    }
}

// Like Dislike Buttons
function sendLikeOrDislike(resourceId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    const sseUrl = "/sse/sendLikeOrDislike"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

    const url = `/resources/${action}?resourceId=${resourceId}`;
    //console.log("URL: " + url);
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        },
    })
        .then(response => {
            if (response.ok) {
                // Sikeres kérés esetén elküldjük egy SSE üzenetet a like/dislike értékről
                // Az üzenetet most a SSE URL-re küldjük, ami a szerver oldalon kezeli majd
                fetch(sseUrl, {
                    method: 'POST',
                    body: JSON.stringify({message: `${action}:${resourceId}`}), // Konvertáljuk JSON formátumra
                    headers: {
                        'Content-Type': 'application/json', // Módosítottuk a Content-Type-t
                        'X-CSRF-TOKEN': token
                    },
                })
                response.text().then(data => {
                    // Kezeld itt a szöveget (data)
                    //console.log('sendLikeOrDislike Response:', data);
                    //console.log(data);
                    // Például: frissítheted a DOM-ot adataink alapján
                }).catch(error => {
                    console.error('Error:', error);
                });
            } else {
                throw new Error('Request failed');
            }
        })
        .then(data => {
            //console.log('Response:', data);
            // A válaszban érkező adatokat kezelheted itt (opcionális)
            // Például: frissítheted a DOM-ot a legfrissebb like/dislike értékekkel
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(resourceId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    const sseUrl = "/sse/sendLikeOrDislike"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

    const url = `/resources/${action}?resourceId=${resourceId}`;
    //console.log("URL: " + url);
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        },
    })
        .then(response => {
            if (response.ok) {
                // Sikeres kérés esetén elküldjük egy SSE üzenetet a like/dislike értékről
                // Az üzenetet most a SSE URL-re küldjük, ami a szerver oldalon kezeli majd
                fetch(sseUrl, {
                    method: 'POST',
                    body: JSON.stringify({message: `${action}:${resourceId}`}), // Konvertáljuk JSON formátumra
                    headers: {
                        'Content-Type': 'application/json', // Módosítottuk a Content-Type-t
                        'X-CSRF-TOKEN': token
                    },
                })
                response.text().then(data => {
                    // Kezeld itt a szöveget (data)
                    //console.log('sendLikeOrDislike Response:', data);
                    //console.log(data);
                    // Például: frissítheted a DOM-ot adataink alapján
                }).catch(error => {
                    console.error('Error:', error);
                });
            } else {
                throw new Error('Request failed');
            }
        })
        .then(data => {
            //console.log('Response:', data);
            // A válaszban érkező adatokat kezelheted itt (opcionális)
            // Például: frissítheted a DOM-ot a legfrissebb like/dislike értékekkel
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
function setLikeOrDislikeStatusToActiveOrInactive(resourceId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    //const sseUrl = "/sse/sendLikeOrDislike"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

    const url = `/resources/${action}?resourceId=${resourceId}`;
    //console.log("URL: " + url);
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        },
    }).then(response => {
        if (response.ok) {
            //console.log(response.text());
            return response.text();
        } else {
            throw new Error('Request failed');
        }
    }).then(data => {
        //console.log(data);
    }).catch(error => {
        console.error('Error:', error);
    });
}

function handleLikeButtonClick(likeButton) {
    // Az adott sor azonosítójának megszerzése
    const rowId = likeButton.closest('tr').id;
    const resourceId = rowId.replace('resource-row-', '');
    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
    if (likeButton.classList.contains('like-button-link-active')) {
        // Like visszavonása
        sendLikeOrDislike(resourceId, 'revokelike');

        likeButton.classList.remove('like-button-link-active');
        // Távolítsuk el az aktív like gomb állapotát a helyi tárolóból is
        setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToInactive');
        //localStorage.removeItem(`likeButtonState_${UserId}_${resourceId}`);
    } else {
        const activeDislikeButton = document.querySelector(`#${rowId} .dislike-button-link.dislike-button-link-active`);
        if (activeDislikeButton) {
            activeDislikeButton.classList.remove('dislike-button-link-active');
            //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToInactive');
            //localStorage.removeItem(`dislikeButtonState_${UserId}_${resourceId}`);
            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(resourceId, 'likeResourceAndRevokeDislike');
            // Módosítsuk az osztályt a "like-button-link-active"-ra
            likeButton.classList.add('like-button-link-active');


            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActiveAndDislikeToInactive');
            // Mentsük el az aktív like gomb állapotát a helyi tárolóban
            //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
            //localStorage.setItem(`likeButtonState_${UserId}_${resourceId}`, 'active');
        } else {
            // Like küldése a szervernek
            sendLikeOrDislike(resourceId, 'like');

            // Módosítsuk az osztályt a "like-button-link-active"-ra
            likeButton.classList.add('like-button-link-active');

            // Mentsük el az aktív like gomb állapotát a helyi tárolóban
            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
            //localStorage.setItem(`likeButtonState_${UserId}_${resourceId}`, 'active');
        }
    }
}

function handleDislikeButtonClick(dislikeButton) {
    // Az adott sor azonosítójának megszerzése
    const rowId = dislikeButton.closest('tr').id;
    const resourceId = rowId.replace('resource-row-', '');

    // Ellenőrizze, hogy az adott sorban már van aktív like gomb
    // const activeLikeButton = document.querySelector(`#${rowId} .like-button-link.like-button-link-active`);
    // if (activeLikeButton) {
    //     sendRevokeLikeOrDislike(resourceId, 'revokelike');
    //     activeLikeButton.classList.remove('like-button-link-active');
    //     localStorage.removeItem(`likeButtonState_${resourceId}`);
    // }
    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActive');
    if (dislikeButton.classList.contains('dislike-button-link-active')) {
        // Dislike visszavonása
        sendLikeOrDislike(resourceId, 'revokedislike');

        dislikeButton.classList.remove('dislike-button-link-active');
        // Távolítsuk el az aktív dislike gomb állapotát a helyi tárolóból is
        setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToInactive');
        //localStorage.removeItem(`dislikeButtonState_${UserId}_${resourceId}`);
    } else {
        const activeLikeButton = document.querySelector(`#${rowId} .like-button-link.like-button-link-active`);
        if (activeLikeButton) {
            activeLikeButton.classList.remove('like-button-link-active');

            // itt a kozosbe kicserelni ezt a meghivast
            //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToInactive');
            //localStorage.removeItem(`likeButtonState_${UserId}_${resourceId}`);
            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(resourceId, 'dislikeResourceAndRevokeLike');
            // Módosítsuk az osztályt a "dislike-button-link-active"-ra
            dislikeButton.classList.add('dislike-button-link-active');

            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActiveAndLikeToInactive');

            // Mentsük el az aktív dislike gomb állapotát a helyi tárolóban
            //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActive');
            //localStorage.setItem(`dislikeButtonState_${UserId}_${resourceId}`, 'active');
        } else {
            // Dislike küldése a szervernek
            sendLikeOrDislike(resourceId, 'dislike');

            // Módosítsuk az osztályt a "dislike-button-link-active"-ra
            dislikeButton.classList.add('dislike-button-link-active');

            // Mentsük el az aktív dislike gomb állapotát a helyi tárolóban
            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActive');
            //localStorage.setItem(`dislikeButtonState_${UserId}_${resourceId}`, 'active');
            }}}

$(document).ready(async function () {
    $(document).trigger('myCustomLoadEvent');

    // SSE
    var urlEndpoint = "/sse/subscribe";
    //itt az eventsource a szerver oldalon lévő végpontot figyeli
    const eventSource = new EventSource(urlEndpoint);

    eventSource.onopen = function (event) {
        console.log('SSE connection opened.');
    };

    eventSource.onerror = function (event) {
        console.error('SSE error:', event);
    };

    eventSource.addEventListener('LikeOrDislikeLink', function (event) {
        const data = JSON.parse(event.data);
        const rowId = data.rowId;
        const likeCountElement = document.querySelector(`#resource-row-${rowId} #likeButton`);
        const dislikeCountElement = document.querySelector(`#resource-row-${rowId} #dislikeButton`);
        likeCountElement.textContent = data.like;
        dislikeCountElement.textContent = data.dislike;
    });

// A like gomb eseménykezelője
    document.querySelectorAll('.like-button-link').forEach(likeButton => {
        likeButton.addEventListener('click', () => {
            handleLikeButtonClick(likeButton);
        });
    });

// A dislike gomb eseménykezelője
    document.querySelectorAll('.dislike-button-link').forEach(dislikeButton => {
        dislikeButton.addEventListener('click', () => {
            handleDislikeButtonClick(dislikeButton);
        });
    });


})

// NEGYEDIK VERZIO (vegleges)
// lekerni az osszes like es dislike allasat az adatbazisbol
let likeAndDislikeStatuses = [];
document.addEventListener('DOMContentLoaded', function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    //const url = `/resources/${action}?resourceId=${resourceId}`
    fetch("/resources/getLikeAndDislikeStatuses", {
        method: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed');
            }
        })
        .then(data => {
            likeAndDislikeStatuses = data.likeanddislike;
            handleLikeAndDislikeStatuses();
            //console.log(likeAndDislikeStatuses);
        })
        .catch(error => {
            console.error('Error:', error);
        })
});

function handleLikeAndDislikeStatuses() {
    // Itt már rendelkezésre állnak az adatok
    //console.log(likeAndDislikeStatuses);

    // Most már kezelheted az adatokat
    for (let i = 0; i < likeAndDislikeStatuses.length; i++) {
        const likeAndDislikeData = likeAndDislikeStatuses[i];
        const resourceId = likeAndDislikeData.resourceId;
        const like = likeAndDislikeData.like;
        const dislike = likeAndDislikeData.dislike;

        // Itt kezeld az adatokat vagy végezz velük bármit, amit szeretnél
        //console.log(`Resource ID: ${resourceId}, Like: ${like}, Dislike: ${dislike}`);
        const likeCountElement = document.querySelector(`#resource-row-${resourceId} .like-button-link`);
        const dislikeCountElement = document.querySelector(`#resource-row-${resourceId} .dislike-button-link`);
        //console.log(likeCountElement);
        //console.log(dislikeCountElement);
        if (like === 1) {
            likeCountElement.classList.add('like-button-link-active');
        } else if (dislike === 1) {
            dislikeCountElement.classList.add('dislike-button-link-active');
        }

    }
}

// // HARMADIK VERZIO
// $(document).on('myCustomLoadEvent', function () {
//     console.log('Saját oldalbetöltési esemény kiváltva.');
//     function getLikeAndDislikeStatus(resourceId, action) {
//         return new Promise((resolve, reject) => {
//             var token = $("meta[name='_csrf']").attr("content");
//             var header = $("meta[name='_csrf_header']").attr("content");
//             const url = `/resources/${action}?resourceId=${resourceId}`;
//             fetch(url, {
//                 method: 'GET',
//                 headers: {
//                     'Content-Type': 'application/x-www-form-urlencoded',
//                     'X-CSRF-TOKEN': token
//                     //'Cache-Control': 'no-cache'
//                 }
//             }).then(response => {
//                 if (response.ok) {
//                     return response.text();
//                 } else {
//                     throw new Error('Request failed');
//                 }
//             }).then(data => {
//                 console.log("Data: ",data)
//                 resolve(data);
//             }).catch(error => {
//                 console.error('Error:', error);
//                 reject(error);
//             });
//         });
//     }
//
//
//         const likeButtons = document.querySelectorAll('.like-button-link');
//         for (const likeButton of likeButtons) {
//             const rowId = likeButton.closest('tr').id;
//             const resourceId = rowId.replace('resource-row-', '');
//             getLikeAndDislikeStatus(resourceId, 'getLikeStatus').then(likeStatusData => {
//                 console.log('likeStatusData: ', likeStatusData);
//                 if (likeStatusData === '1') {
//                     likeButton.classList.add('like-button-link-active');
//                 }
//             });
//         }
//
//         const dislikeButtons = document.querySelectorAll('.dislike-button-link');
//         for (const dislikeButton of dislikeButtons) {
//             const rowId = dislikeButton.closest('tr').id;
//             const resourceId = rowId.replace('resource-row-', '');
//             getLikeAndDislikeStatus(resourceId, 'getDislikeStatus').then(dislikeStatusData => {
//                 console.log('dislikeStatusData: ', dislikeStatusData);
//                 if (dislikeStatusData === '1') {
//                     dislikeButton.classList.add('dislike-button-link-active');
//                 }
//             });
//         }
// });



// SEARCH:
document.addEventListener("DOMContentLoaded", function () {
    const table = document.querySelector(".link-table");
    const tableBody = table.querySelector("tbody");
    const rows = Array.from(tableBody.querySelectorAll("tr"));
    const rowsPerPage = 20;
    let currentPage = 1;

    function updatePageCounter() {
        const pageCount = Math.ceil(rows.length / rowsPerPage);
        document.getElementById("page-counter").textContent = `Page ${currentPage} of ${pageCount}`;
    }

    function showRowsForCurrentPage() {
        const startIdx = (currentPage - 1) * rowsPerPage;
        const endIdx = Math.min(startIdx + rowsPerPage, rows.length);

        rows.forEach((row, index) => {
            if (index >= startIdx && index < endIdx) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    }

    function setPage(page) {
        currentPage = page;
        showRowsForCurrentPage();
        updatePageCounter();
    }

    updatePageCounter();
    showRowsForCurrentPage();

    document.getElementById("next-page-button").addEventListener("click", () => {
        if (currentPage < Math.ceil(rows.length / rowsPerPage)) {
            setPage(currentPage + 1);
        }
        else {
            setPage(1);
        }
    });

    document.getElementById("prev-page-button").addEventListener("click", () => {
        if (currentPage > 1) {
            setPage(currentPage - 1);
        }
        else {
            setPage(Math.ceil(rows.length / rowsPerPage));
        }
    });

});

document.addEventListener("DOMContentLoaded", function () {
    const filterInput = document.getElementById("filter-input");
    const tableRows = document.querySelectorAll("#link-table-body tr");
    const filterSelectedValue = document.getElementById("filter-selectedValue");
    const topicCheckboxes = document.querySelectorAll("#topic-myCheckboxes input[type='checkbox']");

    filterInput.addEventListener("input", function () {
        filterRows();
    });

    filterSelectedValue.addEventListener("click", function () {
        filterRows();
    });

    topicCheckboxes.forEach(function (checkbox) {
        checkbox.addEventListener("change", function () {
            filterRows();
        });
    });

    function filterRows() {
        const searchText = filterInput.value.toLowerCase();
        const selectedFilter = filterSelectedValue.value;
        const selectedTopics = getSelectedTopics();

        tableRows.forEach(function (row) {
            const linkText = row.querySelector(".table-link a").innerText.toLowerCase();
            const topicText = row.querySelector(".table-topic").innerText.toLowerCase();
            const likeCount = parseInt(row.querySelector(".like-count").innerText);

            const topicSelected = selectedTopics.includes(topicText);
            const textMatches = linkText.includes(searchText);

            if (
                (selectedFilter === "by Name" && textMatches && topicSelected) ||
                (selectedFilter === "by Likes" && topicSelected)
            ) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    }

    function getSelectedTopics() {
        const selectedTopics = [];
        topicCheckboxes.forEach(function (checkbox) {
            if (checkbox.checked) {
                const topicText = checkbox.parentElement.querySelector("span").innerText.toLowerCase();
                selectedTopics.push(topicText);
            }
        });
        return selectedTopics;
    }
});


let originalRows = []; // Változó az eredeti sorok tárolásához

function saveOriginalRows() {
    const table = document.getElementById('dataTable');
    originalRows = Array.from(table.getElementsByTagName('tr'));
}

function restoreOriginalTable() {
    const tableBody = document.getElementById('dataTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';

    originalRows.forEach(row => {
        const clonedRow = row.cloneNode(true);
        tableBody.appendChild(clonedRow);

        const likeButton = clonedRow.querySelector('.like-button-link');
        if (likeButton) {
            likeButton.addEventListener('click', () => {
                handleLikeButtonClick(likeButton);
            });
        }

        const dislikeButton = clonedRow.querySelector('.dislike-button-link');
        if (dislikeButton) {
            dislikeButton.addEventListener('click', () => {
                handleDislikeButtonClick(dislikeButton);
            });
        }
    });
}

function searchTable() {
    if (originalRows.length === 0) {
        saveOriginalRows();
    } else {
        restoreOriginalTable();
    }

    const input = document.getElementById('filter-input');
    const filter = input.value.toUpperCase();
    const selectedValues = [];
    const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');

    checkboxes.forEach((checkbox) => {
        selectedValues.push(checkbox.value);
    });

    const table = document.getElementById('dataTable');
    const rows = table.getElementsByTagName('tr');
    const filteredRows = [];

    for (let i = 1; i < rows.length; i++) {
        const cells = rows[i].getElementsByTagName('td');
        let nameFound = false;
        let topicFound = false;

        const cell = cells[1];

        if (cell) {
            const cellText = cell.textContent || cell.innerText;
            if (cellText.toUpperCase().indexOf(filter) > -1) {
                nameFound = true;
            }
        }

        const topicCell = cells[2];

        if (topicCell) {
            const cellContent = topicCell.textContent || topicCell.innerText;
            if (selectedValues.includes(cellContent.trim())) {
                topicFound = true;
            }
        }

        if (nameFound && topicFound) {
            filteredRows.push(rows[i]);
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
    }

    const rowsPerPage = 20;
    let currentPage = 1;

        function updatePageCounter() {
            const pageCount = Math.ceil(filteredRows.length / rowsPerPage);
            document.getElementById("page-counter").textContent = `Page ${currentPage} of ${pageCount}`;
        }

        function showRowsForCurrentPage() {
            const startIdx = (currentPage - 1) * rowsPerPage;
            const endIdx = Math.min(startIdx + rowsPerPage, rows.length);

            filteredRows.forEach((row, index) => {
                if (index >= startIdx && index < endIdx) {
                    row.style.display = "";
                } else {
                    row.style.display = "none";
                }
            });
        }

        function setPage(page) {
            currentPage = page;
            showRowsForCurrentPage();
            updatePageCounter();
        }

        updatePageCounter();
        showRowsForCurrentPage();


        document.getElementById("next-page-button").addEventListener("click", () => {
            if (currentPage < Math.ceil(filteredRows.length / rowsPerPage)) {
                setPage(currentPage + 1);
            }
            else{
                setPage(1);
            }
        });

        document.getElementById("prev-page-button").addEventListener("click", () => {
            if (currentPage > 1) {
                setPage(currentPage - 1);
            }
            else    {
                setPage(Math.ceil(filteredRows.length / rowsPerPage));
            }
        });
}

document.getElementById('search-button').addEventListener('click', () => {
    //document.getElementById('search-button').click();
    searchInResourcesTable();
    //searchTable();
});

function searchInResourcesTable() {
    // ide a loading modalt
    showLoadingModal();

    const searchinput = document.getElementById('filter-input');
    const filter = searchinput.value.toLowerCase();
    const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');
    const selectedValues = Array.from(checkboxes).map(checkbox => checkbox.value.trim().toUpperCase());
    // console.log("Keresési szöveg: " + filter);
    // console.log("Mik vonnak bepipalva: " + selectedValues);
    const queryString = 'filter=' + encodeURIComponent(filter) + '&selectedTopics=' + encodeURIComponent(selectedValues.join(','));
    fetch('/resources/filtered?' + queryString, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Request failed');
        }
    }).then(data => {
        //console.log(data);
        window.location.href = '/resources/filtered?' + queryString;

    }).catch(error => {
        console.error('Error:', error);
        hideLoadingModal();
    });
}

function listSuggestions() {
    const input = document.getElementById('filter-input');
    const filter = input.value.trim().toUpperCase();
    const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');
    const selectedValues = Array.from(checkboxes).map(checkbox => checkbox.value.trim().toUpperCase());
    const suggestionList = document.getElementById('suggestion-list');
    suggestionList.innerHTML = '';

    let suggestionCount = 0;

    const table = document.getElementById('dataTable');
    const rows = table.getElementsByTagName('tr');

    if (filter === '') {
        const suggestionList = document.getElementById('suggestion-list');
        suggestionList.style.display = 'none';
        return;
    }

    for (let i = 1; i < rows.length && suggestionCount < 5; i++) {
        const cells = rows[i].getElementsByTagName('td');
        const nameCell = cells[1];
        const topicCell = cells[2];

        if (nameCell && topicCell) {
            const nameText = nameCell.textContent.trim().toUpperCase();
            const topicText = topicCell.textContent.trim().toUpperCase();

            if (nameText.includes(filter) && selectedValues.includes(topicText)) {

                const span = nameCell.querySelector('a');
                if (span) {
                    const suggestionItem = document.createElement('div');
                    suggestionItem.textContent = span.textContent;

                    suggestionItem.addEventListener('click', function() {
                        input.value = span.textContent;
                        suggestionList.style.display = 'none';
                    });

                    suggestionList.appendChild(suggestionItem);
                    suggestionCount++;
                }
            }
        }
    }

    if (suggestionCount === 0) {
        suggestionList.style.display = 'none';
        return;
    }
    suggestionList.style.display = 'block';
}

document.getElementById('filter-input').addEventListener('input', listSuggestions);


// MODIFY
function saveModifiedLinkDataToServer(resourceId) {
    var data = [];

    const linkNameId = "resourceName-edit-modify-" + resourceId;
    const linkName = document.getElementById(linkNameId).value;
    const linkTopicId = "topic-dropbtn-modal-modify-" + resourceId;
    const linkTopicElement = document.getElementById(linkTopicId);
    const linkTopicValue = linkTopicElement.innerText;

    data.push({
        name: linkName,
        topic: linkTopicValue,
        link_id: resourceId
    });

    if ( linkName === "" || linkTopicValue === "") {
        showErrorMessageInExam("Please fill out all fields!");
    } else {
        sendModifiedResourceDataToServer(data);
    }
}

function sendModifiedResourceDataToServer(data) {
    showLoadingModal()
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var formData = new FormData();
    formData.append("name", data[0].name);
    formData.append("topic", data[0].topic);
    formData.append("link_id", data[0].link_id);

    fetch('/resources/modifyResources', {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': token
        },
        body: formData
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text();
        }
    }).then(data => {
        if (data === "Success") {
            location.reload();
        }
    }).catch(error => {
        hideLoadingModal()
        console.error('An error occurred:', error);
    });
}


//DELETE
function deleteResourcesData(linkId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    showLoadingModal()
    fetch(`/resources/deleteResources?link_id=${linkId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text();
        }
    }).then(data => {
        if (data === "Success") {
            location.reload();
        }
    }).catch(error => {
        console.error('An error occurred:', error);
        hideLoadingModal()
    });
}

document.addEventListener('click', function(event) {
    var suggestionList = document.getElementById('suggestion-list');
    if (event.target !== suggestionList) {
        suggestionList.style.display = 'none';
    }
});