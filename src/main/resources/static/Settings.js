const settingsMenu = document.querySelector(".settingsMenu");
const overlay = document.querySelector(".overlay");
const settingsBtn = document.querySelector(".settingsBtn");
const resetDataBtn = document.querySelector("#resetDataBtn");

import {
    refreshMainPageContent
} from "./Display.js";

settingsBtn.addEventListener("click", ()=>{

    settingsMenu.classList.toggle("active");
})

resetDataBtn.addEventListener("click", ()=>{
deleteUserData();
})

async function deleteUserData(){

    try {
        await fetch(`/reset/data`, {method: "POST"});
        console.log("Data reset completed");
        refreshMainPageContent();
    } catch(err){
        console.log(err);
    }

}