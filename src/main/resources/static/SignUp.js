const continueBtn = document.querySelector("#signUpContinueBtn")
const emailInput = document.querySelector("#emailSignUp");
const passwordInput = document.querySelector("#passwordSignUp");
const inputs = document.querySelector(".signUpInputs");


let email = "", password = "";
continueBtn.addEventListener("click", (e) => {
        e.preventDefault();
        email = emailInput.value;
        password = passwordInput.value;

        console.log(email)
        console.log(password);
        addUser(email, password);
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


async function addUser(email, password) {
    try {
        const response = await fetch(`/Signup?email=${email}&password=${password}`, {method: "POST"});

        // nav to homepage
        if (response.ok) {
            window.location.href = "/Home";
        }
        // send user email to verify
        const data = await response.json();
        fetch(`/SendVerificationEmail?email=${data.email}&token=${data.token}`, {method: "POST"});

    } catch (err) {
        console.log(err);
    }
}