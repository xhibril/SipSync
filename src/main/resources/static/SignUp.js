const continueBtn = document.querySelector("#continueBtn")
const emailInput = document.querySelector("#emailSignUp");
const passInput = document.querySelector("#passwordSignUp");


let email, password = "";
continueBtn.addEventListener("click", (event) =>{

        event.preventDefault();
    email = emailInput.value;
    password = passInput.value;

        console.log(email)
        console.log(password);
       addUser(email, password);


}
)







async function addUser(email, password){

    try {

        const response = await fetch(`/Signup?email=${email}&password=${password}`, {method: "POST"});

        console.log("Success")
    } catch(err){
        console.log(err);

    }





}