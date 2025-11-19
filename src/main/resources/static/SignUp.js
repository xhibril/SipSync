const continueBtn = document.querySelector("#continueBtn")
const emailInput = document.querySelector("#emailSignUp");
const passInput = document.querySelector("#passwordSignUp");
const input = document.querySelector(".signUpInputs");


let email, password = "";
continueBtn.addEventListener("click", (e) => {

        e.preventDefault();
        email = emailInput.value;
        password = passInput.value;

        console.log(email)
        console.log(password);
        addUser(email, password);
    }
)

input.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
        continueBtn.click();
    }
});


async function addUser(email, password) {

    try {

        const response = await fetch(`/Signup?email=${email}&password=${password}`, {method: "POST"});

        if (response.ok) {
            window.location.href = "/Home";
        }

        const data = await response.json();
        fetch(`/SendVerificationEmail?email=${data.email}&token=${data.token}`, {method: "POST"});



        console.log("Success")
    } catch (err) {
        console.log(err);

    }


}