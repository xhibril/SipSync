const logs = document.querySelector(".logs");
const noLogsFoundImage = document.querySelector("#noLogsGif");
import {refreshMainPage} from "./display.js";
import {displayErrorMessage} from "./validation.js";

let message;

// add log to "Today's Logs"
export function addLog(amount, id, time){
    console.log("LOG ID :" + id);
    const row = document.createElement("div");
    row.className = "row";

    row.innerHTML = `
        <img src="/images/clock.png">
        <p class="timeLogs" contenteditable="false"></p>
        <p class="logAmount" contenteditable="true"></p>
        <p>mL</p>`;

    const logAmount = row.querySelector(".logAmount");
    const timeLogs = row.querySelector(".timeLogs");
    logAmount.textContent = amount;
    logAmount.contentEditable = true;

    timeLogs.textContent = time;

    logAmount.addEventListener("keydown", async (e) => {
        if (e.key === "Enter") {
            let newValue = logAmount.textContent;
            e.preventDefault(); // prevent new line
            let logId = id;

            // check if update / deletion is successful
            if (await updateLog(newValue, logId)) {
                logAmount.blur();                         // exit editing
                if (newValue === 0 || !newValue) {
                    row.remove();                 // remove the log if value is 0 or empty
                }
                refreshMainPage("DAILY");         // refresh content
            }
        }
    })
    logsFound(true);
    logs.appendChild(row);
}

async function updateLog(newValue, logId) {
    newValue = Number(newValue);

    if (newValue === 0 || !newValue) {
        try {
            await fetch(`/delete/log?logId=${logId}`, {method: "POST"});
            return true;
        } catch (err){
            message = "Unable to delete the log. Please try again later.";
            displayErrorMessage(true, message);
            return false;
        }

    } else {

        try {
            await fetch(`/update/amount?amount=${newValue}&id=${logId}`, {method: "POST"});
            return true;
        } catch (err){
            message = "Unable to update log. Please try again later.";
            displayErrorMessage(true, message);
            return false;
        }
    }
}


export function logsFound(bool){
    if(bool === true) {
        noLogsFoundImage.classList.add('hidden');
        logs.classList.remove('hidden');
    } else {
        noLogsFoundImage.classList.remove('hidden');
        logs.classList.add('hidden');
    }
}



