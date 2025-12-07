const errorMessage = document.querySelector("#errorMessage");
const errormessageText = document.querySelector("#errorMessageText");

let message;
export function validateNumInputs(...input){
    for (let value of input){
        value = Number(value)
        if(value <= 0) {
            message = "Oops! Value must be more than 0.";
            displayErrorMessage(true, message);
            return false;
        }

       if(isNaN(value)){
           message = "Oops! Some fields are missing or have invalid values. Please check and try again.";
           displayErrorMessage(true, message);
           return false;
       }
    }
    return true;
}


export function displayErrorMessage(bool, message) {
    if(bool === true){
        errorMessage.classList.remove('show');
        errorMessage.style.transition = 'none';
        void errorMessage.offsetWidth;
        errorMessage.style.transition = '';
        errorMessage.classList.add('show');
        if(message){
            errormessageText.innerHTML = message;
        }

        setTimeout(() => {
            errorMessage.classList.remove('show');
        }, 3000);

    } else {
        errorMessage.classList.remove('show');
    }
}


export function validateForm(...input){
    const allowed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&* ";

    for (let value of input) {
        for (let char of value) {
            if (!allowed.includes(char)) {
                return false;
            }
        }
    }
    return true;
}