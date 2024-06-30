'use strict';

let stompClient = null;
let IdForUserInDiplomaThesesPage = null;
function connectToWebSocketForDiplomaThesesPage() {
    var elementToGetDiplomaThesesUserId = document.querySelector('[id^="userIdForDiplomaThesesPage-"]');
    IdForUserInDiplomaThesesPage = elementToGetDiplomaThesesUserId.id.split("-")[1];

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
    }
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnectedForDiplomaThesesPage, onErrorInDiplomaThesesPage);
}

function onConnectedForDiplomaThesesPage() {
    stompClient.subscribe(`/user/${IdForUserInDiplomaThesesPage}/queue/messages`, onMessageReceivedNotificationInDiplomaThesesPage)
}

function onErrorInDiplomaThesesPage(error) {
}

async function onMessageReceivedNotificationInDiplomaThesesPage(payload) {
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

document.addEventListener("DOMContentLoaded", function() {
document.getElementById("examExamples").addEventListener("click", function () {
    window.location.href = "/resources/examExamples";
});
});

document.addEventListener("DOMContentLoaded", function() {
document.getElementById("resources").addEventListener("click", function () {
    window.location.href = "/resources";
});});

document.addEventListener("DOMContentLoaded", function() {
document.getElementById("examExamplesDrop").addEventListener("click", function () {
    window.location.href = "/resources/examExamples";
});});

document.addEventListener("DOMContentLoaded", function() {
document.getElementById("resourcesDrop").addEventListener("click", function () {
    window.location.href = "/resources";
});});

function toggleFilterDropdown() {
    var dropdownContent = document.getElementById("filter-myDropdown");
    dropdownContent.classList.toggle("active");
}

function closeDropdown(selectedItem) {
    var dropdownContent = document.getElementById("filter-myDropdown");
    dropdownContent.classList.remove("active");
}

function resetInputValue() {
    const input = document.getElementById('filter-input');
    const sugList = document.querySelector('#suggestion-list');
    const topicList = document.querySelector('.topic-checkboxes');
    if (input) {
        topicList.style.display = 'none';
        sugList.style.display = 'none';
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const dropBtn = document.querySelector('.dropbtn');
    if (dropBtn) {
        dropBtn.addEventListener('click', resetInputValue);
    }
});

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

function setupModifyDiplomaThesesModal(diplomaId) {
    var modalId = "diplomaThesesModifyModal-" + diplomaId;
    var modal = document.getElementById(modalId);

    if (modal) {
        var btnId = "modifyIcon";
        var btn1 = document.getElementById(btnId);
        modal.style.display = "flex";
    }
}

function setupDeleteDiplomaThesesModal(diplomaId) {
    var modalId = "diplomaThesesDeleteModal-" + diplomaId;
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

function toggleDropdownModifyModal(diplomaId) {
    var dropdownId = "topic-myDropdown-modify-" + diplomaId;
    var dropdown = document.getElementById(dropdownId);

    if (dropdown.style.display === "block") {
        dropdown.style.display = "none";
    } else {
        dropdown.style.display = "block";
    }
}

function closeDropdownTopics(selectedItem) {
    var dropdownContent = document.getElementById("topic-myDropdown");
    dropdownContent.style.display = "none";
}

function closeDropdownModifyTopics(diplomaId, selectedItem) {
    var dropdownContentId = "topic-myDropdown-modify-" + diplomaId;
    var dropdownContent = document.getElementById(dropdownContentId);
    dropdownContent.style.display = "none";
}

function validateName() {
    var nameInput = document.getElementById("diplomaTheses-edit");
    var nameValue = nameInput.value.trim();
    var uploadButton = document.getElementById("upload-button-modal");
    var yearInput = document.getElementById("diplomaTheses-edit-year");
    var yearValue = yearInput.value.trim();
    var currentYear = new Date().getFullYear();

    var nameValid = /^[a-zA-Zéáűúőíöü\s'-]{1,30}$/.test(nameValue);
    var yearValid = /^(2000|[2-9]\d{3}|1\d{4}|20[01]\d)$/.test(yearValue) && !isNaN(yearValue) && parseInt(yearValue) <= currentYear;

    if (!nameValid) {
        nameInput.classList.add("highlight");
    } else {
        nameInput.classList.remove("highlight");
    }

    if (!yearValid) {
        yearInput.classList.add("highlight");
    } else {
        yearInput.classList.remove("highlight");
    }

    if (!nameValid || !yearValid) {
        uploadButton.disabled = true;
        uploadButton.style.opacity = 0.5;
        uploadButton.style.cursor = "not-allowed";
    } else {
        uploadButton.disabled = false;
        uploadButton.style.opacity = 1;
        uploadButton.style.cursor = "pointer";
    }
}

function validateModifiedName(diplomaId) {
    var nameInput = document.getElementById("diplomaTheses-edit-modify-" + diplomaId);
    var nameValue = nameInput.value.trim();
    var modifyButton = document.getElementById("modify-button-modal-" + diplomaId);
    var yearInput = document.getElementById("diplomaTheses-edit-year-modify-" + diplomaId);
    var yearValue = yearInput.value.trim();
    var currentYear = new Date().getFullYear();

    var nameValid = /^[a-zA-Zéáűúőíöü\s'-]{1,30}$/.test(nameValue);
    var yearValid = /^(2000|[2-9]\d{3}|1\d{4}|20[01]\d)$/.test(yearValue) && !isNaN(yearValue) && parseInt(yearValue) <= currentYear;

    if (!nameValid) {
        nameInput.classList.add("highlight");
    } else {
        nameInput.classList.remove("highlight");
    }

    if (!yearValid) {
        yearInput.classList.add("highlight");
    } else {
        yearInput.classList.remove("highlight");
    }

    if (!nameValid || !yearValid) {
        modifyButton.disabled = true;
        modifyButton.style.opacity = 0.5;
        modifyButton.style.cursor = "not-allowed";
    } else {
        modifyButton.disabled = false;
        modifyButton.style.opacity = 1;
        modifyButton.style.cursor = "pointer";
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
            const diplomaId = link.dataset.diplomaId;
            const selectedValueInputId = "topic-selected-modal-modify-" + diplomaId;
            const dropdownButtonId = "topic-dropbtn-modal-modify-" + diplomaId;
            const selectedValueInput = document.getElementById(selectedValueInputId);
            const dropdownButton = document.getElementById(dropdownButtonId);


            selectedValueInput.value = link.textContent;
            dropdownButton.textContent = link.textContent;

            if (diplomaId && diplomaId !== "Choose a topic") {
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

var cancelButton = document.querySelector('.cancel-button-modal.close-diplomaTheses');
document.addEventListener("DOMContentLoaded", function() {
    cancelButton.addEventListener('click', closeModal);
});

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

function closeModifyModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.style.display = "none";
    } else {
        console.error("Modal with id '" + id + "' not found.");
    }
}

document.addEventListener("DOMContentLoaded", function() {
document.getElementById("fileUpload").addEventListener("change", function() {
    const fileInput = document.getElementById("fileUpload");
    const fileNameInput = document.getElementById("diplomaThesesFileName-edit");

    if (fileInput.files.length > 0) {
        const selectedFile = fileInput.files[0];
        fileNameInput.value = selectedFile.name;
    }
});
});

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("fileUploadButton").addEventListener("click", function() {
        const fileInput = document.getElementById("fileUpload");
        fileInput.click();
    });
});

var linkCounter = 0;
function addLinkRow() {
    try {
        var selectedFileName = document.getElementById("diplomaThesesFileName-edit").value;
        var selectedName = document.getElementById("diplomaThesesName-edit").value;
        var selectedYear = document.getElementById("diplomaTheses-edit-year").value;
        var selectedTopic = document.getElementById("topic-selected-modal").value;

        if (selectedTopic !== "" && selectedFileName !== "" && selectedName !== "" && selectedYear !== "") {
            var tableContainer = document.querySelector(".table-container table tbody");
            var row = document.createElement("tr");
            var linkNumber = document.createElement("td");
            linkNumber.textContent = linkCounter;
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

            var likeButton = document.createElement("button");
            likeButton.textContent = "Like";
            likeButton.className = "like-button-link";

            var dislikeButton = document.createElement("button");
            dislikeButton.textContent = "Dislike";
            dislikeButton.className = "dislike-button-link";

            var downloadButton = document.createElement("button");
            downloadButton.textContent = "Download";
            downloadButton.className = "download-button";

            linkButtons.appendChild(likeButton);
            linkButtons.appendChild(dislikeButton);
            linkButtons.appendChild(downloadButton);

            row.appendChild(linkNumber);
            row.appendChild(linkName);
            row.appendChild(linkYear);
            row.appendChild(linkTopic);
            row.appendChild(linkUser);
            row.appendChild(linkLikes);
            row.appendChild(linkButtons);
            tableContainer.appendChild(row);
            linkCounter++;
        }
    } catch (error) {
        console.error('An error occurred:', error);
    }
}


async function saveDiplomaThesesDataToServer() {
    var pdfFile = document.getElementById("fileUpload").files[0];
    var pdfFileName = document.getElementById("diplomaTheses-edit").value;
    var pdfFileYear = document.getElementById("diplomaTheses-edit-year").value;
    var pdfTopic = document.getElementById("topic-selected-modal").value;

    if (pdfFile == null || pdfFileName === "" || pdfFileYear === "" || pdfTopic === "") {
        showErrorMessageInDiploma("Please fill out all fields!");
        return;
    }
    showLoadingModal();
    var data = {
        pdf: pdfFile,
        name: pdfFileName,
        year: pdfFileYear,
        topic: pdfTopic
    };

    try {
        await sendDiplomaThesesDataToServer(data);
    } catch (error) {
        hideLoadingModal();
    }
}

function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block";
}

function hideLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "none";
}

async function sendDiplomaThesesDataToServer(data) {
    var token = $("meta[name='_csrf']").attr("content");

    var formData = new FormData();
    formData.append("pdf", data.pdf);
    formData.append("name", data.name);
    formData.append("year", data.year);
    formData.append("topic", data.topic);

    try {
        const response = await fetch('/resources/diplomaTheses/uploadDiplomaTheses', {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': token
            },
            body: formData
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const responseData = await response.text();
        if (responseData === "Success") {
            showLoadingModal();
            location.reload();
        } else if (responseData === "Too large") {
            hideLoadingModal();
            showErrorMessageInExam("The file is too large!");
        } else if (responseData === "TOO LARGE FILE") {
            hideLoadingModal();
            showErrorMessageInExam("The file size exceeds the maximum limit of 10 MB!");
        } else {
            hideLoadingModal();
            showErrorMessageInExam("Unknown error occurred");
        }

    } catch (error) {
        console.error('An error occurred:', error);
        throw error;
    }
}

//DELETE
function deleteDiplomaThesesData(diplomaId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    showLoadingModal()
    fetch(`/resources/diplomaTheses/deleteDiplomaTheses?diploma_id=${diplomaId}`, {
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

// MODIFY
function saveModifiedDiplomaThesesDataToServer(diplomaId) {
    var data = [];

    const pdfFileNameId = "diplomaTheses-edit-modify-" + diplomaId;
    const pdfFileName = document.getElementById(pdfFileNameId).value;
    const pdfFileYearId = "diplomaTheses-edit-year-modify-" + diplomaId;
    const pdfFileYear = document.getElementById(pdfFileYearId).value;
    const pdfTopicId = "topic-dropbtn-modal-modify-" + diplomaId;
    const pdfTopicElement = document.getElementById(pdfTopicId);
    const pdfTopicValue = pdfTopicElement.innerText;

    data.push({
        name: pdfFileName,
        year: pdfFileYear,
        topic: pdfTopicValue,
        diploma_id: diplomaId
    });

    if ( pdfFileName === "" || pdfFileYear === "" || pdfTopicValue === "") {
        showErrorMessageInDiploma("Please fill out all fields!");
    } else {
        sendModifiedDiplomaThesesDataToServer(data);
    }
}

function sendModifiedDiplomaThesesDataToServer(data) {
    showLoadingModal()
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var formData = new FormData();
    formData.append("name", data[0].name);
    formData.append("year", data[0].year);
    formData.append("topic", data[0].topic);
    formData.append("diploma_id", data[0].diploma_id);

    fetch('/resources/diplomaTheses/modifyDiplomaTheses', {
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
        } else if(data === "Too large"){
            showErrorMessageInExam("The file is too large!");
        } else if (data === "TOO LARGE FILE"){
            showErrorMessageInExam("The file size exceeds the maximum limit of 10 MB!");
        }
    }).catch(error => {
        hideLoadingModal()
        console.error('An error occurred:', error);
    });
}

function showErrorMessageInDiploma(message) {
    var errorMessageElement = document.getElementById('error-message-diploma-modal-content');
    errorMessageElement.innerText = message;
}

var pdfImageContainer = document.querySelector(".pdf-image-image-container");
var pdfImageModal = document.querySelector(".pdf-image-modal");
var pdfEnImageContainer = document.querySelector(".pdf-en-abstract-image-container");
var pdfEnImageModal = document.querySelector(".pdf-en-abstract-image-modal");

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
                fetch(sseUrl, {
                    method: 'POST',
                    body: JSON.stringify({message: `${action}:${diplomaId}`}),
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': token
                    },
                })
                response.text().then(data => {
                }).catch(error => {
                    console.error('Error:', error);
                });
            } else {
                throw new Error('Request failed');
            }
        })
        .then(data => {
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
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        },
    })
        .then(response => {
            if (response.ok) {
                fetch(sseUrl, {
                    method: 'POST',
                    body: JSON.stringify({message: `${action}:${diplomaId}`}),
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': token
                    },
                })
                response.text().then(data => {
                }).catch(error => {
                    console.error('Error:', error);
                });
            } else {
                throw new Error('Request failed');
            }
        })
        .then(data => {
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function setLikeOrDislikeStatusToActiveOrInactive(diplomaId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    const url = `/resources/diplomaTheses/${action}?diplomaId=${diplomaId}`;
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        },
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Request failed');
        }
    }).then(data => {
    }).catch(error => {
        console.error('Error:', error);
    });
}

