document.getElementById("favorites").addEventListener("click", function () {
    window.location.href = "/myGroup/favorites";
});

// Function to show the filter container
function showFilterContainer() {
    var searchDiv = document.getElementById("searchDiv");
    var searchButton = document.getElementById("myFilterBtn");

    if (searchDiv.style.display === "none" || searchDiv.style.display === "") {
        searchDiv.style.display = "flex";
        searchButton.style.display = "none"; // Hide the button when left div is displayed
    } else {
        searchDiv.style.display = "none";
        searchButton.style.display = "block"; // Show the button when left div is not displayed
    }
}

function cancelFilterWindow() {
    var searchDiv = document.getElementById("searchDiv");
    var searchButton = document.getElementById("myFilterBtn");

    if (searchDiv.style.display === "flex" || searchDiv.style.display === "") {
        searchDiv.style.display = "none";
        searchButton.style.display = "block";
    } else {
        searchDiv.style.display = "flex";
        searchButton.style.display = "none";
    }
}

// Function to handle window resize
function handleWindowResize() {
    var searchDiv = document.getElementById("searchDiv");
    var searchButton = document.getElementById("myFilterBtn");
    if (window.innerWidth <= 600) {
        searchDiv.style.display = "none";
        searchButton.style.display = "block";
    } else {
        searchDiv.style.display = "flex";
        searchButton.style.display = "none";
    }
}

// Add event listener for window resize
var isWindowSizeBelowThreshold = window.innerWidth <= 600;

window.addEventListener("resize", function() {
    if (window.innerWidth <= 600 && !isWindowSizeBelowThreshold) {
        isWindowSizeBelowThreshold = true;
        handleWindowResize();
    } else if (window.innerWidth > 600 && isWindowSizeBelowThreshold) {
        isWindowSizeBelowThreshold = false;
        handleWindowResize();
    }
});

// Initial check on page load
handleWindowResize();

let selectedSide = '';

function selectSide(side) {
    if (selectedSide === side) {
        selectedSide = '';
    } else {
        selectedSide = side;
        if (side === 'left') {
            document.querySelector('.mentor-side').style.backgroundColor = 'rgb(22, 175, 132)';
            document.querySelector('.mentor-side').style.color = 'white';
            document.querySelector('.mentee-side').style.backgroundColor = 'white';
            document.querySelector('.mentee-side').style.color = 'rgb(22, 175, 132)';
        } else {
            document.querySelector('.mentor-side').style.backgroundColor = 'white';
            document.querySelector('.mentee-side').style.backgroundColor = 'rgb(22, 175, 132)';
            document.querySelector('.mentee-side').style.color = 'white';
            document.querySelector('.mentor-side').style.color = 'rgb(22, 175, 132)';
        }
    }
}

function toggleDropdown() {
    document.getElementById("checkboxDropdown-myGroup").classList.toggle("show");
}

window.onclick = function(event) {
    if (!event.target.matches('.dropbtn-myGroup')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
};

document.addEventListener('DOMContentLoaded', function() {
    const checkboxContainers = document.querySelectorAll('.checkbox-container');

    checkboxContainers.forEach(container => {
        const label = container.querySelector('label');
        const checkbox = label.querySelector('input[class="dropdown-checkbox"]');
        const labelText = label.textContent.trim();

        const checkboxElement = document.createElement('div');
        checkboxElement.classList.add('checkbox-item');

        checkboxElement.innerHTML = `
            <label>
                <input type="checkbox" value="${checkbox.value}">
                <span class="checkmark"></span>
                ${labelText}
            </label>
        `;

        document.getElementById('checkboxDropdown-myGroup').appendChild(checkboxElement);
    });
});
