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
            mentorbutton.classList.add('selected');
            menteebutton.classList.remove('selected');

        } else {
            if (mentorbutton.classList.contains('active')) {
                mentorbutton.classList.remove('active');
            }
            menteebutton.classList.add('active');
            document.querySelector('.mentor-side').style.backgroundColor = 'white';
            document.querySelector('.mentee-side').style.backgroundColor = 'rgb(22, 175, 132)';
            document.querySelector('.mentee-side').style.color = 'white';
            document.querySelector('.mentor-side').style.color = 'rgb(22, 175, 132)';
            menteebutton.classList.add('selected');
            mentorbutton.classList.remove('selected');
        }
    }
    toggleFieldsAvailability();
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




// itt attol fugg melyik active ugyebar arra az URL-re kell iranyitson
function searchUsers(){
    var menteebutton =  document.querySelector('.mentee-side');
    var mentorbutton =  document.querySelector('.mentor-side');
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    if (menteebutton.classList.contains('active')) {
        showLoadingModal();
        window.location.href = "/myGroup/mentees";
    } else if (mentorbutton.classList.contains('active')) {
        showLoadingModal();
        window.location.href = "/myGroup/mentors";
    } else {
        //Meg kell jelenitse hogy valamelyiket muszaj kivalasztani
        // es esetleg azt a mentor/mentee div-nek adjon piros keretet
        alert("Hibat kell megjelenitsen, ")
        //window.location.href = "/myGroup/myCustomGroup";
    }


}


// profilkepek megjelenitese
let profileimages = [];
document.addEventListener('DOMContentLoaded', function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var currentURL = window.location.href;
    console.log("Current URL: ", currentURL);
    var MainInformationPage = document.getElementById("information-box");
    var mentorbutton =  document.querySelector('.mentor-side');
    var menteebutton =  document.querySelector('.mentee-side');

    if (currentURL.includes("myGroup/mentees")) {
        MainInformationPage.style.display = "none";

        var rating = document.querySelectorAll('.rating');
        rating.forEach(function (element) {
           element.style.display = "none";
        });
        // lekerni a mentee-k profilkepeit
        console.log("Helloka menteek");
        fetch("/myGroup/mentees/getallmenteeprofileimage", {
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
            //console.log(profileimages);
            handlereprofileimages();
        }).catch(error => {
            console.log("Error: ", error);
        });
    } else if (currentURL.includes("myGroup/mentors")) {
        MainInformationPage.style.display = "none";

        // lekerni a mentorok profilkepeit
        console.log("Helloka mentorok");
        fetch("/myGroup/mentors/getallmentorprofileimage", {
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
            //console.log(profileimages);
            handlereprofileimages();
        }).catch(error => {
            console.log("Error: ", error);
        });
    }
});

function handlereprofileimages() {

    //console.log("Profile images hossz : ", profileimages.length);
    for (let i = 0; i < profileimages.length; i++) {
        //console.log("Profile images : ", profileimages[i]);
        const profilData = profileimages[i];
        const profilImage = profilData[0];
        const profilId = profilData[1];

        var profileImg  = document.getElementById('myGroupProfileImg-' + profilId);
        if (profilImage != null) {
            profileImg.src = 'data:image/jpeg;base64,' + profilImage;
        }

    }
}

function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block"; // Megjelenítjük a modal ablakot
}

function isRoleSelected() {
    const mentorButton = document.querySelector('.mentor-side');
    const menteeButton = document.querySelector('.mentee-side');
    return mentorButton.classList.contains('selected') || menteeButton.classList.contains('selected');
}

function isTopicSelected() {
    const checkboxes = document.querySelectorAll('.topic-box input[type="checkbox"]');
    for (const checkbox of checkboxes) {
        if (checkbox.checked) {
            return true;
        }
    }
    return false;
}

function toggleFieldsAvailability() {
    const topicBox = document.querySelector('.topic-box');
    const dropdownButton = document.querySelector('.dropdown-myGroup');
    const searchButton = document.querySelector('.search-button');

    const roleSelected = isRoleSelected();
    const topicSelected = isTopicSelected();

    if (roleSelected) {
        topicBox.style.opacity = '1';
        topicBox.style.pointerEvents = 'auto';
        searchButton.style.opacity = '1';
        searchButton.style.pointerEvents = 'auto';
    } else {
        topicBox.style.opacity = '0.5';
        topicBox.style.pointerEvents = 'none';
        searchButton.style.opacity = '0.5';
        searchButton.style.pointerEvents = 'none';
    }

    if (topicSelected) {
        dropdownButton.style.opacity = '1';
        dropdownButton.style.pointerEvents = 'auto';
    } else {
        dropdownButton.style.opacity = '0.5';
        dropdownButton.style.pointerEvents = 'none';
    }
}

document.querySelectorAll('.role-button button').forEach(button => {
    button.addEventListener('click', toggleFieldsAvailability);
});

document.querySelectorAll('.topic-box input[type="checkbox"]').forEach(checkbox => {
    checkbox.addEventListener('change', toggleFieldsAvailability);
});

toggleFieldsAvailability();


