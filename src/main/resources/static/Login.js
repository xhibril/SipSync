const emailInput = document.querySelector("#emailLogin");
const passwordInput = document.querySelector("#passwordLogin");
const inputs = document.querySelector(".loginInputs");
const continueBtn = document.querySelector("#loginContinueBtn");


let email = "", password = "";
continueBtn.addEventListener("click", (e) => {
        e.preventDefault();
        email = emailInput.value;
        password = passwordInput.value;

        console.log(email)
        console.log(password);
        loginUser(email, password);
    }
)

inputs.forEach(input =>{
    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            continueBtn.click();
        }
    });


})



async function loginUser(email, password){

    const res = await fetch(`/Login?email=${email}&password=${password}`, {method: "GET"});
    const result = await res.text();

    if(result === "SUCCESS"){
        // got to homepage if details r correct
        window.location.href = "/Home";
    } else {
        console.log("Invalid email or password");
    }
}