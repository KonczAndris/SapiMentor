document.addEventListener("DOMContentLoaded", function() {
    var h1Element = document.getElementById("index-text");
    h1Element.addEventListener("click", function() {
        window.location.href = "/";
    });
});

function setupMentorModal() {
    var mentorModal = document.getElementById("mentorModal");

    var mentorBtn = document.getElementById("myMentorBtn");
    var mentorClose = document.getElementsByClassName("close-mentor")[0];

    mentorBtn.onclick = function() {
        mentorModal.style.display = "flex";
    };

    mentorClose.onclick = function() {
        mentorModal.style.display = "none";
    };
}

function setupModal() {
    var modal = document.getElementById("myModal");
    var btn = document.getElementById("myBtn");
    var span = document.getElementsByClassName("close")[0];

    if (modal && btn && span) {
        btn.onclick = function() {
            modal.style.display = "block";
        };
        span.onclick = function() {
            modal.style.display = "none";
        };
    }
}

function setupSkillsModal() {
    var modal = document.getElementById("skillsModal");
    var btn = document.getElementById("mySkillBtn");
    var span = document.getElementsByClassName("close-skill")[0];

    if (modal && btn && span) {
    btn.onclick = function() {
        modal.style.display = "flex";
    }
    span.onclick = function() {
        modal.style.display = "none";
    }}
}

function setupUploadModal() {
    var modal = document.getElementById("uploadModal");

    var btn = document.getElementById("change-image-icon");
    var span = document.getElementsByClassName("close-upload")[0];
    btn.onclick = function() {
        modal.style.display = "flex";
    }
    span.onclick = function() {
        modal.style.display = "none";
    }
}

function toggleKeywordsRow() {
    const keywordsRow = document.querySelector('.tr-keywords');
    keywordsRow.style.display = (keywordsRow.style.display === 'none' || keywordsRow.style.display === '') ? 'table-row' : 'none';
}

document.addEventListener('DOMContentLoaded', function() {
    const addButton = document.querySelector('.add-button');
    addButton.addEventListener('click', toggleKeywordsRow);
});

function selectTopic(topic) {
    const topicInput = document.getElementById('topic-input');
    topicInput.value = topic.textContent;
}

document.addEventListener('DOMContentLoaded', function() {
    const topicLinks = document.querySelectorAll('.dropdown-content a');

    topicLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault(); // Prevent the link from navigating
            selectTopic(link);
        });
    });
});

function selectTag(tag) {
    if (tag.classList.contains("topic-tag-display")) {
        tag.classList.remove("topic-tag-display");
        tag.classList.add("topic-tag-display-selected");
    } else if (tag.classList.contains("topic-tag-display-selected")) {
        tag.classList.remove("topic-tag-display-selected");
        tag.classList.add("topic-tag-display");
    }
}

function closeRow(button) {
    const row = button.closest('tr'); // Find the closest parent row element
    row.style.display = 'none'; // Hide the row
}

function toggleDropdown() {
    var dropdownContent = document.getElementById("myDropdown");
    dropdownContent.classList.toggle("active");

    var dropbtn = document.querySelector(".dropbtn");
    if (dropdownContent.classList.contains("active")) {
        dropbtn.style.borderRadius = "20px 20px 0 0";
    } else {
        dropbtn.style.borderRadius = ""; // Reset to default value
    }
}

function closeModalOnClickOutside() {
    var modal1 = document.getElementById("skillsModal");
    var modal2 = document.getElementById("myModal");
    var modal3 = document.getElementById("mentorModal");
    var modal4 = document.getElementById("uploadModal");

    window.addEventListener("click", function(event) {
        if (event.target == modal1) {
            modal1.style.display = "none";
        }
        if (event.target == modal2) {
            modal2.style.display = "none";
        }
        if (event.target == modal3) {
            modal3.style.display = "none";
        }
        if (event.target == modal4) {
            modal4.style.display = "none";
        }
    });
}

function validateFirstName() {
    var firstNameInput = document.getElementById("firstName-edit");
    var firstNameValue = firstNameInput.value.trim();

    if (!/^[a-zA-Z]{1,20}$/.test(firstNameValue)) {
        firstNameInput.classList.add("highlight");
    } else {
        firstNameInput.classList.remove("highlight");
    }
}

function validateLastName() {
    var lastNameInput = document.getElementById("lastName-edit");
    var lastNameValue = lastNameInput.value.trim();

    if (!/^[a-zA-Zéáűúőíöü\s'-]{1,20}$/.test(lastNameValue)) {
        lastNameInput.classList.add("highlight");
    } else {
        lastNameInput.classList.remove("highlight");
    }
}

function validateSpecialization() {
    var specializationInput = document.getElementById("specialization-edit");
    var specializationValue = specializationInput.value.trim();

    if (!/^[a-zA-Z\s]{4,20}$/.test(specializationValue)) {
        specializationInput.classList.add("highlight");
    } else {
        specializationInput.classList.remove("highlight");
    }
}


function validateYear() {
    var yearInput = document.getElementById("year-edit");
    var yearValue = yearInput.value.trim();

    if (!/^[1-4]$/.test(yearValue)) {
        yearInput.classList.add("highlight");
    } else {
        yearInput.classList.remove("highlight");
    }
}

function validatePhone() {
    var phoneInput = document.getElementById("phone-edit");
    var phoneValue = phoneInput.value.trim();

    if (!/^\d{10,}$/.test(phoneValue)){
        phoneInput.classList.add("highlight");
    } else {
        phoneInput.classList.remove("highlight");
    }
}


window.onload = function() {
    toggleDivs();
};

let isLeftVisible = false;

function toggleDivs() {
  const leftDiv = document.querySelector('.left');
  const rightDiv = document.querySelector('.right');
  const centerDiv = document.querySelector('.center');

  if (isLeftVisible) {
    rightDiv.classList.add('hidden');
    leftDiv.classList.remove('hidden');
  } else {
    leftDiv.classList.add('hidden');
    rightDiv.classList.remove('hidden');
  }
  isLeftVisible = !isLeftVisible;
}

function removeTag(element) {
    element.remove();
}

function deleteRow(button) {
    var row = button.closest('tr');

    if (row) {
        row.remove();
    }}

document.getElementById('upload-upload').addEventListener('click', function() {
    document.getElementById('imageUpload').click();
});

setupMentorModal();
setupModal();
setupSkillsModal();
setupUploadModal();
closeModalOnClickOutside();
validateFirstName();
validateLastName();
validateSpecialization();
validateYear();
validatePhone();














