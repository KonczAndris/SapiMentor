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

let profileimagesforSelectedUsers = [];
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

            const commentsDiv = document.getElementById('comments-info');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

function handlereselectedimages() {
    for (let i = 0; i < profileimagesforSelectedUsers.length; i++) {
        const commentprofileData = profileimagesforSelectedUsers[i];
        const commentprofileImage = commentprofileData[0];
        const commentprofileId = commentprofileData[1];

        // Get all elements with the class 'comment-profile-image-' + commentprofileId
        let commentedProfileImgs = document.getElementsByClassName('comment-profile-image-' + commentprofileId);

        // Convert HTMLCollection to array to use forEach (if needed, for modern browsers)
        Array.from(commentedProfileImgs).forEach(commentedProfileImg => {
            if (commentprofileImage && commentedProfileImg) {
                commentedProfileImg.src = 'data:image/jpeg;base64,' + commentprofileImage;
            }
        });
    }
}


document.getElementById('back-from-info-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-computer-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('computer-site').classList.add('active');
    var selectedTopicId = document.getElementById('computer-site').id;

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

            const commentsDiv = document.getElementById('comments-computer');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-computer-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-automation-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('automation-site').classList.add('active');
    var selectedTopicId = document.getElementById('automation-site').id;

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

            const commentsDiv = document.getElementById('comments-automation');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-automation-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-machine-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('machine-site').classList.add('active');
    var selectedTopicId = document.getElementById('machine-site').id;

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

            const commentsDiv = document.getElementById('comments-machine');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-machine-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-mecha-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('mecha-site').classList.add('active');
    var selectedTopicId = document.getElementById('mecha-site').id;

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

            const commentsDiv = document.getElementById('comments-mecha');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-mecha-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-telecommunication-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('telecommunication-site').classList.add('active');
    var selectedTopicId = document.getElementById('telecommunication-site').id;

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

            const commentsDiv = document.getElementById('comments-telecommunication');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-telecommunication-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-landscape-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('landscape-site').classList.add('active');
    var selectedTopicId = document.getElementById('landscape-site').id;

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

            const commentsDiv = document.getElementById('comments-landscape');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-landscape-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-horticulture-div').addEventListener('click', function() {
    document.getElementById('large-engineering-div').classList.remove('active');
    document.getElementById('horticulture-site').classList.add('active');
    var selectedTopicId = document.getElementById('horticulture-site').id;

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

            const commentsDiv = document.getElementById('comments-horticulture');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-horticulture-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-communication-div').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('communication-site').classList.add('active');
    var selectedTopicId = document.getElementById('communication-site').id;

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

            const commentsDiv = document.getElementById('comments-communication');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-communication-site').addEventListener('click', function() {
    window.location.href = "/topics";
});

document.getElementById('small-translation-div').addEventListener('click', function() {
    document.getElementById('large-languages-div').classList.remove('active');
    document.getElementById('translation-site').classList.add('active');
    var selectedTopicId = document.getElementById('translation-site').id;

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

            const commentsDiv = document.getElementById('comments-translation');

            data.forEach(data => {
                var commentSection = document.createElement('div');
                commentSection.classList.add('comment-section');
                commentSection.id = 'comment-section-' + data[0];

                var commentRow = document.createElement('div');
                commentRow.classList.add('comment-row');
                commentSection.appendChild(commentRow);

                var commentProfileImg = document.createElement('img');
                commentProfileImg.id = 'commentProfileImg-' + data[0];
                commentProfileImg.alt = 'Profile Image';
                commentProfileImg.classList.add('comment-profile-image-' + data[0]);
                commentProfileImg.classList.add('comment-profile-image');
                commentProfileImg.src = "/img/anonym.jpg";
                commentRow.appendChild(commentProfileImg);

                var commentInfos = document.createElement('div');
                commentInfos.classList.add('comment-infos');
                commentRow.appendChild(commentInfos);

                var commentUsername = document.createElement('p');
                commentUsername.style.margin = '0';
                commentUsername.textContent = data[1] && data[2] ? data[1] + " " + data[2] : "Unknown";
                commentInfos.appendChild(commentUsername);

                var commentLabelDate = document.createElement('p');
                commentLabelDate.classList.add('comment-label-date');
                commentLabelDate.id = 'comment-label-date-' + data[0];
                commentLabelDate.textContent = data[3] ? data[3] : "Unknown";
                commentInfos.appendChild(commentLabelDate);

                var commentContent = document.createElement('div');
                commentContent.classList.add('comment-content');
                commentSection.appendChild(commentContent);

                var commentSubject = document.createElement('div');
                commentSubject.classList.add('comment-subject');
                commentSubject.textContent = data[5] ? data[5] : "No Subject";
                commentContent.appendChild(commentSubject);

                var commentLabelText = document.createElement('p');
                commentLabelText.classList.add('comment-label-text');
                commentLabelText.id = 'comment-label-text-' + data[0];
                commentLabelText.textContent = data[4] ? data[4] : "No Comment";
                commentContent.appendChild(commentLabelText);

                commentsDiv.appendChild(commentSection);
            });
            updateCommentSubjects();
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
            profileimagesforSelectedUsers = data.selectedUserImages;
            setInterval(() => {
                handlereselectedimages();
            }, 1000);
        })
        .catch(error => {
            console.error('Error fetching selected users images:', error);
        });
});

