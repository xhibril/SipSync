const notification = document.querySelector("#notification");
const notificationText = document.querySelector("#notification-text");
const errorMessageIcon = document.querySelector("#error-icon");
const successMessageIcon = document.querySelector("#success-icon");

// handle notification pop up
export function showMessage(type, message) {
    let messageTimeout;

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

    // make duration 3s for successful, and 3.5s for error
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


// validate input is valid nums > 0
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

// allowed for each field
const allowedMap = {
    NAME: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ. ",
    EMAIL: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._-+@",
    PASSWORD: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!.@#$%^&*",
    MESSAGE: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,!?;:'\"()[]{}-_/@#$%^&*+=|~`"
};

// check if field has only allowed chars
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


// handles response
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

    if(status === "ok") return true;
}


// check password strength
export function validatePasswordStrength(password){
    const minLength = 8;
    const maxLength = 32;

    if(password.length < minLength) return "Password must be longer than 8 characters.";
    if(password.length > maxLength) return "Password must be shorter than 32 characters.";

    const regex = /^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.]).+$/;

    if (!regex.test(password)) return "Password must be 8–32 characters and include at least one uppercase letter, " +
                                      "one number, and one special character (.!@#$%^&*).";
    return "VALID";
}


export function validateEmailDomain(email){
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if(!emailRegex.test(email)) return "Please enter a valid email";
    return "VALID";
}
