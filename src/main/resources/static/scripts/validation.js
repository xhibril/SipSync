const notification = document.querySelector("#notification");
const notificationText = document.querySelector("#notificationText");
const errorMessageIcon = document.querySelector("#errorMessageIcon");
const successMessageIcon = document.querySelector("#successMessageIcon");

export function validateNumInputs(...input){
    for (let value of input){
        value = Number(value)
        if(value <= 0) {
            displayErrorMessage(true, "Oops! Value must be more than 0.");
            return false;
        }

       if(isNaN(value)){
           displayErrorMessage(true, "Oops! Some fields are missing or have invalid values. Please check and try again.");
           return false;
       }
    }
    return true;
}


export function showMessage(type, message) {

    notification.classList.remove(type);
    notification.style.transition = 'none';
    void notification.offsetWidth;
    notification.style.transition = '';
    notification.classList.add(type);

    if(message){
        notificationText.innerHTML = message;
    }

    // make duration 1.5s for successful, and 3s for error
    let duration;
    if(type === "success"){
        successMessageIcon.classList.remove('hidden');
        errorMessageIcon.classList.add('hidden');
        duration = 1500;
    } else {
        successMessageIcon.classList.add('hidden');
        errorMessageIcon.classList.remove('hidden');
        duration = 3000;
    }

    setTimeout(() => {
        notification.classList.remove(type);
    }, duration);
}




export function validateForm(...input){
    const allowed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!.@#$%^&* ";

    for (let value of input) {
        for (let char of value) {
            if (!allowed.includes(char)) {
                return false;
            }
        }
    }
    return true;
}