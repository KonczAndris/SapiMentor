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
    var selectedTopicId = document.getElementById('info-site').id;

    // Fetch selected topic details
    var token = $("meta[name='_csrf']").attr("content");
    var url = '/topics/getSelectedTopicDetails?selectedTopicId=' + selectedTopicId;

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch selected topic details');
            }
            return response.json(); // Assuming this returns JSON data
        })
        .then(data => {
            console.log('Selected topic details:', data); // Log the data for debugging
            // Update your modal or process the data as needed
            // Example: document.getElementById('profileModal').innerHTML = data;
            // displayModal();
        })
        .catch(error => {
            console.error('Error fetching selected topic details:', error);
        });

    // Fetch selected users' images
    var url2 = '/topics/getSelectedUsersImages?selectedTopicId=' + selectedTopicId;
    fetch(url2, {
        method: "GET",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch selected users images');
            }
            return response.json(); // Assuming this returns JSON data
        })
        .then(data => {
            console.log('Selected users images:', data);

            const commentsDiv = document.querySelector('.comments');

            // Clear existing comments if necessary
            commentsDiv.innerHTML = '';

            // Iterate through comments and append them to the commentsDiv
            data.selectedUserImages.forEach(commentData => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + commentData.userId;

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + commentData.userId;
                commentProfileImg.src = commentData.userImage && commentData.userImage !== "" ? 'data:image/jpeg;base64,' + commentData.userImage : "/img/anonym.jpg";
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image');
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = commentData.firstName && commentData.lastName ? commentData.firstName + " " + commentData.lastName : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + commentData.userId;
                commentLabelDate.textContent = commentData.date ? commentData.date : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = commentData.subject ? commentData.subject : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + commentData.userId;
                commentLabelText.textContent = commentData.comment ? commentData.comment : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
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
    var urlEndpoint = "/sse/subscribe";
    const eventSource = new EventSource(urlEndpoint);

    eventSource.onopen = function(event) {
        console.log('SSE connection opened.');
    };

    eventSource.onerror = function(event) {
        console.error('SSE error:', event);
    };

    eventSource.addEventListener('UserCommentTopics', function(event) {
        const commentData = JSON.parse(event.data);
        const topicDivId = commentData.ratedTopicId;
        const topicDiv = document.getElementById(topicDivId);
        const commentsDiv = document.getElementById('comments-info');

        if (topicDiv) {
            var commentSection = document.createElement('div');
            commentSection.classList.add('comment-section');
            commentSection.id = 'comment-section-' + commentData.userId;

            var commentRow = document.createElement('div');
            commentRow.classList.add('comment-row');
            commentSection.appendChild(commentRow);

            var commentProfileImg = document.createElement('img');
            commentProfileImg.id = 'commentProfileImg-' + commentData.userId;
            commentProfileImg.src = commentData.userImage && commentData.userImage !== "" ? 'data:image/jpeg;base64,' + commentData.userImage : "/img/anonym.jpg";
            commentProfileImg.alt = 'Profile Image';
            commentProfileImg.classList.add('comment-profile-image');
            commentRow.appendChild(commentProfileImg);

            var commentInfos = document.createElement('div');
            commentInfos.classList.add('comment-infos');
            commentRow.appendChild(commentInfos);

            var commentUsername = document.createElement('p');
            commentUsername.style.margin = '0';
            commentUsername.textContent = commentData.firstName && commentData.lastName ? commentData.firstName + " " + commentData.lastName : "Unknown";
            commentInfos.appendChild(commentUsername);

            var commentLabelDate = document.createElement('p');
            commentLabelDate.classList.add('comment-label-date');
            commentLabelDate.id = 'comment-label-date-' + commentData.userId;
            commentLabelDate.textContent = commentData.date ? commentData.date : "Unknown";
            commentInfos.appendChild(commentLabelDate);

            var commentContent = document.createElement('div');
            commentContent.classList.add('comment-content');
            commentSection.appendChild(commentContent);

            var commentSubject = document.createElement('div');
            commentSubject.classList.add('comment-subject');
            commentSubject.textContent = commentData.subject ? commentData.subject : "No Subject";
            commentContent.appendChild(commentSubject);

            var commentLabelText = document.createElement('p');
            commentLabelText.classList.add('comment-label-text');
            commentLabelText.id = 'comment-label-text-' + commentData.userId;
            commentLabelText.textContent = commentData.comment ? commentData.comment : "No Comment";
            commentContent.appendChild(commentLabelText);

            commentsDiv.appendChild(commentSection);
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

