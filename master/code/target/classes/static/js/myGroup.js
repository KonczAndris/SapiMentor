'use strict';

let stompClient = null;
let IdForUserInMyGroupPage = null;
function connectToWebSocketForMyGroupPage() {
    var elementToGetMyGroupUserId = document.querySelector('[id^="userIdForMyGroupPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInMyGroupPage = elementToGetMyGroupUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
    }else {
        var socket = new SockJS('/wss');
    }

    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForMyGroupPage, onErrorInMyGroupPage);
}

function onConnectedForMyGroupPage() {
    stompClient.subscribe(`/user/${IdForUserInMyGroupPage}/queue/messages`, onMessageReceivedNotificationInMyGroupPage)
}

function onErrorInMyGroupPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
}

async function onMessageReceivedNotificationInMyGroupPage(payload) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    document.querySelectorAll('.nbr-msg').forEach(item => {
        item.classList.remove('hiddenMsg');
        item.textContent = '';
    });
}

// ez azert hogy a valosideju ertesiteseket is megkapjuk
// es ha frissitunk vagy ha csak siman ugy kapunk ertesitest hogy nem vagyunk
// belepve akkor is megkapjuk az ertesitest
document.addEventListener('DOMContentLoaded', () => {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    connectToWebSocketForMyGroupPage();

    var elementToGetMyGroupProfileUserId = document.querySelector('[id^="userIdForMyGroupPage-"]');
    var mygroupasprofileUserId = elementToGetMyGroupProfileUserId.id.split("-")[1];

    fetch(`/myGroup/getMyGroupProfileNotificationStatus?userId=${mygroupasprofileUserId}`, {
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




// Ez azert kell hogy az oldal frissitese nelkul is egybol mindenkinel megjelenjen a comment
$(document).ready(function() {
    // SSE
    var urlEndpoint = "/sse/subscribe"

    //itt az eventsource a szerver oldalon lévő végpontot figyeli
    const eventSource = new EventSource(urlEndpoint);

    eventSource.onopen = function (event) {
        console.log('SSE connection opened.');
    };

    eventSource.onerror = function (event) {
        console.error('SSE error:', event);
    };

    if(!window.location.href.includes("profile")) {
        eventSource.addEventListener('UserComment', function (event) {

            const userCommentData = JSON.parse(event.data);
            const ratingUserId = userCommentData.ratingUserId;
            //console.log("Az ID: " + ratingUserId);
            //console.log("Received UserComment:", userCommentData);
            const userProfileModal = document.getElementById('profileMainModal_' + userCommentData.ratedUserId);
            //console.log("User profile modal: ", userProfileModal);

            if(userProfileModal !== null) {
                // console.log("User profile modal found");
                var commentSection = document.createElement('div');
                commentSection.classList = 'comment-section'
                commentSection.id = 'comment-section-' + ratingUserId;
                //console.log("A Comment-Section: " + commentSection);

                var commentRow = document.createElement('div');
                commentRow.classList = 'comment-row';
                commentSection.appendChild(commentRow);

                // Profile image
                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + ratingUserId;
                if (userCommentData.userImage == null || userCommentData.userImage === "") {
                    commentProfileImg.src = "/img/anonym.jpg";
                } else {
                    commentProfileImg.src = 'data:image/jpeg;base64,' + userCommentData.userImage;
                }
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList = 'comment-profile-image';
                commentRow.appendChild(commentProfileImg);
                //

                var commentInfos = document.createElement('div');
                commentInfos.classList = 'comment-infos';
                commentRow.appendChild(commentInfos);

                // User Name
                var commentUsernameDiv = document.createElement('div');
                commentInfos.appendChild(commentUsernameDiv);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = userCommentData.firstName + " " + userCommentData.lastName;
                commentUsernameDiv.appendChild(commentUsername);
                //

                // Date
                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList = 'comment-label-date';
                commentLabelDate.id = 'comment-label-date-' + ratingUserId;
                commentLabelDate.textContent = userCommentData.date;
                commentInfos.appendChild(commentLabelDate);

                var commentRating = document.createElement('div');
                commentRating.classList = 'comment-rating';
                commentRating.id = 'comment-rating-' + ratingUserId;
                commentInfos.appendChild(commentRating);

                //Star 5
                var commentStar5 = document.createElement('input');
                commentStar5.type = 'radio';
                commentStar5.id = 'comment-star5_' + ratingUserId;
                commentStar5.name = 'rating';
                commentStar5.value = '5';
                commentStar5.checked = userCommentData.score >= 4.5;
                commentStar5.disabled = true;
                commentRating.appendChild(commentStar5);

                var commentStar5Label = document.createElement('label');
                commentStar5Label.htmlFor = 'comment-star5_' + ratingUserId;
                if (userCommentData.score >= 4.5) {
                    commentStar5Label.style.color = 'gold';
                } else {
                    commentStar5Label.style.color = '';
                }
                commentStar5Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar5Label);

                //

                //Star 4
                var commentStar4 = document.createElement('input');
                commentStar4.type = 'radio';
                commentStar4.id = 'comment-star4_' + ratingUserId;
                commentStar4.name = 'rating';
                commentStar4.value = '4';
                commentStar4.checked = userCommentData.score >= 3.5;
                commentStar4.disabled = true;
                commentRating.appendChild(commentStar4);

                var commentStar4Label = document.createElement('label');
                commentStar4Label.htmlFor = 'comment-star4_' + ratingUserId;
                if (userCommentData.score >= 3.5) {
                    commentStar4Label.style.color = 'gold';
                } else {
                    commentStar4Label.style.color = '';
                }
                commentStar4Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar4Label);

                //

                //Star 3
                var commentStar3 = document.createElement('input');
                commentStar3.type = 'radio';
                commentStar3.id = 'comment-star3_' + ratingUserId;
                commentStar3.name = 'rating';
                commentStar3.value = '3';
                commentStar3.checked = userCommentData.score >= 2.5;
                commentStar3.disabled = true;
                commentRating.appendChild(commentStar3);

                var commentStar3Label = document.createElement('label');
                commentStar3Label.htmlFor = 'comment-star3_' + ratingUserId;
                if (userCommentData.score >= 2.5) {
                    commentStar3Label.style.color = 'gold';
                } else {
                    commentStar3Label.style.color = '';
                }
                commentStar3Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar3Label);

                //

                //Star 2
                var commentStar2 = document.createElement('input');
                commentStar2.type = 'radio';
                commentStar2.id = 'comment-star2_' + ratingUserId;
                commentStar2.name = 'rating';
                commentStar2.value = '2';
                commentStar2.checked = userCommentData.score >= 1.5;
                commentStar2.disabled = true;
                commentRating.appendChild(commentStar2);

                var commentStar2Label = document.createElement('label');
                commentStar2Label.htmlFor = 'comment-star2_' + ratingUserId;
                if (userCommentData.score >= 1.5) {
                    commentStar2Label.style.color = 'gold';
                } else {
                    commentStar2Label.style.color = '';
                }
                commentStar2Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar2Label);

                //

                //Star 1
                var commentStar1 = document.createElement('input');
                commentStar1.type = 'radio';
                commentStar1.id = 'comment-star1_' + ratingUserId;
                commentStar1.name = 'rating';
                commentStar1.value = '1';
                commentStar1.checked = userCommentData.score >= 0.5;
                commentStar1.disabled = true;
                commentRating.appendChild(commentStar1);

                var commentStar1Label = document.createElement('label');
                commentStar1Label.htmlFor = 'comment-star1_' + ratingUserId;
                if (userCommentData.score >= 0.5) {
                    commentStar1Label.style.color = 'gold';
                } else {
                    commentStar1Label.style.color = '';
                }
                commentStar1Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar1Label);

                //

                var commentContent = document.createElement('div');
                commentContent.classList = 'comment-content';
                commentSection.appendChild(commentContent);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList = 'comment-label-text';
                commentLabelText.id = 'comment-label-text-' + ratingUserId;
                commentLabelText.textContent = userCommentData.comment;
                commentContent.appendChild(commentLabelText);

                var commentContainer = document.getElementById("commentContainer");
                commentContainer.appendChild(commentSection);
            }
        });
    } else {
        console.log("Profile oldalon vagyunk");
        eventSource.addEventListener('UserComment', function (event) {

            const userCommentData = JSON.parse(event.data);
            const ratingUserId = userCommentData.ratingUserId;
            //console.log("Az ID: " + ratingUserId);
            //console.log("Received UserComment:", userCommentData);
            const userProfileContainer = document.getElementById('profile-container-Id_' + userCommentData.ratedUserId);
            //console.log("User profile modal: ", userProfileContainer);

            if(userProfileContainer !== null) {
                // console.log("User profile modal found");
                var commentSection = document.createElement('div');
                commentSection.classList = 'comment-section'
                commentSection.id = 'comment-section-' + ratingUserId;
                //console.log("A Comment-Section: " + commentSection);

                var commentRow = document.createElement('div');
                commentRow.classList = 'comment-row';
                commentSection.appendChild(commentRow);

                // Profile image
                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + ratingUserId;
                if (userCommentData.userImage == null || userCommentData.userImage === "") {
                    commentProfileImg.src = "/img/anonym.jpg";
                } else {
                    commentProfileImg.src = 'data:image/jpeg;base64,' + userCommentData.userImage;
                }
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList = 'comment-profile-image';
                commentRow.appendChild(commentProfileImg);
                //

                var commentInfos = document.createElement('div');
                commentInfos.classList = 'comment-infos';
                commentRow.appendChild(commentInfos);

                // User Name
                var commentUsernameDiv = document.createElement('div');
                commentInfos.appendChild(commentUsernameDiv);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = userCommentData.firstName + " " + userCommentData.lastName;
                commentUsernameDiv.appendChild(commentUsername);
                //

                // Date
                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList = 'comment-label-date';
                commentLabelDate.id = 'comment-label-date-' + ratingUserId;
                commentLabelDate.textContent = userCommentData.date;
                commentInfos.appendChild(commentLabelDate);

                var commentRating = document.createElement('div');
                commentRating.classList = 'comment-rating';
                commentRating.id = 'comment-rating-' + ratingUserId;
                commentInfos.appendChild(commentRating);

                //Star 5
                var commentStar5 = document.createElement('input');
                commentStar5.type = 'radio';
                commentStar5.id = 'comment-star5_' + ratingUserId;
                commentStar5.name = 'rating';
                commentStar5.value = '5';
                commentStar5.checked = userCommentData.score >= 4.5;
                commentStar5.disabled = true;
                commentRating.appendChild(commentStar5);

                var commentStar5Label = document.createElement('label');
                commentStar5Label.htmlFor = 'comment-star5_' + ratingUserId;
                if (userCommentData.score >= 4.5) {
                    commentStar5Label.style.color = 'gold';
                } else {
                    commentStar5Label.style.color = '';
                }
                commentStar5Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar5Label);

                //

                //Star 4
                var commentStar4 = document.createElement('input');
                commentStar4.type = 'radio';
                commentStar4.id = 'comment-star4_' + ratingUserId;
                commentStar4.name = 'rating';
                commentStar4.value = '4';
                commentStar4.checked = userCommentData.score >= 3.5;
                commentStar4.disabled = true;
                commentRating.appendChild(commentStar4);

                var commentStar4Label = document.createElement('label');
                commentStar4Label.htmlFor = 'comment-star4_' + ratingUserId;
                if (userCommentData.score >= 3.5) {
                    commentStar4Label.style.color = 'gold';
                } else {
                    commentStar4Label.style.color = '';
                }
                commentStar4Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar4Label);

                //

                //Star 3
                var commentStar3 = document.createElement('input');
                commentStar3.type = 'radio';
                commentStar3.id = 'comment-star3_' + ratingUserId;
                commentStar3.name = 'rating';
                commentStar3.value = '3';
                commentStar3.checked = userCommentData.score >= 2.5;
                commentStar3.disabled = true;
                commentRating.appendChild(commentStar3);

                var commentStar3Label = document.createElement('label');
                commentStar3Label.htmlFor = 'comment-star3_' + ratingUserId;
                if (userCommentData.score >= 2.5) {
                    commentStar3Label.style.color = 'gold';
                } else {
                    commentStar3Label.style.color = '';
                }
                commentStar3Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar3Label);

                //

                //Star 2
                var commentStar2 = document.createElement('input');
                commentStar2.type = 'radio';
                commentStar2.id = 'comment-star2_' + ratingUserId;
                commentStar2.name = 'rating';
                commentStar2.value = '2';
                commentStar2.checked = userCommentData.score >= 1.5;
                commentStar2.disabled = true;
                commentRating.appendChild(commentStar2);

                var commentStar2Label = document.createElement('label');
                commentStar2Label.htmlFor = 'comment-star2_' + ratingUserId;
                if (userCommentData.score >= 1.5) {
                    commentStar2Label.style.color = 'gold';
                } else {
                    commentStar2Label.style.color = '';
                }
                commentStar2Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar2Label);

                //

                //Star 1
                var commentStar1 = document.createElement('input');
                commentStar1.type = 'radio';
                commentStar1.id = 'comment-star1_' + ratingUserId;
                commentStar1.name = 'rating';
                commentStar1.value = '1';
                commentStar1.checked = userCommentData.score >= 0.5;
                commentStar1.disabled = true;
                commentRating.appendChild(commentStar1);

                var commentStar1Label = document.createElement('label');
                commentStar1Label.htmlFor = 'comment-star1_' + ratingUserId;
                if (userCommentData.score >= 0.5) {
                    commentStar1Label.style.color = 'gold';
                } else {
                    commentStar1Label.style.color = '';
                }
                commentStar1Label.innerHTML = '&#9733';
                commentRating.appendChild(commentStar1Label);

                //

                var commentContent = document.createElement('div');
                commentContent.classList = 'comment-content';
                commentSection.appendChild(commentContent);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList = 'comment-label-text';
                commentLabelText.id = 'comment-label-text-' + ratingUserId;
                commentLabelText.textContent = userCommentData.comment;
                commentContent.appendChild(commentLabelText);

                var commentContainer = document.getElementById("commentContainer");
                commentContainer.appendChild(commentSection);
            }
        });
    }


});




