'use strict';

let stompClient = null;
let IdForUserInTopicsPage = null;
function connectToWebSocketForTopicsPage() {
    var elementToGetTopicsUserId = document.querySelector('[id^="userIdForTopicsPage-"]');
    // console.log("elementToGetUserId: ", elementToGetUserId);
    IdForUserInTopicsPage = elementToGetTopicsUserId.id.split("-")[1];
    // console.log("IdForUserInIndexPage: ", IdForUserInIndexPage);

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
        console.log("sima ws-t hasznal");
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
        console.log("wss-t hasznal");
    }
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForTopicsPage, onErrorInTopicsPage);
}

function onConnectedForTopicsPage() {
    stompClient.subscribe(`/user/${IdForUserInTopicsPage}/queue/messages`, onMessageReceivedNotificationInTopicsPage)
}

function onErrorInTopicsPage(error) {

}

async function onMessageReceivedNotificationInTopicsPage(payload) {
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

    connectToWebSocketForTopicsPage();

    var elementToGetTopicsProfileUserId = document.querySelector('[id^="userIdForTopicsPage-"]');
    var topicsprofileUserId = elementToGetTopicsProfileUserId.id.split("-")[1];

    fetch(`/topics/getTopicsProfileNotificationStatus?userId=${topicsprofileUserId}`, {
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

document.getElementById('info-navigation-card').addEventListener('click', function() {
    document.getElementById('large-topic-div').classList.remove('active');
    document.getElementById('large-info-div').classList.add('active');
    console.log("megjelenitodik az infotanszekhaz tartozo oldalak");
});

document.getElementById('engineering-navigation-card').addEventListener('click', function() {
    document.getElementById('large-topic-div').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('languages-navigation-card').addEventListener('click', function() {
    document.getElementById('large-topic-div').classList.remove('active');
    document.getElementById('large-languages-div').classList.add('active');
});

document.getElementById('back-from-info').addEventListener('click', function() {
    document.getElementById('large-info-div').classList.remove('active');
    document.getElementById('large-topic-div').classList.add('active');
});

document.getElementById('back-from-engineering').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('large-topic-div').classList.add('active');
});

document.getElementById('back-from-languages').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('large-topic-div').classList.add('active');
});

document.getElementById('small-info-div').addEventListener('click', function() {
    document.getElementById('large-info-div').classList.remove('active');
    document.getElementById('info-site').classList.add('active');
    console.log("megjelenitodik informatika szak adatai");
    // TODO: ide kell majd az adatok lekerese (fetch keres) a backendtol az informatika szakrol
    // (a tobbi szaknal is igy kell majd)
    // TODO: ezt kell majd atadni a getSelectedTopicDetails es a getSelectedUsersImages a fetch keresnel
    var selectedTopicId = document.getElementById('info-site').id;
    console.log("selectedTopicId: ", selectedTopicId);
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    //var userId = button.id.split("-")[1];
    var url = '/topics/getSelectedTopicDetails?selectedTopicId=' + selectedTopicId;

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            console.log("Response: ", response);
            return response.text();
        } else {
            throw new Error('Something went wrong');
        }
    }).then(data => {
        if (data !== 'error') {
            // TODO:
            //document.getElementById('profileModal').innerHTML = data;
            // const closeBtn = document.querySelector('.close-profile-modal');
            // if (closeBtn !== null){
            //     closeBtn.addEventListener('click', closeModal);
            // }
            // displayModal();
        } else {
            throw new Error('Something went wrong');
        }
    }).catch(error => {
        console.log("Error: ", error);
    });

    var url2 = '/topics/getSelectedUsersImages?selectedTopicId=' + selectedTopicId;
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
        // TODO: itt kell majd a kepek megjelenitese
        // profileimagesforSelectedUsers = data.selectedUserImages;
        // setInterval(() => {
        //     handlereselectedimages();
        // }, 1000);
    }).catch(error => {
        console.log("Error: ", error);
    });
});

document.getElementById('back-from-info-site').addEventListener('click', function() {
    document.getElementById('info-site').classList.remove('active');
    document.getElementById('large-info-div').classList.add('active');
});

document.getElementById('small-computer-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('computer-site').classList.add('active');
});

