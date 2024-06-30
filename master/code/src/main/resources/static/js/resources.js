'use strict';
let stompClient = null;
let IdForUserInLinksPage = null;

function connectToWebSocketForLinksPage() {
    var elementToGetLinksUserId = document.querySelector('[id^="userIdForLinksPage-"]');
    IdForUserInLinksPage = elementToGetLinksUserId.id.split("-")[1];

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
    }
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnectedForLinksPage, onErrorInLinksPage);
}

function onConnectedForLinksPage() {
    stompClient.subscribe(`/user/${IdForUserInLinksPage}/queue/messages`, onMessageReceivedNotificationInLinksPage)
}

function onErrorInLinksPage(error) {

}

async function onMessageReceivedNotificationInLinksPage(payload) {
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

var linkCounter = 0;
function addLinkRow() {
    try {
        var selectedLink = document.getElementById("resourceLink-edit").value;
        var selectedName = document.getElementById("resourceName-edit").value;
        var selectedTopic = document.getElementById("topic-selected-modal").value;

        if (selectedTopic !== "" && selectedLink !== "" && selectedName !== "") {
            var tableContainer = document.querySelector(".table-container table tbody");
            var row = document.createElement("tr");
            var linkNumber = document.createElement("td");
            linkNumber.textContent = linkCounter;

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

            var likeButton = document.createElement("button");
            likeButton.textContent = "Like";
            likeButton.className = "like-button-link";

            var dislikeButton = document.createElement("button");
            dislikeButton.textContent = "Dislike";
            dislikeButton.className = "dislike-button-link";

            linkButtons.appendChild(likeButton);
            linkButtons.appendChild(dislikeButton);

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
    modal.style.display = "block";
}

function hideLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "none";
}

function sendResourcesDataToServer(data) {
    showLoadingModal();

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
        }
    }).then(data => {

        if (data === "Success") {
            location.reload();
        } else if (data === "NotSafe") {
            showErrorMessage("The link is not safe!");
        } else {
            showErrorMessage("An error occurred." +
                " Please try again later.");
        }

    }).catch(error => {
            hideLoadingModal();
            console.error('Hiba történt:', error);
    });
}

function showErrorMessage(message) {
    var errorMessageElement = document.getElementById('error-message-modal-content');
    errorMessageElement.innerText = message;
}

