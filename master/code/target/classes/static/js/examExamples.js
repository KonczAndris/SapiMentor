'use strict';

let stompClient = null;
let IdForUserInExmasPage = null;
function connectToWebSocketForExmasPage() {
    var elementToGetExmasUserId = document.querySelector('[id^="userIdForExamsPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInExmasPage = elementToGetExmasUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForExmasPage, onErrorInExmasPage);
}

function onConnectedForExmasPage() {
    stompClient.subscribe(`/user/${IdForUserInExmasPage}/queue/messages`, onMessageReceivedNotificationInExmasPage)
}

function onErrorInExmasPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceivedNotificationInExmasPage(payload) {
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

    connectToWebSocketForExmasPage();

    var elementToGetExmasProfileUserId = document.querySelector('[id^="userIdForExamsPage-"]');
    var exmasprofileUserId = elementToGetExmasProfileUserId.id.split("-")[1];

    fetch(`/resources/examExamples/getExmasProfileNotificationStatus?userId=${exmasprofileUserId}`, {
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

document.getElementById("resources").addEventListener("click", function () {
    window.location.href = "/resources";
});

document.getElementById("diplomaThesesDrop").addEventListener("click", function () {
    window.location.href = "/resources/diplomaTheses";
});

document.getElementById("resourcesDrop").addEventListener("click", function () {
    window.location.href = "/resources";
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

function cancelFilterWindow(){
    const filterContainer = document.querySelector(".filter-container");
    const openSearchButtons = document.querySelector(".open-search-buttons");
    filterContainer.style.display = "none";
    openSearchButtons.style.display = "flex";
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


function setupExamExamplesModal() {
    var modal = document.getElementById("examExamplesModal");
    if (modal) {
        var btn1 = document.getElementById("upload-upload");
        var span = document.getElementsByClassName("close-examExamples")[0];

        if (btn1) {
            btn1.onclick = function() {
                modal.style.display = "flex";
            }
        }

        if (span) {
            span.onclick = function() {
                modal.style.display = "none";
            }
        }

    }
}

setupExamExamplesModal();


function closeModalOnClickOutside() {
    var modal1 = document.getElementById("examExamplesModal");

    window.addEventListener("click", function(event) {
        if (event.target == modal1) {
            modal1.style.display = "none";
        }
    });
}

closeModalOnClickOutside();

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

function setupSkillsModal() {
    var modal = document.getElementById("examExamplesModal");

    var btn1 = document.getElementById("upload-upload");
    var btn2 = document.getElementById("upload-hidden");
    var span = document.getElementsByClassName("close-examExamples")[0];

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

function closeModal() {
    var modal = document.getElementById('examExamplesModal');
    modal.style.display = 'none';
}

// Add click event listener to the "Cancel" button
var cancelButton = document.querySelector('.cancel-button-modal.close-examExamples');
cancelButton.addEventListener('click', closeModal);

function closeModalOnClickOutside() {
    var modal1 = document.getElementById("examExamplesModal");

    window.addEventListener("click", function(event) {
        if (event.target == modal1) {
            modal1.style.display = "none";
        }
    });
}

setupSkillsModal();
closeModalOnClickOutside();

// Function to handle file selection and update the input field
document.getElementById("fileUpload").addEventListener("change", function() {
    const fileInput = document.getElementById("fileUpload");
    const fileNameInput = document.getElementById("examExampleFileName-edit");

    if (fileInput.files.length > 0) {
        // Get the selected file
        const selectedFile = fileInput.files[0];

        // Update the input field with the file name
        fileNameInput.value = selectedFile.name;
    }
});

// Function to trigger the file input when the upload button is clicked
document.getElementById("fileUploadButton").addEventListener("click", function() {
    const fileInput = document.getElementById("fileUpload");
    fileInput.click();
});

var linkCounter = 0;
function addLinkRow() {
    try {
        // var selectedFile = ...
        var selectedFileName = document.getElementById("examExampleFileName-edit").value;
        var selectedName = document.getElementById("examExampleName-edit").value;
        var selectedTopic = document.getElementById("topic-selected-modal").value;

        if (selectedTopic !== "" && selectedFileName !== "" && selectedName !== "") {
            var tableContainer = document.querySelector(".table-container table tbody");

            // Create a new row
            var row = document.createElement("tr");

            // Create cells for the row
            var linkNumber = document.createElement("td");
            linkNumber.textContent = linkCounter;

            // Create a clickable link (anchor) for linkName
            var linkName = document.createElement("td");
            linkName = selectedName;

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

            var downloadButton = document.createElement("button");
            downloadButton.textContent = "Download";
            downloadButton.className = "download-button";

            // Append Like and Dislike buttons to the linkButtons cell
            linkButtons.appendChild(likeButton);
            linkButtons.appendChild(dislikeButton);
            linkButtons.appendChild(downloadButton);

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

function saveExamExamplesDataToServer() {
    //var tableRows = document.querySelectorAll(".table-container table tbody tr");
    var data = [];

    // tableRows.forEach(function (row) {
    //     var linkNumber = row.querySelector("td:first-child").textContent;
    //     var linkName = row.querySelector("td:nth-child(2)").textContent;
    //     var linkTopic = row.querySelector("td:nth-child(3)").textContent;
    //
    //     // Assuming you have an array for storing likes and dislikes for each row
    //     var linkLikes = row.querySelector("td:nth-child(4)").textContent;
    //
    //     // Push the data into the 'data' array
    //     data.push({
    //         linkNumber: linkNumber,
    //         linkName: linkName,
    //         linkTopic: linkTopic,
    //         linkLikes: linkLikes,
    //     });
    // });
    var image = document.getElementById("fileUpload").files[0];
    var imageFileName = document.getElementById("examExampleName-edit").value;
    var imageTopic = document.getElementById("topic-selected-modal").value;

    data.push({
       name: imageFileName,
       image: image,
       topic: imageTopic,

    });

    if (image === undefined || imageFileName === "" || imageTopic === "Choose a topic") {
        showErrorMessageInExam("Please fill out all fields!");
    } else {
        sendExamsDataToServer(data);
    }
    //console.log(data);
    // sendExamsDataToServer(data);
}
// ezt is andrisnak
function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block"; // Megjelenítjük a modal ablakot
}

// ezt is andrisnak
function hideLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "none"; // Elrejtjük a modal ablakot
}
function sendExamsDataToServer(data) {
    // ide kell majd hogy behozza a toltokepernyot
    showLoadingModal()
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var formData = new FormData();
    formData.append("image", data[0].image);
    formData.append("name", data[0].name);
    formData.append("topic", data[0].topic);
    //console.log(formData);
    // for (var pair of formData.entries()) {
    //     console.log(pair[0] + ': ' + pair[1]);
    // }

    fetch('/resources/examExamples/uploadExams', {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': token,
        },
        body: formData
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text();
            // throw new Error('Hiba történt a válaszban');
        }
    }).then(data => {
        // ezt is andrisnak
        //hideLoadingModal(); // Elrejtjük a modal ablakot
        // Kell kezelni a valaszt es megjeleniteni a hibauzeneteket
        hideLoadingModal()
        if (data === "Success") {
            location.reload();
        } else if(data === "Too large"){
            //alert("The file is too large!");
            showErrorMessageInExam("The file is too large!\n" +
                "The maximum file size is 2MB!");
        } else if(data === "Wrong type"){
            //alert("This type png is not supported!");
            showErrorMessageInExam("This type of png is not supported!\n"); // Egyéb hiba esetén
        }
    }).catch(error => {
        hideLoadingModal()
        // ezt is andrisnak
        //hideLoadingModal(); // Elrejtjük a modal ablakot
        console.error('Hiba történt:', error);
    });
}

function showErrorMessageInExam(message) {
    var errorMessageElement = document.getElementById('error-message-exam-modal-content');
    errorMessageElement.innerText = message;
    // További stílusok vagy műveletek hozzáadhatók a látványosság érdekében
}

//NEW
// Get the elements
var examImageContainer = document.querySelector(".exam-image-image-container");
var examImageModal = document.querySelector(".exam-image-modal");
var examImageCloseButton = document.querySelector(".exam-image-close-button");

// Show the modal on hover
// examImageContainer.addEventListener("click", function() {
//     examImageModal.style.display = "block";
//     examImageModal.style.cursor = "default";
// });

// Hide the modal when clicking outside of it

// Eltároljuk az összes sor és modális ablak elemet
const examImageContainers = document.querySelectorAll('.exam-image-image-container');
const examImageModals = document.querySelectorAll('.exam-image-modal');
// Az egyes sorokhoz rendelt modális ablakok kezelése
for (let i = 0; i < examImageContainers.length; i++) {
    const examImageContainer = examImageContainers[i];
    const examImageModal = examImageModals[i];

    examImageContainer.addEventListener("click", function() {
        examImageModal.style.display = "block";
        examImageModal.style.cursor = "default";
    });

    // Az egyes modális ablakok elrejtése, ha a modális ablakon kívülre kattintunk
    window.addEventListener("click", function(event) {
        if (event.target == examImageModal) {
            examImageModal.style.display = "none";
        }
    });
}

// function clearBrowserCache() {
//     if ('caches' in window) {
//         caches.keys().then(function(names) {
//             names.forEach(function(name) {
//                 caches.delete(name);
//             });
//         });
//     }
// }
// window.addEventListener("beforeunload", function(event) {
//     // Itt töröld a böngésző gyorsítárát, amikor a felhasználó elhagyja az oldalt
//     clearBrowserCache();
// });

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

    eventSource.addEventListener('LikeOrDislike', function (event) {
        const data = JSON.parse(event.data);
        const rowId = data.rowId;
        const likeCountElement = document.querySelector(`#exam-row-${rowId} #likeButton`);
        const dislikeCountElement = document.querySelector(`#exam-row-${rowId} #dislikeButton`);

        //console.log('Received SSE message:', data.rowId);
        // Itt frissitsd a like/dislike ertekeket a DOM-ban
        //const likeDislikeCountElement = document.querySelector(`#resource-row-${rowId} .like-dislike-count`);
        //console.log(likeDislikeCountElement);
        likeCountElement.textContent = data.like;
        dislikeCountElement.textContent = data.dislike;
    });


    function sendLikeOrDislike(examId, action) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        const sseUrl = "/sse/sendLikeOrDislikeForExam"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

        const url = `/resources/examExamples/${action}?examId=${examId}`;
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
                        body: JSON.stringify({message: `${action}:${examId}`}), // Konvertáljuk JSON formátumra
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


    function sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(examId, action) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        const sseUrl = "/sse/sendLikeOrDislikeForExam"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

        const url = `/resources/examExamples/${action}?examId=${examId}`;
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
                        body: JSON.stringify({message: `${action}:${examId}`}), // Konvertáljuk JSON formátumra
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


    // ide azt hogy allitsuk at a like gomb erteket 0-ra vagyis inaktivra
    function setLikeOrDislikeStatusToActiveOrInactive(examId, action) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        //const sseUrl = "/sse/sendLikeOrDislike"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

        const url = `/resources/examExamples/${action}?examId=${examId}`;
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


// A like gomb eseménykezelője
    document.querySelectorAll('.like-button-link').forEach(likeButton => {
        likeButton.addEventListener('click', () => {
            // Az adott sor azonosítójának megszerzése
            const rowId = likeButton.closest('tr').id;
            const examId = rowId.replace('exam-row-', '');
            //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
            if (likeButton.classList.contains('like-button-link-active')) {
                // Like visszavonása
                sendLikeOrDislike(examId, 'revokelike');

                likeButton.classList.remove('like-button-link-active');
                // Távolítsuk el az aktív like gomb állapotát a helyi tárolóból is
                setLikeOrDislikeStatusToActiveOrInactive(examId, 'setLikeToInactive');
                //localStorage.removeItem(`likeButtonState_${UserId}_${resourceId}`);
            } else {
                const activeDislikeButton = document.querySelector(`#${rowId} .dislike-button-link.dislike-button-link-active`);
                if (activeDislikeButton) {
                    activeDislikeButton.classList.remove('dislike-button-link-active');
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToInactive');
                    //localStorage.removeItem(`dislikeButtonState_${UserId}_${resourceId}`);
                    sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(examId, 'likeExamAndRevokeDislike');
                    // Módosítsuk az osztályt a "like-button-link-active"-ra
                    likeButton.classList.add('like-button-link-active');


                    setLikeOrDislikeStatusToActiveOrInactive(examId, 'setLikeToActiveAndDislikeToInactive');
                    // Mentsük el az aktív like gomb állapotát a helyi tárolóban
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
                    //localStorage.setItem(`likeButtonState_${UserId}_${resourceId}`, 'active');
                } else {
                    // Like küldése a szervernek
                    sendLikeOrDislike(examId, 'like');

                    // Módosítsuk az osztályt a "like-button-link-active"-ra
                    likeButton.classList.add('like-button-link-active');

                    // Mentsük el az aktív like gomb állapotát a helyi tárolóban
                    setLikeOrDislikeStatusToActiveOrInactive(examId, 'setLikeToActive');
                    //localStorage.setItem(`likeButtonState_${UserId}_${resourceId}`, 'active');
                }
            }

            // Ellenőrizze, hogy az adott sorban már van aktív dislike gomb
            // const activeDislikeButton = document.querySelector(`#${rowId} .dislike-button-link.dislike-button-link-active`);
            // if (activeDislikeButton) {
            //     sendRevokeLikeOrDislike(resourceId, 'revokedislike');
            //     activeDislikeButton.classList.remove('dislike-button-link-active');
            //     localStorage.removeItem(`dislikeButtonState_${resourceId}`);
            // }


        });
    });

// A dislike gomb eseménykezelője
    document.querySelectorAll('.dislike-button-link').forEach(dislikeButton => {
        dislikeButton.addEventListener('click', () => {
            // Az adott sor azonosítójának megszerzése
            const rowId = dislikeButton.closest('tr').id;
            const examId = rowId.replace('exam-row-', '');

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
                sendLikeOrDislike(examId, 'revokedislike');

                dislikeButton.classList.remove('dislike-button-link-active');
                // Távolítsuk el az aktív dislike gomb állapotát a helyi tárolóból is
                setLikeOrDislikeStatusToActiveOrInactive(examId, 'setDislikeToInactive');
                //localStorage.removeItem(`dislikeButtonState_${UserId}_${resourceId}`);
            } else {
                const activeLikeButton = document.querySelector(`#${rowId} .like-button-link.like-button-link-active`);
                if (activeLikeButton) {
                    activeLikeButton.classList.remove('like-button-link-active');

                    // itt a kozosbe kicserelni ezt a meghivast
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToInactive');
                    //localStorage.removeItem(`likeButtonState_${UserId}_${resourceId}`);
                    sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(examId, 'dislikeExamAndRevokeLike');
                    // Módosítsuk az osztályt a "dislike-button-link-active"-ra
                    dislikeButton.classList.add('dislike-button-link-active');

                    setLikeOrDislikeStatusToActiveOrInactive(examId, 'setDislikeToActiveAndLikeToInactive');

                    // Mentsük el az aktív dislike gomb állapotát a helyi tárolóban
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActive');
                    //localStorage.setItem(`dislikeButtonState_${UserId}_${resourceId}`, 'active');
                } else {
                    // Dislike küldése a szervernek
                    sendLikeOrDislike(examId, 'dislike');

                    // Módosítsuk az osztályt a "dislike-button-link-active"-ra
                    dislikeButton.classList.add('dislike-button-link-active');

                    // Mentsük el az aktív dislike gomb állapotát a helyi tárolóban
                    setLikeOrDislikeStatusToActiveOrInactive(examId, 'setDislikeToActive');
                    //localStorage.setItem(`dislikeButtonState_${UserId}_${resourceId}`, 'active');
                }

            }
        });
    });


})



// NEGYEDIK VERZIO (vegleges)
//lekerni az osszes like es dislike allasat az adatbazisbol
let likeAndDislikeStatuses = [];
document.addEventListener('DOMContentLoaded', function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    //const url = `/resources/${action}?resourceId=${resourceId}`
    fetch("/resources/examExamples/getLikeAndDislikeStatuses", {
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
            //console.log(likeAndDislikeStatuses);
            handleLikeAndDislikeStatuses();
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
        const examId = likeAndDislikeData.resourceId;
        const like = likeAndDislikeData.like;
        const dislike = likeAndDislikeData.dislike;

        // Itt kezeld az adatokat vagy végezz velük bármit, amit szeretnél
        //console.log(`Exam ID: ${examId}, Like: ${like}, Dislike: ${dislike}`);
        const likeCountElement = document.querySelector(`#exam-row-${examId} .like-button-link`);
        const dislikeCountElement = document.querySelector(`#exam-row-${examId} .dislike-button-link`);
        //console.log(likeCountElement);
        //console.log(dislikeCountElement);
        if (like === 1) {
            likeCountElement.classList.add('like-button-link-active');
        } else if (dislike === 1) {
            dislikeCountElement.classList.add('dislike-button-link-active');
        }

    }
}

let examimages = [];
document.addEventListener('DOMContentLoaded', function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    //const url = `/resources/${action}?resourceId=${resourceId}`
    fetch("/resources/examExamples/getallexamimage", {
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
            //console.log(data.igenigen);
            examimages = data.examimagesandid;
            //console.log(examimages);
            handlerexmaimages();
            //console.log(likeAndDislikeStatuses);
        })
        .catch(error => {
            console.error('Error:', error);
        })
});

function handlerexmaimages() {
    // Itt már rendelkezésre állnak az adatok
    //console.log(likeAndDislikeStatuses);

    // Most már kezelheted az adatokat
    for (let i = 0; i < examimages.length; i++) {
        const examsData = examimages[i];
        //console.log(likeAndDislikeData[1]);
        const image = examsData[0];
        const examId = examsData[1];

        //console.log(base64Image);
        var modal = document.getElementById('myModal-' + examId);
        var modalImg = document.getElementById('modalImg-' + examId);

        //modal.style.display = 'block';
        modalImg.src = 'data:image/jpeg;base64,' + image;

    }
}




$(document).ready(function() {
    $(".dynamic-resize").each(function() {
        var img = $(this);

        img.on('load', function() {
            if (img.width() > img.height()) {
                img.css('width', '60%');
                img.css('height', 'auto');
            } else {
                img.css('height', '80%');
                img.css('width', 'auto');
            }
        });
    });
});


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

let originalRows = []; // Változó az eredeti sorok tárolásához

function saveOriginalRows() {
    const table = document.querySelector(".link-table");
    originalRows = Array.from(table.getElementsByTagName('tr'));
}

function restoreOriginalTable() {
    const tableBody = document.getElementById('dataTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';

    originalRows.forEach(row => tableBody.appendChild(row.cloneNode(true)));
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
    document.getElementById('search-button').click();
    searchTable();
});

document.querySelectorAll('.sortable').forEach(headerCell => {
    headerCell.addEventListener('click', () => {
        const table = document.getElementById('dataTable');
        const tbody = table.querySelector('tbody');
        const headerIndex = Array.prototype.indexOf.call(headerCell.parentElement.children, headerCell);
        const rows = Array.from(tbody.querySelectorAll('tr'));

        const sortType = headerCell.dataset.sort || 'string';
        const column = headerCell.dataset.column;

        const currentSortOrder = headerCell.getAttribute('data-order');
        const isAscending = currentSortOrder === 'asc';

        const sortedRows = rows.sort((a, b) => {
            let valueA = a.children[headerIndex].innerText.trim();
            let valueB = b.children[headerIndex].innerText.trim();

            if (column === 'likes') {
                valueA = parseFloat(valueA.split(' ')[0]); // Csak a like szám
                valueB = parseFloat(valueB.split(' ')[0]); // Csak a like szám
            } else if (sortType === 'number') {
                valueA = parseFloat(valueA);
                valueB = parseFloat(valueB);
            }

            // Kis- és nagybetűk figyelmen kívül hagyása az ABC szerinti rendezésnél
            if (typeof valueA === 'string' && typeof valueB === 'string') {
                return isAscending ? valueA.localeCompare(valueB, 'en', { sensitivity: 'base' }) : valueB.localeCompare(valueA, 'en', { sensitivity: 'base' });
            }

            if (isAscending) {
                return valueA > valueB ? 1 : -1;
            } else {
                return valueA < valueB ? 1 : -1;
            }
        });

        // Fordítsa meg a rendezési sorrendet
        headerCell.setAttribute('data-order', isAscending ? 'desc' : 'asc');

        // Törölje a rendezetlen sorokat és tegye a rendezetteket a tbody-ba
        tbody.innerHTML = '';
        sortedRows.forEach(row => {
            tbody.appendChild(row);
        });
    });
});

// A kép megnyitásáért felelős függvény
function openImageModal(event) {
    const clickedImage = event.target; // A kattintott kép elem
    const modal = clickedImage.nextElementSibling; // A kép melletti modális elem

    // Ellenőrizze, hogy a kattintott elem valóban egy kép-e
    if (clickedImage.tagName === 'IMG' && modal.classList.contains('exam-image-modal')) {
        modal.style.display = "block";
    }
}

// Kép megnyitásának eseménykezelője
document.querySelectorAll('.exam-image-image-container').forEach(imageContainer => {
    imageContainer.addEventListener("click", openImageModal);
});