function handleLikeButtonClick(likeButton) {
    const rowId = likeButton.closest('tr').id;
    const diplomaId = rowId.replace('diploma-row-', '');
    if (likeButton.classList.contains('like-button-link-active')) {
        sendLikeOrDislike(diplomaId, 'revokelike');
        likeButton.classList.remove('like-button-link-active');
        setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setLikeToInactive');
    } else {
        const activeDislikeButton = document.querySelector(`#${rowId} .dislike-button-link.dislike-button-link-active`);
        if (activeDislikeButton) {
            activeDislikeButton.classList.remove('dislike-button-link-active');
            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(diplomaId, 'likeDiplomaAndRevokeDislike');
            likeButton.classList.add('like-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setLikeToActiveAndDislikeToInactive');
        } else {
            sendLikeOrDislike(diplomaId, 'like');
            likeButton.classList.add('like-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setLikeToActive');
        }
    }
}

function handleDislikeButtonClick(dislikeButton) {
    const rowId = dislikeButton.closest('tr').id;
    const diplomaId = rowId.replace('diploma-row-', '');
    if (dislikeButton.classList.contains('dislike-button-link-active')) {
        sendLikeOrDislike(diplomaId, 'revokedislike');
        dislikeButton.classList.remove('dislike-button-link-active');
        setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setDislikeToInactive');
    } else {
        const activeLikeButton = document.querySelector(`#${rowId} .like-button-link.like-button-link-active`);
        if (activeLikeButton) {
            activeLikeButton.classList.remove('like-button-link-active');
            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(diplomaId, 'dislikeDiplomaAndRevokeLike');
            dislikeButton.classList.add('dislike-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setDislikeToActiveAndLikeToInactive');
        } else {
            sendLikeOrDislike(diplomaId, 'dislike');
            dislikeButton.classList.add('dislike-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(diplomaId, 'setDislikeToActive');
        }
    }}

if (typeof jQuery !== 'undefined') {
$(document).ready(async function () {
    $(document).trigger('myCustomLoadEvent');
    var urlEndpoint = "/sse/subscribe";
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
        likeCountElement.textContent = data.like;
        dislikeCountElement.textContent = data.dislike;
    });

    document.querySelectorAll('.like-button-link').forEach(likeButton => {
        likeButton.addEventListener('click', () => {
            handleLikeButtonClick(likeButton);
        });
    });

    document.querySelectorAll('.dislike-button-link').forEach(dislikeButton => {
        dislikeButton.addEventListener('click', () => {
            handleDislikeButtonClick(dislikeButton);
        });
    });
});
} else {
    console.warn('jQuery is not defined. Skipping jQuery-dependent code.');
}

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
        handleLikeAndDislikeStatuses();
    } catch (error) {
        console.error('Error:', error);
    }
});

