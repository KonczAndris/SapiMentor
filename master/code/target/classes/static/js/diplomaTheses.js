'use strict';

let stompClient = null;
let IdForUserInDiplomaThesesPage = null;
function connectToWebSocketForDiplomaThesesPage() {
    var elementToGetDiplomaThesesUserId = document.querySelector('[id^="userIdForDiplomaThesesPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInDiplomaThesesPage = elementToGetDiplomaThesesUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForDiplomaThesesPage, onErrorInDiplomaThesesPage);
}

function onConnectedForDiplomaThesesPage() {
    stompClient.subscribe(`/user/${IdForUserInDiplomaThesesPage}/queue/messages`, onMessageReceivedNotificationInDiplomaThesesPage)
}

function onErrorInDiplomaThesesPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceivedNotificationInDiplomaThesesPage(payload) {
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

    connectToWebSocketForDiplomaThesesPage();

    var elementToGetDiplomaThesesProfileUserId = document.querySelector('[id^="userIdForDiplomaThesesPage-"]');
    var DiplomaThesesprofileUserId = elementToGetDiplomaThesesProfileUserId.id.split("-")[1];

    fetch(`/resources/diplomaTheses/getDiplomaThesesProfileNotificationStatus?userId=${DiplomaThesesprofileUserId}`, {
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



document.getElementById("examExamples").addEventListener("click", function () {
    window.location.href = "/resources/examExamples";
});

document.getElementById("resources").addEventListener("click", function () {
    window.location.href = "/resources";
});

document.getElementById("examExamplesDrop").addEventListener("click", function () {
    window.location.href = "/resources/examExamples";
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

function toggleDiplomaDropdown() {
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


function setupDiplomaThesesModal() {
    var modal = document.getElementById("diplomaThesesModal");

    if (modal) {
        var btn1 = document.getElementById("upload-upload");
        var span = document.getElementsByClassName("close-diplomaTheses")[0];

        if (btn1) {
            btn1.onclick = function () {
                modal.style.display = "flex";
            }
        }

        if (span) {
            span.onclick = function () {
                modal.style.display = "none";
            }
        }
    }

}

setupDiplomaThesesModal();


function closeModalOnClickOutside() {
    var modal1 = document.getElementById("diplomaThesesModal");

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
    var modal = document.getElementById("diplomaThesesModal");

    var btn1 = document.getElementById("upload-upload");
    var btn2 = document.getElementById("upload-hidden");
    var span = document.getElementsByClassName("close-diplomaTheses")[0];

    if (btn1) {
        btn1.onclick = function() {
            modal.style.display = "flex";
        }
    }

    if (btn2) {
        btn2.onclick = function () {
            modal.style.display = "flex";
        }
    }

    if (span) {
        span.onclick = function () {
            modal.style.display = "none";
        }
    }
}

function closeModal() {
    var modal = document.getElementById('diplomaThesesModal');
    modal.style.display = 'none';
}

// Add click event listener to the "Cancel" button
var cancelButton = document.querySelector('.cancel-button-modal.close-diplomaTheses');
cancelButton.addEventListener('click', closeModal);

function closeModalOnClickOutside() {
    var modal1 = document.getElementById("diplomaThesesModal");

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
    const fileNameInput = document.getElementById("diplomaThesesFileName-edit");

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
        var selectedFileName = document.getElementById("diplomaThesesFileName-edit").value;
        var selectedName = document.getElementById("diplomaThesesName-edit").value;
        var selectedYear = document.getElementById("diplomaTheses-edit-year").value;
        var selectedTopic = document.getElementById("topic-selected-modal").value;

        if (selectedTopic !== "" && selectedFileName !== "" && selectedName !== "" && selectedYear !== "") {
            var tableContainer = document.querySelector(".table-container table tbody");

            // Create a new row
            var row = document.createElement("tr");

            // Create cells for the row
            var linkNumber = document.createElement("td");
            linkNumber.textContent = linkCounter;

            // Create a clickable link (anchor) for linkName
            var linkName = document.createElement("td");
            linkName = selectedName;

            var linkYear = document.createElement("td");
            linkYear = selectedYear;

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
            row.appendChild(linkYear);
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

function saveDiplomaThesesDataToServer() {
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
    var pdfFile = document.getElementById("fileUpload").files[0];
    var pdfFileName = document.getElementById("diplomaTheses-edit").value;
    var pdfFileYear = document.getElementById("diplomaTheses-edit-year").value;
    var pdfTopic = document.getElementById("topic-selected-modal").value;

    data.push({
        name: pdfFileName,
        pdf: pdfFile,
        topic: pdfTopic,
        year: pdfFileYear
    });

    if (pdfFile == null || pdfFileName === "" || pdfFileYear === "" || pdfTopic === "") {
        //alert("Please fill out all fields!");
        showErrorMessageInDiploma("Please fill out all fields!");
    } else {
        sendDiplomaThesesDataToServer(data);
    }

    //console.log(data);
    // sendDiplomaThesesDataToServer(data);
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
function sendDiplomaThesesDataToServer(data) {
    // ide kell majd hogy behozza a toltokepernyot
    showLoadingModal()
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var formData = new FormData();
    formData.append("pdf", data[0].pdf);
    formData.append("name", data[0].name);
    formData.append("year", data[0].year);
    formData.append("topic", data[0].topic);
    // for (var pair of formData.entries()) {
    //     console.log(pair[0] + ': ' + pair[1]);
    // }

    fetch('/resources/diplomaTheses/uploadDiplomaTheses', {
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
            // throw new Error('Hiba történt a válaszban');
        }
    }).then(data => {
        // ezt is andrisnak
        //hideLoadingModal(); // Elrejtjük a modal ablakot
        // Kell kezelni a valaszt es megjeleniteni a hibauzeneteket
        //console.log(data)
        hideLoadingModal()
        if (data === "Success") {
            location.reload();
        } else if(data === "Too large"){
            //alert("The file is too large!");
            showErrorMessageInExam("The file is too large!");
        } else if (data === "TOO LARGE FILE"){
            //alert("The file size exceeds the maximum limit of 10 MB!");
            showErrorMessageInExam("The file size exceeds the maximum limit of 10 MB!"); // Egyéb hiba esetén
        }
    }).catch(error => {
        hideLoadingModal()
        console.error('An error occurred:', error);
        //alert("An error occurred. Please try again later.");
    });

}

function showErrorMessageInDiploma(message) {
    var errorMessageElement = document.getElementById('error-message-diploma-modal-content');
    errorMessageElement.innerText = message;
    // További stílusok vagy műveletek hozzáadhatók a látványosság érdekében
}

//NEW
// Get the elements
var pdfImageContainer = document.querySelector(".pdf-image-image-container");
var pdfImageModal = document.querySelector(".pdf-image-modal");
// Show the modal on hover
// pdfImageContainer.addEventListener("click", function() {
//     pdfImageModal.style.display = "block";
//     pdfImageModal.style.cursor = "default";
// });

// Hide the modal when clicking outside of it
// window.addEventListener("click", function(event) {
//     if (event.target == pdfImageModal) {
//         pdfImageModal.style.display = "none";
//     }
// });

// Eltároljuk az összes sor és modális ablak elemet
// const pdfImageContainers = document.querySelectorAll('.pdf-image-image-container');
// const pdfImageModals = document.querySelectorAll('.pdf-image-modal');
// // Az egyes sorokhoz rendelt modális ablakok kezelése
// for (let i = 0; i < pdfImageContainers.length; i++) {
//     const pdfImageContainer = pdfImageContainers[i];
//     const pdfImageModal = pdfImageModals[i];
//
//     pdfImageContainer.addEventListener("click", function() {
//         pdfImageModal.style.display = "block";
//         pdfImageModal.style.cursor = "default";
//     });
//
//     // Az egyes modális ablakok elrejtése, ha a modális ablakon kívülre kattintunk
//     window.addEventListener("click", function(event) {
//         if (event.target == pdfImageModal) {
//             pdfImageModal.style.display = "none";
//         }
//     });
// }
//
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

    eventSource.addEventListener('LikeOrDislikeDiploma', function (event) {
        const data = JSON.parse(event.data);
        const rowId = data.rowId;
        const likeCountElement = document.querySelector(`#diploma-row-${rowId} #likeButton`);
        const dislikeCountElement = document.querySelector(`#diploma-row-${rowId} #dislikeButton`);

        //console.log('Received SSE message:', data.rowId);
        // Itt frissitsd a like/dislike ertekeket a DOM-ban
        //const likeDislikeCountElement = document.querySelector(`#resource-row-${rowId} .like-dislike-count`);
        //console.log(likeDislikeCountElement);
        likeCountElement.textContent = data.like;
        dislikeCountElement.textContent = data.dislike;
    });


    function sendLikeOrDislike(diplomaId, action) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        const sseUrl = "/sse/sendLikeOrDislikeForDiploma"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

        const url = `/resources/diplomaTheses/${action}?diplomaId=${diplomaId}`;
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
                    console.log("Lassu vagyok");
                    // Sikeres kérés esetén elküldjük egy SSE üzenetet a like/dislike értékről
                    // Az üzenetet most a SSE URL-re küldjük, ami a szerver oldalon kezeli majd
                    fetch(sseUrl, {
                        method: 'POST',
                        body: JSON.stringify({message: `${action}:${diplomaId}`}), // Konvertáljuk JSON formátumra
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


    function sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(diplomaId, action) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        const sseUrl = "/sse/sendLikeOrDislikeForDiploma"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

        const url = `/resources/diplomaTheses/${action}?diplomaId=${diplomaId}`;
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
                        body: JSON.stringify({message: `${action}:${diplomaId}`}), // Konvertáljuk JSON formátumra
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
    function setLikeOrDislikeStatusToActiveOrInactive(diplomaId, action) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        //const sseUrl = "/sse/sendLikeOrDislike"; // Módosítottuk a SSE URL-t sendLikeOrDislike-re

        const url = `/resources/diplomaTheses/${action}?diplomaId=${diplomaId}`;
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
            console.log("likeButtonelso", likeButton);
            // Az adott sor azonosítójának megszerzése
            const rowId = likeButton.closest('tr').id;
            const diplomaId = rowId.replace('diploma-row-', '');
            //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
            if (likeButton.classList.contains('like-button-link-active')) {
                // Like visszavonása
                sendLikeOrDislike(diplomaId, 'revokelike');

                likeButton.classList.remove('like-button-link-active');
                // Távolítsuk el az aktív like gomb állapotát a helyi tárolóból is
                setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setLikeToInactive');
                //localStorage.removeItem(`likeButtonState_${UserId}_${resourceId}`);
            } else {
                const activeDislikeButton = document.querySelector(`#${rowId} .dislike-button-link.dislike-button-link-active`);
                if (activeDislikeButton) {
                    activeDislikeButton.classList.remove('dislike-button-link-active');
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToInactive');
                    //localStorage.removeItem(`dislikeButtonState_${UserId}_${resourceId}`);
                    sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(diplomaId, 'likeDiplomaAndRevokeDislike');
                    // Módosítsuk az osztályt a "like-button-link-active"-ra
                    likeButton.classList.add('like-button-link-active');


                    setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setLikeToActiveAndDislikeToInactive');
                    // Mentsük el az aktív like gomb állapotát a helyi tárolóban
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
                    //localStorage.setItem(`likeButtonState_${UserId}_${resourceId}`, 'active');
                } else {
                    // Like küldése a szervernek
                    sendLikeOrDislike(diplomaId, 'like');

                    // Módosítsuk az osztályt a "like-button-link-active"-ra
                    likeButton.classList.add('like-button-link-active');

                    // Mentsük el az aktív like gomb állapotát a helyi tárolóban
                    setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setLikeToActive');
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
            const diplomaId = rowId.replace('diploma-row-', '');

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
                sendLikeOrDislike(diplomaId, 'revokedislike');

                dislikeButton.classList.remove('dislike-button-link-active');
                // Távolítsuk el az aktív dislike gomb állapotát a helyi tárolóból is
                setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setDislikeToInactive');
                //localStorage.removeItem(`dislikeButtonState_${UserId}_${resourceId}`);
            } else {
                const activeLikeButton = document.querySelector(`#${rowId} .like-button-link.like-button-link-active`);
                if (activeLikeButton) {
                    activeLikeButton.classList.remove('like-button-link-active');

                    // itt a kozosbe kicserelni ezt a meghivast
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToInactive');
                    //localStorage.removeItem(`likeButtonState_${UserId}_${resourceId}`);
                    sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(diplomaId, 'dislikeDiplomaAndRevokeLike');
                    // Módosítsuk az osztályt a "dislike-button-link-active"-ra
                    dislikeButton.classList.add('dislike-button-link-active');

                    setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setDislikeToActiveAndLikeToInactive');

                    // Mentsük el az aktív dislike gomb állapotát a helyi tárolóban
                    //setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActive');
                    //localStorage.setItem(`dislikeButtonState_${UserId}_${resourceId}`, 'active');
                } else {
                    // Dislike küldése a szervernek
                    sendLikeOrDislike(diplomaId, 'dislike');

                    // Módosítsuk az osztályt a "dislike-button-link-active"-ra
                    dislikeButton.classList.add('dislike-button-link-active');

                    // Mentsük el az aktív dislike gomb állapotát a helyi tárolóban
                    setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setDislikeToActive');
                    //localStorage.setItem(`dislikeButtonState_${UserId}_${resourceId}`, 'active');
                }

            }
        });
    });
});

////////ezt kell megnezni mert ez ker le egy csomo mindent valamiert
// NEGYEDIK VERZIO (vegleges)
//lekerni az osszes like es dislike allasat az adatbazisbol
let likeAndDislikeStatuses = [];
document.addEventListener('DOMContentLoaded', async function () {
    try {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        const response = await fetch("/resources/diplomaTheses/getLikeAndDislikeStatuses", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': token
            }
        });

        if (!response.ok) {
            throw new Error('Request failed');
        }

        const data = await response.json();
        likeAndDislikeStatuses = data.likeanddislike;
        //console.log(likeAndDislikeStatuses);
        handleLikeAndDislikeStatuses();
    } catch (error) {
        console.error('Error:', error);
    }
});

function handleLikeAndDislikeStatuses() {
    // Itt már rendelkezésre állnak az adatok
    //console.log(likeAndDislikeStatuses);

    // Most már kezelheted az adatokat
    for (let i = 0; i < likeAndDislikeStatuses.length; i++) {
        const likeAndDislikeData = likeAndDislikeStatuses[i];
        const diplomaId = likeAndDislikeData.resourceId;
        const like = likeAndDislikeData.like;
        const dislike = likeAndDislikeData.dislike;


        // Itt kezeld az adatokat vagy végezz velük bármit, amit szeretnél
        //console.log(`Resource ID: ${resourceId}, Like: ${like}, Dislike: ${dislike}`);
        const likeCountElement = document.querySelector(`#diploma-row-${diplomaId} .like-button-link`);
        const dislikeCountElement = document.querySelector(`#diploma-row-${diplomaId} .dislike-button-link`);
        //console.log(likeCountElement);
        //console.log(dislikeCountElement);
        if (like === 1) {
            likeCountElement.classList.add('like-button-link-active');
        } else if (dislike === 1){
            dislikeCountElement.classList.add('dislike-button-link-active');
        }

    }
}



// let diplomaPDF = [];
// document.addEventListener("DOMContentLoaded", function () {
//     var token = $("meta[name='_csrf']").attr("content");
//     var header = $("meta[name='_csrf_header']").attr("content");
//
//     fetch("/resources/diplomaTheses/getalldiplomapdf", {
//         method: 'GET',
//         headers: {
//             'Content-Type': 'application/x-www-form-urlencoded',
//             'X-CSRF-TOKEN': token
//         }
//     }).then(response => {
//         if (response.ok) {
//             return response.json();
//         } else {
//             throw new Error('Request failed');
//         }
//     })
//         .then(data => {
//             //console.log(data.igenigen);
//             diplomaPDF = data.diplomaPdfsandid;
//             //console.log(diplomaPDF);
//             // Elindítjuk a PDF fájlok egyenkénti betöltését
//             for (let i = 0; i < diplomaPDF.length; i++) {
//                 const diplomaThesesData = diplomaPDF[i];
//                 const PDF = diplomaThesesData[0];
//                 const diplomaId = diplomaThesesData[1];
//
//                 loadAndDisplayPDF(PDF, diplomaId);
//             }
//         })
//         .catch(error => {
//             console.error('Error:', error);
//         })
//     function loadAndDisplayPDF(pdfContent, diplomaId) {
//         //console.log('PDF Content:', pdfContent); // Logoljuk az adatot
//         // A base64 kódolt adatok dekódolása
//         var binaryPDF = atob(pdfContent);
//         var arrayPDF = new Uint8Array(binaryPDF.length);
//         for (var j = 0; j < binaryPDF.length; j++) {
//             arrayPDF[j] = binaryPDF.charCodeAt(j);
//         }
//
//         // Blob létrehozása a PDF adatokból
//         var pdfBlob = new Blob([arrayPDF], { type: 'application/pdf;charset=utf-8' });
//
//         // Blob URL létrehozása
//         var blobUrl = URL.createObjectURL(pdfBlob);
//
//         var modalPDF = document.getElementById('modalPDF-' + diplomaId);
//         console.log(modalPDF);
//
//         // PDF megjelenítése
//         modalPDF.href = blobUrl;
//     }
// });
//
let diplomaPDF = [];
function getDiplomaId (diplomaId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    showLoadingModal()
    const url = `/resources/diplomaTheses/getdiplomabyid?diplomaId=${diplomaId}`;

    fetch(url, {
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
            for (let i = 0; i < data.length; i++) {
                const id = data[i][0];
                const diplomaThesesFile = data[i][1];
                // console.log(id); // diploma ID
                // console.log(diplomaThesesFile); // diploma fájl
                diplomaPDF.push({
                    id: id,
                    diplomaThesesFile: diplomaThesesFile
                });
            }
            hideLoadingModal()
            // diplomaPDF = data.diploma;
            //console.log(diplomaPDF);
            handlerdiplomaPDFs();

        })
        .catch(error => {
            hideLoadingModal()
            console.error('Error:', error);
        })

}


function handlerdiplomaPDFs() {
    // Itt már rendelkezésre állnak az adatok
    //console.log(likeAndDislikeStatuses);
    //console.log(diplomaPDF.length);
    // Most már kezelheted az adatokat
    for (let i = 0; i < diplomaPDF.length; i++) {
        const diplomaThesesData = diplomaPDF[i];
        const PDF = diplomaThesesData.diplomaThesesFile;
        const diplomaId = diplomaThesesData.id;
        //console.log("PDF ADAT:" + PDF)
        //console.log("DIPLOMA ID:" + diplomaId)

        // A base64 kódolt adatok dekódolása
        var binaryPDF = atob(PDF);
        var arrayPDF = new Uint8Array(binaryPDF.length);
        for (var j = 0; j < binaryPDF.length; j++) {
            arrayPDF[j] = binaryPDF.charCodeAt(j);
        }

        // Blob létrehozása a PDF adatokból
        var pdfBlob = new Blob([arrayPDF], { type: 'application/pdf;charset=utf-8' });
        // Blob URL létrehozása
        var blobUrl = URL.createObjectURL(pdfBlob);
        //console.log(blobUrl);

        //console.log(base64Image);
        var modal = document.getElementById('myModal-' + diplomaId);
        var modalPDF = document.getElementById('modalPDF-' + diplomaId);
        var modalPDFMobile = document.getElementById('modalPDFMobile-' + diplomaId);
        //console.log(modalPDF);

        // modal.style.display = 'block';
        // modalPDF.src = blobUrl;
        // modalPDF.href = blobUrl;
        // modalPDF.click();
        //console.log(modalPDF.src);

        if (isMobileOrTabletScreen()) {
            // Mobil eszköz esetén
            modalPDFMobile.href = blobUrl;
            modalPDFMobile.click();
        } else {
            // Nem mobil eszköz esetén
            modal.style.display = 'block';
            modalPDF.src = blobUrl;
        }

    }
    diplomaPDF = [];
}

function isMobileOrTabletScreen() {
    return window.innerWidth <= 1024; // Például, 767 pixel vagy alatta van mobilnak tekintve
}

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
    const table = document.getElementById('dataTable');
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

        const topicCell = cells[3];

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

// Az eseménykezelő, ami bezárja a modalt a bezáró gombra kattintva
// Bezáró gomb eseménykezelője a modal bezárására
document.querySelectorAll('.close-modal-btn').forEach(closeBtn => {
    closeBtn.addEventListener('click', function() {
        const modalId = closeBtn.parentNode.id;
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'none';
        }
    });
});
