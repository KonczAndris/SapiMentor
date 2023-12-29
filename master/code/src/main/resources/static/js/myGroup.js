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

// document.addEventListener("DOMContentLoaded", function () {
//     var selectedSkillsByTopic = {};  // Objektum, amely tárolja a kiválasztott skill-eket témánként
//
//     // Get all topic checkboxes
//     var topicCheckboxes = document.querySelectorAll('.topic-checkbox');
//
//     // Add click event listener to each topic checkbox
//     topicCheckboxes.forEach(function (checkbox) {
//         checkbox.addEventListener('click', function () {
//             var selectedTopic = checkbox.value;
//
//             // Check if the topic is checked or unchecked
//             var isChecked = checkbox.hasAttribute('checked') || checkbox.checked;
//             if (isChecked) {
//                 // Initialize the selectedSkillsByTopic entry if not exists
//                 if (!selectedSkillsByTopic[selectedTopic]) {
//                     selectedSkillsByTopic[selectedTopic] = [];
//                 }
//
//                 // Show skill checkboxes related to the selected topic
//                 var relatedSkillCheckboxes = document.querySelectorAll('#skill-checkbox-' + selectedTopic);
//                 relatedSkillCheckboxes.forEach(function (relatedCheckbox) {
//                     relatedCheckbox.style.display = 'block';
//                 });
//             } else {
//                 // Hide skill checkboxes related to the unselected topic
//                 var relatedSkillCheckboxes = document.querySelectorAll('#skill-checkbox-' + selectedTopic);
//                 relatedSkillCheckboxes.forEach(function (relatedCheckbox) {
//                     relatedCheckbox.style.display = 'none';
//                 });
//
//                 // Remove the entry for the unselected topic
//                 delete selectedSkillsByTopic[selectedTopic];
//             }
//
//             // Toggle visibility of the skills based on the current selected topics
//             updateSkillsVisibility(selectedSkillsByTopic);
//         });
//     });
//
//     // Function to toggle visibility of skills based on selected topics
//     function updateSkillsVisibility(selectedSkillsByTopic) {
//         // Hide all skill checkboxes
//         var skillCheckboxes = document.querySelectorAll('.skill-checkbox');
//         skillCheckboxes.forEach(function (skillCheckbox) {
//             skillCheckbox.style.display = 'none';
//         });
//
//         // Show skill checkboxes related to the selected topics
//         Object.keys(selectedSkillsByTopic).forEach(function (topic) {
//             var relatedSkillCheckboxes = document.querySelectorAll('#skill-checkbox-' + topic);
//             relatedSkillCheckboxes.forEach(function (relatedCheckbox) {
//                 relatedCheckbox.style.display = 'block';
//             });
//         });
//     }
// });





document.addEventListener("DOMContentLoaded", function () {
    var selectedSkillsByTopic = {};  // Objektum, amely tárolja a kiválasztott skill-eket témánként

    // Get all topic checkboxes
    var topicCheckboxes = document.querySelectorAll('.topic-checkbox');

    // Add click event listener to each topic checkbox
    topicCheckboxes.forEach(function (checkbox) {
        checkbox.addEventListener('click', function () {
            var selectedTopic = checkbox.value;

            // Check if the topic is checked or unchecked
            var isChecked = checkbox.hasAttribute('checked') || checkbox.checked;
            if (isChecked) {
                // Initialize the selectedSkillsByTopic entry if not exists
                if (!selectedSkillsByTopic[selectedTopic]) {
                    selectedSkillsByTopic[selectedTopic] = [];
                }

                updateSkillsVisibility(selectedSkillsByTopic);
                var skillCheckboxes = document.querySelectorAll('.skill-checkbox');
                skillCheckboxes.forEach(function (skillCheckbox) {
                    // Add click event listener to each skill checkbox
                    skillCheckbox.addEventListener('click', function () {
                        var selectedtopic = skillCheckbox.parentNode;
                        console.log("Selected topic1: ", selectedtopic);
                        var igen = selectedtopic.id;
                        console.log("Selected topic2: ", igen.split("-")[2]);
                        if (igen.split("-")[2] === selectedTopic){
                            var selectedSkill = skillCheckbox.value;
                            var isChecked = skillCheckbox.hasAttribute('checked') || skillCheckbox.checked;

                            if (isChecked) {
                                // Add the selected skill to the corresponding topic
                                if (!selectedSkillsByTopic[selectedTopic].includes(selectedSkill)) {
                                    selectedSkillsByTopic[selectedTopic].push(selectedSkill);
                                }
                            } else {
                                // Remove the selected skill from the corresponding topic
                                var skillIndex = selectedSkillsByTopic[selectedTopic].indexOf(selectedSkill);
                                if (skillIndex !== -1) {
                                    selectedSkillsByTopic[selectedTopic].splice(skillIndex, 1);
                                }
                            }
                        }


                        // Toggle visibility of the skills based on the current selected topics
                        updateSkillsVisibility(selectedSkillsByTopic);
                    });
                });
            } else {
                // Remove the entry for the unselected topic
                delete selectedSkillsByTopic[selectedTopic];
                updateSkillsVisibility(selectedSkillsByTopic);
            }
        });
    });

    // Function to toggle visibility of skills based on selected topics
    function updateSkillsVisibility(selectedSkillsByTopic) {
        // Hide all skill checkboxes
        var skillCheckboxes = document.querySelectorAll('.skill-checkboxx');
        skillCheckboxes.forEach(function (skillCheckbox) {
            skillCheckbox.style.display = 'none';
        });

        // Show skill checkboxes related to the selected topics
        Object.keys(selectedSkillsByTopic).forEach(function (topic) {
            var relatedSkillCheckboxes = document.querySelectorAll('#skill-checkboxx-' + topic);
            relatedSkillCheckboxes.forEach(function (relatedCheckbox) {
                relatedCheckbox.style.display = 'block';
            });
        });
    }

    function sendSearchDataToServer() {
        // Convert the data to JSON
        var searchData = {
            selectedSkillsByTopic: selectedSkillsByTopic
        };
        //console.log("Igen12: " + Object.keys(selectedSkillsByTopic));

        // igy megkapom a kivalasztott skill-eket es topicokat is
        // ha nincs kivalasztva semmi akkor csak siman a mentee/mentor oldalra iranyitson
        // ha van kivalasztva valami akkor pedig a kereses oldalra iranyitson
        if (Object.keys(selectedSkillsByTopic).length === 0) {
            console.log("Nincs kiválasztva semmi");
        } else {
            console.log("Kiválasztott skill-ek: ");
            Object.keys(selectedSkillsByTopic).forEach(function (topic) {
                console.log("Téma: " + topic);
                console.log("Kijelölt skill-ek: " + selectedSkillsByTopic[topic].join(", "));
            });

            console.log("Adatok: ", searchData);
        }



        // Send a POST request to the server
        // fetch('/your-server-search-endpoint', {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json',
        //     },
        //     body: JSON.stringify(searchData),
        // })
        //     .then(response => response.json())
        //     .then(data => {
        //         // Handle the response from the server if needed
        //         console.log(data);
        //     })
        //     .catch(error => {
        //         console.error('Error sending search data to the server:', error);
        //     });
    }

    var searchButton = document.getElementById('search-button');
    searchButton.addEventListener('click', function () {
        // Send selected data to the server when the "Search" button is clicked
        sendSearchDataToServer();
    });
});








