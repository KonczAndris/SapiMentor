'use strict';

let stompClient = null;
let IdForUserInMyGroupPage = null;
function connectToWebSocketForMyGroupPage() {
    var elementToGetMyGroupUserId = document.querySelector('[id^="userIdForMyGroupPage-"]');
    IdForUserInMyGroupPage = elementToGetMyGroupUserId.id.split("-")[1];

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
        console.log("sima ws-t hasznal");
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
        console.log("wss-t hasznal");
    }

    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForMyGroupPage, onErrorInMyGroupPage);
}

function onConnectedForMyGroupPage() {
    stompClient.subscribe(`/user/${IdForUserInMyGroupPage}/queue/messages`, onMessageReceivedNotificationInMyGroupPage)
}

function onErrorInMyGroupPage(error) {

}

async function onMessageReceivedNotificationInMyGroupPage(payload) {
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

$(document).ready(function() {
    var urlEndpoint = "/sse/subscribe"
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
            const userProfileModal = document.getElementById('profileMainModal_' + userCommentData.ratedUserId);
            if(userProfileModal !== null) {
                var commentSection = document.createElement('div');
                commentSection.classList = 'comment-section'
                commentSection.id = 'comment-section-' + ratingUserId;

                var commentRow = document.createElement('div');
                commentRow.classList = 'comment-row';
                commentSection.appendChild(commentRow);

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

                var commentInfos = document.createElement('div');
                commentInfos.classList = 'comment-infos';
                commentRow.appendChild(commentInfos);

                var commentUsernameDiv = document.createElement('div');
                commentInfos.appendChild(commentUsernameDiv);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = userCommentData.firstName + " " + userCommentData.lastName;
                commentUsernameDiv.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList = 'comment-label-date';
                commentLabelDate.id = 'comment-label-date-' + ratingUserId;
                commentLabelDate.textContent = userCommentData.date;
                commentInfos.appendChild(commentLabelDate);

                var commentRating = document.createElement('div');
                commentRating.classList = 'comment-rating';
                commentRating.id = 'comment-rating-' + ratingUserId;
                commentInfos.appendChild(commentRating);

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
        eventSource.addEventListener('UserComment', function (event) {

            const userCommentData = JSON.parse(event.data);
            const ratingUserId = userCommentData.ratingUserId;

            const userProfileContainer = document.getElementById('profile-container-Id_' + userCommentData.ratedUserId);

            if(userProfileContainer !== null) {
                var commentSection = document.createElement('div');
                commentSection.classList = 'comment-section'
                commentSection.id = 'comment-section-' + ratingUserId;

                var commentRow = document.createElement('div');
                commentRow.classList = 'comment-row';
                commentSection.appendChild(commentRow);

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

                var commentInfos = document.createElement('div');
                commentInfos.classList = 'comment-infos';
                commentRow.appendChild(commentInfos);

                var commentUsernameDiv = document.createElement('div');
                commentInfos.appendChild(commentUsernameDiv);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = userCommentData.firstName + " " + userCommentData.lastName;
                commentUsernameDiv.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList = 'comment-label-date';
                commentLabelDate.id = 'comment-label-date-' + ratingUserId;
                commentLabelDate.textContent = userCommentData.date;
                commentInfos.appendChild(commentLabelDate);

                var commentRating = document.createElement('div');
                commentRating.classList = 'comment-rating';
                commentRating.id = 'comment-rating-' + ratingUserId;
                commentInfos.appendChild(commentRating);

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

function showFilterContainer() {
    var searchDiv = document.getElementById("searchDiv");
    var searchButton = document.getElementById("myFilterBtn");

    if (searchDiv.style.display === "none" || searchDiv.style.display === "") {
        searchDiv.style.display = "flex";
        searchButton.style.display = "none";
    } else {
        searchDiv.style.display = "none";
        searchButton.style.display = "block";
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

function searchUsers(){
    var menteebutton =  document.querySelector('.mentee-side');
    var mentorbutton =  document.querySelector('.mentor-side');
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    if (menteebutton.classList.contains('active')) {
        window.location.href = "/myGroup/mentees";
    } else if (mentorbutton.classList.contains('active')) {
        window.location.href = "/myGroup/mentors";
    }
}

let profileimages = [];
document.addEventListener('DOMContentLoaded', function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var currentURL = window.location.href;
    var MainInformationPage = document.getElementById("information-box");
    var mentorbutton =  document.querySelector('.mentor-side');
    var menteebutton =  document.querySelector('.mentee-side');

    if (currentURL.includes("myGroup/mentees")) {
        MainInformationPage.style.display = "none";

        var rating = document.querySelectorAll('.rating');
        rating.forEach(function (element) {
           element.style.display = "none";
        });
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
            handlereprofileimages();
        }).catch(error => {
            console.log("Error: ", error);
        });
    } else if (currentURL.includes("myGroup/mentors/filtered")) {
        MainInformationPage.style.display = "none";

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
            handlereprofileimages();
        }).catch(error => {
            console.log("Error: ", error);
        });
    }
});

function handlereprofileimages() {
    for (let i = 0; i < profileimages.length; i++) {
        //console.log("Profile images : ", profileimages[i]);
        const profilData = profileimages[i];
        const profilImage = profilData[0];
        const profilId = profilData[1];

        var profileImg  = document.getElementById('myGroupProfileImg-' + profilId);
        if (profilImage != null && profileImg != null ) {
            profileImg.src = 'data:image/jpeg;base64,' + profilImage;
        }
    }
}

function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block";
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

    var selectedSkillsByTopic = {};

    var topicCheckboxes = document.querySelectorAll('.topic-checkbox');

    topicCheckboxes.forEach(function (checkbox) {
        checkbox.addEventListener('click', function () {
            var selectedTopic = checkbox.value;

            var isChecked = checkbox.hasAttribute('checked') || checkbox.checked;
            if (isChecked) {
                if (!selectedSkillsByTopic[selectedTopic]) {
                    selectedSkillsByTopic[selectedTopic] = [];
                }

                updateSkillsVisibility(selectedSkillsByTopic);
                var skillCheckboxes = document.querySelectorAll('.skill-checkbox');
                skillCheckboxes.forEach(function (skillCheckbox) {
                    skillCheckbox.addEventListener('click', function () {
                        var selectedtopic = skillCheckbox.parentNode;
                        var igen = selectedtopic.id;
                        if (igen.split("-")[2] === selectedTopic){
                            var selectedSkill = skillCheckbox.value;
                            var isChecked = skillCheckbox.hasAttribute('checked') || skillCheckbox.checked;

                            if (isChecked) {
                                if (!selectedSkillsByTopic[selectedTopic].includes(selectedSkill)) {
                                    selectedSkillsByTopic[selectedTopic].push(selectedSkill);
                                }
                            } else {
                                var skillIndex = selectedSkillsByTopic[selectedTopic].indexOf(selectedSkill);
                                if (skillIndex !== -1) {
                                    selectedSkillsByTopic[selectedTopic].splice(skillIndex, 1);
                                }
                            }
                        }
                        updateSkillsVisibility(selectedSkillsByTopic);
                    });
                });
            } else {
                delete selectedSkillsByTopic[selectedTopic];
                updateSkillsVisibility(selectedSkillsByTopic);
            }
        });
    });

    function updateSkillsVisibility(selectedSkillsByTopic) {
        var skillCheckboxes = document.querySelectorAll('.skill-checkboxx');
        skillCheckboxes.forEach(function (skillCheckbox) {
            skillCheckbox.style.display = 'none';
        });

        Object.keys(selectedSkillsByTopic).forEach(function (topic) {
            var relatedSkillCheckboxes = document.querySelectorAll('#skill-checkboxx-' + topic);
            relatedSkillCheckboxes.forEach(function (relatedCheckbox) {
                relatedCheckbox.style.display = 'block';
            });
        });
    }

    function sendSearchDataToServer() {
        var searchData = {
            selectedSkillsByTopic: selectedSkillsByTopic
        };
        var menteebutton =  document.querySelector('.mentee-side');
        var mentorbutton =  document.querySelector('.mentor-side');
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        if (Object.keys(selectedSkillsByTopic).length === 0) {

            if (menteebutton.classList.contains('active')) {
                window.location.href = "/myGroup/mentees";
            } else if (mentorbutton.classList.contains('active')) {
                window.location.href = "/myGroup/mentors";
            } else {
                alert("Hibat kell megjelenitsen, ")
            }
        } else {
            if (menteebutton.classList.contains('active')) {
                const queryString = Object.keys(selectedSkillsByTopic).map(topic => {
                    const skills = selectedSkillsByTopic[topic].map(skill => encodeURIComponent(skill)).join(',');
                    return `${encodeURIComponent(topic)}=${skills}`;
                }).join('&');
                const url = `/myGroup/mentees/filtered?${queryString}`;
                window.location.href = url;

            } else if (mentorbutton.classList.contains('active')) {
                const queryString = Object.keys(selectedSkillsByTopic).map(topic => {
                    const skills = selectedSkillsByTopic[topic].map(skill => encodeURIComponent(skill)).join(',');
                    return `${encodeURIComponent(topic)}=${skills}`;
                }).join('&');
                const url = `/myGroup/mentors/filtered?${queryString}`;
                window.location.href = url;
            }
        }
    }

    var searchButton = document.getElementById('search-button');
    searchButton.addEventListener('click', function () {
        sendSearchDataToServer();
    });
});

const profileButtons = document.querySelectorAll('.profile-button-watch');
const profileModal = document.getElementById('profileModal');
const profileImageButtons = document.querySelectorAll('.rounded-image');

function displayModal() {
    var currentURL = window.location.href;
    if (profileModal !== null) {
        profileModal.style.display = 'block';
    }

    if (currentURL.includes("myGroup/mentees")) {
        var rating = document.querySelector('.rating.modal-rating-star');
        var interaction_buttons = document.querySelector('.interaction-buttons');
        var comments = document.getElementById('showCommentsButton');

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
profileButtons.forEach(button => {
    button.addEventListener('click', function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
       var userId = button.id.split("-")[1];
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        }).catch(error => {
            console.log("Error: ", error);
        });

    });
});

profileImageButtons.forEach(button => {
    button.addEventListener('click', function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var userId = button.id.split("-")[1];
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        }).catch(error => {
            console.log("Error: ", error);
        });

    });
});

