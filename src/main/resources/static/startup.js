const displayStreak = document.querySelector("#displayStreak");

import {refreshMainPage} from "./display.js";
import {addLog, logsFound} from "./logs.js";
import {displayErrorMessage} from "./validation.js";

refreshMainPage("DAILY");
checkTodayLogsOnLoad();
checkStreakOnLoad();


// hide error message on load
displayErrorMessage(false);

async function checkStreakOnLoad() {
    const streakRes = await fetch("/check/streak").then(r => r.json())
    displayStreak.innerHTML = streakRes;
}

export async function checkTodayLogsOnLoad() {
    const logsRes = await fetch("/logs/today").then(r => r.json())

    if (logsRes.length > 0) {
        logsRes.forEach(log => {
            addLog(log.amount, log.id, log.time);
        });
        logsFound(true);
    } else {
        logsFound(false);
    }

}