document.getElementById('back-from-translation-site').addEventListener('click', function() {
    window.location.href = "/topics";
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
        const commentsDivInfo = document.getElementById('comments-info');
        const commentsDivComputer = document.getElementById('comments-computer');
        const commentsDivAutomation = document.getElementById('comments-automation');
        const commentsDivMecha = document.getElementById('comments-mecha');
        const commentsDivMachine = document.getElementById('comments-machine');
        const commentsDivTelecommunication = document.getElementById('comments-telecommunication');
        const commentsDivLandscape = document.getElementById('comments-landscape');
        const commentsDivHorticulture = document.getElementById('comments-horticulture');
        const commentsDivCommunication = document.getElementById('comments-communication');
        const commentsDivTranslation = document.getElementById('comments-translation');


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

            const infoSite = document.getElementById('info-site');
            if (infoSite.classList.contains('active')) {
            commentsDivInfo.appendChild(commentSection);}

            const computerSite = document.getElementById('computer-site');
            if (computerSite.classList.contains('active')) {
                commentsDivComputer.appendChild(commentSection);}

            const automationSite = document.getElementById('automation-site');
            if (automationSite.classList.contains('active')) {
                commentsDivAutomation.appendChild(commentSection);}

            const mechaSite = document.getElementById('mecha-site');
            if (mechaSite.classList.contains('active')) {
                commentsDivMecha.appendChild(commentSection);}

            const machineSite = document.getElementById('machine-site');
            if (machineSite.classList.contains('active')) {
                commentsDivMachine.appendChild(commentSection);}

            const telecommunicationSite = document.getElementById('telecommunication-site');
            if (telecommunicationSite.classList.contains('active')) {
                commentsDivTelecommunication.appendChild(commentSection);}

            const landscapeSite = document.getElementById('landscape-site');
            if (landscapeSite.classList.contains('active')) {
                commentsDivLandscape.appendChild(commentSection);}

            const horticultureSite = document.getElementById('horticulture-site');
            if (horticultureSite.classList.contains('active')) {
                commentsDivHorticulture.appendChild(commentSection);}

            const communicationSite = document.getElementById('communication-site');
            if (communicationSite.classList.contains('active')) {
                commentsDivCommunication.appendChild(commentSection);}

            const translationSite = document.getElementById('translation-site');
            if (translationSite.classList.contains('active')) {
                commentsDivTranslation.appendChild(commentSection);}
            updateCommentSubjects();
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


function updateCommentSubjects() {
    // Társzótár létrehozása a value értékek és a leíró szövegek társításához
    const subjects = {
        "drawing-design-landscape": {
            english: "Drawing and Design",
            hungarian: "Rajz és tervezés"
        },
        "descriptive-geometry-landscape": {
            english: "Descriptive Geometry",
            hungarian: "Ábrázoló geometria"
        },
        "soil-science-landscape": {
            english: "Soil Science",
            hungarian: "Talajtan"
        },
        "architecture-landscape": {
            english: "Architecture",
            hungarian: "Építészet"
        },
        "art-history-landscape": {
            english: "Art History",
            hungarian: "Művészettörténet"
        },
        "ethnography-landscape": {
            english: "Ethnography",
            hungarian: "Néprajz"
        },
        "topography-landscape": {
            english: "Topography",
            hungarian: "Topográfia"
        },
        "dendrology-plant-knowledge-landscape": {
            english: "Dendrology and Plant Knowledge",
            hungarian: "Dendrológia és dísznövényismeret"
        },
        "history-of-landscape-architecture-landscape": {
            english: "History of Landscape Architecture",
            hungarian: "A tájépítészet története"
        },
        "green-area-design-landscape": {
            english: "Green Area Design",
            hungarian: "Zöldfelület-tervezés"
        },
        "landscape-design-landscape": {
            english: "Landscape Design",
            hungarian: "Tájtervezés"
        },
        "landscape-rehabilitation-landscape": {
            english: "Landscape Rehabilitation",
            hungarian: "Tájrehabilitáció"
        },
        "programming": {
            english: "Programming",
            hungarian: "Programozás"
        },
        "algorithms-info": {
            english: "Data Structures and Algorithms",
            hungarian: "Adatszerkezetek és Algoritmusok"
        },
        "discrete-info": {
            english: "Discrete Mathematics",
            hungarian: "Diszkrét matematika"
        },
        "oop-info": {
            english: "Object-Oriented Programming",
            hungarian: "Objektum orientált programozás"
        },
        "ai-info": {
            english: "Artificial Intelligence",
            hungarian: "Mesterséges intelligencia"
        },
        "databases-info": {
            english: "Databases",
            hungarian: "Adatbázisok"
        },
        "graphs-info": {
            english: "Graph Theory",
            hungarian: "Gráfelmélet"
        },
        "software-info": {
            english: "Software Development",
            hungarian: "Szoftverfejlesztés"
        },
        "testing-info": {
            english: "Software Testing",
            hungarian: "Szoftvertesztelés"
        },
        "mobile-info": {
            english: "Mobile Development",
            hungarian: "Mobilos alkalmazás fejlesztés"
        },
        "programming-computer": {
            english: "Programming",
            hungarian: "Programozás"
        },
        "algorithms-computer": {
            english: "Data Structures and Algorithms",
            hungarian: "Adatszerkezetek és Algoritmusok"
        },
        "computer-architecture-computer": {
            english: "Computer Architectures",
            hungarian: "Számítógép-architektúrák"
        },
        "operating-systems-computer": {
            english: "Operating Systems",
            hungarian: "Operációs rendszerek"
        },
        "computer-networks-computer": {
            english: "Computer Networks",
            hungarian: "Számítógépes hálózatok"
        },
        "distributed-systems-computer": {
            english: "Distributed Systems",
            hungarian: "Osztott rendszerek"
        },
        "digital-electronics-computer": {
            english: "Digital Electronics",
            hungarian: "Digitális elektronika"
        },
        "microprocessor-systems-computer": {
            english: "Microprocessor Systems Design and Programming",
            hungarian: "Mikroprocesszor-rendszerek tervezése és programozása"
        },
        "artificial-intelligence-computer": {
            english: "Artificial Intelligence",
            hungarian: "Mesterséges intelligencia"
        },
        "cryptography-computer": {
            english: "Cryptography",
            hungarian: "Kriptográfia"
        },
        "industrial-process-control-computers-automation": {
            english: "Industrial Process Control Computers Programming",
            hungarian: "Ipari folyamatirányító számítógépek programozása"
        },
        "robot-control-automation": {
            english: "Robot Control",
            hungarian: "Robotok irányítása"
        },
        "operating-systems-computer-networks-automation": {
            english: "Operating Systems and Computer Networks",
            hungarian: "Operációs rendszerek és számítógépes hálózatok"
        },
        "microprocessor-systems-design-programming-automation": {
            english: "Microprocessor Systems Design and Programming",
            hungarian: "Mikroprocesszor-rendszerek tervezése és programozása"
        },
        "digital-signal-processing-automation": {
            english: "Digital Signal Processing",
            hungarian: "Digitális jelfeldolgozás"
        },
        "optimization-techniques-automation": {
            english: "Optimization Techniques",
            hungarian: "Optimalizálási eljárások"
        },
        "programming-machine": {
            english: "Programming",
            hungarian: "Programozás"
        },
        "computer-design-machine": {
            english: "Computer Design",
            hungarian: "Számítógépes tervezés"
        },
        "machining-theory-machine": {
            english: "Machining Theory",
            hungarian: "Forgácsoláselmélet"
        },
        "industrial-electronics-machine": {
            english: "Industrial Electronics",
            hungarian: "Ipari elektronika"
        },
        "mechanisms-machine": {
            english: "Mechanisms",
            hungarian: "Mechanizmusok"
        },
        "machine-elements-machine": {
            english: "Machine Elements",
            hungarian: "Gépelemek"
        },
        "manufacturing-theory-machine": {
            english: "Manufacturing Theory",
            hungarian: "Gyártáselmélet"
        },
        "cnc-technologies-machine": {
            english: "CNC Technologies",
            hungarian: "CNC-technológiák"
        },
        "materials-science-machine": {
            english: "Materials Science",
            hungarian: "Anyagtudományok"
        },
        "tool-design-machine": {
            english: "Tool, Fixture, and Jig Design",
            hungarian: "Szerszám-, készülék- és idomszertervezés"
        },
        "mechanics-mecha": {
            english: "Mechanics",
            hungarian: "Mechanika"
        },
        "mechanisms-mecha": {
            english: "Mechanisms",
            hungarian: "Mechanizmusok"
        },
        "computer-aided-design-mecha": {
            english: "Computer-Aided Design (CAD)",
            hungarian: "Számítógépes tervezés (CAD)"
        },
        "systems-theory-mecha": {
            english: "Systems Theory",
            hungarian: "Rendszerelmélet"
        },
        "analog-digital-electronics-mecha": {
            english: "Analog and Digital Electronics",
            hungarian: "Analóg és digitális elektronika"
        },
        "cnc-control-programming-mecha": {
            english: "CNC Control and Programming",
            hungarian: "CNC vezérlés és programozás"
        },
        "robotics-design-control-mecha": {
            english: "Robotics Design and Control",
            hungarian: "Robotok tervezése és vezérlése"
        },
        "programming-telecommunication": {
            english: "Programming",
            hungarian: "Programozás"
        },
        "databases-telecommunication": {
            english: "Databases",
            hungarian: "Adatbázisok"
        },
        "operating-systems-telecommunication": {
            english: "Operating Systems",
            hungarian: "Operációs rendszerek"
        },
        "computer-networks-telecommunication": {
            english: "Computer Networks",
            hungarian: "Számítógéphálózatok"
        },
        "internet-protocols-telecommunication": {
            english: "Internet Protocols",
            hungarian: "Internetprotokollok"
        },
        "software-design-telecommunication": {
            english: "Software Design",
            hungarian: "Szoftvertervezés"
        },
        "analog-digital-electronics-telecommunication": {
            english: "Analog and Digital Electronics",
            hungarian: "Analóg és digitális elektronika"
        },
        "electrical-measurements-telecommunication": {
            english: "Electrical Measurements",
            hungarian: "Villamos mérések"
        },
        "digital-signal-processing-telecommunication": {
            english: "Digital Signal Processing",
            hungarian: "Digitális jel- és hangfeldolgozás"
        },
        "computer-circuit-design-telecommunication": {
            english: "Computer Circuit Design",
            hungarian: "Számítógépes áramkörtervezés"
        },
        "telecommunication-theory-telecommunication": {
            english: "Telecommunication Theory",
            hungarian: "Hírközléselmélet"
        },
        "modulation-techniques-telecommunication": {
            english: "Modulation Techniques",
            hungarian: "Modulációs technikák"
        },
        "radio-communication-telecommunication": {
            english: "Radio Communication",
            hungarian: "Rádiókommunikáció"
        },
        "mobile-communication-systems-telecommunication": {
            english: "Mobile Communication Systems",
            hungarian: "Mobil kommunikációs rendszerek"
        },
        "ip-telephony-telecommunication": {
            english: "IP Telephony",
            hungarian: "IP telefónia"
        },
        "optical-networks-telecommunication": {
            english: "Optical Networks",
            hungarian: "Optikai hálózatok"
        },
        "television-broadcasting-telecommunication": {
            english: "Television Broadcasting",
            hungarian: "Televíziózás"
        },
            "ornamental-plant-cultivation-horticulture": {
                english: "Ornamental Plant Cultivation",
                hungarian: "Dísznövénytermesztés"
            },
            "medicinal-plant-cultivation-horticulture": {
                english: "Medicinal Plant Cultivation",
                hungarian: "Gyógynövénytermesztés"
            },
            "fruit-cultivation-horticulture": {
                english: "Fruit Cultivation",
                hungarian: "Gyümölcstermesztés"
            },
            "fruit-variety-knowledge-horticulture": {
                english: "Fruit Variety Knowledge",
                hungarian: "Gyümölcsfajta-ismeret"
            },
            "viticulture-horticulture": {
                english: "Viticulture",
                hungarian: "Szőlőtermesztés"
            },
            "winemaking-horticulture": {
                english: "Winemaking",
                hungarian: "Borászat"
            },
            "vegetable-cultivation-horticulture": {
                english: "Vegetable Cultivation",
                hungarian: "Zöldségtermesztés"
            },
            "micropropagation-breeding-horticulture": {
                english: "Micropropagation and Breeding of Horticultural Plants",
                hungarian: "Kertészeti növények mikroszaporítása, nemesítése"
            },
            "horticultural-entomology-pathology-horticulture": {
                english: "Horticultural Entomology and Pathology",
                hungarian: "Kertészeti rovartan és kórtan"
            },
            "soil-science-agrochemistry-horticulture": {
                english: "Soil Science and Agrochemistry",
                hungarian: "Talajtan és agrokémia"
            },
            "garden-design-horticulture": {
                english: "Garden Design",
                hungarian: "Kerttervezés"
            },
            "intercultural-communication": {
                english: "Intercultural Communication",
                hungarian: "Interkulturális kommunikáció"
            },
            "institutional-communication": {
                english: "Institutional Communication",
                hungarian: "Intézményi kommunikáció"
            },
            "communication-law-communication": {
                english: "Communication Law",
                hungarian: "Kommunikációs jog"
            },
            "press-history-communication": {
                english: "Press History",
                hungarian: "Sajtótörténet"
            },
            "communication-theory-communication": {
                english: "Communication Theory",
                hungarian: "Kommunikációelmélet"
            },
            "stylistics-and-style-practice-communication": {
                english: "Stylistics and Style Practice",
                hungarian: "Stilisztika és stílusgyakorlat"
            },
            "mass-communication-systems-communication": {
                english: "Mass Communication Systems",
                hungarian: "Tömegkommunikációs rendszerek"
            },
            "advertising-communication": {
                english: "Advertising",
                hungarian: "Reklám"
            },
            "marketing-and-management-communication": {
                english: "Marketing and Management",
                hungarian: "Marketing és menedzsment"
            },
            "press-genres-communication": {
                english: "Press Genres",
                hungarian: "Sajtóműfajok"
            },
            "public-relations-communication": {
                english: "Public Relations",
                hungarian: "Közkapcsolatok (Public Relation)"
            },
            "cultural-and-art-history-communication": {
                english: "Cultural History and Art History",
                hungarian: "Művelődéstörténet és művészettörténet"
            },
            "sociology-communication": {
                english: "Sociology",
                hungarian: "Szociológia"
            },
            "social-psychology-communication": {
                english: "Social Psychology",
                hungarian: "Szociálpszichológia"
            },
            "political-science-communication": {
                english: "Political Science",
                hungarian: "Politológia"
            },
            "negotiation-techniques-communication": {
                english: "Negotiation Techniques",
                hungarian: "Tárgyalási technikák"
            },
            "consumer-behavior-communication": {
                english: "Consumer Behavior",
                hungarian: "Fogyasztói magatartás"
            },
        "english-translation": {
            english: "English Language and Culture",
            hungarian: "Angol Nyelv és Kultúra"
        },
        "german-translation": {
            english: "German Language and Culture",
            hungarian: "Német Nyelv és Kultúra"
        },
        "romanian-translation": {
            english: "Romanian Language and Culture",
            hungarian: "Román Nyelv és Kultúra"
        },
        "hungarian-translation": {
            english: "Hungarian Language and Culture",
            hungarian: "Magyar Nyelv és Kultúra"
        },
        "terminology-translation": {
            english: "Terminology",
            hungarian: "Terminológia"
        },
        "specialized-language-translation": {
            english: "Specialized Language",
            hungarian: "Szaknyelv"
        },
        "translation-theory-translation": {
            english: "Translation Theory and Practice",
            hungarian: "Fordításelmélet és -gyakorlat"
        }
    };

    // Az összes .comment-subject elem kiválasztása
    const commentSubjects = document.querySelectorAll('.comment-subject');

    commentSubjects.forEach(subjectElement => {
        const text = subjectElement.textContent.trim();

        // Végigmegyünk a szótár kulcsain és értékein
        for (const [key, value] of Object.entries(subjects)) {
            if (text === key) {

                if (localStorage.getItem('language') === 'hungarian') {
                    subjectElement.textContent = `${value.hungarian}`;
                }
                else {
                    subjectElement.textContent = `${value.english}`;
                }
                break;
            }
        }
    });
}

