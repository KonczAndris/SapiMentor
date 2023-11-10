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
    //console.log(data);
    sendExamsDataToServer(data);
}

function sendExamsDataToServer(data) {
    // ide kell majd hogy behozza a toltokepernyot

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

        if (data === "Success") {
            location.reload();
        } else if(data === "Too large"){
            alert("The file is too large!");
        } else if(data === "Wrong type"){
            alert("This type png is not supported!");
        }
    }).catch(error => {
        // ezt is andrisnak
        //hideLoadingModal(); // Elrejtjük a modal ablakot
        console.error('Hiba történt:', error);
    });
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
// lekerni az osszes like es dislike allasat az adatbazisbol
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
        const examId = likeAndDislikeData.resourceId;
        const like = likeAndDislikeData.like;
        const dislike = likeAndDislikeData.dislike;

        // Itt kezeld az adatokat vagy végezz velük bármit, amit szeretnél
        //console.log(`Resource ID: ${resourceId}, Like: ${like}, Dislike: ${dislike}`);
        const likeCountElement = document.querySelector(`#exam-row-${examId} .like-button-link`);
        const dislikeCountElement = document.querySelector(`#exam-row-${examId} .dislike-button-link`);
        //console.log(likeCountElement);
        //console.log(dislikeCountElement);
        if (like === 1) {
            likeCountElement.classList.add('like-button-link-active');
        } else {
            dislikeCountElement.classList.add('dislike-button-link-active');
        }

    }
}

// function clearBrowserCache() {
//     if ('caches' in window) {
//         // Törlés az összes gyorsítótári tárhelyből
//         caches.keys().then(function(cacheNames) {
//             cacheNames.forEach(function(cacheName) {
//                 caches.delete(cacheName);
//             });
//         });
//     }
//
//     // Törlés az összes böngésző cache-tól
//     window.location.reload(true);
// }


