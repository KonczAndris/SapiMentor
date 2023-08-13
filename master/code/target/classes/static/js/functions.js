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


function loadProfileContent(event) {
    event.preventDefault();
    const rightContent = document.getElementById("right-content");
    rightContent.style.opacity = '0';
    rightContent.style.visibility = 'hidden';

    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                rightContent.innerHTML = xhr.responseText;
                setTimeout(() => {
                    rightContent.style.opacity = '1';
                    rightContent.style.visibility = 'visible';
                }, 20);
            } else {
                console.error('Error loading profile content:', xhr.status);
            }
        }
    };
    xhr.open("GET", "/profile", true);
    xhr.send();
}
document.addEventListener("DOMContentLoaded", function() {
});