function handleLikeAndDislikeStatuses() {
    for (let i = 0; i < likeAndDislikeStatuses.length; i++) {
        const likeAndDislikeData = likeAndDislikeStatuses[i];
        const diplomaId = likeAndDislikeData.resourceId;
        const like = likeAndDislikeData.like;
        const dislike = likeAndDislikeData.dislike;

        const likeCountElement = document.querySelector(`#diploma-row-${diplomaId} .like-button-link`);
        const dislikeCountElement = document.querySelector(`#diploma-row-${diplomaId} .dislike-button-link`);

        if(likeCountElement !== null || dislikeCountElement !== null) {
            if (like === 1) {
                likeCountElement.classList.add('like-button-link-active');
            } else if (dislike === 1){
                dislikeCountElement.classList.add('dislike-button-link-active');
            }
        }
    }
}

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
                diplomaPDF.push({
                    id: id,
                    diplomaThesesFile: diplomaThesesFile
                });
            }
            hideLoadingModal()
            handlerdiplomaPDFs();

        })
        .catch(error => {
            hideLoadingModal()
            showNoteModal();
        });
}

function showNoteModal() {
    const modal = document.getElementById('notificationModal');
    modal.style.display = 'block';
}

function closeNoteModal() {
    const modal = document.getElementById('notificationModal');
    modal.style.display = 'none';
    console.log("closeNoteModal");
}

