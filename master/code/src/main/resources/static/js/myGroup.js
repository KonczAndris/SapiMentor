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
        var mentorbutton =  document.querySelector('.mentor-side');
        var menteebutton =  document.querySelector('.mentee-side');
        if (side === 'left') {
            if (menteebutton.classList.contains('active')) {
                menteebutton.classList.remove('active');
            }
            mentorbutton.classList.add('active');
            document.querySelector('.mentor-side').style.backgroundColor = 'rgb(22, 175, 132)';
            document.querySelector('.mentor-side').style.color = 'white';
            document.querySelector('.mentee-side').style.backgroundColor = 'white';
            document.querySelector('.mentee-side').style.color = 'rgb(22, 175, 132)';

        } else {
            if (mentorbutton.classList.contains('active')) {
                mentorbutton.classList.remove('active');
            }
            menteebutton.classList.add('active');
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


// profilkepek megjelenitese
let profileimages = [];
document.addEventListener('DOMContentLoaded', function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    console.log("Helloka");
    fetch("/myGroup/getallprofileimage", {
        method: "GET",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Something went wrong');
        }
    }).then(data => {
        profileimages = data.profileimagesandid;
        console.log(profileimages);
        handlereprofileimages();
    }).catch(error => {
        console.log("Error: ", error);
    });

});

function handlereprofileimages() {
    for (let i = 0; i < profileimages.length; i++) {
        const profilData = profileimages[i];
        const profilImage = profilData[0];
        const profilId = profilData[1];

        var profileImg  = document.getElementById('myGroupProfileImg-' + profilId);
        if (profilImage != null) {
            profileImg.src = 'data:image/jpeg;base64,' + profilImage;
        }

    }
}


// document.addEventListener('DOMContentLoaded', function() {
//
//
//     const checkboxContainers = document.querySelectorAll('.checkbox-container');
//
//     checkboxContainers.forEach(container => {
//         const label = container.querySelector('label');
//         const checkbox = label.querySelector('input[class="dropdown-checkbox"]');
//         const labelText = label.textContent.trim();
//
//         const checkboxElement = document.createElement('div');
//         checkboxElement.classList.add('checkbox-item');
//
//         checkboxElement.innerHTML = `
//             <label>
//                 <input type="checkbox" value="${checkbox.value}">
//                 <span class="checkmark"></span>
//                 ${labelText}
//             </label>
//         `;
//
//         document.getElementById('checkboxDropdown-myGroup').appendChild(checkboxElement);
//     });
// });

document.addEventListener('DOMContentLoaded', function() {
    const checkboxContainers = document.querySelectorAll('.checkbox-container');

    checkboxContainers.forEach(container => {
        const label = container.querySelector('label');
        const checkbox = label.querySelector('input[class="dropdown-checkbox"]');
        const labelText = label.textContent.trim();

        const checkboxElement = document.createElement('div');
        checkboxElement.classList.add('checkbox-item');

        const checkboxValue = checkbox ? checkbox.value : ''; // Ellenőrizzük, hogy a checkbox változó létezik-e
        checkboxElement.innerHTML = `
            <label>
                <input type="checkbox" value="${checkboxValue}">
                <span class="checkmark"></span>
                ${labelText}
            </label>
        `;

        document.getElementById('checkboxDropdown-myGroup').appendChild(checkboxElement);
    });
});



