const displayStreak = document.querySelector("#streak");
import {refreshMainPage} from "./dashboard-refresh.js";
import {addLog, logsFound} from "./logs.js";
import {showMessage} from "./notification.js";
import {isBeingRateLimited, redirectToLoginPage} from "./http-responses.js";

refreshMainPage("DAILY");
checkTodayLogsOnLoad();
checkStreakOnLoad();


// check and calculate the streak on start up
export async function checkStreakOnLoad() {
    try {
        const streakResponse = await fetch("/streak/evaluate");
        if(!streakResponse.ok) {
            if(isBeingRateLimited(streakResponse)) return;
            redirectToLoginPage(streakResponse);
           throw new Error("Server returned an error.");
        }
            const streakRes = await streakResponse.json();
            displayStreak.innerHTML = streakRes;

    } catch(err){
        showMessage("error", "Could not load your streak. Please try again later.");
    }
}

// load all of today's logs on start up
export async function checkTodayLogsOnLoad() {
    try {
        const logsResponse = await fetch("/log/today");
        if (!logsResponse.ok){
            redirectToLoginPage(logsResponse);
            if(isBeingRateLimited(logsResponse)) return;
            throw new Error("Server returned an error.");
        }
            const logsRes = await logsResponse.json();
            if (logsRes.length > 0) {
                logsRes.forEach(log => {
                    addLog(log.amount, log.id, log.time);
                });
                logsFound(true);
            } else {
                logsFound(false);
            }
    } catch (err){
        showMessage("error", "Could not load your daily logs. Please try again later.");
    }
}