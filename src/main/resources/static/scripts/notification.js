const notification = document.querySelector("#notification");
const notificationText = document.querySelector("#notification-text");
const errorMessageIcon = document.querySelector("#error-icon");
const successMessageIcon = document.querySelector("#success-icon");

// handle notification pop up
let messageTimeout = null;
export function showMessage(type, message) {
    if(messageTimeout) {
        clearTimeout(messageTimeout);
    }

    notification.classList.remove("success", "error");
    notification.style.transition = 'none';
    void notification.offsetWidth;
    notification.style.transition = '';
    notification.classList.add(type);

    if(message){
        notificationText.innerHTML = message;
    }

    let duration;
    if(type === "success"){
        successMessageIcon.classList.remove('hidden');
        errorMessageIcon.classList.add('hidden');
        duration = 3000;
    } else {
        successMessageIcon.classList.add('hidden');
        errorMessageIcon.classList.remove('hidden');
        duration = 3500;
    }

    messageTimeout = setTimeout(() => {
        notification.classList.remove(type);
        messageTimeout = null;
    }, duration);
}