function sendDataToServer(data) {
    var resourceDataItems = JSON.stringify(data);
    document.getElementById("resourceDataItems").value = resourceDataItems;
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
                fetch(sseUrl, {
                    method: 'POST',
                    body: JSON.stringify({message: `${action}:${resourceId}`}),
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
function sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(resourceId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    const sseUrl = "/sse/sendLikeOrDislike";

    const url = `/resources/${action}?resourceId=${resourceId}`;
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
                    body: JSON.stringify({message: `${action}:${resourceId}`}),
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
function setLikeOrDislikeStatusToActiveOrInactive(resourceId, action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    const url = `/resources/${action}?resourceId=${resourceId}`;
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
    const resourceId = rowId.replace('resource-row-', '');
    if (likeButton.classList.contains('like-button-link-active')) {
        sendLikeOrDislike(resourceId, 'revokelike');

        likeButton.classList.remove('like-button-link-active');
        setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToInactive');
    } else {
        const activeDislikeButton = document.querySelector(`#${rowId} .dislike-button-link.dislike-button-link-active`);
        if (activeDislikeButton) {
            activeDislikeButton.classList.remove('dislike-button-link-active');
            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(resourceId, 'likeResourceAndRevokeDislike');
            likeButton.classList.add('like-button-link-active');

            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActiveAndDislikeToInactive');
        } else {
            sendLikeOrDislike(resourceId, 'like');
            likeButton.classList.add('like-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setLikeToActive');
        }
    }
}

function handleDislikeButtonClick(dislikeButton) {
    const rowId = dislikeButton.closest('tr').id;
    const resourceId = rowId.replace('resource-row-', '');
    if (dislikeButton.classList.contains('dislike-button-link-active')) {
        // Dislike visszavonása
        sendLikeOrDislike(resourceId, 'revokedislike');

        dislikeButton.classList.remove('dislike-button-link-active');
        setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToInactive');
    } else {
        const activeLikeButton = document.querySelector(`#${rowId} .like-button-link.like-button-link-active`);
        if (activeLikeButton) {
            activeLikeButton.classList.remove('like-button-link-active');
            sendLikeAndRevokeDislikeOrDislikeAndRevokeLike(resourceId, 'dislikeResourceAndRevokeLike');
            dislikeButton.classList.add('dislike-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActiveAndLikeToInactive');
        } else {
            sendLikeOrDislike(resourceId, 'dislike');
            dislikeButton.classList.add('dislike-button-link-active');
            setLikeOrDislikeStatusToActiveOrInactive(resourceId, 'setDislikeToActive');
        }
    }
}

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

    eventSource.addEventListener('LikeOrDislikeLink', function (event) {
        const data = JSON.parse(event.data);
        const rowId = data.rowId;
        const likeCountElement = document.querySelector(`#resource-row-${rowId} #likeButton`);
        const dislikeCountElement = document.querySelector(`#resource-row-${rowId} #dislikeButton`);
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
        })
        .catch(error => {
            console.error('Error:', error);
        })
});

function handleLikeAndDislikeStatuses() {
    for (let i = 0; i < likeAndDislikeStatuses.length; i++) {
        const likeAndDislikeData = likeAndDislikeStatuses[i];
        const resourceId = likeAndDislikeData.resourceId;
        const like = likeAndDislikeData.like;
        const dislike = likeAndDislikeData.dislike;
        const likeCountElement = document.querySelector(`#resource-row-${resourceId} .like-button-link`);
        const dislikeCountElement = document.querySelector(`#resource-row-${resourceId} .dislike-button-link`);
        if (like === 1) {
            likeCountElement.classList.add('like-button-link-active');
        } else if (dislike === 1) {
            dislikeCountElement.classList.add('dislike-button-link-active');
        }
    }
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
//
//     const rowsPerPage = 20;
//     let currentPage = 1;
//
//         function updatePageCounter() {
//             const pageCount = Math.ceil(filteredRows.length / rowsPerPage);
//             document.getElementById("page-counter").textContent = `Page ${currentPage} of ${pageCount}`;
//         }
//
//         function showRowsForCurrentPage() {
//             const startIdx = (currentPage - 1) * rowsPerPage;
//             const endIdx = Math.min(startIdx + rowsPerPage, rows.length);
//
//             filteredRows.forEach((row, index) => {
//                 if (index >= startIdx && index < endIdx) {
//                     row.style.display = "";
//                 } else {
//                     row.style.display = "none";
//                 }
//             });
//         }
//
//         function setPage(page) {
//             currentPage = page;
//             showRowsForCurrentPage();
//             updatePageCounter();
//         }
//
//         updatePageCounter();
//         showRowsForCurrentPage();
//
//
//         document.getElementById("next-page-button").addEventListener("click", () => {
//             if (currentPage < Math.ceil(filteredRows.length / rowsPerPage)) {
//                 setPage(currentPage + 1);
//             }
//             else{
//                 setPage(1);
//             }
//         });
//
//         document.getElementById("prev-page-button").addEventListener("click", () => {
//             if (currentPage > 1) {
//                 setPage(currentPage - 1);
//             }
//             else    {
//                 setPage(Math.ceil(filteredRows.length / rowsPerPage));
//             }
//         });
// }

document.getElementById('search-button').addEventListener('click', () => {
    searchInResourcesTable();
});

function searchInResourcesTable() {
    showLoadingModal();

    const searchinput = document.getElementById('filter-input');
    const filter = searchinput.value.toLowerCase();
    const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');
    const selectedValues = Array.from(checkboxes).map(checkbox => checkbox.value.trim().toUpperCase());

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