function handlerdiplomaPDFs() {
    for (let i = 0; i < diplomaPDF.length; i++) {
        const diplomaThesesData = diplomaPDF[i];
        const PDF = diplomaThesesData.diplomaThesesFile;
        const diplomaId = diplomaThesesData.id;
        var binaryPDF = atob(PDF);
        var arrayPDF = new Uint8Array(binaryPDF.length);
        for (var j = 0; j < binaryPDF.length; j++) {
            arrayPDF[j] = binaryPDF.charCodeAt(j);
        }

        var pdfBlob = new Blob([arrayPDF], { type: 'application/pdf;charset=utf-8' });
        var blobUrl = URL.createObjectURL(pdfBlob);

        var modal = document.getElementById('myModal-' + diplomaId);
        var modalPDF = document.getElementById('modalPDF-' + diplomaId);
        var modalPDFMobile = document.getElementById('modalPDFMobile-' + diplomaId);

        if (isMobileOrTabletScreen()) {
            modalPDFMobile.href = blobUrl;
            modalPDFMobile.click();
        } else {
            modal.style.display = 'block';
            modalPDF.src = blobUrl;
        }
    }
    diplomaPDF = [];
}

let diplomaEnAbstractPDF = [];
function getDiplomaEnAbstractId (diplomaId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    showLoadingModal()
    const url = `/resources/diplomaTheses/geten_abstractbyid?diplomaId=${diplomaId}`;

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
                diplomaEnAbstractPDF.push({
                    id: id,
                    diplomaThesesFile: diplomaThesesFile
                });
            }
            handlerdiplomaEnAbstractPDFs();
            hideLoadingModal();

        })
        .catch(error => {
            hideLoadingModal()
            console.error('Error:', error);
            showNoteModal();
        });
}


