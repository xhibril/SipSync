import {showMessage} from "./notification.js";

export function redirectToLoginPage(response){
    if(response.status === 401){
        window.location.href = "/login";
    }
}

export function isBeingRateLimited(response){
    if(response.status === 429){
        showMessage("error", "Too many requests. Please try again later.");
        return true;
    }
    return false;
}