if(document.getElementById("favorites") !== null) {
    document.getElementById("favorites").addEventListener("click", function () {
        window.location.href = "/myGroup/favorites";
    });
}


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

function toggleDropdownMyGroup() {
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
    //console.log("Current URL: ", currentURL);
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
        //console.log("Helloka menteek");
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
            //console.log("Igen: " + profileimages);
            handlereprofileimages();
        }).catch(error => {
            console.log("Error: ", error);
        });
    } else if (currentURL.includes("myGroup/mentors")) {
        MainInformationPage.style.display = "none";

        // lekerni a mentorok profilkepeit
        //console.log("Helloka mentorok");
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
            //console.log("Igen: " + profileimages);
            handlereprofileimages();
        }).catch(error => {
            console.log("Error: ", error);
        });
    } else if (currentURL.includes("myGroup/mentees/filtered")) {
        MainInformationPage.style.display = "none";

        var rating = document.querySelectorAll('.rating');
        rating.forEach(function (element) {
            element.style.display = "none";
        });
        // lekerni a mentee-k profilkepeit
        //console.log("Helloka menteek");
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
            //console.log("Igen: " + profileimages);
            handlereprofileimages();
        }).catch(error => {
            console.log("Error: ", error);
        });
    } else if (currentURL.includes("myGroup/mentors/filtered")) {
        MainInformationPage.style.display = "none";

        // lekerni a mentorok profilkepeit
        //console.log("Helloka mentorok");
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
            //console.log("Igen: " + profileimages);
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

        //console.log("Profil image: ", profilImage);
        //console.log("Profil id: ", profilId);

        var profileImg  = document.getElementById('myGroupProfileImg-' + profilId);
        //console.log("Profile image: ", profileImg);
        if (profilImage != null && profileImg != null ) {
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



document.addEventListener("DOMContentLoaded", function () {
    document.querySelector('.profile-modal-background').style.display = 'none';

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
                        //console.log("Selected topic1: ", selectedtopic);
                        var igen = selectedtopic.id;
                        //console.log("Selected topic2: ", igen.split("-")[2]);
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
        var menteebutton =  document.querySelector('.mentee-side');
        var mentorbutton =  document.querySelector('.mentor-side');
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        // igy megkapom a kivalasztott skill-eket es topicokat is
        // ha nincs kivalasztva semmi akkor csak siman a mentee/mentor oldalra iranyitson
        // ha van kivalasztva valami akkor pedig a kereses oldalra iranyitson
        if (Object.keys(selectedSkillsByTopic).length === 0) {
            console.log("Nincs kiválasztva semmi");


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
        } else {
            // console.log("Kiválasztott skill-ek: ");
            // Object.keys(selectedSkillsByTopic).forEach(function (topic) {
            //     console.log("Téma: " + topic);
            //     console.log("Kijelölt skill-ek: " + selectedSkillsByTopic[topic].join(", "));
            // });

            //console.log("Adatok: ", searchData);

            if (menteebutton.classList.contains('active')) {
                const queryString = Object.keys(selectedSkillsByTopic).map(topic => {
                    const skills = selectedSkillsByTopic[topic].map(skill => encodeURIComponent(skill)).join(',');
                    return `${encodeURIComponent(topic)}=${skills}`;
                }).join('&');
                const url = `/myGroup/mentees/filtered?${queryString}`;
                showLoadingModal();
                window.location.href = url;

            } else if (mentorbutton.classList.contains('active')) {
                const queryString = Object.keys(selectedSkillsByTopic).map(topic => {
                    const skills = selectedSkillsByTopic[topic].map(skill => encodeURIComponent(skill)).join(',');
                    return `${encodeURIComponent(topic)}=${skills}`;
                }).join('&');
                const url = `/myGroup/mentors/filtered?${queryString}`;
                showLoadingModal();
                window.location.href = url;
            }

        }
    }

    var searchButton = document.getElementById('search-button');
    searchButton.addEventListener('click', function () {
        // Send selected data to the server when the "Search" button is clicked
        sendSearchDataToServer();
    });
});


// Get the button element by its class name
const profileButtons = document.querySelectorAll('.profile-button-watch');
const profileModal = document.getElementById('profileModal');


// Function to display the modal
function displayModal() {
    var currentURL = window.location.href;
    if (profileModal !== null) {
        profileModal.style.display = 'block';
    }

    if (currentURL.includes("myGroup/mentees")) {
        var rating = document.querySelector('.rating.modal-rating-star');
        var interaction_buttons = document.querySelector('.interaction-buttons');
        var comments = document.getElementById('showCommentsButton');

        //console.log("Interaction:" + interaction_buttons);

        if(rating !== null && comments !== null){
            rating.style.display = "none";
            comments.style.display = "none";
        } else {
            interaction_buttons.style.display = "none";
        }

        if (interaction_buttons !== null) {
            interaction_buttons.style.display = "none";
        }
        

    }
}

function closeModal() {
    if (profileModal !== null ) {
        profileModal.style.display = 'none';
    }
}

function specialCloseModal() {
    if (profileModal !== null) {
        location.reload();
        profileModal.style.display = 'none';
    }
}

let profileimagesforSelectedUsers = [];
// Event listener for each profile button
profileButtons.forEach(button => {
    // button.addEventListener('click', displayModal);
    button.addEventListener('click', function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
       //console.log("Button clicked", button.id);
       var userId = button.id.split("-")[1];
       //console.log("User id: ", userId);
       var url = '/myGroup/getSelectedUserDetails?selectedUserId=' + userId;

       fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': token
            }
       }).then(response => {
           if (response.ok) {
               return response.text();
           } else {
               throw new Error('Something went wrong');
           }
       }).then(data => {
           if (data !== 'error') {
               //console.log("Sikeres lekerdezes");
               //console.log("Data: ", data);

               document.getElementById('profileModal').innerHTML = data;
               const closeBtn = document.querySelector('.close-profile-modal');
               if (closeBtn !== null){
                   closeBtn.addEventListener('click', closeModal);
               }
               displayModal();
           } else {
                throw new Error('Something went wrong');
           }

       }).catch(error => {
              console.log("Error: ", error);
       });

        var url2 = '/myGroup/getSelectedUsersImages?selectedUserId=' + userId;
        fetch(url2, {
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
            //console.log("Data: ", data);
            profileimagesforSelectedUsers = data.selectedUserImages;
            //console.log("Igen: " + profileimagesforSelectedUsers);
            setInterval(() => {
                handlereselectedimages();
            }, 1000); // 2000 milliszekundum = 2 másodperc
        }).catch(error => {
            console.log("Error: ", error);
        });

    });
});


