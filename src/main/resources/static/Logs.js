console.log("logs");
const logs = document.querySelector(".logs");
const noLogsFound = document.querySelector("#noLogsGif");
console.log(noLogsFound);
console.log(logs);

import {
    refreshMainPageContent
} from "./Display.js";


let logId = 0;
// add log to "Today's Logs"
export function addLog(amount, id){
    const row = document.createElement("div");
    row.className = "row";

    row.innerHTML = `
        <img src="/images/clock.png">
        <p class="timeLogs" contenteditable="false">12:30</p>
        <p class="logAmount" contenteditable="true"></p>
        <p>mL</p>
    `;

    const p = row.querySelector(".logAmount");

    p.textContent = amount;
    p.contentEditable = true;

    console.log(id);
    p.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            let newValue = p.textContent;
            e.preventDefault();  // prevent new line
            p.blur();            // exit editing
            p.dataset.id = id;
            logId = id;

            if(newValue === 0 || !newValue){
                row.remove();



            }

            updateAmount(newValue);

        }
    })
    logsFound(true);
    logs.appendChild(row);
}

async function updateAmount(newValue){
    newValue = Number(newValue);

    if(newValue === 0 || !newValue){

        await fetch(`/delete/log?logId=${logId}`, {method: "POST"})
            .then(e =>{
                console.log("Updated success");
                refreshMainPageContent("DAILY");
            })
    } else {

        await fetch(`/update/amount?amount=${newValue}&id=${logId}`, {method: "POST"})
            .then(e =>{
                console.log("Updated success");
                refreshMainPageContent("DAILY");
            })
            .catch(err =>{
                console.log(err);
            })




    }


}



export function logsFound(bool){
    console.log(bool);
    if(bool === true) {

        noLogsFound.classList.add('hidden');
        logs.classList.remove('hidden');

    } else {
        noLogsFound.classList.remove('hidden');
        logs.classList.add('hidden');

    }
}