function handlereselectedimages() {
    for (let i = 0; i < profileimagesforSelectedUsers.length; i++) {
        //console.log("Profile images : ", profileimages[i]);
        const commentprofileData = profileimagesforSelectedUsers[i];
        const commentprofileImage = commentprofileData[0];
        const commentprofileId = commentprofileData[1];

         var commentedProfileImg  = document.getElementById('commentProfileImg-' + commentprofileId);
        if (commentprofileImage != null && commentedProfileImg != null ) {
            commentedProfileImg.src = 'data:image/jpeg;base64,' + commentprofileImage;
        }
    }
}

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
        var favoriteButton = document.getElementById('favoriteButton-' + parentCellId);

        favoriteButton.addEventListener('click', function() {
             if (uncheckedHeart.classList.contains('active')) {

                    if (favoriteButton.classList.contains('notmentee') && window.location.href.includes('mentors')) {
                        alert("You are not a mentee,so you can't add this mentor to your favorites!");
                    } else if (favoriteButton.classList.contains('notmentor') && window.location.href.includes('mentees')) {
                        alert("You are not a mentor,so you can't add this mentee to your favorites!");
                    } else {

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

                        const profileButtonContainerSure = document.getElementById('profile-button-container-sure-' + parentCellId);
                        if (profileButtonContainerSure) {
                            profileButtonContainerSure.style.display = 'none';
                            profileButtonContainer.style.display = 'flex';
                        }
                    });

                    cancelButton.addEventListener('click', () => {
                        if (profileButtonContainerSure) {
                            profileButtonContainerSure.style.display = 'none';
                            profileButtonContainer.style.display = 'flex';
                        }
                    });
                }
        });
    });
}