function handlereselectedimages() {

    //console.log("Profile images hossz : ", profileimages.length);
    for (let i = 0; i < profileimagesforSelectedUsers.length; i++) {
        //console.log("Profile images : ", profileimages[i]);
        const commentprofileData = profileimagesforSelectedUsers[i];
        const commentprofileImage = commentprofileData[0];
        const commentprofileId = commentprofileData[1];

        //console.log("Profil image: ", commentprofileImage);
        //console.log("Profil id: ", commentprofileId);

         var commentedProfileImg  = document.getElementById('commentProfileImg-' + commentprofileId);
        //console.log("Profile image div: ", commentedProfileImg);
        if (commentprofileImage != null && commentedProfileImg != null ) {
            commentedProfileImg.src = 'data:image/jpeg;base64,' + commentprofileImage;
        }

    }
}


// Function to close the modal when clicking outside the modal content
window.addEventListener('click', function(event) {
    if (event.target === profileModal) {
        profileModal.style.display = 'none';
    }
});



function showHeartIcons() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var favoriteIds = document.querySelectorAll('.favorite-id');

    favoriteIds.forEach(function(favoriteId) {
        var idValue = parseInt(favoriteId.textContent);

        console.log("Favorite id: ", idValue);

        var parentCell = favoriteId.parentElement;
        //console.log("Parent cell: ", parentCell);
        var checkedHeart = parentCell.querySelector('.checked-heart');
        var uncheckedHeart = parentCell.querySelector('.unchecked-heart');

        if (idValue === 0 || isNaN(idValue)) {
            uncheckedHeart.classList.add('active');
            checkedHeart.classList.add('inactive');
            uncheckedHeart.style.display = 'inline';
            checkedHeart.style.display = 'none';
        } else if (idValue === 1) {
            uncheckedHeart.classList.add('inactive');
            checkedHeart.classList.add('active');
            uncheckedHeart.style.display = 'none';
            checkedHeart.style.display = 'inline';
        }


        var parentCellId = parentCell.id.split("-")[2];
        //console.log("Parent cell id: ", parentCellId);
        var favoriteButton = document.getElementById('favoriteButton-' + parentCellId);
        //console.log("Favorite button: ", favoriteButton);

        favoriteButton.addEventListener('click', function() {
           //console.log("Favorite " + parentCellId + " button clicked");
           if (uncheckedHeart.classList.contains('active')) {
               //console.log("Hozzaadom a kedvencekhez");
               uncheckedHeart.style.display = 'none';
               checkedHeart.style.display = 'inline';
               uncheckedHeart.classList.remove('active');
               uncheckedHeart.classList.add('inactive');
               checkedHeart.classList.remove('inactive');
               checkedHeart.classList.add('active');
               var urlforsave = '/myGroup/saveFavorite?favoriteUserId=' + parentCellId;

               fetch(urlforsave, {
                   method: 'POST',
                   headers: {
                       'Content-Type': 'application/x-www-form-urlencoded',
                       'X-CSRF-TOKEN': token
                   }
               }).then(response => {
                     if (response.ok) {
                          return response.text();
                     } else {
                          throw new Error('Something went wrong');
                     }
               }).then(data => {
                        if (data === 'ok') {
                            console.log("Sikeres mentes");
                        } else {
                            throw new Error('Something went wrong');
                        }
               }).catch(error => {
                        console.log("Error: ", error);
               });

           }

           else if (checkedHeart.classList.contains('active')) {
               const sureButton = document.getElementById('sureButton-' + parentCellId);
               const cancelButton = document.getElementById('cancelButton-' + parentCellId);
               const profileButtonContainerSure = document.getElementById('profile-button-container-sure-' + parentCellId);
               const profileButtonContainer = document.getElementById('profile-button-container-' + parentCellId);
               if (profileButtonContainerSure) {
                   profileButtonContainerSure.style.display = 'flex';
                   profileButtonContainer.style.display = 'none';
               }

               sureButton.addEventListener('click', () => {
                   console.log("Kiveszem a kedvencek kozul");
                   uncheckedHeart.style.display = 'inline';
                   checkedHeart.style.display = 'none';
                   checkedHeart.classList.remove('active');
                   checkedHeart.classList.add('inactive');
                   uncheckedHeart.classList.remove('inactive');
                   uncheckedHeart.classList.add('active');

                   var urlforrevoke = '/myGroup/revokeFavorite?favoriteUserId=' + parentCellId;

                   fetch(urlforrevoke, {
                       method: 'POST',
                       headers: {
                           'Content-Type': 'application/x-www-form-urlencoded',
                           'X-CSRF-TOKEN': token
                       }
                   }).then(response => {
                       if (response.ok) {
                           return response.text();
                       } else {
                           throw new Error('Something went wrong');
                       }
                   }).then(data => {
                       if (data === 'ok') {
                           console.log("Sikeres visszavonas");
                       } else {
                           throw new Error('Something went wrong');
                       }
                   }).catch(error => {
                       console.log("Error: ", error);
                   });

                   // Hide the sure-modal after the action is performed
                   const profileButtonContainerSure = document.getElementById('profile-button-container-sure-' + parentCellId);
                   if (profileButtonContainerSure) {
                       profileButtonContainerSure.style.display = 'none';
                       profileButtonContainer.style.display = 'flex';
                   }
               });

               cancelButton.addEventListener('click', () => {
                   // Hide the sure-modal without performing any action
                   if (profileButtonContainerSure) {
                       profileButtonContainerSure.style.display = 'none';
                       profileButtonContainer.style.display = 'flex';
                   }
               });
           }


        });
    });
}

