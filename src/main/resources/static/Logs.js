console.log("logs");
const logs = document.querySelector(".logs");
const noLogsFound = document.querySelector("#noLogsGif");
console.log(noLogsFound);
console.log(logs);

// add log to "Today's Logs"
export function addLog(amount){
    logs.innerHTML += `<p>${amount}</p>`;
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