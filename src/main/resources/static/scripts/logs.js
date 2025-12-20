import {redirectToLoginPage} from "./redirect.js";
import {refreshMainPage} from "./dashboard-refresh.js";
import {showMessage} from "./validation.js";

const logs = document.querySelector("#logs-container");
const noLogsFoundImage = document.querySelector("#no-logs-found-image");


// add log to "Today's Logs"
export function addLog(amount, id, time) {
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
            let newValue = parseInt(logAmount.textContent.trim(), 10); // base 10
            e.preventDefault();
            let logId = id;

            // check if update / deletion is successful
            if (await handleLog(newValue, logId)) {
                logAmount.blur();                         // exit editing
                if (Number.isNaN(newValue) || newValue <= 0){
                    row.remove();                 // remove the log if value is 0 or empty

                    // display pic if no logs found
                    if (logs.children.length === 0) {
                        logsFound(false);
                    }
                }
            }
        }
    })
    logsFound(true);
    logs.appendChild(row);
}

async function handleLog(newValue, logId) {
    newValue = Number(newValue);

    // delete log if its zero or empty
    if(Number.isNaN(newValue) || newValue <= 0) return deleteLog(logId);

    // update otherwise
    return updateLog(newValue, logId);
}

async function deleteLog(logId) {
    try {
        const deleteLogResponse = await fetch(`/delete/log?logId=${logId}`, {method: "POST"});
        if (!deleteLogResponse.ok) {
            redirectToLoginPage(deleteLogResponse);
            throw new Error("Server returned an error.");
        }
        showMessage("success", "Log deleted.");
        return true;

    } catch (err) {
        showMessage("error", "Could not delete log. Please try again later.");
        return false;
    }
}

async function updateLog(newValue, logId) {
    try {
        const updateLog = await fetch(`/update/log?amount=${newValue}&id=${logId}`, {method: "POST"});
        if (!updateLog.ok) {
            redirectToLoginPage(updateLog);
            throw new Error("Server returned an error.");
        }
        showMessage("success", "Log edited.");
        return true;

    } catch (err) {
        showMessage("error", "Could not edit log. Please try again later");
        return false;
    }
}


export function logsFound(bool) {
    if (bool === true) {
        noLogsFoundImage.classList.add('hidden');
        logs.classList.remove('hidden');
    } else {
        noLogsFoundImage.classList.remove('hidden');
        logs.classList.add('hidden');
    }
}