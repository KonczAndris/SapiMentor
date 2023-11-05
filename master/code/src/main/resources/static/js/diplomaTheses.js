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

    console.log(data);
    sendDiplomaThesesDataToServer(data);
}

function sendDiplomaThesesDataToServer(data) {
    // ide kell majd hogy behozza a toltokepernyot

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

        if (data === "Success") {
            location.reload();
        } else if(data === "Too large"){
            alert("The file is too large!");
        }
    }).catch(error => {
        // ezt is andrisnak
        //hideLoadingModal(); // Elrejtjük a modal ablakot
        console.error('Hiba történt:', error);
    });

}

//NEW
// Get the elements
var pdfImageContainer = document.querySelector(".pdf-image-image-container");
var pdfImageModal = document.querySelector(".pdf-image-modal");
// Show the modal on hover
pdfImageContainer.addEventListener("click", function() {
    pdfImageModal.style.display = "block";
    pdfImageModal.style.cursor = "default";
});

// Hide the modal when clicking outside of it
window.addEventListener("click", function(event) {
    if (event.target == pdfImageModal) {
        pdfImageModal.style.display = "none";
    }
});