// Hívás a függvényre, például az oldal betöltésekor vagy más eseményre
showHeartIcons();

function showRateSection() {
    document.getElementById('ratingSection').style.display = 'block';
    var isChecked = false;
    var ratedRating = document.querySelectorAll('input[name="selectrating"]');
    console.log("Rated rating: ", ratedRating);

    ratedRating.forEach(function (element) {
        if (element.checked){
            isChecked = true;
        }
    });
    console.log("Is checked: ", isChecked);

    var ratedButton = document.getElementById('save-rating');

    if (isChecked) {
        ratedRating.forEach(function (element) {
            element.disabled = true;
        });
        ratedButton.disabled = true;
    } else {
        ratedButton.disabled = false;
        ratedRating.forEach(function (element) {
            element.disabled = false;
        });
    }
}

document.getElementById('commentInput').addEventListener('focus', function () {
    this.selectionStart = this.selectionEnd = this.value.length;
});

// SSE a kommentekhez




function saveRating() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");


    // Az input mezőkből kiolvasott adatok
    if (document.querySelector('input[name="selectrating"]:checked') === null){
        var errorMessages = document.getElementById('error-for-ratingSection');
        errorMessages.textContent = 'Please select a rating first!';
        errorMessages.style.display = 'block';
        errorMessages.style.color = 'red';
        //console.log("Nincs kivalasztva rating")
    } else {
        var rating = document.querySelector('input[name="selectrating"]:checked').value;
        //console.log("Rating: ", rating);
        var comment = document.getElementById('commentInput').value;


       // console.log("Comment: ", comment);

        var profileModalElement = document.querySelector('[id*="profileMainModal_"]');
        //console.log("Igen: ", profileModalElement);
        var userId = profileModalElement.id.split("_")[1];
        //console.log("User id: ", userId);

        // Az aktuális dátum létrehozása
        var currentDate = new Date();
        //console.log('Jelenlegi_1 dátum:', currentDate);
        // Dátum formázása 2024.01.20. formátumra
        var formattedDate = currentDate.toLocaleDateString('hu-HU', { year: 'numeric', month: '2-digit', day: '2-digit' });

        // Eredmény kiíratása a konzolra
        //console.log('Jelenlegi_2 dátum:', formattedDate);

        const sseUrl = "/sse/sendCommentInMyGroup";

        fetch('/myGroup/saveRating', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': token
            },
            body: JSON.stringify({
                userId: userId,
                score: rating,
                comment: comment,
                date: formattedDate
            })
        }).then(response => {
            if (response.ok) {
                fetch(sseUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': token
                    },
                    body: JSON.stringify({
                        userId: userId,
                        score: rating,
                        comment: comment,
                        date: formattedDate
                    })
                }).then(response => {
                    if (response.ok) {
                        //console.log("Nagyoasfajsofosa");
                        //return response.text();
                    } else {
                        throw new Error('Error in SSE fetch');
                    }
                })

                return response.text();
            } else {
                throw new Error('Something went wrong in rating save');
            }
        }).then(data => {
            if (data === 'ok') {
                console.log("Sikeres mentes");
                document.getElementById('error-for-ratingSection').style.display = 'none';
                document.getElementById('ratingSection').style.display = 'none';
                document.getElementById('rating-button').style.display = 'inline';

                var currentRatingDiv = document.getElementById('modal-rating-star-for-modal');
                console.log("Current rating div: ", currentRatingDiv);
                var checkedInputs = Array.from(currentRatingDiv.querySelectorAll('input[name="rating"]'))
                    .filter(input => input.hasAttribute('checked') );
                console.log("Checked inputs: ", checkedInputs);

                if (checkedInputs.length > 0) {
                    var lastCheckedValue = checkedInputs.length;
                    console.log('Az utolsó kiválasztott érték: ' + lastCheckedValue);
                    console.log('Rating: ' + rating);
                    var floatRating = parseFloat(rating);
                    var floatLastCheckedValue = parseFloat(lastCheckedValue);
                    var avg = (floatLastCheckedValue + floatRating)/2;
                    console.log('Az új átlag érték: ' + avg);


                    // Beállítjuk az új átlag értéknek megfelelően a csillagokat
                    for (var i = 1; i <= 5; i++) {
                        var starInput = currentRatingDiv.querySelector('input[value="' + i + '"]');
                        var starLabel = currentRatingDiv.querySelector('label[for="star' + i + '"]');

                        if (i <= avg) {
                            starInput.checked = true;
                            starLabel.style.color = 'gold';
                        } else {
                            starInput.checked = false;
                            starLabel.style.color = '';
                        }
                    }
                    const closeBtn = document.querySelector('.close-profile-modal');
                    if (closeBtn !== null){
                        closeBtn.addEventListener('click', specialCloseModal);
                    }

                } else {
                    console.log('Nincs kiválasztott érték.');
                }

            } else {
                throw new Error('Something went wrong');
            }
        }).catch(error => {
            console.log("Error: ", error);
        });
    }

}




