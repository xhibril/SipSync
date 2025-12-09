const logs = document.querySelector(".logs");
const noLogsFoundImage = document.querySelector("#noLogsGif");

import {refreshMainPage} from "./display.js";
import {showMessage} from "./validation.js";

// add log to "Today's Logs"
export function addLog(amount, id, time){
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
            let newValue = Number(logAmount.textContent);
            e.preventDefault(); // prevent new line
            let logId = id;

            // check if update / deletion is successful
            if (await updateLog(newValue, logId)) {
                logAmount.blur();                         // exit editing
                if (!newValue) {
                    row.remove();                 // remove the log if value is 0 or empty

                    // display " no logs found picture" if logs is empty
                    if(logs.children.length === 0){
                        logsFound(false);
                    }
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

    if (!newValue) {
        // edit log
        try {
           const deleteResponse =  await fetch(`/delete/log?logId=${logId}`, {method: "POST"});

           if(!deleteResponse.ok){
               throw new Error("Server returned an error.");
           }
               showMessage("success", "Log deleted.");
               return true;

        } catch (err){
            showMessage("error", "Could not delete log. Please try again later.");
            return false;
        }
    } else {
        // update log
        try {
            const updateLog = await fetch(`/update/amount?amount=${newValue}&id=${logId}`, {method: "POST"});
            if(!updateLog.ok){
                throw new Error("Server returned an error.");
            }
                showMessage("success", "Log edited.");
                return true;

        } catch (err){
            showMessage("error", "Could not edit log. Please try again later");
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