function handlerdiplomaEnAbstractPDFs() {
    for (let i = 0; i < diplomaEnAbstractPDF.length; i++) {
        const diplomaThesesData = diplomaEnAbstractPDF[i];
        const PDF = diplomaThesesData.diplomaThesesFile;
        const diplomaId = diplomaThesesData.id;
        var binaryPDF = atob(PDF);
        var arrayPDF = new Uint8Array(binaryPDF.length);
        for (var j = 0; j < binaryPDF.length; j++) {
            arrayPDF[j] = binaryPDF.charCodeAt(j);
        }

        var pdfBlob = new Blob([arrayPDF], { type: 'application/pdf;charset=utf-8' });
        var blobUrl = URL.createObjectURL(pdfBlob);

        var modal = document.getElementById('myEnAbstractModal-' + diplomaId);
        var modalPDF = document.getElementById('modalEnPDF-' + diplomaId);
        var modalPDFMobile = document.getElementById('modalEnPDFMobile-' + diplomaId);

        if (isMobileOrTabletScreen()) {
            modalPDFMobile.href = blobUrl;
            modalPDFMobile.click();
        } else {
            modal.style.display = 'block';
            modalPDF.src = blobUrl;
        }
    }
    diplomaEnAbstractPDF = [];
}

let diplomaHunAbstractPDF = [];
function getDiplomaHunAbstractId (diplomaId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    showLoadingModal()
    const url = `/resources/diplomaTheses/gethu_abstractbyid?diplomaId=${diplomaId}`;

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
                diplomaHunAbstractPDF.push({
                    id: id,
                    diplomaThesesFile: diplomaThesesFile
                });
            }
            hideLoadingModal()
            handlerdiplomaHunAbstractPDFs();

        })
        .catch(error => {
            hideLoadingModal()
            console.error('Error:', error);
            showNoteModal();
        });
}