function cancelRating() {
    document.getElementById('ratingSection').style.display = 'none';
}

function rateWithStars(rating) {
    // Get all the star elements
    var stars = document.querySelectorAll('.rating-rate .modal-rating-star-rate label');

    // Loop through each star and apply the color based on the rating
    for (var i = 0; i < stars.length; i++) {
        if (i + 1 <= rating) {
            // If the star's index is less than or equal to the selected rating, make it gold
            stars[i].style.color = 'gold';
        } else {
            // Otherwise, make it grey
            stars[i].style.color = 'grey';
        }
    }
}

// function showCommentSection1() {
//     console.log("showCommentSection1 called");
//     // Hide all comment sections
//     var commentSections = document.querySelectorAll('.comment-section');
//     commentSections.forEach(function(section) {
//         section.style.display = 'none';
//     });
//
//     // Show the specific comment section with ID 'comment-section-1'
//     var commentSection1 = document.getElementById('comment-section-2');
//     if (commentSection1) {
//         commentSection1.style.display = 'block';
//     }
// }

var currentCommentIndex = 0;

function showNextCommentSection() {
    let commentSections = document.querySelectorAll('.comment-section');
    let currentCommentSection = commentSections[currentCommentIndex];

    // Elrejtjük az aktuális elemet
    if (currentCommentSection) {
        currentCommentSection.classList.remove('active');
    }

    // Növeljük a kiválasztott indexet
    currentCommentIndex++;

    // Megjelenítjük a következő elemet
    let nextCommentSection = commentSections[currentCommentIndex];

    if (nextCommentSection) {
        nextCommentSection.classList.add('active');
    } else {
        // Ha nincs további elem, visszaállunk az alapértelmezett elemre
        currentCommentIndex = 0;
        commentSections[currentCommentIndex].classList.add('active');
    }
}

