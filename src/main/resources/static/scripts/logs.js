import {isBeingRateLimited, redirectToLoginPage} from "./http-responses.js";
import {showMessage} from "./notification.js";
import {refreshMainPage} from "./dashboard-refresh.js";

const logs = document.querySelector("#logs-container");
const noLogsFoundImage = document.querySelector("#no-logs-found-image");

// add log to "Today's Logs"
export function addLog(amount, id, time) {
    const row = document.createElement("div");
    row.className = "row";
    row.dataset.logId = id;

    row.innerHTML = `
        <img src="/images/clock.png">
        <p class="timeLogs"></p>
        <p class="logAmount" contenteditable="true"></p>
        <p>mL</p>
    `;

    const logAmount = row.querySelector(".logAmount");
    const timeLogs = row.querySelector(".timeLogs");

    logAmount.textContent = amount;
    timeLogs.textContent = time;

    logAmount.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            logAmount.blur();

            const newValue = parseInt(logAmount.textContent.trim(), 10);
            onLogEdit(row, id, newValue);
        }
    });

    logsFound(true);
    logs.prepend(row);
}

export function removeLog(id){
    const row = document.querySelector(`.row[data-log-id="${id}"]`);

    if (!row) return;
    row.remove();

    if (logs.children.length === 0) {
        logsFound(false);
    }
}


async function onLogEdit(row, logId, newValue) {
    // delete
    if (Number.isNaN(newValue) || newValue <= 0) {
        row.remove();

        if (logs.children.length === 0) {
            logsFound(false);
        }

        await deleteLog(logId); return;
    }

    // update
    await updateLog(newValue, logId);
}


async function deleteLog(logId) {
    try {
        const deleteLogResponse = await fetch(`/log/delete?logId=${logId}`, {method: "POST"});
        if (!deleteLogResponse.ok) {
            redirectToLoginPage(deleteLogResponse);
            if(isBeingRateLimited(deleteLogResponse)) return;
            throw new Error();
        }
            await refreshMainPage("DAILY");

        return true;
    } catch (err) {
        showMessage("error", "Could not delete log. Please try again later.");
        return false;
    }
}

async function updateLog(newValue, logId) {
    try {
        const updateLog = await fetch(`/log/update?amount=${newValue}&id=${logId}`, {method: "POST"});
        if (!updateLog.ok) {
            redirectToLoginPage(updateLog);
            if(isBeingRateLimited(updateLog)) return;
            throw new Error();
        }
        await refreshMainPage("DAILY");
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