function handlerdiplomaHunAbstractPDFs() {
    for (let i = 0; i < diplomaHunAbstractPDF.length; i++) {
        const diplomaThesesData = diplomaHunAbstractPDF[i];
        const PDF = diplomaThesesData.diplomaThesesFile;
        const diplomaId = diplomaThesesData.id;
        var binaryPDF = atob(PDF);
        var arrayPDF = new Uint8Array(binaryPDF.length);
        for (var j = 0; j < binaryPDF.length; j++) {
            arrayPDF[j] = binaryPDF.charCodeAt(j);
        }

        var pdfBlob = new Blob([arrayPDF], { type: 'application/pdf;charset=utf-8' });
        var blobUrl = URL.createObjectURL(pdfBlob);

        var modal = document.getElementById('myHunAbstractModal-' + diplomaId);
        var modalPDF = document.getElementById('modalHunPDF-' + diplomaId);
        var modalPDFMobile = document.getElementById('modalHunPDFMobile-' + diplomaId);

        if (isMobileOrTabletScreen()) {
            modalPDFMobile.href = blobUrl;
            modalPDFMobile.click();
        } else {
            modal.style.display = 'block';
            modalPDF.src = blobUrl;
        }
    }
    diplomaHunAbstractPDF = [];
}

function isMobileOrTabletScreen() {
    return window.innerWidth <= 1024;
}