function showPreviousCommentSection() {
    let commentSections = document.querySelectorAll('.comment-section');
    let currentCommentSection = commentSections[currentCommentIndex];

    // Elrejtjük az aktuális elemet
    if (currentCommentSection) {
        currentCommentSection.classList.remove('active');
    }

    // Csökkentjük a kiválasztott indexet
    currentCommentIndex--;

    // Megjelenítjük az előző elemet
    let previousCommentSection = commentSections[currentCommentIndex];

    if (previousCommentSection) {
        previousCommentSection.classList.add('active');
    } else {
        // Ha nincs előző elem, visszaugrunk az utolsó elemre
        currentCommentIndex = commentSections.length - 1;
        commentSections[currentCommentIndex].classList.add('active');
    }
}

// Az oldal betöltésekor hívódik meg
document.addEventListener('DOMContentLoaded', function () {
    // Csak az első elem kapja meg az active osztályt
    let commentSections = document.querySelectorAll('.comment-section');
    commentSections.forEach(function (section, index) {
        if (index === 0) {
            section.classList.add('active');
        } else {
            section.classList.remove('active');
        }
    });
});

function toggleAndShowNext() {
    toggleComments();
    showNextCommentSection();
}

function toggleComments() {
    var commentsDiv = document.getElementById("comment-section-container");
    var showButton = document.getElementById("showCommentsButton");

    if (commentsDiv.style.display === "none" || commentsDiv.style.display === "") {
        commentsDiv.style.display = "block";
        showButton.style.display = "none";
        let commentSections = document.querySelectorAll('.comment-section');
        commentSections.forEach(function (section, index) {
            if (index === 0) {
                section.classList.add('active');
            } else {
                section.classList.remove('active');
            }
        });
    } else {
        commentsDiv.style.display = "none";
    }
}
