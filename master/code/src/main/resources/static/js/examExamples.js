'use strict';

let stompClient = null;
let IdForUserInExmasPage = null;
function connectToWebSocketForExmasPage() {
    var elementToGetExmasUserId = document.querySelector('[id^="userIdForExamsPage-"]');
    IdForUserInExmasPage = elementToGetExmasUserId.id.split("-")[1];

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
    }
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnectedForExmasPage, onErrorInExmasPage);
}

function onConnectedForExmasPage() {
    stompClient.subscribe(`/user/${IdForUserInExmasPage}/queue/messages`, onMessageReceivedNotificationInExmasPage)
}

function onErrorInExmasPage(error) {
}

async function onMessageReceivedNotificationInExmasPage(payload) {
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

function closeDropdownTopics(selectedItem) {
    var dropdownContent = document.getElementById("topic-myDropdown");
    dropdownContent.style.display = "none";
}

function closeDropdownModifyTopics(examId, selectedItem) {
    var dropdownContentId = "topic-myDropdown-modify-" + examId;
    var dropdownContent = document.getElementById(dropdownContentId);

    dropdownContent.style.display = "none";
}

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

function setupModifyExamsModal(examId) {
    var modalId = "examExamplesModifyModal-" + examId;
    var modal = document.getElementById(modalId);

    if (modal) {
        var btnId = "modifyIcon";
        var btn1 = document.getElementById(btnId);
        modal.style.display = "flex";
    }
}

function setupDeleteExamsModal(examId) {
    var modalId = "examExamplesDeleteModal-" + examId;
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

function toggleDropdownModifyModal(examId) {
    var dropdownId = "topic-myDropdown-modify-" + examId;
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
            const examId = link.dataset.examId;
            const selectedValueInputId = "topic-selected-modal-modify-" + examId;
            const dropdownButtonId = "topic-dropbtn-modal-modify-" + examId;
            const selectedValueInput = document.getElementById(selectedValueInputId);
            const dropdownButton = document.getElementById(dropdownButtonId);

            selectedValueInput.value = link.textContent;
            dropdownButton.textContent = link.textContent;

            if (examId && examId !== "Choose a topic") {
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

function closeModifyModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.style.display = "none";
    } else {
        console.error("Modal with id '" + id + "' not found.");
    }
}

function validateName() {
    var nameInput = document.getElementById("examExampleName-edit");
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

function validateModifiedName(examId) {
    var nameInput = document.getElementById("examExampleName-edit-modify-" + examId);
    var nameValue = nameInput.value.trim();
    var modifyButton = document.getElementById("modify-button-modal-" + examId);
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

document.getElementById("fileUpload").addEventListener("change", function() {
    const fileInput = document.getElementById("fileUpload");
    const fileNameInput = document.getElementById("examExampleFileName-edit");

    if (fileInput.files.length > 0) {
        const selectedFile = fileInput.files[0];
        fileNameInput.value = selectedFile.name;
    }
});

document.getElementById("fileUploadButton").addEventListener("click", function() {
    const fileInput = document.getElementById("fileUpload");
    fileInput.click();
});

var linkCounter = 0;
function addLinkRow() {
    try {
        var selectedFileName = document.getElementById("examExampleFileName-edit").value;
        var selectedName = document.getElementById("examExampleName-edit").value;
        var selectedTopic = document.getElementById("topic-selected-modal").value;

        if (selectedTopic !== "" && selectedFileName !== "" && selectedName !== "") {
            var tableContainer = document.querySelector(".table-container table tbody");
            var row = document.createElement("tr");

            var linkNumber = document.createElement("td");
            linkNumber.textContent = linkCounter;

            var linkName = document.createElement("td");
            linkName = selectedName;

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

function saveExamExamplesDataToServer() {
    var data = [];

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

}
function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block";
}

function hideLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "none";
}
function sendExamsDataToServer(data) {
    showLoadingModal();
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var formData = new FormData();
    formData.append("image", data[0].image);
    formData.append("name", data[0].name);
    formData.append("topic", data[0].topic);

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
        }
    }).then(data => {
        if (data === "Success") {
            location.reload();
        } else if(data === "Too large"){
            hideLoadingModal()
            showErrorMessageInExam("The file is too large!\n" +
                "The maximum file size is 6MB!");
        } else if(data === "Wrong type"){
            hideLoadingModal()
            showErrorMessageInExam("This type of png is not supported!\n");
        }
    }).catch(error => {
        hideLoadingModal()
        console.error('Hiba történt:', error);
    });
}

function showErrorMessageInExam(message) {
    var errorMessageElement = document.getElementById('error-message-exam-modal-content');
    errorMessageElement.innerText = message;
    errorMessageElement.style.color = 'red';
}

var examImageContainer = document.querySelector(".exam-image-image-container");
var examImageModal = document.querySelector(".exam-image-modal");
var examImageCloseButton = document.querySelector(".exam-image-close-button");

const examImageContainers = document.querySelectorAll('.exam-image-image-container');
const examImageModals = document.querySelectorAll('.exam-image-modal');
const examImageZooms = document.querySelectorAll('.zoom-icon');
const examNameIds = document.querySelectorAll('#exam-name-id');

for (let i = 0; i < examImageContainers.length; i++) {
    const examImageContainer = examImageContainers[i];
    const examImageModal = examImageModals[i];
    const examImageZoom = examImageZooms[i];
    const examNameId = examNameIds[i];

    examNameId.addEventListener("click", function() {
            if (examImageModal.id.includes("ItIsNotAnImage")) {
                getExamImgPdf(examImageModal.id.split("-")[1]);
                examImageModal.style.cursor = "pointer";
            } else {
                examImageModal.style.display = "block";
                examImageModal.style.cursor = "default";
            }
    });

    examImageZoom.addEventListener("click", function() {
        if (examImageModal.id.includes("ItIsNotAnImage")) {
            getExamImgPdf(examImageModal.id.split("-")[1]);
            examImageModal.style.cursor = "pointer";
        } else {
            examImageModal.style.display = "block";
            examImageModal.style.cursor = "default";
        }
    });

    window.addEventListener("click", function(event) {
        if (event.target === examImageModal) {
            examImageModal.style.display = "none";
        }
    });
}

function sendLikeOrDislike(examId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    const sseUrl = "/sse/sendLikeOrDislikeForExam";
    const url = `/resources/examExamples/${action}?examId=${examId}`;
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
                    body: JSON.stringify({message: `${action}:${examId}`}),
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

function sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(examId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    const sseUrl = "/sse/sendLikeOrDislikeForExam";

    const url = `/resources/examExamples/${action}?examId=${examId}`;
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
                    body: JSON.stringify({message: `${action}:${examId}`}),
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

function setLikeOrDislikeStatusToActiveOrInactive(examId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    const url = `/resources/examExamples/${action}?examId=${examId}`;
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
    const examId = rowId.replace('exam-row-', '');
    if (likeButton.classList.contains('like-button-link-active')) {
        sendLikeOrDislike(examId, 'revokelike');

        likeButton.classList.remove('like-button-link-active');
        setLikeOrDislikeStatusToActiveOrInactive(examId, 'setLikeToInactive');
    } else {
        const activeDislikeButton = document.querySelector(`#${rowId} .dislike-button-link.dislike-button-link-active`);
        if (activeDislikeButton) {
            activeDislikeButton.classList.remove('dislike-button-link-active');
            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(examId, 'likeExamAndRevokeDislike');
            likeButton.classList.add('like-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(examId, 'setLikeToActiveAndDislikeToInactive');
        } else {
            sendLikeOrDislike(examId, 'like');
            likeButton.classList.add('like-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(examId, 'setLikeToActive');
        }
    }
}

function handleDislikeButtonClick(dislikeButton) {
    const rowId = dislikeButton.closest('tr').id;
    const examId = rowId.replace('exam-row-', '');

    if (dislikeButton.classList.contains('dislike-button-link-active')) {
        sendLikeOrDislike(examId, 'revokedislike');
        dislikeButton.classList.remove('dislike-button-link-active');
        setLikeOrDislikeStatusToActiveOrInactive(examId, 'setDislikeToInactive');
    } else {
        const activeLikeButton = document.querySelector(`#${rowId} .like-button-link.like-button-link-active`);
        if (activeLikeButton) {
            activeLikeButton.classList.remove('like-button-link-active');

            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(examId, 'dislikeExamAndRevokeLike');
            dislikeButton.classList.add('dislike-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(examId, 'setDislikeToActiveAndLikeToInactive');
        } else {
            sendLikeOrDislike(examId, 'dislike');
            dislikeButton.classList.add('dislike-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(examId, 'setDislikeToActive');
        }
    }}

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
    eventSource.addEventListener('LikeOrDislikeExam', function (event) {
        const data = JSON.parse(event.data);
        const rowId = data.rowId;
        const likeCountElement = document.querySelector(`#exam-row-${rowId} #likeButton`);
        const dislikeCountElement = document.querySelector(`#exam-row-${rowId} #dislikeButton`);

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
})

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
    for (let i = 0; i < likeAndDislikeStatuses.length; i++) {
        const likeAndDislikeData = likeAndDislikeStatuses[i];
        const examId = likeAndDislikeData.resourceId;
        const like = likeAndDislikeData.like;
        const dislike = likeAndDislikeData.dislike;

        const likeCountElement = document.querySelector(`#exam-row-${examId} .like-button-link`);
        const dislikeCountElement = document.querySelector(`#exam-row-${examId} .dislike-button-link`);

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
            examimages = data.examimagesandid;
            console.log(examimages);
            handlerexmaimages();
        })
        .catch(error => {
            console.error('Error:', error);
        })
});

function handlerexmaimages() {
    for (let i = 0; i < examimages.length; i++) {
        const examsData = examimages[i];
        const image = examsData[0];
        const examId = examsData[1];
        var modal = document.getElementById('myModal-' + examId);
        var modalImg = document.getElementById('modalImg-' + examId);

        if(modal !== null && modalImg !== null) {
            if (image !== null ) {
                modalImg.src = 'data:image/jpeg;base64,' + image;
            } else {
                modal.id = modal.id + '-' +'ItIsNotAnImage';
            }
        }
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

let originalRows = [];

function saveOriginalRows() {
    const table = document.querySelector(".link-table");
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

// function searchTable() {
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
//
//     checkboxes.forEach((checkbox) => {
//         selectedValues.push(checkbox.value);
//     });
//
//     const table = document.getElementById('dataTable');
//     const rows = table.getElementsByTagName('tr');
//     const filteredRows = [];
//
//     for (let i = 1; i < rows.length; i++) {
//         const cells = rows[i].getElementsByTagName('td');
//         let nameFound = false;
//         let topicFound = false;
//
//         const cell = cells[1];
//
//         if (cell) {
//             const cellText = cell.textContent || cell.innerText;
//             if (cellText.toUpperCase().indexOf(filter) > -1) {
//                 nameFound = true;
//             }
//         }
//
//         const topicCell = cells[2];
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
//     filteredRows.forEach(row => {
//         const examImageContainer = row.querySelector('.exam-image-image-container');
//         const examImageModal = row.querySelector('.exam-image-modal');
//         const examId = row.id.split('-')[1];
//
//         examImageContainer.addEventListener("click", function() {
//             if (examImageModal.id.includes("ItIsNotAnImage")) {
//                 getExamImgPdf(examImageModal.id.split("-")[1]);
//             } else {
//                 examImageModal.style.display = "block";
//                 examImageModal.style.cursor = "default";
//             }
//
//             var modalImg = document.getElementById('modalImg-' + examId);
//             modalImg.src = 'data:image/jpeg;base64,' + examimages.find(data => data[1] === examId)[0];
//         });
//
//         window.addEventListener("click", function(event) {
//             if (event.target === examImageModal) {
//                 examImageModal.style.display = "none";
//             }
//         });
//     });
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
//
//     document.getElementById("next-page-button").addEventListener("click", () => {
//         if (currentPage < Math.ceil(filteredRows.length / rowsPerPage)) {
//             setPage(currentPage + 1);
//         }
//         else{
//             setPage(1);
//         }
//     });
//
//     document.getElementById("prev-page-button").addEventListener("click", () => {
//         if (currentPage > 1) {
//             setPage(currentPage - 1);
//         }
//         else    {
//             setPage(Math.ceil(filteredRows.length / rowsPerPage));
//         }
//     });
// }

document.getElementById('search-button').addEventListener('click', () => {
    searchInExamExamples();
});

function searchInExamExamples() {
    showLoadingModal();

    const searchinput = document.getElementById('filter-input');
    const filter = searchinput.value.trim().toLowerCase();
    const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');
    const selectedValues = Array.from(checkboxes).map(checkbox => checkbox.value.trim().toUpperCase());
    const queryString = 'filter=' + encodeURIComponent(filter) + '&selectedTopics=' + encodeURIComponent(selectedValues.join(','));
    fetch('/resources/examExamples/filtered?' + queryString, {
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
        window.location.href = '/resources/examExamples/filtered?' + queryString;
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

document.getElementById('filter-input').addEventListener('input', listSuggestions);

let examPDF = [];
function getExamImgPdf(examId) {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    showLoadingModal()
    const url = `/resources/examExamples/getexampdfbyid?examId=${examId}`;

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
                const examExamplesFile = data[i][1];
                examPDF.push({
                    id: id,
                    examExamplesFile: examExamplesFile
                });
            }
            hideLoadingModal()
            handlerexamPDFs();
        })
        .catch(error => {
            hideLoadingModal()
            console.error('Error:', error);
        });
}

function handlerexamPDFs() {
    for (let i = 0; i < examPDF.length; i++) {
        const examExamplesData = examPDF[i];
        const PDF = examExamplesData.examExamplesFile;
        const examId = examExamplesData.id;
        console.log("PDF ADAT:" + PDF)
        console.log("Exam ID:" + examId)

        var binaryPDF = atob(PDF);
        var arrayPDF = new Uint8Array(binaryPDF.length);
        for (var j = 0; j < binaryPDF.length; j++) {
            arrayPDF[j] = binaryPDF.charCodeAt(j);
        }

        var pdfBlob = new Blob([arrayPDF], { type: 'application/pdf;charset=utf-8' });
        var blobUrl = URL.createObjectURL(pdfBlob);

        var modal = document.getElementById('myExModal-' + examId);
        var modalPDF = document.getElementById('modalExPDF-' + examId);
        var modalPDFMobile = document.getElementById('modalExPDFMobile-' + examId);
        var closeModalBtn = document.getElementById('closeModalExBtn-' + examId);
        console.log("Igen123: " + modalPDF);
        console.log("Igen124: " + modal);

        if (isMobileOrTabletScreen()) {
            modalPDFMobile.href = blobUrl;
            modalPDFMobile.click();
        } else {
            modal.style.display = 'block';
            modalPDF.src = blobUrl;
            modalPDF.addEventListener("click", function(event) {
                if (event.target.tagName !== 'IFRAME') {
                    event.stopPropagation();
                }
            });

            modal.addEventListener("click", function(event) {
                if (event.target !== modalPDF && event.target !== modalPDFMobile) {
                    event.stopPropagation();
                }
            });

            closeModalBtn.addEventListener("click", function() {
                modal.style.display = 'none';
            });
        }
    }
    examPDF = [];
}

function isMobileOrTabletScreen() {
    return window.innerWidth <= 1024;
}

// MODIFY
function saveModifiedExamExamplesDataToServer(examId) {
    var data = [];

    const fileNameId = "examExampleName-edit-modify-" + examId;
    const fileName = document.getElementById(fileNameId).value;
    const pdfTopicId = "topic-dropbtn-modal-modify-" + examId;
    const pdfTopicElement = document.getElementById(pdfTopicId);
    const pdfTopicValue = pdfTopicElement.innerText;
    console.log("pdfFileName: " + fileName);
    console.log("pdfTopic: " + pdfTopicValue);
    console.log("examId: " + examId);

    data.push({
        name: fileName,
        topic: pdfTopicValue,
        exam_id: examId
    });

    if ( fileName === "" || pdfTopicValue === "") {
        showErrorMessageInExam("Please fill out all fields!");
    } else {
        sendModifiedExamExamplesDataToServer(data);
    }
}

function sendModifiedExamExamplesDataToServer(data) {
    showLoadingModal()
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var formData = new FormData();
    formData.append("name", data[0].name);
    formData.append("topic", data[0].topic);
    formData.append("exam_id", data[0].exam_id);

    fetch('/resources/examExamples/modifyExamExamples', {
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
            showErrorMessageInExam("The file size exceeds the maximum limit of 6 MB!");
        }
    }).catch(error => {
        hideLoadingModal()
        console.error('An error occurred:', error);
    });
}

//DELETE
function deleteExamExamplesData(examId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    showLoadingModal()
    fetch(`/resources/examExamples/deleteExamExamples?exam_id=${examId}`, {
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