document.addEventListener('click', function(event) {
    if (event.target.classList.contains('close-pdf-modal-btn')) {
        const modalId = event.target.closest('.pdf-image-modal').id;
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'none';
        }
    }
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

let originalRows = [];

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

document.addEventListener('DOMContentLoaded', function() {
    const checkbox = document.querySelector('.tags-checkbox');
    const filterInput = document.querySelector('.filter-input');

    checkbox.addEventListener('change', function() {
        if (this.checked) {
            filterInput.setAttribute('placeholder', 'Search by Keywords...');
            filterInput.style.fontSize = 'smaller';
        } else {
            filterInput.setAttribute('placeholder', 'Search by Name...');
            filterInput.style.fontSize = 'smaller';
        }
    });
});

// async function searchTable() {
//     if (originalRows.length === 0) {
//         saveOriginalRows();
//     } else {
//         restoreOriginalTable();
//     }
//
//     const input = document.getElementById('filter-input');
//     const filter = input.value.toUpperCase();
//     const selectedValues = [];
//     const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');
//     const tagCheckbox = document.querySelector('.tags-checkbox');
//
//     function isCheckboxChecked() {
//         return tagCheckbox.checked;
//     }
//
//
//     checkboxes.forEach((checkbox) => {
//         selectedValues.push(checkbox.value);
//     });
//
//     const table = document.getElementById('dataTable');
//     const rows = table.getElementsByTagName('tr');
//     const filteredRows = [];
//
//
//     for (let i = 1; i < rows.length; i++) {
//         const cells = rows[i].getElementsByTagName('td');
//         const tagCells = rows[i].getElementsByTagName('td');
//         let nameFound = false;
//         let topicFound = false;
//
//         const cell = cells[1];
//         const tagCell = cells[7];
//
//         if (cell) {
//             const cellText = cell.textContent || cell.innerText;
//             if (cellText.toUpperCase().indexOf(filter) > -1) {
//                 nameFound = true;
//             }
//         }
//
//         if (isCheckboxChecked()) {
//             if (tagCell) {
//                 const cellText = tagCell.textContent || tagCell.innerText;
//                 if (cellText.toUpperCase().indexOf(filter) > -1) {
//                     nameFound = true;
//                 }
//             }
//         }
//
//         const topicCell = cells[3];
//
//         if (topicCell) {
//             const cellContent = topicCell.textContent || topicCell.innerText;
//             if (selectedValues.includes(cellContent.trim())) {
//                 topicFound = true;
//             }
//         }
//
//         if (nameFound && topicFound) {
//             filteredRows.push(rows[i]);
//             rows[i].style.display = '';
//         } else {
//             rows[i].style.display = 'none';
//         }
//     }
//     const rowsPerPage = 20;
//     let currentPage = 1;
//
//     function updatePageCounter() {
//         const pageCount = Math.ceil(filteredRows.length / rowsPerPage);
//         document.getElementById("page-counter").textContent = `Page ${currentPage} of ${pageCount}`;
//     }
//
//     function showRowsForCurrentPage() {
//         const startIdx = (currentPage - 1) * rowsPerPage;
//         const endIdx = Math.min(startIdx + rowsPerPage, rows.length);
//
//         filteredRows.forEach((row, index) => {
//             if (index >= startIdx && index < endIdx) {
//                 row.style.display = "";
//             } else {
//                 row.style.display = "none";
//             }
//         });
//     }
//
//     function setPage(page) {
//         currentPage = page;
//         showRowsForCurrentPage();
//         updatePageCounter();
//     }
//
//     updatePageCounter();
//     showRowsForCurrentPage();
//
//     document.getElementById("next-page-button").addEventListener("click", () => {
//         if (currentPage < Math.ceil(filteredRows.length / rowsPerPage)) {
//             setPage(currentPage + 1);
//         } else {
//             setPage(1);
//         }
//     });
//
//     document.getElementById("prev-page-button").addEventListener("click", () => {
//         if (currentPage > 1) {
//             setPage(currentPage - 1);
//         } else {
//             setPage(Math.ceil(filteredRows.length / rowsPerPage));
//         }
//     });
//
//     await new Promise(resolve => setTimeout(resolve, 200));
//
//     hideLoadingModal();
// }

document.getElementById('search-button').addEventListener('click', () => {
    searchInDiplomaThesesTable();
});

function searchInDiplomaThesesTable() {
    showLoadingModal();
    const searchInput = document.getElementById('filter-input');
    const filter = searchInput.value.trim().toLowerCase();
    const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');
    const selectedValues = Array.from(checkboxes).map(checkbox => checkbox.value.trim().toUpperCase());
    const tagCheckbox = document.querySelector('.tags-checkbox');

    if(tagCheckbox.checked === false) {
        const queryString = 'name=' + encodeURIComponent(filter) + '&selectedTopics=' + encodeURIComponent(selectedValues.join(','));
        fetch ('/resources/diplomaTheses/filteredByName?' + queryString, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Request failed');
            }
        }).then(data => {
            //console.log(data);
            window.location.href = '/resources/diplomaTheses/filteredByName?' + queryString;
        }).catch(error => {
            console.error('Error:', error);
            hideLoadingModal();
        })
    } else {
        const queryString = 'keyword=' + encodeURIComponent(filter) + '&selectedTopics=' + encodeURIComponent(selectedValues.join(','));
        fetch ('/resources/diplomaTheses/filteredByKeyword?' + queryString, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Request failed');
            }
        }).then(data => {
            window.location.href = '/resources/diplomaTheses/filteredByKeyword?' + queryString;
        }).catch(error => {
            console.error('Error:', error);
        })
    }
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
        const topicCell = cells[5];

        if (nameCell && topicCell) {
            const nameText = nameCell.textContent.trim().toUpperCase();
            const topicText = topicCell.textContent.trim().toUpperCase();

            if (nameText.includes(filter) && selectedValues.includes(topicText)) {
                const span = nameCell.querySelector('span');
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

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById('filter-input').addEventListener('input', listSuggestions);
});


