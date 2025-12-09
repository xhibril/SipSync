const notification = document.querySelector("#notification");
const notificationText = document.querySelector("#notificationText");
const errorMessageIcon = document.querySelector("#errorMessageIcon");
const successMessageIcon = document.querySelector("#successMessageIcon");

export function validateNumInputs(...input){
    for (let value of input){
        value = Number(value)
        if(value <= 0) {
            return false;
        }
       if(isNaN(value)){
           return false;
       }
    }
    return true;
}


const allowedMap = {
    NAME: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ. ",
    EMAIL: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._-+@",
    PASSWORD: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!.@#$%^&*",
    MESSAGE: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,!?;:'\"()[]{}-_/@#$%^&*+=|~`"
};

export function validateInput(type, input){
    const allowed = allowedMap[type];
        input = input.trim();

        if (input === ""){
            return "empty";
        }
        for (let char of input) {
            if (!allowed.includes(char)) {
                return "invalid";
            }
    }
    return "ok";
}


export function handleValidation(type, input, fieldName){
    const status = validateInput(type, input);

    if(status === "empty"){
        showMessage("error", "All fields must be filled.");
        return false;
    }

    if(status === "invalid"){
        showMessage("error", `Please enter a valid ${fieldName}`);
        return false;
    }

    if(status === "ok"){
        return true;
    }
}


let messageTimeout;
export function showMessage(type, message) {

    if(messageTimeout) {
        clearTimeout(messageTimeout);
    }

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

    messageTimeout = setTimeout(() => {
        notification.classList.remove(type);
        messageTimeout = null;
    }, duration);
}

