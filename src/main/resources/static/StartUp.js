const displayStreak = document.querySelector("#displayStreak");

import {refreshMainPageContent} from "./Display.js";
import { addLog, logsFound} from "./Logs.js";

refreshMainPageContent("DAILY");
checkStreakOnStartUp();
checkTodayLogs();





async function checkStreakOnStartUp(){
   const streakRes = await fetch("/check/streak").then(r=>r.json())
    displayStreak.innerHTML = streakRes;
}



export async function checkTodayLogs() {
    const logsRes = await fetch("/logs/today").then(r => r.json())

    if(logsRes.length > 0){
        logsRes.forEach(log => {
            addLog(log.amount, log.id);
        });

        logsFound(true);

    } else {
        logsFound(false);
    }

}