document.getElementById('back-from-computer-site').addEventListener('click', function() {
    document.getElementById('computer-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-automation-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('automation-site').classList.add('active');
});

document.getElementById('back-from-automation-site').addEventListener('click', function() {
    document.getElementById('automation-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-machine-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('machine-site').classList.add('active');
});

document.getElementById('back-from-machine-site').addEventListener('click', function() {
    document.getElementById('machine-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-mecha-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('mecha-site').classList.add('active');
});

document.getElementById('back-from-mecha-site').addEventListener('click', function() {
    document.getElementById('mecha-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-telecommunication-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('telecommunication-site').classList.add('active');
});

document.getElementById('back-from-telecommunication-site').addEventListener('click', function() {
    document.getElementById('telecommunication-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-landscape-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('landscape-site').classList.add('active');
});

document.getElementById('back-from-landscape-site').addEventListener('click', function() {
    document.getElementById('landscape-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-horticulture-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('horticulture-site').classList.add('active');
});

document.getElementById('back-from-horticulture-site').addEventListener('click', function() {
    document.getElementById('horticulture-site').classList.remove('active');
    document.getElementById('large-engineering-div').classList.add('active');
});

document.getElementById('small-communication-div').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('communication-site').classList.add('active');
});

document.getElementById('back-from-communication-site').addEventListener('click', function() {
    document.getElementById('communication-site').classList.remove('active');
    document.getElementById('large-languages-div').classList.add('active');
});

document.getElementById('small-translation-div').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('translation-site').classList.add('active');
});

document.getElementById('back-from-translation-site').addEventListener('click', function() {
    document.getElementById('translation-site').classList.remove('active');
    document.getElementById('large-languages-div').classList.add('active');
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

    eventSource.addEventListener('UserCommentTopics', function (event) {

        const userCommentData = JSON.parse(event.data);
        const ratingUserId = userCommentData.ratingUserId;

        const topicDiv = document.getElementById(userCommentData.ratedTopicId);

        if(topicDiv !== null) {
            var commentSection = document.createElement('div');
            commentSection.classList = 'comment-section';
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

            var commentContent = document.createElement('div');
            commentContent.classList = 'comment-content';
            commentSection.appendChild(commentContent);

            var commentSubject = document.createElement('p');
            commentSubject.classList = 'comment-subject';
            commentContent.appendChild(commentSubject);

            var commentLabelText = document.createElement('p');
            commentLabelText.classList = 'comment-label-text';
            commentLabelText.id = 'comment-label-text-' + ratingUserId;
            commentLabelText.textContent = userCommentData.comment;
            commentContent.appendChild(commentLabelText);

            //var commentContainer = document.getElementById("commentContainer");
            //commentContainer.appendChild(commentSection);

            // itt maskepp van a mygroup-nal a felepites itt nincs commentContainer azert add hibat .
        }
    });
});


//COMMENTING
function saveInfoComment(siteId) {
    console.log("Save info comment");
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var errorMessages = document.getElementById(siteId).querySelector('.error-for-comment_Section');
    if (document.getElementById(siteId).querySelector('.comment-box').value.trim() === ''){
        if (localStorage.getItem('language') === 'hungarian') {
            errorMessages.textContent = 'Kérlek írj egy kommentet!';
            errorMessages.style.display = 'block';
            errorMessages.style.color = 'red';
        } else {
            errorMessages.textContent = 'Please write your comment first!';
            errorMessages.style.display = 'block';
            errorMessages.style.color = 'red';
        }

    } else {
        errorMessages.style.display = 'none';

        var commentValue = document.getElementById(siteId).querySelector('.comment-box').value;
        var englishSelectValue = document.getElementById(siteId).querySelector('.subject-select').value;

        var subjectValue;
        if (localStorage.getItem('language') === 'hungarian') {
             subjectValue = document.getElementById(siteId).querySelector('.hungarian-select').value;
        } else {
             subjectValue = document.getElementById(siteId).querySelector('.english-select').value;
        }

        var currentDate = new Date();
        var formattedDate = currentDate.toLocaleDateString('hu-HU', { year: 'numeric', month: '2-digit', day: '2-digit' });

        const sseUrl = "/sse/sendCommentInTopics";

        fetch('/topics/saveComment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': token
            },
            body: JSON.stringify({
                ratedTopicId: siteId,
                comment: commentValue,
                subject: subjectValue,
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
                        ratedTopicId: siteId,
                        comment: commentValue,
                        subject: subjectValue,
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
            }

        }).catch(error => {
            console.log("Error: ", error);
        });
    }

}

