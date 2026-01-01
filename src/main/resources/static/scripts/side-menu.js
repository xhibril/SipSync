import {isBeingRateLimited, redirectToLoginPage} from "./http-responses.js";
import {refreshMainPage} from "./dashboard-refresh.js";
import {showMessage} from "./notification.js";
import {lockBtn, unlockBtn} from "./button-state.js";

const sideMenuBtn = document.querySelector("#side-menu-btn");
const sideMenuContainer = document.querySelector("#side-menu");
const sideMenuContent = document.querySelector("#side-menu-content");

const homeBtn = document.querySelector("#home-btn");
const calculatorBtn = document.querySelector("#calculator-btn");
const feedbackBtn = document.querySelector("#feedback-btn");
const resetDataBtn = document.querySelector("#data-reset");
const logoutBtn = document.querySelector("#logout-btn");




sideMenuBtn.addEventListener("click", ()=>{
    sideMenuContainer.classList.toggle("active");
    sideMenuContent.classList.toggle("active");
    sideMenuBtn.classList.toggle("active");
})

homeBtn.addEventListener("click", ()=>{
    window.location.href = "/home";
})

calculatorBtn.addEventListener("click", ()=>{
    window.location.href = "/calculator";
})

feedbackBtn.addEventListener("click", ()=>{
    window.location.href = "/feedback";
})


resetDataBtn.addEventListener("click", () => {
    deleteUserData();
})

logoutBtn.addEventListener("click", ()=>{
    logout();
})

if (window.innerWidth > 1024) {
    sideMenuBtn.addEventListener("click", () => {
        sideMenuContainer.classList.toggle("active");
    });
}


async function deleteUserData() {
    try {
        const resetDataResponse = await fetch("/data/reset", {method: "POST"});
        if (!resetDataResponse.ok){
            redirectToLoginPage(resetDataResponse);
            if(isBeingRateLimited(resetDataResponse)) return;
            throw new Error();
        }
            showMessage("success", "Data deleted. Refreshing...");
            // fresh reload
            setTimeout(() => {
                window.location.href = window.location.pathname + "?t=" + Date.now();
            }, 2000);
    } catch (err) {
        showMessage("error", "Could not delete user data. Please try again later.");
    }
}



async function logout(){
    lockBtn(logoutBtn, "Logging in...");
    try{
        const logoutResponse = await fetch("/api/logout", {method:"POST"});
        if(!logoutResponse.ok) {
            if(isBeingRateLimited(logoutResponse)) return;
            throw new Error();
        }
        window.location.href = "/login";
    } catch(err){
        showMessage("error", "Could not log you out. Please try again.");
    } finally{
        unlockBtn(logoutBtn);
    }
}