showHeartIcons();

var favoriteButton = document.getElementById('modal-contact-button');
if (favoriteButton !== null) {
    favoriteButton.addEventListener('click', function() {
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
                window.location.href = '/myGroup/favorites';
            } else {
                throw new Error('Something went wrong');
            }
        }).catch(error => {
            console.log("Error: ", error);
        });
    });
}

function showRateSection() {
    document.getElementById('ratingSection').style.display = 'block';
    var isChecked = false;
    var ratedRating = document.querySelectorAll('input[name="selectrating"]');
    ratedRating.forEach(function (element) {
        if (element.checked){
            isChecked = true;
        }
    });

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

function saveRating() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    if (document.querySelector('input[name="selectrating"]:checked') === null){
        var errorMessages = document.getElementById('error-for-ratingSection');
        errorMessages.textContent = 'Please select a rating first!';
        errorMessages.style.display = 'block';
        errorMessages.style.color = 'red';
    } else {
        var rating = document.querySelector('input[name="selectrating"]:checked').value;
        var comment = document.getElementById('commentInput').value;
        var profileModalElement = document.querySelector('[id*="profileMainModal_"]');
        var userId = profileModalElement.id.split("_")[1];
        var currentDate = new Date();
        var formattedDate = currentDate.toLocaleDateString('hu-HU', { year: 'numeric', month: '2-digit', day: '2-digit' });
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

                if (checkedInputs.length >= 0) {
                    var lastCheckedValue = checkedInputs.length;
                    console.log('Az utolsó kiválasztott érték: ' + lastCheckedValue);
                    console.log('Rating: ' + rating);
                    var floatRating = parseFloat(rating);
                    var floatLastCheckedValue = parseFloat(lastCheckedValue);
                    var avg = (floatLastCheckedValue + floatRating)/2;
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
    var stars = document.querySelectorAll('.rating-rate .modal-rating-star-rate label');
    for (var i = 0; i < stars.length; i++) {
        if (i + 1 <= rating) {
            stars[i].style.color = 'gold';
        } else {
            stars[i].style.color = 'grey';
        }
    }
}


var currentCommentIndex = 0;

function showNextCommentSection() {
    let commentSections = document.querySelectorAll('.comment-section');
    let currentCommentSection = commentSections[currentCommentIndex];
    if (currentCommentSection) {
        currentCommentSection.classList.remove('active');
    }
    currentCommentIndex++;
    let nextCommentSection = commentSections[currentCommentIndex];

    if (nextCommentSection) {
        nextCommentSection.classList.add('active');
    } else {
        currentCommentIndex = 0;
        commentSections[currentCommentIndex].classList.add('active');
    }
}

function showPreviousCommentSection() {
    let commentSections = document.querySelectorAll('.comment-section');
    let currentCommentSection = commentSections[currentCommentIndex];
    if (currentCommentSection) {
        currentCommentSection.classList.remove('active');
    }
    currentCommentIndex--;

    let previousCommentSection = commentSections[currentCommentIndex];
    if (previousCommentSection) {
        previousCommentSection.classList.add('active');
    } else {
        currentCommentIndex = commentSections.length - 1;
        commentSections[currentCommentIndex].classList.add('active');
    }
}

document.addEventListener('DOMContentLoaded', function () {
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

function notMentee() {
    alert("You are not a mentee, so you cannot add this mentor to your favorites!");
}

function redirectToFavorites(uId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var contactButton = document.getElementById('modal-contact-button');
    var urlforsave = '/myGroup/saveFavorite?favoriteUserId=' + uId;

    if (contactButton.classList.contains('notmentee') && window.location.href.includes('mentors')) {
        alert("You are not a mentee,so you can't add this mentor to your favorites!");
    } else if (contactButton.classList.contains('notmentor') && window.location.href.includes('mentees')) {
        alert("You are not a mentor,so you can't add this mentee to your favorites!");
    } else {
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
        window.location.href = "/myGroup/favorites";
    }
}

function scrollToBottomInMyGroupPage() {
    var commentSection = document.getElementById('comment-section-container');
    if (commentSection.style.display === 'none' || commentSection.style.display === '') {
        commentSection.style.display = 'block';  // Show the comment section if it is hidden
    }

    if (commentSection) {
        commentSection.scrollIntoView({ behavior: 'smooth', block: 'end' });
    }
}

document.getElementById('showCommentsButton').addEventListener('click', scrollToBottomInMyGroupPage);

