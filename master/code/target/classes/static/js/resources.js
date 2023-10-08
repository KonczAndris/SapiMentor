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
    var tableRows = document.querySelectorAll(".table-container table tbody tr");
    var data = [];

    tableRows.forEach(function (row) {
        var linkNumber = row.querySelector("td:first-child").textContent;
        var linkName = row.querySelector("td:nth-child(2)").textContent;
        var linkTopic = row.querySelector("td:nth-child(3)").textContent;

        // Assuming you have an array for storing likes and dislikes for each row
        var linkLikes = row.querySelector("td:nth-child(4)").textContent;

        // Push the data into the 'data' array
        data.push({
            linkNumber: linkNumber,
            linkName: linkName,
            linkTopic: linkTopic,
            linkLikes: linkLikes,
        });
    });

    console.log(data);
    sendResourcesDataToServer(data);
}


function sendResourcesDataToServer(data) {
    var profileTopicsDataItems = JSON.stringify(data);
    document.getElementById("resourceDataItems").value = profileTopicsDataItems;
    console.log(profileTopicsDataItems);
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
