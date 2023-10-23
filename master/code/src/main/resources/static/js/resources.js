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


function toggleFilterDropdown() {
    var dropdownContent = document.getElementById("filter-myDropdown");
    dropdownContent.classList.toggle("active");
}


function closeDropdown(selectedItem) {
    var dropdownContent = document.getElementById("filter-myDropdown");
    dropdownContent.classList.remove("active");
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

function setupResourceModal() {
    var modal = document.getElementById("myResourceModal");

    var btn1 = document.getElementById("upload-upload");
    var span = document.getElementsByClassName("close-resource")[0];
    btn1.onclick = function() {
        modal.style.display = "flex";
    }
    span.onclick = function() {
        modal.style.display = "none";
    }
}


function closeModalOnClickOutside() {
    var modal1 = document.getElementById("myResourceModal");

    window.addEventListener("click", function(event) {
        if (event.target == modal1) {
            modal1.style.display = "none";
        }
    });
}

setupResourceModal();
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
    var modal = document.getElementById("resourceModal");

    var btn1 = document.getElementById("upload-upload");
    var btn2 = document.getElementById("upload-hidden");
    var span = document.getElementsByClassName("close-resource")[0];
    btn1.onclick = function() {
        modal.style.display = "flex";
    }
    btn2.onclick = function() {
        modal.style.display = "flex";
    }
    span.onclick = function() {
        modal.style.display = "none";
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

document.addEventListener("DOMContentLoaded", function () {
    const table = document.querySelector(".link-table");
    const tableBody = table.querySelector("tbody");
    const rows = Array.from(tableBody.querySelectorAll("tr"));
    const rowsPerPage = 8;
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
    });

    document.getElementById("prev-page-button").addEventListener("click", () => {
        if (currentPage > 1) {
            setPage(currentPage - 1);
        }
    });

    const reverseButton = document.getElementById("reverseButton");
    const reverseButton2 = document.getElementById("myReverseBtn");
    reverseButton.addEventListener("click", function () {
        rows.reverse();
        rows.forEach(row => tableBody.removeChild(row));
        rows.forEach(row => tableBody.appendChild(row));
        updatePageCounter();
        showRowsForCurrentPage();
    });
    reverseButton2.addEventListener("click", function () {
        rows.reverse();
        rows.forEach(row => tableBody.removeChild(row));
        rows.forEach(row => tableBody.appendChild(row));
        updatePageCounter();
        showRowsForCurrentPage();
    });
});

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
    // var tableRows = document.querySelectorAll(".table-container table tbody tr");
     var data = [];
    //
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

    var link = document.getElementById("resourceLink-edit").value;
    var linkName = document.getElementById("resourceName-edit").value;
    var linkTopic = document.getElementById("topic-selected-modal").value;

    data.push({
        name: linkName,
        link: link,
        topic_name: linkTopic,
    });


    //console.log(data);
    sendResourcesDataToServer(data);
}

// ezt is andrisnak
// function showLoadingModal() {
//     var modal = document.getElementById("loading-modal");
//     modal.style.display = "block"; // Megjelenítjük a modal ablakot
// }

// ezt is andrisnak
// function hideLoadingModal() {
//     var modal = document.getElementById("loading-modal");
//     modal.style.display = "none"; // Elrejtjük a modal ablakot
// }

// link feltoltese a szerverre (JSON)
function sendResourcesDataToServer(data) {
    // ezt is andrisnak
    //showLoadingModal(); // Megjelenítjük a modal ablakot


    var resourcesUploadDataItems = JSON.stringify(data);
    //document.getElementById("resourceDataItems").value = profileTopicsDataItems;
    //console.log("Adatok: " + resourcesUploadDataItems);
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    fetch('http://localhost:8080/resources/uploadResources', {
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
        // ezt is andrisnak
        //hideLoadingModal(); // Elrejtjük a modal ablakot
        // Kell kezelni a valaszt es megjeleniteni a hibauzeneteket
        console.log(data);
        if (data === "Success") {
            location.reload();
        }
    }).catch(error => {
            // ezt is andrisnak
            //hideLoadingModal(); // Elrejtjük a modal ablakot
            console.error('Hiba történt:', error);
    });
}