var infoModal = document.getElementById("infoModal");

function openInfoModal() {
    infoModal.style.display = "block";
}

function closeInfoModal() {
    infoModal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == infoModal) {
        closeInfoModal();
    }
}

var currentPage = 1;

function nextInfoPage() {
    var paragraphs = document.querySelectorAll('.info-modal-content p');
    if (currentPage < paragraphs.length) {
        paragraphs[currentPage - 1].style.display = 'none';
        currentPage++;
        paragraphs[currentPage - 1].style.display = 'block';
    } else {
        paragraphs[paragraphs.length - 1].style.display = 'none';
        currentPage = 1;
        paragraphs[currentPage - 1].style.display = 'block';
    }
}

function prevInfoPage() {
    var paragraphs = document.querySelectorAll('.info-modal-content p');
    if (currentPage > 1) {
        paragraphs[currentPage - 1].style.display = 'none';
        currentPage--;
        paragraphs[currentPage - 1].style.display = 'block';
    } else {
        paragraphs[0].style.display = 'none';
        currentPage = paragraphs.length;
        paragraphs[currentPage - 1].style.display = 'block';
    }
}

document.addEventListener('click', function(event) {
    var suggestionList = document.getElementById('suggestion-list');
    if (event.target !== suggestionList) {
        suggestionList.style.display = 'none';
    }
});