function sendDataToServer(data) {
    var resourceDataItems = JSON.stringify(data);
    document.getElementById("resourceDataItems").value = resourceDataItems;
    console.log(resourceDataItems);


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

    // The regular expression checks for at least 5 letters followed by an optional number
    if (!/^[a-zA-Zéáűúőíöü\s'-]{5,}(?:\d)?$/.test(nameValue)) {
        nameInput.classList.add("highlight");
        uploadButton.disabled = true;
        uploadButton.cursor = "not-allowed";
    } else {
        nameInput.classList.remove("highlight");
        uploadButton.disabled = false;
        uploadButton.opacity = 1;
    }
}

$(document).ready(async function () {

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
        const likeCountElement = document.querySelector(`#resource-row-${rowId} #likeButton`);
        const dislikeCountElement = document.querySelector(`#resource-row-${rowId} #dislikeButton`);

        //console.log('Received SSE message:', data.rowId);
        // Itt frissitsd a like/dislike ertekeket a DOM-ban
        //const likeDislikeCountElement = document.querySelector(`#resource-row-${rowId} .like-dislike-count`);
        //console.log(likeDislikeCountElement);
        likeCountElement.textContent = data.like;
        dislikeCountElement.textContent = data.dislike;
    });


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
                        console.log('sendLikeOrDislike Response:', data);
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
                        console.log('sendLikeOrDislike Response:', data);
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

    // function getUserId(callback){
    //     let userData;
    //     fetch("/resources/getUserId")
    //         .then(response => {
    //             if (response.ok) {
    //                 //console.log(response.text());
    //                 return response.text();
    //             } else {
    //                 throw new Error('Request failed');
    //             }
    //         })
    //         .then(data => {
    //             //console.log(data);
    //             callback(data);
    //         })
    //         .catch(error => {
    //             console.error('Error:', error);
    //         })
    // }

    // ide azt hogy allitsuk at a like gomb erteket 0-ra vagyis inaktivra
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
            console.log(data);
        }).catch(error => {
            console.error('Error:', error);
        });
    }


// A like gomb eseménykezelője
    document.querySelectorAll('.like-button-link').forEach(likeButton => {
        likeButton.addEventListener('click', () => {
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
                }

            }
        });
    });

    // ide azt hogy jelenitsuk meg a like gomb statuszanak az erteket a DOM-ban
    // async function getLikeAndDislikeStatus(resourceId, action) {
    //     var token = $("meta[name='_csrf']").attr("content");
    //     var header = $("meta[name='_csrf_header']").attr("content");
    //     try {
    //         const url = `/resources/${action}?resourceId=${resourceId}`;
    //         const response = fetch(url, {
    //             method: 'GET',
    //             headers: {
    //                 'Content-Type': 'application/x-www-form-urlencoded',
    //                 'X-CSRF-TOKEN': token
    //             }
    //         })
    //         if (response.ok) {
    //             const data = await response.json();
    //             return data;
    //         } else {
    //             throw new Error('Request failed');
    //         }
    //
    //     } catch (error) {
    //         console.error('An error occurred:', error);
    //         throw error;
    //     }
    // }

    async function getLikeAndDislikeStatus(resourceId, action) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        try {
            const url = `/resources/${action}?resourceId=${resourceId}`;
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': token
                }
            });

            if (response.ok) {
                const data = await response.text();
                //console.log("Data1: ",data)
                return data;
            } else {
                const errorText = await response.text();
                console.error(`Hiba a kérésben: ${response.status} - ${response.statusText}. Hibaüzenet: ${errorText}`);
                throw new Error(`Hiba a kérésben: ${response.status} - ${response.statusText}`);
            }
        } catch (error) {
            console.error('An error occurred:', error);
            throw error;
        }
    }


    // ide azt hogy jelenitsuk meg a dislike gomb statuszanak az erteket a DOM-ban
    // const likeStatusData = await getLikeAndDislikeStatus(2, 'getLikeStatus');
    // console.log('likeStatusData: ', likeStatusData);
// Az oldal betöltésekor állítsuk vissza az aktív gombok állapotát a helyi tárolóból
    window.addEventListener('load', async () => {
        const likeButtons = document.querySelectorAll('.like-button-link');
        const dislikeButtons = document.querySelectorAll('.dislike-button-link');
        for (const likeButton of likeButtons) {
            const rowId = likeButton.closest('tr').id;
            const resourceId = rowId.replace('resource-row-', '');
            const likeStatusData = await getLikeAndDislikeStatus(resourceId, 'getLikeStatus');
             //console.log('Igen: ',likeStatusData);

             if (likeStatusData === '1') {
                 likeButton.classList.add('like-button-link-active');
             }
            // if (localStorage.getItem(`likeButtonState_${userId}_${resourceId}`) === 'active') {
            //     likeButton.classList.add('like-button-link-active');
            // }
        }

        for (const dislikeButton of dislikeButtons) {
            const rowId = dislikeButton.closest('tr').id;
            const resourceId = rowId.replace('resource-row-', '');
            const dislikeStatusData = await getLikeAndDislikeStatus(resourceId, 'getDislikeStatus');
            if (dislikeStatusData === '1') {
                dislikeButton.classList.add('dislike-button-link-active');
            }
            // if (localStorage.getItem(`dislikeButtonState_${userId}_${resourceId}`) === 'active') {
            //     dislikeButton.classList.add('dislike-button-link-active');
            // }
        }
    });
})



